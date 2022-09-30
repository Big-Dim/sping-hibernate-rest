package com.dimpo.geofrm.dto;

import com.dimpo.geofrm.entity.GeologicalClasses;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SectionDto
{
    private Long id;
    private String name;
    private Set<GeologicalClasses> geoClass;

    public void addGeoClass(GeologicalClasses geoCl){
        if(geoCl == null) return;
        if(geoClass == null) geoClass = new HashSet<GeologicalClasses>();
        if(!geoClass.contains(geoCl))geoClass.add(geoCl);

    }
}
