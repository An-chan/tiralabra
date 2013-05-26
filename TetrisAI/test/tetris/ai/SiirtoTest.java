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
        siirto1 = new Siirto(3, 5, 0, 0, 1);
        siirto2 = new Siirto(4, 3, 0, 0, 2);
        siirtoRivi = new Siirto(6, 3, 1, 4, 0);
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
     public void compareToTempTest(){
         assertEquals(1, siirtoRivi.compareTo(siirto1));
         assertEquals(-1, siirto2.compareTo(siirto1));
         assertEquals(0, siirto1.compareTo(siirto1));
     }
     
     // en ole testannut compareTo:n toimintaa sen enempää toistaiseksi koska
     // en tiedä toteutanko ohjelman lopulta sillä vai jollakin toisella vertailutavalla
}
