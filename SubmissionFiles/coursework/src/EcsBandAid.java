import music.Composition;
import music.MusicScore;
import music.MusicSheet;
import org.junit.jupiter.api.Test;
import people.Person;
import people.conductors.Conductor;
import people.musicians.*;
import utils.SoundSystem;
import utils.Tables;

import javax.sound.midi.MidiUnavailableException;
import java.util.*;

public class EcsBandAid {

    private SoundSystem soundSystem;
    private HashMap<Integer, List<Musician>> musicians;
    private List<Composition> compositions;
    private Conductor conductor;

    /**
     * Basic EcsBandAid constructor
     *
     * @param soundSystem Sounds system to be used by this band
     */
    public EcsBandAid(SoundSystem soundSystem){
        this.soundSystem = soundSystem;
        musicians = new HashMap<Integer, List<Musician>>();
        compositions = new ArrayList<Composition>();
        conductor = new Conductor("Collin", this.soundSystem);
    }

    /**
     * Basic EcsBandAid constructor with initial musicians and compositions.
     *
     * @param soundSystem           sounds system to be used by this band.
     * @param musicianIterator      initially pool of musicians.
     * @param compositionIterator   initially pool of compositions.
     */
    public EcsBandAid(SoundSystem soundSystem,
                      Iterator<Musician> musicianIterator, Iterator<Composition> compositionIterator){
        this.soundSystem = soundSystem;
        musicians = new HashMap<Integer, List<Musician>>();
        compositions = new ArrayList<Composition>();
        conductor = new Conductor("Collin", this.soundSystem);

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
        if (musicians.containsKey(musician.getInstrumentID())){
            musicians.get(musician.getInstrumentID()).add(musician);
        }
        else{
            musicians.put(musician.getInstrumentID(), new ArrayList<Musician>() {});
            musicians.get(musician.getInstrumentID()).add(musician);
        }
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
        System.out.println("START\n");

        // ensures that there are enough compositions
        System.out.println("Validating compositions pool.");
        if (compositions.size() < 1){
            throw new Exception("Cant perform for a year, there are no composition in the composition pool");
        }

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
        HashMap<Integer, Integer> requiredMusicians = new HashMap<Integer, Integer>();

        for (Composition composition: compositionsToPlay){

            HashMap<Integer, Integer> tmp = new HashMap<Integer, Integer>();
            for (MusicScore musicScore: composition.getScores()){
                if (tmp.containsKey(musicScore.getInstrumentID())){
                    tmp.replace(musicScore.getInstrumentID(), tmp.get(musicScore.getInstrumentID()) + 1);
                }
                else{
                    tmp.put(musicScore.getInstrumentID(), 1);
                }
            }

            for (Integer key: tmp.keySet()) {
                if (requiredMusicians.containsKey(key)){
                    if (requiredMusicians.get(key) < tmp.get(key)){
                        requiredMusicians.replace(key, tmp.get(key));
                    }
                }
                else{
                    requiredMusicians.put(key, tmp.get(key));
                }
            }
        }
        // offsets the found musician requirement by musician already in the band,this mean that musician
        // that carry over to the next year will play and no replacement will be provided for them.
        for (Musician m : conductor.getMusicians()){
            if (requiredMusicians.containsKey(m.getInstrumentID())){
                requiredMusicians.replace(m.getInstrumentID(), requiredMusicians.get(m.getInstrumentID()) - 1);
            }
        }

        // ensures that there are enough musicians
        System.out.println("Validating musicians pool.");
        for (Integer key : requiredMusicians.keySet()){
            if (musicians.containsKey(key)){
                if (musicians.get(key).size() < requiredMusicians.get(key)){
                    throw new Exception("There are not enough musicians in the pool to assemble a band for " +
                            "the chosen compositions!");
                }
            }
            else{
                throw new Exception("There are not enough musicians in the pool to assemble a " +
                        "band for the chosen compositions!");
            }
        }

        // assembles the band
        System.out.println("\nRecruiting a musicians to play in the band:");
        for (Integer key: requiredMusicians.keySet()){
            Collections.shuffle(musicians.get(key));
            for (int i = 0; i < requiredMusicians.get(key); i++){
                for (int j = 0; j < musicians.get(key).size(); j++){
                    // ensures that the same musician does not join the band twice before leaving when simulating multiple years
                    if (!conductor.hasMusician(musicians.get(key).get(j))){
                        // I assume that all musicians in the pool are Instrumentalist
                        System.out.println(((Instrumentalist)musicians.get(key).get(j)).getName() + " has joined the band!");
                        conductor.registerMusician(musicians.get(key).get(j));
                        break;
                    }
                    if (j == musicians.get(key).size() - 1){
                        throw new Exception("There are not enough musicians in the pool to assemble a " +
                                "band for the chosen compositions!");
                    }
                }
            }
        }

        // plays the three randomly selected compositions
        System.out.println("\nStarting to play.");
        System.out.println("Performed by:");
        for (Musician m : conductor.getMusicians()){
            System.out.println(((Person)m).getName() + " playing " + Tables.IDToName.get(m.getInstrumentID()));
        }
        System.out.println("");
        for (Composition composition : compositionsToPlay){
            System.out.println("Now playing " + composition.getName());
            conductor.playComposition(composition);
            // small brake between composition
            Thread.sleep(500);
        }
        System.out.println("Finished the year.\n");

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

        System.out.println("END\n");
    }

    /**
     * Performs for a given number of years
     *
     * @param numOfYears perform for the given number of years
     */
    public void performForYears(int numOfYears) throws Exception {
        for (int i = 0; i < numOfYears; i++){
            System.out.println("Year " + (i + 1));
            performForAYear();
        }
    }
}
