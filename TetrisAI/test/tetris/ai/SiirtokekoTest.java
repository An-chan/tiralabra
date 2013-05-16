
package tetris.ai;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import tetris.ai.utility.*;

public class SiirtokekoTest {
    
    public SiirtokekoTest() {
    }
    Siirtokeko keko;
    Siirto siirto1;
    Siirto siirto2;
    
    @Before
    public void setUp() {
        keko = new Siirtokeko();
        siirto1 = new Siirto(2, 3, 0, 1, 0);
        siirto2 = new Siirto(3, 2, 0, 1, 0);
    }
    
    @After
    public void tearDown() {
    }

     @Test
     public void kekoAluksiTyhjaTest(){
         assertEquals(0, keko.koko());
     }
     
     @Test
     public void kekoonLisaysYksiTest(){
         keko.lisaa(siirto1);
         assertEquals(1, keko.koko());
     }
     
     @Test
     public void keostaPoistoYksiTest(){
         keko.lisaa(siirto1);
         assertEquals(siirto1, keko.pienin());
     }
     
     @Test
     public void kekoonLisaysKaksiTest(){
         keko.lisaa(siirto2);
         keko.lisaa(siirto1);
         assertEquals(2, keko.koko());
     }
     
     @Test
     public void keostaPoistoKaksiTest(){
         keko.lisaa(siirto2);
         keko.lisaa(siirto1);
         assertEquals(siirto1, keko.pienin());
     }
     
     @Test
     public void kekoTyhjeneeOikeinTest(){
         keko.lisaa(siirto2);
         keko.lisaa(siirto1);
         keko.tyhjenna();
         assertEquals(0, keko.koko());
     }
}
