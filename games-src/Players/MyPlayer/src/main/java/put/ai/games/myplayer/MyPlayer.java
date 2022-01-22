package put.ai.games.myplayer;

import put.ai.games.aries.AriesBoard;
import put.ai.games.aries.AriesMove;
import put.ai.games.game.Board;
import put.ai.games.game.Move;
import put.ai.games.game.Player;

import java.util.List;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class MyPlayer extends Player {

  boolean debug = true;
  int MAX_DEPTH = 2;

  @Override
  public String getName() {
      return "Anna Panfil 145233";
  }

  private int getDistanceToWinningField(int x, int y, Color player, Board b){
    if (player == Color.PLAYER1)
      return 2 * (b.getSize()-1) - x - y; // win in bottom right corner
    else
      return x+y; // win on 0,0
  }

  int checkWinning(Board b, Color player){
      if (b.getState(0,0) == Color.PLAYER2)
          if (player == Color.PLAYER2) return Integer.MAX_VALUE;
          else return Integer.MIN_VALUE+1;
      else if (b.getState(b.getSize(), b.getSize()) == Color.PLAYER1)
          if (player == Color.PLAYER1) return Integer.MAX_VALUE;
          else return Integer.MIN_VALUE+1;
      else return 0;
  }

  private int stateEvaluation(Board b, Color player){
    /* Calculate objective function value – to maximize*/

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
                  return Integer.MAX_VALUE; //our winning move
          }
          else if (b.getState(i, j) == opponent){
              opponents_pieces++;
              opponents_distance = min(getDistanceToWinningField(i, j, opponent, board), opponents_distance);

              if (opponents_distance == 0)
                  return Integer.MIN_VALUE+1; //their winning move
          }
      }
    }

//      if (debug){
//        System.out.println("Pieces: " + player + " " + my_pieces +" "+ opponent + " "+ opponents_pieces);
//        System.out.println("Distance: " + player + " " +  my_distance + " " + opponent + " " + opponents_distance);
//      }

      return -9*opponents_pieces - 10*my_distance + 8*my_pieces + 7*opponents_distance; // my_pieces - my_distance - opponents_pieces + opponents_distance;
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

          curr_move_value = stateEvaluation(boardNew, getColor());
//          if (debug){
//              System.out.println(move.toString() + " " + curr_move_value);
//          }

          if (curr_move_value == Integer.MIN_VALUE+1) // winning move
              return curr_move;
          if (curr_move_value < best_move_value) {
              best_move = curr_move;
              best_move_value = curr_move_value;
          }
      }
    return best_move;
  }

   private class BestMove{
      int value;
      AriesMove move; //TODO: lista ruchów

      BestMove(int value){
          this.value = value;
          this.move = new AriesMove(null, 0,0,0,0, null, null);
      }

       BestMove(int value, Move move){
           this.value = value;
           this.move = (AriesMove) move;
       }

      //TODO: porównanie - przeciążyć operator
      //TODO: minus unarny = przeciążyć
  }

  BestMove NegMax(Board board, Color player, int depth, long endRoundTime){
      System.out.println("\nNew recursion " + depth + " " + player);
      int val;
      if ((val = checkWinning(board, player)) != 0){
          System.out.println("Win " + player + " "+ val + " on depth " + depth);
          return new BestMove(val);
      }
      if (depth == 0){
          val = stateEvaluation(board, player);
          return new BestMove(val);
      }

      List<Move> moves = board.getMovesFor(player);

      BestMove curr_move;
      BestMove best_move = new BestMove(Integer.MIN_VALUE+1, moves.get(0));
      Board boardNew;
      long start, end, duration;
      long maxTime = 0;

      for (Move move : moves) {
          System.out.println(move.toString());

          if (System.nanoTime() + maxTime > endRoundTime) {
              System.out.println("Out of time :( " + System.nanoTime() + maxTime + " " + endRoundTime);
              break;  //out of time
          }

          start = System.nanoTime();
          boardNew = board.clone();
          boardNew.doMove(move);

          curr_move = NegMax(boardNew, getOpponent(player), depth - 1, endRoundTime);
          curr_move.value = -curr_move.value;

          if (curr_move.value > best_move.value) {
              curr_move.move = (AriesMove) move;
              best_move = curr_move;
              System.out.println("New best " + player +" " + curr_move.move.toString() + " "+ curr_move.value);
              if (best_move.value == Integer.MAX_VALUE) break; // winning move
          }
          end = System.nanoTime();
          if ((duration = end - start) > maxTime) maxTime = duration;

          System.out.println("Value for move " + curr_move.move.toString() + " "+ curr_move.value + " for " + player);
          //TODO: odcięcia
      }
      System.out.println("Best move for "+ player + " depth "+ depth  + ": " + best_move.move.toString() + " " + best_move.value);
      return best_move;
  }


  @Override
  public Move nextMove(Board b) {
    Color player = getColor();
    System.out.println ("NOWY RUCH " + player + "\n-----------------------------");


    long endRoundTime = System.nanoTime() + getTime()*1000000;
    BestMove bestMove = NegMax(b, player, MAX_DEPTH, endRoundTime);

    return bestMove.move;
  }
}
