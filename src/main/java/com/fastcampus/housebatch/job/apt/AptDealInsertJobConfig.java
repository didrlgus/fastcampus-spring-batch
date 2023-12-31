package com.fastcampus.housebatch.job.apt;

import java.time.YearMonth;
import java.util.Arrays;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersValidator;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.CompositeJobParametersValidator;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.batch.item.xml.builder.StaxEventItemReaderBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import com.fastcampus.housebatch.adapter.ApartmentApiResource;
import com.fastcampus.housebatch.core.dto.AptDealDto;
import com.fastcampus.housebatch.core.repository.LawdRepository;
import com.fastcampus.housebatch.core.service.AptDealService;
import com.fastcampus.housebatch.job.validator.YearMonthParameterValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class AptDealInsertJobConfig {
	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;

	private final ApartmentApiResource apartmentApiResource;

	@Bean
	public Job aptDealInsertJob(
		Step guLawdCdStep,
//		Step contextPrintStep,
		Step aptDealInsertStep,
		JobParametersValidator aptDealJobParameterValidator) {
		return jobBuilderFactory.get("aptDealInsertJob")
			.incrementer(new RunIdIncrementer())
			.validator(aptDealJobParameterValidator)
			.start(guLawdCdStep)
			.on("CONTINUABLE").to(aptDealInsertStep).next(guLawdCdStep)
			.from(guLawdCdStep)
			.on("*").end()
			.end()
			.build();
	}

	@Bean
	public JobParametersValidator aptDealJobParameterValidator() {
		CompositeJobParametersValidator validator = new CompositeJobParametersValidator();
		validator.setValidators(Arrays.asList(
			new YearMonthParameterValidator()
//			new LawdCdParameterValidator()
		));
		return validator;
	}

	@JobScope
	@Bean
	public Step guLawdCdStep(Tasklet guLawdCdTasklet) {
		return stepBuilderFactory.get("guLawdCdStep")
			.tasklet(guLawdCdTasklet)
			.build();
	}

	@StepScope
	@Bean
	public Tasklet guLawdCdTasklet(LawdRepository lawdRepository) {
		return new GuLawdTasklet(lawdRepository);
	}

//	@JobScope
//	@Bean
//	public Step contextPrintStep(Tasklet contextPrintTasklet) {
//		return stepBuilderFactory.get("contextPrintStep")
//			.tasklet(contextPrintTasklet)
//			.build();
//	}
//
//	@StepScope
//	@Bean
//	public Tasklet contextPrintTasklet(
//		@Value("#{jobExecutionContext['guLawdCd']}") String guLawdCd
//	) {
//		return ((contribution, chunkContext) -> {
//			System.out.println("[contextPrintStep] guLawdCd = "+ guLawdCd);
//			return RepeatStatus.FINISHED;
//		});
//	}

	@JobScope
	@Bean
	public Step aptDealInsertStep(
		StaxEventItemReader<AptDealDto> aptDealResourceReader,
		ItemWriter<AptDealDto> aptDealWriter
	) {
		return stepBuilderFactory.get("aptDealInsertStep")
			.<AptDealDto, AptDealDto>chunk(10)
			.reader(aptDealResourceReader)
			.writer(aptDealWriter)
			.build();
	}

	@StepScope
	@Bean
	public StaxEventItemReader<AptDealDto> aptDealResourceReader(
		@Value("#{jobParameters['yearMonth']}") String yearMonth,
		@Value("#{jobExecutionContext['guLawdCd']}") String guLawdCd,
		Jaxb2Marshaller aptDealDtoMarshaller
	) {
		return new StaxEventItemReaderBuilder<AptDealDto>()
			.name("aptDealResourceReader")
			.resource(apartmentApiResource.getResource(guLawdCd, YearMonth.parse(yearMonth)))
			.addFragmentRootElements("item")
			.unmarshaller(aptDealDtoMarshaller)
			.build();
	}

	@StepScope
	@Bean
	public Jaxb2Marshaller aptDealDtoMarshaller() {
		Jaxb2Marshaller jaxb2Marshaller = new Jaxb2Marshaller();
		jaxb2Marshaller.setClassesToBeBound(AptDealDto.class);
		return jaxb2Marshaller;
	}

	@StepScope
	@Bean
	public ItemWriter<AptDealDto> aptDealWriter(AptDealService aptDealService) {
		return items -> {
			items.forEach(aptDealService::upsert);
			System.out.println("============= Writing Completed =============");
		};
	}
}
