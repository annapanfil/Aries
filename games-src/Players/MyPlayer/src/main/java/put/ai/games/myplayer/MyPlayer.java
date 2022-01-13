package put.ai.games.myplayer;

import java.util.List;
import java.util.Random;
import put.ai.games.game.Board;
import put.ai.games.game.Move;
import put.ai.games.game.Player;
import put.ai.games.game.moves.MoveMove;

// import put.ai.games.aries.AriesMove;
//import put.ai.games.aries.BoardState;


public class MyPlayer extends Player {

  private class WinException extends Exception{
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


  private int getClosestTopLeft(Board b, Color player) throws WinException{
    /* Look in array starting from the top left corner to find the closest piece (On^2)*/
    if(b.getState(0,0) == player)
      throw new WinException();  // winning state

    for (int k=1; k < b.getSize; k++){
      for (int i=0; i<k; i++){
        for (int j=k; j>=0; j--){
          if (b.getState(x,y) == player)
            return i+j;  //IDEA: if 0th row or column, smaller (or return other number). Look in the 0th rows first
        }
      }
    }
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
          if (b.getState(x,y) == player)
            return 2*last_field-i-j;
          }
        }
      }
  }

  private int stateEvaluation(Move m, Board b){
      /* Calculate objective function value */

      MoveMove move = (MoveMove) m;
      Color player = getColor();
      Color opponent = getOpponent(player);

      if (debug){
        System.out.print("from: (" + move.getSrcX() + ", " + move.getSrcY() + ") ");
        System.out.println("to: ("+ move.getDstX() + ", " + move.getDstY() + ") ");
      }

      BoardState state = move.getDstState();
      board = boardState.state

      int my_pieces = state.getCount1();
      int opponents_pieces = state.getCount2();

      try{
        int my_distance = getClosestCorner(b, player);
      }
      catch (WinException e){
        return (int) Integer.MIN_VALUE; //our winning move
      }

      try{
        int opponents_distance = getClosestCorner(b, opponent);
      }
      catch (WinException e){
        return (int) Integer.MAX_VALUE; //their winning move
      }


      if (debug){
        System.out.println("Pieces: My " + my_pieces + "Their "+ opponents_pieces);
        System.out.println("Distance: My " + my_distance + "Their " + opponents_distance);
      }

      return opponents_pieces+my_distance-my_pieces-opponents_distance;
  }


  Move chooseBestMove(List<Move> moves, Board board){
    /* Choose move with minimum objective function value */

    //self.getTime()
    Move best_move;
    int best_move_value = Integer.MAX_VALUE;
    Move curr_move;
    int curr_move_value;

    iterator = moves.iterator();
    while(iterator.hasNext()){
      curr_move = (Move) iterator.next();

      curr_move_value = stateEvaluation(move, board);
      if (curr_move_value == Integer.MIN_VALUE)
        return move;
      if(curr_move_value < best_move_value){
        best_move = move;
        best_move_value = curr_move_value;
      }
    }
    return best_move;
  }


  @Override
  public Move nextMove(Board b) {

      Color player = getColor();
      System.out.print(player + " ");
      int count = 0;

      for (int x = 0; x < b.getSize(); x++) {
          for (int y = 0; y < b.getSize(); y++) {
              if (b.getState(x,y) == player) {
                  count++;
              }
          }
      }
      System.out.println(count);

      List<Move> moves = b.getMovesFor(getColor());

      return chooseBestMove(moves, board);
  }
}
