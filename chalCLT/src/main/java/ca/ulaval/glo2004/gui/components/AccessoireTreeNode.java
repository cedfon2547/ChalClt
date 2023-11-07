package ca.ulaval.glo2004.gui.components;

import java.util.UUID;

import javax.swing.tree.DefaultMutableTreeNode;

import ca.ulaval.glo2004.domaine.Accessoire;
import ca.ulaval.glo2004.domaine.TypeAccessoire;

public class AccessoireTreeNode extends DefaultMutableTreeNode {
    private String nom;
    private TypeAccessoire typeAccessoire;
    private UUID uuid;
    private Accessoire.AccessoireDTO accessoireDTO;

    public AccessoireTreeNode(Accessoire.AccessoireDTO accessoireDTO) {
        this.nom = accessoireDTO.accessoireNom;
        this.typeAccessoire = accessoireDTO.accessoireType;
        this.uuid = accessoireDTO.accessoireId;
        this.accessoireDTO = accessoireDTO;
    }

    public String getNom() {
        return nom;
    }

    public TypeAccessoire getTypeAccessoire() {
        return typeAccessoire;
    }

    public UUID getUuid() {
        return uuid;
    }

    public Accessoire.AccessoireDTO getAccessoireDTO() {
        return this.accessoireDTO;
    }

    public void setAccessoireDTO(Accessoire.AccessoireDTO accessoireDTO) {
        this.accessoireDTO = accessoireDTO;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public boolean equals(AccessoireTreeNode node) {
        return this.getAccessoireDTO().accessoireId.equals(node.getAccessoireDTO().accessoireId);
    }
}
