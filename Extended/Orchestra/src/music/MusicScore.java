package music;

import utils.Tables;

public class MusicScore {

    private String instrumentName;
    private int[] notes;
    private boolean soft;

    /**
     * Constructor for MusicScore
     *
     * @param instrumentName Name of the instrument to play given notes
     * @param notes Notes to be played by the given instrument
     * @param soft Decides loudness
     */
    public MusicScore(String instrumentName, int[] notes, boolean soft){
        this.instrumentName = instrumentName;
        this.notes = notes;
        this.soft = soft;
    }

    /**
     * Converts instrumentName to InstrumentID
     *
     * @return instrumentID
     */
    public int getInstrumentID() {
        // Assumes that the instrumentName is valid
        return Tables.nameToID.get(instrumentName);
    }

    /**
     * Getter for notes
     *
     * @return notes
     */
    public int[] getNotes() {return notes;}

    /**
     * Getter for soft
     *
     * @return soft
     */
    public boolean isSoft() {return soft;}
}
