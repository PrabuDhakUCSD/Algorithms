package strings;

import java.util.Arrays;

/*
 * Given an int array, generate all combinations of it without 
 * duplicates.
 * 
 * Input: [3,7,9]
 * Output:
 *  3
 *  7
 *  9
 *  3,7
 *  3,9
 *  7,9
 *  3,7,9
 *  
 *  Input: [3,7,7]
 *  Output:
 *  3
 *  7
 *  3,7
 *  7,7
 *  3,7,7
 */
public class AllCombinations {

    public static void printCombinations(int[] input) {
        int len = input.length;
        int[] output = new int[len];
        
        // to avoid duplicate combinations.
        Arrays.sort(input);
        
        helper(input, 0, len, output, 0);
    }
    
    public static void helper(int[] input, int ipInd, int len, int[] output,
                              int opInd) {
        if (ipInd >= len)
            return;
        
        // include current input char
        output[opInd] = input[ipInd];
        print(output, opInd);
        
        helper(input, ipInd+1, len, output, opInd+1);
        
        // skip all characters which are same as current char.
        while (ipInd+1 < len && input[ipInd+1] == input[ipInd]) {
            ipInd++;
        }
        
        helper(input, ipInd+1, len, output, opInd);
    }
    
    private static void print(int[] output, int lastInd) {
        for(int i=0; i<=lastInd; i++) {
            System.out.print(output[i]);
            if (i != lastInd)
                System.out.print(", ");
        }
        
        System.out.println();
    }
    
    public static void main(String[] args) {
        printCombinations(new int[] {9, 3, 7});
        System.out.println("--------------");
        printCombinations(new int[] {7, 9, 7});
        System.out.println("--------------");
        printCombinations(new int[] {7,7,7});
    }
}
