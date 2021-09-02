package com.nsr.jasperReport.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nsr.jasperReport.entity.InvoiceInfo;


public interface InvoiceInfoRepository  extends JpaRepository<InvoiceInfo, Integer>{
}
