package put.ai.games.aries;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import put.ai.games.game.Move;
import put.ai.games.game.Player;
import put.ai.games.game.Player.Color;
import put.ai.games.game.Board;

//TODO decide on one word, stone or piece
//TODO format code
//NOTE I assume that we can squeeze enemy even in situation like this:
// F   ->    E E E F   =>   F E E F
public final class AriesBoard implements Board //TypicalBoard
{

    protected final LinkedList<AriesMove> moves;
    protected BoardState currentState = null;
    protected final int size;
    protected final EnumMap<Color, List<Move>> cachedMoves = new EnumMap(Color.class);


    public AriesBoard() {
        this(8);
    }


    public AriesBoard(int size) {
        this(BoardStateBuilder.startFromInitial(validateSize(size)).finish(), new LinkedList<AriesMove>());
    }


    //this constructor is mainly for creating boards for test purposes
    //blank characters are removed, then size of board is calculated
    public AriesBoard(String str) {
        this(BoardStateBuilder.startFromString(str).finish(), new LinkedList<AriesMove>());
    }


    //TODO add size to BoardState for optimisation
    //this is a final constructor called by all other constructors
    protected AriesBoard(BoardState currentState, LinkedList<AriesMove> moves) {
        this.size = currentState.state.length;
        this.currentState = currentState;
        this.moves = moves;
    }


    @Override
    public AriesBoard clone() {
        return new AriesBoard(this.currentState, (LinkedList<AriesMove>) moves.clone());
    }


    //this way is effective, because normally exception is not thrown
    //and size checking is done inside anyway
    @Override
    public Color getState(int x, int y) {
        try {
            return currentState.state[x][y];
        } catch (ArrayIndexOutOfBoundsException ex) {
            return Color.EMPTY;
        }
    }


    //if size is invalid throws exception
    //if size is valid returns the input value
    //this method is needed to work around:
    //"call to this must be first statement in constructor"
    protected static int validateSize(int size) {
        if (size < 2 || ((size % 2) != 0)) {
            throw new IllegalArgumentException("Requested board size inadequate for playing Aries game.");
        }
        return size;
    }


    public boolean isValid(int x, int y) {
        return !(x < 0 || y < 0 || x >= size || y >= size);
    }


    @Override
    public int getSize() {
        return size;
    }


    //checks if currentState is equal
    //history is not compared
    public static boolean equalStates(AriesBoard boardA, AriesBoard boardB) {
        return boardA.currentState.equals(boardB.currentState);
    }


    //only checks history
    //
    //assumes both boards are not null
    //moves will never be null because of how constructors are formulated
    //
    //currentState is not checked (but it should be the same)
    //I wrote this because it's hard to trust any equals implementation for collections
    protected static boolean equalHistory(AriesBoard boardA, AriesBoard boardB) {
        if (boardA.moves.size() != boardB.moves.size()) {
            return false;
        }

        Iterator<AriesMove> mitA = boardA.moves.iterator();
        Iterator<AriesMove> mitB = boardA.moves.iterator();
        while (mitA.hasNext()) {
            if (!mitA.next().equals(mitB.next())) {
                return false;
            }
        }

        return true;

    }


    //comparing history is derogatory for many advanced algorithms
    //that use transposition table etc.
    //(but not totally)
    //
    //however I decided not to break equals contract
    @Override
    public boolean equals(Object otherObj) {
        if (otherObj == this) {
            return true;
        }

        //this also checks for null
        if (!(otherObj instanceof AriesBoard)) {
            return false;
        }

        AriesBoard otherBoard = (AriesBoard) otherObj;

        //compare current states
        if (!equalStates(this, otherBoard)) {
            return false;
        }
        //compare history
        return equalHistory(this, otherBoard);
    }


    @Override
    public int hashCode() {
        return currentState.hashCode();
    }


    //TODO I could add caching winner but it's not required
    //we assume that this function is called at each turn
    //so we don't have to check if previous player won in previous move
    @Override
    public Color getWinner(Color currentPlayer) {
        //theoretically you can be pushed to winning state by the enemy
        //so we don't check the move, but the board state
        boolean p1wonField = (currentState.state[size - 1][size - 1] == Color.PLAYER1);
        boolean p2wonField = (currentState.state[0][0] == Color.PLAYER2);

        if (p1wonField) {
            if (p2wonField) {
                throw new IllegalArgumentException("Impossible situation: both players on their winning fields");
            }
            return Color.PLAYER1;
        }
        if (p2wonField) {
            return Color.PLAYER2;
        }

        if (currentState.getCount2() == 0) {
            if (currentState.getCount1() == 0) {
                throw new IllegalArgumentException("Impossible situation: both players lost all stones");
            }
            return Color.PLAYER1;
        }
        if (currentState.getCount1() == 0) {
            return Color.PLAYER2;
        }

        //repetition is a failure so the player with no possible moves loses
        //there is no other situation in which player has no moves (I cannot think of any)
        if (getMovesFor(currentPlayer).isEmpty()) {
            return Player.getOpponent(currentPlayer);
        }

        //so considering the condition above we cannot have a draw here
        return null;
    }


    @Override
    public void doMove(Move move) {
        AriesMove aMove = (AriesMove) move;

        if (aMove.getSrcState() != currentState) {
            throw new IllegalArgumentException("Move was not generated for the current state");
        }

        if (aMove.getDstState() == null) {
            throw new IllegalArgumentException("Move has no destination board filled");
        }

        //we don't check repetition here, because we already did so during generation
        currentState = aMove.getDstState();
        moves.add(aMove);

        invalidateCachedMoves();
    }


    @Override
    public void undoMove(Move move) {
        AriesMove aMove = (AriesMove) move;
        AriesMove lastMove = moves.removeLast();

        if (aMove != lastMove) {
            throw new IllegalArgumentException("Move is not the last move performed.");
        }

        currentState = aMove.getSrcState();

        invalidateCachedMoves();
    }


    @Override
    public List<Move> getMovesFor(Color color) {
        // turn off caching:
        // return directGetMovesFor(color);

        List<Move> cachedPlayerMoves = cachedMoves.get(color);
        if (cachedPlayerMoves != null) {
            return cachedPlayerMoves;
        }

        List<Move> calculatedMoves = directGetMovesFor(color);
        cachedMoves.put(color, calculatedMoves);

        return calculatedMoves;

    }


    protected List<Move> directGetMovesFor(Color color) {
        LinkedList<Move> result = new LinkedList<>();

        for (int x = 0; x < size; ++x) {
            for (int y = 0; y < size; ++y) {
                if (currentState.state[x][y] == color) {
                    getMovesFromFieldInDirection(color, x, y, 0, 1, result);
                    getMovesFromFieldInDirection(color, x, y, 1, 0, result);
                    getMovesFromFieldInDirection(color, x, y, 0, -1, result);
                    getMovesFromFieldInDirection(color, x, y, -1, 0, result);
                }
            }
        }

        return result;
    }


    //unfortunatelly, to check if moves are not leading to repetitions of board state
    //we have to evaluate them here
    protected void getMovesFromFieldInDirection(Color color, int x, int y, int dx, int dy, LinkedList<Move> result) {
        Color opponentColor = Player.getOpponent(color);

        int nx = x + dx;
        int ny = y + dy;

        while (isValid(nx, ny)) {
            //friendly stone, we cannot move here
            //and we cannot move further
            if (currentState.state[nx][ny] == color) {
                break;
            }

            //prepare copy of inner board
            //for manipulation of pieces
            BoardStateBuilder nextStateBuilder = BoardStateBuilder.startFromBoardState(currentState);
            nextStateBuilder.setField(x, y, Color.EMPTY);
            nextStateBuilder.setField(nx, ny, color);

            //if field is empty, we just move there
            //and maybe we can move further
            if (currentState.state[nx][ny] == Color.EMPTY) {
                tryPossibleMoveAppending(result,
                    new AriesMove(color, x, y, nx, ny, currentState, nextStateBuilder.finish()));
            }
            //enemy stone, we can move here, but not further
            //calculate push effects
            else {
                int fx = nx + dx;
                int fy = ny + dy;
                while (isValid(fx, fy)) {
                    if (currentState.state[fx][fy] == Color.EMPTY) {
                        nextStateBuilder.setField(fx, fy, opponentColor);
                        break;
                    } else if (currentState.state[fx][fy] == color) {
                        break;
                    }
                    fx += dx;
                    fy += dy;
                }
                tryPossibleMoveAppending(result,
                    new AriesMove(color, x, y, nx, ny, currentState, nextStateBuilder.finish()));
                break;
            }
            nx += dx;
            ny += dy;
        }

    }


    protected void tryPossibleMoveAppending(List<Move> result, AriesMove move) {
        for (AriesMove m : moves) {
            if (m.getSrcState().equals(move.getDstState())) {
                return;
            }
        }
        result.add(move);
    }


    protected void invalidateCachedMoves() {
        cachedMoves.clear();
    }
}
