package com.fastcampus.housebatch.core.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fastcampus.housebatch.core.entity.Apt;

public interface AptRepository extends JpaRepository<Apt, Long> {

	Optional<Apt> findAptByAptNameAndJibun(String aptName, String jibun);
}
