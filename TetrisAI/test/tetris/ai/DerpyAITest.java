/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tetris.ai;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import tetris.ai.utility.*;
import tetris.domain.Muodostelma;
import tetris.domain.Muoto;
import tetris.peli.Tetris;

/**
 *
 * @author Anni
 */
public class DerpyAITest {
    
    public DerpyAITest() {
    }
    DerpyAI ai;
    Tetris peli;
    
    @Before
    public void setUp() {
        peli = new Tetris();
        ai = new DerpyAI(peli);
    }
    
    @After
    public void tearDown() {
    }
    
    @Test
    public void aiPaivittyyOikeinTest(){
        assertEquals(peli.getPutoava(), ai.getMuodostelma());
    }
    
    @Test
    public void laskeSivutYksinkertainenTest(){
        Muodostelma muod = new Muodostelma(Muoto.L, peli);
        muod.putoa();
        muod.kierra();
        muod.kierra();
        ai.siirraMuodostelma(muod, 9);
        ai.pudotaMuodostelma(muod);
        int sivut = ai.laskeSivut(muod);
        assertEquals(5, sivut);
    }
    
    @Test
    public void laskeSivutIMuotoTest(){
        Muodostelma muod = new Muodostelma(Muoto.I, peli);
        muod.putoa();
        muod.putoa();
        muod.kierra();
        ai.siirraMuodostelma(muod, 8);
        ai.pudotaMuodostelma(muod);
        ai.lisaaKentalle(muod.getPalikat());
        Muodostelma uusi = new Muodostelma(Muoto.I, peli);
        uusi.putoa();
        uusi.putoa();
        uusi.kierra();
        ai.siirraMuodostelma(uusi, 9);
        ai.pudotaMuodostelma(uusi);
        int sivut = ai.laskeSivut(uusi);
        assertEquals(9, sivut);
    }
    
    @Test
    public void laskeSivutMonimutkainenTest(){
        
    }
    
    @Test
    public void laskeRivitTest(){
        
    }
    
    @Test
    public void teeSiirtoTest(){
        
    }
    
    @Test
    public void siirraMuodostelmaNormaaliSyoteTest(){
        
    }
    
    @Test
    public void siirraMuodostelmaEiSiirraMahdottomiinTest(){
        
    }
    
    @Test
    public void pudotaMuodostelmaToimiiOikeinTest(){
        
    }
}
