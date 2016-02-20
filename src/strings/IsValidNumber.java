package strings;

/*
 * Check whether given string is a valid number of base10 or base16 or base8
 * Example true cases:
 *  Base10:
 *  +345
 *  +234.345
 *  -34.456
 *  .3743
 *  
 *  Hex:
 *  +0x45af
 *  -0x45.234ad
 *  0x0.34ad
 *  0x.45af
 *  
 *  Octal:
 *  04567
 *  04567.2345
 *  0.2367
 */
public class IsValidNumber {

    public static boolean isValid(String input) {
        if (input.length() == 0)
            return false;
        
        char[] ip = input.toCharArray();
        int len = ip.length;
        
        int start = 0;

        if (ip[0] == '+' || ip[0] == '-') {
            start++;
            len--;
        }
        
        if (len == 0)
            return false;
        
        if (isPrefixHex(ip, start, len))
            return validateHex(ip, start);
        
        if (isPrefixOctal(ip, start, len))
            return validateOctal(ip, start);
        
        return validateBase10(ip, start);
    }
    
    private static boolean isPrefixHex(char[] ip, int start, int len) {
        return len >= 2 && ip[start] == '0' && ip[start+1] == 'x';
    }
    
    private static boolean isPrefixOctal(char[] ip, int start, int len) {
        return len >= 1 && ip[start] == '0';
    }
    
    private static boolean validateHex(char[] ip, int start) {
        return isWellformedNumber(ip, start+2, 16);
    }
    
    private static boolean validateOctal(char[] ip, int start) {
        return isWellformedNumber(ip, start+1, 8);
    }
    
    private static boolean validateBase10(char[] ip, int start) {
        return isWellformedNumber(ip, start, 10);
    }
    
    private static boolean isWellformedNumber(char[] ip, int start, int base) {
        boolean seenDot = false;
        
        if (start == ip.length) // 0 characters
            return false;
        
        for(int i=start; i<ip.length; i++) {
           if (!validateChar(ip[i], base))
               return false;
      
           if (ip[i] == '.') {
               if (seenDot)
                   return false;
               seenDot = true;
               
               if (i+1 == ip.length) // no characters after dot.
                   return false;
           }
        }
        
        return true;
    }
    
    private static boolean validateChar(char c, int base) {
        if (base == 16)
            return (c >='0' && c<='9') || (c>='a' && c<='f') || c=='.';
        
        if (base == 10)
            return (c >='0' && c<='9') || c=='.';
        
        return (c >='0' && c<='7') || c=='.';
    }
    
    public static void main(String[] args) {
        
        // True cases
        System.out.println("All should be True now\n\n");
        System.out.println(isValid("234"));
        System.out.println(isValid("+234"));
        System.out.println(isValid("-234.456"));
        System.out.println(isValid(".987"));
        System.out.println(isValid("-.987"));
        
        System.out.println(isValid("0x2df"));
        System.out.println(isValid("-0x3b5"));
        System.out.println(isValid("+0x3cf.dfa"));
        System.out.println(isValid("0x0.af"));
        System.out.println(isValid("0x.d4f"));
        
        System.out.println(isValid("+0123.237"));
        System.out.println(isValid("00.34"));
        System.out.println(isValid("-0.76"));
        System.out.println(isValid("-0.777"));
        
        // False cases
        System.out.println("\n\nAll should be False now\n\n");
        
        System.out.println(isValid(""));
        System.out.println(isValid("+"));
        System.out.println(isValid("45."));
        System.out.println(isValid("234d"));
        System.out.println(isValid("++234"));
        System.out.println(isValid("-234.45.6"));
        System.out.println(isValid(".a987"));
        System.out.println(isValid("-.98.7"));
        
        System.out.println(isValid("0x"));
        System.out.println(isValid("0x2df."));
        System.out.println(isValid("-0x3b5x"));
        System.out.println(isValid("+0x3cf.df.a"));
        
        System.out.println(isValid("0"));
        System.out.println(isValid("+01239"));
        System.out.println(isValid("0a"));
        System.out.println(isValid("-0."));
    }
}