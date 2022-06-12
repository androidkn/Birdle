import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;
import java.io.PrintWriter;

public class BirdleGame {

  // SIZE

  //ATTRIBUTES
  private String word;
  private ArrayList<String> birds;
  private ArrayList<String> guessList;
  private Scanner input;
  private boolean correct;
  private static int GUESSES;
  private static int LETTERS;
  private char[] letterData;

  //CONSTRUCTORS
  public BirdleGame(String mode) {
    correct = false;
    input = new Scanner(System.in);
    birds = new ArrayList<String>();
    guessList = new ArrayList<String>();
    String pathname = "FiveLetterBirds.txt";
    String guessPathname = "FiveLetterGuesses.txt";
    if (mode.equals("hard")) {
      pathname = "AllBirdList.txt";
      guessPathname = "AllGuessList.txt";
    } 
    System.out.println(pathname);
    readBirds(pathname, birds);
    readBirds(guessPathname, guessList);
    word = chooseBird();
    //word = "xxxxxxx";
    System.out.println(word);
    System.out.println(word.length());
    GUESSES = word.length() + 1;
    LETTERS = word.length();
    letterData = new char[26];
    for (int i = 0; i < letterData.length; i ++) {
      letterData[i] = '-';
    }
  }

  public void readBirds(String pathname, ArrayList<String> destination)
  {
    File file = new File(pathname);
    String outputpathname = "lengthlist.txt";
    File fileOut= new File(outputpathname);
    PrintWriter output = null;
    try {
      input = new Scanner(file);
      output = new PrintWriter(fileOut);
    } catch (FileNotFoundException ex) {
      System.out.println("*** Cannot open " + pathname + " or " + outputpathname + " ***");
      System.exit(1);
    } 
    destination.add("0");
    destination.add("1");
    while(input.hasNextLine()) {
      String line = input.nextLine();
      boolean added = false;
      for (int i = 1; i < destination.size(); i ++) {
        if (line.length() < destination.get(i).length()) {
          destination.add(i - 1, line);
          added = true;
          break;
        }
      }
      if (!added) {
        destination.add(line);
      }
    }
    destination.remove("0");
    destination.remove("1");
    for (String s : destination) {
      //output.println(s);
    }
    output.close();
  }

  private String chooseBird() {
    int i = (int)(birds.size() * Math.random());
    return birds.get(i);
  }

  public String getWord(){
    return this.word;
  }

  public void guessBird() {
    String parsed = "XXXXX";
    while (!parsed.equals("GGGGG")) {
      String guess = "";
      input = new Scanner(System.in);
      do {
        System.out.println("\nenter guess: ");
        guess = input.next();
      } while (guess.length() != word.length() || !validGuess(guess));
      parsed = parseGuess(guess);
      System.out.println(parsed);
    }
    System.out.println("You guessed the bird!");
  }

  public String parseGuess(String guess) {
    //System.out.println("guess is " + guess);
    char[] result = new char[word.length()];
    char[] tempWord = new char[word.length()];
    for (int i = 0; i < word.length(); i ++) {
      tempWord[i] = Character.toLowerCase(word.charAt(i));
    }
    for (int i = 0; i < word.length(); i ++) {
      result[i] = 'O';
    }
    //for (char i : tempWord) { System.out.print(i + " "); }
    for (int i = 0; i < tempWord.length; i ++) {
      if (tempWord[i] == Character.toLowerCase(guess.charAt(i))) {
        result[i] = 'G';
        tempWord[i] = '.';
      }
    }
    //for (char i : result) { System.out.print(i + " "); }
    //for (char i : tempWord) { System.out.print(i + " "); }
    for (int i = 0; i < tempWord.length; i ++) {
      if (result[i] != 'G') {
        for (int v = 0; v < tempWord.length; v ++) {
          if (tempWord[v] == guess.charAt(i)) {
            result[i] = 'Y';
            tempWord[v] = '.';
          }
        }
      }
    }
    //System.out.println();
    //for (char i : result) { System.out.print(i + " "); }
    //for (char i : tempWord) { System.out.print(i + " "); }
    String resultString = "";
    correct = true;
    for (int i = 0; i < result.length; i ++) {
      resultString += result[i];
      if (result[i] != 'G') {
        correct = false;
      }
      //System.out.println((int)word.charAt(i) - 96);
      char current = letterData[(int)guess.charAt(i) - 97];
      if (result[i] == 'G') {
        letterData[(int)guess.charAt(i) - 97] = result[i];
      } else if (result[i] == 'Y') {
        if (current != 'G') {
          letterData[(int)guess.charAt(i) - 97] = result[i];
        }
      } else if (result[i] == 'O') {
        if (current != 'G' && current != 'Y') {
          letterData[(int)guess.charAt(i) - 97] = result[i];
        }
      }
      //letterData[(int)guess.charAt(i) - 97] = result[i];
    }
    //System.out.println("parsedguess returns "  + resultString);
    //for (int i = 0; i < letterData.length; i ++) {  System.out.println(i + " " + letterData[i]); }
    return resultString;
  }

  public boolean validGuess(String guess) {
    boolean valid = true;
    if (!guessList.contains(guess.toLowerCase())) {
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

  public char[] getLetterData () {
    return letterData;
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