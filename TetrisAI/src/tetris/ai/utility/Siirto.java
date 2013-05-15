/*
 * Siirrot toteutetaan omina olioinaan, joista sitten valitaan pisteytyksen
 * perusteella paras. Pisteytyksen kriteerit voivat vielä vaihtua. Comparable-
 * rajapinta korvataan myös myöhemmin itsetehdyllä rakenteella, kun toteutan
 * oman kekoni siirtojen setvimistä varten.
 */
package tetris.ai.utility;

import tetris.domain.*;

public class Siirto implements Comparable<Siirto> {

    private int arvo;
    private int korkeus; // muodostelman "keskipalikan" y-sijainti
    private int sivut;
    private int rivit; // siirron muodostamien täysien rivien määrä
    private int pivotX; // muodostelman "keskipalikan" x-sijainti
    private int kierrot; // tehtyjen kiertojen määrä

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

    @Override
    public int compareTo(Siirto t) {
        if (this.arvo == t.arvo){
            if (this.rivit == t.rivit){
                if (this.sivut == t.sivut){
                    if (this.korkeus == t.korkeus){
                        return 0;
                    } else if (this.korkeus < t.korkeus){
                        return -1;
                    } else {
                        return 1;
                    }
                } else if (this.sivut > t.sivut){
                    return -1;
                } else {
                    return 1;
                }
            } else if (this.rivit > t.rivit){
                return -1;
            } else {
                return 1;
            }
        } else if (this.arvo > t.arvo){
            return -1;
        } else {
            return 1;
        }
    }
}
