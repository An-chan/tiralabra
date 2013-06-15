/*
 * Tänne tulee tekoälyä varten rakennettu keko, joka säilöö Siirto-olioita,
 * mutta toistaiseksi luokka on vain placeholder
 */

package tetris.ai.utility;

import java.util.PriorityQueue;

public class Siirtokeko {
    private Siirto[] keko;
    private int koko;
    
    /**
     * Konstruktori luo uuden keon, jonka koko on nolla ja jossa on aluksi
     * tilaa kymmenelle alkiolle.
     */
    public Siirtokeko(){
        this.keko = new Siirto[10];
        this.koko = 0;
    }
    
    /**
     * Metodi lisää kekoon uuden siirto-olion ja varmistaa, että
     * kekoehto on lisäyksen jälkeen edelleen voimassa. Mikäli keossa
     * ei enää ole tilaa uusille siirroille, kasvatetaan sen kokoa
     * kaksinkertaiseksi ennen lisäystä.
     * @param siirto - kekoon lisättävä siirto-olio
     */
    public void lisaa(Siirto siirto){
        this.koko++;
        if ( koko == 1){
            keko[0] = siirto;
            return;
        }
        if ( keko.length == koko){
            tuplaaKoko();
        }
        int i = this.koko-1;
        while (i > 0 && !keko[vanhempi(i)].suurempiKuin(siirto)){
            keko[i] = keko[vanhempi(i)];
            i = vanhempi(i);
        }
        keko[i] = siirto;
    }
    
    /**
     * Metodi palauttaa keon suurimman siirron, ts. sen, jolla on
     * paras kokonaispistemäärä. Huom: metodi EI poista oliota keosta!
     * @return Siirto - keon huipulla oleva siirto
     */
    public Siirto suurin(){
        if (koko == 0){
            return null;
        }
        return keko[0];
    }
    
    /**
     * Metodi tyhjentää keon kokonaan seuraavan siirron laskemista varten.
     */
    public void tyhjenna(){
        for (int i = 0; i < this.koko; i++){
            keko[i] = null;
        }
        this.koko = 0;
    }
    
    /**
     * Metodi palauttaa keossa sillä hetkellä olevien siirtojen määrän, ts.
     * keon senhetkisen koon.
     * @return int - keon koko
     */
    public int koko(){
        return this.koko;
    }
    
    /**
     * Palauttaa kekoehdon voimaan mikäli se on jostakin muutoksesta rikkoontunut.
     * Metodi on rekursiivinen.
     * @param i - indeksi, josta kekoehdon palauttaminen aloitetaan
     */
    private void heapify(int i){
        int vasenLapsi = vasenLapsi(i);
        int oikeaLapsi = oikeaLapsi(i);
        if (oikeaLapsi < this.koko){
            Siirto suurempi = null;
            int suurempiInd = 0;
            if (keko[oikeaLapsi].suurempiKuin(keko[vasenLapsi])){
                suurempi = keko[oikeaLapsi];
                suurempiInd = oikeaLapsi;
            } else {
                suurempi = keko[vasenLapsi];
                suurempiInd = vasenLapsi;
            }
            if (suurempi.suurempiKuin(keko[i])){
                keko[suurempiInd] = keko[i];
                keko[i] = suurempi;
                heapify(suurempiInd);
            }
        } else if (vasenLapsi == this.koko && keko[vasenLapsi].suurempiKuin(keko[i])){
            Siirto temp = keko[vasenLapsi];
            keko[vasenLapsi] = keko[i];
            keko[i] = temp;
        }
    }
    
    /**
     * Metodi poistaa keon parhaan siirron keosta ja palauttaa sen
     * paluuarvona.
     * @return Siirto - keon huipulla oleva siirto, joka poistettiin
     */
    public Siirto poistaSuurin(){
        Siirto suurin = keko[0];
        this.koko--;
        keko[0] = keko[this.koko];
        heapify(0);
        return suurin;
    }
    
    /**
     * Lisäyksessä käytettävä metodi, joka tuplaa keon koon jos se on täysi
     */
    private void tuplaaKoko(){
        Siirto[] uusi = new Siirto[keko.length*2];
        for (int i = 0; i < keko.length; i++){
            uusi[i] = keko[i];
        }
        this.keko = uusi;
    }
    
    /**
     * Vasemman lapsen indeksin laskemiseen tarkoitettu metodi
     * @param i - vanhemman indeksi
     * @return int - halutun lapsen indeksi
     */
    private int vasenLapsi(int i){
        return i*2;
    }
    
    /**
     * Oikean lapsen indeksin laskemiseen tarkoitettu metodi
     * @param i - vanhemman indeksi
     * @return int - halutun lapsen indeksi
     */
    private int oikeaLapsi(int i){
        return i*2+1;
    }
    
    /**
     * Vanhemman indeksin laskemiseen tarkoitettu metodi
     * @param i - indeksi, jonka vanhempi halutaan löytää
     * @return int - vanhemman indeksi
     */
    private int vanhempi(int i){
        return i/2;
    }
}
