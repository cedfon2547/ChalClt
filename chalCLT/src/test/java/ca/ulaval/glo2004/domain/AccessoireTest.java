package ca.ulaval.glo2004.domain;

import ca.ulaval.glo2004.domaine.Accessoire;
import ca.ulaval.glo2004.domaine.ChalCLTProjet;
import ca.ulaval.glo2004.domaine.TypeAccessoire;
import ca.ulaval.glo2004.domaine.TypeMur;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.*;

public class AccessoireTest {

    @Test
    public void InstanceTestFenetre(){
        TypeAccessoire typeAccessoire = TypeAccessoire.Fenetre;
        TypeMur typeMur = TypeMur.Droit;
        double[] pos = {2.0, 3.0};
        double[] dim = {1.0,2.0};

        Accessoire accessoire = new Accessoire(typeAccessoire, typeMur,pos, dim, true);

        assertEquals(TypeAccessoire.Fenetre, accessoire.getAccessoireType());
        assertEquals(TypeMur.Droit, accessoire.getTypeMur());
        assertEquals(2, accessoire.getPosition()[0], 0);
        assertEquals(3, accessoire.getPosition()[1], 0);
        assertEquals(1, accessoire.getDimension()[0], 0);
        assertEquals(2, accessoire.getDimension()[1], 0);
    }

    @Test
    public void InstanceTestPorte(){
        TypeAccessoire typeAccessoire = TypeAccessoire.Porte;
        TypeMur typeMur = TypeMur.Droit;
        double[] pos = {2.0, 3.0};
        double[] dim = {1.0,2.0};

        Accessoire accessoire = new Accessoire(typeAccessoire, typeMur,pos, dim, true);

        assertEquals(TypeAccessoire.Porte, accessoire.getAccessoireType());
        assertEquals(TypeMur.Droit, accessoire.getTypeMur());
        assertEquals(2, accessoire.getPosition()[0], 0);
        assertEquals(3, accessoire.getPosition()[1], 0);
        assertEquals(1, accessoire.getDimension()[0], 0);
        assertEquals(2, accessoire.getDimension()[1], 0);
    }

    // methode pour eviter la redondance du code
    public Accessoire setTest() {
        TypeAccessoire typeAccessoire = TypeAccessoire.Porte;
        TypeMur typeMur = TypeMur.Droit;
        double[] pos = {4.0, 3.0};
        double[] dim = {7.0, 2.0};
        return new Accessoire(typeAccessoire, typeMur, pos, dim, true);
    }


    @Test
    public void setTypeTest(){
        Accessoire accessoire = setTest();
        accessoire.setAccessoireType(TypeAccessoire.Fenetre);
        assertEquals(TypeAccessoire.Fenetre, accessoire.getAccessoireType());
    }

    @Test
    public void setIdTest(){
        Accessoire accessoire = setTest();
        UUID uuid = UUID.randomUUID();
        accessoire.setAccessoireId(uuid);
        assertEquals(uuid, accessoire.getAccessoireId());
    }
    @Test
    public void setNomTest(){
        Accessoire accessoire = setTest();
        String nom = "NomRandom101";
        accessoire.setAccessoireNom(nom);
        assertEquals(nom, accessoire.getAccessoireNom());
    }

    @Test
    public void setValideTest(){
        Accessoire accessoire = setTest();
        accessoire.setValide(false);
        assertFalse(accessoire.getValide());
    }


    @Test
    public void getMarginWithRectTest(){
        TypeAccessoire typeAccessoire = TypeAccessoire.Porte;
        TypeMur typeMur = TypeMur.Droit;
        double[] pos = {4.0, 3.0};
        double[] dim = {7.0,2.0};
        Accessoire accessoire = new Accessoire(typeAccessoire, typeMur,pos, dim, true);
        double[] pos2 = {5.0, 2.0};
        double[] dim2 = {3.0, 4.0};
        double[] result = accessoire.getMarginWithRect(pos2, dim2);

        assertEquals(1.0, result[0], 0);
        assertEquals(1.0, result[1], 0);
        assertEquals(3.0, result[2], 0);
        assertEquals(1.0, result[3], 0);
    }

    @Test
    public void constructeurAccessoireDTOAvecDTO() {
        TypeAccessoire typeAccessoire = TypeAccessoire.Fenetre;
        TypeMur typeMur = TypeMur.Droit;
        double[] pos = {2.0, 3.0};
        double[] dim = {1.0,2.0};
        
        Accessoire f = new Accessoire(typeAccessoire, typeMur,pos, dim, true);
        Accessoire.AccessoireDTO fDTO1 = new Accessoire.AccessoireDTO(f);
        Accessoire.AccessoireDTO fDTO2 = new Accessoire.AccessoireDTO(fDTO1);
        
        assertEquals(f.getAccessoireType(), fDTO2.accessoireType);
        assertEquals(f.getTypeMur(), fDTO2.typeMur);
        assertEquals(f.getPosition()[0], fDTO2.position[0], 0);
        assertEquals(f.getPosition()[1], fDTO2.position[1], 0);
        assertEquals(f.getDimension()[0], fDTO2.dimensions[0], 0);
        assertEquals(f.getDimension()[1], fDTO2.dimensions[1], 0);
    }
    
    // vérifie si les méthodes writeObject et readObject fonctionnent
    @Test
    public void serializableValide() {
        TypeAccessoire typeAccessoire = TypeAccessoire.Fenetre;
        TypeMur typeMur = TypeMur.Droit;
        double[] pos = {2.0, 3.0};
        double[] dim = {1.0,2.0};
        
        Accessoire f = new Accessoire(typeAccessoire, typeMur,pos, dim, true);
        Accessoire.AccessoireDTO fDTO1 = new Accessoire.AccessoireDTO(f);
        
        try {
            String path = System.getProperty("user.dir") + "\\test.txt";
            File fichier = new File(path);
            FileOutputStream fos = new FileOutputStream(fichier);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(fDTO1);
            oos.flush();
            oos.close();
            
            FileInputStream fis = new FileInputStream(fichier);
            ObjectInputStream ois = new ObjectInputStream(fis);
            Accessoire.AccessoireDTO fDTO2 = (Accessoire.AccessoireDTO) ois.readObject();

            ois.close();
            fichier.delete();
            
            assertEquals(fDTO1.accessoireType, fDTO2.accessoireType);
            assertEquals(fDTO1.typeMur, fDTO2.typeMur);
            assertEquals(fDTO1.accessoireId, fDTO2.accessoireId);
            assertEquals(fDTO1.valide, fDTO2.valide);
            assertEquals(fDTO1.accessoireNom, fDTO2.accessoireNom);
        }
        
        catch (IOException | ClassNotFoundException e) {
            // jsp
            e.printStackTrace();
        }
    }
}
