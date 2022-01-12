/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package put.ai.games.game.moves.impl;

import put.ai.games.game.Player.Color;
import put.ai.games.game.moves.ShiftMove;

public class ShiftMoveImpl extends AbstractMove implements ShiftMove {

    private final Direction direction;
    private final int x;


    public ShiftMoveImpl(Direction direction, int x, Color color) {
        super(color);
        this.direction = direction;
        this.x = x;
    }


    @Override
    public Direction getDirection() {
        return direction;
    }


    @Override
    public int getX() {
        return x;
    }


    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ShiftMoveImpl)) {
            return false;
        }
        ShiftMoveImpl other = (ShiftMoveImpl) obj;
        return super.equals(obj) && other.direction == this.direction && other.x == this.x;
    }


    @Override
    public int hashCode() {
        int hash = super.hashCode();
        hash = 37 * hash + (this.direction != null ? this.direction.hashCode() : 0);
        hash = 37 * hash + this.x;
        return hash;
    }
}
