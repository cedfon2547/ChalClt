package ca.ulaval.glo2004.domain.utils;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ca.ulaval.glo2004.domaine.utils.ImperialDimension;

/* TODO: À continuer... */
public class ImperialDimensionTest {
    @Test
    public void testInstance() {
        ImperialDimension dim = new ImperialDimension(5, 8, 3, 8);
        
        assertEquals(5, dim.getPieds());
        assertEquals(8, dim.getPouces());
        assertEquals(3, dim.getNumerateur());
        assertEquals(8, dim.getDenominateur());
    }

    @Test
    public void testFormat() {
        // 5' 8" 3/8
        ImperialDimension dim = new ImperialDimension(5, 8, 3, 8);
        ImperialDimension formattedDim = ImperialDimension.format(dim);
        
        assertEquals(5, formattedDim.getPieds());
        assertEquals(8, formattedDim.getPouces());
        assertEquals(3, formattedDim.getNumerateur());
        assertEquals(8, formattedDim.getDenominateur());
    }

    @Test
    public void testAddition() {
        // 3/8 + 3/8 = 6/8 = 3/4
        // 8" + 8" = 16" = 1' 4"
        // 5' + 5' = 10' + 1' = 11'
        // 11' 4" 3/4
        
        ImperialDimension dim1 = new ImperialDimension(5, 8, 3, 8);
        ImperialDimension dim2 = new ImperialDimension(5, 8, 3, 8);
        ImperialDimension dim3 = dim1.addition(dim2);
        
        assertEquals(11, dim3.getPieds());
        assertEquals(4, dim3.getPouces());
        assertEquals(3, dim3.getNumerateur());
        assertEquals(4, dim3.getDenominateur());

        // 5/16 + 3/8 = 5/16 + 6/16 = 11/16
        // 7" + 6" = 13" = 1' 1"
        // 3' + 3' = 6' + 1' = 7'
        // 7' 1" 11/16
        
        ImperialDimension dim4 = new ImperialDimension(3, 7, 5, 16);
        ImperialDimension dim5 = new ImperialDimension(3, 6, 3, 8);
        ImperialDimension dim6 = dim4.addition(dim5);

        assertEquals(7, dim6.getPieds());
        assertEquals(1, dim6.getPouces());
        assertEquals(11, dim6.getNumerateur());
        assertEquals(16, dim6.getDenominateur());

    }

    @Test
    public void testSoustraction() {
        // 3/8 - 3/8 = 0/8 = 0/1
        // 8" - 8" = 0" = 0' 0"
        // 5' - 5' = 0' + 0' = 0'
        // 0' 0" 0/1
        
        ImperialDimension dim1 = new ImperialDimension(5, 8, 3, 8);
        ImperialDimension dim2 = new ImperialDimension(5, 8, 3, 8);
        ImperialDimension dim3 = dim1.soustraction(dim2);
        
        assertEquals(0, dim3.getPieds());
        assertEquals(0, dim3.getPouces());
        assertEquals(0, dim3.getNumerateur());
        assertEquals(1, dim3.getDenominateur());

        // 5/16 - 3/16 = 2/16 = 1/8
        // 7" - 6" = 1" = 0' 1"
        // 3' - 3' = 0' + 0' = 0'
        // 0' 1" 1/8

        ImperialDimension dim4 = new ImperialDimension(3, 7, 5, 16);
        ImperialDimension dim5 = new ImperialDimension(3, 6, 3, 16);
        ImperialDimension dim6 = dim4.soustraction(dim5);

        assertEquals(0, dim6.getPieds());
        assertEquals(1, dim6.getPouces());
        assertEquals(1, dim6.getNumerateur());
        assertEquals(8, dim6.getDenominateur());
    }
}
