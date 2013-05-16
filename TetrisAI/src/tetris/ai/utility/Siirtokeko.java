/*
 * Tänne tulee tekoälyä varten rakennettu keko, joka säilöö Siirto-olioita,
 * mutta toistaiseksi luokka on vain placeholder
 */

package tetris.ai.utility;

import java.util.PriorityQueue;

public class Siirtokeko {
    private PriorityQueue<Siirto> tempKeko;
    
    public Siirtokeko(){
        this.tempKeko = new PriorityQueue<Siirto>();
    }
    
    /*
     * Metodi lisää kekoon uuden siirto-olion ja varmistaa, että
     * keko-ehto on lisäyksen jälkeen voimassa
     */
    public void lisaa(Siirto siirto){
        this.tempKeko.add(siirto);
    }
    
    /*
     * Metodi palauttaa keon suurimman siirron, ts. sen, jolla on
     * paras kokonaispistemäärä. Huom: metodi EI poista oliota keosta!
     */
    public Siirto suurin(){
        return this.tempKeko.peek();
    }
    
    /*
     * Metodi tyhjentää keon kokonaan seuraavan siirron laskemista varten.
     */
    public void tyhjenna(){
        this.tempKeko.clear();
    }
    
    /*
     * Metodi palauttaa keossa sillä hetkellä olevien siirtojen määrän.
     */
    public int koko(){
        return this.tempKeko.size();
    }
}
