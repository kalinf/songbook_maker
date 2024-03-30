import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;

//klasa implementuje okno udostępniające edycję śpiewnika, jego zapis, a także generowanie go do pliku PDF
//udostępnia tytuł, listę piosenek oraz przyciski uruchamiające funkcje
public class Songbook_Swing {
    JPanel panel = new JPanel(new BorderLayout());
    JPanel p1 = new JPanel(new BorderLayout());
    JPanel p2 = new JPanel(new BorderLayout());
    JPanel p3 = new JPanel(new FlowLayout());
    JPanel p4 = new JPanel(new FlowLayout());
    JPanel p5 = new JPanel(new BorderLayout());
    JPanel p6 = new JPanel(new BorderLayout());
    JButton SaveButt, SongSortButt, AddButt, RemoveButt, EditButt, PDFGenButt;
    JLabel titleL, songsL, accPathL;
    JTextField titleT, accPathT;

    //funkcja zapisuje obiekt "songbook" do pliku pod aresem "filename"
    private void saveToFile(Songbook songbook, String filename) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename));
        oos.writeObject(songbook);
        oos.close();
    }

    //statyczna funkcja uruchamiająca okno z komunikatem błędu
    public static void errorMessage(String message, Color_palette theme){
        JFrame frame = new JFrame("BŁĄD");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        ErrorMessageSwing editor = new ErrorMessageSwing(message, theme);
        frame.getContentPane().add(editor.panel).setBackground(theme.CP.get(0));//kolorki;
        frame.pack();
        frame.setVisible(true);
    }
    public Songbook_Swing(Songbook songbook, String filename, Color_palette theme){
        initialize(songbook, filename, theme);
    }
    private void initialize(Songbook songbook, String filename, Color_palette theme){
        titleL = new JLabel("Tytuł");
        p1.add(titleL, BorderLayout.NORTH);
        titleT = new JTextField(songbook.title, 20);
        p1.add(titleT, BorderLayout.SOUTH);
        songsL = new JLabel("Piosenki");
        p2.add(songsL, BorderLayout.NORTH);
        JList<String> songs = new JList<>(songbook.titles);
        JScrollPane scroll = new JScrollPane(songs);
        p2.add(scroll, BorderLayout.SOUTH);
        AddButt = new JButton("Dodaj piosenkę");
        p3.add(AddButt, BorderLayout.NORTH);
        RemoveButt = new JButton("Usuń piosenkę");
        p3.add(RemoveButt);
        EditButt = new JButton("Edytuj piosenkę");
        p3.add(EditButt, BorderLayout.SOUTH);
        SongSortButt = new JButton("Sortuj piosenki");
        p4.add(SongSortButt);
        PDFGenButt = new JButton("Generuj PDF");
        p4.add(PDFGenButt);
        accPathL = new JLabel("Ścieżka dostępu");
        p5.add(accPathL, BorderLayout.NORTH);
        accPathT = new JTextField(filename, 20);
        p5.add(accPathT, BorderLayout.SOUTH);
        SaveButt = new JButton("Zapisz");
        p4.add(SaveButt);
        p6.add(p1, BorderLayout.NORTH);
        p6.add(p2, BorderLayout.CENTER);
        p6.add(p3, BorderLayout.SOUTH);
        panel.add(p6, BorderLayout.NORTH);
        panel.add(p4, BorderLayout.CENTER);
        panel.add(p5, BorderLayout.SOUTH);

        //przycisk otwiera okno edycji nowej piosenki
        AddButt.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Song song = new Song();
                JFrame frame = new JFrame("Piosenka");
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                Song_Swing editor = new Song_Swing(song, songbook, theme);
                frame.getContentPane().add(editor.panel);
                frame.pack();
                frame.setVisible(true);
            }
        });

        //przycisk usuwa ze śpiewnika wszystkie zaznaczone piosenki
        RemoveButt.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                List<String> selected = songs.getSelectedValuesList();
                for(String s : selected) {
                    songbook.RemoveSong(s);
                }
            }
        });

        //przycisk otwiera okno edycji zaznaczonej piosenki
        EditButt.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                List<String> selected = songs.getSelectedValuesList();
                if(selected.size() == 1){
                    JFrame frame = new JFrame("Piosenka");
                    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    try {
                        Song_Swing editor = new Song_Swing(songbook.findSong(songs.getSelectedValuesList().get(0)), songbook, theme);
                        frame.getContentPane().add(editor.panel);
                        frame.pack();
                        frame.setVisible(true);
                    }
                    catch (NotFoundException ne){
                        errorMessage("Nie odnaleziono piosenki", theme);
                    }
                }
                else{
                    errorMessage("Niedozwolona ilość wybranych piosenek", theme);
                }
            }
        });

        //przycisk sortuje piosenki alfabetycznie po tytułach
        SongSortButt.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                songbook.SongSort();
            }
        });

        //przycisk zapisuje zmiany dokonane na śpiewniku i zapisuje go do pliku pod adres "accPathT"
        SaveButt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                songbook.title = titleT.getText();
                try {
                    saveToFile(songbook, accPathT.getText());
                } catch (IOException ex) {
                    errorMessage("Błąd dostępu", theme);
                }
            }
        });

        //przycisk uruchamia okno generowania pliku PDF, podając konstruktorowi edytowany aktualnie śpiewnik
        PDFGenButt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = new JFrame("Generator PDF");
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                PDF_Swing editor = new PDF_Swing(songbook, theme);
                frame.getContentPane().add(editor.panel);
                frame.pack();
                frame.setVisible(true);
            }
        });
    }
}
