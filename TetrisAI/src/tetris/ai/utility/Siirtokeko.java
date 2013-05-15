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
    
    public void lisaa(Siirto siirto){
        this.tempKeko.add(siirto);
    }
    
    public Siirto pienin(){
        return this.tempKeko.peek();
    }
    
    public void tyhjenna(){
        this.tempKeko.clear();
    }
}
