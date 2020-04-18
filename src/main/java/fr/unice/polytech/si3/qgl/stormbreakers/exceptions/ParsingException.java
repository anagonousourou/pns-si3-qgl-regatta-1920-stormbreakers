package fr.unice.polytech.si3.qgl.stormbreakers.exceptions;

public class ParsingException extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public ParsingException(Exception exception){
        super(exception);
    }

}