/*Design Tic-Tac-Toe
Design
Medium
Design a Tic-tac-toe game that is played between two players on anxngrid.
You may assume the following rules:
1.A move is guaranteed to be valid and is placed on an empty block.
2.Once a winning condition is reached, no more moves is allowed.
3.A player who succeeds in placing n of their marks in a horizontal, vertical, or diagonal row wins the game.
Example:
Given n = 3, assume that player 1 is "X" and player 2 is "O" in the board.
​
TicTacToe toe = new TicTacToe(3);
​
toe.move(0, 0, 1); -> Returns 0 (no one wins)
|X| | |
| | | |    // Player 1 makes a move at (0, 0).
| | | |
​
toe.move(0, 2, 2); -> Returns 0 (no one wins)
|X| |O|
| | | |    // Player 2 makes a move at (0, 2).
| | | |
​
toe.move(2, 2, 1); -> Returns 0 (no one wins)
|X| |O|
| | | |    // Player 1 makes a move at (2, 2).
| | |X|
​
toe.move(1, 1, 2); -> Returns 0 (no one wins)
|X| |O|
| |O| |    // Player 2 makes a move at (1, 1).
| | |X|
​
toe.move(2, 0, 1); -> Returns 0 (no one wins)
|X| |O|
| |O| |    // Player 1 makes a move at (2, 0).
|X| |X|
​
toe.move(1, 0, 2); -> Returns 0 (no one wins)
|X| |O|
|O|O| |    // Player 2 makes a move at (1, 0).
|X| |X|
​
toe.move(2, 1, 1); -> Returns 1 (player 1 wins)
|X| |O|
|O|O| |    // Player 1 makes a move at (2, 1).
|X|X|X|
Follow up:
Could you do better than O(n^2) permove()operation?
Could you get O(1) per move() operation?
Solution & Analysis
O(n) Time, O(n^2) Space solution
直白解法：board[][]存当前棋盘，每次move()，check横竖和两条对角线是否满足当前选手赢。*/
class TicTacToe {
    private char[][] board;
    private static char X = 'X';
    private static char O = 'O';
    private int size;
​
    /** Initialize your data structure here. */
    public TicTacToe(int n) {
        board = new char[n][n];
        size = n;
    }
​
    /** Player {player} makes a move at ({row}, {col}).
        @param row The row of the board.
        @param col The column of the board.
        @param player The player, can be either 1 or 2.
        @return The current winning condition, can be either:
                0: No one wins.
                1: Player 1 wins.
                2: Player 2 wins. */
    public int move(int row, int col, int player) {
        char c;
        if (player == 1) {
            c = X;
        } else {
            c = O;
        }
        if (board[row][col] != 0) {
            // throw error, occupied
            return 0;
        }
        board[row][col] = c;
​
        if (hasWon(row, col, size, c)) {
            return player;
        }
        return 0;
    }
​
    private boolean hasWon(int row, int col, int n, char c) {
​
        // check horizontal
        boolean rowLine = true;
        for (int i = 0; i < n; i++) {
            rowLine = rowLine && (board[i][col] == c);
        }
        // check vertical
        boolean colLine = true;
        for (int j = 0; j < n; j++) {
            colLine = colLine && (board[row][j] == c);
        }
        // check diagonal
        if (row + col == n - 1 || row == col) {
            boolean diagLine1 = true;
            boolean diagLine2 = true;
            for (int j = 0; j < n; j++) {
                diagLine1 = diagLine1 && (board[j][j] == c);
            }
            for (int j = 0; j < n; j++) {
                diagLine2 = diagLine2 && (board[n - 1 - j][j] == c);
            }
            return rowLine || colLine || diagLine1 || diagLine2;
        } else {
            return rowLine || colLine;
        }
    }
}
​
/**
 * Your TicTacToe object will be instantiated and called as such:
 * TicTacToe obj = new TicTacToe(n);
 * int param_1 = obj.move(row,col,player);
 */
// O(1) Time, O(n) Space solution
// 存每一行，每一列，每一个对角线的累计值。trick在于player 1用+1, player2用 -1，这样用取累计值的绝对值就可以通过判断是否达到n来判定是否获胜。
// Adapted from @bdwalker:
// The key observation is that in order to win Tic-Tac-Toe you must have the entire row or column. Thus, we don't need to keep track of an entire n^2 board. We only need to keep a count for each row and column. If at any time a row or column matches the size of the board then that player has won.
// To keep track of which player, I add one for Player1 and -1 for Player2. There are two additional variables to keep track of the count of the diagonals. Each time a player places a piece we just need to check the count of that row, column, diagonal and anti-diagonal.
public class TicTacToe {
    private int[] rows;
    private int[] cols;
    private int diagonal;
    private int antiDiagonal;
​
    /** Initialize your data structure here. */
    public TicTacToe(int n) {
        rows = new int[n];
        cols = new int[n];
    }
​
    /** Player {player} makes a move at ({row}, {col}).
    @param row The row of the board.
    @param col The column of the board.
    @param player The player, can be either 1 or 2.
    @return The current winning condition, can be either:
            0: No one wins.
            1: Player 1 wins.
            2: Player 2 wins. */
    public int move(int row, int col, int player) {
        int toAdd = player == 1 ? 1 : -1;
​
        rows[row] += toAdd;
        cols[col] += toAdd;
        if (row == col) {
            diagonal += toAdd;
        }
​
        if (col + row == cols.length - 1) {
            antiDiagonal += toAdd;
        }
​
        int size = rows.length;
        if (Math.abs(rows[row]) == size ||
            Math.abs(cols[col]) == size ||
            Math.abs(diagonal) == size  ||
            Math.abs(antiDiagonal) == size) {
            return player;
        }
​
        return 0;
    }
}
//Not using Math.abs(), changing matching target @hot13399:
class TicTacToe {
​
    int[] rows, cols;
    int d1, d2, n;
​
    public TicTacToe(int n) {
      rows = new int[n];
      cols = new int[n];
      d1 = 0;
      d2 = 0;
      this.n = n;
    }
​
    public int move(int row, int col, int player) {
      int val = (player == 1) ? 1 : -1;
      int target = (player == 1) ? n : -n;
​
      if(row == col) { // diagonal 
        d1 += val;
        if(d1 == target) return player;
      }
​
      if(row + col + 1 == n) { // diagonal 
        d2 += val;
        if(d2 == target) return player;
      }
​
      rows[row] += val;
      cols[col] += val;
​
      if(rows[row] == target || cols[col] == target) return player;
​
      return 0;
    }
}
// Reference
// https://leetcode.com/problems/design-tic-tac-toe/solutions/81898/Java-O(1)-solution-easy-to-understand/



//---------------------------------------------------------------------------------------------------
import java.util.Scanner;

class Main {

public
    static void main(String[] args) {

        // Variable to represent the size of our tic tac toe board
        int n = 3;

        // nxn array that represents our tic tac toe board
        char[][] board = new char[n][n];

        // Initialize our board with dashes (empty positions)
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                board[i][j] = '-';
            }
        }

        // Create a Scanner for asking the players for their names
        Scanner in = new Scanner(System.in);
        System.out.println("Tic Tac Toe is ready for play!\n");
        System.out.print("What is your name?, player 1: ");
        String p1 = in.nextLine();
        System.out.print(" What is your name?, player 2:");
        String p2 = in.nextLine();

        // Create a player1 boolean that is true if it is player 1's turn and
        // false if it is player 2's turn
        boolean player1 = true;

        // Create a gameEnded boolean and use it as the condition in the while
        // loop
        boolean gameEnded = false;
        while (!gameEnded) {

            // Draw the board
            drawBoard(board);

            // Print whose turn it is
            if (player1) {
                System.out.println(p1 + "'s Turn (x):");
            } else {
                System.out.println(p2 + "'s Turn (o):");
            }

            // Create a char variable that stores either 'x' or 'o' based on
            // what player's turn it is
            char c = '-';
            if (player1) {
                c = 'x';
            } else {
                c = 'o';
            }

            // Create row and col variables which represent indexes that
            // correspond to a position on our board
            int row = 0;
            int col = 0;

            // Only break out of the while loop once the user enters a valid
            // position
            while (true) {

                // Ask the user for what position they want to place their x or
                // o
                System.out.print("Enter a row number: ");
                row = in.nextInt();
                System.out.print("Enter a column number: ");
                col = in.nextInt();

                // Check if the row and col are outside of the board
                if (row < 0 || col < 0 || row >= n || col >= n) {
                    System.out.println("This position is off the bounds of the "
                                       "board! Try again.");

                    // Check if the position on the board the user entered is
                    // empty (has a -) or not
                } else if (board[row][col] != '-') {
                    System.out.println("Someone has already made a move at "
                                       "this position! Try again.");

                    // Otherwise, the position is valid so break out of the
                    // while loop
                } else {
                    break;
                }
            }

            // Set the position on the board at row, col to c
            board[row][col] = c;

            // Check to see if either player has won
            if (playerHasWon(board) == 'x') {
                System.out.println(p1 + " has won!");
                gameEnded = true;
            } else if (playerHasWon(board) == 'o') {
                System.out.println(p2 + " has won!");
                gameEnded = true;
            } else {

                // If neither player has won, check to see if there has been a
                // tie (if the board is full)
                if (boardIsFull(board)) {
                    System.out.println("It's a tie!");
                    gameEnded = true;
                } else {

                    // If player1 is true, make it false, and vice versa; this
                    // way, the players alternate each turn
                    player1 = !player1;
                }
            }
        }

        // Draw the board at the end of the game
        drawBoard(board);
    }

    // Make a function to draw the tic tac toe board
public
    static void drawBoard(char[][] board) {
        System.out.println("Board:");
        for (int i = 0; i < board.length; i++) {

            // The inner for loop prints out each row of the board
            for (int j = 0; j < board[i].length; j++) {
                System.out.print(board[i][j]);
            }

            // This print statement makes a new line so that each row is on a
            // separate line
            System.out.println();
        }
    }

    // Make a function to see if someone has won and return the winning char
public
    static char playerHasWon(char[][] board) {

        // Checking each row
        for (int i = 0; i < board.length; i++) {

            // The boolean inARow is true if a player has won by putting n of
            // their chars in row i and false otherwise
            boolean inARow = true;

            // The char value is one of the chars in row i; we can use this to
            // check if every other char in row i is equal to value
            char value = board[i][0];

            // First we have to check if the value is not -, because if it is,
            // that means that there is an empty spot in the row so we can
            // automatically say that inARow is false
            if (value == '-') {
                inARow = false;

                // Otherwise, we can use a nested for loop to check each
                // position in the row starting at index 1 (since index 0 is our
                // value and we don't need to check if board[i][0] equals value)
                // and check if that position equals value
            } else {
                for (int j = 1; j < board[i].length; j++) {

                    // If board[i][j] doesn't equal value, then we know that
                    // inARow is false; we can also break out of the nested for
                    // loop because we don't need to look at the rest of this
                    // row
                    if (board[i][j] != value) {
                        inARow = false;
                        break;
                    }
                }
            }

            // If inARow is true, then we know that each position in row i had a
            // char equal to value which was not a -. In other words, a player
            // has won so we can return value (the char of the winner)
            if (inARow) {
                return value;
            }
        }

        // We can use the same construction to check each column
        for (int j = 0; j < board[0].length; j++) {
            boolean inACol = true;
            char value = board[0][j];

            if (value == '-') {
                inACol = false;
            } else {

                for (int i = 1; i < board.length; i++) {
                    if (board[i][j] != value) {
                        inACol = false;
                        break;
                    }
                }
            }

            if (inACol) {

                return value;
            }
        }

        // We can use a similar construction to check the diagonals
        // Check the diagonal going from upper left to bottom right: [0][0],
        // [1][1], [2][2]...
        boolean inADiag1 = true;
        char value1 = board[0][0];
        if (value1 == '-') {
            inADiag1 = false;
        } else {
            for (int i = 1; i < board.length; i++) {
                if (board[i][i] != value1) {
                    inADiag1 = false;
                    break;
                }
            }
        }

        if (inADiag1) {
            return value1;
        }

        // Check the diagonal going from upper right to bottom left: [0][n-1],
        // [1][n-2], [2][n-3]...
        boolean inADiag2 = true;
        char value2 = board[0][board.length - 1];

        if (value2 == '-') {
            inADiag2 = false;
        } else {
            for (int i = 1; i < board.length; i++) {
                if (board[i][board.length - 1 - i] != value2) {
                    inADiag2 = false;
                    break;
                }
            }
        }

        if (inADiag2) {
            return value2;
        }

        // Otherwise nobody has not won yet
        return ' ';
    }

    // Make a function to check if all of the positions on the board have been
    // filled
public
    static boolean boardIsFull(char[][] board) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] == '-') {
                    return false;
                }
            }
        }
        return true;
    }
}