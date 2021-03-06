package com.github.gquintana.laapin;

import com.github.gquintana.laapin.affichage.GrillePanel;
import com.github.gquintana.laapin.affichage.StatsPanel;
import com.github.gquintana.laapin.moteur.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;

public class Main extends Application {
    private Configuration configuration;
    private Moteur moteur;
    private GrillePanel grillePanel;
    private Grille grille;
    private StatsPanel statsPanel;


    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Laapin");
        grillePanel = new GrillePanel(grille, configuration);
        statsPanel = new StatsPanel();
        HBox root = new HBox(statsPanel, grillePanel);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.getIcons().add(grillePanel.imageLapin);
        primaryStage.setOnCloseRequest(event -> {
            if (moteur != null) {
                moteur.arreter();
            }
        });
        primaryStage.show();
        primaryStage.toFront();
    }

    @Override
    public void init() throws Exception {
        super.init();
        configuration = new Configuration();
        configuration.load();
        if (!getParameters().getRaw().isEmpty()) {
            String configFileName = getParameters().getRaw().get(0);
            try(InputStream inputStream = Resources.openStream(configFileName)) {
                configuration.load(inputStream);
            }
        }
        moteur = new Moteur(configuration, new MoteurListener() {
            @Override
            public void onDemarrer(Grille grille) {
                Main.this.grille = grille;
                Platform.runLater(() -> {
                    mettreAJourStatsPanel(grille, null);
                });
            }

            @Override
            public void onAgir(Grille grille, Lapin lapin, ResultatAction resultatAction) {
                Platform.runLater(() -> {
                    mettreAJourStatsPanel(grille, resultatAction);
                    mettreAJourGrillePanel();
                });
            }

            @Override
            public void onArreter(Grille grille) {
                Platform.runLater(() -> {
                    mettreAJourStatsPanel(grille, null);
                    mettreAJourGrillePanel();
                });
            }
        });
        moteur.demarrer();
    }

    private void mettreAJourGrillePanel() {
        if (grillePanel != null) {
            grillePanel.repaint();
        }
    }

    private void mettreAJourStatsPanel(Grille grille, ResultatAction resultatAction) {
        if (statsPanel != null) {
            statsPanel.setLapinsList(grille.lapins());
            if (resultatAction != null) {
                statsPanel.addAction(resultatAction);
            }
        }
    }

    @Override
    public void stop() throws Exception {
        moteur.arreter();
        super.stop();
    }

    public static void main(String[] args) throws IOException {
        launch(Main.class, args);
    }
}