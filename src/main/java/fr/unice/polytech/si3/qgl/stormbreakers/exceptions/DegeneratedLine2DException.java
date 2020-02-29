package fr.unice.polytech.si3.qgl.stormbreakers.exceptions;

/**
 * File: 	DegeneratedLine2DException.java
 * Project: javaGeom
 * 
 * Distributed under the LGPL License.
 *
 * Created: 19 aout 2010
 */



/**
 * A degenerated line, whose direction vector is undefined, had been
 * encountered.
 * This kind of exception can occur during polygon or polylines algorithms,
 * when polygons have multiple vertices. 
 * @author dlegland
 * @since 0.9.0
 */
public class DegeneratedLine2DException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	
	/**
	 * @param msg the error message
	 * @param line the degenerated line
	 */
	public DegeneratedLine2DException(String msg) {
		super(msg);
	}
		
	
	
}
