/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package put.ai.games.game;

import java.util.Arrays;
import put.ai.games.game.Player.Color;

public abstract class TypicalBoard implements Board {

    protected Color[][] state;


    public TypicalBoard(int boardSize) {
        state = new Color[boardSize][boardSize];
        for (int i = 0; i < boardSize; ++i) {
            for (int j = 0; j < boardSize; ++j) {
                state[i][j] = Color.EMPTY;
            }
        }
    }


    protected TypicalBoard(TypicalBoard other) {
        state = new Color[other.state.length][other.state.length];
        for (int i = 0; i < state.length; ++i) {
            for (int j = 0; j < state.length; ++j) {
                state[i][j] = other.state[i][j];
            }
        }
    }


    @Override
    public int getSize() {
        return state.length;
    }


    protected boolean isValid(int x, int y) {
        return !(x < 0 || y < 0 || x >= state.length || y >= state.length);
    }


    @Override
    public Color getState(int x, int y) {
        if (!isValid(x, y)) {
            return Color.EMPTY;
        }
        return state[x][y];
    }


    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof TypicalBoard)) {
            return false;
        }
        TypicalBoard other = (TypicalBoard) obj;
        return Arrays.deepEquals(this.state, other.state);
    }


    @Override
    public int hashCode() {
        return Arrays.deepHashCode(state);
    }


    @Override
    public String toString() {
        String result = "";
        for (int y = 0; y < state.length; ++y) {
            for (int x = 0; x < state.length; ++x) {
                switch (state[x][y]) {
                    case PLAYER1:
                        result += "1";
                        break;
                    case PLAYER2:
                        result += "2";
                        break;
                    case EMPTY:
                        result += "_";
                        break;
                }
            }
            result += "\n";
        }
        return result;
    }


    @Override
    public abstract TypicalBoard clone();


    protected boolean hasCell(Color c) {
        for (int i = 0; i < state.length; ++i) {
            for (int j = 0; j < state.length; ++j) {
                if (state[i][j] == c) {
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * Czy gracz koloru color może wykonać jescze jakiś ruch
     */
    protected abstract boolean canMove(Color color);


    @Override
    public Color getWinner(Color currentPlayer) {
        boolean wins1 = !canMove(Color.PLAYER2);
        boolean wins2 = !canMove(Color.PLAYER1);
        if (wins1 && wins2) {
            return Color.EMPTY;
        }
        if (wins1 && !wins2) {
            return Color.PLAYER1;
        }
        if (!wins1 && wins2) {
            return Color.PLAYER2;
        }
        return null;
    }


    public int countStones(Color player) {
        int count = 0;

        for (int x = 0; x < getSize(); x++) {
            for (int y = 0; y < getSize(); y++) {
                if (state[x][y] == player) {
                    count++;
                }
            }
        }

        return count;
    }

}
