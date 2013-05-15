package tetris;

import javax.swing.SwingUtilities;
import tetris.peli.Tetris;
import tetris.ui.Kayttoliittyma;
import tetris.ai.*;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        
        Tetris tetris = new Tetris();
        Kayttoliittyma kayttoliittyma = new Kayttoliittyma(tetris);
        SwingUtilities.invokeLater(kayttoliittyma);
        DerpyAI ai = new DerpyAI(tetris);
        while (true){
            tetris.peliSykli();
        }
    }
}
