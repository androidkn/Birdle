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
  private JTextField reveal;

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

    frame = new JFrame("Birdle " + mode);
		//frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
    frame.setSize(600,600);
    frame.setBackground(Color.white);
    
    alph = this.get2();
    alph.setMaximumSize(alph.getPreferredSize());
    disp = display.display();
    disp.setMaximumSize(disp.getPreferredSize());

    reveal = new JTextField("The word was: " + game.getWord().toUpperCase());
    reveal.setEditable(false);
    reveal.setVisible(false);
    reveal.setFont(display.getFont());
    reveal.setMaximumSize(new Dimension(500,40));
    reveal.setHorizontalAlignment(JTextField.CENTER);
    reveal.setBorder(null);
    
    all = new JPanel(); 
    //Border padding = BorderFactory.createEmptyBorder(10, 10, 10, 10);
    all.setBorder(BorderFactory.createEmptyBorder(25, 10, 10, 10));
    
    all.setLayout(new BoxLayout(all, BoxLayout.PAGE_AXIS));
    all.add(disp);
    all.add(reveal);
    all.add(Box.createRigidArea(new Dimension(10,10)));
    all.add(alph);
    frame.setContentPane(all);
    frame.setVisible(true);
    alph.requestFocus();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
    
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
        if (e.getActionCommand().equals("\n")) {
          // IF THE KEY PRESSED IS "ENTER:
          enterButton();
        } else if (e.getActionCommand().equals("\b")) {
          // IF THE KEY PRESSED IS "DELETE" OR "BACKSPACE":
          deleteButton();
        } else {
          // IF KEY PRESSED IS A LETTER:
          String character = e.getActionCommand().toLowerCase();
          letterButton(character);
        }
      }
    });
    editDisplay();
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
        enterButton();
      }
    });

    // DELETE BUTTON
    JButton delete = new JButton("<<");
    delete.setPreferredSize(new Dimension(50, 50));
    delete.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
        deleteButton();
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
        btn.setBackground(display.getWhite());
        btn.setForeground(display.getBlack());
        btn.addActionListener(new ActionListener(){
    			public void actionPerformed(ActionEvent e){
            letterButton(letter);
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

  private void setDisp() {
    for (JButton c : buttons) {
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
    }
  }

  private void enterButton() {
    System.out.println("\nguess is " + guess);
    if (game.validGuess(guess) && !correct) {
      parsedGuess = game.parseGuess(guess);
      System.out.println(parsedGuess);
      if (BirdleGame.isCorrect(parsedGuess)) {
        correct = true;
      }
      for (int i = 0; i < parses[0].length; i ++) {
        parses[onRow][i] = parsedGuess.charAt(i);
      }
      setDisp();
      editDisplay();
      onRow ++;
      guess = "";
      if (onRow == guesses.length && !correct) {
        fail();
      }
    } else {
      System.out.println("Please enter valid guess");
    }
  }

  private void deleteButton() {
    if (guess.length() > 0 && !correct) {
      guess = guess.substring(0,guess.length()-1);
    }
    editDisplay();
  }

  private void letterButton(String letter) {
    if (guess.length() < BirdleGame.getLetters() && !correct) {
      guess += letter;
      editDisplay();
    }
  }

  public void editDisplay() {
    for (int c = 0; c < guesses[0].length; c ++) {
      if (onRow < guesses.length) {
        if (c < guess.length()) {
          guesses[onRow][c] = guess.charAt(c);
        } else {
          guesses[onRow][c] = 0;
        }
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
    System.out.println("disp " + correct);
  }

  public void fail() {
    System.out.println("failed");
    reveal.setVisible(true);
  }
}