package people.musicians;

public interface Musician {

    /**
     * Setter for seat.
     *
     * @param seat Seat for this musician, assumed to be between 0 and 15.
     */
    abstract void setSeat(int seat);

    /**
     * Getter for seat.
     *
     * @return return this musicians seat.
     */
    abstract int getSeat();

    /**
     * Reads musicians score represented in the notes array.
     *
     * @param notes Score/notes to be read.
     * @param soft Set to true for soft notes and false for hard.
     */
    abstract void readScore(int[] notes, boolean soft);

    /**
     * Plays the next node, there will be no effect if there are no more notes to play.
     * NOTE: setSeat and readScore have to be called before playNextNote can be used.
     */
    abstract void playNextNote();

    /**
     * Getter for InstrumentID
     *
     * @return instrumentID
     */
    abstract int getInstrumentID();

    /**
     * Getter for the number of played notes
     *
     * @return playedNotes
     */
    abstract int getPlayedNotes();

    /**
     * Skips the given umber of notes
     *
     * @param numberOfNotesToSkip number of notes to skip
     */
    abstract void skipNotes(int numberOfNotesToSkip);
}
