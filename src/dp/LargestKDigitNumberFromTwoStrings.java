package dp;

/*
 * Given two string S1 and S2 containing numbers and integer 1<=K<=len1+len2
 * return the largest integer of K digits (call it R) formed using digits in
 * S1 and S2 while maintaining the relative order of digits unchanged i.e.,
 * digits in S1 should follow the ordering in R. Same for S2 as well. But S1 and
 * S2 can be mixed in R.
 * 
 * Eg:
 * 
 * S1 = 429
 * S2 = 683
 * 
 * K = 1 | R = 9
 * K = 2 | R = 98
 * K = 3 | R = 983
 * K = 4 | R = 9683
 * K = 5 | R = 84329
 * K = 6 | R = 684329
 * 
 * S1 = 415
 * S2 = 73
 * 
 * K = 1 | R = 7
 * K = 2 | R = 75
 * K = 3 | R = 753
 * K = 4 | R = 7453
 * K = 5 | R = 74315
 * 
 * dp[k][i][j] = largest K digit number formed using S1[1...i] and S2[1...j]
 * 
 * Assume s1 and s2 have 1 based index.
 * 
 * dp[k][0][0] = -1 // not possible with no input strings
 * dp[1][0][j] (j=1 to len2) = max(dp[1][0][j-1], s2[j])
 * dp[1][i][0] (i=1 to len1) = max(dp[1][i-1][0], s1[i])
 * 
 * dp[k][0][0] = -1
 * dp[k][i][j] = max (
 *                     dp[k][i-1][j]   // s1[i] not used in result
 *                     dp[k][i][j-1]   // s2[j] not used in result
 *                     dp[k-1][i-1][j]*10 + s1[i] // s1[i] used at end. s2[j]
 *                                                // may or may not be used
 *                     dp[k-1][i][j-1]*10 + s2[j] // s2[j] used at end. s1[i]
 *                                                // may or may not be used
 *                   )
 */
public class LargestKDigitNumberFromTwoStrings {

    public static int getLargest(int[] s1, int[] s2, int k) {
        int len1 = s1.length;
        int len2 = s2.length;
        
        if (k == 0 || len1 + len2 < k)
            // not enough characters to construct a k digit number.
            return -1;
        
        int[][][] dp = new int[k][len1+1][len2+1];

        dp[0][0][0] = -1;
        
        // fill k=1 2D array
        for(int col=1; col<=len2; col++)
            dp[0][0][col] = Math.max(dp[0][0][col-1], s2[col-1]);
        
        for(int row=1; row<=len1; row++)
            dp[0][row][0] = Math.max(dp[0][row-1][0], s1[row-1]);
        
        for(int row=1; row<=len1; row++)
            for(int col=1; col<=len2; col++)
                dp[0][row][col] = // max(leftcell, abovecell)
                Math.max(dp[0][row-1][col], dp[0][row][col-1]);
        
        // file k>=2 2D arrays
        for(int digit=1; digit<k; digit++) {
            for(int row=0; row<=len1; row++) {
                for(int col=0; col<=len2; col++) {
                    int value;
                    if (row == 0 && col == 0)
                        value = -1;
                    else if (row == 0) // first row
                        // consider only:
                        // 1. without current column (same layer)
                        // 2. with current column added to the end of value
                        //    from previous layer
                        value = Math.max(dp[digit][row][col-1],
                                formNumberFromPrevLayer(dp[digit-1][row][col-1], s2[col-1]));
                    else if (col == 0)// first col
                        // consider only:
                        // 1. without current row (same layer)
                        // 2. with current row added to the end of value
                        //    from previous layer
                        value = Math.max(dp[digit][row-1][col],
                                formNumberFromPrevLayer(dp[digit-1][row-1][col], s1[row-1]));
                    else {
                        // best of left and above cell in the same layer.
                        value = Math.max(dp[digit][row][col-1], dp[digit][row-1][col]);
                        // max(left cell from prevlayer with current col as 
                        // last digit, above cell from prevlayer with current
                        // row as last digit)
                        value = Math.max(value, Math.max(
                                formNumberFromPrevLayer(dp[digit-1][row][col-1], s2[col-1]),
                                formNumberFromPrevLayer(dp[digit-1][row-1][col], s1[row-1])));
                    }
                        
                    dp[digit][row][col] = value;
                }
            }
        }
        
        return dp[k-1][len1][len2];
    }
    
    private static int formNumberFromPrevLayer(int prevVal, int newDigit) {
        if (prevVal == -1)
            return -1;
        
        return prevVal*10 + newDigit;
    }
    
    public static void main(String[] args) {
        int[] s1 = {4,2,9};
        int[] s2 = {6,8,3};
        
        for (int digits=0; digits<=s1.length+s2.length; digits++)
            System.out.println(getLargest(s1, s2, digits));
        
        System.out.println("\n-----------\n");
        
        int[] ss1 = {4,1,5};
        int[] ss2 = {7,3};
        
        for (int digits=0; digits<=ss1.length+ss2.length; digits++)
            System.out.println(getLargest(ss1, ss2, digits));
 
    }
}