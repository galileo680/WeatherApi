package com.nubisoft.WeatherApi.repositories;

import com.nubisoft.WeatherApi.entity.WeatherRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WeatherRepository extends JpaRepository<WeatherRecord, Long> {

}