package com.ml.quasar.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ml.quasar.exception.AppException;
import com.ml.quasar.model.vo.PositionVo;
import com.ml.quasar.model.vo.SatelliteConstellationVo;
import com.ml.quasar.model.vo.SatelliteResponseVo;
import com.ml.quasar.service.broadcast.I_BroadcastService;
import com.ml.quasar.service.location.I_LocationService;

@RestController
@RequestMapping(path = "/core")
@Scope("request")
public class TopSecretController {
	
	/**
	 * Logger.
	 */
	private Logger logger = LoggerFactory.getLogger(TopSecretController.class);
	
	@Autowired
	private I_LocationService locationService;
	
	@Autowired
	private I_BroadcastService broadcastService;
	
	@Autowired
	private Map<String, String> dataProperties;
	
	@PostMapping(value = "/top_secret", consumes = "application/json", produces = "application/json")
	public ResponseEntity<?> topSecret(@RequestBody SatelliteConstellationVo satelliteConstellationVo) {
		try {
			this.getLogger().info("about to persist top secret {}", satelliteConstellationVo);
			
			// This example I get from a math paper with this values and this result (expectedPosition)
	        List<PositionVo> positions = setPositionFromProperties();
	        
	        double[] expectedPosition = new double[]{21.0, 20.0};

	        double[] libCalculatedPosition1 = locationService.getLocation(positions, satelliteConstellationVo.getSatellites());
	        
	        double[] handCalculatedPosition = locationService.getHandLocation(positions, satelliteConstellationVo.getSatellites());
	        
	        System.out.println("With lib: " + libCalculatedPosition1[0] + "," + libCalculatedPosition1[1] 
	        		+ " - to hand: " + handCalculatedPosition[0] + "," + handCalculatedPosition[1]);
	        
	        logger.debug("result {}, and expected {}", handCalculatedPosition, expectedPosition);

	        String message = broadcastService.getMessageFromSatellites(satelliteConstellationVo.getSatellites());
			SatelliteResponseVo satelliteResponseVo = new SatelliteResponseVo(handCalculatedPosition, message);
			return ResponseEntity.status(HttpStatus.OK).body(satelliteResponseVo);
		} catch (AppException ex) {
			logger.debug("App exception", ex);
			return ex.getRespose();
		} catch (Exception e) {
			logger.debug("unexpected exception", e);
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	private List<PositionVo> setPositionFromProperties() {
		List<PositionVo> locations = new ArrayList<PositionVo>();
		locations.add(new PositionVo(Double.parseDouble(dataProperties.get("kenobi").split(",")[0].trim()), Double.parseDouble(dataProperties.get("kenobi").split(",")[1].trim())));
		locations.add(new PositionVo(Double.parseDouble(dataProperties.get("skywalker").split(",")[0].trim()), Double.parseDouble(dataProperties.get("skywalker").split(",")[1].trim())));
		locations.add(new PositionVo(Double.parseDouble(dataProperties.get("sato").split(",")[0].trim()), Double.parseDouble(dataProperties.get("sato").split(",")[1].trim())));
		return locations;
	}
	
	@RequestMapping(value = "/topsecret_split", consumes = "application/json", produces = "application/json", method = {RequestMethod.POST, RequestMethod.GET})
	public ResponseEntity<?> topsecret_split(@RequestBody SatelliteConstellationVo satelliteConstellationVo) {
		try {
			this.getLogger().info("about to persist top secret {}", satelliteConstellationVo);
			
			// This example I get from a math paper with this values and this result (expectedPosition)
	        List<PositionVo> positions = setPositionFromProperties();
	        
	        double[] expectedPosition = new double[]{21.0, 20.0};

	        double[] libCalculatedPosition1 = locationService.getLocation(positions, satelliteConstellationVo.getSatellites());
	        
	        double[] handCalculatedPosition = locationService.getHandLocation(positions, satelliteConstellationVo.getSatellites());
	        
	        System.out.println("With lib: " + libCalculatedPosition1[0] + "," + libCalculatedPosition1[1] 
	        		+ " - to hand: " + handCalculatedPosition[0] + "," + handCalculatedPosition[1]);
	        
	        logger.debug("result {}, and expected {}", handCalculatedPosition, expectedPosition);

	        String message = broadcastService.getMessageFromSatellites(satelliteConstellationVo.getSatellites());
			SatelliteResponseVo satelliteResponseVo = new SatelliteResponseVo(handCalculatedPosition, message);
			return ResponseEntity.status(HttpStatus.OK).body(satelliteResponseVo);
		} catch (AppException ex) {
			logger.debug("App exception", ex);
			return ex.getRespose();
		} catch (Exception e) {
			logger.debug("unexpected exception", e);
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	@GetMapping(value = "/gettest", produces = "application/json")
	public void gettest() {
		try {
			this.getLogger().info("get test ok!");
			
		} catch (Exception e) {
			logger.debug("unexpected exception", e);
			e.printStackTrace();
		}
	}

	public Logger getLogger() {
		return logger;
	}

}
