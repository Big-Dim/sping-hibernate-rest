package com.dimpo.geofrm.dto;

import com.dimpo.geofrm.entity.GeologicalClasses;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Transient;
import javax.validation.constraints.NotEmpty;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GeoDto
{
    private Long id;
    @NotEmpty(message = "Name should not be empty")
    private String geoName;
    @NotEmpty(message = "Code should not be empty")
    private String code;
    @NotEmpty(message = "ID should not be empty")
    private Long secId;
    @NotEmpty(message = "Name should not be empty")
    private String secName;

}
