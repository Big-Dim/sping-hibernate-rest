package com.dimpo.geofrm.controller;

import com.dimpo.geofrm.dto.RequestStatus;
import com.dimpo.geofrm.dto.ResponseDto;
import com.dimpo.geofrm.service.GeoService;
import com.dimpo.geofrm.service.impl.AsyncJobsManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping(path = "/api")
public class ApiController {

    private GeoService geoService;
    @Autowired
    protected AsyncJobsManager asyncJobsManager;

    public ApiController(GeoService geoService) {
        this.geoService = geoService;
    }


    @GetMapping(path = "/export/{job-id}/file", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<Resource> getJobOutputFile(@PathVariable(name = "job-id") String jobId) throws Throwable {
        //LOGGER.debug("Received request to fetch output file of job-id: {}", jobId);

        File outputFile = geoService.getOutputFile(jobId);
        InputStreamResource resource = new InputStreamResource(new FileInputStream(outputFile));

        return ResponseEntity.ok()
                .contentLength(outputFile.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    @GetMapping(path = "/export", produces = "application/json")
    public ResponseDto exportFromDb() throws Throwable {
        String jobId = UUID.randomUUID().toString();
        ResponseDto ret = new ResponseDto();
        ret.setJobId(jobId);
        ret.setRequestStatus(RequestStatus.UNKNOWN);
        if (geoService.fetchJob(jobId) != null) {
            ret.setMsg("A Job with same job-id already exists!");
            ret.setRequestStatus(RequestStatus.ERROR);
        }
        CompletableFuture<ResponseDto> completableFuture = null;
        try {
            completableFuture = geoService.exportData(jobId);
            asyncJobsManager.putJob(jobId, completableFuture);
            ret.setMsg("Data export is in progress");
            ret.setRequestStatus(RequestStatus.SUBMITTED);
        } catch (InterruptedException e) {
            ret.setMsg(e.getMessage());
            ret.setRequestStatus(RequestStatus.ERROR);
        }

        return ret;
    }

    @GetMapping(path = "/export/{job-id}", produces = "application/json")
    public ResponseDto getExportStatus(@PathVariable(name = "job-id") String jobId) throws Throwable {
        return geoService.getJobStatus(jobId);
    }

    @PostMapping(path = "/import")
    public ResponseDto importToDbAsync(@RequestPart(required = true) MultipartFile file) {

         String jobId = UUID.randomUUID().toString();
         ResponseDto ret = new ResponseDto();
         ret.setJobId(jobId);
         ret.setRequestStatus(RequestStatus.UNKNOWN);
         ret.setFileName(file.getName());
         if (null != geoService.fetchJob(jobId)) {
             ret.setMsg("A Job with same job-id already exists!");
             ret.setRequestStatus(RequestStatus.ERROR);
         }
         CompletableFuture<ResponseDto> completableFuture = null;
         try {
             completableFuture = geoService.importData(jobId, file);
             asyncJobsManager.putJob(jobId, completableFuture);
             ret.setMsg("Data import is in progress");
             ret.setRequestStatus(RequestStatus.SUBMITTED);
         } catch (InterruptedException e) {
             ret.setMsg(e.getMessage());
             ret.setRequestStatus(RequestStatus.ERROR);
         }

         return ret;
    }
    @GetMapping(path = "/import/{job-id}", produces = "application/json")
    public ResponseDto getJobStatus(@PathVariable(name = "job-id") String jobId) throws Throwable {
        return geoService.getJobStatus(jobId);
    }


}
