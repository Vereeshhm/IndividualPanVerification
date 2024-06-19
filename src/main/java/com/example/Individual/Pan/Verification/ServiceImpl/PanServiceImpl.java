package com.example.Individual.Pan.Verification.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.example.Individual.Pan.Verification.Entity.APILOG;
import com.example.Individual.Pan.Verification.Repository.ApiLogRepository;
import com.example.Individual.Pan.Verification.Service.PanService;
import com.example.Individual.Pan.Verification.Utils.PanRequest;
import com.example.Individual.Pan.Verification.Utils.PropertiesConfig;
import com.google.gson.Gson;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class PanServiceImpl implements PanService {

	@Autowired
	ApiLogRepository apiLogRepository;

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	PropertiesConfig config;

	@Override
	public String getPanVerification(PanRequest panRequest, HttpServletRequest request, HttpServletResponse response) {
		String response1 = null;
		APILOG apilog = new APILOG();

		try {
			String requestUrl = request.getRequestURL().toString();

			panRequest.getNumber();
			panRequest.getName();
			panRequest.getFuzzy();
			panRequest.getPanStatus();

			Gson gson = new Gson();

			String requestBodyJson = gson.toJson(panRequest);
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			 headers.set("Authorization", config.getToken());

			HttpEntity<String> entity = new HttpEntity(requestBodyJson, headers);

			apilog.setUrl(requestUrl);
			apilog.setRequestBody(requestBodyJson);

			response1 = restTemplate.postForObject(config.getApiUrl(), entity, String.class);
			apilog.setResponseBody(response1);
			apilog.setStatus("SUCCESS");
			apilog.setStatusCode(HttpStatus.OK.value());
			System.out.println(response1 + "RESPONSE");
			return response1;
		} catch (HttpClientErrorException.TooManyRequests e) {
			// Handling Too Many Requests Exception specifically
			apilog.setStatusCode(HttpStatus.TOO_MANY_REQUESTS.value());
			apilog.setStatus("FAILURE");
			response1 = e.getResponseBodyAsString();
			System.out.println(response1 + "Response");
			apilog.setResponseBodyAsJson("API rate limit exceeded");
		} catch (HttpClientErrorException.Unauthorized e) {
			// Handling Unauthorized Exception specifically
			apilog.setStatusCode(HttpStatus.UNAUTHORIZED.value());
			apilog.setStatus("FAILURE");
			response1 = e.getResponseBodyAsString();

			System.out.println(response1 + "Response");
			apilog.setResponseBodyAsJson("No API key found in request");

		} catch (HttpClientErrorException e) {
			apilog.setStatusCode(e.getStatusCode().value());
			apilog.setStatus("FAILURE");
			response1 = e.getResponseBodyAsString();
			apilog.setResponseBody(response1);

		}

		catch (Exception e) {
			apilog.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			apilog.setStatus("ERROR");
			response1 = e.getMessage();
			String errorMessage = "Error inserting into pan_verification_logs: " + e.getMessage();
			System.err.println(errorMessage);
			apilog.setResponseBody(response1);
		}

		finally {
			apiLogRepository.save(apilog);
		}
		return response1;

	}

}
