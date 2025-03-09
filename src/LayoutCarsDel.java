import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LayoutCarsDel extends JFrame {
    private JPanel panel1;
    private JComboBox<CarItem> comboBox1;
    private JButton anulujButton;
    private JButton usunButton;

    public LayoutCarsDel() {
        this.setContentPane(this.panel1);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 500);
        setLocationRelativeTo(null);
        setResizable(false);

        loadCarDataIntoComboBox();

        anulujButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LayoutCarsDel.this.dispose();

            }
        });

        usunButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteSelectedCar();

                comboBox1.removeItem(comboBox1.getSelectedItem());
            }
        });

        setVisible(true);
    }

    private void loadCarDataIntoComboBox() {
        Connection conn = DatabaseConnection.Connector("admin", "admin");
        if (conn != null) {
            try {
                String sql = "SELECT ID, Marka, Model FROM pojazdy";
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    ResultSet rs = pstmt.executeQuery();
                    while (rs.next()) {
                        int id = rs.getInt("ID");
                        String marka = rs.getString("Marka");
                        String model = rs.getString("Model");
                        CarItem carItem = new CarItem(id, marka, model);
                        comboBox1.addItem(carItem);
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Błąd podczas ładowania danych pojazdów: " + ex.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Nie udało się połączyć z bazą danych.", "Błąd", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteSelectedCar() {
        CarItem selectedCar = (CarItem) comboBox1.getSelectedItem();
        if (selectedCar != null) {
            Connection conn = DatabaseConnection.Connector("admin", "admin");
            if (conn != null) {
                String sql = "DELETE FROM pojazdy WHERE ID = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setInt(1, selectedCar.getId());

                    int affectedRows = pstmt.executeUpdate();
                    if (affectedRows > 0) {
                        JOptionPane.showMessageDialog(null, "Usunięto pojazd.", "Sukces", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "Nie znaleziono pojazdu do usunięcia.", "Błąd", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Błąd podczas usuwania pojazdu: " + ex.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Nie udało się połączyć z bazą danych.", "Błąd", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Proszę wybrać pojazd do usunięcia.", "Błąd", JOptionPane.WARNING_MESSAGE);
        }
    }


    private static class CarItem {
        private final int id;
        private final String marka;
        private final String model;

        public CarItem(int id, String marka, String model) {
            this.id = id;
            this.marka = marka;
            this.model = model;
        }

        public int getId() {
            return id;
        }

        @Override
        public String toString() {
            return marka + " " + model + " (ID: " + id + ")";
        }
    }
}
