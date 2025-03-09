import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class LayoutAdminAdd extends JFrame{
    private JPanel JPanel;
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JButton anulujButton;
    private JButton dodajButton;
    private JTextField textField4;
    private JTextField textField5;

    public LayoutAdminAdd(){
        this.setContentPane(this.JPanel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 500);
        setLocationRelativeTo(null);
        setResizable(false);

        anulujButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LayoutAdminAdd.this.dispose();
                new LayoutAdmin().setVisible(true);
            }
        });
        dodajButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name=textField1.getText();
                String surname=textField2.getText();
                String login=textField3.getText();
                String password=textField4.getText();
                String speciality=textField5.getText();

                Connection conn = DatabaseConnection.Connector("admin", "admin");
                if (conn != null) {
                    try {
                        String sqlMechanic = "INSERT INTO mechanicy (Imie, Nazwisko, Login, Hasło, Specjalność) VALUES (?, ?, ?, ?, ?)";
                        try (PreparedStatement pstmtMechanic = conn.prepareStatement(sqlMechanic)) {
                            pstmtMechanic.setString(1, name);
                            pstmtMechanic.setString(2, surname);
                            pstmtMechanic.setString(3, login);
                            pstmtMechanic.setString(4, password);
                            pstmtMechanic.setString(5, speciality);

                            int affectedRowsMechanic = pstmtMechanic.executeUpdate();
                            if (affectedRowsMechanic == 0) {
                                throw new SQLException("Tworzenie mechanika nie powiodło się, żaden wiersz nie został dodany.");
                            }
                        }

                        String createUserSQL = "CREATE USER ?@'%' IDENTIFIED BY ?";
                        try (PreparedStatement pstmtCreateUser = conn.prepareStatement(createUserSQL)) {
                            pstmtCreateUser.setString(1, login);
                            pstmtCreateUser.setString(2, password);
                            pstmtCreateUser.execute();
                        }

                        String grantPrivilegesSQL = "GRANT SELECT, INSERT, UPDATE, DELETE ON zakladmechaniki.* TO ?@'%'";
                        try (PreparedStatement pstmtGrantPrivileges = conn.prepareStatement(grantPrivilegesSQL)) {
                            pstmtGrantPrivileges.setString(1, login);
                            pstmtGrantPrivileges.execute();
                        }

                        JOptionPane.showMessageDialog(null, "Dodano pomyślnie użytkownika i nadano uprawnienia.", "Sukces", JOptionPane.INFORMATION_MESSAGE);
                        LayoutAdminAdd.this.dispose();
                         new LayoutAdmin().setVisible(true);

                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Błąd podczas dodawania użytkownika: " + ex.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Nie udało się połączyć z bazą danych.", "Błąd", JOptionPane.ERROR_MESSAGE);
                }


            }
        });
    }

}
