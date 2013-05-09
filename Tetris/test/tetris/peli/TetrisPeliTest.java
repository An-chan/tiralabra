package tetris.peli;


import java.awt.event.ActionEvent;
import java.util.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import tetris.domain.*;
import tetris.peli.Tetris;
import tetris.ui.Piirtoalusta;

public class TetrisPeliTest {
    
    public TetrisPeliTest() {
    }
    
    Tetris peli;
    List<Palikka> palikkalista;
    
    @Before
    public void setUp() {
        peli = new Tetris();
        palikkalista = new ArrayList<Palikka>();
        palikkalista.add(new Palikka(0,19));
        palikkalista.add(new Palikka(1,19));
        palikkalista.add(new Palikka(2,19));
        palikkalista.add(new Palikka(3,19));
        palikkalista.add(new Palikka(4,19));
        palikkalista.add(new Palikka(5,19));
        palikkalista.add(new Palikka(6,19));
        palikkalista.add(new Palikka(7,19));
        palikkalista.add(new Palikka(8,19));
        palikkalista.add(new Palikka(9,19));
    }
    
    @After
    public void tearDown() {
    }
    
    @Test
    public void peliinLuodaanYksiPutoavaTest(){
        Muodostelma putoava = peli.getPutoava();
        assertEquals(putoava.putoaa, true);
    }
    
    @Test
    public void piirtoalustaAsettuuOikeinTest(){
        Piirtoalusta alusta = new Piirtoalusta(peli);
        peli.setAlusta(alusta);
        assertEquals(peli.getAlusta(), alusta);
    }
    
    @Test
    public void pausePaalleToimiiTest(){
        peli.pausePaallePois();
        assertEquals(peli.onkoPause(), true);
    }
    
    @Test
    public void pausePoisToimiiTest(){
        peli.pausePaallePois();
        peli.pausePaallePois();
        assertEquals(peli.onkoPause(), false);
    }
    
    @Test
    public void taydetRivitHavaitaanYksiRiviTest(){
        peli.lisaaPalikatPeliin(palikkalista);
        List<Integer> poistettavat = peli.tarkastaTaydetRivit();
        assertEquals(1, poistettavat.size());
    }
    
    @Test
    public void taydetRivitHavaitaanEpataysiRiviEiHavaitaTest(){
        palikkalista.remove(6);
        peli.lisaaPalikatPeliin(palikkalista);
        List<Integer> poistettavat = peli.tarkastaTaydetRivit();
        assertEquals(null, poistettavat);
    }
    
    @Test
    public void taydetRivitHavaitaanUseampiRiviTest(){
        palikkalista.add(new Palikka(0,10));
        palikkalista.add(new Palikka(1,10));
        palikkalista.add(new Palikka(2,10));
        palikkalista.add(new Palikka(3,10));
        palikkalista.add(new Palikka(4,10));
        palikkalista.add(new Palikka(5,10));
        palikkalista.add(new Palikka(6,10));
        palikkalista.add(new Palikka(7,10));
        palikkalista.add(new Palikka(8,10));
        palikkalista.add(new Palikka(9,10));
        peli.lisaaPalikatPeliin(palikkalista);
        List<Integer> poistettavat = peli.tarkastaTaydetRivit();
        assertEquals(2, poistettavat.size());
    }
    
    @Test
    public void taydetRivitPoistetaanYksiRiviTest(){
        peli.lisaaPalikatPeliin(palikkalista);
        List<Integer> poistettavat = peli.tarkastaTaydetRivit();
        peli.poistaTaydetRivit(poistettavat);
        poistettavat = peli.tarkastaTaydetRivit();
        assertEquals(null, poistettavat);
    }
    
    @Test
    public void taydetRivitPoistetaanUseampiRiviTest(){
        palikkalista.add(new Palikka(0,10));
        palikkalista.add(new Palikka(1,10));
        palikkalista.add(new Palikka(2,10));
        palikkalista.add(new Palikka(3,10));
        palikkalista.add(new Palikka(4,10));
        palikkalista.add(new Palikka(5,10));
        palikkalista.add(new Palikka(6,10));
        palikkalista.add(new Palikka(7,10));
        palikkalista.add(new Palikka(8,10));
        palikkalista.add(new Palikka(9,10));
        peli.lisaaPalikatPeliin(palikkalista);
        List<Integer> poistettavat = peli.tarkastaTaydetRivit();
        peli.poistaTaydetRivit(poistettavat);
        poistettavat = peli.tarkastaTaydetRivit();
        assertEquals(null, poistettavat);
    }
    
    @Test
    public void pistemaaranKasvuOikeinTest(){
        assertEquals(peli.getPisteet(), 0);
        peli.lisaaPalikatPeliin(palikkalista);
        List<Integer> poistettavat = peli.tarkastaTaydetRivit();
        peli.poistaTaydetRivit(poistettavat);
        assertEquals(peli.getPisteet(), 100);
    }
    
    @Test
    public void tasonKasvuOikeinPienetTest(){
        peli.setPisteet(200);
        peli.laskeTaso();
        assertEquals(1, peli.getTaso());
        peli.setPisteet(600);
        peli.laskeTaso();
        assertEquals(2, peli.getTaso());
        peli.setPisteet(1190);
        peli.laskeTaso();
        assertEquals(2, peli.getTaso());
        peli.setPisteet(1200);
        peli.laskeTaso();
        assertEquals(3, peli.getTaso());
    }
    
    @Test
    public void tasonKasvuOikeinSuuretTest(){
        peli.setPisteet(10000);
        peli.laskeTaso();
        assertEquals(8, peli.getTaso());
        peli.setPisteet(11100);
        peli.laskeTaso();
        assertEquals(9, peli.getTaso());
        peli.setPisteet(8400);
        peli.laskeTaso();
        assertEquals(7, peli.getTaso());
        peli.setPisteet(12900);
        peli.laskeTaso();
        assertEquals(9, peli.getTaso());
        peli.setPisteet(13000);
        peli.laskeTaso();
        assertEquals(10, peli.getTaso());
    }
    
    @Test
    public void vaikeustasonKasvatusToimiiTest(){
        peli.nostaVaikeustasoa();
        peli.laskeTaso();
        assertEquals(2, peli.getTaso());
        peli.nostaVaikeustasoa();
        peli.nostaVaikeustasoa();
        peli.nostaVaikeustasoa();
        peli.laskeTaso();
        assertEquals(5, peli.getTaso());
    }
    
    @Test
    public void vaikeustasonKasvatusSuuriMaaraTest(){
        for (int i = 1; i < 20; i++){
            peli.nostaVaikeustasoa();
        }
        peli.laskeTaso();
        assertEquals(20, peli.getTaso());
        for (int i = 0; i < 10; i++){
            peli.nostaVaikeustasoa();
        }
        peli.laskeTaso();
        assertEquals(30, peli.getTaso());
    }
}
