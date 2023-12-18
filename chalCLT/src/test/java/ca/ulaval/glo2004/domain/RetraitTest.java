package ca.ulaval.glo2004.domain;

import ca.ulaval.glo2004.domaine.Retrait;
import ca.ulaval.glo2004.domaine.TypeRetrait;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;


public class RetraitTest {

    @Test
    public void testInstanceAcessoire(){
        double[] pos = {4.0, 8.0};
        double[] dim = {3.0, 6.0};
        Retrait retrait = new Retrait(TypeRetrait.Accessoire, pos , dim);

        assertEquals(TypeRetrait.Accessoire, retrait.getType());
        assertEquals(4.0, retrait.getPosition()[0], 0);
        assertEquals(8.0, retrait.getPosition()[1], 0);
        assertEquals(3.0, retrait.getDimension()[0], 0);
        assertEquals(6.0, retrait.getDimension()[1], 0);
    }

    @Test
    public void testInstanceMur(){
        double[] pos = {2.0, 3.0};
        double[] dim = {5.0, 2.0};
        Retrait retrait = new Retrait(TypeRetrait.Rainure, pos , dim);

        assertEquals(TypeRetrait.Rainure, retrait.getType());
        assertEquals(2.0, retrait.getPosition()[0], 0);
        assertEquals(3.0, retrait.getPosition()[1], 0);
        assertEquals(5.0, retrait.getDimension()[0], 0);
        assertEquals(2.0, retrait.getDimension()[1], 0);
    }

    @Test
    public void testsetType(){
        double[] pos = {2.0, 3.0};
        double[] dim = {5.0, 2.0};
        Retrait retrait = new Retrait(TypeRetrait.Rainure, pos , dim);
        retrait.setType(TypeRetrait.Accessoire);
        assertEquals(TypeRetrait.Accessoire, retrait.getType());
    }
    
    // vérifie si les méthodes writeObject et readObject fonctionnent
    @Test
    public void serializableValide() {
        TypeRetrait type = TypeRetrait.Rainure;
        double[] pos = {2.0, 3.0};
        double[] dim = {1.0,2.0};
        
        Retrait r = new Retrait(type, pos, dim);
        Retrait.RetraitDTO rDTO1 = new Retrait.RetraitDTO(r);
        
        try {
            String path = System.getProperty("user.dir") + "\\test.txt";
            File fichier = new File(path);
            FileOutputStream fos = new FileOutputStream(fichier);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(rDTO1);
            oos.flush();
            oos.close();
            
            FileInputStream fis = new FileInputStream(fichier);
            ObjectInputStream ois = new ObjectInputStream(fis);
            Retrait.RetraitDTO rDTO2 = (Retrait.RetraitDTO) ois.readObject();

            ois.close();
            fichier.delete();
            
            assertEquals(rDTO1.type, rDTO2.type);
            assertEquals(rDTO1.position[0], rDTO2.position[0], 0);
            assertEquals(rDTO1.position[1], rDTO2.position[1], 0);
            assertEquals(rDTO1.dimensions[0], rDTO2.dimensions[0], 0);
            assertEquals(rDTO1.dimensions[1], rDTO2.dimensions[1], 0);
        }
        
        catch (IOException | ClassNotFoundException e) {
            // jsp
            e.printStackTrace();
            assertTrue(false);
        }
    }
}
