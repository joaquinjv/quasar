package com.ml.quasar.service.broadcast;

import java.util.List;

import com.ml.quasar.exception.AppException;
import com.ml.quasar.model.vo.SatelliteVo;

public interface I_BroadcastService {

	public String getMessageFromSatellites(List<SatelliteVo> satellites) throws AppException;
	
}
