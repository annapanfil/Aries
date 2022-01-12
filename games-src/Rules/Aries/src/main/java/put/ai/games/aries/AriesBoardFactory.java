package put.ai.games.aries;

import put.ai.games.engine.UniversalBoardFactory;

public class AriesBoardFactory extends UniversalBoardFactory {

    public AriesBoardFactory()
            throws NoSuchMethodException {
        super(AriesBoard.class, "Aries", "http://homepages.di.fc.ul.pt/~jpn/gv/aries.htm");
    }
}