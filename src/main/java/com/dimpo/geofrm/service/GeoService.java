package com.dimpo.geofrm.service;

import com.dimpo.geofrm.dto.GeoDto;
import com.dimpo.geofrm.dto.ResponseDto;
import com.dimpo.geofrm.dto.SectionDto;
import com.dimpo.geofrm.entity.GeologicalClasses;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface GeoService {

    void saveSection(GeoDto geoDto);

    void saveGeo(GeoDto geoDto);
    GeologicalClasses findById(Long id);
    List<GeologicalClasses> findAll();
    List<GeologicalClasses> findByCode(String code);

     CompletableFuture<ResponseDto> importData(String jobId, MultipartFile file) throws InterruptedException;

    CompletableFuture<ResponseDto> fetchJob(String jobId);

    public ResponseDto getJobStatus(String jobId) ;

    CompletableFuture<ResponseDto> exportData(String jobId) throws InterruptedException;
    File getOutputFile(String jobId) throws Throwable;


}
