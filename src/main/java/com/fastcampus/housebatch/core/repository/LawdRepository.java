package com.fastcampus.housebatch.core.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fastcampus.housebatch.core.entity.Lawd;

public interface LawdRepository extends JpaRepository<Lawd, Long> {
	Optional<Lawd> findByLawdCd(String lawdCd);
}
