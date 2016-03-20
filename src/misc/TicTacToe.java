package misc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

// Strategy pattern.
// The computer player's behavior/strategy can be replaced by inheriting from the interface below
// Also, the human player's behavior inherits from the same interface
// This also makes it easy to modify the game for 2 human players, 2 computer players etc.

interface IReadOnlyBoard
{
    public MarkerType getCell(int row, int col);
    public int getFirstEmptyCell();
    public int getSize();
}

interface MoveMethod
{
    public int move(IReadOnlyBoard board);
}

class SimpleMoveStrategy implements MoveMethod
{
    public SimpleMoveStrategy() {
    }

    public int move(IReadOnlyBoard board) {
        return board.getFirstEmptyCell();
    }
}

class HumanMove implements MoveMethod
{
    public HumanMove() {
    }
    
    public int move(IReadOnlyBoard board) {
        String move_str ;
        int move_int = 0 ;
        boolean valid_input = false ;
        while(!valid_input) {
            System.out.print("Where to ? ");
            move_str = Util.getUserInput() ;
            try {
                move_int = Integer.parseInt(move_str);
                if( ( move_int <= (board.getSize())*(board.getSize()) ) &&
                        move_int >= 1 ) {
                    valid_input = true;
                }
            } catch(NumberFormatException ex) {
                ex.printStackTrace();
            }

            if( !valid_input ) {
                System.out.println("Invalid input");
            }
        }
        return move_int ;
    }
}

enum MarkerType {
    NONE, NOUGHT, CROSS
}

class Player
{
    private String name ;
    private MarkerType marker_type ;
    private MoveMethod move_strategy ;

    public Player(String pname, MarkerType type, MoveMethod move_s )
    {
        name = pname ;
        marker_type = type ;
        move_strategy = move_s ;
    }

    public String getName() {
        return name ;
    }

    public MarkerType getPlayerType() {
        return marker_type ;
    }

    public int getMove(IReadOnlyBoard board) {
        return move_strategy.move(board);
    }
}

class SquareBoard implements IReadOnlyBoard
{
    private MarkerType[][] board;
    private final int SIZE;
    
    public SquareBoard(int size) {
        this.SIZE = size;
        board = new MarkerType[SIZE][SIZE];
        
        for(int i=0; i<SIZE; i++)
            Arrays.fill(board[i], MarkerType.NONE);
    }
    
    @Override
    public MarkerType getCell(int row, int col) {
        if (row >=0 && row < SIZE && col >= 0 && col < SIZE )
            return board[row][col];
        
        throw new IllegalArgumentException();
    }
    
    
    @Override
    public int getFirstEmptyCell() {
        for(int i=0; i<SIZE; i++)
            for(int j=0; j<SIZE; j++)
                if (isCellFree(i, j))
                    return getBoardPosFromRowColIndex(i, j);
        
        return 0;
    }
    
    @Override
    public int getSize() {
        return SIZE;
    }
    
    boolean isCellFree(int row, int col) {
        return getCell(row, col) == MarkerType.NONE;
    }
   
    int getBoardPosFromRowColIndex(int row, int col) {
        int boardPos = -1; // default value when row/col is invalid.
        if (row >= 0 && row < SIZE && col>=0 && col < SIZE )
            boardPos = row*SIZE + col + 1;
    
        return boardPos;
    }
    
    boolean setCellIfEmpty(int pos, MarkerType marker) {
        int rowInd = getRowIndexFromBoardPos(pos);
        int colInd = getColIndexFromBoardPos(pos);
        
        if(isCellFree(rowInd, colInd)) {
            board[rowInd][colInd] = marker;
            return true;
        }
        
        return false;
    }
    
    int getRowIndexFromBoardPos(int pos) {
        if (pos >= 1 && pos <= SIZE*SIZE)
            return (pos-1)/SIZE;

        throw new IllegalArgumentException();
    }
    
    int getColIndexFromBoardPos(int pos) {
        if (pos >= 1 && pos <= SIZE*SIZE)
            return (pos-1)%SIZE;
        
        throw new IllegalArgumentException();
    }
    
    boolean isRowIdentical(int row) {
        MarkerType firstVal = board[row][0];
        
        if (firstVal == MarkerType.NONE)
            return false;
        
        for (int col=1; col<SIZE; col++) {
            if (board[row][col] != firstVal)
                return false;
        }
            
        return true;
    }
    
    boolean isColIdentical(int col) {
        MarkerType firstVal = board[0][col];
        
        if (firstVal == MarkerType.NONE)
            return false;
        
        for (int row=1; row<SIZE; row++) {
            if (board[row][col] != firstVal)
                return false;
        }
        
        return true;
    }
    
    boolean isDiag1Identical() {
        MarkerType firstVal = board[0][0];
        
        if (firstVal == MarkerType.NONE)
            return false;
        
        for(int i=1, j=1; i<SIZE && j<SIZE; i++, j++) {
            if (board[i][j] != firstVal)
                return false;
        }
        
        return true;
    }
    
    boolean isDiag2Identical() {
        MarkerType firstVal = board[0][SIZE-1];
        
        if (firstVal == MarkerType.NONE)
            return false;
        
        for(int i=1, j=SIZE-2; i<SIZE && j>=0; i++, j--) {
            if (board[i][j] != firstVal)
                return false;
        }
        
        return true;
    }
}

class TicTacToe
{
    protected static final int N = 3 ;
    private static final int HSPACE = 20 ;
    private SquareBoard board;
    private Player player1,player2;

    public static String getPosDescription(int pos) {
        String str = "";
        if( pos == 5 ) {
            str = "center" ;
            return str ;
        }

        if( (pos-1)/3 == 0 )
            str += "upper " ;
        else if( (pos-1)/3 == 1 )
            str += "middle " ;
        else
            str += "lower " ;

        if( (pos-1) % 3 == 0 )
            str += "left" ;
        else if( (pos-1)%3 == 1 )
            str += "middle" ;
        else
            str += "right" ;

        return str ;
    }

    public TicTacToe(Player player1, Player player2)
    {
        board = new SquareBoard(N);
        this.player1 = player1;
        this.player2 = player2;
    }
    
    public void play() {
        int move1, move2;
        WinConfig w = WinConfig.NONE;
        
        System.out.println("Please make your move selection by entering a " +
                "number 1-9 corresponding to the movement key on the right.\n");
        System.out.println(toString()) ;

        while( isWinningConfig() == WinConfig.NONE  )
        {
            do {
            move1 = player1.getMove(this.board);
            } while(!setMove(move1, player1.getPlayerType() ));


            if( ( w = isWinningConfig() ) == WinConfig.WIN ) {
                System.out.println("");
                System.out.println(toString() );
                System.out.println("You have beaten my poor AI!");
                break ;
            }
            else if( w == WinConfig.DRAW ) {
                System.out.println("");
                System.out.println(toString()) ;
                System.out.println("Well played. It is a draw!");
                break ;
            }

            move2 = player2.getMove(this.board);
            System.out.println("");
            System.out.println("You have put an X in the " +
                    TicTacToe.getPosDescription(move1) + ". I will put a O in the " +
                    TicTacToe.getPosDescription(move2) + "." ) ;
            setMove(move2, player2.getPlayerType() ) ;

            if( ( w = isWinningConfig() ) == WinConfig.WIN ) {
                System.out.println("");
                System.out.println(toString() );
                System.out.println("I won. Thanks for playing.") ;
                break ;
            }
            else if( w == WinConfig.DRAW ) {
                System.out.println("");
                System.out.println(toString()) ;
                System.out.println("Well played. It is a draw!");
                break ;
            }
            
            System.out.println(toString());
        }
    }
    
    public boolean setMove(int move, MarkerType p_type)
    {
        boolean isValidMove = false;
        
        try {
            if (board.setCellIfEmpty(move, p_type))
                isValidMove = true;
            
        } catch (IllegalArgumentException ex) {
            isValidMove = false;
        }
        
        if (!isValidMove) {
            System.out.println("Invalid move");
            return false;
        }
        
        return true;
    }

    private enum WinConfig {
        DRAW, WIN, NONE
    }

    private WinConfig isWinningConfig()
    {
        WinConfig w = WinConfig.WIN ;
        // rows
        for( int i = 0 ; i < N ; i ++ )
        {
            if (board.isRowIdentical(i))
                return w;
        }
        // columns
        for( int i = 0 ; i < N ; i ++ )
        {
            if (board.isColIdentical(i))
                return w;
        }
        // diags
        if (board.isDiag1Identical() || board.isDiag2Identical())
            return w;

        // draw
        w = WinConfig.DRAW ;
        for( int i = 0 ; i < N ; i ++ )
            for( int j = 0 ; j < N ; j ++ )
                {
                    if( board.isCellFree(i, j) )
                        return WinConfig.NONE ;
                }
        return w ;
    }

    private String getRowString(int row)
    {
        String s = "" ;
        for( int i = 0 ; i < N ; i ++ )
        {
            switch(board.getCell(row, i)) {
            case NONE: s += " " ;
                break ;
            case NOUGHT: s += "O" ;
                break ;
            case CROSS: s += "X" ;
            }

            if( i != N-1 )
                {
                    s += " | " ;
                }
        }

        s += String.format("%" + HSPACE + "s", "");

        for( int i = 0 ; i < N ; i ++ )
        {
            s += board.getBoardPosFromRowColIndex(row, i) ;

            if( i == N-1 ) {
                s += "\n";
            }
            else {
                s += " | " ;
            }
        }
        return s;
    }

    public String toString()
    {
        String s = "";
        // iterate through the rows
        for( int i = 0 ; i < N ; i ++ ) {
            s += getRowString(i);
        }
        return s;
    }

    public static void main( String[] args )
    {
        System.out.println("Welcome to Tic-Tac-Toe.");
        System.out.println("");
        Player player1, player2;

        System.out.println("Enter player name");
        player1 = new Player(Util.getUserInput(),MarkerType.CROSS,new HumanMove());
        player2 = new Player("",MarkerType.NOUGHT,new SimpleMoveStrategy());
        
        System.out.println("\nHuman player " + player1.getName() +
                " vs Computer Player " + player2.getName() + ":" ) ;

        TicTacToe game = new TicTacToe(player1, player2);
        game.play();
    }
}

class Util
{
    private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in)) ;
    static String getUserInput() {
        String input = "" ;
        try {
            input = reader.readLine();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return input ;
    }
}