/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package put.ai.games.game;

/**
 * Klasa bazowa dla algorytmów grających
 */
public abstract class Player {

    /**
     * Kolor gracza/piona
     */
    public enum Color {

        /**
         * Puste pole, niezdefiniowany kolor gracza
         */
        EMPTY,
        PLAYER1,
        PLAYER2
    };


    /**
     * Kolor tego gracza
     */
    private Color myColor;
    /**
     * Czas jednej rundy, wyrażony w milisekundach
     */
    private long time;


    /**
     * Wywoływane przez silnik gry przed pierwszym wywołaniem nextMove
     */
    public void setColor(Color color) {
        this.myColor = color;
    }


    /**
     * Kolor tego gracza, ustawiany przez grę przed początkiem rozgrywki
     */
    public Color getColor() {
        return myColor;
    }


    /**
     * Wywoływane przez silnik gry przed pierwszym wywołaniem nextMove
     */
    public void setTime(long time) {
        this.time = time;
    }


    /**
     * Informuje ile milisekund będzie mógł gracz myśleć, zanim odda grę walkowerem
     */
    public long getTime() {
        return time;
    }


    /**
     * Zwraca kolor przeciwnika koloru color
     */
    public static Color getOpponent(Color color) {
        switch (color) {
            case PLAYER1:
                return Color.PLAYER2;
            case PLAYER2:
                return Color.PLAYER1;
            case EMPTY:
            default:
                throw new IllegalArgumentException("Color must be well defined");

        }
    }


    /**
     * Zwraca nazwę gracza. Powinna być ona w formacie: <code>Imię1 Nazwisko1 Indeks1,
     * Imię2 Nazwisko2 Indeks2 (inf dodatkowe)</code>
     */
    public abstract String getName();


    /**
     * Zwraca ruch, który powinien zostac wykonany w stanie reprezentowanym przez board
     */
    public abstract Move nextMove(Board board);
}
