package com.fastcampus.housebatch.core.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.fastcampus.housebatch.core.entity.Lawd;

public interface LawdRepository extends JpaRepository<Lawd, Long> {
	Optional<Lawd> findByLawdCd(String lawdCd);

	@Query("select distinct substring(l.lawdCd, 1, 5) from Lawd l where l.exist = true and l.lawdCd not like '%00000000'")
	List<String> findDistinctGuLawdCd();
}
