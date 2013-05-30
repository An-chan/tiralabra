/*
 * Tämän tekoälyn on tarkoitus olla ikäänkuin kehittelyn alkuaste, eikä
 * se siis pärjää tetriksessä vielä kauhean hyvin. Sen sisäinen logiikka
 * on hyvin yksinkertainen, mutta käytän sitä logiikan testailuun ja
 * parantamiseen.
 */
package tetris.ai;

import tetris.ai.utility.*;
import tetris.domain.*;
import tetris.peli.Tetris;

public class DerpyAI {
    private Tetris peli;
    private Palikka[][] pelipalikat;
    private Muodostelma putoava;
    private Siirtokeko keko;
    
    /*
     * Uutta tekoälyluokkaa luotaessa tarvitaan parametrinä peli, jota sen tulee
     * pelata. Lisäksi konstruktorissa luodaan tekoälyn optimointiin käyttämä
     * keko ja päivitetään sen tiedot pelin tilanteesta
     */
    public DerpyAI(Tetris peli){
        this.peli = peli;
        this.keko = new Siirtokeko();
        paivitaTiedot();
    }
    
    /*
     * Metodi päivittää pelitilanteen tekoälyn laskentaa varten
     */
    private void paivitaTiedot(){
        this.putoava = peli.getPutoava();
        this.pelipalikat = this.peli.getPalikkaTaulukko();
    }
    
    /*
     * Metodi etsii kaikki mahdolliset siirrot ja lisää ne kekoon:
     * - Ensin lasketaan kaikki lailliset x-sijainnit, ts. sivusuuntainen liike
     * - Toiseksi otetaan huomioon kaikki mahdolliset kierrot
     * - Lisätään kekoon kukin mahdollinen siirto
     */
    private void etsiSiirrot(){
        for (int i = 0; i < 10; i++){ //etsii lailliset x-sijainnit
            Muodostelma muod = putoava.kloonaa();
            // siirrä muodostelma oikeaan kohtaan
            if (muod.getMuoto() == Muoto.nelio){
                // nelio-muoto aina samanlainen, ts. sillä ei ole kiertoja
                laskeSiirto(muod, 0);
            } else if (muod.getMuoto() == Muoto.S || 
                    muod.getMuoto() == Muoto.peiliS || muod.getMuoto() == Muoto.I){
                // näillä muodoilla vain kaksi eri orientaatiota
                laskeSiirto(muod, 0);
                muod.kierra();
                laskeSiirto(muod, 1);
            } else {
                Muodostelma kopio = muod.kloonaa();
                // lopuilla muodoilla kaikki neljä orientaatiota
                kopio.putoa();
                for (int j = 0; j < 4; j++){
                    for (int k = 0; k < j; k++){
                        kopio.kierra();
                    }
                    laskeSiirto(kopio, j);
                }
            }
        }
    }
    
    /*
     * Metodi luo Siirto-olion kekoon lisättäväksi
     * Toistaiseksi vaillinainen toiminnallisuus
     */
    private void laskeSiirto(Muodostelma muod, int kierrot){
        int x = muod.getPalikat().get(1).getX();
        pudotaMuodostelma(muod);
        int y = muod.getPalikat().get(1).getY();
        int sivut = 0; // sivujen laskemiseen tulee oma systeeminsä
        int rivit = 0; //rivien laskemiseen tulee oma systeeminsä
        Siirto uusi = new Siirto(y, sivut, rivit, x, kierrot);
        this.keko.lisaa(uusi);
    }
    
    private void pudotaMuodostelma(Muodostelma muod){
        while(muod.putoaa){
            muod.putoa();
        }
    }
    
    private void siirraMuodostelma(Muodostelma muod, int x){
        int pivotX = muod.getPalikat().get(1).getX();
        if (x < pivotX){
            while (x < pivotX){
                int temp = pivotX;
                muod.siirra(-1);
                pivotX = muod.getPalikat().get(1).getX();
                if (pivotX == temp){
                    break;
                }
            }
        } else {
            while (pivotX < x){
                int temp = pivotX;
                muod.siirra(1);
                pivotX = muod.getPalikat().get(1).getX();
                if (pivotX == temp){
                    break;
                }
            }
        }
    }
    
    public void teeSiirto(){
        etsiSiirrot();
        Siirto paras = this.keko.suurin();
        int x = paras.getX();
        int kierrot = paras.getKierrot();
        siirraMuodostelma(this.putoava, x);
        this.putoava.putoa();
        for (int i = 0; i <= kierrot; i++){
            this.putoava.kierra();
        }
    }
}
