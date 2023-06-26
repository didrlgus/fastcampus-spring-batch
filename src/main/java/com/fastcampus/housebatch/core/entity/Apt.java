package com.fastcampus.housebatch.core.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fastcampus.housebatch.core.dto.AptDealDto;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "apt")
@Data
@Entity
public class Apt {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long aptId;

	@Column(nullable = false)
	private String aptName;

	@Column(nullable = false)
	private String jibun;

	@Column(nullable = false)
	private String dong;

	@Column(nullable = false)
	private String guLawdCd;

	@Column(nullable = false)
	private Integer builtYear;

	@Column(nullable = false)
	@CreatedDate
	private LocalDateTime createdAt;

	@Column(nullable = false)
	@LastModifiedDate
	private LocalDateTime updatedAt;

	public Apt(String aptName, String jibun, String dong,
		String guLawdCd, Integer builtYear) {
		this.aptName = aptName;
		this.jibun = jibun;
		this.dong = dong;
		this.guLawdCd = guLawdCd;
		this.builtYear = builtYear;
	}

	public static Apt from(AptDealDto dto) {
		return new Apt(
			dto.getAptName().trim(),
			dto.getJibun().trim(),
			dto.getDong().trim(),
			dto.getRegionalCode().trim(),
			dto.getBuildYear()
		);
	}
}
