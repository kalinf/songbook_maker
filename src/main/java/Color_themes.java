import java.util.Arrays;
import java.util.Vector;

public class Color_themes {
    Vector<Color_palette> themes = new Vector<Color_palette>();
    int n = 3; //number of colors in a palette
    //              #tło      #czcionka #czcionka
    //                                  #guziki
    String[] tab = {"C0BCB5", "592941", "AF5D63",
                    "A2D3C2", "230C0F", "846075",
                    "D8B4E2", "210B2C", "55286F",
                    "FFFD82", "2D232E", "584537",
                    "100B00", "EFFFC8", "4A721D"};
    public int NumberOfThemes = tab.length/n;
    public Color_themes(){
        for(int t = 0; t < NumberOfThemes; t++){
            this.themes.add(new Color_palette(Arrays.copyOfRange(tab, n*t, n * (t+1))));
        }
    }
    public Color_palette getTheme(int t){
        return themes.get(t%NumberOfThemes);
    }
}
