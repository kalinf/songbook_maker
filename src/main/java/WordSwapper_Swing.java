import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

//klasa implementuje okno zamiany słów piosenki
public class WordSwapper_Swing {
    JPanel panel = new JPanel();
    JButton SaveButt;
    JLabel ChangeFromL, ChangeToL;
    JTextField ChangeFromT, ChangeToT;

    public WordSwapper_Swing(Song song, Color_palette theme) {
        initialize(song, theme);
    }

    private void initialize(final Song song, Color_palette theme){
        ChangeFromL = new JLabel("zamień");
        panel.add(ChangeFromL);
        ChangeFromT = new JTextField("", 13);
        panel.add(ChangeFromT);
        ChangeToL = new JLabel("na");
        panel.add(ChangeToL);
        ChangeToT = new JTextField("", 13);
        panel.add(ChangeToT);
        SaveButt = new JButton("Zamień");
        panel.add(SaveButt);

        //przycisk powoduje zamianę w piosence "song" słowa pobranego z "ChangeFromT" na słowo pobrane z "ChangeToT"
        SaveButt.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                song.WordSwap(ChangeFromT.getText(), ChangeToT.getText());
            }
        });
    }
}
