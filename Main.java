import javax.swing.*;
//import java.awt.*;
//import java.awt.event.*; 

// VIDEO: AROUND 10-15 MINS

class Main {

  private static JFrame frame;
  
  public static void main(String[] args) {
    frame = new JFrame();
    int option = buttons2();
    System.out.println(option);
    if (option == 0) {
      new BirdleAlphabet("easy");
    } else {
      new BirdleAlphabet("hard");
    }
    //new BirdleAlphabet("hard");
  }

  public static int buttons2() {
    Object[] options = {"EASIER", "HARDER"};
    int n = JOptionPane.showOptionDialog(frame,
    "Pick the mode of gameplay:\nfive letter birds only (EASIER), or\nany number of letters (HARDER)",
    "Birdle",
    JOptionPane.YES_NO_OPTION,
    JOptionPane.QUESTION_MESSAGE,
    null,     //do not use a custom Icon
    options,  //the titles of buttons
    options[0]); //default button title
    return n;
  }
 }

// TO DO

// make game end once all green
// add message if not valid entry (red letters?)
// resize frame to phone screen
// if you don't get it by end, display word

// *** fix button system when game starts ***

// VIDEO: AROUND 10-15 MINS