package com.ml.quasar.model.vo;

import java.io.Serializable;

public class PositionVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8004691096845971231L;
	
	private double x;
	private double y;
	
	public PositionVo(double[] calculatedPosition) {
		super();
		this.x = calculatedPosition[0];
		this.y = calculatedPosition[1];
	}
	
	public PositionVo(double x, double y) {
		super();
		this.x = x;
		this.y = y;
	}
	
	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}
	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y = y;
	}
	
}
