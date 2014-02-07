/*
 * Copyright 2012-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springside.samples.quickservice.functional;

import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.client.RestTemplate;
import org.springside.samples.quickservice.QuickServiceApplication;
import org.springside.samples.quickservice.domain.Task;

public class TaskRestServiceTest {
	private static ConfigurableApplicationContext context;

	private RestTemplate restTemplate = new RestTemplate();
	private String resourceUrl = "http://localhost:8080/task";

	private static class TaskList extends ArrayList<Task> {
	}

	@BeforeClass
	public static void start() throws Exception {
		Future<ConfigurableApplicationContext> future = Executors.newSingleThreadExecutor().submit(
				new Callable<ConfigurableApplicationContext>() {
					@Override
					public ConfigurableApplicationContext call() throws Exception {
						return SpringApplication.run(QuickServiceApplication.class);
					}
				});
		context = future.get(60, TimeUnit.SECONDS);
	}

	@AfterClass
	public static void stop() {
		if (context != null) {
			context.close();
		}
	}

	@Test
	public void listTask() {
		TaskList tasks = restTemplate.getForObject(resourceUrl, TaskList.class);
		assertThat(tasks).hasSize(5);
		Task firstTask = tasks.get(0);

		assertThat(firstTask.getTitle()).isEqualTo("Spring Boot");
		assertThat(firstTask.getUserName()).isEqualTo("Admin");
	}

	@Test
	public void getTask() {
		Task task = restTemplate.getForObject(resourceUrl + "/{id}", Task.class, 1L);
		assertThat(task.getTitle()).isEqualTo("Spring Boot");
		assertThat(task.getUserName()).isEqualTo("Admin");
	}

}
