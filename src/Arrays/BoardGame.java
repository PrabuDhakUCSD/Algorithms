package Arrays;
import java.util.*;

/*
 * You’re given a board game which is a row of squares, each labeled with an integer.
 * This can be represented by a list, e.g. [1, 3, 2, 0, 5, 2, 8, 4, 1]
 * 
 * Given a start position on the board, you “win” by landing on a zero, where you move
 * by jumping from square to square either left or right the number of spaces specified
 * on the square you’re currently on.
 */
public class BoardGame {

    public static boolean canWin(int[] board, int startPos) {
        // assume board is valid
        Set<Integer> beingVisited = new HashSet<Integer>();
        int[] winFromIndex = new int[board.length];
        Arrays.fill(winFromIndex, 0, winFromIndex.length, -1);
        int[] recursionCount = new int[1];
        int winPossibility = helper(board, startPos, beingVisited,
                winFromIndex, recursionCount);
        System.out.print("Recursion count: " + recursionCount[0] + "-----");
        assert(winPossibility != -1);
        return winPossibility == 1;
    }

    public static int helper(int[] board, int startPos, Set<Integer> beingVisited,
            int[] winFromIndex, int[] recursionCount) {
        recursionCount[0]++;
        
        if (startPos < 0 || startPos >= board.length || beingVisited.contains(startPos))
            return 0;

        if (winFromIndex[startPos] != -1)
            return winFromIndex[startPos];
        
        if (board[startPos] == 0) {
            winFromIndex[startPos] = 1;
        } else {
            beingVisited.add(startPos);
            if (helper(board, startPos-board[startPos], beingVisited, winFromIndex, recursionCount) == 1 ||
                helper(board, startPos+board[startPos], beingVisited, winFromIndex, recursionCount) == 1) {
                winFromIndex[startPos] = 1;
            } else {
                winFromIndex[startPos] = 0;
            }
            beingVisited.remove(startPos);
        }

        return winFromIndex[startPos];
    }
    public static void main(String[] args) {
        int[] input = new int[] {1,3,2,0,5,2,8,4,5,1,0,7,4,2,3,0,5,3,10,11};
        for(int i=0; i<input.length; i++)
            System.out.println(i + " -------- " + canWin(input, i));
    }
}