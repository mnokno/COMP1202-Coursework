package people.conductors;

import music.Composition;
import music.MusicScore;
import orchestra.Orchestra;
import people.Person;
import people.musicians.Musician;
import utils.PORCHData;
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
     * Plays out the composition.
     *
     * @param startNote skips the first n notes, starting to play form startNote.
     * @param composition composition to be played.
     */
    private void playNotes(int startNote, Composition composition){
        // skips notes that were played before saved
        for (int i = 0; i < startNote; i++){
            orchestra.playNextNote();
            currentNote++;
        }

        // plays the composition
        for (int i = startNote; i < composition.getLength(); i++){
            if (abort){
                System.out.println("ABORTED IN CONDUCTOR!");
                soundSystem.stopPlaying();
                soundSystem.setSilentMode(true);
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
    }

    /**
     * Plays out the composition.
     *
     * @param composition composition to be played.
     */
    private void playNotes(Composition composition){
        playNotes(0, composition);
    }

    /**
     * Assembles the orchestra to play the given composition.
     *
     * @param composition composition to be played by teh orchestra.
     */
    private void assembleOrchestra(Composition composition) throws Exception {
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
    }

    /**
     * Plays the given composition.
     *
     * @param composition composition to be played.
     */
    public void playComposition(Composition composition) throws Exception {
        // assembles orchestra
        assembleOrchestra(composition);
        // plays the composition
        soundSystem.setSilentMode(false);
        playNotes(composition);
        if (abort){
            abort = false;
            return;
        }
        currentNote = -1;
        soundSystem.stopPlaying();
        soundSystem.setSilentMode(true);
    }

    /**
     * Resume play of a composition based on the provided data.
     *
     * @param composition Composition to be resumed.
     * @param data Data that specifies how the composition should be resumed.
     */
    public void resumeComposition(Composition composition, PORCHData data) throws Exception {
        // assembles orchestra
        assembleOrchestra(composition);
        // plays the composition
        soundSystem.setSilentMode(false);
        playNotes(data.currentNote, composition);
        if (abort){
            abort = false;
            return;
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

    /**
     * Generates a string of data that cna be used to restore the state of the current composition.
     *
     * @return data that can be used to restore the state of the current composition.
     */
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

    /**
     * Aborts play of the current composition
     */
    public void abortPlay(){
        abort = true;
    }
}
