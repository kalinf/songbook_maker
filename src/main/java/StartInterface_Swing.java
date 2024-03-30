import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
//klasa implementuje okno interfejsu startowego aplikacji, udostępnia pole tekstowe przeznaczone
//do wpisania w nie ścieżki dostępu do pliku oraz przyciski uruchamiające konkretne funkcje programu
public class StartInterface_Swing {
    JPanel panel = new JPanel();
    JPanel row1 = new JPanel();
    JPanel row2 = new JPanel();
    JLabel accPathL;
    JTextField accPathT;
    JButton NewSongbookButt;
    JButton SortPDFButt;
    JButton EditSongbookButt;
    public StartInterface_Swing(Color_palette theme){
        initialize(theme);
    }
    private void initialize(Color_palette theme){
        row1.setBackground(theme.CP.get(0));//kolorki
        row2.setBackground(theme.CP.get(0));//kolorki
        row1.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        row2.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panel.setLayout(new BorderLayout());

        accPathL = new JLabel("Ścieżka dostępu");
        accPathL.setForeground(theme.CP.get(1)); //kolorki
        row1.add(accPathL);
        accPathT = new JTextField("");
        accPathT.setPreferredSize(new Dimension(200, 30));
        row1.add(accPathT);

        NewSongbookButt = new JButton("Stwórz nowy śpiewnik");
        NewSongbookButt.setForeground(theme.CP.get(2)); //kolorki
        row2.add(NewSongbookButt);
        SortPDFButt = new JButton("Sortuj PDF do druku");
        SortPDFButt.setForeground(theme.CP.get(2)); //kolorki
        row2.add(SortPDFButt);
        EditSongbookButt = new JButton("Edytuj istniejący śpiewnik");
        EditSongbookButt.setForeground(theme.CP.get(2)); //kolorki
        row2.add(EditSongbookButt);
        panel.add(row1, BorderLayout.NORTH);
        panel.add(row2, BorderLayout.SOUTH);

        //przycisk uruchamia okno edyji nowego śpiewnika
        NewSongbookButt.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Songbook songbook = new Songbook();
                JFrame frame = new JFrame("Śpiewnik");
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                Songbook_Swing editor = new Songbook_Swing(songbook, "", theme);
                frame.getContentPane().add(editor.panel);
                frame.pack();
                frame.setVisible(true);
            }
        });
        //przycisk uruchamia okno edycji śpiewnika wskazanego w polu "accpathT"
        EditSongbookButt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    ObjectInputStream ois = new ObjectInputStream(new FileInputStream(accPathT.getText()));
                    JFrame frame = new JFrame("Śpiewnik");
                    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    Songbook songbook = (Songbook) ois.readObject();
                    Songbook_Swing editor = new Songbook_Swing(songbook, accPathT.getText(), theme);
                    frame.getContentPane().add(editor.panel);
                    frame.pack();
                    frame.setVisible(true);
                }
                catch(FileNotFoundException ex){
                    Songbook_Swing.errorMessage("Błędna ścieżka dostępu", theme);
                } catch (IOException ex) {
                    Songbook_Swing.errorMessage("dlaczego :(", theme);
                } catch (ClassNotFoundException ex) {
                    Songbook_Swing.errorMessage("Obiekt niewłaściwej klasy", theme);
                }
            }
        });
        //przycisk uruchamia okno sortera, który posortuje plik znajdujący się pod
        //adresem "accpathT"
        SortPDFButt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = new JFrame("Sortowanie PDF do druku");
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                PDF_Sort_Swing editor = new PDF_Sort_Swing(accPathT.getText(), theme);
                frame.getContentPane().add(editor.panel);
                frame.pack();
                frame.setVisible(true);
            }
        });
    }
}
