/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package put.ai.games.game;

import java.util.List;
import put.ai.games.game.Player.Color;

/**
 * Stan planszy
 */
public interface Board extends Cloneable {

    /**
     * Zrób ruch move
     */
    void doMove(Move move);


    /**
     * Zwraca listę wszystkich ruchów dopuszczalnych w danym układzie planszy Dla wszystkich tych ruchów zachodzi:
     * <code>move.getColor()==color</code>.
     */
    List<Move> getMovesFor(Color color);


    /**
     * Zwraca rozmiar planszy
     */
    int getSize();


    /**
     * Zwraca kolor pionka zajmującego pole o współrzędnych (x,y). Dla niepoprawnych współrzędnych zwracana jest wartość
     * odpowiadająca pustemu polu
     */
    Color getState(int x, int y);


    /**
     * Wycofuje ruch move, przywracając planszę do stanu sprzed tego ruchu
     */
    void undoMove(Move move);


    /**
     * Tworzy dokładną, głęboką kopię tej planszy, która nie współdzieli z nią żadnych obiektów
     */
    Board clone();


    /**
     * Gracz zwyciężający w danym układzie planszy zakladajac, ze najblizszy ruch ma zrobic currentPlayer. null oznacza
     * brak zdefiniowanego zwycięzcy (rozgrywka w toku), a EMPTY remis
     */
    Color getWinner(Color currentPlayer);
}
