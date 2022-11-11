package tests;

import music.MusicSheet;
import org.junit.jupiter.api.Test;
import people.conductors.Conductor;
import people.musicians.Cellist;
import people.musicians.Pianist;
import people.musicians.Violinist;
import utils.SoundSystem;

import java.util.Arrays;
import java.util.List;

public class CustomTests {

    @Test
    public void Part4Test() throws Exception {
        MusicSheet musicSheet = new MusicSheet("Test1", "Moderato", 3);

        List<String> notesA = Arrays.stream(new String[]{"G4", "F4", "G4", "F4", "G4", "F4", "G4", "F4", "G4",
                " none ", " none ", "G4", "F4", "G4", "F4", "G4", "G4"}).toList();
        List<String> notesB = Arrays.stream(new String[]{"A3", "B3", "D4", "B3", "F#4", " none ", "F#4", " none ",
                "E4", " none ", "A3", "B3", "D4", "B3", "E4", " none ", "E4", " none ", "D4", " none ", " none ",
                "A3", "B3", "D4", "B3", "D4", " none ", "E4", "C#4", " none ", "A3",
                " none ", "A3", "E4", " none ", "D4"}).toList();
        List<String> notesC = Arrays.stream(new String[]{"C4", "C4", "D4", " none ", "C4", " none ", "F4", " none ",
                "E4", " none ", " none ", " none ", "C4", "C4", "D4", " none ", "C4", " none ", "G4", " none ",
                "F4", " none ", " none ", " none ", "C4", "C4", "C5", " none ", "A4", " none ", "F4", " none ", "E4",
                " none ", "D4", " none ", "Bb4", "Bb4", "A4", " none ", "F4", " none ", "G4", " none ", "F4",
                " none ", " none ", " none "}).toList();

        musicSheet.addScore("Cello", notesA, true);
        musicSheet.addScore("Violin", notesB, false);
        musicSheet.addScore("Piano", notesC, true);

        SoundSystem soundSystem = new SoundSystem();
        Violinist tom = new Violinist("Tom", soundSystem, 0);
        Pianist jack = new Pianist("Jack", soundSystem, 0);
        Cellist emma = new Cellist("Emma", soundSystem, 0);
        Conductor conductor = new Conductor("Megan", soundSystem);
        conductor.registerMusician(tom);
        conductor.registerMusician(jack);
        conductor.registerMusician(emma);

        conductor.playComposition(musicSheet);
    }
}
