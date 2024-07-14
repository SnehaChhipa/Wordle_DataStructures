package project20280.exercises;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Wk1 {

    public static void q1() {
        int[] my_array = {25, 14, 56, 15, 36, 56, 77, 18, 29, 49};
        double sum = 0;
        double average;
        for (int j : my_array) {
            sum += j;
        }
        average = sum/my_array.length;

        System.out.println(my_array);
        System.out.println(average);
    }

    public static int getIndexOf(int[] arr, int x) {
        int index = -1;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == x) {
                index = i;
            }
        }
        return index;
    }

    public static String[] getCommonElements(String[] arr1, String[] arr2) {
        String[] answer;
        int index = 0;

        if (arr1.length > arr2.length) {
             answer = new String[arr1.length];
        }
        else {
             answer = new String[arr2.length];
        }

        for (String s1 : arr1) {
            for (String s2: arr2) {
                if (s1.equals(s2)) {
                    answer[index] = s1;
                    index++;
                    break;
                }
            }
        }

        String[] finalAnswer = new String[index];
        System.arraycopy(answer, 0, finalAnswer, 0, index);
        return finalAnswer;
    }

    public static int[][] addMatrices(int[][] mat1, int[][] mat2){
        int size = mat1.length;
        int[][] sum = new int[size][mat1[0].length];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                sum[i][j] = mat1[i][j] + mat2[i][j];
            }
        }

        return sum;
    }

    public static double[][] matProd(double[][] mat1, double[][]mat2) {
        int x = mat1.length;
        int y = mat2[0].length;

        if (mat1[0].length != mat2.length) {
            throw new IllegalArgumentException("Matrix Multiplication is invalid");
        }

        double[][] prod = new double[x][y];

        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                prod[i][j] = 0;
                for (int m = 0; m < mat1[0].length; m++) {
                    for (int n = 0; n < mat2.length; n++) {
                        prod[i][j] += mat1[i][m]*mat2[n][j];
                    }
                }
            }
        }

        return prod;
    }

    public static int q6Even(Integer[] arr) {
        return Arrays.stream(arr).mapToInt(Integer::intValue).filter((i) -> i%2 == 0).sum();
    }

    public static int q6Odd(Integer[] arr) {
        return Arrays.stream(arr).mapToInt(Integer::intValue).filter((i) -> i%2 != 0).sum();
    }

    public static int[] q7(int[] arr) {
        List<Integer> temp = new ArrayList<>();

        for (int i = 1; i < arr.length; i++) {
            if (!temp.contains(arr[i])) {
                temp.add(arr[i]);
            }
        }

        int[] arr_distinct = new int[temp.size()];
        int i = 0;
        for (int x : temp) {
            arr_distinct[i] = x;
            i++;
        }
        return arr_distinct;
    }
    public static int[][] getTriples(int[]arr, int target) {
        System.out.println("starting function");
        List<int[]> tempTriplets = new ArrayList<>();
        int[][] triplets;
        int[] uniqueArr = q7(arr);
        for (int k : uniqueArr) {
            System.out.print(k + " ");
        }
        System.out.println(" ");
        int i, j;
        int temp;
        for (i = 1; i < uniqueArr.length-1; i++) {
            for (j = i; j > 0 && j > uniqueArr[j-1]; j--) {
                temp = uniqueArr[j];
                uniqueArr[j] = uniqueArr[j-1];
                uniqueArr[j-1] = temp;
            }
        }

        int x = 0, y = 1, z = 2;
        int[] triple = new int[3];

//        while (z < uniqueArr.length) {
//            System.out.println(uniqueArr[x] + uniqueArr[y] + uniqueArr[z]);
//            if (uniqueArr[x] + uniqueArr[y] + uniqueArr[z] == target) {
//                System.out.println("match target");
//               triple[0] = uniqueArr[x];
//               triple[1] = uniqueArr[y];
//               triple[2] = uniqueArr[z];
//               tempTriplets.add(triple);
//            }
//            z++; y++; x++;
//        }

        while (x < uniqueArr.length - 2) {
            y = 1;
            while (y < uniqueArr.length - 1) {
                z = 2;
                while (z < uniqueArr.length) {
                    System.out.println(uniqueArr[x] + ", " + uniqueArr[y] + ", " + uniqueArr[z]);
                    if (uniqueArr[x] + uniqueArr[y] + uniqueArr[z] == target) {
                        System.out.println("match target");
                        triple[0] = uniqueArr[x];
                        triple[1] = uniqueArr[y];
                        triple[2] = uniqueArr[z];
                        tempTriplets.add(triple);
                    }
                    z++;
                }
                y++;
            }
            x++;
        }

        x = 0; y = 1; z = 2;
        triplets = new int[tempTriplets.size()][3];

        for (int n = 0; n < tempTriplets.size(); n++) {
            triplets[n][x] = tempTriplets.get(n)[x];
            triplets[n][y] = tempTriplets.get(n)[y];
            triplets[n][z] = tempTriplets.get(n)[z];
        }

        return triplets;
    }

    public static void main(String [] args) {
        q1();

        //q2:
        int [] arr = {90 , 77, 67, 55, 75, 57, 98, 17, 50, 23, 30, 100 , 34,
                75, 28, 43, 14, 52, 64, 13};
        int x = 90;
        int indexOf = getIndexOf(arr , x); // your function
        System .out. println (" index of " + x + " : " + indexOf);

        //q3:
        String [] arr1 = {"abc", "bcd", "def"};

        String [] arr2 = {"bcd", "ghi", "abc", "xyz"};

        String [] common = getCommonElements (arr1 , arr2); // your code

        System .out. println ( Arrays . asList ( common ));

        int m = 5, n = 5;
        int[][] mat1 = new int[m][n];
        int[][] mat2 = new int[m][n];

        Random rnd = new Random();

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                mat1[i][j] = rnd.nextInt();
                mat2[i][j] = rnd.nextInt();
            }
        }

        int[][] sum = addMatrices(mat1, mat2);
        System.out.println(Arrays.deepToString(sum));


        int[] array = new int[]{1, 2, 3, 4, 1, 1, 7, -2, 1, 10};
        int[][] triplets = getTriples(array, 6);
        System.out.println(Arrays.deepToString(triplets));



    }


}
