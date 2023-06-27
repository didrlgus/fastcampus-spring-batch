package com.fastcampus.housebatch.job.lawd;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.assertj.core.util.Maps;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.fastcampus.housebatch.BatchTestConfig;
import com.fastcampus.housebatch.core.service.LawdService;

@SpringBatchTest
@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@ContextConfiguration(classes = {LawdInsertJobConfig.class, BatchTestConfig.class})
public class LawdInsertJobConfigTest {

	@Autowired
	private JobLauncherTestUtils jobLauncherTestUtils;

	@MockBean
	private LawdService lawdService;

	@Test
	public void success() throws Exception {
		JobParameters parameters = new JobParameters(Maps.newHashMap("filePath", new JobParameter("TEST_LAWD_CODE.txt")));
		JobExecution execution = jobLauncherTestUtils.launchJob(parameters);

		assertEquals(execution.getExitStatus(), ExitStatus.COMPLETED);
		verify(lawdService, times(3)).upsert(any());
	}

	@Test
	public void fail_whenFileNotFound() throws Exception {
		JobParameters parameters = new JobParameters(Maps.newHashMap("filePath", new JobParameter("NOT_EXIST_FILE.txt")));

		assertThrows(JobParametersInvalidException.class, () -> jobLauncherTestUtils.launchJob(parameters));
		verify(lawdService, never()).upsert(any());
	}
}
