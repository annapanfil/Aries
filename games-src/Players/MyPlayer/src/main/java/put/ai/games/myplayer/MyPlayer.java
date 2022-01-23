package put.ai.games.myplayer;

import put.ai.games.aries.AriesBoard;
import put.ai.games.aries.AriesMove;
import put.ai.games.game.Board;
import put.ai.games.game.Move;
import put.ai.games.game.Player;

import java.util.List;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.lang.Thread.sleep;

public class MyPlayer extends Player {

  boolean debug = true;
  int MAX_DEPTH = 2;
  static int INFTY = Integer.MAX_VALUE;
  static int LOSE = -INFTY + 1;
  static int WIN = INFTY - 1;

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
      System.out.println(b.getState(b.getSize()-1, b.getSize()-1));
      if (b.getState(0,0) == Color.PLAYER2){
          if (player == Color.PLAYER2) return WIN;
          else return LOSE;
      }
      else if (b.getState(b.getSize()-1, b.getSize()-1) == Color.PLAYER1){
          if (player == Color.PLAYER1) return WIN;
          else return LOSE;
      }
      else return 0;
  }

  private int stateEvaluation(Board b, Color player){
    /* Calculate objective function value – to maximize*/

    Color opponent = getOpponent(player);
    AriesBoard board = (AriesBoard) b;

    int my_pieces = 0;
    int opponents_pieces = 0;
    int my_distance = INFTY;
    int opponents_distance = INFTY;

    for (int i=0; i<board.getSize(); i++){
      for (int j=0; j<board.getSize(); j++){
          if (b.getState(i, j) == player){
              my_pieces++;
              my_distance = min(getDistanceToWinningField(i, j, player, board), my_distance);
              if (my_distance == 0)
                  return WIN; //our winning move
          }
          else if (b.getState(i, j) == opponent){
              opponents_pieces++;
              opponents_distance = min(getDistanceToWinningField(i, j, opponent, board), opponents_distance);

              if (opponents_distance == 0)
                  return LOSE; //their winning move
          }
      }
    }

      //TODO: Add avg distance, uzależnić wagi od rozmiaru planszy
      return -9*opponents_pieces - 10*my_distance + 8*my_pieces + 7*opponents_distance;
  }


  Move chooseBestMove(List<Move> moves, Board board){
    /* Choose move with minimum objective function value */

    AriesMove best_move = null;
    int best_move_value = INFTY;
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

          if (curr_move_value == WIN) // winning move
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

       BestMove neg(){ //super, much more readable than -
          return new BestMove(-this.value, this.move);
       }

       @Override
       public String toString() {
           return value + " " + move.toString();
       }
   }

  BestMove NegMax(Board board, Color player, int depth, BestMove alpha, BestMove beta, long endRoundTime){
      System.out.println("\n New recursion " + depth + " " + player + " a " + alpha + " B " + beta);
      int val;
      if ((val = checkWinning(board, player)) != 0){
          System.out.println("Win " + player + " "+ val + " on depth " + depth);
          return new BestMove(val);
      }
      else System.out.println("No win " + player + " "+ val + " on depth " + depth);
      if (depth == 0){
          val = stateEvaluation(board, player);
          System.out.println("return " + val + " (" + player +")");
          return new BestMove(val);
      }

      List<Move> moves = board.getMovesFor(player);

      BestMove curr_move;
      Board boardNew;
      long start, end, duration;
      long maxTime = 0;

      for (Move move : moves) {
          System.out.println("Analiza ruchu " + move.toString() +" depth: "+  depth + " " +player);
          if (System.nanoTime() + maxTime > endRoundTime) {
              System.out.println(player + "Out of time :( " + System.nanoTime() + maxTime + " " + endRoundTime);
              //TODO: use bestMove()
              break;  //out of time
          }

          start = System.nanoTime();

          boardNew = board.clone();
          boardNew.doMove(move);

          curr_move = NegMax(boardNew, getOpponent(player), depth - 1, beta.neg(), alpha.neg(), endRoundTime);
          curr_move.value = -curr_move.value;

          if (curr_move.value > alpha.value) {

              curr_move.move = (AriesMove) move;
              alpha = curr_move;
              System.out.println("New best " + player +" " + curr_move);
//              if (alpha.value == WIN) break; // winning move
          }
          else{
              System.out.println("No new best " + player +" " + curr_move + move.toString() + "a: " + alpha);
          }

          if (alpha.value >= beta.value) {
              System.out.println("Return beta for "+ player + " depth "+ depth  + ": " + beta);
              return beta;
          }
          end = System.nanoTime();
          if ((duration = end - start) > maxTime) maxTime = duration;

          System.out.println("Value for move " + move.toString() + ": " + curr_move.value + " for " + player);
      }
      System.out.println("Best move for "+ player + " depth "+ depth  + ": " + alpha);
      return alpha;
  }


  @Override
  public Move nextMove(Board b){
    Color player = getColor();
    System.out.println ("NOWY RUCH " + player + "\n-----------------------------");


    long endRoundTime = System.nanoTime() + getTime()*1000000;
    BestMove bestMove = NegMax(b,
                                player,
                                MAX_DEPTH,
                                new BestMove(-INFTY),
                                new BestMove(INFTY),
                                endRoundTime);

//      try {
//          sleep(3000);
//      } catch (InterruptedException e) {
//          e.printStackTrace();
//      }
      return bestMove.move;
  }
}