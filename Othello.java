
import java.util.Scanner;

/** Class Othello
  * represents a two person game of Othello (also known as Reversi or Black and White Chess). 
  * The game starts on a 2D board with two black and two white pegs on diagonals in the 
  * centre of the board. The players alternate turns starting with black.
  * The player puts a peg on one of the squares and where the new peg forms a line
  * that surrounds the opponent's pegs in any direction, the pegs in that line switch colours.
  * The game ends when the board is filled with pegs.
  */
public class Othello
{
  // Declare the constants
  /** A constant representing an empty square on the board. */
  private static final char NO_CHIP = ' ';
  /** A constant representing a black peg on the board. */
  private static final char BLACK_UP = 'b';
  /** A constant representing a white peg on the board. */
  private static final char WHITE_UP = 'w';
  /** A constant indicating the size of the game board. */
  private static final int BOARD_SIZE = 8;
  
  // Declare the instance variables
  /** This array keeps track of the logical state of the game. */
  private char[][] grid = new char[BOARD_SIZE][BOARD_SIZE]
    ;
  /** This board contains the physical state of the game. */
  private Board gameBoard = new Board(BOARD_SIZE, BOARD_SIZE);
  
  
  /** 
   * Othello constructor.
   * 
   * PRE: none
   * POST: Logically and physically intializes the Othello game with pegs.
   */ 
  public Othello()
  {
    // ADD CODE HERE
    for (int i=0; i<grid.length; i++)
    {
      for (int j=0; j<grid[i].length; j++)
      {
        grid[i][j]=NO_CHIP;
      }
    }
    grid[3][3]=WHITE_UP;
    grid[3][4]=BLACK_UP;
    grid[4][3]=BLACK_UP;
    grid[4][4]=WHITE_UP;
    
    // This method must be called to refresh the board after the logic of the game has changed
    this.updateView();
  }

  
  /**
   * This method will initiate the play of the Othello game.
   * A player takes a turn placing a peg on the board which
   * will cause one or more pegs of the opposite colour to be switched. 
   * Once the game is over, the message "Game over. The winner is x.", 
   * where x is the colour of the player that has the most pegs up
   * at the end of the game. If the player's have the same number of
   * pegs up then the message "Game over. It is a tie game." should appear.
   * 
   * PRE: this is properly constructed
   * POST: a game of Othello is completed
   */
  public void play()
  {
    // DO NOT MODIFY THIS CODE
    // First move is made by black
    char move = BLACK_UP;
    while (!this.gameOver())
    {
      this.displayStatus(move);
      int row = 0;
      int col = 0;
      boolean valid = false;
      
      // Get a click from the player until he/she chooses a valid location
      while (!valid)
      {
        Coordinate c = this.gameBoard.getClick();
        row = c.getRow();
        col = c.getCol();
        valid = this.validMove(move, row, col);
      }
     
      this.takeTurn(move, row, col);
      // After the turn, switch the players
      if (move == BLACK_UP)
      {
        move = WHITE_UP;
      } else
      {
        move = BLACK_UP;
      }
    }
    this.endGame();
  }
    
  /** 
   * This method will handle the logic of a single turn in the Othello game.
   * It should "flip" the opponents pegs wherever they are surrounded in a line
   * by the latest peg played and the first peg of the player's same colour in
   * that line.
   * The method this.updateView() should be called at the end of this method.
   * 
   * PRE: 0 <= row < this.gameBoard.getRows() && 0 <= col < this.gameBoard.getColumns()
   *      colour == BLACK_UP || WHITE_UP
   *      The row and col values are a valid location for a move in the game.
   * POST: The pegs of the opposite colour are flipped according to the rules of Othello.
   */
  private void takeTurn(char turn, int row, int col)
  {
    // ADD CODE HERE
    //Check Above
    grid[row][col]=turn;
    //check above & below
    direction(row, col, turn, 0, -1);
    direction(row, col, turn, 0, 1);
    //check right & right 
    direction(row, col, turn, 1,0);
    direction(row, col, turn, -1, 0);
    //check corners
    direction(row, col, turn, 1,1);
    direction(row, col, turn, 1,-1);
    direction(row, col, turn, -1,1);
    direction(row, col, turn, -1,-1);
    
    // This method must be called to refresh the board after the logic of the game has changed
    this.updateView();
  }
  /*This method will check the colours of pegs in an indicated direction
   *  PRE: 0 <= row < this.gameBoard.getRows() && 0 <= col < this.gameBoard.getColumns()
   *      colour == BLACK_UP || WHITE_UP
   *      The row and col values are a valid location for a move in the game.
   * POST: The pegs of the opposite colour are flipped according to the rules of Othello
   *       when the method is called and the parameters are entered correctly.
   */
  private void direction(int row, int column, char colour, int colDir, int rowDir)
  {
    int currentRow= row + rowDir;
    int currentCol = column + colDir;
    if (currentRow==8 || currentRow<0 || currentCol==8 || currentCol<0)
    {
      return;
    }
    while (grid[currentRow][currentCol]==BLACK_UP || grid[currentRow][currentCol]==WHITE_UP)
    {
      if (grid[currentRow][currentCol]==colour)
      {
        while(!(row==currentRow && column==currentCol))
        {
          grid[currentRow][currentCol]=colour;
          currentRow=currentRow-rowDir;
          currentCol=currentCol-colDir;
        }
        break;
      }else
      {
      currentRow=currentRow + rowDir;
      currentCol=currentCol + colDir;
      }
      if (currentRow<0 || currentCol<0 || currentRow==8 || currentCol==8)
      { 
        break;
      }
    }
  }
  /** 
   * This method will determine if the player has selected a valid move.
   * A valid move is when the player has selected an empty square, adjacent
   * to a peg of the opposite colour on the board.
   * 
   * PRE: none
   * POST: Returns true when the move was valid, false otherwise.
   */
  private boolean validMove(char turn, int row, int col)
  {
    // ADD CODE HERE
    boolean result=false;
    char oppCol=BLACK_UP;
    if (turn==BLACK_UP)
    {
      oppCol=WHITE_UP;
    }

  //current
  if (grid[row][col]==NO_CHIP)
  {
    if (row+1<8 && col+1<8 && grid[row+1][col+1]==oppCol)
    { 
      result=true;
    }else if(row+1<8 && grid[row+1][col]==oppCol)
    {
      result=true;
    }else if(col+1<8 && grid[row][col+1]==oppCol)
    {
      result=true;
    }else if (col-1>-1 && grid[row][col-1]==oppCol)
    {
      result=true;
    }else if (row-1>-1 && col-1>-1 && grid[row-1][col-1]==oppCol)
    {
      result=true;
    }else if (row-1>-1 && grid[row-1][col]==oppCol)
    { 
      result=true;
    }else if(row-1>-1 && col+1<8 && grid[row-1][col+1]==oppCol)
    {
      result=true;
    }else if (row+1<8 && col-1>-1 && grid[row+1][col-1]==oppCol)
    {
      result = true;
    }
  }
  return result;
}
        
  /**
   * This method will display the current status of the game.
   * The message will appear as
   * **black** has x pieces up --- white has y pieces up OR
   * black has x pieces up --- **white** has y pieces up
   * will appear at the bottom of the board, where x and y are numbers
   * indicating how many pegs of that colour are showing. The ** **
   * surrounding one of the colours indicates whose turn it is.
   * PRE: turn == BLACK_UP || WHITE_UP
   * POST: a message is displayed on the board that shows the number
   *       of pieces each player has. There are **  ** surrounding
   *       the colour of the player whose turn is next.
   */
  private void displayStatus(char turn)
  {
    // ADD CODE HERE
    int countb=0;
    int countw=0;
    for (int i=0; i<8; i++)
    {
      for (int j=0; j<8; j++)
      {
        if (grid[i][j]==BLACK_UP)
        {
          countb++;
        }else if(grid[i][j]==WHITE_UP)
        {
          countw++;
        }
      }
    }
    if (turn==BLACK_UP)
    {
      this.gameBoard.displayMessage("**black** has " +countb+ " pieces up --- white has " +countw+ " pieces up");
    }else if(turn==WHITE_UP)
    {
    this.gameBoard.displayMessage("black has " +countb+ " pieces up --- **white** has " +countw+ " pieces up");
    }
  }
  
  /** 
   * This method will determine when the game is over.
   * The game is over when the board is filled with pegs.
   * 
   * PRE: none
   * POST: Returns true when there are no valid moves left, false otherwise.
   */
  private boolean gameOver()
  {
    // ADD CODE HERE
    boolean full=false;
    int countTot=0;
    for (int i=0; i<8; i++)
    {
      for (int j=0; j<8; j++)
      {
        if (grid[i][j]==BLACK_UP || grid[i][j]==WHITE_UP)
        {
          countTot++;
        }
      }
    }
        if (countTot==64)
        {
          full=true;
        }
    return full;
  }
  
  /**
   * This method will display the message "Game over. The winner is x.", 
   * where x is the colour of the player that has the most pegs up
   * at the end of the game. If the player's have the same number of
   * pegs up then the message "Game over. It is a tie game." should appear.
   * 
   * PRE: the game is over
   * POST: a message indicating the winner of the game will appear
   */
  private void endGame()
  {
    int countw=0;
    int countb=0;     
    for (int i=0; i<grid.length; i++)     
    { 
      for (int j=0; j<grid[i].length; j++)       
      {       
        if (grid[i][j]==BLACK_UP)       
        {
          countb++;       
        }else if (grid[i][j]==WHITE_UP)       
        {
          countw++;       
        }     
      }     
    } if (countb>countw)     
    {     
      this.gameBoard.displayMessage("Game over. The winner is black.");     
    }else if (countw>countb)     
    {       
      this.gameBoard.displayMessage("Game over. The winner is white.");     
    }else     
    {       
      this.gameBoard.displayMessage("Game over. It is a tie game.");     
    }

  }
   
  /** 
   * This method will reflect the current state of the pegs on the board.
   * It should be called at the end of the constructor and the end of the
   * takeTurn method and any other time the game has logically changed.
   * 
   * NOTE: This is the only method that requires calls to putPeg and removePeg.
   *       All other methods should manipulate the logical state of the game in the 
   *       grid array and then call this method to refresh the gameBoard.
   * 
   * PRE: none
   * POST: Where the array holds a value of BLACK_UP, a black peg is put in that spot.
   *       Where the array holds a value of WHITE_UP, a white peg is put in that spot.
   *       Where the array holds a value of NO_CHIP, a peg should be removed from that spot.
   */
  private void updateView()
  {
    for (int i=0; i<grid.length; i++)
    {
      for (int j=0; j<grid[i].length; j++)
      {
        if (grid[i][j]==WHITE_UP)
        {
          gameBoard.putPeg("white",i,j);
        }else if(grid[i][j]==BLACK_UP)
        {
          gameBoard.putPeg("black",i,j);
        }
      }
    }

  }
  
  
  /*
   * This main method is included in the Othello class so 
   * that is not necessary to switch between files to run the program.
   */
  public static void main(String[] args)
  {
    Othello game = new Othello();
    game.play();
  }
  
  
}