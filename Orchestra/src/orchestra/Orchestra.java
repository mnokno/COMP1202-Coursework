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
            // I don't like this part, since there could be some other musician
            // already seating in this seat, but it says to do it this wayS.
            seating.put(musician.getSeat(), musician);
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
