/*
 * PegasusAI on tekoälyn ydinluokka, joka hoitaa kaiken oleellisen hallinnoinnin.
 * Jokaisella pelikierroksella se käy läpi mahdolliset siirrot ja luo niistä
 * siirto-olioita, jotka lisätään kekoon. Lopuksi se poimii keosta parhaan siirron
 * ja suorittaa sen. Kaikki siirron laskemiseen tarvittavat metodit löytyvät
 * tästä luokasta.
 */
package tetris.ai;

import java.util.List;
import tetris.ai.utility.*;
import tetris.domain.*;
import tetris.peli.Tetris;

public class PegasusAI {

    private Tetris peli;
    private Palikka[][] pelipalikat;
    private Muodostelma putoava;
    private Siirtokeko keko;

    /**
     * Uutta tekoälyluokkaa luotaessa tarvitaan parametrinä peli, jota sen tulee
     * pelata. Lisäksi konstruktorissa luodaan tekoälyn optimointiin käyttämä
     * keko ja päivitetään sen tiedot pelin tilanteesta
     *
     * @param peli Tetris-luokan ilmentymä, johon tekoäly halutaan liittää
     */
    public PegasusAI(Tetris peli) {
        this.peli = peli;
        this.keko = new Siirtokeko();
        paivitaTiedot();
    }

    /**
     * Metodi päivittää pelitilanteen tekoälyn laskentaa varten kloonaamalla
     * senhetkisen pelitilanteen tekoälyn käyttöä varten.
     */
    private void paivitaTiedot() {
        this.putoava = peli.getPutoava();
        this.pelipalikat = new Palikka[20][10];
        Palikka[][] pelip = peli.getPalikkaTaulukko();
        for (int i = 0; i < pelip.length; i++) {
            for (int j = 0; j < pelip[i].length; j++) {
                if (pelip[i][j] != null) {
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
    private void etsiSiirrot() {
        paivitaTiedot();
        for (int i = 0; i < 10; i++) { //etsii lailliset x-sijainnit
            Muodostelma muod = putoava.kloonaa();
            if (muod.getMuoto() == Muoto.nelio) {
                // nelio-muoto aina samanlainen, ts. sillä ei ole kiertoja
                siirraMuodostelma(muod, i);
                laskeSiirto(muod, 0);
            } else if (muod.getMuoto() == Muoto.S || muod.getMuoto() == Muoto.peiliS) {
                // näillä muodoilla vain kaksi eri orientaatiota
                siirraMuodostelma(muod, i);
                laskeSiirto(muod, 0);
                muod.putoa();
                muod.kierra();
                siirraMuodostelma(muod, i);
                laskeSiirto(muod, 1);
            } else if (muod.getMuoto() == Muoto.I) {
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
                for (int j = 1; j < 4; j++) {
                    if (i == 9) {
                        siirraMuodostelma(muod, i - 1);
                    }
                    if (i == 0) {
                        siirraMuodostelma(muod, i + 1);
                    }
                    muod.kierra();
                    siirraMuodostelma(muod, i);
                    laskeSiirto(muod, j);
                }
            }
        }
    }

    /**
     * Laskee uuteen siirtoon tarvittavat parametrit ja luo uuden siirron sekä
     * lisää sen siirtokekoon. Tarvittavat parametrit lasketaan omissa metodeissaan,
     * joita tämä metodi kutsuu. Metodi käyttää käsiteltävän muodostelman kopiota,
     * jotta se ei sotke muiden metodien toimintaa.
     *
     * @param muod käsiteltävä muodostelma
     * @param kierrot muodostelman kiertojen määrä, 0-3
     */
    void laskeSiirto(Muodostelma muod, int kierrot) {
        Muodostelma kopio = muod.kloonaa();
        int x = kopio.getPalikat().get(1).getX();
        pudotaMuodostelma(kopio);
        int y = kopio.getPalikat().get(1).getY();
        int sivut = laskeSivut(kopio);
        int rivit = laskeRivit(kopio);
        int kolot = laskeKolot(kopio);
        Siirto uusi = new Siirto(y, sivut, rivit, x, kierrot, kolot);
        this.keko.lisaa(uusi);
    }

    /**
     * Metodi pudottaa muodostelman suoraan alimpaan mahdolliseen kohtaan sen
     * senhetkisellä x-sijainnilla
     *
     * @param muod käsiteltävä muodostelma
     */
    void pudotaMuodostelma(Muodostelma muod) {
        while (muod.putoaa) {
            muod.putoa();
        }
    }

    /**
     * Debuggaamiseen ja testaamiseen tarkoitettu metodi, joka lisää
     * parametrina annetun listan palikat kentälle.
     *
     * @param List<Palikka> lista lisättävistä palikoista
     */
    void lisaaKentalle(List<Palikka> palikat) {
        for (int i = 0; i < palikat.size(); i++) {
            this.pelipalikat[palikat.get(i).getY()][palikat.get(i).getX()] = palikat.get(i);
        }
    }

    /**
     * Metodi siirtää muodostelman haluttuun x-koordinaatin positioon.
     * Jos muodostelmaa ei voida siirtää annettuun x-koordinaattiin asti,
     * siirretään se niin lähelle kuin mahdollista. Sijainnin määrittelyyn
     * käytetään muodostelman pivot-palikkaa (sitä, jonka ympäri kierto tapahtuu).
     *
     * @param muod käsiteltävä muodostelma
     * @param x haluttu pivot-palikan x-koordinaatti, 0-9
     */
    void siirraMuodostelma(Muodostelma muod, int x) {
        int pivotX = muod.getPivot().getX();
        if (x < pivotX) {
            while (x < pivotX) {
                int temp = pivotX;
                muod.siirra(-1);
                pivotX = muod.getPivot().getX();
                if (pivotX == temp) {
                    break;
                }
            }
        } else {
            while (pivotX < x) {
                int temp = pivotX;
                muod.siirra(1);
                pivotX = muod.getPivot().getX();
                if (pivotX == temp) {
                    break;
                }
            }
        }
    }

    /**
     * Metodi hakee keosta parhaan sinne lisätyn siirron, ts. keon huipulla
     * olevan siirron, ja suorittaa sen parametrien mukaisesti. Lopuksi
     * keko tyhjennetään, jottei sinne jää siirtoja jotka saattaisivat häiritä
     * seuraavan kierroksen laskentaa.
     * Siirtäminen suoritetaan seuraavasti:
     * - Suoritetaan mahdolliset kierrot
     * - Siirretään muodostelma haluttuun x-sijaintiin
     * - Pudotetaan muodostelma niin alas kuin mahdollista
     */
    public void teeSiirto() {
        etsiSiirrot();
        Siirto paras = this.keko.suurin();
        int x = paras.getX();
        int kierrot = paras.getKierrot();
        this.putoava.putoa(); 
        if (kierrot > 0) {
            for (int i = 0; i < kierrot; i++) {
                this.putoava.kierra();
            }
        }
        siirraMuodostelma(this.putoava, x);
        pudotaMuodostelma(putoava);
        this.keko.tyhjenna();
    }

    /**
     * Metodi laskee, montako täyttä riviä siirrolla saadaan aikaiseksi. Tämän
     * tehdäkseen se liittää ensin kaikki muodostelman palikat kentälle, laskee
     * täydet rivit, ja poistaa palikat lopuksi kentältä, jotta ne eivät sotke
     * tulevia laskuja.
     *
     * @param muod käsiteltävä muodostelma
     * @return täysien rivien määrä
     */
    int laskeRivit(Muodostelma muod) {
        int rivit = 0;
        for (int i = 0; i < 4; i++) {
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
        for (int i = 0; i < 4; i++) {
            Palikka p = muod.getPalikat().get(i);
            int x = p.getX();
            int y = p.getY();
            this.pelipalikat[y][x] = null;
        }
        return rivit;
    }

    /**
     * Metodi laskee monellako sivullaan käsiteltävä muodostelma koskettaa joko
     * jo pelissä olevia palikoita tai pelialueen reunoja.
     *
     * @param muod käsiteltävä muodostelma
     * @return kontaktisivujen määrä
     */
    int laskeSivut(Muodostelma muod) {
        int sivut = 0;
        List<Palikka> palikat = muod.getPalikat();
        for (int i = 0; i < palikat.size(); i++) {
            int x = palikat.get(i).getX();
            int y = palikat.get(i).getY();
            if (x + 1 > 9) {
                sivut++;
            } else if (this.pelipalikat[y][x + 1] != null) {
                sivut++;
            }
            if (x - 1 < 0) {
                sivut++;
            } else if (this.pelipalikat[y][x - 1] != null) {
                sivut++;
            }
            if (y + 1 > 19) {
                sivut++;
            } else if (this.pelipalikat[y + 1][x] != null) {
                sivut++;
            }
        }
        return sivut;
    }

    /**
     * Metodi laskee muodostelman alle jäävien tyhjien paikkojen tai "kolojen"
     * määrän. Ensin tarkastetaan, onko käsittelyssä oleva palikka muodostelman
     * alin (ts. onko sen alapuolella muodostelmaan kuuluvia palikoita), ja sitten
     * etsitään mahdolliset null-arvoiset kentät muodostelman alapuolelta kentältä.
     * Jos null-arvoinen eli tyhjä kohta löytyy, selvitetään sen syvyys ja lisätään
     * tämä palautettavaan kolojen määrään.
     *
     * @param muod käsiteltävä muodostelma
     * @return kolot, muodostelman alle jäävien tyhjien paikkojen määrä
     */
    int laskeKolot(Muodostelma muod) {
        int kolot = 0;
        List<Palikka> palikat = muod.getPalikat();
        for (int i = 0; i < palikat.size(); i++) {
            int x = palikat.get(i).getX();
            int y = palikat.get(i).getY();
            boolean alin = true;
            for (int j = i + 1; j < palikat.size(); j++) {
                if (palikat.get(j).getX() == x && palikat.get(j).getY() == y + 1) {
                    alin = false;
                }
            }
            if (alin){
                int kierros = 1;
                while (y + kierros < 20 && pelipalikat[y+kierros][x] == null) {
                    kolot++;
                    kierros++;
                }
            }
        }
        return kolot;

    }

    /**
     * Palauttaa tekoälyn käyttämän keon debuggaamista varten
     *
     * @return Siirtokeko - tekoälyn käyttämä keko
     */
    public Siirtokeko getKeko() {
        return this.keko;
    }

    /**
     * Palauttaa senhetkisen muodostelman
     *
     * @return käsittelyssä oleva muodostelma
     */
    public Muodostelma getMuodostelma() {
        return this.putoava;
    }
}
