package dp;
/*
 * Input: Set of points that can be scored in a game.
 *        ScoreA - final score of team A after n games
 *        ScoreB - final score of team B after n games
 *        
 * Output: Max number of times lead can change
 * 
 * Lead change is when lead of a team changes after playing a game. It can be
 *   - from a team leading to other team leading
 *   - lead to tie or tie to lead is not considered a lead
 *
 * dp[x][y] = max #leadchange possible to reach final score of x and y
 * dp[0][0] = 0 // 0,0 is the initial score
 * dp[x][y] = max ( dp[x-i][y-j] + isLeadChanges(x-i, y-i, x, y) for all
 *            possible points of i and j such that i<=x and j<=y and
 *            dp[x-i][y-j] is a valid reachable score.
 */
public class LeadChange {

    static class Tuple {
        Tuple(int s1, int s2, int lc) {
            this.s1 = s1;
            this.s2 = s2;
            this.maxLeadChange = lc;
        }
        
        int s1;
        int s2;
        int maxLeadChange;
    }
    public static void findLeadChange(int[] possiblePoints, int s1, int s2) {
        if (possiblePoints == null || possiblePoints.length == 0 || s1 < 0
            || s2 < 0)
            throw new IllegalArgumentException();
        
        Tuple[][] dp = new Tuple[s1+1][s2+1];
        
        for(int i=0; i<=s1; i++) {
            for (int j=0; j<=s2; j++) {
                if (i == 0 && j == 0 ) {
                    dp[i][j] = new Tuple(0, 0, 0);
                    continue;
                }
                
                dp[i][j] = new Tuple(-1,-1,-1); // -1 indicates unreachable 
                                                // score
                
                for(int p1 : possiblePoints) {
                    for(int p2: possiblePoints) {
                        if (isPossibleScore(p1, p2, i, j, dp)) {
                            int lc = dp[i-p1][j-p2].maxLeadChange + 
                                    isLeadChanged(i-p1, j-p2, i, j);
                            
                            if (lc > dp[i][j].maxLeadChange) {
                                dp[i][j].s1 = p1;
                                dp[i][j].s2 = p2;
                                dp[i][j].maxLeadChange = lc;
                            }
                        }
                    }
                }
            }
        }
        
        System.out.println("Max lead changes: " + dp[s1][s2].maxLeadChange);
        
        while(s1 != 0 || s2 != 0) {
            System.out.println(s1 + ", " + s2);
            Tuple t = dp[s1][s2];
            s1 -= t.s1;
            s2 -= t.s2;
        }
        
        System.out.println("0, 0");
    }
    
    private static int isLeadChanged(int old1, int old2, int new1,
            int new2) {
        if((old1 >  old2 && new1 < new2)
               ||(old2 >  old1 && new2 < new1)) {
            // System.out.println(String.format("%d, %d, %d, %d %d", old1, old2, new1, new2, 1));
            return 1;
        }
        
        // System.out.println(String.format("%d, %d, %d, %d %d", old1, old2, new1, new2, 0));
        return 0;
    }
    
    private static boolean isPossibleScore(int cs1, int cs2, int ts1, int ts2,
            Tuple[][] dp) {
        if (cs1 == 0 && cs2 == 0)
            return false;
        
        return cs1 <= ts1 && cs2 <= ts2 && dp[ts1-cs1][ts2-cs2].s1 != -1 &&
                dp[ts1-cs1][ts2-cs2].s2 != -1;
    }
    
    public static void main(String[] args) {
        int[] points = {0, 2, 3, 4, 6};
        findLeadChange(points, 7, 13);
    }
}