import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LayoutCars extends JFrame{
    private JTable tableCars;
    private JButton Powrot;
    private JTextField textField1;
    private JButton dodajButton;
    private JPanel JPanelCars;
    private JButton wyeksportujPojazdyButton;

    public LayoutCars() {
        super("Program");
        this.setContentPane(this.JPanelCars);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 500);
        setLocationRelativeTo(null);
        setResizable(false);

        DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "Marka", "Model", "Silnik", "Komentarz"}, 0);
        tableCars.setModel(model);
        tableCars.getColumnModel().getColumn(0).setPreferredWidth(5);
        tableCars.getColumnModel().getColumn(1).setPreferredWidth(25);
        tableCars.getColumnModel().getColumn(2).setPreferredWidth(25);
        tableCars.getColumnModel().getColumn(3).setPreferredWidth(25);
        loadCarsData(model);

        dodajButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String CarId = textField1.getText(); // Pobieranie ID mechanika z textField1
                if (!CarId.isEmpty()) {
                    LayoutCars.this.dispose(); // Zamknięcie obecnego okna
                    new LayoutCarsAddCom(CarId).setVisible(true); // Otwarcie nowego okna z przekazaniem ID
                } else {
                    JOptionPane.showMessageDialog(LayoutCars.this, "Proszę wprowadzić ID pojazdu.", "Błąd", JOptionPane.ERROR_MESSAGE);
                }

            }
        });
        Powrot.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LayoutCars.this.dispose();
                new LayoutAdmin().setVisible(true);
            }
        });


        wyeksportujPojazdyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exportTableToCSV(tableCars, "pojazdy_export.csv");
            }
        });
    }
    private void loadCarsData(DefaultTableModel model) {
        String query = "SELECT ID_Pojazdu, Marka, Model, Silnik, Komentarz FROM pojazdy";
        try (Connection conn = DatabaseConnection.Connector("admin", "admin");
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("ID_Pojazdu"),
                        rs.getString("Marka"),
                        rs.getString("Model"),
                        rs.getString("Silnik"),
                        rs.getString("Komentarz")
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Błąd podczas ładowania danych mechaników: " + ex.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void exportTableToCSV(JTable table, String fileName) {
        // Ścieżka do folderu src/csv w strukturze katalogów projektu
        String folderPath = "src/csv/";
        File directory = new File(folderPath);
        if (!directory.exists()) {
            directory.mkdirs(); // Tworzy folder 'csv' w 'src', jeśli nie istnieje
        }

        String filePath = folderPath + fileName;
        try (FileWriter csvWriter = new FileWriter(filePath)) {
            for (int i = 0; i < table.getColumnCount(); i++) {
                csvWriter.append(table.getColumnName(i));
                if (i < table.getColumnCount() - 1) csvWriter.append(",");
            }
            csvWriter.append("\n");

            for (int i = 0; i < table.getRowCount(); i++) {
                for (int j = 0; j < table.getColumnCount(); j++) {
                    csvWriter.append("\"").append(table.getValueAt(i, j).toString()).append("\"");
                    if (j < table.getColumnCount() - 1) csvWriter.append(",");
                }
                csvWriter.append("\n");
            }

            JOptionPane.showMessageDialog(this, "Dane zostały wyeksportowane do pliku CSV w ścieżce " + filePath, "Informacja", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Błąd podczas zapisywania do pliku CSV: " + ex.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
        }
    }

}
