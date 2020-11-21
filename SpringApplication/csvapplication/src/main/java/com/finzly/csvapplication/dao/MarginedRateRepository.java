package com.finzly.csvapplication.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.finzly.csvapplication.model.MarginedRate;

@Repository
public interface MarginedRateRepository extends JpaRepository<MarginedRate,Integer> {

}
