package music;

import utils.Tables;

import java.util.List;

public class MusicSheet implements Composition {

    private String name;
    private String tempo;
    private int noteLength;
    private int length;
    private MusicScore[] musicScores;
    private int musicScoresCurrentIndex;

    /**
     * Basic contractor for MusicSheet
     *
     * @param name mame of this composition.
     * @param tempo tempo of this composition.
     * @param length number of notes in the composition.
     */
    public MusicSheet(String name, String tempo, int length){
        this.name = name;
        this.tempo = tempo;
        noteLength = Tables.tempoToLength.get(tempo);
        this.length = length;
        musicScores = new MusicScore[length];
        musicScoresCurrentIndex = 0;
    }

    /**
     * Getter for name.
     *
     * @return name of the composition.
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Add a score to the composition.The score is represented by the name of the instrument instrumentName,
     * the String representation of the notes in the music score (notes), and whether or not
     * the music should be played softly (soft). We will look at the conversion of the String
     * representation to the MIDI equivalence in the next part.
     *
     * @param instrumentName name for the score.
     * @param notes          notes for the score.
     * @param soft           loudness of the score.
     */
    @Override
    public void addScore(String instrumentName, List<String> notes, boolean soft) {
        if (musicScoresCurrentIndex < length){
            // converts notes from human text to MIDI number
            int[] intNotes = new int[notes.size()];
            for (int i = 0; i < notes.size(); i++){
                intNotes[i] = Tables.noteNameToMIDI.get(notes.get(i));
            }
            // adds this score
            this.musicScores[musicScoresCurrentIndex] = new MusicScore(instrumentName, intNotes, soft);
            // updates score count
            musicScoresCurrentIndex++;
        }
    }

    /**
     * Getter for scores.
     *
     * @return scores.
     */
    @Override
    public MusicScore[] getScores() {
        return musicScores;
    }

    /**
     * Gets the length of the composition (i.e., how many notes are to be played).
     *
     * @return the length of the composition.
     */
    @Override
    public int getLength() {
        int max = 0;
        for (MusicScore musicScore : musicScores){
            if (max < musicScore.getNotes().length){
                max = musicScore.getNotes().length;
            }
        }
        return max;
    }

    /**
     * Gets the length (in ms) for a note. This dictates the tempo of the
     * composition.
     *
     * @return time required to play all the notes.
     */
    @Override
    public int getNoteLength() {
        return noteLength;
    }
}
