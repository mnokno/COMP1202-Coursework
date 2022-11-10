import people.musicians.Cellist;
import people.musicians.Pianist;
import people.musicians.Violinist;
import utils.SoundSystem;

import javax.sound.midi.MidiUnavailableException;

public class Main {
    public static void main(String[] args) {

        try
        {
            SoundSystem soundSystem = new SoundSystem();
            int[] notes = new int[]{67,65,67,65,67,65,67,65,67,0,0,67,65,67,65,67,67};

            Violinist tom = new Violinist("Tom", soundSystem, 0);
            Pianist jack = new Pianist("Jack", soundSystem, 0);
            Cellist emma = new Cellist("Emma", soundSystem, 0);

            tom.readScore(notes, true);
            jack.readScore(notes, true);
            emma.readScore(notes, true);

            System.out.println("playing");
            for (int i = 0; i < notes.length; i++){
                tom.playNextNote();
                Thread.sleep(500);
                jack.playNextNote();
                Thread.sleep(500);
                emma.playNextNote();
                Thread.sleep(500);
            }
            System.out.println("finished");
        }
        catch (InterruptedException | MidiUnavailableException e)
        {
            System.out.println(e.toString());
        }

    }
}