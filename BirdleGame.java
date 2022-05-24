import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;
import java.io.PrintWriter;

public class BirdleGame {

  // SIZE

  //ATTRIBUTES
  private String word;
  private final String CHEAT = "divya"; // CHEAT CODE
  private String pathname;
  private ArrayList<String> birds;
  private Scanner input;
  private boolean correct;
  private static final int GUESSES = 6;
  private static final int LETTERS = 5;

  //CONSTRUCTORS
  public BirdleGame() {
    correct = false;
    input = new Scanner(System.in);
    birds = new ArrayList<String>();
    pathname = "FiveLetterBirds.txt";
    readBirds(pathname);
    word = chooseBird();
    //System.out.println(word);
    System.out.println(word.length());
  }

  public void readBirds(String pathname)
  {
    File file = new File(pathname);
    try {
      input = new Scanner(file);
    } catch (FileNotFoundException ex) {
      System.out.println("*** Cannot open " + pathname + " ***");
      System.exit(1);  // quit the program
    } 
    birds.add("0");
    birds.add("1");
    while(input.hasNextLine()) {
      String line = input.nextLine();
      boolean added = false;
      for (int i = 1; i < birds.size(); i ++) {
        if (line.length() < birds.get(i).length()) {
          birds.add(i - 1, line);
          added = true;
          break;
        }
      }
      if (!added) {
        birds.add(line);
      }
      //birds.add(line);
      //System.out.println(line);	
    }
    birds.remove("0");
    birds.remove("1");
  }

  private String chooseBird() {
    int i = (int)(birds.size() * Math.random());
    return birds.get(i);
  }

  public String getWord(){
    return this.word;
  }

  private void guessBird() {
    String guess = "";
    input = new Scanner(System.in);
    do {
      System.out.println("\nenter guess:");
      guess = input.next();
      //cheat code
      if (guess.equals(CHEAT)) {
        System.out.println("Cheat code activated! Word: " + word);
      }
    } while (guess.length() != word.length() || !validGuess(guess));
  }

  public String parseGuess(String guess) {
    char[] result = new char[word.length()];
    for (int i = 0; i < word.length(); i ++) {
      if (word.substring(i,i+1).equalsIgnoreCase(guess.substring(i,i+1))) {
        result[i] = 'G';
      } else if (word.contains(guess.substring(i, i + 1))) {
        result[i] = 'Y';
      } else {
        result[i] = 'O';
      }
    }
    String result2 = "";
    correct = true;
    for (char i : result) {
      result2 += i;
      if (i != 'G') {
        correct = false;
      }
    }
    return result2;
  }

  public boolean validGuess(String guess) {
    boolean valid = true;
    if (guess.equals(CHEAT)) {
      System.out.println("Cheat code activated! Word: " + word);
    }
    if (!birds.contains(guess)) {
      System.out.println("not in bird list");
      valid = false;
    }
    if (guess.length() != word.length()) {
      System.out.println("length does not match");
      valid = false;
    }
    return valid;
  }


  // TESTER METHODS

  private void testBirds() {
    print();
    int[] lengths = new int[19];
    for (String b : birds) {
      lengths[b.length()]  ++;
    }
    printArr(lengths);
  }

  private void print() {
    for (int i = 0; i < birds.size(); i ++) {
      System.out.println(i + 1 + "  " + birds.get(i));
    }
  }

  public void printArr(int[] lengths) {
    System.out.print("[ ");
    for (int i : lengths) {
      System.out.print(i + " ");
    }
    System.out.print("]");
  }

  public static int getGuesses() {
    return GUESSES;
  }
  public static int getLetters() {
    return LETTERS;
  }

  public static boolean isCorrect(String parsedGuess) {
    for (int i = 0; i < parsedGuess.length(); i ++) {
      if (parsedGuess.charAt(i) != 'G') {
        return false;
      }
    }
    return true;
  }
}