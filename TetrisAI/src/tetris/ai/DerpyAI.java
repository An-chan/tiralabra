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
    
    public DerpyAI(Tetris peli){
        this.peli = peli;
        this.keko = new Siirtokeko();
        this.putoava = peli.getPutoava();
        paivitaTiedot();
    }
    
    /*
     * Metodi päivittää pelitilanteen tekoälyn laskentaa varten
     */
    private void paivitaTiedot(){
        this.pelipalikat = this.peli.getPalikkaTaulukko();
    }
    
    /*
     * Metodi etsii kaikki mahdolliset siirrot ja lisää ne kekoon:
     * - Ensin lasketaan kaikki lailliset x-sijainnit, ts. sivusuuntainen liike
     * - Toiseksi otetaan huomioon kaikki mahdolliset kierrot
     * - Lisätään kekoon kukin mahdollinen siirto
     */
    private void etsiSiirrot(){
        for (int i = 0; i < 10; i++){
            Muodostelma muod = putoava.kloonaa();
            //etsi lailliset x-sijainnit
            if (muod.getMuoto() == Muoto.nelio){
                // nelio-muoto aina samanlainen
                Palikka pivot = muod.getPalikat().get(1);
                laskeSiirto(pivot.getX(), pivot.getY(), 0);
            } else if (muod.getMuoto() == Muoto.S || 
                    muod.getMuoto() == Muoto.peiliS || muod.getMuoto() == Muoto.I){
                // näillä muodoilla vain kaksi eri orientaatiota
                Palikka pivot = muod.getPalikat().get(1);
                laskeSiirto(pivot.getX(), pivot.getY(), 0);
                muod.kierra();
                
            } else {
                // lopuilla muodoilla kaikki neljä orientaatiota
                muod.putoa();
                for (int j = 0; j < 4; j++){
                    for (int k = 0; k < j; k++){
                        muod.kierra();
                    }
                    Palikka pivot = muod.getPalikat().get(1);
                    laskeSiirto(pivot.getX(), pivot.getY(), j);
                }
            }
        }
    }
    
    /*
     * Metodi luo Siirto-olion kekoon lisättäväksi
     * Toistaiseksi vaillinainen toiminnallisuus
     */
    private void laskeSiirto(int x, int y, int kierrot){
        //emuloidaan muodostelman pudottamista
        
        //Siirto uusi = new Siirto(y, sivut, rivit, x, kierrot);     
    }
}
