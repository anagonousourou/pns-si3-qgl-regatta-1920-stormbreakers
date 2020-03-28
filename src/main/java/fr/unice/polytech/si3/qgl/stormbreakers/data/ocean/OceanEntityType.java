package fr.unice.polytech.si3.qgl.stormbreakers.data.ocean;

public enum OceanEntityType {
	COURANT("COURANT","C"),
	RECIF("RECIF","R")/*,
	WIND("WIND","W"),
	BOAT("BOAT","W")*/;
	
	
    public final String EntityCode;
    public final String shortCode;
	private OceanEntityType(String entityCode, String shortCode) {
		// TODO Auto-generated constructor stub
		this.EntityCode=entityCode;
		this.shortCode=shortCode;
	}
}
