package orchestra;

import people.musicians.Musician;

import java.util.HashMap;

public class Orchestra {

    private HashMap<Integer, Musician> seating;

    /**
     * Orchestra constructor, create a new empty Orchestra.
     */
    public Orchestra(){
        seating = new HashMap<Integer, Musician>(16);
    }

    /**
     * Attempts to sit down a musician
     *
     * @param musician musician to be seated.
     * @return
     * - If the input musician is already in the orchestra, return 2.<br/>
     * – If there are no free seats in the orchestra, return 1.<br/>
     * – Otherwise, return 0 in this case.
     */
    public int sitDown(Musician musician){
        if (seating.containsValue(musician)){
            return 2;
        }
        else if (seating.size() == 16){
            return 1;
        }
        else{
            // checks if the seat is taken before setting down the musician
            if (seating.containsKey(musician.getSeat())){
                // finds an empty seat
                for (int i = 0; i < 16; i++){
                    if (!seating.containsKey(i)){
                        musician.setSeat(i);
                        seating.put(i, musician);
                        break;
                    }
                }
            }
            else{
                // the seat was empty
                seating.put(musician.getSeat(), musician);
            }
            return 0;
        }
    }

    /**
     * Checks if the given musician is currency seated.
     *
     * @param musician Musician to be checked.
     * @return true if the musician is seated, false otherwise.
     */
    public boolean isSeated(Musician musician){
        return seating.containsValue(musician);
    }


    /**
     * Makes a musician stand up removing them from seated dictionary.
     *
     * @param musician Musician to stand up
     */
    public void standUp(Musician musician){
        if (isSeated(musician)){
            seating.remove(musician.getSeat());
        }
    }

    /**
     * Makes all seated only musician play their next note
     */
    public void playNextNote(){
        for (Musician m: seating.values()) {
            m.playNextNote();
        }
    }
}
