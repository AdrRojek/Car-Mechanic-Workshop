import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class LayoutWorkerOut extends JFrame {
    private JPanel JPanelOut;
    private JTextArea textArea1;
    private JButton zwolnijButton;
    private JButton powrotButton;
    private JTextField textField1;
    private String login;

    public LayoutWorkerOut(String login) {
        this.setContentPane(this.JPanelOut);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 500);
        setLocationRelativeTo(null);
        setResizable(false);
        this.login = login;
        zwolnijButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                verifyAndRemoveUser(login);
                LayoutWorkerOut.this.dispose();
                new Main().setVisible(true);
            }
        });
        powrotButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LayoutWorkerOut.this.dispose();
                new LayoutWorker(login).setVisible(true);
            }
        });
    }

    private void verifyAndRemoveUser(String login) {
        String providedSurname = textField1.getText();
        String reason = textArea1.getText();
        Connection conn = DatabaseConnection.Connector();
        if (conn != null) {
            try {
                conn.setAutoCommit(false);
                String selectQuery = "SELECT ID_Mechanika, Nazwisko FROM mechanicy WHERE Login = ?";
                PreparedStatement selectStmt = conn.prepareStatement(selectQuery);
                selectStmt.setString(1, login);
                ResultSet rs = selectStmt.executeQuery();

                Integer mechanicId = null;
                String actualSurname = null;
                if (rs.next()) {
                    mechanicId = rs.getInt("ID_Mechanika");
                    actualSurname = rs.getString("Nazwisko");
                }
                rs.close();
                selectStmt.close();

                if (mechanicId != null && providedSurname.equals(actualSurname)) {

                    String insertQuery = "INSERT INTO zwolnienia (ID_Mechanika, Powod, NazwiskoMechanika) VALUES (?, ?, ?)";
                    PreparedStatement insertStmt = conn.prepareStatement(insertQuery);
                    insertStmt.setInt(1, mechanicId);
                    insertStmt.setString(2, reason);
                    insertStmt.setString(3, providedSurname);
                    insertStmt.executeUpdate();


                    String deleteQuery = "DELETE FROM mechanicy WHERE Login = ?";
                    PreparedStatement deleteStmt = conn.prepareStatement(deleteQuery);
                    deleteStmt.setString(1, login);
                    deleteStmt.executeUpdate();


                    String deleteUser = String.format("DROP USER '%s'@'%%'", login.replace("'", "''"));
                    Statement delUserStmt = conn.createStatement();
                    delUserStmt.execute(deleteUser);

                    conn.commit();

                    JOptionPane.showMessageDialog(null, "Użytkownik został usunięty.");
                    this.dispose();
                } else {
                    conn.rollback();
                    JOptionPane.showMessageDialog(null, "Podane nazwisko nie pasuje do loginu użytkownika.");
                }
            } catch (SQLException ex) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
                JOptionPane.showMessageDialog(null, "Błąd bazy danych: " + ex.getMessage());
            } finally {
                try {
                    if (conn != null && !conn.isClosed()) {
                        conn.setAutoCommit(true);
                        conn.close();
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }





    static class DatabaseConnection {
        public static Connection Connector() {
            String dbUrl = "jdbc:mysql://localhost:3306/zakladmechaniki";
            String username = "admin";
            String password = "admin";
            try {
                return DriverManager.getConnection(dbUrl, username, password);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Nie można połączyć z bazą danych.", "Błąd połączenia", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
                return null;
            }
        }
    }
}

