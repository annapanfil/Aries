package put.ai.games.aries;

import java.util.Arrays;
import put.ai.games.game.Player.Color;

//TODO moveUp?
//we will treat this class as immutable,
//but we won't make it immutable per se
//for optimisation purposes
//-- state array will be accesible from outside directly
final class BoardState {

    protected final Color[][] state;
    protected final int hash;
    protected final int count1;
    protected final int count2;


    protected BoardState(Color[][] state, int hash, int count1, int count2) {
        assert state != null;
        this.state = state;
        this.hash = hash;
        this.count1 = count1;
        this.count2 = count2;
    }


    @Override
    public int hashCode() {
        return hash;
    }


    public int getCount1() {
        return count1;
    }


    public int getCount2() {
        return count2;
    }


    @Override
    public boolean equals(Object otherObj) {
        if (otherObj == this) {
            return true;
        }

        //this also checks for null
        if (!(otherObj instanceof BoardState)) {
            return false;
        }

        BoardState otherBoard = (BoardState) otherObj;

        if (otherBoard.hashCode() != this.hashCode()) {
            return false;
        }

        return Arrays.deepEquals(otherBoard.state, this.state);
    }

}
