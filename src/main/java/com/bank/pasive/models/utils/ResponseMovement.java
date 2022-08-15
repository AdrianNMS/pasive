package com.bank.pasive.models.utils;

import com.bank.pasive.models.documents.Parameter;
import lombok.Data;

@Data
public class ResponseMovement
{
    private Float data;
    private String message;
    private String status;
}
