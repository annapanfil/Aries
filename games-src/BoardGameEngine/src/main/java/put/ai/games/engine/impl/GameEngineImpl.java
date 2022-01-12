/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package put.ai.games.engine.impl;

import put.ai.games.engine.GameEngine;
import java.util.logging.Level;
import java.util.logging.Logger;
import put.ai.games.engine.BoardFactory;
import put.ai.games.game.Board;
import put.ai.games.engine.Callback;
import put.ai.games.game.Move;
import put.ai.games.game.Player;
import put.ai.games.game.Player.Color;
import put.ai.games.game.exceptions.CheatingException;
import put.ai.games.game.exceptions.NoMoveException;
import put.ai.games.game.exceptions.RuleViolationException;
import put.ai.games.game.exceptions.TimeoutException;

public class GameEngineImpl implements GameEngine {

    private Player[] players = new Player[2];
    private Color[] colors = new Color[] { Color.PLAYER1, Color.PLAYER2 };
    private int timeout = 20000; //ms
    private BoardFactory boardFactory;


    public GameEngineImpl(BoardFactory boardFactory) {
        this.boardFactory = boardFactory;
    }


    @Override
    public void addPlayer(Player p) {
        for (int i = 0; i < players.length; ++i) {
            if (players[i] == null) {
                players[i] = p;
                return;
            }
        }
        throw new RuntimeException("Maxium number of players already reached");
    }


    private static class NextMoveJob implements Runnable {

        private Board b;
        private Player p;
        private Move m;
        private Exception ex = null;


        private NextMoveJob(Player p, Board b) {
            this.p = p;
            this.b = b.clone();
        }


        @Override
        public void run() {
            try {
                m = p.nextMove(b);
            } catch (Exception ex) {
                this.ex = ex;
            }
        }


        public Exception getException() {
            return ex;
        }


        public boolean hasException() {
            return ex != null;
        }


        public Move getMove() {
            return m;
        }
    }


    @Override
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }


    private int getMaxNoOfMoves(Board b) {
        return 2 * b.getSize() * b.getSize();
    }


    @Override
    public Color play(Callback cb)
            throws RuleViolationException {
        Board b = boardFactory.create();
        for (int i = 0; i < players.length; ++i) {
            players[i].setColor(colors[i]);
            players[i].setTime(timeout);
        }
        if (cb != null) {
            cb.update(colors[0], b, null);
        }
        for (int moveNo = 0; moveNo < getMaxNoOfMoves(b); moveNo++) {
            for (int i = 0; i < players.length; ++i) {
                NextMoveJob job = new NextMoveJob(players[i], b);
                Thread t = new Thread(job);
                t.start();
                try {
                    t.join(timeout);
                } catch (InterruptedException ex) {
                    Logger.getLogger(GameEngineImpl.class.getName()).log(Level.SEVERE, null, ex);
                    throw new RuntimeException(ex);
                }
                if (job.hasException()) {
                    Logger.getLogger(GameEngineImpl.class.getName()).log(Level.SEVERE, null, job.getException());
                    throw new RuleViolationException(colors[i], job.getException());
                }
                if (t.isAlive()) {
                    throw new TimeoutException(colors[i]);
                }
                Move m = job.getMove();
                if (m == null) {
                    throw new NoMoveException(colors[i]);
                }
                if (!MoveValidator.isValidMove(m, b, colors[i])) {
                    throw new CheatingException(colors[i]);
                }
                b.doMove(m);
                if (cb != null) {
                    cb.update(colors[1 - i], b, m);
                }
                Color winner = b.getWinner(colors[1 - i]);
                if (winner != null) {
                    return winner;
                }
            }
        }
        return null;
    }
}
