package com.ml.quasar.service.broadcast;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ml.quasar.exception.AppException;
import com.ml.quasar.message.I_MessageSolver;
import com.ml.quasar.model.vo.SatelliteVo;

@Service
public class BroadcastService implements I_BroadcastService {

	@Autowired
	private I_MessageSolver messageSolver;
	
	@Override
	public String getMessageFromSatellites(List<SatelliteVo> satellites) throws AppException {
		
		List<List<String>> messages = new ArrayList<List<String>>();
		for (SatelliteVo satelliteVo : satellites) {
			messages.add(satelliteVo.getMessage());
		}
		if (messages.size() == 0) return "";
		// Get the length of the message
		int size = messages.get(0).size();
		String result = "";
		for (int i = 0; i < size; i++) {
			String resultTemp = searchWord(messages, i);
			// There was some error collected the message
			if (resultTemp == "") throw new AppException(this.getMessageSolver().getMessage("broadcast.message.cannot.read"));
			result += resultTemp + " ";
		}
		return result.trim();
	}

	private String searchWord(List<List<String>> messages, int index) {
		for (List<String> list : messages) {
			if (list.get(index) != "") return list.get(index);
		}
		return "";
	}

	public I_MessageSolver getMessageSolver() {
		return messageSolver;
	}

}
