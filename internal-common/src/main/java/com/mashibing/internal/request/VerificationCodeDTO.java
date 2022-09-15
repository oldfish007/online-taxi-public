package com.mashibing.internal.request;

import lombok.Data;

@Data
public class VerificationCodeDTO {

    private String passengerPhone;
    private String verificationCode;
}
