package people.musicians;

import utils.SoundSystem;

public class Cellist extends Instrumentalist {

    /**
     * Constructor for Cellist class.
     *
     * @param name         name of this person.
     * @param soundSystem  sound system to be used for this Cellist
     */
    public Cellist(String name, SoundSystem soundSystem) {
        super(name, soundSystem, 43);
    }

    /**
     * Constructor for Cellist class.
     * Automatically sets seat on initialization.
     *
     * @param name         name of this person.
     * @param soundSystem  sound system to be used for this Cellist
     * @param seat         where this Cellist should seat
     */
    public Cellist(String name, SoundSystem soundSystem, int seat) {
        super(name, soundSystem, 43, seat);
    }
}
