package ca.ulaval.glo2004.gui.components;

import java.util.UUID;

import javax.swing.tree.DefaultMutableTreeNode;

import ca.ulaval.glo2004.domaine.TypeAccessoire;

public class AccessoireTreeNode extends DefaultMutableTreeNode {

    private String nom;

    private TypeAccessoire typeAccessoire;

    private UUID uuid;

    public AccessoireTreeNode(String nom,TypeAccessoire typeAcc,UUID uuid){
        this.nom = nom;
        this.typeAccessoire = typeAcc;
        this.uuid = uuid;
    }

    public String getNom() {
        return nom;
    }

    public TypeAccessoire getTypeAccessoire(){
        return typeAccessoire;
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

    @Override
        public boolean equals(Object o){
            if(o == this){
                return true;
            }
            if(!(o instanceof AccessoireTreeNode)){
                return false;
            }
            return true;
        } 
    
}
