package ca.ulaval.glo2004.domain.utils;

import ca.ulaval.glo2004.domaine.PreferencesUtilisateur;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PreferencesUtilisateurTest {
    
    @Test
    public void ConstructeurPrefUtil() {
        PreferencesUtilisateur test = new PreferencesUtilisateur();
        
        assertTrue(!(test.getAfficherGrille() || test.getAfficherVoisinSelection()));
        assertEquals(java.awt.Color.BLACK, test.getBackgroundColor());
        assertEquals(java.awt.Color.GRAY, test.getGridColor());
        assertEquals(50, test.getGridSpacing(), 0);
    }
    
    @Test
    public void ConstructeurAltPrefUtil() {
        PreferencesUtilisateur test = new PreferencesUtilisateur(true, true);
        
        assertTrue(test.getAfficherGrille() && test.getAfficherVoisinSelection());
        assertEquals(java.awt.Color.BLACK, test.getBackgroundColor());
        assertEquals(java.awt.Color.GRAY, test.getGridColor());
        assertEquals(50, test.getGridSpacing(), 0);
    }
    
    @Test
    public void SetAfficherGrilleTrue() {
        PreferencesUtilisateur test = new PreferencesUtilisateur();
        test.setAfficherGrille(true);
        
        assertTrue(test.getAfficherGrille());
    }
    
    @Test
    public void SetAfficherVoisinTrue() {
        PreferencesUtilisateur test = new PreferencesUtilisateur();
        test.setAfficherVoisinSelection(true);
        
        assertTrue(test.getAfficherVoisinSelection());
    }
    
    @Test
    public void SetAfficherGrilleFalse() {
        PreferencesUtilisateur test = new PreferencesUtilisateur(true, true);
        test.setAfficherGrille(false);
        
        assertTrue(!(test.getAfficherGrille()));
    }
    
    @Test
    public void SetAfficherVoisinFalse() {
        PreferencesUtilisateur test = new PreferencesUtilisateur(true, true);
        test.setAfficherVoisinSelection(false);
        
        assertTrue(!(test.getAfficherVoisinSelection()));
    }
    
    @Test
    public void SetBackgroundColor() {
        PreferencesUtilisateur test = new PreferencesUtilisateur();
        test.setBackgroundColor(java.awt.Color.BLUE);
        
        assertEquals(java.awt.Color.BLUE, test.getBackgroundColor());
    }
    
    @Test
    public void SetGridColor() {
        PreferencesUtilisateur test = new PreferencesUtilisateur();
        test.setGridColor(java.awt.Color.RED);
        
        assertEquals(java.awt.Color.RED, test.getGridColor());
    }
    
    @Test
    public void SetGridSpace() {
        PreferencesUtilisateur test = new PreferencesUtilisateur();
        test.setGridSpacing(80);
        
        assertEquals(80, test.getGridSpacing(), 0);
    }
}
