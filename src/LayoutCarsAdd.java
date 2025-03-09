import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class LayoutCarsAdd extends JFrame{
    private javax.swing.JPanel JPanel;
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JButton anulujButton;
    private JButton dodajButton;
    private JTextArea textArea1;
    private JButton wczytajZPlikuCSVButton;

    public LayoutCarsAdd(){
        this.setContentPane(this.JPanel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 500);
        setLocationRelativeTo(null);
        setResizable(false);

        anulujButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LayoutCarsAdd.this.dispose();
                new LayoutAdmin().setVisible(true);
            }
        });
        dodajButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String marka=textField1.getText();
                String model=textField2.getText();
                String silnik=textField3.getText();
                String komentarz=textArea1.getText();


                Connection conn = DatabaseConnection.Connector("admin", "admin");
                if (conn != null) {
                    try {
                        String sqlMechanic = "INSERT INTO pojazdy (Marka, Model, Silnik, Komentarz) VALUES (?, ?, ?, ?)";
                        try (PreparedStatement pstmtMechanic = conn.prepareStatement(sqlMechanic)) {
                            pstmtMechanic.setString(1, marka);
                            pstmtMechanic.setString(2, model);
                            pstmtMechanic.setString(3, silnik);
                            pstmtMechanic.setString(4, komentarz);


                            int affectedRowsMechanic = pstmtMechanic.executeUpdate();
                            if (affectedRowsMechanic == 0) {
                                throw new SQLException("Dodawanie pojazdu nie powiodło się.");
                            }
                        }


                        JOptionPane.showMessageDialog(null, "Dodano pomyślnie Pojazd.", "Sukces", JOptionPane.INFORMATION_MESSAGE);

                        LayoutCarsAdd.this.dispose();
                        new LayoutAdmin().setVisible(true);

                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Błąd podczas dodawania Pojazdu: " + ex.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Nie udało się połączyć z bazą danych.", "Błąd", JOptionPane.ERROR_MESSAGE);
                }


            }
        });
        wczytajZPlikuCSVButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        wczytajZPlikuCSVButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Wybierz plik CSV z danymi pojazdów");
                int result = fileChooser.showOpenDialog(LayoutCarsAdd.this);

                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    importDataFromCSV(selectedFile);
                }
            }
        });
    }
    private void importDataFromCSV(File file) {
        String line;
        String cvsSplitBy = ",";
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            boolean firstLine = true;
            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }

                String[] carData = line.split(cvsSplitBy);
                addCarToDatabase(carData[0], carData[1], carData[2], carData[3]);
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Błąd podczas czytania pliku CSV: " + e.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addCarToDatabase(String marka, String model, String silnik, String komentarz) {
        Connection conn = DatabaseConnection.Connector("admin", "admin");
        if (conn != null) {
            String sqlMechanic = "INSERT INTO pojazdy (Marka, Model, Silnik, Komentarz) VALUES (?, ?, ?, ?)";
            try (PreparedStatement pstmtMechanic = conn.prepareStatement(sqlMechanic)) {
                pstmtMechanic.setString(1, marka);
                pstmtMechanic.setString(2, model);
                pstmtMechanic.setString(3, silnik);
                pstmtMechanic.setString(4, komentarz);

                int affectedRowsMechanic = pstmtMechanic.executeUpdate();
                if (affectedRowsMechanic == 0) {
                    throw new SQLException("Dodawanie pojazdu nie powiodło się, żaden wiersz nie został dodany.");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Błąd podczas dodawania pojazdu: " + ex.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Nie udało się połączyć z bazą danych.", "Błąd", JOptionPane.ERROR_MESSAGE);
        }
    }

}
