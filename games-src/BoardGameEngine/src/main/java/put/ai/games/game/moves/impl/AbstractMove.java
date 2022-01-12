/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package put.ai.games.game.moves.impl;

import put.ai.games.game.Move;
import put.ai.games.game.Player.Color;

public abstract class AbstractMove implements Move {

    private final Color color;


    public AbstractMove(Color color) {
        this.color = color;
        assert this.color == Color.PLAYER1 || this.color == Color.PLAYER2;
    }


    @Override
    public Color getColor() {
        return color;
    }


    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof AbstractMove)) {
            return false;
        }
        AbstractMove other = (AbstractMove) obj;
        return other.color == this.color;
    }


    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + (this.color != null ? this.color.hashCode() : 0);
        return hash;
    }
}
