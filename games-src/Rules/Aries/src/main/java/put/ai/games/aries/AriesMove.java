package put.ai.games.aries;

import java.util.Objects;
import put.ai.games.game.Player.Color;
import put.ai.games.game.moves.MoveMove;

public class AriesMove implements MoveMove {

    protected final Color color;
    protected final int srcX, srcY, dstX, dstY;
    //because we have to evaluate the move anyway
    //we store the current evaluation result
    protected final BoardState srcState, dstState;


    public AriesMove(Color color, int srcX, int srcY, int dstX, int dstY, BoardState srcState, BoardState dstState) {
        this.color = color;

        this.srcX = srcX;
        this.srcY = srcY;
        this.dstX = dstX;
        this.dstY = dstY;

        this.srcState = srcState;
        this.dstState = dstState;
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
    public int getSrcX() {
        return srcX;
    }


    @Override
    public int getSrcY() {
        return srcY;
    }


    @Override
    public Color getColor() {
        return color;
    }


    /*package*/BoardState getSrcState() {
        return srcState;
    }


    /*package*/BoardState getDstState() {
        return dstState;
    }


    /*package*/boolean isHorizontal() {
        return srcY == dstY;
    }


    /*package*/boolean isVertical() {
        return srcX == dstX;
    }


    /*package*/boolean isDiagonal() {
        return !isHorizontal() && !isVertical();
    }


    @Override
    public String toString() {
        return String.format("(%d,%d)->(%d,%d)", srcX, srcY, dstX, dstY);
    }


    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.color);
        hash = 37 * hash + this.srcX;
        hash = 37 * hash + this.srcY;
        hash = 37 * hash + this.dstX;
        hash = 37 * hash + this.dstY;
        if (srcState != null) {
            hash = 37 * hash + this.srcState.hashCode();
        }
        if (dstState != null) {
            hash = 37 * hash + this.dstState.hashCode();
        }
        return hash;
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AriesMove other = (AriesMove) obj;
        if (this.color != other.color) {
            return false;
        }
        if (this.srcX != other.srcX) {
            return false;
        }
        if (this.srcY != other.srcY) {
            return false;
        }
        if (this.dstX != other.dstX) {
            return false;
        }
        if (this.dstY != other.dstY) {
            return false;
        }
        if (!Objects.equals(this.srcState, other.srcState)) {
            return false;
        }
        return Objects.equals(this.dstState, other.dstState);
    }

}
