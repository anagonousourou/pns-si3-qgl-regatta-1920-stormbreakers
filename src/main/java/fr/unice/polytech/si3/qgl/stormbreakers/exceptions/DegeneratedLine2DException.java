package fr.unice.polytech.si3.qgl.stormbreakers.exceptions;

/**
 * A degenerated line, whose direction vector is undefined, had been
 * encountered.
 */
public class DegeneratedLine2DException extends RuntimeException {

	/**
	 * @param msg the error message
	 */
	public DegeneratedLine2DException(String msg) {
		super(msg);
	}

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
}
