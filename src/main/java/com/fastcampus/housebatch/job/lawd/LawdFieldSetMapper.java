package com.fastcampus.housebatch.job.lawd;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

import com.fastcampus.housebatch.core.entity.Lawd;

public class LawdFieldSetMapper implements FieldSetMapper<Lawd> {

	public static final String LAWD_CD = "lawdCd";
	public static final String LAWD_DONG = "lawdDong";
	public static final String EXIST = "exist";

	private static final String EXIST_TRUE = "존재";

	@Override
	public Lawd mapFieldSet(FieldSet fieldSet) throws BindException {
		return new Lawd(
			fieldSet.readString(LAWD_CD),
			fieldSet.readString(LAWD_DONG),
			fieldSet.readBoolean(EXIST, EXIST_TRUE)
		);
	}
}
