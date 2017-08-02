class TicTacToe {
   Board board;
   Player player1, player2;
   IWinAlgo winAlgo;

   public TicTacToe() {
      player1 = new HumanPlayer("foo", MarkerType.NOUGHT);
      player2 = new ComputerPlayer("bar", MarkerType.NOUGHT;
   }

   public void playGame() {
      while(true) {
         while( winAlgo.getWinConfig(board) == WinConfig.INPROGRESS) {
            // board.setValue(player1.getNextMove(board), player1.markerType);
            // board.setValue(player2.getNextMove(board), player2.markerType);
         }
      }
   }
}

enum MarkerType {
   NOUGHT,
   CROSS
}

enum WinConfig {
   WIN,
   DRAW,
   INPROGRESS
}

// This interface helps to expose only readonly properties of our Board concrete class
// to Player.getNextMove method.
interface IReadOnlyBoard {
   MarkerType getValue(int position);
   boolean isFree(int position);
   WinConfig getCurrentState();
   int getRowCount();
   int getColCount();
}

interface IWinAlgo {
   WinConfig getWinConfig(IReadOnlyBoard);
}

class SimpleWinAlgo implements IWinAlgo {
   @Override
   WinConfig getWinConfig(IReadOnlyBoard board){
   }
}

class Board implements IReadOnlyBoard {
   MarkerType[][] grid;


   public Board() {
      // init grid
   }

   @Override
   MarkerType getValue(int position) {
      // throw if the position is not valid
   }

   @Override
   boolean isFree(int position) {
      // throw if the position is not valid
   }

   @Override
   int getRowCount() {
   }

   @Override
   int getColCount() {
   }

   void setValue(int position, MarkerType markerType) {
      // throw if the position is not free
   }
}

abstract class Player {
   String name;
   MarkerType markerType;

   public Player(String name, MarkerType markerType) {
      // assign the values
   }

   abstract int getNextMove(IReadOnlyBoard);
}

class ComputerPlayer extends Player {
   public ComputerPlayer(String name, MarkerType markerType){
      super(name, markerType);
   }

   @Override
   int getNextMove(IReadOnlyBoard board) {
      // get next free slot in board and return it
   }
}

class HumanPlayer extends Player {
   public HumanPlayer(String name, MarkerType markerType){
      super(name, markerType);
   }

   @Override
   int getNextMove(IReadOnlyBoard board) {
       // prompt user for a position
      // validate that position is free; if not free prompt again
      // return a valid position
   }   
}
