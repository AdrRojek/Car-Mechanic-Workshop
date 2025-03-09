import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class LayoutAdminDelete extends JFrame{
    private javax.swing.JPanel JPanel;
    private JTextField textField1;
    private JTextField textField2;
    private JButton anulujButton;
    private JButton usuńButton;
    private JTextField tak;
    private JTextField textField3;
    private JPanel JPanelDelete;

    public LayoutAdminDelete(){
        super("Program");
        this.setContentPane(this.JPanelDelete);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 500);
        setLocationRelativeTo(null);
        setResizable(false);

        usuńButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String potw=tak.getText();
                if(potw.equals("tak")){
                    String login=textField3.getText();

                    Connection conn = DatabaseConnection.Connector("admin", "admin");
                    if (conn != null) {
                        try {
                            String sqlMechanic = "DELETE FROM mechanicy WHERE Login = ?";
                            try (PreparedStatement pstmtMechanic = conn.prepareStatement(sqlMechanic)) {
                                pstmtMechanic.setString(1, login);


                                int affectedRowsMechanic = pstmtMechanic.executeUpdate();
                                if (affectedRowsMechanic == 0) {
                                    throw new SQLException("Usunięcie mechanika nie powiodło się, żaden wiersz nie został usunięty.");
                                }
                            }

                            String createUserSQL = "DROP USER ?@'%'";
                            try (PreparedStatement pstmtCreateUser = conn.prepareStatement(createUserSQL)) {
                                pstmtCreateUser.setString(1, login);
                                pstmtCreateUser.execute();
                            }

                            JOptionPane.showMessageDialog(null, "Usunięto mechanika", "Sukces", JOptionPane.INFORMATION_MESSAGE);

                            LayoutAdminDelete.this.dispose();
                            new LayoutAdmin().setVisible(true);

                        } catch (SQLException ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(null, "Błąd podczas usuwania mechanika: " + ex.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Nie udało się połączyć z bazą danych.", "Błąd", JOptionPane.ERROR_MESSAGE);
                    }
                }else{
                    JOptionPane.showMessageDialog(null, "Brak potwierdzenia!");
                }
            }
        });
        anulujButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LayoutAdminDelete.this.dispose();
                new LayoutAdmin().setVisible(true);
            }
        });
    }
}
