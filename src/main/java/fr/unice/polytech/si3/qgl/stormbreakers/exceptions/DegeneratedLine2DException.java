package fr.unice.polytech.si3.qgl.stormbreakers.exceptions;

/**
 * A degenerated line, whose direction vector is undefined, had been
 * encountered.
 */
public class DegeneratedLine2DException extends RuntimeException {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	// TODO: 07/03/2020 Remove serialUID

	/**
	 * @param msg the error message
	 */
	public DegeneratedLine2DException(String msg) {
		super(msg);
	}

}
