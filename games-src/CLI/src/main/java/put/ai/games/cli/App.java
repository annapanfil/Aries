package put.ai.games.cli;

import com.higherfrequencytrading.affinity.impl.PosixJNAAffinity;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import put.ai.games.engine.BoardFactory;
import put.ai.games.engine.Callback;
import put.ai.games.engine.GameEngine;
import put.ai.games.engine.impl.GameEngineImpl;
import put.ai.games.engine.loaders.MetaPlayerLoader;
import put.ai.games.engine.loaders.PlayerLoadingException;
import put.ai.games.game.Board;
import put.ai.games.game.Move;
import put.ai.games.game.Player;
import put.ai.games.game.Player.Color;
import put.ai.games.game.exceptions.RuleViolationException;
import put.ai.games.rulesprovider.RulesProvider;

public class App {

    public static String escape(String n) {
        n = n.replaceAll("[^\\p{Graph}\\p{Blank}]", "_");
        return n.replace("\"", "\"\"");
    }


    public static String getName(Player p) {
        return "\"" + escape(p.getName()) + "\"";
    }


    public static void main(String[] args)
            throws InstantiationException, IllegalAccessException {
        int boardSize = 8;
        int timeout = 20000;
        for (String arg : args) {
            System.err.println(arg);
        }
        PrintStream origOut = System.out;
        System.setOut(System.err);
        System.err.println(args.length);
        if (args.length < 3) {
            System.err.printf("Arguments: first-player-jar second-player-jar game [board-size=%d] [timeout=%d]\n",
                boardSize, timeout);
            return;
        }
        if (args.length >= 4) {
            boardSize = Integer.parseInt(args[3]);
        }
        if (args.length >= 5) {
            timeout = Integer.parseInt(args[4]);
        }
        Class<? extends Player>[] cl = new Class[2];
        for (int i = 0; i < cl.length; ++i) {
            try {
                cl[i] = MetaPlayerLoader.INSTANCE.load(args[i]);
            } catch (PlayerLoadingException ex) {
                System.err.printf("Can not load player from %s: %s\n", args[i], ex);
                return;
            }
        }
        if (!PosixJNAAffinity.INSTANCE.LOADED) {
            System.err.println("Can not set affinitiy. Dying!");
            return;
        }
        BoardFactory boardFactory = RulesProvider.INSTANCE.getRulesByName(args[2]);
        System.setSecurityManager(new SecurityManager());
        if (boardFactory == null) {
            System.err.printf("Unknown game name `%s'!\n", args[0]);
            return;
        } else {
            System.err.printf("Playing %s\n", boardFactory.getClass().getSimpleName());
        }
        Map<String, Object> config = new HashMap<>();
        config.put(BoardFactory.BOARD_SIZE, boardSize);
        boardFactory.configure(config);
        Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
        GameEngine g = new GameEngineImpl(boardFactory);
        g.setTimeout(timeout);
        Player[] p = new Player[cl.length];
        String result = "";
        PosixJNAAffinity.INSTANCE.setAffinity(1);
        ThreadGroup tg = new ThreadGroup("players");
        tg.setMaxPriority(Thread.MIN_PRIORITY);
        for (int i = 0; i < cl.length; ++i) {
            Wrapper w = new Wrapper(cl[i].newInstance(), (1 << i));
            p[i] = w;
            Thread t = new Thread(tg, w);
            t.start();
            g.addPlayer(p[i]);
            result += getName(p[i]) + ";";
        }
        String error = "";
        Color winner;
        try {
            winner = g.play(new Callback() {

                @Override
                public void update(Color c, Board b, Move m) {
                    System.out.println(b);
                }
            });
        } catch (RuleViolationException ex) {
            winner = Player.getOpponent(ex.getGuilty());
            error = "\"" + escape(ex.toString()) + "\"";
        }
        result += String.format("\"%s\";%s;", winner, error);
        origOut.println(result);
        System.exit(0);
    }
}
