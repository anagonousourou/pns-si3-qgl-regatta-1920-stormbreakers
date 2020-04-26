package fr.unice.polytech.si3.qgl.stormbreakers.data.ocean;

public enum OceanEntityType {
	STREAM("stream", "C"), REEF("reef", "R"),BOAT("ship","s");

	public final String entityCode;
	public final String shortCode;

	private OceanEntityType(String entityCode, String shortCode) {

		this.entityCode = entityCode;
		this.shortCode = shortCode;
	}
}
