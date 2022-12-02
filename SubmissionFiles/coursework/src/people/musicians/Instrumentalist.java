package people.musicians;

import people.Person;
import utils.SoundSystem;
import utils.Tables;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Instrumentalist extends Person implements Musician {

    private int instrumentID;
    private List<Integer> notes;
    private Iterator<Integer> nextNote;
    private SoundSystem soundSystem;
    private int seat;
    private int loudness;

    /**
     * Constructor Instrumentalist Person class.
     *
     * @param name          name of this person.
     * @param soundSystem   sound system to be used for this Instrumentalist
     */
    public Instrumentalist(String name, SoundSystem soundSystem, int instrumentID) {
        super(name);
        this.soundSystem = soundSystem;
        this.instrumentID = instrumentID;
    }

    /**
     * Constructor Instrumentalist Person class.
     * Automatically sets seat on initialization.
     *
     * @param name         name of this person.
     * @param soundSystem  sound system to be used for this Instrumentalist
     * @param seat         where this Instrumentalist should seat
     */
    public Instrumentalist(String name, SoundSystem soundSystem, int instrumentID, int seat) {
        super(name);
        this.soundSystem = soundSystem;
        this.instrumentID = instrumentID;
        setSeat(seat);
    }

    /**
     * Sets a set for this musician,
     * automatically class SoundSystem.setInstrument with the given seat.
     *
     * @param seat Seat for this musician, assumed to be between 0 and 15.
     */
    @Override
    public void setSeat(int seat) {
        // clams the value between 0 and 15
        this.seat = Math.max(0, Math.min(seat, 15));
        // updates sound system
        soundSystem.setInstrument(seat, instrumentID);
    }

    /**
     * Getter for seat.
     *
     * @return return this musicians seat.
     */
    @Override
    public int getSeat() {
        return seat;
    }

    /**
     * Reads musicians score represented in the notes array.
     *
     * @param notes Score/notes to be read.
     * @param soft  Set to true for soft notes and false for hard.
     */
    @Override
    public void readScore(int[] notes, boolean soft) {
        // loads notes
        this.notes = new ArrayList<Integer>();
        for (int note: notes){
            this.notes.add(note);
        }
        // loads loudness
        loudness = Tables.idToLoudness.get(instrumentID)[soft ? 0 : 1];
        // initiates iterator
        nextNote = this.notes.iterator();
    }


    /**
     * Plays the next node, there will be no effect if there are no more notes to play.
     * NOTE: setSeat and readScore have to be called before playNextNote can be used.
     */
    @Override
    public void playNextNote() {
        if (nextNote.hasNext()){
            soundSystem.playNote(seat, nextNote.next(), loudness);
        }
    }

    /**
     * Getter for InstrumentID
     *
     * @return instrumentID
     */
    @Override
    public int getInstrumentID() {
        return instrumentID;
    }
}
