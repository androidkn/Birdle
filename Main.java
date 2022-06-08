import javax.swing.*;
import java.awt.*;
import java.awt.event.*; 

class Main {
  public static void main(String[] args) {
    //BirdleGame game = new BirdleGame("easy");
    //game.guessBird();
    new BirdleAlphabet("hard");
    //buttons();
  }

  public static void buttons() {
    
    JRadioButton fiveButton = new JRadioButton("5 letter mode (EASY)");
    fiveButton.setMnemonic(KeyEvent.VK_B);
    //fiveButton.setSelected(true);

    JRadioButton allButton = new JRadioButton("any number of letters (HARD)");
    allButton.setMnemonic(KeyEvent.VK_C);

    //Group the radio buttons.
    ButtonGroup group = new ButtonGroup();
    group.add(fiveButton);
    group.add(allButton);

    JFrame frame = new JFrame("Make a choice!");
    JPanel panel = new JPanel();
		frame.setSize(600, 600);
    frame.setBackground(Color.white);
    frame.setVisible(true);
    panel.add(fiveButton);
    panel.add(allButton);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.add(panel);

    fiveButton.getInputMap().put(KeyStroke.getKeyStroke("A"), "fivePressed");
    fiveButton.getActionMap().put("fivePressed", new AbstractAction() {
      public void actionPerformed(ActionEvent e){
        frame.dispose();
        new BirdleAlphabet("easy");
      }
    });

    allButton.getInputMap().put(KeyStroke.getKeyStroke("B"), "allPressed");
    allButton.getActionMap().put("allPressed", new AbstractAction() {
      public void actionPerformed(ActionEvent e){
        frame.dispose();
        new BirdleAlphabet("hard");
      }
    });

    //Register a listener for the radio buttons.
    fiveButton.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
        frame.dispose();
        new BirdleAlphabet("easy");
      }
    });
    allButton.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
        frame.dispose();
        new BirdleAlphabet("hard");
      }
    });
  }
}

// TO DO

// make game end once all green
// add message if not valid entry (red letters?)
// When incorrect and press "delete", everything gets deleted- fix
// tell jframe to stop being a bitch about screen resizing


// FEATURES

// "easy" "medium" "hard" modes
   // changes # letters in bird chosen
   // easy = fewer letters ??