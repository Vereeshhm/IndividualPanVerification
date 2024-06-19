package com.example.Individual.Pan.Verification.Service;

import com.example.Individual.Pan.Verification.Utils.PanRequest;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface PanService {

	String getPanVerification(PanRequest panRequest, HttpServletRequest request, HttpServletResponse response);

}
