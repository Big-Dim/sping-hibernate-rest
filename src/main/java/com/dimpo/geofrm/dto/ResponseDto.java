package com.dimpo.geofrm.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.io.File;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "outputFile")
public class ResponseDto {
    private String jobId;
    private String msg;
    RequestStatus requestStatus;
    private String fileName;
    @JsonIgnore
    private File outputFile;

    public ResponseDto(String jobId, String msg, RequestStatus requestStatus) {
        this.jobId = jobId;
        this.msg = msg;
        this.requestStatus = requestStatus;
    }
}
