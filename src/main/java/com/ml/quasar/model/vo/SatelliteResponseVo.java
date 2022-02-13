package com.ml.quasar.model.vo;

import java.io.Serializable;

public class SatelliteResponseVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3761167148571659381L;
	
	private PositionVo position;
	private String message;
	
	public SatelliteResponseVo(double[] calculatedPosition, String message) {
		super();
		this.setMessage(message);
		this.setPosition(new PositionVo(calculatedPosition));
	}
	public PositionVo getPosition() {
		return position;
	}
	public void setPosition(PositionVo position) {
		this.position = position;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
}
