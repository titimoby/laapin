package com.github.gquintana.laapin.joueur;

import java.util.Objects;

public class Action {
    public final TypeAction type;
    public final Direction direction;

    public Action(TypeAction type, Direction direction) {
        this.type = type;
        this.direction = direction;
    }

    public static class Builder {
        public final TypeAction type;
        public Direction direction;

        public Builder(TypeAction type) {
            this.type = type;
        }

        public Action vers(Direction direction) {
            this.direction = direction;
            return new Action(this.type, direction);
        }

        public Action aGauche() {
            return vers(Direction.GAUCHE);
        }

        public Action aDroite() {
            return vers(Direction.DROITE);
        }

        public Action enHaut() {
            return vers(Direction.HAUT);
        }

        public Action enBas() {
            return vers(Direction.BAS);
        }
    }

    public static Builder avancer() {
        return new Builder(TypeAction.AVANCER);
    }

    public static Builder frapper() {
        return new Builder(TypeAction.FRAPPER);
    }

    public static Builder sauter() {
        return new Builder(TypeAction.SAUTER);
    }

    public static Action seReposer() {
        return new Action(TypeAction.SE_REPOSER, null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Action action = (Action) o;
        return type == action.type &&
                direction == action.direction;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, direction);
    }
}
