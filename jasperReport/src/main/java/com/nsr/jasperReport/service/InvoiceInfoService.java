package com.nsr.jasperReport.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.eclipse.jdt.core.compiler.InvalidInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import com.nsr.jasperReport.entity.InvoiceInfo;
import com.nsr.jasperReport.entity.ShippingDetails;
import com.nsr.jasperReport.repository.InvoiceInfoRepository;
import com.nsr.jasperReport.repository.ShippingRepository;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsReportConfiguration;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;

@Service
public class InvoiceInfoService {

	@Autowired
	private InvoiceInfoRepository invoiceInfoRepository;

	@Autowired
	private ShippingRepository shippingRepository;

	public Optional<InvoiceInfo> getInvoiceInfo(Integer invoiceNumber) {
		return invoiceInfoRepository.findById(invoiceNumber);
	}

	public List<ShippingDetails> getDetails(Integer trnNumber) {
		return shippingRepository.getShippingDetails(trnNumber);
	}

	public ResponseEntity<byte[]> getReport(String reportFormat, Integer InvoiceNumber)
			throws JRException, FileNotFoundException, InvalidInputException {
		String contentType = null;
		byte[] bytes = null;
		Map<String, Object> parameters = new HashMap<>();
		Optional<InvoiceInfo> invoiceData = getInvoiceInfo(InvoiceNumber);
		if (invoiceData.isPresent()) {
			InvoiceInfo invoice = invoiceData.get();
			List<ShippingDetails> shippingdetails = getDetails(invoice.getInvoiceNumber());
			parameters.put("InvoiceNumber", invoice.getInvoiceNumber());
			parameters.put("InvoiceFrom", invoice.getInvoiceFrom());
			parameters.put("InvoiceTo", invoice.getInvoiceTo());
			parameters.put("InvoiceCreatedDate", invoice.getInvoiceCreatedDate());
			parameters.put("Address", invoice.getAddress());
			parameters.put("City", invoice.getCity());
			parameters.put("TRNNumber", invoice.getTRNNumber());
			parameters.put("VatAmount", invoice.getVatAmount());
			parameters.put("Country", invoice.getCountry());
			parameters.put("Discount", invoice.getDiscount());

			File file = ResourceUtils.getFile("classpath:Blank.jrxml");
			JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(shippingdetails);

			JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
			HttpHeaders headers = new HttpHeaders();

			if (reportFormat.equalsIgnoreCase("pdf")) {

				contentType = "application/pdf";
				bytes = JasperExportManager.exportReportToPdf(jasperPrint);
				headers.set(HttpHeaders.CONTENT_DISPOSITION, "inline;filename=invoice.pdf");

				return ResponseEntity.ok().header("Content-Type", contentType + "; charset=UTF-8").headers(headers)
						.body(bytes);
			}
			if (reportFormat.equalsIgnoreCase("excel")) {

				contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
				headers.set(HttpHeaders.CONTENT_DISPOSITION, "inline;filename=invoice.xlsx");
				SimpleExporterInput input = new SimpleExporterInput(jasperPrint);

				try (ByteArrayOutputStream byteArray = new ByteArrayOutputStream()) {
					SimpleOutputStreamExporterOutput output = new SimpleOutputStreamExporterOutput(byteArray);
					JRXlsxExporter exporter = new JRXlsxExporter();
					exporter.setExporterInput(input);
					exporter.setExporterOutput(output);
					SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
					configuration.setOnePagePerSheet(true);
					configuration.setDetectCellType(true);
					configuration.setCollapseRowSpan(false);
					exporter.setConfiguration(configuration);
					exporter.exportReport();
					bytes = byteArray.toByteArray();
					output.close();

				} catch (IOException e) {
					System.out.println(e.getMessage());
				}

				return ResponseEntity.ok().header("Content-Type", contentType + "; charset=UTF-8").headers(headers)
						.body(bytes);
			}
		}
		return ResponseEntity.notFound().build();
	}

}
