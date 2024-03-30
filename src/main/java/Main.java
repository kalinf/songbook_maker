import javax.swing.*;
import java.awt.*;

public class Main {
    //metoda uruchamia okno interfejsu startowego
    public static void main(String args[]){
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        Color_themes themes = new Color_themes();
        //to jest prowizorka
        int t = 4;
        //bo chciałabym zapisywać wybrany motyw w jakimś pliku, żeby był pamiętany przy kolejnym uruchomieniu
        //i żeby można było zmieniać go z innych interfejsów (być może)
        Color_palette theme = themes.getTheme(t);
        JFrame frame = new JFrame("EDYTOR ŚPIEWNIKÓW");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        StartInterface_Swing editor = new StartInterface_Swing(theme);
        frame.getContentPane().add(editor.panel);
        frame.pack();
        frame.setVisible(true);
    }
}
