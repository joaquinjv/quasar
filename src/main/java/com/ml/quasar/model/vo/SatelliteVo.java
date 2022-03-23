package com.ml.quasar.model.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SatelliteVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8784356098434167493L;
	
	private String name;
	private double distance;
	private List<String> message = new ArrayList<String>();
	
	public SatelliteVo() {
		super();
	}
	
	public SatelliteVo(int msgSize) {
		String[] emptyMsg = new String[msgSize];
		Arrays.fill(emptyMsg, "");
		this.message.addAll(Arrays.asList(emptyMsg));
	}

	public SatelliteVo(String name, double distance) {
		super();
		this.name = name;
		this.distance = distance;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getMessage() {
		return message;
	}

	public void setMessage(List<String> message) {
		this.message = message;
	}
	
}
