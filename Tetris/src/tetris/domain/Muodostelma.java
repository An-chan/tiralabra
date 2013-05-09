
package tetris.domain;

import java.util.ArrayList;
import java.util.List;
import tetris.peli.Tetris;
/**
 * @author Anni Perheentupa
 * 
 * Tämä luokka käsittelee Palikka-olioista muodostuvia muodostelmia ja
 * suorittaa niille yhteisiä operaatioita, kuten siirtämisen, kiertämisen
 * ja putoamisen.
 * 
 * @see Palikka
 */
public class Muodostelma {
    private Muoto muoto;
    private ArrayList<Palikka> palikat;
    public boolean putoaa;
    private Tetris peli;
    
    public Muodostelma(Muoto muoto, Tetris peli){
        this.palikat = new ArrayList<Palikka>();
        this.muoto = muoto;
        this.putoaa = true;
        this.peli = peli;
        luoPalikat();
    }
    
    public void luoPalikat(){
        // palikkamuodostelman keskikohta luodaan ruudun ylälaidan keskelle, ts. kohtaan (0, 5)
        // x-koordinaatti kasvaa alaspäin, y-koordinaatti kasvaa oikealle
        switch (muoto){
            case I:
                palikat.add(new Palikka(3, 0));
                palikat.add(new Palikka(4, 0));
                palikat.add(new Palikka(5, 0));
                palikat.add(new Palikka(6, 0));
                break;
            case L:
                palikat.add(new Palikka(4, 0));
                palikat.add(new Palikka(5, 0));
                palikat.add(new Palikka(6, 0));
                palikat.add(new Palikka(4, 1));
                break;
            case peiliL:
                palikat.add(new Palikka(4, 0));
                palikat.add(new Palikka(5, 0));
                palikat.add(new Palikka(6, 0));
                palikat.add(new Palikka(6, 1));
                break;
            case nelio:
                palikat.add(new Palikka(4, 0));
                palikat.add(new Palikka(5, 0));
                palikat.add(new Palikka(4, 1));
                palikat.add(new Palikka(5, 1));
                break;
            case S:
                palikat.add(new Palikka(4, 1));
                palikat.add(new Palikka(5, 0));
                palikat.add(new Palikka(6, 0));
                palikat.add(new Palikka(5, 1));
                break;
            case peiliS:
                palikat.add(new Palikka(3, 0));
                palikat.add(new Palikka(4, 0));
                palikat.add(new Palikka(4, 1));
                palikat.add(new Palikka(5, 1));
                break;
            case T:
                palikat.add(new Palikka(4, 0));
                palikat.add(new Palikka(5, 0));
                palikat.add(new Palikka(6, 0));
                palikat.add(new Palikka(5, 1));
                break;
        }
    }
    
    /**
     * Palikkamuodostelmaa siirretään yhden ruudun verran alaspäin.
     */
    public void putoa(){
        tormays();
        if (putoaa){
            for (Palikka palikka : palikat) {
                palikka.putoa();
            }
        }
    }
    
    /**
     * Metodi siirtää koko muodostelmaa joko oikealle tai vasemmalle
     * annetun syötteen mukaisesti, kuitenkin vain ruutu kerrallaan.
     * HUOM! Seiniin törmäys vielä keskeneräisesti käsitelty!
     * 
     * @param ymuutos sijainnin muutos, 1 jos siirrytään oikealle, -1 vasemmalle
     */
    public void siirra(int xMuutos){
        if (peli.onkoPause()){
            return;
        }
        if (xMuutos < 0){
            int vas = etsiVasenX();
            if (vas + xMuutos < 0){
                return;
            }
        }
        if (xMuutos > 0){
            int oik = etsiOikeaX();
            if (oik + xMuutos > 9){
                return;
            }
        }
        if (siirtoTormays(xMuutos)){
            return;
        }
        for (Palikka palikka : palikat){
            palikka.siirra(xMuutos);
        }
    }
    
    
    /** 
     *  Muodostelmaa kierretään aina oikealle, käyttäen palikkaa nro 1 
     *  (eli toista palikkaa) keskipisteenä.
     * */
    public void kierra(){
        if (peli.onkoPause()){
            return;
        }
        int keskiY = palikat.get(1).getY();
        int keskiX = palikat.get(1).getX();
        if (this.muoto == Muoto.nelio){
            return;             // nelio-muodostelma on aina samanlainen
        }
        if (this.muoto == Muoto.I){
            if (keskiX-1 < 0 || keskiY-1 < 0 || keskiY+2 > 19 || keskiX+2 > 10){
                return;
            } 
            this.Ikierto();
            return;
        }
        if (tarkastaKierto()){
            return;
        }
        for (int i = 0; i < palikat.size(); i++){
            if (i == 1){
                continue;
            }
            Palikka tama = palikat.get(i);
            if (tama.getX() == keskiX && tama.getY() == keskiY-1){         //keskikohdan yläpuolella
                tama.siirraKierrossa(1, 1);
            } else if (tama.getX() == keskiX && tama.getY() == keskiY+1){ //keskikohdan alapuolella
                tama.siirraKierrossa(-1, -1);
            } else if (tama.getX() == keskiX+1 && tama.getY() == keskiY){ //keskikohdan oikealla
                tama.siirraKierrossa(-1, 1);
            } else if (tama.getX() == keskiX-1 && tama.getY() == keskiY){ //keskikohdan vasemmalla
                tama.siirraKierrossa(1, -1);
            } else if (tama.getX() == keskiX+1 && tama.getY() == keskiY-1){ //keskikohdan yläoikealla
                tama.siirraKierrossa(0, 2);
            }  else if (tama.getX() == keskiX-1 && tama.getY() == keskiY-1){ //keskikohdan ylävasemmalla
                tama.siirraKierrossa(2, 0);
            }  else if (tama.getX() == keskiX+1 && tama.getY() == keskiY+1){ //keskikohdan alaoikealla
                tama.siirraKierrossa(-2, 0);
            }  else if (tama.getX() == keskiX-1 && tama.getY() == keskiY+1){ //keskikohdan alavasemmalla
                tama.siirraKierrossa(0, -2);
            }
        }
    }
    
    /**
     * Metodi tarkastaa, voidaanko kierrossa tarvittavat siirto-operaatiot
     * tehdä osumatta toisiin palikoihin tai menemättä läpi seinistä.
     * 
     * @return true jos törmätään, false jos ei
     */
    public boolean tarkastaKierto(){
        Palikka[][] pelipalikat = peli.getPalikkaTaulukko();
        
        int keskiY = palikat.get(1).getY();
        int keskiX = palikat.get(1).getX();
        
        for (int i = 0; i < palikat.size(); i++){
            if (i == 1){
                continue;
            }
            Palikka tama = palikat.get(i);
            int tamaX = tama.getX();
            int tamaY = tama.getY();
            if (tamaX == keskiX && tamaY == keskiY-1){
                if (tamaX +1 > 9 || tamaY + 1 > 19){
                    return true;
                } else if (pelipalikat[tamaY+1][tamaX+1] != null){
                    return true;
                }
            } else if (tamaX == keskiX && tamaY == keskiY+1){
                if (tamaX -1 < 0 || tamaY -1 < 0){
                    return true;
                } else if (pelipalikat[tamaY-1][tamaX-1] != null){
                    return true;
                }
            } else if (tamaX == keskiX+1 && tamaY == keskiY){
                if (tamaX -1 < 0 || tamaY +1 > 19){
                    return true;
                } else if (pelipalikat[tamaY+1][tamaX-1] != null){
                    return true;
                }
            } else if (tama.getX() == keskiX-1 && tama.getY() == keskiY){ //keskikohdan vasemmalla
                if (tamaX +1 > 9 || tamaY -1 < 0){
                    return true;
                } else if (pelipalikat[tamaY-1][tamaX+1] != null){
                    return true;
                }
            } else if (tama.getX() == keskiX+1 && tama.getY() == keskiY-1){ //keskikohdan yläoikealla
                if (tamaY +2 > 19){
                    return true;
                } else if (pelipalikat[tamaY+2][tamaX] != null){
                    return true;
                }
            }  else if (tama.getX() == keskiX-1 && tama.getY() == keskiY-1){ //keskikohdan ylävasemmalla
                if (tamaX +2 > 9){
                    return true;
                } else if (pelipalikat[tamaY][tamaX+2] != null){
                    return true;
                }
            }  else if (tama.getX() == keskiX+1 && tama.getY() == keskiY+1){ //keskikohdan alaoikealla
                if (tamaX -2 < 0){
                    return true;
                } else if (pelipalikat[tamaY][tamaX-2] != null){
                    return true;
                }
            }  else if (tama.getX() == keskiX-1 && tama.getY() == keskiY+1){ //keskikohdan alavasemmalla
                if (tamaY -2 < 0){
                    return true;
                } else if (pelipalikat[tamaY-2][tamaX] != null){
                    return true;
                }
            }
        }
        return false;
    }
    
    public void Ikierto(){
        int keskiY = palikat.get(1).getY();
        int keskiX = palikat.get(1).getX();
        if (palikat.get(0).getY() == keskiY){
            palikat.get(0).siirraKierrossa(1, -1);
            palikat.get(2).siirraKierrossa(-1, 1);
            palikat.get(3).siirraKierrossa(-2, 2);
        } else if (palikat.get(0).getX() == keskiX) {
            palikat.get(0).siirraKierrossa(-1, 1);
            palikat.get(2).siirraKierrossa(1, -1);
            palikat.get(3).siirraKierrossa(2, -2);
        }
    }
    
    /**
     * Metodi kutsuu palikoiden törmäysmetodia ja tarkastaa, osuuko joku muodostelman
     * palikoista allaolevaan palikkaan tai ruudun alalaitaan. Mikäli mikään palikoista
     * osuu johonkin, muodostelma lakkaa putoamasta.
     */
    public void tormays(){
        this.tormays(peli.getPalikat());
    }
    
    public void tormays(List<Palikka> palikkalista){
        for (Palikka palikka : palikat) {
            if(palikka.tormaa(palikkalista)){
                this.putoaa=false;
                return;
            }
        }
    }
    
    /**
     * Metodi tarkastaa törmäykset tilanteissa, joissa muodostelmaa siirretään oikealle
     * tai vasemmalle.
     * @param xMuutos x-koordinaatin haluttu muutos
     * @return true jos törmää, false jos ei
     */
    public boolean siirtoTormays(int xMuutos){
        Palikka[][] pelipalikat = peli.getPalikkaTaulukko();
        for (Palikka palikka : this.palikat) {
            if (pelipalikat[palikka.getY()][palikka.getX()+xMuutos] != null){
                return true;
            }
        }
        return false;
    }
    
    public List<Palikka> getPalikat(){
        return this.palikat;
    }
    
    /**
     * Metodi on siirtämistä varten tarkoitettu tarkistus, jossa katsotaan missä
     * muodostelman kaikkein vasemmanpuoleisin palikka sijaitsee.
     * 
     * @return int vasemmanpuoleisimman palikan x-koordinaatti
     */
    public int etsiVasenX(){
        int vasX = 9;
        for (Palikka palikka : palikat) {
            if (palikka.getX() < vasX){
                vasX = palikka.getX();
            }
        }
        return vasX;
    }
    
    /**
     * Metodi etsii muodostelman oikeanpuoleisimman
     * palikan x-koordinaatin muodostelman siirtämistä varten.
     * @return int oikeanpuoleisimman palikan x-koordinaatti
     */
    public int etsiOikeaX(){
        int oikX = 0;
        for (Palikka palikka : palikat) {
            if (palikka.getX() > oikX){
                oikX = palikka.getX();
            }
        }
        return oikX;
    }

}
