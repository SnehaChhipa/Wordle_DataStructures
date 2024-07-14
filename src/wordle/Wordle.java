package wordle;

import project20280.hashtable.ChainHashMap;
import project20280.interfaces.Entry;
import project20280.interfaces.Position;
import project20280.priorityqueue.HeapPriorityQueue;
import project20280.tree.LinkedBinaryTree;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Wordle {

    String fileName = "wordle/resources/dictionary.txt";
    //String fileName = "wordle/resources/extended-dictionary.txt";

    List<String> dictionary = null;
    final int num_guesses = 5;
    final long seed = 42;
    //Random rand = new Random(seed);
    Random rand = new Random();

    static final String winMessage = "CONGRATULATIONS! YOU WON! :)";
    static final String lostMessage = "YOU LOST :( THE WORD CHOSEN BY THE GAME IS: ";
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
    public static final String ANSI_GREY_BACKGROUND = "\u001B[100m";

    Wordle() {
        this.dictionary = readDictionary(fileName);
        compression_analysis(dictionary, huffman(parseDictionary(dictionary)));
    }

    public static void main(String[] args) {
        Wordle game = new Wordle();

//        String target = game.getRandomTargetWord();
//        System.out.println("target: " + target);
//        game.play(target);

        game.solver(game.dictionary);

    }

    public void play(String target) {
        // TODO
        // TODO: You have to fill in the code
        for (int i = 0; i < num_guesses; ++i) {
            String guess = getGuess();

            if (Objects.equals(guess, target)) { // you won!
                win(target);
                return;
            }

            // the hint is a string where green="+", yellow="o", grey="_"
            String[] hint = computeHints(target, guess);

            // after setting the yellow and green positions, the remaining hint positions must be "not present" or "_"
            System.out.println("hint: " + Arrays.toString(hint));


            // check for a win
            int num_green = 0;
            for (int k = 0; k < 5; ++k) {
                if (hint[k] == "+") num_green += 1;
            }
            if (num_green == 5) {
                win(target);
                return;
            }
        }

        lost(target);
    }

    public void lost(String target) {
        System.out.println();
        System.out.println(lostMessage + target.toUpperCase() + ".");
        System.out.println();

    }

    public void win(String target) {
        System.out.println(ANSI_GREEN_BACKGROUND + target.toUpperCase() + ANSI_RESET);
        System.out.println();
        System.out.println(winMessage);
        System.out.println();
    }

    public String getGuess() {
        Scanner myScanner = new Scanner(System.in, StandardCharsets.UTF_8.displayName());  // Create a Scanner object
        System.out.println("Guess:");

        String userWord = myScanner.nextLine();  // Read user input
        userWord = userWord.toLowerCase(); // covert to lowercase

        // check the length of the word and if it exists
        while ((userWord.length() != 5) || !(dictionary.contains(userWord))) {
            if ((userWord.length() != 5)) {
                System.out.println("The word " + userWord + " does not have 5 letters.");
            } else {
                System.out.println("The word " + userWord + " is not in the word list.");
            }
            // Ask for a new word
            System.out.println("Please enter a new 5-letter word.");
            userWord = myScanner.nextLine();
        }
        return userWord;
    }

    public String getRandomTargetWord() {
        // generate random values from 0 to dictionary size
        return dictionary.get(rand.nextInt(dictionary.size()));
    }

    public List<String> readDictionary(String fileName) {
        List<String> wordList = new ArrayList<>();

        try {
            // Open and read the dictionary file
            InputStream in = this.getClass().getClassLoader().getResourceAsStream(fileName);
            assert in != null;
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String strLine;

            //Read file line By line
            while ((strLine = reader.readLine()) != null) {
                wordList.add(strLine.toLowerCase());
            }
            //Close the input stream
            in.close();

        } catch (Exception e) {//Catch exception if any
            System.err.println("Error: " + e.getMessage());
        }
        return wordList;
    }


    //The below functions creates the huffman tree amd returns the position of the root of the tree created.
    public Position<String> huffman(String dictionary) {
        ChainHashMap<String, Integer> charFrequencies = frequencyMap(dictionary);
        HeapPriorityQueue<Integer, Position<String>> pq = new HeapPriorityQueue<>();
        LinkedBinaryTree<String> bt = new LinkedBinaryTree<>();

        for (Entry<String, Integer> map : charFrequencies.entrySet()) {
            Position<String> pos = bt.add(map.getKey(), null, null, null);
            pq.insert(map.getValue(), pos);
        }

        while (pq.size() > 1) {
            Entry<Integer, Position<String>> e1 = pq.removeMin();
            Entry<Integer, Position<String>> e2 = pq.removeMin();

            Position<String> leftT = e1.getValue();
            Position<String> rightT = e2.getValue();

            String rootVal = leftT.getElement().concat(rightT.getElement());
            Position<String> T_root = bt.add(rootVal, null, leftT, rightT);

            pq.insert(e1.getKey() + e2.getKey(), T_root);
        }

        Entry<Integer, Position<String>> e = pq.removeMin();
        bt.setRoot(e.getValue());
        System.out.println("******************* Huffman Encoding Tree *******************\n");
        System.out.println(bt.toBinaryTreeString());
        return e.getValue();
    }

    //The function below concatenates all words in the dictionary into one single string.
    public String parseDictionary(List<String> dictionary) {
        String string = "";
        for (String s : dictionary) {
            string = string.concat(s);
        }
        return string;
    }

    //The below function counts the frequency of letters in the dictionary string
    public ChainHashMap<String, Integer> frequencyMap(String string) {
        ChainHashMap<String, Integer> answer = new ChainHashMap<>();
        answer.put(String.valueOf(string.charAt(0)), 1);
        for (int i = 1; i < string.length(); i++) {
            int count = 0;
            if (answer.get(String.valueOf(string.charAt(i))) != null) {
                count = answer.get(String.valueOf(string.charAt(i)));
            }
            answer.put(String.valueOf(string.charAt(i)), count + 1);
        }

        return answer;
    }

    public void compression_analysis(List<String> dictionary, Position<String> huffman) {
        List<String> asciiEncoding = asciiEncode(dictionary);
        List<String> huffmanEncoding = huffmanEncode(dictionary, huffman);

        String asciiString = "";
        String huffmanString = "";
        String longestCode = huffmanEncoding.get(0);
        String shortestCode = huffmanEncoding.get(0);
        List<String> longestWords = new ArrayList<>();
        List<String> shortestWords = new ArrayList<>();

        for (String word : asciiEncoding) {
            asciiString = asciiString.concat(word);
        }
        for (String word : huffmanEncoding) {
            huffmanString = huffmanString.concat(word);
            if (word.length() <= shortestCode.length())
                shortestCode = word;
            if (word.length() >= longestCode.length())
                longestCode = word;
        }

        for (String word : huffmanEncoding) {
            if (word.length() == shortestCode.length()) {
                shortestWords.add(dictionary.get(huffmanEncoding.indexOf(word)));
            }
            if (word.length() == longestCode.length()) {
                longestWords.add(dictionary.get(huffmanEncoding.indexOf(word)));
            }
        }

        System.out.println("COMPRESSION ANALYSIS: ");
        System.out.println("8 bits are required to encode the word list using ascii coding");
        System.out.println("A maximum of 8 bits are required to encode the word list using ascii coding");
        System.out.println("Length of complete word list using ascii code: " + asciiString.length());
        System.out.println("Length of complete word list using huffman code: " + huffmanString.length());
        double compressionRatio = (double) huffmanString.length() /asciiString.length();
        compressionRatio = (double) Math.round(compressionRatio * 100);
        System.out.println("Compression ratio is: " + compressionRatio + "%");
        System.out.println("Word with longest huffman code has length " + longestCode.length() + " bits is -> " + longestWords.get(0));
        System.out.println("Word with shortest huffman code has length " + shortestCode.length() + " bits is -> " + shortestWords.get(0) + "\n\n");
    }

    public List<String> huffmanEncode(List<String> file, Position<String> root) {
        LinkedBinaryTree<String> huffman = new LinkedBinaryTree<>();
        huffman.setRoot(root);
        List<String> answer = new ArrayList<>();
        String code;
        boolean charFound;
        Position<String> curr;

        for (String word : file) {
            code = "";
            for (int c = 0; c < word.length(); c++) {
                charFound = false;
                curr = huffman.root();
                while (!charFound) {

                    if (curr.getElement().equals(String.valueOf(word.charAt(c)))) {
                        charFound = true;
                    }

                    if (huffman.left(curr) != null) {
                        if (huffman.left(curr).getElement().contains(String.valueOf(word.charAt(c)))) {
                            code = code.concat("0");
                            curr = huffman.left(curr);
                        }
                    }

                    if (huffman.right(curr) != null) {
                        if (huffman.right(curr).getElement().contains(String.valueOf(word.charAt(c)))) {
                            code = code.concat("1");
                            curr = huffman.right(curr);
                        }
                    }
                }
            }
            answer.add(code);
        }

        return answer;
    }


    public List<String> asciiEncode(List<String> file) {
        List<String> answer = new ArrayList<>();

        String code;
        for (String word : file) {
            code = "";
            for (int c = 0; c < word.length(); c++) {
                String letter = "";
                int x = Character.getNumericValue(word.charAt(c));
                letter = Integer.toBinaryString(x);
                while (letter.length() != 8) {
                    letter = letter.concat("0");
                }
                code = code.concat(letter);
            }
            answer.add(code);
        }
        return answer;
    }

    //The below function returns the word with maximum frequency from the list of possible words.
    public String getBestGuess(ChainHashMap<String, Integer> possible_words) {
        int maxFrequency = 0;
        String bestGuess = "";
        for (Entry<String, Integer> entry : possible_words.entrySet()) {
            if (entry.getValue() > maxFrequency) {
                maxFrequency = entry.getValue();
                bestGuess = entry.getKey();
            }
        }
        return bestGuess;
    }

    public void solver(List<String> dictionary) {
        ChainHashMap<String, Integer> character_frequencies = frequencyMap(parseDictionary(dictionary));
        ChainHashMap<String, Integer> possible_words = new ChainHashMap<>();

        //creating a hashmap where the key is the word and the value is the sum of frequencies of its letters
        for (String word : dictionary) {
            int sum = 0;
            for (int c = 0; c < word.length(); c++) {
                sum += character_frequencies.get(String.valueOf(word.charAt(c)));
            }
            possible_words.put(word, sum);
        }

        System.out.println("************************** Solver ***************************");
        String target = dictionary.get(rand.nextInt(dictionary.size()));
        System.out.println("TARGET WORD-> " + target);
        System.out.println("size of initial dictionary ----> " + possible_words.size() +
                ", with collisions: " + possible_words.numOfCollisions() +
                ", with a load factor of " + String.format("%.3f", possible_words.getLoadFactor()));
        String guess;
        int count = 0;
        while (true) {
            guess = getBestGuess(possible_words);
            System.out.println("Guess " + (count+1) + ". " + guess);

            if (Objects.equals(guess, target)) { // you won!
                if (count < 5)
                    win(target);
                else {
                    System.out.println("***");
                    System.out.println("\nYOU LOST :( !! The number of guesses exceeds 5 !!");
                }
                return;
            }

            String[] hint = computeHints(target, guess);
            System.out.println("hint: " + Arrays.toString(hint));

            // check for a win
            int num_green = 0;
            for (int k = 0; k < 5; ++k) {
                if (hint[k] == "+") num_green += 1;
            }
            if (num_green == 5) {
                win(target);
                return;
            }

            possible_words = filterDictionary(guess, hint, possible_words);
            System.out.println("size of filtered dictionary ----> " + possible_words.size() +
                    ", with collisions: " + possible_words.numOfCollisions() +
                    ", with a load factor of " + String.format("%.3f", possible_words.getLoadFactor()));
            count++;
        }
    }

    public String[] computeHints(String target, String guess) {
        String[] hint = {"_", "_", "_", "_", "_"};
        List<Character> unique_chars = new ArrayList<>();
        int[] frequencies = {0, 0, 0, 0, 0};
        frequencies[0] = 1;
        unique_chars.add(target.charAt(0));
        for (int n = 1; n < 5; n++) {
            if (unique_chars.contains(target.charAt(n))) {
                frequencies[n]++;
            }
            else {
                unique_chars.add(target.charAt(n));
                frequencies[n] = 1;
            }
        }


        for (int k = 0; k < 5; k++) {
            if (guess.charAt(k) == target.charAt(k)) {
                hint[k] = "+";
                frequencies[k] -= 1;
            }
        }

        for (int k = 0; k < 5; k++) {
            if (target.contains(String.valueOf(guess.charAt(k))) && target.indexOf(guess.charAt(k)) != k) {
                int index = target.indexOf(guess.charAt(k));
                if (frequencies[index] > 0) {
                    hint[k] = "o";
                    frequencies[index] -= 1;
                }
            }
        }

        return hint;
    }

    public ChainHashMap<String, Integer> filterDictionary(String guess, String[] hint, ChainHashMap<String, Integer> possible_words) {
        return filter2(guess, hint, filter1(guess, hint, filter3(guess, hint, possible_words)));
    }

    //Removes all "_":
    public ChainHashMap<String, Integer> filter1(String guess, String[] hint, ChainHashMap<String, Integer> possible_words) {
        ChainHashMap<String, Integer> filtered = new ChainHashMap<>();

        ChainHashMap<Character, Integer> hint_analysis = new ChainHashMap<>();
        for (int i = 0; i < 5; i++) {
            if (hint[i].equals("_")) {
                hint_analysis.put(guess.charAt(i), i);
            }
        }

        List<Character> must_chars = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            if (hint[i].equals("+")) {
                must_chars.add(guess.charAt(i));
            }
        }

        List<Character> chars = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            if (hint[i].equals("o")) {
                chars.add(guess.charAt(i));
            }
        }

        boolean valid;
        if (!hint_analysis.isEmpty()) {
            for (Entry<String, Integer> word : possible_words.entrySet()) {
                valid = true;
                for (Entry<Character, Integer> analysis : hint_analysis.entrySet()) {
                    if (must_chars.contains(analysis.getKey()) || chars.contains(analysis.getKey())) {
                        if (word.getKey().charAt(analysis.getValue()) == analysis.getKey()) {
                            valid = false;
                            break;
                        }
                    } else {
                        if (word.getKey().contains(String.valueOf(analysis.getKey()))) {
                            valid = false;
                            break;
                        }
                    }
                }
                if (valid && !word.getKey().equals(guess)) {
                    filtered.put(word.getKey(), word.getValue());
                }
            }
            return filtered;
        }

        return possible_words;
    }

    //Filters all "o":
    public ChainHashMap<String, Integer> filter2(String guess, String[] hint, ChainHashMap<String, Integer> possible_words) {
        ChainHashMap<String, Integer> filtered = new ChainHashMap<>();
        List<String> hint_analysis = new ArrayList<>();
        List<Integer> position = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            if (hint[i].equals("o")) {
                hint_analysis.add(String.valueOf(guess.charAt(i)));
                position.add(i);
            }
        }

            if (!hint_analysis.isEmpty()) {
                  for (Entry<String, Integer> word : possible_words.entrySet()) {
                      boolean valid;
                      //filtering hashmap:
                      valid = false;
                      for (int i = 0; i < hint_analysis.size(); i++) {
                          if (!word.getKey().contains(hint_analysis.get(i))) {
                              break;
                          } else {
                              if (word.getKey().indexOf(hint_analysis.get(i)) == position.get(i)) {
                                  break;
                              }
                          }
                          if (i == hint_analysis.size() - 1) {
                              valid = true;
                              break;
                          }
                      }
                      if (valid && !word.getKey().equals(guess)) {
                            filtered.put(word.getKey(), word.getValue());
                      }
                  }
                  return filtered;
            }

        return possible_words;
    }

    //Keeps all "+":
    public ChainHashMap<String, Integer> filter3(String guess, String[] hint, ChainHashMap<String, Integer> possible_words) {
        ChainHashMap<String, Integer> filtered = new ChainHashMap<>();

        int count = 0;
        for (int i = 0; i < 5; i++) {
            if (hint[i].equals("+")) {
                count++;
            }
        }

        if (count > 0) {
            for (Entry<String, Integer> word : possible_words.entrySet()) {
                int match = 0;
                for (int i = 0; i < 5; i++) {
                    if (hint[i].equals("+") && String.valueOf(guess.charAt(i)).equals(String.valueOf(word.getKey().charAt(i)))) {
                                match++;
                    }
                }
                if (match == count && !word.getKey().equals(guess)) {
                            filtered.put(word.getKey(), word.getValue());
                }
            }
            return filtered;
        }

        return possible_words;
    }

}