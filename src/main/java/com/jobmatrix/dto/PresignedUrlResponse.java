package com.jobmatrix.dto;

import lombok.*;
import java.net.URL;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class PresignedUrlResponse {
    private URL uploadUrl;
    private String s3Key;

}