package put.ai.games.rulesprovider;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import put.ai.games.engine.BoardFactory;
import put.ai.games.aries.AriesBoardFactory;

public class RulesProvider {

    public static final RulesProvider INSTANCE = new RulesProvider();
    private List<BoardFactory> rules;


    private RulesProvider() {
        BoardFactory[] games = new BoardFactory[0];
        //turning formatter off for easier scripting and better look
        // @formatter:off
        try {
            games = new BoardFactory[]{
                    new AriesBoardFactory(),
            };
            if (false)
                throw new NoSuchMethodException();
        } catch (NoSuchMethodException ex) {
            Logger.getLogger(RulesProvider.class.getName()).log(Level.SEVERE, null, ex);
        }
        // @formatter:on
        rules = Collections.unmodifiableList(Arrays.asList(games));
    }


    public List<BoardFactory> getAvailablesRules() {
        return rules;
    }


    public BoardFactory getRulesByName(String name) {
        for (BoardFactory f : rules) {
            if (name.equalsIgnoreCase(f.getName())) {
                return f;
            }
        }
        return null;
    }
}
