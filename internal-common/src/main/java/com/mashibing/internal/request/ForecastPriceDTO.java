package com.mashibing.internal.request;

import lombok.Data;

@Data
public class ForecastPriceDTO {
    private String depLongitude;

    private String depLatitude;

    private String destLongitude;

    private String destLatitude;

}
