/**
 * @author Christina Duong 20238163
 * @date 19-10-2023
 * IFT2015 Structures de données
 *
 * WordSearch is a program to solve a word search puzzle.
 * With a list of distinct, lowercase words and a two-dimensional board
 * composed of M x N cells, it will identify the words from a provided list.
 */

//importing java internal libraries
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Date;


public class Main {
    /**
     * Contains the general method for the Word Search Puzzle
     * @param args input in .txt format with the required format
     */
    public static void main(String[] args) {
        //uncomment the following lines if you are using the same methode to calculate the time of execution
        //feel free to use your own method
        //instance attributes
        //long start, end; //start and end in ms
        //start = new Date().getTime(); //create a starting time

        try { //catch exceptions
            BufferedReader reader = new BufferedReader(new FileReader(args[0]));
            String line; //instance of line for the reader to read
            int query = 1; //counter for the queries

            while ((line = reader.readLine()) != null) { //reads non-empty lines
                String[] dimensions = line.split(" "); //splits the first line of input for the dimensions
                int M = Integer.parseInt(dimensions[0]); //number of rows in the grid
                //the number of columns will not be necessary in the Main class

                char[][] board = new char[M][]; //creates a 2D Array for the grid
                for (int i = 0; i < M; i++) { //for every position in the 2D Array
                    // fill the position with the letter read by the reader
                    board[i] = reader.readLine().replace(" ", "").toCharArray();
                }

                //reads the words to search
                String[] words = reader.readLine().split(" "); //delimiters for the spaces in between the words
                List<String> wordList = new ArrayList<>(); //ArrayList that contains all the words to search
                Collections.addAll(wordList, words); //put those words in lexicographic order

                //call the function WordSearch to do a recursive search in the grid for the words
                WordSearch finder = new WordSearch(board, wordList);
                finder.find();

                System.out.println("Query " + query + ":"); //print the query and its number
                //for every answer found, in a lexicographic order
                for (String result : finder.getResults()) {
                    System.out.println(result); //print the word and its path
                }

                query++; //increment query to indicate a new query (if it exists)
            }

            reader.close(); //close the reader

        } catch (IOException e) { //catch exceptions
            e.printStackTrace();
        }

        //uncomment the following lines if you are using the same method to calculate the time of execution
        //feel free to use your own method
        //end = new Date().getTime(); //create an ending time
        //long executionTime = end - start; //calculate the execution time (difference between the start and the end)
        //System.out.println("Execution time: "+ executionTime+ "ms."); //print the execution time (in ms)
    }

}
