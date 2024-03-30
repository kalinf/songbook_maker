import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

//klasa implementuje okno edycji piosenki; udostępnia jej tytuł, chwyty, tekst, a także przyciski funkcyjne
public class Song_Swing {
    JPanel panel = new JPanel(new BorderLayout(5, 5));
    JPanel p1 = new JPanel(new BorderLayout(5, 5));
    JPanel p2 = new JPanel(new BorderLayout(5, 5));
    JPanel p3 = new JPanel(new BorderLayout(5, 5));
    JPanel p4 = new JPanel();
    JPanel p5 = new JPanel(new BorderLayout(5, 5));
    JLabel titleL, chordsL, textL;
    JTextField titleT;
    JTextArea textT, chordsT;
    JButton saveButt, wordSwapButt;
    String title;
    Boolean saveClicked = false;

    //argumentami konstruktora są edytowana piosenka i śpiewnik, do którego należy
    public Song_Swing(Song song, Songbook songbook, Color_palette theme) {
        initialize(song, songbook, theme);
    }

    private void initialize(final Song song, Songbook songbook, Color_palette theme){
        title = song.title;

        titleL = new JLabel("Tytuł");
        p1.add(titleL, BorderLayout.NORTH);
        titleT = new JTextField(song.title);
        titleT.setPreferredSize(new Dimension(200, 30));
        p1.add(titleT, BorderLayout.SOUTH);
        textL = new JLabel("Tekst");
        p2.add(textL, BorderLayout.NORTH);
        textT = new JTextArea(song.text, 30, 30);
        textT.setLineWrap(true);
        textT.setWrapStyleWord(true);
        p2.add(new JScrollPane(textT), BorderLayout.SOUTH);
        chordsL = new JLabel("Chwyty");
        p3.add(chordsL, BorderLayout.NORTH);
        chordsT = new JTextArea(song.chords, 10, 30);
        chordsT.setLineWrap(true);
        chordsT.setWrapStyleWord(true);
        p3.add(new JScrollPane(chordsT), BorderLayout.SOUTH);
        saveButt = new JButton("Zapisz");
        p4.add(saveButt);
        wordSwapButt = new JButton("Zamiana słów");
        p4.add(wordSwapButt);
        p5.add(p3, BorderLayout.NORTH);
        p5.add(p4, BorderLayout.SOUTH);
        panel.add(p1, BorderLayout.NORTH);
        panel.add(p2, BorderLayout.CENTER);
        panel.add(p5, BorderLayout.SOUTH);

        //przycisk zapisuje zmiany dokonane na piosence i jeśli tytuł jest unikatowy, a piosenka nie została
        //jeszcze dodana do śpiewnika, dodaje ją do śpiewnika
        saveButt.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                song.title = titleT.getText();
                song.text = textT.getText();
                song.chords = chordsT.getText();

                if(Objects.equals(song.title, "") || (!Objects.equals(song.title, title) && songbook.titles.contains(song.title))){
                    Songbook_Swing.errorMessage("Niedozwolony tytuł", theme);
                }
                else {
                    songbook.titles.add(song.title);
                    songbook.titles.remove(title);
                    title = song.title;
                    if(!saveClicked){
                        songbook.AddSong(song);
                        saveClicked = true;
                    }
                }
            }
        });

        //przycisk uruchamia okno zamiany słów piosenki
        wordSwapButt.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFrame frame = new JFrame("Zamiana słów");
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                WordSwapper_Swing editor = new WordSwapper_Swing(song, theme);
                frame.getContentPane().add(editor.panel);
                frame.pack();
                frame.setVisible(true);
            }
        });
    }
}
