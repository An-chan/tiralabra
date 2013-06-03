package tetris.peli;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import javax.swing.JLabel;
import tetris.ai.DerpyAI;
import tetris.domain.*;
import tetris.ui.*;

/**
 * @author Anni
 *
 * Tetris-luokka käsittelee pelialueen toimintoja ja pitää kirjaa pelissä
 * olevista palikoista (sekä putoava muodostelma että jo kasatut palikat).
 * Lisäksi luokka pitää kirjaa pisteistä ja vaikeustasosta, ja alustaa kaikki
 * muuttujat oletusarvoihin uuden pelin alkaessa.
 */
public class Tetris {

    private Muodostelma putoava;
    private Palikka[][] pelipalikat;
    private Piirtoalusta pelialue;
    private int viive;
    private boolean pause;
    private boolean jatkuu;
    private int pisteet;
    private int taso;
    private int alkutaso;
    private JLabel status;
    private JLabel pisteLabel;
    private JLabel tasoLabel;
    private DerpyAI ai;
    private boolean onkoAI;

    public Tetris() {
        this.pause = false;
        this.jatkuu = true;
        this.pisteet = 0;
        this.taso = 1;
        this.alkutaso = 0;
        this.viive = 2000;
        this.onkoAI = false;
        this.pelipalikat = new Palikka[20][];
        for (int i = 0; i < 20; i++) {
            pelipalikat[i] = new Palikka[10];
        }
        luoUusiPutoava();
    }

    /**
     * Metodi luo uuden muodostelman putoavaksi muodostelmaksi ja antaa sille
     * satunnaisen muodon.
     *
     * Ei palauta mitään, vaan asettaa tuloksen this.putoava.
     */
    public void luoUusiPutoava() {
        Random rand = new Random();
        int satun = rand.nextInt(7);
        switch (satun) {
            case 0:
                this.putoava = new Muodostelma(Muoto.I, this);
                break;
            case 1:
                this.putoava = new Muodostelma(Muoto.S, this);
                break;
            case 2:
                this.putoava = new Muodostelma(Muoto.peiliS, this);
                break;
            case 3:
                this.putoava = new Muodostelma(Muoto.L, this);
                break;
            case 4:
                this.putoava = new Muodostelma(Muoto.peiliL, this);
                break;
            case 5:
                this.putoava = new Muodostelma(Muoto.T, this);
                break;
            case 6:
                this.putoava = new Muodostelma(Muoto.nelio, this);
                break;
        }
    }

    /**
     * Metodi ajaa pelin normaalin syklin, jossa odotetaan viiveen ajan ja
     * sitten pudotetaan putoavaa muodostelmaa yhden verran alaspäin. Kun
     * muodostelma osuu johonkin ja lakkaa putoamasta, metodi kutsuu rivien
     * täyttymisen ja täysien rivien poistavia metodeja ennen uuden muodostelman
     * luomista ja syklin jatkumista.
     */
    public void peliSykli() {
        while (jatkuu) {
            try {
                Thread.sleep(viive);
            } catch (Exception e) {
                status.setText("Jotain meni vikaan...");
            }
            if (pause) {
                while (pause) {
                    status.setText("Peli pysäytetty, paina P jatkaaksesi");
                }
                status.setText("Paina P pysäyttääksesi pelin");
            }
            this.putoava.putoa();
            if (this.onkoAI){
                this.ai.teeSiirto();
            }
            if (!putoava.putoaa) { // kun putoava muodostelma törmää, sen palikat lisätään pelipalikoihin
                lisaaPalikatPeliin(putoava.getPalikat());
                luoUusiPutoava(); // luodaan uusi putoava muodostelma
                List<Integer> tayttyneet = tarkastaTaydetRivit();
                if (tayttyneet != null) { //tarkastetaan tuliko rivejä täyteen
                    poistaTaydetRivit(tayttyneet);
                }
            }

            this.pelialue.repaint();

        }
        status.setText("Peli päättynyt! Enter aloittaa uuden pelin");
    }

    /**
     * Metodi pysäyttää pelin tilapäisesti tai palauttaa sen pysäytystilasta.
     */
    public void pausePaallePois() {
        if (this.pause) {
            this.pause = false;
        } else {
            this.pause = true;
        }
    }

    public boolean onkoPause() {
        return this.pause;
    }
    
    public boolean getJatkuu(){
        return this.jatkuu;
    }

    /**
     * Metodi tarkastaa, onko jokin pelissä olevien palikoiden riveistä tullut
     * täyteen, ja palauttaa täysien rivien numerot listana poistamista varten
     *
     * @return List<Integer> lista poistettavista riveistä
     */
    public List<Integer> tarkastaTaydetRivit() {
        List<Integer> taydet = new ArrayList<Integer>();
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
                    taydet.add(i);
                }
            }
        }
        if (taydet.isEmpty()) {
            return null;
        }
        return taydet;
    }

    /**
     * Metodi poistaa täyttyneet rivit pelistä ja laskee niiden yläpuolella
     * olevia rivejä alas tarvittavan määrän.
     *
     * @param taydet lista poistettavista riveistä
     */
    public void poistaTaydetRivit(List<Integer> taydet) {
        if (taydet.size() > 1){
            Collections.sort(taydet);
        }
        for (Integer i : taydet) {
            pelipalikat[i] = new Palikka[10];
            for (int x = i-1; x > 0; x--){
                for (int j = 0; j < pelipalikat[x].length; j++){
                    if (pelipalikat[x][j] != null){
                        pelipalikat[x][j].putoa();
                        pelipalikat[x+1][j] = pelipalikat[x][j];
                        pelipalikat[x][j] = null;
                    }
                }
            }
        }
        this.pisteet += 100 * taydet.size();
        laskeTaso();
        paivitaPisteetJaTaso();
    }
    
    /**
     * Metodi pitää kirjaa pisteiden kasvusta ja siten vaikeustason
     * noususta
     */
    public void laskeTaso(){
        
        if (pisteet < 500){
            taso = alkutaso + 1;
        } else if (pisteet < 1200){
            taso = alkutaso + 2;
        } else if (pisteet < 2200){
            taso = alkutaso + 3;
        } else if (pisteet < 3500){
            taso = alkutaso + 4;
        } else if (pisteet < 5000){
            taso = alkutaso + 5;
        } else if (pisteet < 6500){
            taso = alkutaso + 6;
        } else if (pisteet < 8500){
            taso = alkutaso + 7;
        } else if (pisteet < 11000){
            taso = alkutaso + 8;
        } else if (pisteet < 13000){
            taso = alkutaso + 9;
        } else {
            taso = alkutaso + 10;
        }
        this.viive = 2000/taso;
        
    }
    
    /**
     * Metodi nostaa vaikeustasoa, jolloin pelin varsinaiset tasot skaalautuvat
     * yhden tason verran ylöspäin.
     */
    public void nostaVaikeustasoa(){
        this.alkutaso++;
        laskeTaso();
        paivitaPisteetJaTaso();
    }
    
    /**
     * Metodi lähettää tämänhetkisen pistetilanteen käyttöliittymään
     * pelaajalle näytettäväksi.
     */
    public void paivitaPisteetJaTaso(){
        if (this.pisteLabel == null | this.tasoLabel == null){
            return;
        }
        this.pisteLabel.setText(""+this.pisteet);
        this.tasoLabel.setText(""+this.taso);
    }

    /**
     * Metodi palauttaa listan kaikista pelissä olevista palikoista, paitsi
     * senhetkisen putoavan mukaikissaodostelman palikat.
     *
     * @return List<Palikka>, lista jolla kaikki pelin palikat
     */
    public List<Palikka> getPalikat() {
        List<Palikka> palautuva = new ArrayList<Palikka>();
        for (int i = 0; i < pelipalikat.length; i++) {
            for (int j = 0; j < pelipalikat[i].length; j++) {
                if (pelipalikat[i][j] != null) {
                    palautuva.add(pelipalikat[i][j]);
                }
            }
        }
        return palautuva;
    }

    /**
     * Metodi lisää listana annetut palikat peliin oikeille paikoille. Jos pino
     * on kasvanut kattoon asti, peli päättyy.
     *
     * @param palikat lista lisättävistä palikoista
     */
    public void lisaaPalikatPeliin(List<Palikka> palikat) {
        for (Palikka palikka : palikat) {
            pelipalikat[palikka.getY()][palikka.getX()] = palikka;
            if (palikka.getY() == 0) {
                jatkuu = false;
                break;
            }
        }
    }
    
    /**
     * Alustaa kaikki pelin arvot uudelleen oletusarvoihin, jotta uusi pelisykli
     * voidaan aloittaa.
     */
    public void aloitaUusiPeli(){
        this.pause = false;
        this.jatkuu = true;
        this.pisteet = 0;
        this.taso = 1;
        this.alkutaso = 0;
        this.viive = 2000;
        this.pelipalikat = new Palikka[20][];
        for (int i = 0; i < 20; i++) {
            pelipalikat[i] = new Palikka[10];
        }
        luoUusiPutoava();
        paivitaPisteetJaTaso();
        status.setText("Paina P pysäyttääksesi pelin");
    }

    public Muodostelma getPutoava() {
        return this.putoava;
    }

    public Piirtoalusta getAlusta() {
        return this.pelialue;
    }

    public int getPisteet() {
        return this.pisteet;
    }
    
    // debuggaamista varten
    public void setPisteet(int pisteet){
        this.pisteet = pisteet;
    }
    
    public int getTaso(){
        return this.taso;
    }
    
    public Palikka[][] getPalikkaTaulukko(){
        return this.pelipalikat;
    }
    
    public void setAlusta(Piirtoalusta alusta) {
        this.pelialue = alusta;
    }
    
    public void setStatusBar(JLabel status){
        this.status = status;
        status.setText("Paina P pysäyttääksesi pelin");
    }
    
    public void setLabels(JLabel pist, JLabel taso){
        this.tasoLabel = taso;
        this.pisteLabel = pist;
        paivitaPisteetJaTaso();
    }

    /**
     * Debuggaamiseen tarkoitettu metodi, joka tulostaa palikoiden
     * koordinaatteja
     *
     * @param palikat tulostettavat palikat
     */
    public void tulosta(List<Palikka> palikat) {
        System.out.println(putoava.putoaa);
        for (Palikka palikka : palikat) {
            System.out.println("X: " + palikka.getX() + ", Y: " + palikka.getY());
        }
    }
    
    public void lisaaAI(DerpyAI ai){
        this.ai = ai;
        this.onkoAI = true;
    }
    
    public void toggleAI(){
        if (this.onkoAI){
            this.onkoAI = false;
        } else {
            this.onkoAI = true;
        }
    }
}
