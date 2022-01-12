/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package put.ai.games.game.exceptions;

import put.ai.games.game.Player.Color;

public class TimeoutException extends RuleViolationException {

    public TimeoutException(Color guilty) {
        super(guilty, "Myśli za długo");
    }
}
