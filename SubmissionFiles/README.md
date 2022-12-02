# COMP1202-Coursework
Full solution for 2022 COMP1202 coursework.  
Basic directory contain the basic solution.  
Extended directory contains the extended version of the coursework.  

# Basic:
### Attempted pats
Completion: 100.0%  
All parts of the coursework where attempted and successfully completed.
### How to run
#### Through Main.main
Parameters struct: musiciansFile compositionsFile numberOfYearsToSimulate  
Parameters: data/musicians.morch data/compositions.corch 3  
Class with main: Main  
Full command: java Main data/musicians.morch data/compositions.corch 3  
#### Alternative
Alternative you could run/test my code using some provide test in the main class through your ide.
### 5.2 Algorithm
#### Overwrite
Algorithm developed for part 5.2 looks at the requirements
for the selected composition and invites appreciate number of musician
this results in inviting the exact number (smallest number that can perform the selected composition) 
of musician need to preform the composition.
#### Details
The algorithm relies on two dictionary (java HashMap), one will be referred to as GD for global dictionary
and the other will be LD for local dictionary. For each composition integrate over scores and accumulate in LD
the required number of musician to play this composition, then compare the requirements of GD and LD for each
instrument if the requirements for instrument are greeter in LD then GD is set to LD. This calculates the 
minimum number of musician for each instrument type to perform all three composition (musicals are shared 
between composition e.g. if there are three composition requiring one paints GD will have number one 
associated with pianists, since one will be able to play three times). After GD has been updated based on the
chosen composition we iterate over musician that are already in the band from the previous years and subtract
them from required musicians (so that we don't invite another musician to play for them). At this point GD 
contains the exact number of musician for each instrument that need to be innovated to allow the conductor
to play the three randomly selected composition. Finally, we search the global population of musicians to 
invite the required one to the band making sure that we don't invite the same musician to play for two scrolls.
#### Code (Basic-EcsBandAid.java 110-179)
```java
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
            // ensures that the same musician does not join the band twice before leaving when simulatin
            if (!conductor.hasMusician(musicians.get(key).get(j))){
                // I assume that all musicians in the pool are Instrumentalist
                System.out.println(((Instrumentalist)musicians.get(key).get(j)).getName() + " has joined
                conductor.registerMusician(musicians.get(key).get(j));
                break;
            }
            if (j == musicians.get(key).size() - 1){#
                throw new Exception("There are not enough musicians in the pool to assemble a " +
                "band for the chosen compositions!");
            }
        }
    }
}
```

# Extended
### Completion of extension
Completion: 100.0%  
All aspects of the extension chosen has been fully implemented and tested.
### About extension
The extended directory contains and impertinents save/load extension what allows for aborting simulation
at any point (midway through playing a song) and saving data required to resume it at a later time in the exact
same place (will resume at the exact note it was saved on).
### Changes made
#### Changes to Musician.java
- new abstract method: getPlayedNotes getter for played notes
- new abstract method: skipNotes can be used to skip notes when resuming a song midway through
#### Changes to Instrumentals.java
- new variable: playedNotes keep track of how many note the musical has played
- new method: getPlayedNotes getter for played notes
- new method: skipNotes can be used to skip notes when resuming a song midway through
#### Changes to Conductor.java
- new variable: orchestra extracted from playComposition so that it can be accessed be getSaveData when saving
- new variable: currentNote keep track of the current note in the composition
- new variable: abort set to true if when composition is interrupted (e.g. by save)
- changed method: parts of playComposition has been extracted into individual method (playNotes, assembleOrchestra)
so that they can be reused by resumeComposition
- new method: playNotes extracted from playComposition, plays out notes
- new method: assembleOrchestra extracted from playComposition, assembles the orchestra
- new method: resumeComposition resumes play of the composition based on provide data
- new method: getSaveData returns correctly formatted data for save required to resume play of the composition
- new method: abortPlay aborts/stops playing any composition (need to be called from external thread)
#### Changes to Orchestra.java
- new method: getMusicians returns musician that are in the orchestra, required when saving to ensure that
musician are seated in the exact same seats when loading/resuming from file at a later time.
#### Changes to FileReader.java
- new method: loadPORCHData load saved data required to resume simulation
#### New class PORCHData.java
This class is a simple data structure used to easily pass saved progress data.
#### Changes to SoundSystem.java
- new method: stopPlaying stop playing on all notes on all channels.
#### Changes to EcsBandAid.java
- new variable: performers collection of currently performing musicians, needed for save operation
- new variable: chosenCompositions collection of chosen compositions, needed for save operation
- new variable: currentComposition keep track of the current composition, needed for save operation
- new variable: abort keep track of weather or not the simulation should be aborting
- new variable: currentYear keep track of current year, needed for save operation
- new variable: targetYear keep track of the amount of year to be simulated, needed for save operation
- changed method: performForAYear extracted applyDropout code section into its own method so that it can be
reused by resumeYear function.
- new method: resumeYear can be used to resume simulation from a saved state
- new method: save current state of the simulation to a file
- new method: applyDropout applies dropout to each band member band
- new method: resume resumes simulation from a saved state
- new method: abortSimulation aborts/stops simulation (need to be called from external thread)
- new method: hasAborted returns weather or not the simulation has been aborted
- new method: reset resets the simulation to its initial state
#### Changes to Main.java
- changed method: updated main to use save and resume functionality
### Save file format
All saves follow this format:
```
TARGET_YEAR:3
CURRENT_YEAR:1
BAND_MEMBERS:Necron,Ultimecia,Gilgamesh,Bahamut,Richard,Emma,Ardyn,Jakub
COMPOSITIONS:Weaker,Stronger,Stronger
CURRENT_COMPOSITION:1
CURRENTLY_PERFORMING:Necron:0,Ardyn:1,Ultimecia:2,Bahamut:3
CURRENT_NOTE:5
```
- TARGET_YEAR: the amount of year to be simulated
- CURRENT_YEAR: the current year of the simulation
- BAND_MEMBERS: the musicians in the band
- COMPOSITIONS: the compositions to be played
- CURRENT_COMPOSITION: the current composition being played
- CURRENTLY_PERFORMING: the musicians currently performing the current composition
- CURRENT_NOTE: the current note being played in the current composition
### How to use
To save a simulation you need to call save() (from external thread 
otherwise it will wait till the simulation is done before saving), optionally
you can also call a abortSimulation() if you want to stop the simulation from
execution (also needs to be called from external thead). To load adn resume
progress of a simulation you simply call resume().


### How to run
#### Through Main.main
Parameters struct: musiciansFile compositionsFile numberOfYearsToSimulate  
Parameters: data/musicians.morch data/compositions.corch 3  
Class with main: Main  
Full command: java Main data/musicians.morch data/compositions.corch 3
### Main explanation
The main file will start the simulation then abort/save progress after 5 seconds
of play, then it will wait 5 seconds before resuming the simulation from file.
After 10 seconds after resuming it will abort/save progress to file again,
wait 5 seconds and then resume the simulation from file again. This shows that
the save and resume functionality works as intended.
