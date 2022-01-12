package put.ai.games;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import put.ai.games.game.Player;
import put.ai.games.game.Player.Color;

public class BoardTestUtils {

    private BoardTestUtils() {

    }


    public static Player.Color[][] fromString(String s) {
        s = s.replaceAll("[\n\r]", "");
        int n = (int) Math.sqrt(s.length());
        if (s.length() != n * n) {
            throw new IllegalArgumentException("Length of the string must be square of an integer (not counting EOLs)");
        }
        Player.Color[][] result = new Player.Color[n][n];
        for (int y = 0; y < n; ++y) {
            for (int x = 0; x < n; ++x) {
                Color c;
                char ch = s.charAt(y * n + x);
                switch (ch) {
                    case '1':
                        c = Color.PLAYER1;
                        break;
                    case '2':
                        c = Color.PLAYER2;
                        break;
                    case '_':
                    case ' ':
                        c = Color.EMPTY;
                        break;
                    default:
                        throw new IllegalArgumentException(String.format("Unknown character '%c' at %d", ch, y * n + x));
                }
                result[x][y] = c;
            }
        }
        return result;
    }
}
