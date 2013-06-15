package tetris.ui;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import tetris.peli.Tetris;

/**
 * @author Anni
 *
 * Näppiskuuntelija-luokka vastaanottaa käyttäjän syöttämät näppäinkomennot ja
 * tulkitsee ne ohjelman sisäisiksi toiminnoiksi. Se kommunikoi Tetris-luokan
 * kanssa, mutta käyttää oikeastaan lähinnä Muodostelmasta löytyviä metodeita.
 *
 * @see Muodostelma
 */
public class Nappiskuuntelija extends KeyAdapter {

    private Tetris peli;

    public Nappiskuuntelija(Tetris tetris) {
        this.peli = tetris;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keycode = e.getKeyCode();

        switch (keycode) {
            case KeyEvent.VK_LEFT:
                if (peli.onkoPause()) {
                    break;
                }
                peli.getPutoava().siirra(-1);
                break;
            case KeyEvent.VK_RIGHT:
                if (peli.onkoPause()) {
                    break;
                }
                peli.getPutoava().siirra(1);
                break;
            case KeyEvent.VK_DOWN:
                if (peli.onkoPause()) {
                    break;
                }
                peli.getPutoava().putoa();
                break;
            case KeyEvent.VK_UP:
                if (peli.onkoPause()) {
                    break;
                }
                peli.getPutoava().kierra();
                break;
            case KeyEvent.VK_SPACE:
                if (peli.onkoPause()) {
                    break;
                }
                while (peli.getPutoava().putoaa) {
                    peli.getPutoava().putoa();
                }
                break;
            case KeyEvent.VK_P:
                peli.pausePaallePois();
                break;
            case KeyEvent.VK_ENTER:
                if (!peli.getJatkuu()) {
                    peli.aloitaUusiPeli();
                }
                break;
            case KeyEvent.VK_PAGE_UP:
                peli.nostaVaikeustasoa();
                break;
            case KeyEvent.VK_PAGE_DOWN:
                peli.laskeVaikeustasoa();
                break;
            case KeyEvent.VK_ALT:
                peli.toggleAI();
                break;
        }
        peli.getAlusta().repaint();
    }
}