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
    
    /**
     * Uutta tekoälyluokkaa luotaessa tarvitaan parametrinä peli, jota sen tulee
     * pelata. Lisäksi konstruktorissa luodaan tekoälyn optimointiin käyttämä
     * keko ja päivitetään sen tiedot pelin tilanteesta
     * @param peli Tetris-luokan ilmentymä, johon tekoäly halutaan liittää
     */
    public DerpyAI(Tetris peli){
        this.peli = peli;
        this.keko = new Siirtokeko();
        paivitaTiedot();
    }
    
    /**
     * Metodi päivittää pelitilanteen tekoälyn laskentaa varten
     */
    private void paivitaTiedot(){
        this.putoava = peli.getPutoava();
        this.pelipalikat = this.peli.getPalikkaTaulukko();
    }
    
    /**
     * Metodi etsii kaikki mahdolliset siirrot ja lisää ne kekoon:
     * - Ensin lasketaan kaikki lailliset x-sijainnit, ts. sivusuuntainen liike
     * - Toiseksi otetaan huomioon kaikki mahdolliset kierrot
     * - Lisätään kekoon kukin mahdollinen siirto
     */
    private void etsiSiirrot(){
        paivitaTiedot();
        for (int i = 0; i < 10; i++){ //etsii lailliset x-sijainnit
            Muodostelma muod = putoava.kloonaa();
            siirraMuodostelma(muod, i);
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
    
    /**
     * Laskee uuteen siirtoon tarvittavat parametrit ja luo uuden siirron
     * sekä lisää sen siirtokekoon
     * !!Keskeneräinen, ei toimi vielä täysin!!
     * @param muod käsiteltävä muodostelma
     * @param kierrot muodostelman kiertojen määrä
     */
    private void laskeSiirto(Muodostelma muod, int kierrot){
        int x = muod.getPalikat().get(1).getX();
        pudotaMuodostelma(muod);
        int y = muod.getPalikat().get(1).getY();
        int sivut = 0; // sivujen laskemiseen tulee oma systeeminsä
        int rivit = laskeRivit(muod);
        Siirto uusi = new Siirto(y, sivut, rivit, x, kierrot);
        this.keko.lisaa(uusi);
    }
    
    /**
     * Metodi pudottaa muodostelman suoraan alimpaan mahdolliseen kohtaan sen
     * senhetkisellä x-sijainnilla
     * @param muod käsiteltävä muodostelma
     */
    private void pudotaMuodostelma(Muodostelma muod){
        while(muod.putoaa){
            muod.putoa();
        }
    }
    
    /**
     * Metodi siirtää muodostelman haluttuun x-koordinaatin positioon
     * @param muod käsiteltävä muodostelma
     * @param x haluttu pivot-palikan x-koordinaatti
     */
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
    
    /**
     * Metodi hakee keosta parhaan sinne lisätyn siirron ja suorittaa sen
     */
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
        pudotaMuodostelma(putoava);
    }
    
    /**
     * KESKENERÄINEN METODI
     * @param muod
     * @return 
     */
    private int laskeRivit(Muodostelma muod){
        int rivit = 0;
        for (int i = 0; i < 4; i++){
            Palikka p = muod.getPalikat().get(i);
            int x = p.getX();
            int y = p.getY();
            this.pelipalikat[y][x] = p;
        }
        for (int i = pelipalikat.length - 1; i >= 0; i--) {
            if (pelipalikat[i][0] != null) {
                boolean taysi = true;
                for (int j = 0; j < pelipalikat[i].length; j++) {
                    if (pelipalikat[i][j] == null) {
                        taysi = false;
                        break;
                    }
                }
                if (taysi) {
                    rivit++;
                }
            }
        }
        for (int i = 0; i < 4; i++){
            Palikka p = muod.getPalikat().get(i);
            int x = p.getX();
            int y = p.getY();
            this.pelipalikat[y][x] = null;
        }
        return rivit;
    }
}
