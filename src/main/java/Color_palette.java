import java.awt.*;
import java.util.Vector;

public class Color_palette {
    public Vector<Color> CP = new Vector<>();
    public Color_palette(String [] colors){
        for(int i=0; i < colors.length; i++){
            this.CP.add(new Color(Integer.parseInt(colors[i],16)));
        }
    }
}
