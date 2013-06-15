package tetris.ui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import tetris.peli.Tetris;

/**
 * Luokka toteuttaa pelin käyttöliittymän luomalla ikkunan ja lisäämällä
 * siihen tarvittavat komponentit.
 * 
 * @author Anni
 */
public class Kayttoliittyma implements Runnable {

    private JFrame frame;
    private Tetris peli;
    private Piirtoalusta alusta;

    public Kayttoliittyma(Tetris peli) {
        this.peli = peli;
    }

    @Override
    public void run() {
        frame = new JFrame("Pegasus Tetris");
        frame.setPreferredSize(new Dimension(250, 450));
        frame.setResizable(false);

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        luoKomponentit(frame.getContentPane());

        peli.setAlusta(alusta);
        
        frame.pack();
        frame.setVisible(true);
    }

    private void luoKomponentit(Container container) {
        frame.setLayout(new BorderLayout());
        Piirtoalusta pelialue = new Piirtoalusta(peli);
        this.alusta = pelialue;
        alusta.setSize(200, 400);
        ImageIcon iid = new ImageIcon(this.getClass().getResource("taivas.png"));
        JLabel tausta = new JLabel(iid, JLabel.LEFT);
        tausta.setSize(200, 400);
        alusta.add(tausta); 
        
        frame.addKeyListener(new Nappiskuuntelija(peli));
        container.add(this.alusta, BorderLayout.CENTER);
        luoAlaPaneeli(frame.getContentPane());
        luoSivuPaneeli(frame.getContentPane());
    }
    
    /**
     * Luo ikkunan oikeassa reunassa näkyvän paneelin, joka näyttää
     * käyttäjän keräämät pisteet ja pelin senhetkisen vaikeustason
     * 
     * @param container 
     */
    private void luoSivuPaneeli(Container container){
        JPanel sivupaneeli = new JPanel();
        sivupaneeli.setLayout(new GridLayout(5, 1));
        sivupaneeli.setSize(100, 100);
        JLabel pisteetLabel = new JLabel("--Rivit--");
        JLabel pisteet = new JLabel();
        JLabel tasoLabel = new JLabel("Nopeus");
        JLabel taso = new JLabel();
        peli.setLabels(pisteet, taso);
        
        sivupaneeli.add(pisteetLabel);
        sivupaneeli.add(pisteet);
        sivupaneeli.add(tasoLabel);
        sivupaneeli.add(taso);
        container.add(sivupaneeli, BorderLayout.LINE_END);
    }
    
    /**
     * Luo ikkunan alalaidan statuspaneelin, joka kertoo, onko peli
     * pysäytetty, käynnissä, vai päättynyt ja antaa pelaajalle ohjeita
     * tilan muuttamiseksi.
     * 
     * @param container 
     */
    private void luoAlaPaneeli(Container container){
        JLabel status = new JLabel();
        status.setSize(50, 300);
        status.setVisible(true);
        container.add(status, BorderLayout.PAGE_END);
        peli.setStatusBar(status);
    }    

    public JFrame getFrame() {
        return frame;
    }
    
    public Piirtoalusta getAlusta(){
        return this.alusta;
    }
}