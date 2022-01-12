package put.ai.games.aries;

import put.ai.games.game.Player.Color;
import java.util.EnumMap;
import java.util.Random;

class BoardStateBuilder {

    protected Color[][] state;
    protected int hash;
    protected int count1;
    protected int count2;
    protected static EnumMap<Color, Integer>[][] hashKeyTable;
    protected static int hashKeyTableSize;


    protected BoardStateBuilder(Color[][] state, int hash, int count1, int count2) {
        assert state != null;
        this.state = state;
        this.hash = hash;
        this.count1 = count1;
        this.count2 = count2;
    }


    static public BoardStateBuilder startFromBoardState(BoardState boardState) {
        int size = boardState.state.length;

        Color[][] state = new Color[size][size];
        for (int x = 0; x < size; ++x) {
            for (int y = 0; y < size; ++y) {
                state[x][y] = boardState.state[x][y];
            }
        }
        return new BoardStateBuilder(state, boardState.hash, boardState.count1, boardState.count2);
    }


    static public BoardStateBuilder startFromInitial(int size) {
        //we prevent uneven board size in AriesBoard constructor
        int half = size / 2;
        Color[][] state = new Color[size][size];

        for (int y = 0; y < size; ++y) {
            for (int x = 0; x < size; ++x) {
                if ((y < half && x < half)) {
                    state[x][y] = Color.PLAYER1;
                } else if (y >= half && x >= half) {
                    state[x][y] = Color.PLAYER2;
                } else {
                    state[x][y] = Color.EMPTY;
                }
            }
        }
        return startFromRawState(state);
    }


    static public BoardStateBuilder startFromEmpty(int size) {
        Color[][] state = new Color[size][size];

        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                state[i][j] = Color.EMPTY;
            }
        }
        return startFromRawState(state);
    }


    static public BoardStateBuilder startFromRawState(Color[][] state) {
        int count1 = 0;
        int count2 = 0;
        int hash = 0;
        int size = state.length;

        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                if (state[i][j] == Color.PLAYER1) {
                    ++count1;
                } else if (state[i][j] == Color.PLAYER2) {
                    ++count2;
                }
                hash ^= getHashKeyValue(size, i, j, state[i][j]);
            }
        }
        return new BoardStateBuilder(state, hash, count1, count2);
    }


    static public BoardStateBuilder startFromString(String str) {
        str = str.replace("\n", "");
        str = str.replace(" ", "");

        int noFields = str.length();

        int size = (int) Math.sqrt(noFields);
        if ((size * size) != noFields) {
            throw new IllegalArgumentException(String.format(
                "Building from string - improper number of fields for board: %s", noFields));
        }

        Color[][] state = new Color[size][size];

        int i = 0;
        char[] chars = str.toCharArray();
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                if (chars[i] == '1') {
                    state[x][y] = Color.PLAYER1;
                } else if (chars[i] == '2') {
                    state[x][y] = Color.PLAYER2;
                } else if (chars[i] == '_') {
                    state[x][y] = Color.EMPTY;
                } else {
                    throw new IllegalArgumentException(String.format(
                        "Building from string -- illegal character encoding field: %s", chars[i]));
                }

                i++;
            }
        }

        return startFromRawState(state);
    }


    public BoardStateBuilder(BoardState boardState) {
        this.state = boardState.state;
        this.hash = boardState.hash;
        this.count1 = boardState.count1;
        this.count2 = boardState.count2;
    }


    public BoardStateBuilder setField(int x, int y, Color color) {
        int size = state.length;

        Color oldColor = state[x][y];

        hash ^= getHashKeyValue(size, x, y, oldColor);
        if (oldColor == Color.PLAYER1) {
            --this.count1;
        } else if (oldColor == Color.PLAYER2) {
            --this.count2;
        }

        this.state[x][y] = color;

        hash ^= getHashKeyValue(size, x, y, color);
        if (color == Color.PLAYER1) {
            ++this.count1;
        } else if (color == Color.PLAYER2) {
            ++this.count2;
        }

        return this;
    }


    public BoardState finish() {
        BoardState result = new BoardState(state, hash, count1, count2);
        state = null;
        return result;
    }


    protected static int getHashKeyValue(int size, int x, int y, Color color) {
        if (hashKeyTable == null) {
            generateHashKeyTable(size);
            hashKeyTableSize = size;
        } else if (hashKeyTableSize != size) {
            throw new IllegalArgumentException("Cannot alter hash key table size during game.");
        }
        return hashKeyTable[x][y].get(color);
    }


    protected static void generateHashKeyTable(int size) {
        hashKeyTable = new EnumMap[size][size];

        Random random = new Random();

        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                hashKeyTable[x][y] = new EnumMap(Color.class);
                hashKeyTable[x][y].put(Color.PLAYER1, random.nextInt());
                hashKeyTable[x][y].put(Color.PLAYER2, random.nextInt());
                hashKeyTable[x][y].put(Color.EMPTY, random.nextInt());
            }
        }
    }
}
