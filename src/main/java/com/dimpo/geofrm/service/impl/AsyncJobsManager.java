package com.dimpo.geofrm.service.impl;

import com.dimpo.geofrm.dto.ResponseDto;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public class AsyncJobsManager {

	private final ConcurrentMap<String, CompletableFuture<ResponseDto>> mapOfJobs;

	public AsyncJobsManager() {
		mapOfJobs = new ConcurrentHashMap<String, CompletableFuture<ResponseDto>>();
	}

	public void putJob(String jobId, CompletableFuture<ResponseDto> theJob) {
		mapOfJobs.put(jobId, theJob);
	}

	public CompletableFuture<ResponseDto> getJob(String jobId) {
		return mapOfJobs.get(jobId);
	}

	public void removeJob(String jobId) {
		mapOfJobs.remove(jobId);
	}
}
