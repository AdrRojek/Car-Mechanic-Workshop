import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LayoutShowMech extends JFrame{
    private JTable table1;
    private JButton Powrot;
    private JPanel JPanelShow;
    private JTextField textField1;
    private JButton zmieńButton;
    private JButton wyeksportujTabeleButton;

    public LayoutShowMech(){
        super("Program");
        this.setContentPane(this.JPanelShow);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 500);
        setLocationRelativeTo(null);
        setResizable(false);

        DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "Imię", "Nazwisko", "Login", "Specjalność"}, 0);
        table1.setModel(model);

        loadMechanicsData(model);

        Powrot.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LayoutShowMech.this.dispose();
                new LayoutAdmin().setVisible(true);
            }
        });
        zmieńButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        zmieńButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String mechanicId = textField1.getText();
                if (!mechanicId.isEmpty()) {
                    LayoutShowMech.this.dispose();
                    new LayoutChange(mechanicId).setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(LayoutShowMech.this, "Proszę wprowadzić ID mechanika.", "Błąd", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        wyeksportujTabeleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exportTableToCSV(table1, "src/csv/mechanicy_export.csv");
            }
        });
    }

    private void loadMechanicsData(DefaultTableModel model) {
        String query = "SELECT ID_Mechanika, Imie, Nazwisko, Login, Specjalność FROM mechanicy";
        try (Connection conn = DatabaseConnection.Connector("admin", "admin");
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("ID_Mechanika"),
                        rs.getString("Imie"),
                        rs.getString("Nazwisko"),
                        rs.getString("Login"),
                        rs.getString("Specjalność")
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Błąd podczas ładowania danych mechaników: " + ex.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void exportTableToCSV(JTable table, String path) {
        try {
            TableModel model = table.getModel();
            FileWriter csv = new FileWriter(new File(path));

            for (int i = 0; i < model.getColumnCount(); i++) {
                csv.write(model.getColumnName(i) + ",");
            }

            csv.write("\n");

            for (int i = 0; i < model.getRowCount(); i++) {
                for (int j = 0; j < model.getColumnCount(); j++) {
                    csv.write(model.getValueAt(i, j).toString() + ",");
                }
                csv.write("\n");
            }

            csv.close();
            JOptionPane.showMessageDialog(null, "Tabela została wyeksportowana do pliku CSV.");
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Błąd podczas eksportowania do pliku CSV: " + ex.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
        }
    }



}
