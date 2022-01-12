/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package put.ai.games.game.moves.impl;

import put.ai.games.game.Player.Color;
import put.ai.games.game.moves.MoveMove;

public class MoveMoveImpl extends AbstractMove implements MoveMove {

    private final int srcX, srcY, dstX, dstY;


    public MoveMoveImpl(int srcX, int srcY, int dstX, int dstY, Color color) {
        super(color);
        this.srcX = srcX;
        this.srcY = srcY;
        this.dstX = dstX;
        this.dstY = dstY;
        assert this.srcX >= 0 && this.srcY >= 0;
        assert this.dstX >= 0 && this.dstY >= 0;
    }


    @Override
    public int getSrcX() {
        return srcX;
    }


    @Override
    public int getSrcY() {
        return srcY;
    }


    @Override
    public int getDstX() {
        return dstX;
    }


    @Override
    public int getDstY() {
        return dstY;
    }


    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof MoveMoveImpl)) {
            return false;
        }
        MoveMoveImpl other = (MoveMoveImpl) obj;
        return super.equals(obj) && other.srcX == this.srcX && other.srcY == this.srcY && other.dstX == this.dstX
                && other.dstY == this.dstY;
    }


    @Override
    public int hashCode() {
        int hash = super.hashCode();
        hash = 71 * hash + this.srcX;
        hash = 71 * hash + this.srcY;
        hash = 71 * hash + this.dstX;
        hash = 71 * hash + this.dstY;
        return hash;
    }
}
