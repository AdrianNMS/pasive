package com.bank.pasive.models.documents;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data @NoArgsConstructor
public class Parameter {

    private String id;
    private Integer code;
    private Float comissionPercentage;
    private String transactionDay;
    private String maxMovementPerMonth;
    private Integer maxMovement;
    private Float percentageMaxMovement;

}
