package put.ai.games.myplayer;

import put.ai.games.aries.AriesBoard;
import put.ai.games.aries.AriesMove;
import put.ai.games.game.Board;
import put.ai.games.game.Move;
import put.ai.games.game.Player;

import java.util.List;


public class MyPlayer extends Player {

  private static class WinException extends Exception{
    // Color winner;
    //
    // protected WinException(Color winner){
    //   this.winner = winner;
    // }
  }

  boolean debug = true;

  @Override
  public String getName() {
      return "Anna Panfil 145233";
  }


  private int count_pieces(Color player, AriesBoard b){
      int count = 0;

      for (int x = 0; x < b.getSize(); x++) {
          for (int y = 0; y < b.getSize(); y++) {
              if (b.getState(x,y) == player) {
                  count++;
              }
          }
      }
      return count;
  }


  private int getClosestTopLeft(Board b, Color player) throws WinException{
    /* Look in array starting from the top left corner to find the closest piece (On^2)*/
    if(b.getState(0,0) == player)
      throw new WinException();  // winning state

    for (int k=1; k < b.getSize(); k++){
      for (int i=0; i<k; i++){
        for (int j=k; j>=0; j--){
          if (b.getState(i,j) == player)
            return i+j;  //IDEA: if 0th row or column, smaller (or return other number). Look in the 0th rows first
        }
      }
    }
    return Integer.MAX_VALUE;
  }

  private int getClosestBottomRight(Board b, Color player) throws WinException{
    /* Look in array starting from the bottom right corner to find the closest piece (On^2)*/
    int last_field = b.getSize()-1;

    if (b.getState(last_field, last_field) == player){
      throw new WinException();
    }

    for (int k=last_field; k > 0; k--){
      for (int i=k; i<last_field; i++){
        for (int j=last_field; j>=k; j--){
          if (b.getState(i, j) == player)
            return 2*last_field-i-j;
          }
        }
      }
      return Integer.MAX_VALUE;
    }

    private int getClosest(Board b, Color player) throws WinException{
        if (player == Color.PLAYER1){
            return getClosestBottomRight(b, player);
        }
        else
            return getClosestTopLeft(b, player);
    }

  private int stateEvaluation(Move m, Board b){
      /* Calculate objective function value */

      AriesMove move = (AriesMove) m;
      Color player = getColor();
      Color opponent = getOpponent(player);
      AriesBoard board = (AriesBoard) b;

      if (debug){
        System.out.print(move.toString());
      }

//      BoardState state = move.getDstState();
//      board = boardState.state;

      int my_pieces = count_pieces(player, board);
      int opponents_pieces = count_pieces(opponent, board);
      int my_distance, opponents_distance;

      try{
        my_distance = getClosest(b, player);
      }
      catch (WinException e){
        return (int) Integer.MIN_VALUE; //our winning move
      }

      try{
        opponents_distance = getClosest(b, opponent);
      }
      catch (WinException e){
        return (int) Integer.MAX_VALUE; //their winning move
      }


      if (debug){
        System.out.println("Pieces: My " + my_pieces + "Their "+ opponents_pieces);
        System.out.println("Distance: My " + my_distance + "Their " + opponents_distance);
      }

      return 10*opponents_pieces + 9*my_distance - 8*my_pieces - 7*opponents_distance;
  }


  Move chooseBestMove(List<Move> moves, Board board){
    /* Choose move with minimum objective function value */

    //self.getTime()
    Move best_move = null;
    int best_move_value = Integer.MAX_VALUE;
    Move curr_move;
    int curr_move_value;

      for (Move move : moves) {
          curr_move = move;

          curr_move_value = stateEvaluation(curr_move, board);
          System.out.println(curr_move_value);
          if (curr_move_value == Integer.MIN_VALUE)
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
