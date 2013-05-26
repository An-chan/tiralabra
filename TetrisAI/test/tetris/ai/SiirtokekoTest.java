
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
         assertEquals(siirto1, keko.suurin());
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
         assertEquals(siirto1, keko.suurin());
     }
     
     @Test
     public void kekoTyhjeneeOikeinTest(){
         keko.lisaa(siirto2);
         keko.lisaa(siirto1);
         keko.tyhjenna();
         assertEquals(0, keko.koko());
     }
     
     @Test
     public void tyhjaKekoPalauttaaNullTest(){
         keko.lisaa(siirto1);
         keko.tyhjenna();
         assertEquals(null, keko.suurin());
     }
     
     @Test
     public void suurempiKekoPalauttaaSurimmanTest(){
         Siirto siirt1 = new Siirto(0,4,0,5,1); // arvo 4
         Siirto siirt2 = new Siirto(1,5,0,0,1); // arvo 4
         Siirto siirt3 = new Siirto(1,2,1,2,3); // arvo 11
         Siirto siirt4 = new Siirto(6,5,0,8,2); // arvo -1
         Siirto siirt5 = new Siirto(4,1,1,6,2); // arvo 7
         Siirto siirt6 = new Siirto(9,3,1,7,0); // arvo 4
         Siirto siirt7 = new Siirto(12,2,0,3,0); // arvo -10
         keko.lisaa(siirt1);
         keko.lisaa(siirt2);
         keko.lisaa(siirt3);
         keko.lisaa(siirt4);
         keko.lisaa(siirt5);
         keko.lisaa(siirt6);
         keko.lisaa(siirt7);
         assertEquals(siirt3, keko.suurin());
     }
}
