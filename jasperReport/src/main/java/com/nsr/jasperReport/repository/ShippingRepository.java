package com.nsr.jasperReport.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.nsr.jasperReport.entity.ShippingDetails;

@Repository
public interface ShippingRepository  extends JpaRepository<ShippingDetails, Integer>{
	
	@Query(value = "select s from ShippingDetails s where s.tRNNumber=:trnNumber ")
	List<ShippingDetails> getShippingDetails(Integer trnNumber);
}
