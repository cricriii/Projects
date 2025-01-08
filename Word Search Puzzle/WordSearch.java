/**
 * @author Christina Duong
 * @date 19-10-2023
 */

//importing java internal libraries
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WordSearch {
    /**
     * Contains the full recursive method for the Word Search
     */
    private char[][] board; //2D Array that represents the grid
    private int m, n; //dimensions of the grid
    private List<String> words; //words to search
    private List<String> results; //all paths found for all the words that need to be searched

    //Constructor a 2D Array and a list
    public WordSearch(char[][] board, List<String> words) {
        this.board = board; //grid
        this.m = board.length; //number of rows in the grid
        this.n = board[0].length; //number of columns in the grid
        this.words = words; //words to be searched
        this.results = new ArrayList<>(); //new Arraylist that will contain the words and their respective paths
    }

    /**
     * Loop to search for every letter in every word by using the recursiveSearch() function
     */
    public void find() {
        for (String word : words) { //for every word in the list of words to search
            for (int i = 0; i < m; i++) { //every position in the rows
                for (int j = 0; j < n; j++) { //every position in the columns
                    if (recursiveSearch(i, j, word, 0, new ArrayList<>())) {
                    }
                }
            }
        }
        Collections.sort(results); //class the results in a lexicographic order
    }

    /**
     * Contains the recursive logic to search the words in the grid
     * @param i position in the row
     * @param j position in the column
     * @param word word that we are currently searching
     * @param idx index of the letter in the word
     * @param path word path, letter by letter, (x1,y1)->(x2,y2)->...->(xi,yi)->...(xn,yn); 0<=i<=n-1
     * @return false if not found; results if path completed
     */
    private boolean recursiveSearch(int i, int j, String word, int idx, List<String> path) {
        // returns true if the whole word was found successfully
        if (idx == word.length()) { //if the whole word was searched and we found a completed path
            results.add(word + " " + String.join("->", path)); //add to results the word and its path
            return true;
        }

        // returns false if we are outside of the grid or it is not the letter we were searching for
        if (i < 0 || i >= m || j < 0 || j >= n || board[i][j] != word.charAt(idx)) {
            return false;
        }

        //add the path of the letter with the right path format (i,j)
        path.add("(" + i + "," + j + ")");

        //all the possibilities for directions (8 cardinal directions and the position of the letter itself)
        //in order, these are the directions: W,N,E,S,center, NW, NE, SE,SW
        int[] x = {-1, 0, 1, 0, 0, -1, 1, 1, -1};
        int[] y = {0, 1, 0, -1, 0, 1, 1, -1, -1};


        //searching in the 9 directions
        for (int k = 0; k < 9; k++) {
            int i2 = i + x[k]; //increment or decrement the current position by x[k]
            int j2 = j + y[k]; //increment or decrement the current position by y[k]
            //returns false if we do not find the letter we were searching for with the function recursiveSearch
            if (recursiveSearch(i2, j2, word, idx + 1, path)) {
                path.remove(path.size() - 1); //remove the added path (since it is not correct) and return false
                return false;
            }
        }

        path.remove(path.size() - 1); //remove the added path (since it is not correct) and return false
        return false;
    }

    /**
     * returns the results (word and its path)
     */
    public List<String> getResults() {
        return results;
    }

}
