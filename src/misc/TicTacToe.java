package misc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

// Strategy pattern.
// The computer player's behavior/strategy can be replaced by inheriting from the interface below
// Also, the human player's behavior inherits from the same interface
// This also makes it easy to modify the game for 2 human players, 2 computer players etc.

interface MoveMethod
{
    public int move();
}

class SimpleMoveStrategy implements MoveMethod
{
    private TicTacToe game;
    public SimpleMoveStrategy(TicTacToe t) {
        game = t ;
    }

    public int move() {

        for( int i = 0 ; i < TicTacToe.N ; i ++ )
            {
                for( int j = 0 ; j < TicTacToe.N ; j ++ )
                    {
                        if( game.board[i][j] == 0 )
                            return (TicTacToe.getBoardPosFromRowColIndex(i, j)) ;
                    }
            }
        return 0 ;
    }
}

class HumanMove implements MoveMethod
{
    private TicTacToe game;
    public HumanMove(TicTacToe t) {
        game = t ;
    }
    public int move() {
        String move_str ;
        int move_int = 0 ;
        boolean valid_input = false ;
        while(!valid_input) {
            System.out.print("Where to ? ");
            move_str = TicTacToe.getUserInput() ;
            try {
                move_int = Integer.parseInt(move_str);
                if( ( move_int <= (TicTacToe.N)*(TicTacToe.N) ) && move_int >= 1 ) {
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

class TicTacToe
{
    protected static final int N = 3 ;
    private static final int HSPACE = 20 ;
    protected int[][] board;
    private static BufferedReader reader =
        new BufferedReader(new InputStreamReader(System.in)) ;

    class Player
    {
        private String name ;
        private int player_type ;
        private int player_order ;
        private MoveMethod move_strategy ;

        public Player(String pname, int type, int order, MoveMethod move_s )
        {
            name = pname ;
            player_type = type ;
            player_order = order ;
            move_strategy = move_s ;
        }

        public String getName() {
            return name ;
        }

        public int getPlayerType() {
            return player_type ;
        }

        public int getMove() {
            return move_strategy.move();
        }
    }

    private Player player1,player2;

    public Player getplayer1() {
        return player1 ;
    }

    public Player getplayer2() {
        return player2 ;
    }

    public static String getPosDescription(int pos) {
        String str = "";
        if( pos == 5 ) {
            str = "center" ;
            return str ;
        }

        if( (pos-1)/3 == 0 ) {
            str += "upper " ;
        }
        else if( (pos-1)/3 == 1 ) {
            str += "middle " ;
        }
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

    protected static String getUserInput() {
        String input = "" ;
        try {
            input = reader.readLine();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return input ;
    }

    public TicTacToe()
    {
        board = new int[N][N];
        for( int i = 0 ; i < N ; i ++ ) {
            for( int j = 0 ; j < N ; j ++ ) {
                board[i][j] = 0 ;
            }
        }

        System.out.println("Enter player name");
        player1 = new Player(getUserInput(),2,0,new HumanMove(this));

        player2 = new Player("",1,1,new SimpleMoveStrategy(this));
        System.out.println("\nHuman player " + player1.getName() + " vs Computer Player " + player2.getName() + ":" ) ;
    }

    public boolean setMove(int move, int p_type)
    {
        int x_coord;
        int y_coord;
        boolean isValidMove = false;
        
        try {
            x_coord = TicTacToe.getRowIndexFromBoardPos(move);
            y_coord = TicTacToe.getColIndexFromBoardPos(move);
            
            if( board[x_coord][y_coord] == 0 )
            {
                board[x_coord][y_coord] = p_type ;
                isValidMove = true;
            } else {
                isValidMove = false;
            }
            
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
            if (checkRow(i))
                return w;
        }
        // columns
        for( int i = 0 ; i < N ; i ++ )
        {
            if (checkCol(i))
                return w;
        }
        // diags
        if (checkDiag1() || checkDiag2())
            return w;

        // draw
        w = WinConfig.DRAW ;
        for( int i = 0 ; i < N ; i ++ )
            for( int j = 0 ; j < N ; j ++ )
                {
                    if( board[i][j] == 0 )
                        return WinConfig.NONE ;
                }
        return w ;

    }

    private boolean checkRow(int row) {
        int firstVal = board[row][0];
        
        if (firstVal == 0)
            return false;
        
        for (int col=1; col<N; col++) {
            if (board[row][col] != firstVal)
                return false;
        }
            
        return true;
    }
    
    private boolean checkCol(int col) {
        int firstVal = board[0][col];
        
        if (firstVal == 0)
            return false;
        
        for (int row=1; row<N; row++) {
            if (board[row][col] != firstVal)
                return false;
        }
        
        return true;
    }
    
    private boolean checkDiag1() {
        int firstVal = board[0][0];
        
        if (firstVal == 0)
            return false;
        
        for(int i=1, j=1; i<N && j<N; i++, j++) {
            if (board[i][j] != firstVal)
                return false;
        }
        
        return true;
    }
    
    private boolean checkDiag2() {
        int firstVal = board[0][N-1];
        
        if (firstVal == 0)
            return false;
        
        for(int i=1, j=N-2; i<N && j>=0; i++, j--) {
            if (board[i][j] != firstVal)
                return false;
        }
        
        return true;
    }
    
    private String getRowString(int row)
    {
        String s = "" ;
        for( int i = 0 ; i < N ; i ++ )
        {
            switch(board[row][i]) {
            case 0: s += " " ;
                break ;
            case 1: s += "O" ;
                break ;
            case 2: s += "X" ;
            }

            if( i != N-1 )
                {
                    s += " | " ;
                }
        }

            s += String.format("%" + HSPACE + "s", "");

         for( int i = 0 ; i < N ; i ++ )
          {
            s += TicTacToe.getBoardPosFromRowColIndex(row, i) ;

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
    
    public static int getBoardPosFromRowColIndex(int row, int col) {
        int boardPos = -1; // default value when row/col is invalid.
        if (row >= 0 && row < N && col>=0 && col < N )
            boardPos = row*N + col + 1;
    
        return boardPos;
    }
    
    public static int getRowIndexFromBoardPos(int pos) {
        if (pos >= 1 && pos <= N*N)
            return (pos-1)/N;

        throw new IllegalArgumentException();
    }
    
    public static int getColIndexFromBoardPos(int pos) {
        if (pos >= 1 && pos <= N*N)
            return (pos-1)%N;
        
        throw new IllegalArgumentException();
    }

    public static void main( String[] args )
    {
        System.out.println("Welcome to Tic-Tac-Toe.");
        System.out.println("");

        TicTacToe game = new TicTacToe();
        String move_str ;
        int move1 = 0 ;
        int move2 = 0 ;
        int player_type = 0 ;
        WinConfig w = WinConfig.NONE ;

        System.out.println("Please make your move selection by entering a number 1-9 corresponding to the movement key on the right.\n");
        System.out.println(game.toString()) ;

        while( game.isWinningConfig() == WinConfig.NONE  )
        {
            do {
            move1 = game.getplayer1().getMove();
            } while(!game.setMove(move1,game.getplayer1().getPlayerType() ));


            if( ( w = game.isWinningConfig() ) == WinConfig.WIN ) {
                System.out.println("");
                System.out.println(game.toString() );
                System.out.println("You have beaten my poor AI!");
                break ;
            }
            else if( w == WinConfig.DRAW ) {
                System.out.println("");
                System.out.println(game.toString()) ;
                System.out.println("Well played. It is a draw!");
                break ;
            }

            move2 = game.getplayer2().getMove();
            System.out.println("");
            System.out.println("You have put an X in the " + TicTacToe.getPosDescription(move1) + ". I will put a O in the " + TicTacToe.getPosDescription(move2) + "." ) ;
            game.setMove(move2, game.getplayer2().getPlayerType() ) ;

            if( game.isWinningConfig() == WinConfig.WIN ) {
                System.out.println("");
                System.out.println(game.toString() );
                System.out.println("I won. Thanks for playing.") ;
                break ;
            }
            System.out.println(game.toString());
        }

    }
}
