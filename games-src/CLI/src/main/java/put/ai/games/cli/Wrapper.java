/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package put.ai.games.cli;

import com.higherfrequencytrading.affinity.impl.PosixJNAAffinity;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import put.ai.games.game.Board;
import put.ai.games.game.Move;
import put.ai.games.game.Player;

public class Wrapper extends Player implements Runnable {

    private final Player base;
    private final long affinity;
    private final BlockingQueue<Board> boards = new ArrayBlockingQueue<>(1);
    private final BlockingQueue<Object> moves = new ArrayBlockingQueue<>(1);


    public Wrapper(Player pl, long affinity) {
        this.base = pl;
        this.affinity = affinity;
    }


    @Override
    public String getName() {
        synchronized (base) {
            return base.getName();
        }
    }


    @Override
    public Move nextMove(Board board) {
        try {
            boards.put(board);
            Object result = moves.take();
            if (result instanceof Move) {
                return (Move) result;
            } else {
                throw new RuntimeException((Throwable) result);
            }
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }
    }


    @Override
    public void run() {
        System.err.printf("Setting affinity to %d\n", affinity);
        PosixJNAAffinity.INSTANCE.setAffinity(affinity);
        try {
            while (!Thread.interrupted()) {
                Board board = boards.take();
                Object move;
                synchronized (base) {
                    try {
                        move = base.nextMove(board);
                    } catch (Throwable ex) {
                        move = ex;
                    }
                }
                moves.put(move);
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(Wrapper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    @Override
    public Color getColor() {
        synchronized (base) {
            return base.getColor();
        }
    }


    @Override
    public void setColor(Color color) {
        synchronized (base) {
            base.setColor(color);
        }
    }


    @Override
    public void setTime(long time) {
        synchronized (base) {
            base.setTime(time);
        }
    }


    @Override
    public long getTime() {
        synchronized (base) {
            return base.getTime();
        }
    }

}
