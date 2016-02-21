package dp;
/*
 * Given a pattern string and input text, return true if the pattern matches 
 * input text and false otherwise.
 * 
 * Example:
 * 
 * Pat      | Text                      | Result
 * ---------------------------------------------
 * abc      | abc                       | true
 *          | ab, abd                   | false
 * ab*c     | ac, abc, abbc, abbbbbbc   | true
 *          | "", b, bc, acd            | false
 * ab*c?d   | ad, acd, abd, abbd, abbcd | true
 *          | "", abc, abccd            | false
 * a*       | "", a, aaa                | true
 *          | c, d                      | false
 * a?       | "", a                     | true
 *          | b, aa                     | false
 *          
 * Base cases:
 * 
 * dp(i,j) = match pat[0...i] with input[0...j]
 * dp(0,0) = true
 * dp(0,any j) = false
 * dp(any i,1) = false if pat[i]!=* and pat[i]!=?
 *             = true if pat[i] = *|? and dp[i-2][j] == true
 */
public class PatternMatching {

    public static boolean match(String pat, String input) {
        int patLen = pat.length();
        int ipLen = input.length();
        
        char[] pattern = new char[patLen+1];
        char[] ip = new char[ipLen+1];
        
        // System.arraycopy(src, srcpos, dest, destpos, length)
        System.arraycopy(pat.toCharArray(), 0, pattern, 1, patLen);
        System.arraycopy(input.toCharArray(), 0, ip, 1, ipLen);
        
        boolean[][] dp = new boolean[patLen+1][ipLen+1];
        dp[0][0] = true;
        
        // Empty pattern will not match any input
        for(int j=1; j<=ipLen; j++)
            dp[0][j] = false;

        // Covers pattern 'a*' or 'a?' and empty input
        for(int i=1; i<=patLen; i++)
            dp[i][0] = (pattern[i] == '*' || pattern[i] == '?') &&
                    (dp[i-2][0] == true);
        
        for(int i=1; i<=patLen; i++) {
            for(int j=1; j<=ipLen; j++) {
                dp[i][j] = false;
                if (pattern[i] == '*') {
                    if ((dp[i-2][j]) /*do not match with * char */ || 
                            (pattern[i-1] == ip[j] && dp[i][j-1]) /*match*/)
                        dp[i][j] = true;
                }
                else if (pattern[i] == '?') {
                    if ((dp[i-2][j]) ||
                            (pattern[i-1] == ip[j] && dp[i-2][j-1]) /*match*/)
                        dp[i][j] = true;
                }
                else if (pattern[i] == ip[j] && dp[i-1][j-1]){
                    dp[i][j] = true;
                }
            }
        }
        
        return dp[patLen][ipLen];
    }
    
    public static void main(String[] args) {
        // All below should return true
        System.out.println(match("abc", "abc"));
        System.out.println(match("ab*c", "ac"));
        System.out.println(match("ab*c", "abc"));
        System.out.println(match("ab*c", "abbbbbc"));
        System.out.println(match("a*bc", "bc"));
        System.out.println(match("a*bc", "abc"));
        System.out.println(match("a*bc", "aaaabc"));
        System.out.println(match("ab*c?d", "ad"));
        System.out.println(match("ab*c?d", "acd"));
        System.out.println(match("ab*c?d", "abd"));
        System.out.println(match("ab*c?d", "abbbbd"));
        System.out.println(match("ab*c?d", "abbbbcd"));
        System.out.println(match("a?bc", "bc"));
        System.out.println(match("a?bc", "abc"));
        System.out.println(match("a*", "a"));
        System.out.println(match("a*", "aaaaa"));
        System.out.println(match("a*", ""));
        System.out.println(match("a?", "a"));
        System.out.println(match("a?", ""));
        
        System.out.println("\n\n");
        // All below should return false
        System.out.println(match("abc", "bac"));
        System.out.println(match("ab*c", "bbbc"));
        System.out.println(match("ab*c", "abbbcd"));
        System.out.println(match("ab*c", "abbbd"));
        System.out.println(match("ab*c", "abcc"));
        System.out.println(match("ab*c?d", "accd"));
        System.out.println(match("a*", "bbb"));
        System.out.println(match("a?", "aa"));
    }
}