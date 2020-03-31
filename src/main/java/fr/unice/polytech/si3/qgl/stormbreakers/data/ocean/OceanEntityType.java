package fr.unice.polytech.si3.qgl.stormbreakers.data.ocean;

public enum OceanEntityType {
	BOAT("ship","S"),
	COURANT("stream", "C"), RECIF("reef", "R");

	public final String entityCode;
	public final String shortCode;

	private OceanEntityType(String entityCode, String shortCode) {

		this.entityCode = entityCode;
		this.shortCode = shortCode;
	}
}
