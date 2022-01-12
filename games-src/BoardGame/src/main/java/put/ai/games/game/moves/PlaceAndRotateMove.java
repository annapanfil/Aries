package put.ai.games.game.moves;

import put.ai.games.game.Move;

public interface PlaceAndRotateMove extends Move {
    int getPlaceX();

    int getPlaceY();

    int getRotateSrcX();

    int getRotateSrcY();

    int getRotateDstX();

    int getRotateDstY();
}
