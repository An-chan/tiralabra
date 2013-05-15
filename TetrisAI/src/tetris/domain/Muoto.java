
package tetris.domain;

/**
 * @author Anni
 * 
 * Luokka sisältää kaikki mahdolliset tetrominojen muodot. Tällä hetkellä ne ovat
 * kaikki perinteisiä neljäpalikkaisia muotoja, mutta tulevaisuudessa peliin voidaan
 * lisätä esim. uusia vaikeustasoja, jolloin voidaan käyttää muitakin muotoja.
 * 
 * @see Muodostelma
 */
public enum Muoto {
    L, peiliL, T, nelio, I, S, peiliS;
}
