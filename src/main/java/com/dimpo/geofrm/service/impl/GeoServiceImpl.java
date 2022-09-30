package com.dimpo.geofrm.service.impl;

import com.dimpo.geofrm.dto.SectionDto;
import com.dimpo.geofrm.process.ExcelGenerator;
import com.dimpo.geofrm.config.AppConfigs;
import com.dimpo.geofrm.dto.GeoDto;
import com.dimpo.geofrm.dto.RequestStatus;
import com.dimpo.geofrm.dto.ResponseDto;
import com.dimpo.geofrm.entity.GeologicalClasses;
import com.dimpo.geofrm.entity.Section;
import com.dimpo.geofrm.exception.ErrorProcessing;
import com.dimpo.geofrm.repository.GeologicalClassesRepository;
import com.dimpo.geofrm.repository.SectionRepository;
import com.dimpo.geofrm.service.GeoService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@AllArgsConstructor
@Service
public class GeoServiceImpl implements GeoService {

    private SectionRepository sectionRepository;

    private GeologicalClassesRepository geoRepository;

    @Autowired
    protected AsyncJobsManager asyncJobsManager;

    @Autowired
    AppConfigs appConfigs;

    @Override
    public void saveSection(GeoDto geoDto) {
            Section section = new Section();
            section.setName(geoDto.getSecName());
            sectionRepository.save(section);

    }

    @Override
    public void saveGeo(GeoDto geoDto) {
        Long secId = geoDto.getSecId();
        Section section = null;
        if(secId != null){
            Optional<Section> secOpt = sectionRepository.findById(secId);
            if(secOpt.isPresent())
                section = secOpt.get();
        }
        if(section == null){
            section = new Section(secId, geoDto.getSecName());
            section = sectionRepository.save(section);
        }


       GeologicalClasses geoClass = new GeologicalClasses();
       geoClass.setGeoName(geoDto.getGeoName());
       geoClass.setCode(geoDto.getCode());
       geoClass.setSection(section);

       geoRepository.save(geoClass);
    }
    @Override
    public GeologicalClasses findById(Long id) {
        GeologicalClasses sec = null;
        Optional op = geoRepository.findById(id);
        if(op.isPresent()) sec = (GeologicalClasses) op.get();
        return sec;
    }

    @Override
    public List<GeologicalClasses> findAll() {
        return (List<GeologicalClasses>) geoRepository.findAll();
    }

    @Override
    public List<GeologicalClasses> findByCode(String code) {

        int i = geoRepository.findByCode(code).size();
        return geoRepository.findByCode(code);
    }

    @Override

    @Async("asyncTaskExecutor")
    public CompletableFuture<ResponseDto> importData(String jobId, MultipartFile file) throws InterruptedException {
        CompletableFuture<ResponseDto> task =  new CompletableFuture<ResponseDto>();
        try{
            List<SectionDto> lst = ExcelGenerator.uploadFromExcel(file);
            if (!lst.isEmpty()) {
                // save to database
                for(SectionDto sd : lst) {
                    Set<GeologicalClasses> geoLst = sd.getGeoClass();
                    Section sec = new Section(sd.getId(), sd.getName());
                    sec = sectionRepository.save(sec);

                    for(GeologicalClasses gcl : geoLst) {
                        gcl.setSection(sec);
                    }
                    geoRepository.saveAll(geoLst);
                }
            }
            task.complete(new ResponseDto(jobId, "Dat imported to db", RequestStatus.COMPLETE, file.getName(), null));
        } catch (IOException e) {
            task.completeExceptionally(e);
        }

        return task;
    }

    @Override
    @Async("asyncTaskExecutor")
    public CompletableFuture<ResponseDto> exportData(String jobId) throws InterruptedException{

        CompletableFuture<ResponseDto> task =  new CompletableFuture<ResponseDto>();
        try{
            List<GeologicalClasses> lst = findAll();
            String fileUri = appConfigs.getOutFilesLocation() + jobId + ".xlsx";
            File file = new File(fileUri);

            if (!lst.isEmpty()) {
                ExcelGenerator generator = new ExcelGenerator(lst);
                generator.generateExcelFile(file);
            }
            task.complete(new ResponseDto(jobId, "Data exported from db", RequestStatus.COMPLETE,
                    fileUri, file));
        } catch (IOException e) {
            task.completeExceptionally(e);
        }

        return task;
    }

    public ResponseDto getJobStatus(String jobId){
        CompletableFuture<ResponseDto> completableFuture = fetchJobElseThrowException(jobId);
        if(completableFuture == null){
            return new ResponseDto(jobId, "This job ID not found", RequestStatus.ERROR);
        }
        if (!completableFuture.isDone()) {
            return new ResponseDto(jobId,"Job is in progress", RequestStatus.IN_PROGRESS);
        }


        Throwable[] errors = new Throwable[1];
        ResponseDto[] resps = new ResponseDto[1];

        completableFuture.whenComplete((response, ex) -> {
            if (ex != null) {
                resps[0]  = new ResponseDto(jobId, ex.getMessage(), RequestStatus.ERROR);
            } else {
                resps[0] = response;
            }
        });

        return resps[0];
    }

    public File getOutputFile(String jobId) throws Throwable {
        CompletableFuture<ResponseDto> completableFuture = fetchJob(jobId);

        if (null == completableFuture) {
            String fileUri = appConfigs.getOutFilesLocation() + jobId + ".xlsx";
            File file = new File(fileUri);
            if(file.exists()) {
                return file;
            }
            throw new ErrorProcessing("Job not found", true);
        }

        if (!completableFuture.isDone()) {
            throw new ErrorProcessing("Job is still in progress...", true);
        }

        Throwable[] errors = new Throwable[1];
        ResponseDto[] resps = new ResponseDto[1];

        completableFuture.whenComplete((response, ex) -> {
            if (ex != null) {
                resps[0]  = new ResponseDto(jobId, ex.getMessage(), RequestStatus.ERROR);
            } else {
                resps[0] = response;
            }
        });

        if (errors[0] != null) {
            throw errors[0];
        }

        return resps[0].getOutputFile();
    }

     public CompletableFuture<ResponseDto> fetchJob(String jobId) {
         CompletableFuture<ResponseDto> completableFuture = (CompletableFuture<ResponseDto>) asyncJobsManager
                .getJob(jobId);
        return completableFuture;
    }
    public CompletableFuture<ResponseDto> fetchJobElseThrowException(String jobId)  {
        CompletableFuture<ResponseDto> job = fetchJob(jobId);
        return job;
    }

}
