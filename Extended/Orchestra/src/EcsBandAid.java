import music.Composition;
import music.MusicScore;
import people.Person;
import people.conductors.Conductor;
import people.musicians.*;
import utils.FileReader;
import utils.PORCHData;
import utils.SoundSystem;
import utils.Tables;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class EcsBandAid {

    private SoundSystem soundSystem;
    private HashMap<Integer, List<Musician>> musicians;
    private List<Composition> compositions;
    private Conductor conductor;

    private List<Musician> performers;
    private List<Composition> chosenCompositions;
    private int currentComposition = 0;
    private boolean abort = false;
    private int currentYear = -1;
    private int targetYear = -1;

    public static void main(String[] args) throws Exception {
        // Test command: java Main musicians.txt compositions.txt 3
        if (args.length == 3){
            // creates ecsBandAid
            SoundSystem soundSystem = new SoundSystem();
            EcsBandAid ecsBandAid = new EcsBandAid(soundSystem,
                    Arrays.stream(FileReader.loadMusicians(args[0], soundSystem)).iterator(),
                    FileReader.loadCompositions(args[1]).iterator());

            // starts a thread that will save the simulation after 5 seconds of wait
            Thread t1 = new Thread(new Runnable() {
                public void run()
                {
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    ecsBandAid.abortSimulation();
                    ecsBandAid.save();
                }});
            t1.start();

            // starts the simulation
            ecsBandAid.performForYears(Integer.parseInt(args[2]));

            // after 5 seconds of wait we will resume the simulation
            Thread.sleep(5000);
            // starts a thread that will save the simulation after 10 seconds of wait
            Thread t2 = new Thread(new Runnable() {
                public void run()
                {
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    ecsBandAid.abortSimulation();
                    ecsBandAid.save();
                }});
            t2.start();
            ecsBandAid.resume();

            // after 5 seconds of wait we will resume the simulation
            Thread.sleep(5000);
            ecsBandAid.resume();
        }
        else{
            throw new Exception("The main method expected 3 parameter, instated got " + args.length + "!");
        }
    }

    /**
     * Basic EcsBandAid constructor
     *
     * @param soundSystem Sounds system to be used by this band
     */
    public EcsBandAid(SoundSystem soundSystem){
        this.soundSystem = soundSystem;
        musicians = new HashMap<Integer, List<Musician>>();
        compositions = new ArrayList<Composition>();
        performers = new ArrayList<Musician>();
        chosenCompositions = new ArrayList<Composition>();
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
        performers = new ArrayList<Musician>();
        chosenCompositions = new ArrayList<Composition>();
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
        performers.clear();
        chosenCompositions.clear();
        currentComposition = 0;

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
        // saves chosen compositions in case we need to save midway through the year
        chosenCompositions.addAll(Arrays.asList(compositionsToPlay));

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
                    if (j == musicians.get(key).size() -1){
                        throw new Exception("There are not enough musicians in the pool to assemble a " +
                                "band for the chosen compositions!");
                    }
                }
            }
        }
        performers = conductor.getMusicians();

        // plays the three randomly selected compositions
        System.out.println("\nStarting to play.");
        System.out.println("Performed by:");
        for (Musician m : conductor.getMusicians()){
            System.out.println(((Person)m).getName() + " playing " + Tables.IDToName.get(m.getInstrumentID()));
        }
        System.out.println("");
        for (Composition composition : compositionsToPlay){
            if (abort){
                System.out.println("ABORTED FROM WITHIN YEAR SIMULATION!");
                return;
            }
            System.out.println("Now playing " + composition.getName());
            conductor.playComposition(composition);
            // small brake between composition
            Thread.sleep(500);
            currentComposition++;
        }
        // in case the simulation was abated on the last note, meaning it skipped the in loop check
        if (abort){
            System.out.println("ABORTED FROM WITHIN YEAR SIMULATION!");
            return;
        }

        System.out.println("Finished the year.\n");

        applyDropout(0.5f);

        System.out.println("END\n");
    }

    /**
     * Performs for a given number of years
     *
     * @param numOfYears perform for the given number of years
     */
    public void performForYears(int numOfYears) throws Exception {
        abort = false;
        currentYear = 0;
        targetYear = numOfYears;
        for (int i = 0; i < numOfYears; i++){
            if (hasAborted()){
                System.out.println("ABORTED FROM YEARS LOOP!");
                abort = false;
                return;
            }
            System.out.println("Year " + (i + 1));
            performForAYear();
            currentYear++;
        }
    }

    /**
     * Resumes the simulation based on th predated data.
     *
     * @param data the data to resume the simulation from
     */
    private void resumeYear(PORCHData data) throws Exception {
        currentComposition = data.currentComposition;

        System.out.println("START RESUME\n");

        // loads composition to play from the PORCHData object
        for (String compositionName : data.compositions){
            boolean found = false;
            for (Composition composition : compositions){
                if (composition.getName().equals(compositionName)){
                    found = true;
                    chosenCompositions.add(composition);
                    System.out.println("Loaded composition " + composition.getName());
                    break;
                }
            }
            if (!found){
                System.out.println("ERROR: Composition " + compositionName + " not found!");
            }
        }

        // loads selected musicians from the PORCHData object
        for (String bandMemberName : data.bandMembers){
            boolean found = false;
            for (Integer key: musicians.keySet()){
                for (Musician musician : musicians.get(key)){
                    if (((Instrumentalist)musician).getName().equals(bandMemberName)){
                        conductor.registerMusician(musician);
                        performers.add(musician);
                        found = true;
                        break;
                    }
                }
                if (found){
                    break;
                }
            }
            if (!found){
                System.out.println("ERROR: Musician " + bandMemberName + " not found!");
            }
        }

        // logs musician that are performing playing
        System.out.println("\nStarting to play.");
        System.out.println("Performed by:");
        for (Musician m : conductor.getMusicians()){
            System.out.println(((Person)m).getName() + " playing " + Tables.IDToName.get(m.getInstrumentID()));
        }

        // plays the composition
        System.out.println("");
        System.out.println("Now playing " + chosenCompositions.get(currentComposition).getName());
        conductor.resumeComposition(chosenCompositions.get(currentComposition), data);
        for (int i = data.currentComposition + 1; i < 3; i++){
            if (abort){
                System.out.println("ABORTED FROM WITHIN YEAR SIMULATION!");

                return;
            }
            System.out.println("Now playing " + chosenCompositions.get(i).getName());
            conductor.playComposition(chosenCompositions.get(i));
            // small brake between composition
            Thread.sleep(500);
            currentComposition++;
        }
        // in case the simulation was abated on the last note, meaning it skipped the in loop check
        if (abort){
            System.out.println("ABORTED FROM WITHIN YEAR SIMULATION!");
            return;
        }

        System.out.println("Finished the year.\n");

        applyDropout(0.5f);
        currentYear++;

        System.out.println("END\n");
    }

    /**
     * Saves the current state of the simulation to a file
     */
    public void save(){
        // we can assume that the composition.corch and musicians.morch will
        // stay the same so this data does not need to be saved again

        // saves performing musicians
        String data  = "TARGET_YEAR:" + targetYear + "\n";
        data += "CURRENT_YEAR:" + currentYear + "\n";
        data += "BAND_MEMBERS:";
        for (Musician musician : performers){
            data += ((Instrumentalist)musician).getName() + ",";
        }
        data = data.substring(0, data.length() - 1);

        // saves chosen compositions, and the current composition
        data += "\nCOMPOSITIONS:";
        for (Composition composition : chosenCompositions){
            data += composition.getName() + ",";
        }
        data = data.substring(0, data.length() - 1);
        data += "\n" + "CURRENT_COMPOSITION:" + currentComposition + "\n";

        // saves conductor data
        data += conductor.getSaveData();
        System.out.println(":::::::::::::::::::::::::::::::::::::::::::::::::::::::::::");
        System.out.println(data);
        System.out.println(":::::::::::::::::::::::::::::::::::::::::::::::::::::::::::");

        try {
            FileWriter fileWriter = new FileWriter("data/" + FileReader.DEFAULT_MIDEXECUTIONSAVE_FILENAME);
            fileWriter.write(data);
            fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Applies dropout, each musician has the given percentage change to lave the bad
     *
     * @param dropOutRate the chance for a musician to leave the band
     */
    private void applyDropout(float dropOutRate){
        // there is dropOutRate% for each musician to leave the band
        // NOTE conductor is not conductor a musician, so he will never leave
        List<Musician> leavingMembers = new ArrayList<Musician>();
        Random random = new Random();
        for (Musician musician : conductor.getMusicians()){
            if (random.nextDouble() > dropOutRate){
                leavingMembers.add(musician);
            }
        }
        for (Musician musician : leavingMembers){
            System.out.println(((Instrumentalist)musician).getName() + " has left the band!");
            conductor.removeMusician(musician);
        }
    }

    /**
     * Resumes the simulation from a saved state
     */
    public void resume() throws Exception {
        abort = false;
        reset();
        PORCHData porchData = FileReader.loadPORCHData(FileReader.DEFAULT_MIDEXECUTIONSAVE_FILENAME);
        currentYear = porchData.currentYear;
        targetYear = porchData.targetYear;

        // first iteration should be resumed
        resumeYear(porchData);
        // there other iteration can be simple simulated
        for (int i = currentYear; i < targetYear; i++){
            if (hasAborted()){
                System.out.println("ABORTED FROM YEARS LOOP!");
                abort = false;
                return;
            }
            System.out.println("Year " + (i + 1));
            performForAYear();
            currentYear++;
        }
    }

    /**
     * Aborts/stops the simulation midway through.
     */
    public void abortSimulation(){
        conductor.abortPlay();
        abort = true;
    }

    /**
     * Checks if the simulation has been aborted
     *
     * @return true if the simulation has been aborted
     */
    public boolean hasAborted(){
        return abort;
    }

    /**
     * Clears temporary lists and creates a new conductor,
     * equivalent to creating a new ecsBandAid object and loading the initial data again.
     */
    private void reset(){
        performers.clear();
        chosenCompositions.clear();
        conductor = new Conductor("Collin", this.soundSystem);
    }
}
