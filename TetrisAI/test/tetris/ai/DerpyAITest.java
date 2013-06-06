/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tetris.ai;

import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import tetris.ai.utility.*;
import tetris.domain.*;
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
        List<Palikka> palikat = new ArrayList<Palikka>();
        for (int i = 1; i < 10; i++){
            palikat.add(new Palikka(i, 19));
        }
        ai.lisaaKentalle(palikat);
        Muodostelma muod = new Muodostelma(Muoto.L, peli);
        ai.siirraMuodostelma(muod, 0);
        ai.pudotaMuodostelma(muod);
        int rivit = ai.laskeRivit(muod);
        assertEquals(1, rivit);
    }
    
    @Test
    public void siirraMuodostelmaNormaaliSyoteTest(){
        Muodostelma muod = new Muodostelma(Muoto.T, peli);
        ai.siirraMuodostelma(muod, 7);
        assertEquals(7, muod.getPalikat().get(1).getX());
        Muodostelma toinen = new Muodostelma(Muoto.nelio, peli);
        ai.siirraMuodostelma(toinen, 2);
        assertEquals(2, toinen.getPalikat().get(1).getX());
    }
    
    @Test
    public void siirraMuodostelmaEiSiirraMahdottomiinTest(){
        Muodostelma muod = new Muodostelma(Muoto.I, peli);
        ai.siirraMuodostelma(muod, 9);
        assertEquals(7, muod.getPalikat().get(1).getX());
        Muodostelma toinen = new Muodostelma(Muoto.peiliL, peli);
        ai.siirraMuodostelma(toinen, 0);
        assertEquals(1, toinen.getPalikat().get(1).getX());
    }
    
    @Test
    public void pudotaMuodostelmaTyhjaKenttaTest(){
        Muodostelma muod = new Muodostelma(Muoto.I, peli);
        ai.pudotaMuodostelma(muod);
        assertEquals(19, muod.getPalikat().get(1).getY());
    }
    
    @Test
    public void pudotaMuodostelmaToimiiOikeinTest(){
        Palikka este = new Palikka(4, 5);
        List<Palikka> palikat = new ArrayList<Palikka>();
        palikat.add(este);
        peli.lisaaPalikatPeliin(palikat);
        Muodostelma muod = new Muodostelma(Muoto.peiliL, peli);
        ai.pudotaMuodostelma(muod);
        assertEquals(4, muod.getPalikat().get(1).getY());
    }
    
    @Test
    public void pudotaMuodostelmaIMuotoTest(){
        Palikka este = new Palikka(4, 10);
        List<Palikka> palikat = new ArrayList<Palikka>();
        palikat.add(este);
        peli.lisaaPalikatPeliin(palikat);
        Muodostelma muod = new Muodostelma(Muoto.I, peli);
        muod.putoa();
        muod.kierra();
        ai.pudotaMuodostelma(muod);
        assertEquals(7, muod.getPalikat().get(1).getY());
    }
    
    @Test
    public void laskeSiirtoTest(){
        Muodostelma muod = new Muodostelma(Muoto.I, peli);
        muod.putoa();
        muod.kierra();
        ai.siirraMuodostelma(muod, 9);
        ai.laskeSiirto(muod, 1);
        Siirtokeko keko = ai.getKeko();
        assertEquals(1, keko.koko());
        Siirto siirto = keko.suurin();
        assertEquals(2, siirto.getArvo());
        assertEquals(1, siirto.getKierrot());
        assertEquals(9, siirto.getX());
    }
}
