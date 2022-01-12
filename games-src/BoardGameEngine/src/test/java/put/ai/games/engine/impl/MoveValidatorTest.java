/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package put.ai.games.engine.impl;

import put.ai.games.engine.impl.MoveValidator;
import java.util.Arrays;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import put.ai.games.game.Board;
import put.ai.games.game.Player;
import put.ai.games.game.Player.Color;
import put.ai.games.game.moves.MoveMove;
import put.ai.games.game.moves.PlaceMove;
import put.ai.games.game.moves.SkipMove;

@RunWith(MockitoJUnitRunner.class)
public class MoveValidatorTest {

    @Mock
    private Board b;
    @Mock
    private MoveMove mm1;
    @Mock
    private MoveMove mm2;
    @Mock
    private SkipMove sm1;
    @Mock
    private SkipMove sm2;
    @Mock
    private PlaceMove pm1;
    @Mock
    private PlaceMove pm2;
    private MoveMove mm = new MoveMove() {

        @Override
        public int getDstX() {
            return 5;
        }


        @Override
        public int getDstY() {
            return 3;
        }


        @Override
        public int getSrcX() {
            return 2;
        }


        @Override
        public int getSrcY() {
            return 7;
        }


        @Override
        public Color getColor() {
            return Color.PLAYER1;
        }
    };


    @Before
    public void before() {
    }


    @Test
    public void matches() {
        assertTrue(MoveValidator.matches(mm, mm));
        assertTrue(MoveValidator.matches(pm1, pm2));
        assertFalse(MoveValidator.matches(mm, mm1));
        assertFalse(MoveValidator.matches(mm1, mm));
        assertFalse(MoveValidator.matches(mm, mm2));
        assertFalse(MoveValidator.matches(mm2, mm));
        assertFalse(MoveValidator.matches(mm, sm1));
        assertFalse(MoveValidator.matches(sm1, mm));
    }


    @Test
    public void isValidMove() {
        when(b.getMovesFor(Player.Color.PLAYER1)).thenReturn(Arrays.asList(sm1, sm2, mm1, mm2, pm1, pm2));
        when(mm2.getDstX()).thenReturn(5);
        when(mm2.getDstY()).thenReturn(3);
        when(mm2.getSrcX()).thenReturn(2);
        when(mm2.getSrcY()).thenReturn(7);
        when(mm2.getColor()).thenReturn(Color.PLAYER1);
        assertTrue(MoveValidator.isValidMove(mm, b, Color.PLAYER1));
        assertFalse(MoveValidator.isValidMove(mm, b, Color.PLAYER2));
        assertTrue(MoveValidator.isValidMove(mm1, b, Color.PLAYER1));
        assertFalse(MoveValidator.isValidMove(mm1, b, Color.EMPTY));
        assertFalse(MoveValidator.isValidMove(mm1, b, Color.EMPTY));
    }
}
