package com.ml.quasar.model.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SatelliteConstellationVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7766111186941675481L;
	
	private List<SatelliteVo> satellites = new ArrayList<SatelliteVo>();

	public List<SatelliteVo> getSatellites() {
		return satellites;
	}

	public void setSatellites(List<SatelliteVo> satellites) {
		this.satellites = satellites;
	}
	
}
