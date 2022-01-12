/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package put.ai.games.tester;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import put.ai.games.engine.loaders.MetaPlayerLoader;
import put.ai.games.engine.loaders.PlayerLoadingException;
import put.ai.games.game.Player;

public class Tester {

    private static final Pattern namePattern = Pattern
            .compile("^\\p{IsLetter}+ (\\p{IsLetter}|-)+ \\d{5,6}( \\p{IsLetter}+ (\\p{IsLetter}|-)+ \\d{5,6})?$");


    public static boolean isValidName(String name) {
        return name.length() < 100 && namePattern.matcher(name).matches();
    }


    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Arguments: path");
            System.exit(1);
        }
        Class<? extends Player> clazz = null;
        try {
            clazz = MetaPlayerLoader.INSTANCE.load(args[0]);
        } catch (PlayerLoadingException ex) {
            System.out.printf("Nie znaleziono klasy gracza: %s\n", ex);
            System.exit(1);
        }
        System.setSecurityManager(new SecurityManager());
        new Thread(new Runnable() {

            @Override
            public void run() {
                for (;;) {
                    try {
                        Thread.sleep(10000);
                        break;
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Tester.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                System.exit(1);
            }
        }).start();
        try {
            Player player = clazz.newInstance();
            String name = player.getName();
            if (!isValidName(name)) {
                System.out
                        .println("Nazwa gracza nie pasuje do wzorca: Imie1 Nazwisko1 Indeks1 Imie2 Nazwisko2 Indeks2");
                System.exit(1);
            }
            System.out.printf("Gracz: '%s'\n", name);
        } catch (InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(Tester.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.exit(0);
    }
}
