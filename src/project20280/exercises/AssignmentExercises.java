package project20280.exercises;

import project20280.hashtable.ChainHashMap;
import project20280.interfaces.Entry;
import project20280.list.SinglyLinkedList;
import project20280.tree.TreeMap;

import java.io.*;
import java.util.*;

public class AssignmentExercises {
    public static void main(String[] args) throws FileNotFoundException {
        SinglyLinkedList<Integer> listA = new SinglyLinkedList<>();
        listA.addLast(2);
        listA.addLast(6);
        listA.addLast(20);
        listA.addLast(24);

        SinglyLinkedList<Integer> listB = new SinglyLinkedList<>();
        listB.addLast(1);
        listB.addLast(3);
        listB.addLast(5);
        listB.addLast(8);

        SinglyLinkedList<Integer> listC = mergeLists(listA, listB);
        System.out.println(listC);
        AssignmentExercises exercises = new AssignmentExercises();
        List<String> answer = exercises.frequencyCounter("project20280/exercises/resources/sample_text.txt");
        System.out.println(answer);

        treemap_exercise();
    }

    //Exercise on Lists. (Q.9)
    public static SinglyLinkedList<Integer> mergeLists(SinglyLinkedList<Integer> l1, SinglyLinkedList<Integer> l2) {
        SinglyLinkedList<Integer> mergedList = new SinglyLinkedList<>();
        int size = l1.size() + l2.size();

        //Example:
        // [2, 6, 20, 24]
        // [1, 3, 5, 8]

        for (int i = 0; i < size; i++) {
            if (!l1.isEmpty() && !l2.isEmpty()) {
                if (l1.get(0) <= l2.get(0)) {
                    mergedList.addLast(l1.get(0));
                    l1.remove(0);
                }
                else {
                    mergedList.addLast(l2.get(0));
                    l2.remove(0);
                }
            }
            else {
                //Example:
                // [20, 24]
                // []
                if (!l1.isEmpty()) {
                    mergedList.addLast(l1.get(0));
                    l1.remove(0);
                }
                else {
                    mergedList.addLast(l2.get(0));
                    l2.remove(0);
                }
            }

        }
        return mergedList;
    }


    //Exercise on ChainHashmap. (Q.5)
    public List<String> frequencyCounter(String file) {
        List<String> mostFrequentWords = new ArrayList<>();
        ChainHashMap<String, Integer> counter = new ChainHashMap<>();
        List<String> words = readFile(file);

        //fills in the hashmap with words and their frequencies.
        counter.put(words.get(0), 1);
        for (int i = 1; i < words.size(); i++) {
            if (counter.get(words.get(i)) != null) {
                int count = counter.get(words.get(i));
                counter.put(words.get(i), count + 1);
            } else {
                counter.put(words.get(i), 1);
            }
        }

        int[] frequency = new int[words.size()];
        int index = 0;
        for (Entry<String, Integer> entry : counter.entrySet()) {
            frequency[index] = entry.getValue();
            index++;
        }

        Arrays.sort(frequency);

        int count = 0;
        for (Entry<String, Integer> entry : counter.entrySet()) {
            for (int i = frequency.length-10; i < frequency.length; i++) {
                if (entry.getValue() == frequency[i] && count < 10) {
                    mostFrequentWords.add(entry.getKey());
                    count++;
                    break;
                }
            }
            if (count == 10) {
                break;
            }
        }

        return mostFrequentWords;
    }

    //Function to read each words from file.
    public List<String> readFile(String fileName) {
        List<String> wordList = new ArrayList<>();
        String[] line_words;
        try {
            // Open and read the dictionary file
            InputStream in = this.getClass().getClassLoader().getResourceAsStream(fileName);
            assert in != null;
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String strLine;

            //Read file line By line
            while ((strLine = reader.readLine()) != null) {
                line_words = strLine.toLowerCase().split(" ");
                wordList.addAll(Arrays.asList(line_words));
            }
            //Close the input stream
            in.close();

        } catch (Exception e) {//Catch exception if any
            System.err.println("Error: " + e.getMessage());
        }
        return wordList;
    }

    //Exercise on TreeMap. (Q.2)
    public static void treemap_exercise() {
        TreeMap<Integer, Integer> bst = new TreeMap<>();
        Random rnd = new Random();
        int n_max = 50;
        int n = 20;
        rnd.ints(1, n_max)
                .limit(n)
                .distinct()
                .boxed()
                .forEach(x -> bst.put(x, x));

        bst.printTree();
    }


}
