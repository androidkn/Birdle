import javax.swing.*;
import java.awt.*;
import java.awt.event.*; 
import java.util.ArrayList;

// Main class that does the work

public class BirdleAlphabet{

  // Attributes
  private final String[] LETTY = {"qwertyuiop", "asdfghjkl", "zxcvbnm"};
  private final JPanel[] KEYBOARD = {new JPanel(), new JPanel(), new JPanel()};
  ArrayList<JButton> buttons;
  private String guess;
  private String parsedGuess;
  private BirdleGame game;
  private BirdleDisplay display;
  private char[][] guesses;
  private char[][] parses;
  private int onRow;

  private JPanel disp;
  private JPanel alph;
  private JPanel all;
  private JFrame frame;

  private boolean d;
  private boolean correct;

  // Constructor
	public BirdleAlphabet(String mode){
    System.out.println(mode);
		this.game = new BirdleGame(mode);
    this.display = new BirdleDisplay();
    onRow = 0;
    guess = "";
    guesses = new char[BirdleGame.getGuesses()][BirdleGame.getLetters()];
    parses = new char[BirdleGame.getGuesses()][BirdleGame.getLetters()];
    parsedGuess = "OOOOO";
    buttons = new ArrayList<JButton>();
    correct = false;
    for (int r = 0; r < guesses.length; r ++) {
      for (int c = 0; c < guesses[0].length; c ++) {
        guesses[r][c] = 0;
        parses[r][c] = 'W';
      }
    }
    display.setDisp(guesses, parses);

    frame = new JFrame("Birdle");
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
    frame.setBackground(Color.white);

    all = new JPanel();   
    alph = this.get2();
    disp = display.display();
    
    all.add(disp);
    all.add(alph);
    frame.setContentPane(all);
    frame.setVisible(true);
    alph.requestFocus();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
    editDisplay();
    
    // SET UP INPUT MAPS FOR KEYBOARD
    InputMap map = all.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    for (int i = 0; i < 26; i ++) {
      char smallChar = (char)('a' + i);
      char bigChar = (char)('A' + i); 
      map.put(KeyStroke.getKeyStroke(smallChar), "pressed");
      map.put(KeyStroke.getKeyStroke(bigChar), "pressed");
    }
    map.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "pressed");
    map.put(KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0), "pressed");
    map.put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), "pressed");
    all.getActionMap().put("pressed", new AbstractAction() {
      public void actionPerformed(ActionEvent e){
        //System.out.println("|||" + e.getActionCommand() + "|||");
        // IF THE KEY PRESSED IS "ENTER:
        if (e.getActionCommand().equals("\n")) {
          System.out.println("\nguess is " + guess);
          if (game.validGuess(guess)) {
            parsedGuess = game.parseGuess(guess);
            System.out.println(parsedGuess);
            if (BirdleGame.isCorrect(parsedGuess)) {
              correct = true;
            }
            for (int i = 0; i < parses[0].length; i ++) {
              parses[onRow][i] = parsedGuess.charAt(i);
            }
            onRow ++;
            guess = "";
            setDisp();
            editDisplay();
          } else {
            System.out.println("Please enter valid guess");
            guess = "";
          }
        } else if (e.getActionCommand().equals("\b")) {
          // IF THE KEY PRESSED IS "DELETE" OR "BACKSPACE":
          if (guess.length() > 0) {
            guess = guess.substring(0,guess.length()-1);
          }
          editDisplay();
        } else {
          // IF KEY PRESSED IS A LETTER:
          String character = e.getActionCommand().toLowerCase();
          //System.out.println("all saw " + character);
          if (guess.length() < BirdleGame.getLetters()) {
            guess += character;
            editDisplay();
            d = false;
          }
        }
      }
    });
    
    
    do {
      //System.out.println("A " + correct);
      if (!d && !correct) {
        System.out.println("B " + correct);
        editDisplay();
        System.out.println("C " + correct);
        d = true;
        System.out.println("D " + correct);
      }
    } while (!correct);
    System.out.println("QUIT\nQUIT\nQUIT\nQUIT\nQUIT");
	}	

  // Returns the keyboard
  public JPanel get2() {
    JPanel keyboard = new JPanel();
    keyboard.setLayout(new BoxLayout(keyboard, BoxLayout.Y_AXIS));
    Dimension letSpacing = new Dimension(10, 10);

    // ENTER BUTTON
    JButton enter = new JButton("->");
    enter.setPreferredSize(new Dimension(50, 50));
    enter.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
        System.out.println("\nguess is " + guess);
        if (game.validGuess(guess)) {
          parsedGuess = game.parseGuess(guess);
          System.out.println(parsedGuess);
          if (BirdleGame.isCorrect(parsedGuess)) {
            correct = true;
          }
          for (int i = 0; i < parses[0].length; i ++) {
            parses[onRow][i] = parsedGuess.charAt(i);
          }
          onRow ++;
          guess = "";
          setDisp();
          editDisplay();
        } else {
          System.out.println("Please enter valid guess");
          guess = "";
        }
      }
    });

    // DELETE BUTTON
    JButton delete = new JButton("<<");
    delete.setPreferredSize(new Dimension(50, 50));
    delete.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
        if (guess.length() > 0) {
          guess = guess.substring(0,guess.length()-1);
        }
        editDisplay();
      }
    });	

    // LETTER BUTTONS
    for (int i = 0; i < KEYBOARD.length; i ++) {
      KEYBOARD[i].setLayout(new BoxLayout(KEYBOARD[i], BoxLayout.X_AXIS));
      if (i == 2) {
        KEYBOARD[i].add(delete);
        KEYBOARD[i].add(Box.createRigidArea(letSpacing));
      }
      keyboard.add(Box.createRigidArea(new Dimension(10, 10)));
      for (int v = 0; v < LETTY[i].length(); v ++) {
        String letter = LETTY[i].substring(v, v+1);
        JButton btn = new JButton(letter);
        btn.setPreferredSize(new Dimension(50, 50));
        btn.addActionListener(new ActionListener(){
    			public void actionPerformed(ActionEvent e){
            if (guess.length() < BirdleGame.getLetters()) {
              //System.out.print(let);
              guess += letter;
              editDisplay();
              d = false;
            }
    			}
    		});	
        buttons.add(btn);
        KEYBOARD[i].add(btn);
        KEYBOARD[i].add(Box.createRigidArea(letSpacing));
        if (i == 2) {
          KEYBOARD[i].add(enter);
        }
      }
      keyboard.add(KEYBOARD[i]);
    }
    return keyboard;
  }

  public void setDisp() {
    for (JButton c : buttons) {
      //System.out.print(c.getText() + " " + ((int)c.getText().charAt(0) - 97) + "    ");
      char targetColor = game.getLetterData()[(int)c.getText().charAt(0) - 97];
      if (targetColor == 'G') {
        c.setBackground(display.getGreen());
        c.setForeground(display.getWhite());
      } else if (targetColor == 'Y') {
        c.setBackground(display.getYellow());
        c.setForeground(display.getWhite());
      } else if (targetColor == 'O') {
        c.setBackground(display.getGrey());
        c.setForeground(display.getWhite());
      } else {
        c.setBackground(display.getWhite());
        c.setForeground(display.getBlack());
      }
      System.out.println(c.getText() + " " + targetColor);
    }
  }

  public void editDisplay() {
    for (int c = 0; c < guesses[0].length; c ++) {
      if (c < guess.length()) {
        guesses[onRow][c] = guess.charAt(c);
      } else {
        guesses[onRow][c] = 0;
      }
    }
    if (BirdleGame.isCorrect(parsedGuess)) {
      correct = true;
    }
    //BirdleDisplay.printArr(guesses);
    display.setDisp(guesses, parses);
    alph.repaint();
    alph.revalidate();
    disp.repaint();
    disp.revalidate();
    //System.out.println("disp " + correct);
  }
}