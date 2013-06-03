/*
 * Siirrot toteutetaan omina olioinaan, joista sitten valitaan pisteytyksen
 * perusteella paras. Pisteytyksen kriteerit voivat vielä vaihtua. Comparable-
 * rajapinta korvataan myös myöhemmin itsetehdyllä rakenteella, kun toteutan
 * oman kekoni siirtojen setvimistä varten.
 */
package tetris.ai.utility;

import tetris.domain.*;

public class Siirto{

    private int arvo;
    private int korkeus; // muodostelman "keskipalikan" y-sijainti
    private int sivut;
    private int rivit; // siirron muodostamien täysien rivien määrä
    private int pivotX; // muodostelman "keskipalikan" x-sijainti
    private int kierrot; // tehtyjen kiertojen määrä

    /**
     * Konstruktori luo uuden Siirron ja laskee sille arvon annettujen parametrien
     * perusteella. Tällä hetkellä arvo lasketaan ottamalla ensin seiniä/palikoita
     * koskevat sivut ja miinustamalla niistä korkeus, minkä jälkeen tulokseen lisätään
     * täysien rivien määrä kymmenkertaisena, sillä niihin halutaan priorisoida.
     * Kaava saattaa vielä muuttua paljonkin.
     * @param 
     */
    public Siirto(int korkeus, int sivut, int rivit, int x, int kierrot) {
        this.korkeus = korkeus;
        this.sivut = sivut;
        this.rivit = rivit;
        this.pivotX = x;
        this.kierrot = kierrot;
        this.arvo = sivut - korkeus + (rivit*10);
    }
    
    public int getArvo(){
        return this.arvo;
    }

    
    /**
     * 
     * @param toinen
     * @return 
     */
    public boolean suurempiKuin(Siirto toinen){
        if (this.arvo > toinen.arvo){
            return true;
        } else if (this.arvo < toinen.arvo){
            return false;
        } else {
            // jos siirroilla on sama arvo, priorisoidaan syntyneiden rivien mukaan
            if (this.rivit > toinen.rivit){
                return true;
            } else {
                return false;
            }
        }
    }
    
    /**
     * Palauttaa siirron x-sijainnin
     * @return int muodostelman pivot-palikan x-koordinaatti
     */
    public int getX(){
        return this.pivotX;
    }
    
    /**
     * Palauttaa siirron kierrot
     * @return int muodostelman kiertojen määrä
     */
    public int getKierrot(){
        return this.kierrot;
    }
    
    /**
     * Testaukseen tarkoitettu toString, joka kertoo siirron ominaisuuksista
     * sitä tulostettaessa
     * @return String Siirron tekstiesitys
     */
    @Override
    public String toString(){
        return "Siirto: "+"x-sijainti: "+this.pivotX+" kierrot: "
                +this.kierrot+" korkeus: "+this.korkeus;
    }
}
