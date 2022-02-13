package com.ml.quasar.service.location;

import java.util.List;

import com.ml.quasar.model.vo.PositionVo;
import com.ml.quasar.model.vo.SatelliteVo;

public interface I_LocationService {

	public double[] getLocation(List<PositionVo> positions, List<SatelliteVo> satellites);
	
	public double[] getHandLocation(List<PositionVo> positions, List<SatelliteVo> satellites);
	
}
