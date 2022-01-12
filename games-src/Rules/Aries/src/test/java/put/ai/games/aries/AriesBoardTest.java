package put.ai.games.aries;

import java.util.LinkedList;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;
import put.ai.games.game.Move;
import put.ai.games.game.Player.Color;

public class AriesBoardTest {

    //---testing possible moves generation

    @Test
    public void generator1() {
        BoardState state = BoardStateBuilder.startFromEmpty(8).setField(3, 1, Color.PLAYER1)
                .setField(3, 3, Color.PLAYER2).setField(3, 4, Color.PLAYER2).setField(3, 5, Color.PLAYER2).finish();

        AriesBoard board = new AriesBoard(state, new LinkedList<AriesMove>());

        List<Move> moves = board.getMovesFor(Color.PLAYER1);
        assertEquals(10, moves.size());
    }


    @Test
    public void generator2() {
        BoardState state = BoardStateBuilder.startFromEmpty(8).setField(3, 1, Color.PLAYER1)
                .setField(3, 2, Color.PLAYER1).setField(3, 3, Color.PLAYER2).setField(3, 4, Color.PLAYER2)
                .setField(3, 5, Color.PLAYER2).finish();

        AriesBoard board = new AriesBoard(state, new LinkedList<AriesMove>());

        List<Move> moves = board.getMovesFor(Color.PLAYER1);
        assertEquals(16, moves.size());
    }


    /* TODO add more generator tests */

    //---testing winners

    @Test
    public void player1LosesByStones() {
        BoardState state = BoardStateBuilder.startFromEmpty(8).setField(3, 1, Color.PLAYER2)
                .setField(3, 2, Color.PLAYER2).setField(3, 3, Color.PLAYER2).finish();

        AriesBoard board = new AriesBoard(state, new LinkedList<AriesMove>());

        assertEquals(Color.PLAYER2, board.getWinner(Color.EMPTY));
    }


    @Test
    public void player2LosesByStones() {
        BoardState state = BoardStateBuilder.startFromEmpty(8).setField(3, 1, Color.PLAYER1)
                .setField(3, 2, Color.PLAYER1).setField(3, 3, Color.PLAYER1).finish();

        AriesBoard board = new AriesBoard(state, new LinkedList<AriesMove>());

        assertEquals(Color.PLAYER1, board.getWinner(Color.PLAYER2));

    }


    @Test
    public void nobodyWins() {
        BoardState state = BoardStateBuilder.startFromEmpty(8).setField(3, 1, Color.PLAYER1)
                .setField(3, 2, Color.PLAYER1).setField(3, 3, Color.PLAYER2).finish();

        AriesBoard board = new AriesBoard(state, new LinkedList<AriesMove>());

        assertEquals(null, board.getWinner(Color.PLAYER2));
    }


    @Test
    public void player1WinsByPosition() {
        BoardState state = BoardStateBuilder.startFromEmpty(8).setField(3, 1, Color.PLAYER1)
                .setField(3, 2, Color.PLAYER2).setField(3, 3, Color.PLAYER1).setField(7, 7, Color.PLAYER1).finish();

        AriesBoard board = new AriesBoard(state, new LinkedList<AriesMove>());

        assertEquals(Color.PLAYER1, board.getWinner(Color.PLAYER2));
    }


    @Test
    public void player2WinsByPosition() {
        BoardState state = BoardStateBuilder.startFromEmpty(8).setField(0, 0, Color.PLAYER2)
                .setField(3, 1, Color.PLAYER1).setField(3, 2, Color.PLAYER2).setField(3, 3, Color.PLAYER1).finish();

        AriesBoard board = new AriesBoard(state, new LinkedList<AriesMove>());

        assertEquals(Color.PLAYER2, board.getWinner(Color.PLAYER1));
    }


    @Test
    public void player2LosesByNoValidMoves() {
        LinkedList<AriesMove> moves = new LinkedList<>();

        LinkedList<BoardState> states = new LinkedList<>();
        for (int x = 0; x <= 6; x++) {
            BoardStateBuilder stateBuilder = BoardStateBuilder.startFromEmpty(8);
            stateBuilder.setField(0, 0, Color.PLAYER1);
            stateBuilder.setField(x, 0, Color.PLAYER2);
            states.add(stateBuilder.finish());
        }
        for (int y = 7; y >= 0; y--) {
            BoardStateBuilder stateBuilder = BoardStateBuilder.startFromEmpty(8);
            stateBuilder.setField(0, 0, Color.PLAYER1);
            stateBuilder.setField(7, y, Color.PLAYER2);
            states.add(stateBuilder.finish());
        }

        for (int i = 0; i <= states.size() - 2; i++) {
            moves.add(new AriesMove(Color.EMPTY, 0, 0, 0, 0, states.get(i), states.get(i + 1)));
        }

        AriesBoard board = new AriesBoard(states.getLast(), moves);

        assertEquals(Color.PLAYER1, board.getWinner(Color.PLAYER2));

        List<Move> possibleMoves = board.getMovesFor(Color.PLAYER2);
        assertTrue(possibleMoves.isEmpty());
    }


    //---testing moves effects

    @Test
    public void player1MovesToEmptyField() {
        BoardState state = BoardStateBuilder.startFromEmpty(8).setField(3, 1, Color.PLAYER1)
                .setField(4, 1, Color.PLAYER1).setField(5, 1, Color.PLAYER1).setField(3, 4, Color.PLAYER2)
                .setField(3, 5, Color.PLAYER2).finish();

        AriesBoard board = new AriesBoard(state, new LinkedList<AriesMove>());
        List<Move> possibleMoves = board.getMovesFor(Color.PLAYER1);

        AriesMove chosenMove = null;
        for (Move m : possibleMoves) {
            AriesMove am = (AriesMove) m;
            if (am.getColor() == Color.PLAYER1 && am.getSrcX() == 3 && am.getSrcY() == 1 && am.getDstX() == 3
                    && am.getDstY() == 3) {
                chosenMove = am;
            }
        }
        assertNotNull(chosenMove);

        board.doMove(chosenMove);

        assertEquals(Color.EMPTY, board.getState(3, 1));
        assertEquals(Color.PLAYER1, board.getState(3, 3));
    }


    @Test
    public void player1MovesToFirstEnemyField() {
        BoardState state = BoardStateBuilder.startFromEmpty(8).setField(3, 1, Color.PLAYER1)
                .setField(4, 1, Color.PLAYER1).setField(5, 1, Color.PLAYER1).setField(3, 4, Color.PLAYER2)
                .setField(3, 5, Color.PLAYER2).finish();

        AriesBoard board = new AriesBoard(state, new LinkedList<AriesMove>());

        List<Move> possibleMoves = board.getMovesFor(Color.PLAYER1);
        AriesMove chosenMove = null;
        for (Move m : possibleMoves) {
            AriesMove am = (AriesMove) m;
            if (am.getColor() == Color.PLAYER1 && am.getSrcX() == 3 && am.getSrcY() == 1 && am.getDstX() == 3
                    && am.getDstY() == 4) {
                chosenMove = am;
            }
        }
        assertNotNull(chosenMove);

        board.doMove(chosenMove);

        assertEquals(Color.EMPTY, board.getState(3, 1));
        assertEquals(Color.PLAYER1, board.getState(3, 4));
        assertEquals(Color.PLAYER2, board.getState(3, 5));
        assertEquals(Color.PLAYER2, board.getState(3, 6));
        assertEquals(Color.EMPTY, board.getState(3, 7));

    }


    @Test
    public void player2GetsCaptured() {
        BoardState state = BoardStateBuilder.startFromEmpty(8).setField(3, 1, Color.PLAYER1)
                .setField(4, 1, Color.PLAYER1).setField(5, 1, Color.PLAYER1).setField(3, 4, Color.PLAYER2)
                .setField(3, 5, Color.PLAYER2).setField(3, 6, Color.PLAYER1).finish();

        AriesBoard board = new AriesBoard(state, new LinkedList<AriesMove>());
        List<Move> possibleMoves = board.getMovesFor(Color.PLAYER1);

        AriesMove chosenMove = null;
        for (Move m : possibleMoves) {
            AriesMove am = (AriesMove) m;
            if (am.getColor() == Color.PLAYER1 && am.getSrcX() == 3 && am.getSrcY() == 1 && am.getDstX() == 3
                    && am.getDstY() == 4) {
                chosenMove = am;
            }
        }
        assertNotNull(chosenMove);

        board.doMove(chosenMove);

        assertEquals(Color.EMPTY, board.getState(3, 1));
        assertEquals(Color.PLAYER1, board.getState(3, 4));
        assertEquals(Color.PLAYER2, board.getState(3, 5));
        assertEquals(Color.PLAYER1, board.getState(3, 6));
    }


    @Test
    public void player2GetsPushedOffTheBoard() {
        BoardState state = BoardStateBuilder.startFromEmpty(8).setField(3, 1, Color.PLAYER1)
                .setField(4, 1, Color.PLAYER1).setField(5, 1, Color.PLAYER1).setField(3, 4, Color.PLAYER2)
                .setField(3, 5, Color.PLAYER2).setField(3, 6, Color.PLAYER2).setField(3, 7, Color.PLAYER2).finish();

        AriesBoard board = new AriesBoard(state, new LinkedList<AriesMove>());
        List<Move> possibleMoves = board.getMovesFor(Color.PLAYER1);

        AriesMove chosenMove = null;
        for (Move m : possibleMoves) {
            AriesMove am = (AriesMove) m;
            if (am.getColor() == Color.PLAYER1 && am.getSrcX() == 3 && am.getSrcY() == 1 && am.getDstX() == 3
                    && am.getDstY() == 4) {
                chosenMove = am;
            }
        }
        assertNotNull(chosenMove);

        board.doMove(chosenMove);

        assertEquals(Color.EMPTY, board.getState(3, 1));
        assertEquals(Color.PLAYER1, board.getState(3, 4));
        assertEquals(Color.PLAYER2, board.getState(3, 5));
        assertEquals(Color.PLAYER2, board.getState(3, 6));
        assertEquals(Color.PLAYER2, board.getState(3, 7));
    }


    @Test
    public void singlePlayer2GetsPushedOffTheBoard() {
        BoardState state = BoardStateBuilder.startFromEmpty(8).setField(0, 7, Color.PLAYER2)
                .setField(1, 7, Color.PLAYER1).setField(2, 7, Color.PLAYER2).finish();

        AriesBoard board = new AriesBoard(state, new LinkedList<AriesMove>());
        List<Move> possibleMoves = board.getMovesFor(Color.PLAYER1);

        AriesMove chosenMove = null;
        for (Move m : possibleMoves) {
            AriesMove am = (AriesMove) m;
            if (am.getColor() == Color.PLAYER1 && am.getSrcX() == 1 && am.getSrcY() == 7 && am.getDstX() == 0
                    && am.getDstY() == 7) {
                chosenMove = am;
            }
        }
        assertNotNull(chosenMove);

        board.doMove(chosenMove);

        assertEquals(Color.PLAYER1, board.getState(0, 7));
        assertEquals(Color.EMPTY, board.getState(1, 7));
        assertEquals(Color.PLAYER2, board.getState(2, 7));
    }


    /*I commented out wrong moves tests
    -- in current implementation exception will be thrown
    because the move was not generated by us*/
    /*
    @Test(expected = IllegalArgumentException.class)
    public void player1MovesToSecondEnemyField()
    {
        Color[][] state = getEmptyState();

        state[3][1] = Color.PLAYER1;
        state[4][1] = Color.PLAYER1;
        state[5][1] = Color.PLAYER1;
        state[3][4] = Color.PLAYER2;
        state[3][5] = Color.PLAYER2;

        Whitebox.setInternalState(b, state);
        b.doMove(new AriesMove(Color.PLAYER1, 3, 1, 3, 5));
    }
    */

    /*
    @Test(expected = IllegalArgumentException.class)
    public void player1MovesToFirstFriendlyField()
    {
        Color[][] state = getEmptyState();

        state[3][1] = Color.PLAYER1;
        state[4][1] = Color.PLAYER1;
        state[5][1] = Color.PLAYER1;
        state[3][4] = Color.PLAYER2;
        state[3][5] = Color.PLAYER2;

        Whitebox.setInternalState(b, state);
        b.doMove(new AriesMove(Color.PLAYER1, 3, 1, 4, 1));
    }
    */

    /*
    @Test(expected = IllegalArgumentException.class)
    public void player1MovesToSecondFriendlyField()
    {
        Color[][] state = getEmptyState();

        state[3][1] = Color.PLAYER1;
        state[4][1] = Color.PLAYER1;
        state[5][1] = Color.PLAYER1;
        state[3][4] = Color.PLAYER2;
        state[3][5] = Color.PLAYER2;

        Whitebox.setInternalState(b, state);
        b.doMove(new AriesMove(Color.PLAYER1, 3, 1, 5, 1));
    }
    */

    //---testing state repetition prevention

    @Test
    public void avoidingStateRepetition() {
        BoardState state = BoardStateBuilder.startFromEmpty(8).setField(0, 0, Color.PLAYER1)
                .setField(3, 3, Color.PLAYER2).setField(3, 5, Color.PLAYER2).finish();

        AriesBoard board = new AriesBoard(state, new LinkedList<AriesMove>());

        List<Move> possibleMoves = board.getMovesFor(Color.PLAYER2);
        assertEquals(21, possibleMoves.size());

        AriesMove chosenMove = null;
        for (Move m : possibleMoves) {
            AriesMove am = (AriesMove) m;
            if (am.getColor() == Color.PLAYER2 && am.getSrcX() == 3 && am.getSrcY() == 5 && am.getDstX() == 3
                    && am.getDstY() == 6) {
                chosenMove = am;
            }
        }
        assertNotNull(chosenMove);

        board.doMove(chosenMove);

        List<Move> newMoves = board.getMovesFor(Color.PLAYER2);
        //bottom piece cannot move back to (3,5)
        //but now upper piece can move there,
        //so we still have 21
        assertEquals(21, newMoves.size());

    }

}
