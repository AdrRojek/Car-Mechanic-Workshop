import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static java.sql.DriverManager.getConnection;


public class Main extends JFrame {
    private JPasswordField passwordField;
    private JTextField textField;
    private JButton loginButton;
    private JPanel JPanelMain;
    private JPanel JPanelLeft;
    private JPanel JPanelRight;
    private JLabel ImageIcon;


    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Main().setVisible(true);
            }
        });
    }


    public Main() {
        super("Program");
        this.setContentPane(this.JPanelMain);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 500);
        setLocationRelativeTo(null);
        setResizable(false);


        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = textField.getText();
                String password = new String(passwordField.getPassword());

                Connection conn = DatabaseConnection.Connector(username, password);
                if (conn != null) {
                    JOptionPane.showMessageDialog(Main.this, "Zalogowano pomyślnie!");
                    if(username.equals("admin")){
                        Main.this.dispose();
                        new LayoutAdmin().setVisible(true);
                    }else {
                        Main.this.dispose();
                        new LayoutWorker(username).setVisible(true);
                    }
                } else {
                    JOptionPane.showMessageDialog(Main.this, "Błędne dane", "Błąd", JOptionPane.ERROR_MESSAGE);
                }



            }
        });


    }

}
