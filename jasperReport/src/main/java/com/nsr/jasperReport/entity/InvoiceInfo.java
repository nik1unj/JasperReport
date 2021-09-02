package com.nsr.jasperReport.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
@Entity
@Table(name = "invoiceInfo")
public class InvoiceInfo {

	 @Id
	 private Integer invoiceNumber;
	 private String invoiceFrom;
	 private String invoiceTo;
	 private String invoiceCreatedDate;
	 private String address; 
	 private String city;
	 private String country;
	 private Integer tRNNumber;
	 private Integer vatAmount;
	 private Integer discount;
}
