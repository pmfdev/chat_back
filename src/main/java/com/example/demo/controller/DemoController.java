package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.DemoServiceImpl;

@RestController
public class DemoController {

	@Autowired
	DemoServiceImpl demoServ;

	@CrossOrigin(origins = "*") // Permitir todos los orígenes
	@GetMapping("/get")
	public String getDetails() {
		return demoServ.getDetails();
	}

}
