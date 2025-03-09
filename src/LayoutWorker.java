import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LayoutWorker extends JFrame {
    private JButton zwolnijSięButton;
    private JButton sprawdźPojazdyButton;
    private JButton dodajPojazdButton;
    private JButton usuńPojazdButton;
    private JPanel JPanel1;
    private JLabel JLabelName;
    private JButton wylogujSięButton;
    private String userLogin;
    public LayoutWorker(String userLogin) {
        this.setContentPane(this.JPanel1);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 500);
        setLocationRelativeTo(null);
        setResizable(false);
        this.userLogin=userLogin;
        JLabelName.setText("Jesteś zalogowany jako: " + userLogin);


        zwolnijSięButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LayoutWorker.this.dispose();
                new LayoutWorkerOut(userLogin).setVisible(true);
            }
        });
        sprawdźPojazdyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LayoutWorker.this.dispose();
                new LayoutCars().setVisible(true);
            }
        });
        dodajPojazdButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LayoutWorker.this.dispose();
                new LayoutCarsAdd().setVisible(true);
            }
        });
        usuńPojazdButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LayoutWorker.this.dispose();
                new LayoutCarsDel().setVisible(true);
            }
        });
        wylogujSięButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LayoutWorker.this.dispose();
                new Main().setVisible(true);
            }
        });



    }

}
