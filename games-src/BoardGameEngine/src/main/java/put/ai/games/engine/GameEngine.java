/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package put.ai.games.engine;

import put.ai.games.game.Player;
import put.ai.games.game.Player.Color;
import put.ai.games.game.exceptions.RuleViolationException;

public interface GameEngine {

    void addPlayer(Player p);


    Color play(Callback cb)
            throws RuleViolationException;


    void setTimeout(int timeout);
}
