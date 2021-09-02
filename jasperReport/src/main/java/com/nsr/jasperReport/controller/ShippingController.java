package com.nsr.jasperReport.controller;

import java.io.FileNotFoundException;
import java.util.List;

import org.eclipse.jdt.core.compiler.InvalidInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.nsr.jasperReport.entity.ShippingDetails;
import com.nsr.jasperReport.repository.ShippingRepository;
import com.nsr.jasperReport.service.InvoiceInfoService;

import net.sf.jasperreports.engine.JRException;

@RestController
public class ShippingController {


	@Autowired
	private InvoiceInfoService invoiceInfoService;
	
	@Autowired
	private ShippingRepository shippingRepository;
	
	@GetMapping("/report/{format}/{invoiceNumber}")
	public ResponseEntity<byte[]> generateReport(@PathVariable String format,@PathVariable Integer invoiceNumber)
			throws FileNotFoundException, JRException, InvalidInputException {
		return invoiceInfoService.getReport(format,invoiceNumber);
	}

	@GetMapping("/test/{trnNumber}")
	public List<ShippingDetails> getDetails(@PathVariable Integer trnNumber){
		return shippingRepository.getShippingDetails(trnNumber);
	}
	
}
