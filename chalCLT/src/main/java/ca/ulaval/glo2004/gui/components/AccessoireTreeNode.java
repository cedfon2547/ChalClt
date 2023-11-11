package ca.ulaval.glo2004.gui.components;

import java.util.UUID;

import javax.swing.tree.DefaultMutableTreeNode;

import ca.ulaval.glo2004.domaine.Accessoire;
import ca.ulaval.glo2004.domaine.TypeAccessoire;

public class AccessoireTreeNode extends DefaultMutableTreeNode {
    private Accessoire.AccessoireDTO accessoireDTO;

    public AccessoireTreeNode(Accessoire.AccessoireDTO accessoireDTO) {
        this.accessoireDTO = accessoireDTO;
    }

    public String getNom() {
        return accessoireDTO.accessoireNom;
    }

    public TypeAccessoire getTypeAccessoire() {
        return accessoireDTO.accessoireType;
    }

    public UUID getUuid() {
        return accessoireDTO.accessoireId;
    }

    public Accessoire.AccessoireDTO getAccessoireDTO() {
        return this.accessoireDTO;
    }

    public void setAccessoireDTO(Accessoire.AccessoireDTO accessoireDTO) {
        this.accessoireDTO = accessoireDTO;
    }

    public boolean equals(AccessoireTreeNode node) {
        return this.getAccessoireDTO().accessoireId.equals(node.getAccessoireDTO().accessoireId);
    }
}
