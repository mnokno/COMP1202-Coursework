import music.Composition;
import music.MusicScore;
import music.MusicSheet;
import org.junit.jupiter.api.Test;
import people.conductors.Conductor;
import people.musicians.*;
import utils.SoundSystem;

import javax.sound.midi.MidiUnavailableException;
import java.util.*;

public class EcsBandAid {

    private SoundSystem soundSystem;
    private List<Musician> musicians;
    private List<Composition> compositions;

    /**
     * Basic EcsBandAid constructor
     *
     * @param soundSystem Sounds system to be used by this band
     */
    public EcsBandAid(SoundSystem soundSystem){
        this.soundSystem = soundSystem;
        musicians = new ArrayList<Musician>();
        compositions = new ArrayList<Composition>();
    }

    /**
     * Basic EcsBandAid constructor with initial musicians and compositions.
     *
     * @param soundSystem           sounds system to be used by this band.
     * @param musicianIterator      initially pool of musicians.
     * @param compositionIterator   initially pool of compositions.
     */
    public EcsBandAid(SoundSystem soundSystem, Iterator<Musician> musicianIterator, Iterator<Composition> compositionIterator){
        this.soundSystem = soundSystem;
        musicians = new ArrayList<Musician>();
        compositions = new ArrayList<Composition>();

        // adds initial musicians if provided
        if (musicianIterator != null){
            while(musicianIterator.hasNext()){
                addMusician(musicianIterator.next());
            }
        }

        // adds initial compositions if provided
        if (compositionIterator != null){
            while (compositionIterator.hasNext()){
                addComposition(compositionIterator.next());
            }
        }
    }

    /**
     * Adds a new mission to the pool of exposition musicians, NOT the band
     *
     * @param musician Musician to be added
     */
    public void addMusician(Musician musician){
        musicians.add(musician);
    }


    /**
     * Adds a new composition to a pool of playable composition
     *
     * @param composition Composition to be added
     */
    public void addComposition(Composition composition){
        compositions.add(composition);
    }


    /**
     * Performs for a year playing 3 random composition from the compositions pool,
     * using musicians form the musicians pool, by the end of the performance (all 3 composition)
     * each musician has 50% changes to leave the bad.
     */
    public void performForAYear() throws Exception {
        System.out.println("START");

        // ensures that there are enough compositions
        System.out.println("Validating compositions pool.");
        if (compositions.size() < 1){
            throw new Exception("Cant perform for a year, there are no composition in the composition pool");
        }

        // creates a conductor
        System.out.println("Recruiting a conductor to guide the band.");
        Conductor conductor = new Conductor("Candace", soundSystem);
        // chooses compositions to play
        System.out.println("Choosing three random compositions from the pool compositions.");
        Random random = new Random();
        Composition[] compositionsToPlay = new Composition[] {
            compositions.get((int)(random.nextDouble() * compositions.size())),
            compositions.get((int)(random.nextDouble() * compositions.size())),
            compositions.get((int)(random.nextDouble() * compositions.size()))
        };
        // finds out how many musicians we need to play all 3 compositions
        System.out.println("Checking how many musicians are required to play selected compositions.");
        Dictionary<String, Integer> requiredMusiciansDictionary = new Hashtable<String, Integer>();
        int requiredMusicians = 0;
        for (Composition composition: compositionsToPlay){
            if (composition.getScores().length > requiredMusicians){
                for (MusicScore musicScore: composition.getScores()){
                    // TODO
                }
                requiredMusicians = composition.getScores().length;
            }
        }

        // ensures that there are enough compositions
        System.out.println("Validating musicians pool.");
        if (musicians.size() < requiredMusicians){
            throw new Exception("There are not enough musicians in the pool to assemble a band for the chosen compositions!");
        }

        // assembles the band
        System.out.println("Recruiting a musicians to play in the band:");
        Collections.shuffle(musicians);
        for (int i = 0; i < requiredMusicians; i++){
            // I assume that all musicians in the pool are Instrumentalist
            System.out.println(((Instrumentalist)musicians.get(i)).getName() + " has joined the band!");
            conductor.registerMusician(musicians.get(i));
        }
        // plays the three randomly selected compositions
        System.out.println("Starting to play.");
        for (Composition composition : compositionsToPlay){
            System.out.println("Now playing " + composition.getName());
            conductor.playComposition(composition);
            // small brake between composition
            Thread.sleep(500);
        }
        System.out.println("Finished to play.");

        // there is 50% for each musician to leave the band
        // NOTE conductor is not conductor a musician, so he will never leave
        List<Musician> leavingMembers = new ArrayList<Musician>();
        for (Musician musician : conductor.getMusicians()){
            if (random.nextDouble() > 0.5){
                leavingMembers.add(musician);
            }
        }
        for (Musician musician : leavingMembers){
            System.out.println(((Instrumentalist)musician).getName() + " has left the band!");
            conductor.removeMusician(musician);
        }

        System.out.println("END");
    }
}
