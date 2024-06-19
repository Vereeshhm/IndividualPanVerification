package com.example.Individual.Pan.Verification.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Individual.Pan.Verification.Service.PanService;
import com.example.Individual.Pan.Verification.Utils.PanRequest;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;



@RestController
@RequestMapping("/saswat")
public class IndividualPanController {

	
	@Autowired
	PanService panService;
	
	@PostMapping(value = "/panverification", produces = MediaType.APPLICATION_JSON_VALUE)
	public String getPanVerification(@RequestBody PanRequest panRequest, HttpServletRequest request, HttpServletResponse response) {
		return panService.getPanVerification(panRequest,request,response);
		
	}

	
}
