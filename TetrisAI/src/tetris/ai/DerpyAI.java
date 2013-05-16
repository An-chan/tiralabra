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
        paivitaTiedot();
    }
    
    /*
     * Metodi päivittää pelitilanteen tekoälyn laskentaa varten
     */
    private void paivitaTiedot(){
        this.pelipalikat = this.peli.getPalikkaTaulukko();
        this.putoava = this.peli.getPutoava().kloonaa();
    }
    
    /*
     * Metodi etsii kaikki mahdolliset siirrot ja lisää ne kekoon
     */
    private void etsiSiirrot(){
        for (int i = 0; i < 10; i++){
            //etsi lailliset x-sijainnit
            if (putoava.getMuoto() == Muoto.nelio){
                // nelio-muoto aina samanlainen
            } else if (putoava.getMuoto() == Muoto.S || 
                    putoava.getMuoto() == Muoto.peiliS || putoava.getMuoto() == Muoto.I){
                // näillä muodoilla vain kaksi eri orientaatiota
            } else {
                // lopuilla muodoilla kaikki neljä orientaatiota
                putoava.putoa();
                for (int j = 0; j < 4; j++){
                    for (int k = 0; k < j; k++){
                        putoava.kierra();
                    }
                    // lisätään kaikki eri orientaatiot siirroiksi
                    
                }
            }
        }
    }
    
    /*
     * Metodi luo Siirto-olion kekoon lisättäväksi
     * Toistaiseksi vaillinainen toiminnallisuus
     */
    private Siirto laskeSiirto(int x, int y, int kierrot){
        
        return null;
    }
}
