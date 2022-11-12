package people.conductors;

import music.Composition;
import music.MusicScore;
import orchestra.Orchestra;
import people.Person;
import people.musicians.Musician;
import utils.SoundSystem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Conductor extends Person {

    private SoundSystem soundSystem;
    private ArrayList<Musician> musicians;

    /**
     * Constructor for Conductor class.
     *
     * @param name name of this conductor.
     * @param soundSystem SoundSystem to be used by this conductor.
     */
    public Conductor(String name, SoundSystem soundSystem) {
        super(name);
        this.soundSystem = soundSystem;
        musicians = new ArrayList<>();
    }

    /**
     * Adds musician to control of this conductor.
     *
     * @param musician musician to be added to the orchestra staff.
     */
    public void registerMusician(Musician musician){
        musicians.add(musician);
    }


    /**
     * Removes a musician from conductors control.
     *
     * @param musician musician to be removed.
     */
    public void removeMusician(Musician musician){
        musicians.remove(musician);
    }

    /**
     * Getter for registered musicians.
     *
     * @return registered musicians.
     */
    public List<Musician> getMusicians(){
        return musicians;
    }

    /**
     * Plays the given composition.
     *
     * @param composition composition to be played.
     */
    public void playComposition(Composition composition) throws Exception {
        // gets scores
        MusicScore[] musicScores = composition.getScores();

        // creates empty orchestra
        Orchestra orchestra = new Orchestra();

        // assigns musicians to the scrolls
        boolean insufficientHumanResources = false;
        for (MusicScore musicScore: musicScores) {
            for (Musician musician: musicians){
                if (musician.getInstrumentID() == musicScore.getInstrumentID()){
                    if (!orchestra.isSeated(musician)){
                        musician.readScore(musicScore.getNotes(), musicScore.isSoft());
                        orchestra.sitDown(musician);
                        insufficientHumanResources = false;
                        break;
                    }
                }
                insufficientHumanResources = true;
            }
        }
        if (insufficientHumanResources){
            throw new Exception("Conductor does not have enough musicians to plat this composition!");
        }

        // plays the composition
        soundSystem.setSilentMode(false);
        for (int i = 0; i < composition.getLength(); i++){
            try{
                orchestra.playNextNote();
                Thread.sleep(composition.getNoteLength());
            }
            catch (InterruptedException e){
                System.out.println(e.toString());
            }
        }
        soundSystem.setSilentMode(true);
    }
}
