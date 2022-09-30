package com.dimpo.geofrm.process;

import com.dimpo.geofrm.dto.SectionDto;
import com.dimpo.geofrm.entity.GeologicalClasses;
import com.dimpo.geofrm.entity.Section;

import java.util.*;

public class ConvertDto {

    public static List<SectionDto> geoListToSecDTOList(List<GeologicalClasses>geoLst){
        Map<Long, SectionDto> secMap = new HashMap<>();
        if(geoLst == null || geoLst.isEmpty()) return null;
        for(GeologicalClasses gc : geoLst){
            SectionDto secDto = convToSectionDto(gc.getSection());
            Long  key = gc.getSection().getId();
            if(secMap.containsKey(key)){
                secDto = secMap.get(key);
            }
            secDto.addGeoClass(gc);
            secMap.put(key,secDto);
        }
        return new ArrayList<SectionDto>(secMap.values());
    }
    public static SectionDto convToSectionDto(Section sec){
        return new SectionDto(sec.getId(), sec.getName(), null);
    }
}
