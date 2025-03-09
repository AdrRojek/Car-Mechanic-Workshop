import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LayoutAdmin extends JFrame {
    private JPanel panel1;
    private JButton dodajMechanikaButton;
    private JButton zwolnijMechanikaButton;
    private JButton sprawdźPojazdyButton;
    private JButton dodajPojazdButton;
    private JButton usuńPojazdButton;
    private JButton wylogujSięButton;
    private JButton pokażMechanikówButton;

    public LayoutAdmin() {
        this.setContentPane(this.panel1);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 500);
        setLocationRelativeTo(null);
        setResizable(false);


        dodajMechanikaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LayoutAdmin.this.dispose();
                new LayoutAdminAdd().setVisible(true);
            }
        });
        wylogujSięButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LayoutAdmin.this.dispose();
                new Main().setVisible(true);
            }
        });
        zwolnijMechanikaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LayoutAdmin.this.dispose();
                new LayoutAdminDelete().setVisible(true);
            }
        });
        pokażMechanikówButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LayoutAdmin.this.dispose();
                new LayoutShowMech().setVisible(true);
            }
        });
        sprawdźPojazdyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LayoutAdmin.this.dispose();
                new LayoutCars().setVisible(true);
            }
        });
        dodajPojazdButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LayoutAdmin.this.dispose();
                new LayoutCarsAdd().setVisible(true);
            }
        });
        usuńPojazdButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LayoutAdmin.this.dispose();
                new LayoutCarsDel().setVisible(true);
            }
        });
    }

}
