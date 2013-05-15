/*
 * Tämän tekoälyn on tarkoitus olla ikäänkuin kehittelyn alkuaste, eikä
 * se siis pärjää tetriksessä vielä kauhean hyvin. Sen sisäinen logiikka
 * on hyvin yksinkertainen, mutta käytän sitä logiikan testailuun ja
 * parantamiseen.
 */
package tetris.ai;

import tetris.domain.*;
import tetris.peli.Tetris;

public class DerpyAI {
    private Tetris peli;
    private Palikka[][] pelipalikat;
    private Muodostelma putoava;
    
    public DerpyAI(Tetris peli){
        this.peli = peli;
        paivitaTiedot();
    }
    
    private void paivitaTiedot(){
        this.pelipalikat = this.peli.getPalikkaTaulukko();
        this.putoava = this.peli.getPutoava();
    }
}
