package put.ai.games.game.moves;

public interface RotateMove {
    enum Direction {CLOCKWISE, COUNTERCLOCKWISE}


    Direction getDirection();
}
