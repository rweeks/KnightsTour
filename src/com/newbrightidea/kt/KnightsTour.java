package com.newbrightidea.kt;

import java.util.LinkedList;
import java.util.Comparator;
import java.util.Collections;

public class KnightsTour
{
  private static final byte[][] MOVES = { {1, -2}, {2, -1}, {2, 1}, {1, 2}, {-1, 2}, {-2, 1}, {-2, -1}, {-1, -2} };

  private KnightsTour () {}

  private static void tourFrom(byte x, byte y, byte[] board, LinkedList<byte[]> moves)
  {
    visit(x, y, board, moves);
    for (byte[] move: MOVES)
    {
      if ( (x + move[0] < 0) || (x + move[0] > 7) ||
           (y + move[1] < 0) || (y + move[1] > 7) )
      {
        continue;
      }
      if (!isVisited((byte)(x + move[0]), (byte)(y + move[1]), board))
      {
        tourFrom((byte)(x + move[0]), (byte)(y + move[1]), board, moves);
      }
      if ( isComplete(moves) )
      {
        return;
      }
    }
    unvisitLast(board, moves);
  }

  private static void visit(byte x, byte y, byte[] board, LinkedList<byte[]> moves)
  {
    int xmask = 1 << (7-x);
    board[y] |= xmask;
    moves.push( new byte[] { x, y } ); 
  }

  private static void unvisitLast(byte[] board, LinkedList<byte[]> moves)
  {
    byte[] move = moves.pop();
    byte xmask = (byte)(1 << (7-move[0]));
    xmask = (byte)~xmask;
    board[move[1]] &= xmask;
  }

  private static boolean isVisited(byte x, byte y, byte[] board)
  {
    return ((board[y] >> (7 - x)) & (byte)1) == (byte)1;
  }

  private static boolean isComplete(LinkedList<byte[]> moves)
  {
    return (moves.size() == 64);
  }

  public static void main(String[] args)
  {
    byte x = Byte.parseByte(args[0]);
    byte y = Byte.parseByte(args[1]);
    LinkedList<byte[]> moves = new LinkedList<byte[]>();
    byte[] board = new byte[8];
    long startTime = System.currentTimeMillis();
    tourFrom(x, y, board, moves);
    long duration = System.currentTimeMillis() - startTime;
    System.out.printf("Search time: %4.4f sec\n", duration / 1000.0f);
    if ( isComplete(moves) )
    {
      System.out.println( "Found solution:" );
      Collections.reverse(moves);
      for ( byte[] move: moves )
      {
        System.out.printf( "(%2d, %2d)\n", move[0], move[1]);
      }
      System.out.println( "Sorted: " );
      Collections.sort(moves, new Comparator<byte[]>() {
        public int compare(byte[] o1, byte[] o2)
        {
          byte c1 = (o1[0] == o2[0]) ? o1[1] : o1[0];
          byte c2 = (o1[0] == o2[0]) ? o2[1] : o2[0];
          if ( c1 > c2 )
          {
            return 1;
          }
          else if ( c1 < c2 )
          {
            return -1;
          }
          else
          {
            return 0;
          }
        }
      });
      for ( byte[] move: moves )
      {
        System.out.printf( "(%2d, %2d)\n", move[0], move[1]);
      }
    }
    else
    {
      System.out.println( "No solution." );
    }
  }
}
