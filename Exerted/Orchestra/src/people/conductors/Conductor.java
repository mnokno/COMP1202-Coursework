package people.conductors;

import music.Composition;
import music.MusicScore;
import orchestra.Orchestra;
import people.Person;
import people.musicians.Musician;
import utils.SoundSystem;

import java.util.ArrayList;
import java.util.List;

public class Conductor extends Person {

    private SoundSystem soundSystem;
    private ArrayList<Musician> musicians;
    private Orchestra orchestra;
    private int currentNote = -1;
    private boolean abort = false;

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
        // resets current note
        currentNote = 0;
        // gets scores
        MusicScore[] musicScores = composition.getScores();

        // creates empty orchestra
        orchestra = new Orchestra();

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
            if (abort){
                System.out.println("ABORTED IN CONDUCTOR!");
                soundSystem.setSilentMode(true);
                abort = false;
                return;
            }
            try{
                orchestra.playNextNote();
                Thread.sleep(composition.getNoteLength());
                currentNote++;
            }
            catch (InterruptedException e){
                System.out.println(e.toString());
            }
        }
        currentNote = -1;
        soundSystem.setSilentMode(true);
    }


    /**
     * Checks if the given musician is used conductors control.
     *
     * @param m Musician to be checked.
     * @return returns true if the musical is already under conductors control.
     */
    public boolean hasMusician(Musician m){
        return musicians.contains(m);
    }

    public String getSaveData(){
        if (orchestra == null){
            return "NOT_PLAYING";
        }
        else{
            String data = "CURRENTLY_PERFORMING:";
            for (Musician musician: orchestra.getMusicians()){
                data += ((Person)musician).getName() + ":" + musician.getSeat() + ",";
            }
            data = data.substring(0, data.length() - 1);
            data += "\n";
            data += "CURRENT_NOTE:" + currentNote;

            return data;
        }
    }

    public void abortPlay(){
        abort = true;
    }
}
