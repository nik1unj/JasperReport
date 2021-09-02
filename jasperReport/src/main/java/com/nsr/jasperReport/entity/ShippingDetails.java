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
@Table(name = "shippingDetails")
public class ShippingDetails {
	@Id
	private Integer serialNumber;
	private	Integer hsCode;
	private String inventorytype;
	private Integer quantity;
	private Integer soldAmount;
	private Integer discount;
	private Integer duty;
	private Integer netAmount;
	private Integer tRNNumber;

}
