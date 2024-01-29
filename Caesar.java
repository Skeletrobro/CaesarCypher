public class Caesar {
    /*
    * Description: converts a string to a symbol array,
    *              where each element of the array is an
    *              integer encoding of the corresponding
    *              element of the string.
    * Input:  the message text to be converted
    * Output: integer encoding of the message
    */
    public static int[] stringToSymbolArray(String str) {
        str = str.toUpperCase();
        int[] array = new int[str.length()];

        for (int i = 0; i < array.length; i++) {
            array[i] = (int) str.charAt(i) - 'A';
        }
        return array;
    }
    
    /*
    * Description: converts an array of symbols to a string,
    *              where each element of the array is an
    *              integer encoding of the corresponding
    *              element of the string.
    * Input:  integer encoding of the message
    * Output: the message text
    */
    public static String symbolArrayToString(int[] symbols) {
        String convertedString = "";
        String collectedLetters = "";
        for (int i = 0; i < symbols.length; i++) {
            collectedLetters = "" + (char) (symbols[i] + 'A');
            convertedString = convertedString + collectedLetters;
        }
        return convertedString;
    }
    
    /*
    * Description: Shifts a letter (ASCII form) forward based on the offset value 
    * and returns the new letter in its ASCII form. If the symbol is not a letter
    * (EX: punctuations --> symbols not represented by ASCII 0-25), then they do
    * not shift
    * Input: A integer symbol and its integer shift value 
    * Output: the integer symbol after the shift
    */
    public static int shift(int symbol, int offset) {
        if (symbol >= 0 && symbol <= 25) {
            if (symbol + offset <= 25) {
                symbol += offset;
            }
            else {
                symbol = (symbol + offset) % 25 - 1; 
            }
            return symbol;
        }
        else {
            return symbol; 
        }
    }
    
    /*
    * Description: Shifts a letter (ASCII form) backward (unshifts) based on the 
    * offset value and returns the new letter in its ASCII form. If the symbol is 
    * not a letter (EX: punctuations --> symbols not represented by ASCII 0-25),
    * then they do not unshift.
    * Input: A integer symbol and its integer unshift (backward shift) value 
    * Output: the integer symbol after the unshift (backward shift)
    */
    public static int unshift(int symbol, int offset) {
        if (symbol >= 0 && symbol <= 25) {
            if (symbol - offset >= 0) {
                symbol -= offset;
            }
            else {
                symbol = 25 + (symbol - offset + 1);       
            }
            return symbol;
        }
        else {
            return symbol; 
        }    
    }
    

    /*
    * Description: Encrypts a message by first converting the message into an int 
    * symbol array elligible for the shifting function, then shift every element of 
    * the array based on the input key, and finally converts the shifted array 
    * back to a string for return.
    * Input: A message to be encrypted and a key (determines the shift offset)
    * Output: The encrypted message
    */
    public static String encrypt(String message, int key) {
        int[] messageArray = stringToSymbolArray(message);
        for (int i = 0; i < messageArray.length; i++) {
           messageArray[i] = shift(messageArray[i], key);
        }
        String encryptedMessage = symbolArrayToString(messageArray);
        return encryptedMessage;
    }
    
    /*
    * Description: Decrypts a message by first converting the message into an int 
    * symbol array elligible for the unshifting function, then unshift every element 
    * of the array based on the input key, and finally converts the unshifted array 
    * back to a string for return.
    * Input: A message to be decrypted and a key (determines the unshift offset)
    * Output: The decrypted message
    */
    public static String decrypt(String cipher, int key) {
        int[] cipherArray = stringToSymbolArray(cipher);
        for (int i = 0; i < cipherArray.length; i++) {
           cipherArray[i] = unshift(cipherArray[i], key);
        }
        String decryptedMessage = symbolArrayToString(cipherArray);
        return decryptedMessage;
    }
    
   /*
    * Description: Creates an array for all 26 letters, which is used to store the
    * dicitionary frequency of every letter (each an element in the array), or 
    * how often the letter is used in a language, based on a file provided by the
    * user through command line arguments. 
    * Input: A String dictionary file that contains the frequencies of every letter
    * Output: A double array that stores the dictionary frequency of all 26 letters
    */
    public static double[] getDictionaryFrequencies(String fileName) {
        double[] letterFrequences = new double[26];
        In inStream = new In(fileName);
        for (int i = 0; i < 26; i++) {
            letterFrequences[i] = inStream.readDouble();
            inStream.readLine();
        }
        return letterFrequences;
    }
    
    /*
    * Description: Finds the frequency of all 26 letters (ASCII form) within a 
    * provided int symbol array (contains the ASCII forms of letters making up a 
    * cipher). The frequency is found by collecting how many times the ASCII forms
    * of letters appear in the input int symbol array, then dividing these 
    * appearances by the length of the input int symbol array (length of cipher).
    * Repeat so until all  26 letters' frequencies were found and stored in a double
    * array.
    * Input: An int array of symbols (ASCII forms of letters that make up the cipher)
    * Output: A double array that stores the frequency of all 26 letters appearing 
    * in the cipher.
    */
    public static double[] findFrequencies(int[] symbols) {
        double[] cipherFrequencies = new double[26];
        int letterSymbol = 0;
        double letterAppearance = 0;
        for (int i = 0; i < 26; i++) {
            for (int j = 0; j < symbols.length; j++) {
                if (letterSymbol == symbols[j]) {
                    letterAppearance++;
                }
            }
            cipherFrequencies[i] = letterAppearance / symbols.length;
            letterAppearance = 0;
            letterSymbol++;
        }
        return cipherFrequencies;
    }
    
     /*
    * Description: A function that scores frequencies by taking the absolute value
    * of the difference between each letter in the frequency they appeared in the 
    * cipher and their dictionary frequencies (frequencies in a language). 
    * Input: A double array that stores the dictionary frequency of all 26 letters
    * and another double array that stores the frequency of letters appearing
    * in the cipher for all 26 letters. 
    * Output: A double value that indicates the "score" of messages or how close the 
    * message is to decrypted English.
    */
    public static double scoreFrequencies(double[] english, double[] currentFreqs) {
        double score = 0.0;
        for (int i = 0; i < english.length; i++) {
            score += Math.abs(currentFreqs[i] - english[i]);
        }
        return score;
    }
    
    /*
    * Description: A function that decrypts ciphers without the need of an input
    * key. Basically, the function utilizes the scoring frequency system, checking
    * every letter on how well they are compatable to the cipher (in other words, 
    * 1. convert the cipher into a ASCII symbol array 2. Unshift the array with 
    * brute-forced offset values starting from 0 to 25, or A to Z  3. For each shift
    * (for each letter), check the scoring of the frequency by acquiring the array's
    * frequency with the findFrequencies() function and the dictionary frequency
    * through getDictionaryFrequencies(), then finally input the two double arrays 
    * into scoreFrequencies() to get the score/compatability of each letter to the 
    * cipher. Finally, get the letter that results in the lowest score and decrypt
    * the cipher using that letter as key. 
    * Input: A String cipher to be cracked and the filename of the file containing
    * the dictionary frequency of each letter.
    * Output: The cracked/decrypted message
    */
    public static String crack(String cipher, String dictionaryFile) {
        int[] cipherArray = stringToSymbolArray(cipher);
        double minScore = Double.MAX_VALUE;
        int minScoreKey = 0;
        double[] english = getDictionaryFrequencies(dictionaryFile);
        for (int i = 0; i < 26; i++) {
            cipherArray = stringToSymbolArray(cipher);
           for (int j = 0; j < cipherArray.length; j++) {
                cipherArray[j] = unshift(cipherArray[j], i);
            }
            double[] cipherFrequencies = findFrequencies(cipherArray);
            double score = scoreFrequencies(english, cipherFrequencies);
            if (score < minScore) {
                minScore = score;
                minScoreKey = i;
            }
        }
        String crackedMessage = decrypt(cipher, minScoreKey);
        return crackedMessage;
    }

/*
    * Description: The function that mainly controls the user's command line
    * arguments and decides what functions to use based on the user's input in the
    * command line. In this main function, the first command line argument would be
    * the function the user would like to use for their cipher/message (encrypt,
    * decrypt, crack). The second command line argument would read all of the user's 
    * cipher/message file and convert to one single string to be processed by the 
    * selected function. If the user choose to encrypt a message, the third
    * command line argument would take the key/letter (String type), convert it to 
    * char then int, and finally use it along with the message String to encrypt
    * the message via the encrypt function and print out the encrypted message. If
    * the user choose to decrypt a cipher, the third command line argument would 
    * take the key/letter (String type) for decryption, convert it to 
    * char then int, and finally use it along with the cipher String to decrypt
    * the message via the decrypt function and print out the decrypted message. If
    * the user choose to crack a message, then the third command line argument would
    * take in the filename of the dicitionary file containing the dictionary 
    * frequencies of all letters, and finally use the crack function to decrypt 
    * the cipher and print out the cracked cipher. 
    * Input: A string array of command line arguments (3 arguments in total)
    * function, filename containing message/cipher, and key (encrypt/decrypt) or
    * dicitionary filename.
    * Output: void
    */
    public static void main(String[] args) {
        String function = args[0];
        String fileName = args[1];
        In inStream = new In(fileName);
        String cipher = inStream.readAll();
        if (function.equals("encrypt")) {
            String keyString = args[2];
            char key = keyString.charAt(0);
            int keySymbol = (int) key - 'A';
            String encryptedMessage = encrypt(cipher, keySymbol);
            System.out.println(encryptedMessage);
        }
        else if (function.equals("decrypt")) {
            String keyString = args[2];
            char key = keyString.charAt(0);
            int keySymbol = (int) key - 'A';
            String decryptedMessage = decrypt(cipher, (int) keySymbol);
            System.out.println(decryptedMessage);
        }
        else if (function.equals("crack")) {
            String dictionaryFile = args[2];
            String crackedMessage = crack(cipher, dictionaryFile);
            System.out.println(crackedMessage);
        }
    }   
}
