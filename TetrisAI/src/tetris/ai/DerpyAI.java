/*
 * Tämän tekoälyn on tarkoitus olla ikäänkuin kehittelyn alkuaste, eikä
 * se siis pärjää tetriksessä vielä kauhean hyvin. Sen sisäinen logiikka
 * on hyvin yksinkertainen, mutta käytän sitä logiikan testailuun ja
 * parantamiseen.
 */
package tetris.ai;

import java.util.List;
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
        this.pelipalikat = new Palikka[20][10];
        Palikka[][] pelip = peli.getPalikkaTaulukko();
        for (int i = 0; i < pelip.length; i++){
            for (int j = 0; j < pelip[i].length ; j++){
                if (pelip[i][j] != null){
                    pelipalikat[i][j] = pelip[i][j].kloonaa();
                }
                pelipalikat[i][j] = pelip[i][j];
            }
        }
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
            if (muod.getMuoto() == Muoto.nelio){
                // nelio-muoto aina samanlainen, ts. sillä ei ole kiertoja
                siirraMuodostelma(muod, i);
                laskeSiirto(muod, 0);
            } else if (muod.getMuoto() == Muoto.S || muod.getMuoto() == Muoto.peiliS){
                // näillä muodoilla vain kaksi eri orientaatiota
                siirraMuodostelma(muod, i);
                laskeSiirto(muod, 0);
                muod.putoa();
                muod.kierra();
                siirraMuodostelma(muod, i);
                laskeSiirto(muod, 1);
            } else if (muod.getMuoto() == Muoto.I){
                // I-muodolla myös vain kaksi orientaatiota
                siirraMuodostelma(muod, i);
                laskeSiirto(muod, 0);
                muod.putoa();
                muod.putoa();
                muod.kierra();
                siirraMuodostelma(muod, i);
                laskeSiirto(muod, 1);
            } else {
                // lopuilla muodoilla kaikki neljä orientaatiota
                siirraMuodostelma(muod, i);
                laskeSiirto(muod, 0);
                muod.putoa();
                for (int j = 1; j < 4; j++){
                    if (i == 9){
                        siirraMuodostelma(muod, i-1);
                    }
                    if (i == 0){
                        siirraMuodostelma(muod, i+1);
                    }
                    muod.kierra();
                    siirraMuodostelma(muod, i);
                    laskeSiirto(muod, j);
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
    void laskeSiirto(Muodostelma muod, int kierrot){
        Muodostelma kopio = muod.kloonaa();
        int x = kopio.getPalikat().get(1).getX();
        pudotaMuodostelma(kopio); //mitä täällä tapahtuu?
        int y = kopio.getPalikat().get(1).getY();
        int sivut = laskeSivut(kopio);
        int rivit = laskeRivit(kopio);
        Siirto uusi = new Siirto(y, sivut, rivit, x, kierrot);
        this.keko.lisaa(uusi);
    }
    
    /**
     * Metodi pudottaa muodostelman suoraan alimpaan mahdolliseen kohtaan sen
     * senhetkisellä x-sijainnilla
     * @param muod käsiteltävä muodostelma
     */
    void pudotaMuodostelma(Muodostelma muod){
        while(muod.putoaa){
            muod.putoa();
        }
    }
    
    /**
     * Debuggaamiseen
     * @param muod 
     */
    void lisaaKentalle(List<Palikka> palikat){
        for (int i = 0; i < palikat.size(); i++){
            this.pelipalikat[palikat.get(i).getY()][palikat.get(i).getX()] = palikat.get(i);
        }
    }
    
    /**
     * Metodi siirtää muodostelman haluttuun x-koordinaatin positioon
     * @param muod käsiteltävä muodostelma
     * @param x haluttu pivot-palikan x-koordinaatti
     */
    void siirraMuodostelma(Muodostelma muod, int x){
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
        this.putoava.putoa();
        if (kierrot > 0){
            for (int i = 0; i < kierrot; i++){
                this.putoava.kierra();
            }
        }
        siirraMuodostelma(this.putoava, x);
//        System.out.println("x: " + this.putoava.getPalikat().get(1).getX());
        pudotaMuodostelma(putoava);
        this.keko.tyhjenna();
//        System.out.println(paras.toString());
    }
    
    /**
     * Metodi laskee, montako täyttä riviä siirrolla saadaan aikaiseksi. Tämän
     * tehdäkseen se liittää ensin kaikki muodostelman palikat kentälle, laskee
     * täydet rivit, ja poistaa palikat lopuksi kentältä, jotta ne eivät sotke
     * tulevia laskuja.
     * @param muod käsiteltävä muodostelma
     * @return täysien rivien määrä
     */
    int laskeRivit(Muodostelma muod){
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

    /**
     * Metodi laskee monellako sivullaan käsiteltävä muodostelma
     * koskettaa joko jo pelissä olevia palikoita tai pelialueen
     * reunoja. 
     * @param muod käsiteltävä muodostelma
     * @return kontaktisivujen määrä
     */
    int laskeSivut(Muodostelma muod) {
        int sivut = 0;
        List<Palikka> palikat = muod.getPalikat();
        for (int i = 0; i < palikat.size(); i++){
            int x = palikat.get(i).getX();
            int y = palikat.get(i).getY();
            if (x + 1 > 9){
                sivut ++;
            } else if (this.pelipalikat[y][x+1] != null){
                sivut ++;
            }
            if (x - 1 < 0){
                sivut ++;
            } else if (this.pelipalikat[y][x-1] != null){
                sivut ++;
            }
            if (y + 1 > 19){
                sivut ++;
            } else if (this.pelipalikat[y+1][x] != null){
                sivut ++;
            }
        }
        return sivut; 
    }
    
    /**
     * Palauttaa tekoälyn keon debuggaamista varten
     * @return tekoälyn käyttämä keko
     */
    public Siirtokeko getKeko(){
        return this.keko;
    }
    
    /**
     * Palauttaa senhetkisen muodostelman
     * @return käsittelyssä oleva muodostelma
     */
    public Muodostelma getMuodostelma(){
        return this.putoava;
    }
}
