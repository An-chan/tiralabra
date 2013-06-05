/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tetris.ai;

import tetris.ai.utility.Siirto;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Anni
 */
public class SiirtoTest {
    
    public SiirtoTest() {
    }
    Siirto siirto1;
    Siirto siirto2;
    Siirto siirtoRivi;
    
    @Before
    public void setUp() {
        siirto1 = new Siirto(17, 5, 0, 0, 1); // arvo 2
        siirto2 = new Siirto(16, 3, 0, 0, 2); // arvo -1
        siirtoRivi = new Siirto(14, 3, 1, 4, 0); // arvo 7
    }
    
    @After
    public void tearDown() {
    }

     @Test
     public void arvoLasketaanOikeinTest(){
         assertEquals(2, siirto1.getArvo());
         assertEquals(-1, siirto2.getArvo());
     }
     
     @Test
     public void arvoLasketaanOikeinKunRivejaPoistuuTest(){
         assertEquals(7, siirtoRivi.getArvo());
     }
     
     @Test
     public void suurempiKuinTest(){
         assertEquals(true, siirto1.suurempiKuin(siirto2));
         assertEquals(false, siirto2.suurempiKuin(siirtoRivi));
     }
     
     @Test
     public void suurempiKuinArvoSamaTest(){
         Siirto uusi = new Siirto(9, 2, 0, 0, 0);
         assertEquals(true, siirtoRivi.suurempiKuin(uusi));
     }

}
