package hr.fer.oprpp2.hw03.models;

public class TrygonometricResult {
	
	private String angle;
	private String sinus;
	private String cosinus;
	
	public TrygonometricResult(Integer angle) {
		this.angle = String.valueOf(angle);
		this.sinus = String.valueOf(Math.sin(angle));
		this.cosinus = String.valueOf(Math.cos(angle));
	}
	
	public String getAngle() {
		return this.angle;
	}
	
	public String getSinus() {
		return this.sinus;
	}
	
	public String getCosinus() {
		return this.cosinus;
	}
}
