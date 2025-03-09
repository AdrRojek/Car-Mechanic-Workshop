import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class LayoutCarsAddCom extends JFrame {
    private JPanel JPanelCarsCom;
    private JTextPane textPane1;
    private JButton anulujButton;
    private JButton zapiszButton;
    private String Carid;

    public LayoutCarsAddCom(String Carid) {
        super("Program");
        this.Carid = Carid;
        this.setContentPane(this.JPanelCarsCom);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 500);
        setLocationRelativeTo(null);
        setResizable(false);

        anulujButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LayoutCarsAddCom.this.dispose();
                new LayoutCars().setVisible(true);
            }
        });

        zapiszButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String newComment = textPane1.getText();
                if (newComment.isEmpty()) {
                    JOptionPane.showMessageDialog(LayoutCarsAddCom.this, "Proszę wprowadzić komentarz.", "Błąd", JOptionPane.ERROR_MESSAGE);
                } else {
                    updateCarComment(Carid, newComment);
                }
            }
        });
    }

    private void updateCarComment(String carId, String newComment) {
        String sqlUpdateCar = "UPDATE pojazdy SET Komentarz = ? WHERE ID_Pojazdu = ?";

        try (Connection conn = DatabaseConnection.Connector("admin", "admin");
             PreparedStatement pstmtUpdateCar = conn.prepareStatement(sqlUpdateCar)) {

            pstmtUpdateCar.setString(1, newComment);
            pstmtUpdateCar.setString(2, carId);
            pstmtUpdateCar.executeUpdate();

            JOptionPane.showMessageDialog(this, "Komentarz zaktualizowany pomyślnie.");
            this.dispose();
            new LayoutCars().setVisible(true);
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Błąd SQL: " + ex.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
        }
    }
}
