import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        App_Form ap = new App_Form();
        SecondForm secondForm =  new SecondForm();




    }

    public static class App_Form  extends JFrame {
        private JPanel main_Panel;
        private JButton settingsButton;
        private JButton closeButton;

        public App_Form() {


            setContentPane(main_Panel);
            setSize(600,800);
            setTitle("Option Dialog");
            setSize(300, 200);
            setLocation(450,250);
            setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            setVisible(true);


            settingsButton.addActionListener(e -> {
                SecondForm secondForm = new SecondForm();
                secondForm.setVisible(true);


            });

            closeButton.addActionListener(e -> {
                System.exit(0);

            });
        }



        }
}
