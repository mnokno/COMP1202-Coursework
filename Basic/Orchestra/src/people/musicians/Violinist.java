package people.musicians;

import utils.SoundSystem;

public class Violinist extends Instrumentalist{

    /**
     * Constructor for Violinist class.
     *
     * @param name         name of this person.
     * @param soundSystem  sound system to be used for this Violinist
     */
    public Violinist(String name, SoundSystem soundSystem) {
        super(name, soundSystem, 41);
    }

    /**
     * Constructor for Violinist class.
     * Automatically sets seat on initialization.
     *
     * @param name         name of this person.
     * @param soundSystem  sound system to be used for this Violinist
     * @param seat         where this Violinist should seat
     */
    public Violinist(String name, SoundSystem soundSystem, int seat) {
        super(name, soundSystem, 41, seat);
    }
}
