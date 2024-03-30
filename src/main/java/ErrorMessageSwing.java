import javax.swing.*;
//klasa tworzy panel z wiadomością błędu
public class ErrorMessageSwing {
    JPanel panel = new JPanel();
    JLabel err;
    public ErrorMessageSwing(String error, Color_palette theme){
        initialize(error, theme);
    }
    private void initialize(String error, Color_palette theme){
        err = new JLabel(error);
        err.setForeground(theme.CP.get(1)); //kolorki
        panel.add(err);
    }
}
