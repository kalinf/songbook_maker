import java.io.Serializable;
import java.util.Collections;
import java.util.Objects;
import java.util.Vector;

//klasa implementuje śpiewnik - kolekcję piosenek wraz z metodami manipulującymi śpiewnikiem
//posiada osobne pole titles, zawierające tytuły piosenek w śpiewniku
public class Songbook implements Serializable {
    String title;
    Vector<Song> songs;
    Vector<String> titles;
    public Songbook(String title){
        this.title = title;
        songs = new Vector<Song>();
        titles = new Vector<String>();
    }
    public Songbook(){
        this.title = "";
        songs = new Vector<Song>();
        titles = new Vector<String>();
    }

    //funkcja dodaje piosenkę do vectora "songs"
    public void AddSong(Song song){
        songs.add(song);
    }

    //funkcja usuwa piosenkę ze śpiewnika
    public void RemoveSong(String title){
        for(Song s : this.songs){
            if(Objects.equals(s.title, title)){
                this.songs.remove(s);
                break;
            }
        }
        titles.remove(title);
    }

    //fukcja sortująca piosenki w śpiewniku
    public void SongSort(){
        Collections.sort(songs);
        Collections.sort(titles);
    }

    //funkcja szukająca w śpiewniku piosenki po tytule
    public Song findSong(String title) throws NotFoundException{
        for(Song s : songs){
            if(Objects.equals(s.title, title))
                return s;
        }
        throw new NotFoundException();
    }
}
