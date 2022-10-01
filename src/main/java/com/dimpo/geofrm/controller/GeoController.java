package com.dimpo.geofrm.controller;

import com.dimpo.geofrm.dto.GeoDto;
import com.dimpo.geofrm.dto.SectionDto;
import com.dimpo.geofrm.entity.GeologicalClasses;
import com.dimpo.geofrm.process.ConvertDto;
import com.dimpo.geofrm.service.GeoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
public class GeoController {

    private GeoService geoService;

    public GeoController(GeoService geoService) {
        this.geoService = geoService;
    }


    @GetMapping("/sections/by-code/{code}")
    public List<GeologicalClasses> getByCode(@PathVariable("code") final String code) {
        List<GeologicalClasses> lst = geoService.findByCode(code);

        return lst;

    }
    @GetMapping("/byid/{id}")
    public GeologicalClasses getById(@PathVariable("id") final Long id) {
        GeologicalClasses ret = geoService.findById(id);

        return ret;

    }
    @GetMapping("/secdto")
    public List<SectionDto> getAllSection() {
        List<GeologicalClasses>  geoLst = geoService.findAll();
        List<SectionDto> secLst = ConvertDto.geoListToSecDTOList(geoLst);

        return secLst;

    }

 }
