package put.ai.games.myplayer;

import put.ai.games.aries.AriesBoard;
import put.ai.games.aries.AriesMove;
import put.ai.games.game.Board;
import put.ai.games.game.Move;
import put.ai.games.game.Player;

import java.util.List;

import static java.lang.Math.min;

public class MyPlayer extends Player {

  boolean debug = true;

  @Override
  public String getName() {
      return "Anna Panfil 145233";
  }

  private int getDistanceToWinningField(int x, int y, Color player, Board b){
    if (player == Color.PLAYER1)
      return 2 * b.getSize() - x - y; // win in bottom right corner
    else
      return x+y; // win on 0,0
  }

  private int stateEvaluation(Board b){
    /* Calculate objective function value */

    Color player = getColor();
    Color opponent = getOpponent(player);
    AriesBoard board = (AriesBoard) b;

    int my_pieces = 0;
    int opponents_pieces = 0;
    int my_distance = Integer.MAX_VALUE;
    int opponents_distance = Integer.MAX_VALUE;

    for (int i=0; i<board.getSize(); i++){
      for (int j=0; j<board.getSize(); j++){
          if (b.getState(i, j) == player){
              my_pieces++;
              my_distance = min(getDistanceToWinningField(i, j, player, board), my_distance);
              if (my_distance == 0)
                  return Integer.MIN_VALUE; //our winning move
          }
          else if (b.getState(i, j) == opponent){
              opponents_pieces++;
              opponents_distance = min(getDistanceToWinningField(i, j, opponent, board), opponents_distance);

              if (opponents_distance == 0)
                  return Integer.MAX_VALUE; //their winning move
          }
      }
    }

      if (debug){
        System.out.println("Pieces: My " + my_pieces + "Their "+ opponents_pieces);
        System.out.println("Distance: My " + my_distance + "Their " + opponents_distance);
      }

      return 9*opponents_pieces + 10*my_distance - 8*my_pieces - 7*opponents_distance;
  }


  Move chooseBestMove(List<Move> moves, Board board){
    /* Choose move with minimum objective function value */

    //self.getTime()
    AriesMove best_move = null;
    int best_move_value = Integer.MAX_VALUE;
    AriesMove curr_move;
    int curr_move_value;
    Board boardNew;

      for (Move move : moves) {
          curr_move = (AriesMove) move;

          boardNew = board.clone();
          boardNew.doMove(move);



          curr_move_value = stateEvaluation(boardNew);
          if (debug){
              System.out.println(move.toString() + " " + curr_move_value);
          }

          if (curr_move_value == Integer.MIN_VALUE) // winning move
              return curr_move;
          if (curr_move_value < best_move_value) {
              best_move = curr_move;
              best_move_value = curr_move_value;
          }
      }
    return best_move;
  }


  @Override
  public Move nextMove(Board b) {
    Color player = getColor();
    System.out.print(player + " ");

    List<Move> moves = b.getMovesFor(getColor());

    return chooseBestMove(moves, b);
  }
}
