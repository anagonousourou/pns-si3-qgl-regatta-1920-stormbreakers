package fr.unice.polytech.si3.qgl.stormbreakers.staff.reporter;



import fr.unice.polytech.si3.qgl.stormbreakers.data.processing.InputParser;

public class TargetDefiner  {

    CheckpointsManager checkpointsManager;
    InputParser parser;

    public TargetDefiner(CheckpointsManager checkpointsManager, InputParser parser) {
        this.parser = parser;
        this.checkpointsManager = checkpointsManager;
    }

   

}