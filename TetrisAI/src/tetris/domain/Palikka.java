package tetris.domain;

import java.awt.Graphics;
import java.awt.Image;
import java.util.List;
import javax.swing.ImageIcon;

/**
 * @author Anni Perheentupa
 * 
 * Tämä luokka käsittelee pelin Palikka-olioiden ominaisuuksia ja toimintoja,
 * kuten niiden siirtämistä ja törmäämistä. Muodostelmille on oma luokkansa,
 * joka hoitaa muodostelmaan kuuluvien palikoiden joukkotoiminnot kutsumalla
 * Palikka-luokkaa.
 * 
 * @see Muodostelma
 */

public class Palikka {
    private int y;
    private int x;
    private Image kuva;
    
    public Palikka(int x, int y){
        this.y = y;
        this.x = x;
        ImageIcon iid = new ImageIcon(this.getClass().getResource("pilvi.png"));
        kuva = iid.getImage();
    }
    
    /**
     * Metodi tarkastaa, onko suoraan palikan alla toinen palikka
     * johon se törmää eikä voi enää liikkua.
     * 
     * @param toinen: palikka, johon törmäämistä tutkitaan
     * @return boolean: kertoo, törmääkö palikka vai ei
     */
    public boolean tormaa(Palikka toinen){
        if (toinen.getX() == this.x){
            if (toinen.getY() == this.y +1){
                return true;
            }
        }
        return false;
    }
    
    /**
     * Metodi tarkastaa, voidaanko palikkaa siirtää oikealle tai vasemmalle
     * vai törmääkö se siinä toiseen palikkaan.
     * @param toinen palikka, johon verrataan
     * @param siirtyma x-koordinaatin muutos
     * @return true jos törmätään, false jos ei
     */
    public boolean siirtymaTormaa(Palikka toinen, int siirtyma){
        if (toinen.getY() == this.y){
            if (toinen.getX() == this.x + siirtyma){
                return true;
            }
        }
        return false;
    }
    
    /**
     * Metodi, joka testaa törmäämistä listaan palikoita
     * 
     * @param lista: palikat, joihin törmäämistä tutkitaan
     * @return boolean: kertoo, törmääkö palikka vai ei
     */
    public boolean tormaa(List<Palikka> lista){
        for (int i = 0; i < lista.size(); i++){
            if (this.tormaa(lista.get(i))){
                return true;
            }
        }
        if (this.y >= 19){
            return true;
        }
        return false;
    }
    
    /**
     * Metodi testaa siirtymisessä törmäämistä listalle palikoita.
     * @param lista palikat, joihin verrataan
     * @param siirtyma x-koordinaatin muutos
     * @return true jos törmätään johonkin, false jos ei
     */
    public boolean siirtymaTormaa(List<Palikka> lista, int siirtyma){
        for (int i = 0; i < lista.size(); i++){
            if (this.siirtymaTormaa(lista.get(i), siirtyma)){
                return true;
            }
        }
        return false;
    }
    
    public int getY(){
        return this.y;
    }
    
    public int getX(){
        return this.x;
    }
    
    /**
     * Metodi "pudottaa" palikan, eli siirtää sitä yhden ruudun alaspäin.
     * 
     */
    public void putoa(){
        this.y++;
    }
    
    /**
     * Metodi siirtää palikkaa joko yhden ruudun verran oikealle tai
     * yhden ruudun verran vasemmalle.
     * 
     * @param xMuutos: joko 1 (siirrytään oikealle) tai -1 (siirrytään vasemmalle)
     */
    public void siirra(int xMuutos){
        if (this.x + xMuutos >= 0 && this.x + xMuutos < 10){
            this.x = this.x + xMuutos;
        }
    }
    
    /**
     * Metodi siirtää palikoita muodostelmien kierrossa ja
     * vaikuttaa siksi sekä y- että x-sijaintiin.
     * 
     * @param xMuutos x-koordinaatin muutos
     * @param yMuutos y-koordinaatin muutos
     */
    public void siirraKierrossa(int xMuutos, int yMuutos){
        this.y = this.y + yMuutos;
        this.x = this.x + xMuutos;
    }
    
    /**
     * Metodi piirtää olion kuvaaman palikan piirtoalustalle.
     * 
     * @param g Graphics-luokan ilmentymä
     */
    public void piirra(Graphics g){
        g.drawImage(kuva, x*20, y*20, null);
    }
    
    public Palikka kloonaa(){
        return new Palikka(this.x, this.y);
    }
    
}
