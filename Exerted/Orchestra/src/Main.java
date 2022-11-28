import music.Composition;
import music.MusicScore;
import music.MusicSheet;
import org.junit.jupiter.api.Test;
import people.conductors.Conductor;
import people.musicians.Cellist;
import people.musicians.Pianist;
import people.musicians.Violinist;
import utils.FileReader;
import utils.SoundSystem;

import javax.sound.midi.MidiUnavailableException;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        // Test command: java EcsBandAid data/musicians.morch data/compositions.corch 10
        if (args.length == 3){
            // creates ecsBandAid
            SoundSystem soundSystem = new SoundSystem();
            EcsBandAid ecsBandAid = new EcsBandAid(soundSystem,
                    Arrays.stream(FileReader.loadMusicians(args[0], soundSystem)).iterator(),
                    FileReader.loadCompositions(args[1]).iterator());

            // starts a thread that will save the simulation after 5 seconds of wait
            Thread t1 = new Thread(new Runnable() {
                public void run()
                {
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    ecsBandAid.abortSimulation();
                    ecsBandAid.save();
                }});
            t1.start();

            // starts the simulation
            ecsBandAid.performForYears(Integer.parseInt(args[2]));
        }
        else{
            throw new Exception("The main method expected 3 parameter, instated got " + args.length + "!");
        }
    }

    @Test
    public void Part1Test() throws InterruptedException, MidiUnavailableException {
        SoundSystem soundSystem = new SoundSystem();
        int[] notes = new int[]{67,65,67,65,67,65,67,65,67,0,0,67,65,67,65,67,67};

        Violinist tom = new Violinist("Tom", soundSystem, 0);
        Pianist jack = new Pianist("Jack", soundSystem, 1);
        Cellist emma = new Cellist("Emma", soundSystem, 2);

        tom.readScore(notes, true);
        jack.readScore(notes, true);
        emma.readScore(notes, true);

        System.out.println("playing");
        for (int i = 0; i < notes.length; i++){
            tom.playNextNote();
            Thread.sleep(250);
            //jack.playNextNote();
            //Thread.sleep(500);
            //emma.playNextNote();
            //Thread.sleep(500);
        }
        System.out.println("finished");
    }

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
        Pianist jack = new Pianist("Jack", soundSystem, 1);
        Cellist emma = new Cellist("Emma", soundSystem, 2);
        Conductor conductor = new Conductor("Megan", soundSystem);
        conductor.registerMusician(tom);
        conductor.registerMusician(jack);
        conductor.registerMusician(emma);

        conductor.playComposition(musicSheet);
    }

    @Test
    public void Part5Test() throws Exception {
        SoundSystem soundSystem = new SoundSystem();
        EcsBandAid ecsBandAid = new EcsBandAid(soundSystem);

        MusicSheet musicSheetA = new MusicSheet("Ru d aah!", "Moderato", 3);
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
        musicSheetA.addScore("Cello", notesA, true);
        musicSheetA.addScore("Violin", notesB, false);
        musicSheetA.addScore("Piano", notesC, true);

        MusicSheet musicSheetB = new MusicSheet("El'd ra", "Moderato", 3);
        notesA = Arrays.stream(new String[]{"F4", "G4", "G4"}).toList();
        notesB = Arrays.stream(new String[]{"A3", "B3", "D4", "E4", "C#4", " none ", "A3",
                " none ", "A3", "E4", " none ", "D4"}).toList();
        notesC = Arrays.stream(new String[]{"C4", "C4", "D4", " none ", "C4", " none ", "F4", " none ",
                " none ", "D4", " none ", "Bb4", "Bb4", "A4", " none ", "F4", " none ", "G4", " none ", "F4",
                " none ", " none ", " none "}).toList();
        musicSheetB.addScore("Cello", notesA, true);
        musicSheetB.addScore("Violin", notesB, false);
        musicSheetB.addScore("Piano", notesC, true);

        Violinist tom = new Violinist("Tom", soundSystem);
        Violinist ellis = new Violinist("Ellis", soundSystem);
        Violinist emily = new Violinist("Emily", soundSystem);
        Pianist jack = new Pianist("Jack", soundSystem);
        Pianist jazz = new Pianist("Jazz", soundSystem);
        Pianist alfie = new Pianist("Alfie", soundSystem);
        Cellist emma = new Cellist("Emma", soundSystem);
        Cellist janet = new Cellist("Janet", soundSystem);
        Cellist rubin = new Cellist("Rubin", soundSystem);

        ecsBandAid.addComposition(musicSheetA);
        ecsBandAid.addComposition(musicSheetB);
        ecsBandAid.addMusician(tom);
        ecsBandAid.addMusician(ellis);
        ecsBandAid.addMusician(emily);
        ecsBandAid.addMusician(janet);
        ecsBandAid.addMusician(jack);
        ecsBandAid.addMusician(jazz);
        ecsBandAid.addMusician(alfie);
        ecsBandAid.addMusician(emma);
        ecsBandAid.addMusician(rubin);

        ecsBandAid.performForAYear();
    }

    @Test
    public void Part6Test() throws Exception {
        SoundSystem soundSystem = new SoundSystem();
        EcsBandAid ecsBandAid = new EcsBandAid(soundSystem,
                Arrays.stream(FileReader.loadMusicians(FileReader.DEFAULT_MUSICIANS_DIR, soundSystem)).iterator(),
                FileReader.loadCompositions(FileReader.DEFAULT_COMPOSITION_DIR).iterator());
        ecsBandAid.performForAYear();
    }

    @Test
    public void ECS10YearsTest() throws Exception {
        SoundSystem soundSystem = new SoundSystem();
        EcsBandAid ecsBandAid = new EcsBandAid(soundSystem,
                Arrays.stream(FileReader.loadMusicians(FileReader.DEFAULT_MUSICIANS_DIR, soundSystem)).iterator(),
                FileReader.loadCompositions(FileReader.DEFAULT_COMPOSITION_DIR).iterator());
        ecsBandAid.performForYears(10);
    }
}