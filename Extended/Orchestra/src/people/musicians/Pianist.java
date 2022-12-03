package people.musicians;

import utils.SoundSystem;

public class Pianist extends Instrumentalist{

    /**
     * Constructor for Pianist class.
     *
     * @param name         name of this person.
     * @param soundSystem  sound system to be used for this Pianist
     */
    public Pianist(String name, SoundSystem soundSystem) {
        super(name, soundSystem, 1);
    }

    /**
     * Constructor for Pianist class.
     * Automatically sets seat on initialization.
     *
     * @param name         name of this person.
     * @param soundSystem  sound system to be used for this Pianist
     * @param seat         where this Pianist should seat
     */
    public Pianist(String name, SoundSystem soundSystem, int seat) {
        super(name, soundSystem, 1, seat);
    }
}
