package music;

import java.util.List;

public interface Composition {

    /**
     * Getter for name.
     *
     * @return name of the composition.
     */
    public String getName();


    /**
     * Add a score to the composition.The score is represented by the name of the instrument instrumentName,
     * the String representation of the notes in the music score (notes), and whether or not
     * the music should be played softly (soft). We will look at the conversion of the String
     * representation to the MIDI equivalence in the next part.
     *
     * @param instrumentName name for the score.
     * @param notes notes for the score.
     * @param soft loudness of the score.
     */
    public void addScore(String instrumentName, List<String> notes, boolean soft);

    /**
     * Getter for scores
     *
     * @return scores
     */
    public MusicScore[] getScores();


    /**
     * Gets the length of the composition (i.e., how many notes are to be played).
     *
     * @return the length of the composition.
     */
    public int getLength();

    /**
     * Gets the length (in ms) for a note. This dictates the tempo of the
     * composition.
     *
     * @return time required to play all the notes.
     */
    public int getNoteLength();
}
