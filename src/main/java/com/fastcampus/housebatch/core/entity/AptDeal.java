package com.fastcampus.housebatch.core.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fastcampus.housebatch.core.dto.AptDealDto;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "apt_deal")
@Data
@Entity
public class AptDeal {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long aptDealId;

	@ManyToOne
	@JoinColumn(name = "apt_id")
	private Apt apt;

	@Column(nullable = false)
	private Double exclusiveArea;

	@Column(nullable = false)
	private LocalDate dealDate;

	@Column(nullable = false)
	private Long dealAmount;

	@Column(nullable = false)
	private Integer floor;

	@Column(nullable = false)
	private boolean dealCanceled;

	@Column
	private LocalDate dealCanceledDate;

	@Column(nullable = false)
	@CreatedDate
	private LocalDateTime createdAt;

	@Column(nullable = false)
	@LastModifiedDate
	private LocalDateTime updatedAt;

	public AptDeal(Apt apt, Double exclusiveArea, LocalDate dealDate, Long dealAmount, Integer floor,
		boolean dealCanceled, LocalDate dealCanceledDate) {
		this.apt = apt;
		this.exclusiveArea = exclusiveArea;
		this.dealDate = dealDate;
		this.dealAmount = dealAmount;
		this.floor = floor;
		this.dealCanceled = dealCanceled;
		this.dealCanceledDate = dealCanceledDate;
	}

	public static AptDeal of(AptDealDto dto, Apt apt) {
		return new AptDeal(
			apt,
			dto.getExclusiveArea(),
			dto.getDealDate(),
			dto.getDealAmount(),
			dto.getFloor(),
			dto.isDealCanceled(),
			dto.getDealCanceledDate()
		);
	}
}
