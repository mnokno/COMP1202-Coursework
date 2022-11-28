package utils;

import music.Composition;
import music.MusicSheet;
import people.musicians.Instrumentalist;
import people.musicians.Musician;

import javax.sound.midi.MidiUnavailableException;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public final class FileReader {

    public final static String DEFAULT_COMPOSITION_DIR  = "data/compositions.corch";
    public final static String DEFAULT_MUSICIANS_DIR= "data/musicians.morch";

    /**
     * Loads musicians from a given file to an array of Musicians.
     *
     * @param dir Directory from which to load the file, including the file name.
     * @param soundSystem Sound system that the musicians will use.
     * @return Array of musician that where saved in the given file.
     */
    public static Musician[] loadMusicians(String dir, SoundSystem soundSystem){
        try {
            BufferedReader bufferedReader = new BufferedReader(new java.io.FileReader(dir));
            try{
                List<Musician> musicians = new ArrayList<Musician>();
                String line;
                while ((line = bufferedReader.readLine()) != null){
                    String[] splitA = line.replaceAll(" ", "").split("\\(");
                    String[] splitB = splitA[1].split("\\)");
                    musicians.add(new Instrumentalist(splitA[0], soundSystem, Tables.nameToID.get(splitB[0])));
                }
                return musicians.toArray(new Musician[]{});
            } catch (Exception e){
                throw new RuntimeException("Invalid file format!");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Loads musicians from a given file to a List of Compositions.
     *
     * @param dir Directory from which to load the file, including the file name.
     * @return List of compositions that where saved in the given file.
     */
    public static List<Composition> loadCompositions(String dir){
        try {
            BufferedReader bufferedReader = new BufferedReader(new java.io.FileReader(dir));

            List<Composition> musicSheets = new ArrayList<Composition>();
            String line;
            while ((line = bufferedReader.readLine()) != null){
                String[] partsA = line.split(":");
                // it's a start of a composition
                // if any error are thrown then the file format was invalid
                try{
                    if (partsA[0].equals("Name ")){
                        String name = partsA[1];
                        if (name.toCharArray()[0] == ' '){
                            name = name.substring(1);
                        }
                        String tempo = bufferedReader.readLine().replaceAll(" ", "").split(":")[1];
                        String length = bufferedReader.readLine().replaceAll(" ", "").split(":")[1];
                        MusicSheet musicSheet = new MusicSheet(name, tempo, Integer.parseInt(length));
                        while((line = bufferedReader.readLine()) != null &&
                                !line.split(":")[0].equals("Name ") &&
                                !line.equals("")){
                            line = line.replaceAll( " ", "");
                            String[] partsB = line.split("\\{");
                            String[] partsC = partsB[0].split(",");
                            List<String> notes = List.of(partsB[1].split("}")[0].split(","));
                            musicSheet.addScore(partsC[0], notes, partsC[1].equals("soft"));
                        }
                        musicSheets.add(musicSheet);
                    }
                } catch (Exception e){
                    throw new RuntimeException("Invalid file format!");
                }
            }
            return musicSheets;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static PORCHData loadPORCHData(String dir){

        // reads the raw data from the file
        Map<String, String> data = new HashMap<String, String>();
        try {
            BufferedReader bufferedReader = new BufferedReader(new java.io.FileReader(dir));
            String line;
            while ((line = bufferedReader.readLine()) != null){
                String[] parts = line.split(":");
                data.put(parts[0], parts[1]);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // pareses the raw data
        PORCHData porchData = new PORCHData();

        //TARGET_YEAR
        porchData.targetYear = Integer.parseInt(data.get("TARGET_YEAR"));
        //CURRENT_YEAR
        porchData.currentYear = Integer.parseInt(data.get("CURRENT_YEAR"));
        //BAND_MEMBERS
        porchData.bandMembers = new ArrayList<String>(Arrays.asList(data.get("BAND_MEMBERS").split(",")));
        //COMPOSITIONS
        porchData.compositions = new ArrayList<String>(Arrays.asList(data.get("COMPOSITIONS").split(",")));
        //CURRENT_COMPOSITION
        porchData.currentComposition = Integer.parseInt(data.get("CURRENT_COMPOSITION"));
        //CURRENTLY_PERFORMING
        porchData.currentlyPerforming = new Hashtable<String, Integer>();
        //CURRENT_NOTE
        porchData.currentNote = Integer.parseInt(data.get("CURRENT_NOTE"));

        return porchData;
    }
}
