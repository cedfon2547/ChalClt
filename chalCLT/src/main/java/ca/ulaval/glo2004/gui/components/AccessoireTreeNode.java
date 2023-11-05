package ca.ulaval.glo2004.gui.components;

import java.util.UUID;

import javax.swing.tree.DefaultMutableTreeNode;

public class AccessoireTreeNode extends DefaultMutableTreeNode {

    private String nom;

    private UUID uuid;

    public AccessoireTreeNode(String nom,UUID uuid){
        this.nom = nom;
        this.uuid = uuid;
    }

    public String getNom() {
        return nom;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }
    
}
