package com.ml.quasar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.ml.quasar.model.vo.SatelliteConstellationVo;
import com.ml.quasar.model.vo.SatelliteVo;

@ExtendWith(value = { SpringExtension.class })
@WebAppConfiguration
@ComponentScan(basePackages = "com.ml.quasar")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("quasar-test")
@WebMvcTest(TopSecretControllerTest.class)
/**
 * The goal of this class is test some cases of the Controller class. Each one of them is explained individually.
 */
class TopSecretControllerTest {

	private MockMvc mockMvc;
	
	private final String baseurl = "/";
	
	protected Gson gson = new Gson();
	
	@Autowired
	protected WebApplicationContext context;
	
	/**
	 * Logger.
	 */
	private Logger logger = LoggerFactory.getLogger(TopSecretControllerTest.class);
	
	private SatelliteConstellationVo satelliteConstellationVo = null;
	
	/*
	 * A method to instantiate generic data
	 */
	@BeforeAll
	public void initializeGenericData() {
		// Prepare a mock to send 
		satelliteConstellationVo = new SatelliteConstellationVo();
		List<SatelliteVo> satellites = new ArrayList<SatelliteVo>();
		satellites.add(new SatelliteVo("kenobi", 29.0));
		satellites.add(new SatelliteVo("skywalker", 25.0));
		satellites.add(new SatelliteVo("sato", 13.0));
		satelliteConstellationVo.setSatellites(satellites);
		
		mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
	}
	
	/**
	 * This test has to be right
	 */
	@Test
	public void topSecret() {
		try {
			logger.info("running topSecret()");
			
			// Modify the message to get OK
			satelliteConstellationVo.getSatellites().get(0).setMessage(Arrays.asList("este","","","mensaje","secreto"));
			satelliteConstellationVo.getSatellites().get(1).setMessage(Arrays.asList("","es","","",""));
			satelliteConstellationVo.getSatellites().get(2).setMessage(Arrays.asList("este","","un","",""));
			final String messageResult = "este es un mensaje secreto";
			
			String request = this.gson.toJson(satelliteConstellationVo);
			MvcResult result = mockMvc
					.perform(post(baseurl + "top_secret").contentType(MediaType.APPLICATION_JSON).content(request))
					.andExpect(status().isOk()).andReturn();

			String content = result.getResponse().getContentAsString();
			JsonObject jsonObject = this.gson.fromJson(content, JsonObject.class);
			assertEquals(messageResult, jsonObject.get("message").getAsString());
			
			logger.info("end topSecret()");
		} catch (Exception e) {
			// Some error has occurred
			logger.error(e.getMessage(), e);
		}
	}
	
	/**
	 * This test has to fail with 404
	 */
	@Test
	public void topBadSecret() {
		try {
			logger.info("running topBadSecret()");
			
			mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
			
			// Modify the message to get 404
			satelliteConstellationVo.getSatellites().get(0).setMessage(Arrays.asList("este","","","mensaje",""));
			satelliteConstellationVo.getSatellites().get(1).setMessage(Arrays.asList("","es","","",""));
			satelliteConstellationVo.getSatellites().get(2).setMessage(Arrays.asList("este","","un","",""));
			String request = this.gson.toJson(satelliteConstellationVo);
			
			MvcResult result = mockMvc
					.perform(post(baseurl + "top_secret").contentType(MediaType.APPLICATION_JSON).content(request))
					.andExpect(status().isNotFound()).andReturn();

			// We hope for a 404 error
			assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());
			
			logger.info("end topBadSecret()");
		} catch (Exception e) {
			// Some error has occurred
			logger.error(e.getMessage(), e);
		}
	}
	
	/**
	 * This check the coordinates of the unknown position
	 */
	@Test
	public void topSecretCheckUnknownPosition() {
		try {
			logger.info("running topSecretCheckUnknownPosition()");
			
			mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
			
			// Modify the message to get OK
			satelliteConstellationVo.getSatellites().get(0).setMessage(Arrays.asList("este","","","mensaje","secreto"));
			satelliteConstellationVo.getSatellites().get(1).setMessage(Arrays.asList("","es","","",""));
			satelliteConstellationVo.getSatellites().get(2).setMessage(Arrays.asList("este","","un","",""));
			String request = this.gson.toJson(satelliteConstellationVo);
			
			double[] expectedPosition = new double[]{21.0, 20.0};
			
			MvcResult result = mockMvc
					.perform(post(baseurl + "top_secret").contentType(MediaType.APPLICATION_JSON).content(request))
					.andExpect(status().isOk()).andReturn();

			String content = result.getResponse().getContentAsString();
			JsonObject jsonObject = this.gson.fromJson(content, JsonObject.class);
			JsonObject locationVo = this.gson.fromJson(jsonObject.get("position"), JsonObject.class);
			
			// Check latitude
			assertEquals(expectedPosition[0], locationVo.get("x").getAsDouble());
			
			// Check longitude
			assertEquals(expectedPosition[1], locationVo.get("y").getAsDouble());
			
			logger.info("end topSecretCheckUnknownPosition()");
		} catch (Exception e) {
			// Some error has occurred
			logger.error(e.getMessage(), e);
		}
	}
	
}
