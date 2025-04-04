
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class SecondForm extends JFrame {
    private JPanel mainPanel;
    private JRadioButton onTimeRadioButton;
    private JRadioButton countdownSecRadioButton;
    private JTextField textField1;
    private JTextField textField2;
    private JSpinner speed;
    private JButton chooseColorButton;
    private JButton stopButton;
    private JButton startButton;
    private JLabel chooseLabel;


    private static Timer timer;
    private static boolean isCountdown = false;
    private static boolean isTimeSet = false;
    private static int countdownTime = 0;
    private static int colorChangeInterval = 1000;
    private static Color selectedColor = Color.RED;


    public SecondForm() {
        frameInit();
        setLocationRelativeTo(this);


        setContentPane(mainPanel);
        setSize(700, 500);
        setLocation(300, 150);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);


        ButtonGroup group = new ButtonGroup();
        group.add(onTimeRadioButton);
        group.add(countdownSecRadioButton);

        // Default to "On Time" mode
        onTimeRadioButton.setSelected(true);
// Action listener for choosing a color using the JColorChooser dialog

        chooseColorButton.addActionListener(e -> {
            // Open the color chooser dialog and set the selected color to the label's foreground
            selectedColor = JColorChooser.showDialog(this, "choose color", selectedColor);
            chooseLabel.setForeground(selectedColor); // update label color

        });
// Action listener for the "onTime" radio button, which sets the mode to "time-based" instead of countdown
        onTimeRadioButton.addActionListener(e -> {
            isCountdown = false; // Set countdown mode to false (time-based mode)
            startTime(); // Start the timer based on the provided time


        });
// Action listener for the "countdownSec" radio button, which sets the mode to "countdown" instead of time-based
        countdownSecRadioButton.addActionListener(e -> {
            isCountdown = true;


        });

// Action listener for the start button, which decides whether to start a countdown or a time-based timer
        startButton.addActionListener(e -> {
            if (isCountdown) {
                startCountDown();  //  start the countdown timer
            } else {
                startTime();  //  here start time based timer
            }

        });
        // Action listener for the stop button, which stops the current timer and resets the state
        stopButton.addActionListener(e -> {
            if (timer != null) {
                timer.stop(); // stop the time
            } else {
                isTimeSet = false; // reset the time
                chooseLabel.setText("timer stopped."); // update the label with a message
            }
            enableComponents();  // Enable the input components after stopping the time


        });
// Change listener for the speed slider to update the speed at which the color changes in the second window
        speed.addChangeListener(e -> {
            colorChangeInterval = (int) speed.getValue();    // Update the speed of the color change

        });
    }

    // Method to start the time-based timer
    public void startTime() {
        String timeInput = textField1.getText();  // Get the input time from the text field
        try {
            // Convert the entered time to milliseconds

            int delay = convertTimeToMilliseconds(timeInput);
            if (delay > 0) {
// Create a timer with the calculated delay
                timer = new Timer(delay, e -> {
                    chooseLabel.setText("Time reached: " + timeInput + " seconds");   // Update label when time is reached
                    chooseLabel.setForeground(selectedColor); // Set the label color to the selected color
                    // Open the second window when time is reached
                    new SecondWindow(selectedColor, colorChangeInterval).setVisible(true);
                    dispose();  // Close the current window

                });
                timer.setRepeats(false); // The timer only triggers once
                timer.start(); // start the timer
                isTimeSet = true; // Indicate that time has been set
                disableComponents(); // Disable input components to prevent changes during countdown

            } else {
                JOptionPane.showMessageDialog(this, "Please enter a valid time in the format HH:mm:ss.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid time input! Please enter a number.");

        }
    }


    // Method to start the countdown timer
    public void startCountDown() {
        String timeInput = textField2.getText();  // Get the countdown time from the text field

        try {
            // Convert the entered countdown time to milliseconds
            countdownTime = convertTimeToMilliseconds(timeInput);
            if (countdownTime > 0) {

                startButton.setEnabled(false);  // Disable the start button during countdown

                // Create a timer to handle the countdown
                timer = new Timer(1000, e -> {
                    if (countdownTime > 0) {
                        countdownTime -= 1000;  // Decrease the countdown time by one second
                        chooseLabel.setText("Time remaining: " + countdownTime / 1000 + " seconds");
                    } else {
                        chooseLabel.setText("Countdown finished!");
                        timer.stop();
                        startButton.setEnabled(true);

                        new SecondWindow(selectedColor, colorChangeInterval).setVisible(true);
                        dispose();
                    }
                });
                timer.start();
                disableComponents();

            } else {

                JOptionPane.showMessageDialog(this, "Please enter a valid countdown time!");

            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid countdown time input!");

        }

    }

    // Method to disable user inputs during the timer countdown or time-based timer
    private void disableComponents() {
        textField1.setEnabled(false);
        textField2.setEnabled(false);
        onTimeRadioButton.setEnabled(false);
        countdownSecRadioButton.setEnabled(false);
        chooseColorButton.setEnabled(false);
        speed.setEnabled(false);

    }
    // Method to enable user inputs after the timer is stopped
    private void enableComponents() {
        textField1.setEnabled(true);
        textField2.setEnabled(true);
        onTimeRadioButton.setEnabled(true);
        countdownSecRadioButton.setEnabled(true);
        chooseColorButton.setEnabled(true);
        speed.setEnabled(true);
    }


    // Method to convert time from HH:mm:ss format to milliseconds
    private int convertTimeToMilliseconds(String timeInput) throws Exception {

        String[] parts = timeInput.split(":");

        if (parts.length != 3) {
            throw new Exception("Invalid time format! Please enter time in HH:mm:ss format.");
        }

        try {
            int hours = Integer.parseInt(parts[0]); // parse the hours
            int minutes = Integer.parseInt(parts[1]); //parse the minutes
            int seconds = Integer.parseInt(parts[2]); // parse the seconds
            // Validate that time values are not negative
            if (hours < 0 || minutes < 0 || seconds < 0) {
                throw new Exception("Time values cannot be negative.");
            }

            // Convert to milliseconds
            return (hours * 3600 + minutes * 60 + seconds) * 1000;
        } catch (NumberFormatException e) {
            throw new Exception("Invalid time input! Please enter valid numeric values for hours, minutes, and seconds.");
        }
    }

    // Second window class that shows a background color change effect
    public class SecondWindow extends JFrame {

        private JPanel mainPanel;
        private Timer colorChangeTimer;
        private Color currentColor;
        private int colorChangeSpeed;
        private boolean isDefaultColor = true;
        // Constructor to initialize the second window
        public SecondWindow(Color selectedColor, int colorChangeSpeed) {
            this.currentColor = selectedColor;
            this.colorChangeSpeed = colorChangeSpeed;


            setTitle("Second Window");
            setSize(400, 300);
            setLocation(500, 300);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            mainPanel = new JPanel();
            setContentPane(mainPanel);
            mainPanel.setBackground(Color.WHITE);


            startColorChange();
        }

        // Method to start the background color change effect
        public void startColorChange() {
            colorChangeTimer = new Timer(1000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (isDefaultColor) {
                        mainPanel.setBackground(currentColor);
                    } else {
                        mainPanel.setBackground(Color.WHITE);
                    }
                    isDefaultColor = !isDefaultColor;
                }
            });
            colorChangeTimer.setDelay(1000 / colorChangeSpeed);
            colorChangeTimer.start();
        }

        // Method to update the speed of color change
        public void setSpeed(int newSpeed) {
            this.colorChangeSpeed = newSpeed; // Update the speed
            colorChangeTimer.setDelay(1000 / colorChangeSpeed); // Adjust the delay based on new speed
        }


        // Main method to launch the application
        public static void main(String[] args) {
            SwingUtilities.invokeLater(() -> {
                new SecondForm().setVisible(true);
            });
        }
    }
}
