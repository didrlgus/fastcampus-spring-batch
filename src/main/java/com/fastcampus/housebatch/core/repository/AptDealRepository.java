package com.fastcampus.housebatch.core.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fastcampus.housebatch.core.entity.Apt;
import com.fastcampus.housebatch.core.entity.AptDeal;

public interface AptDealRepository extends JpaRepository<AptDeal, Long> {

	Optional<AptDeal> findAptDealByAptAndExclusiveAreaAndDealDateAndDealAmountAndFloor(
		Apt apt, Double exclusiveArea, LocalDate dealDAte, Long dealAmount, Integer floor);
}
