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

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@ToString
@Setter
@Getter
@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name = "lawd")
public class Lawd {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long lawdId;

	@Column(nullable = false)
	private String lawdCd;

	@Column(nullable = false)
	private String lawdDong;

	@Column(nullable = false)
	private Boolean exist;

	@CreatedDate
	private LocalDateTime createdAt;

	@LastModifiedDate
	private LocalDateTime updatedAt;

	public Lawd(String lawdCd, String lawdDong, Boolean exist) {
		this.lawdCd = lawdCd;
		this.lawdDong = lawdDong;
		this.exist = exist;
	}
}
