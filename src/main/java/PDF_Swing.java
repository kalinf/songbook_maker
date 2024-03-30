import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;

//klasa implementuje okno obsługujące generowanie śpiewników w PDF
public class PDF_Swing {
    JPanel panel = new JPanel(new BorderLayout());
    JPanel p1= new JPanel(new FlowLayout());
    JPanel p2= new JPanel(new FlowLayout());
    JPanel p3= new JPanel(new FlowLayout());
    JLabel destL, imageSrcL;
    JTextField destT, imageSrcT;
    JCheckBox coverImageCB;
    Boolean coverImage = false;
    JButton generate;

    //argumentem konstruktora jet obiekt Songbook - śpiewnik, który ma być wygenerowany
    public PDF_Swing(Songbook songbook, Color_palette theme){
        initialize(songbook, theme);
    }
    private void initialize(Songbook songbook, Color_palette theme){
        destL = new JLabel("Nazwa");
        p1.add(destL);
        destT = new JTextField("", 20);
        p1.add(destT);
        imageSrcL = new JLabel("Okładka");
        p2.add(imageSrcL);
        imageSrcT = new JTextField("", 20);
        p2.add(imageSrcT);
        coverImageCB = new JCheckBox("Dodawana okładka");
        p3.add(coverImageCB);
        generate = new JButton("Generuj");
        p3.add(generate);
        panel.add(p1, BorderLayout.NORTH);
        panel.add(p2, BorderLayout.CENTER);
        panel.add(p3, BorderLayout.SOUTH);

        //przycisk generuje plik PDF i zapisuje go pod adres "destT"
        //dodawanie okładki jest zależne od stanu chcekboxa "coverImageCB"
        generate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Songbook_PDF.GeneratePDF(songbook, destT.getText(), imageSrcT.getText(), coverImage);
                } catch (IOException ex) {
                    Songbook_Swing.errorMessage("Błąd generowania", theme);
                }
            }
        });

        //checkbox reaguje na zmianę stanu
        coverImageCB.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                coverImage = e.getStateChange() == ItemEvent.SELECTED;
            }
        });
    }
}
