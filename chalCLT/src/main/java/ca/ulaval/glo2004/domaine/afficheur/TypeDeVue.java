package ca.ulaval.glo2004.domaine.afficheur;

import ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.base.Vector3D;

public enum TypeDeVue {
    Dessus,
    Facade,
    Arriere,
    Droite,
    Gauche;

    public static Vector3D vueDessus() {
        return new Vector3D(-Math.PI / 2, Math.PI, 0);
    }

    public static Vector3D vueFacade() {
        return new Vector3D(0, Math.PI, 0);
    }

    public static Vector3D vueArriere() {
        return new Vector3D(0, 0, 0);
    }

    public static Vector3D vueDroite() {
        return new Vector3D(0, Math.PI / 2, 0);
    }

    public static Vector3D vueGauche() {
        return new Vector3D(0, -Math.PI / 2, 0);
    }

    public static Vector3D getDirection(TypeDeVue vue) {
        if (vue == TypeDeVue.Dessus) {
            return vueDessus();
        } else if (vue == TypeDeVue.Facade) {
            return vueFacade();
        } else if (vue == TypeDeVue.Arriere) {
            return vueArriere();
        } else if (vue == TypeDeVue.Droite) {
            return vueDroite();
        } else if (vue == TypeDeVue.Gauche) {
            return vueGauche();
        }

        return null;
    }

    public static TypeDeVue getTypeFromDirection(Vector3D direction) {
        if (direction.equals(vueDessus())) {
            return TypeDeVue.Dessus;
        } else if (direction.equals(vueFacade())) {
            return TypeDeVue.Facade;
        } else if (direction.equals(vueArriere())) {
            return TypeDeVue.Arriere;
        } else if (direction.equals(vueDroite())) {
            return TypeDeVue.Droite;
        } else if (direction.equals(vueGauche())) {
            return TypeDeVue.Gauche;
        }

        return null;
    }
}
