import java.io.Serializable;

//klasa implementuje obiekt piosenki, zawierającej tytuł, tekst i chwyty,
//implementujący interfejsy Comparable i Serializable
//oraz udostępniający metody manipulacji piosenką
public class Song implements Comparable<Song>, Serializable {
    String title;
    String text;
    String chords;
    public Song(String title, String text, String chords){
        this.chords = chords;
        this.text = text;
        this.title = title;
    }
    public Song(){
        this.text = "";
        this.chords = "";
        this.title = "";
    }

    //funkcja zamienia wystąpienia słowa argumentu "from" na słowo pod argumentem "to"
    public void WordSwap(String from, String to){
        this.text = this.text.replaceAll(from, to);
    }

    //funkcja porównuje piosenki leksykograficznie po tytułach
    public int compareTo(Song s) {
        return this.title.compareTo(s.title);
    }
}
