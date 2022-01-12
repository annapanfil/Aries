/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package put.ai.games.game.moves.impl;

import put.ai.games.game.Player.Color;
import put.ai.games.game.moves.PlaceMove;

public class PlaceMoveImpl extends AbstractMove implements PlaceMove {

    private final int x, y;


    public PlaceMoveImpl(int x, int y, Color color) {
        super(color);
        this.x = x;
        this.y = y;
        assert this.x >= 0 && this.y >= 0;
    }


    @Override
    public int getX() {
        return x;
    }


    @Override
    public int getY() {
        return y;
    }


    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof PlaceMoveImpl)) {
            return false;
        }
        PlaceMoveImpl other = (PlaceMoveImpl) obj;
        return super.equals(obj) && other.x == this.x && other.y == this.y;
    }


    @Override
    public int hashCode() {
        int hash = super.hashCode();
        hash = 79 * hash + this.x;
        hash = 79 * hash + this.y;
        return hash;
    }
}
