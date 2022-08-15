package com.bank.pasive.models.utils;

import com.bank.pasive.models.documents.Parameter;
import lombok.Data;

import java.util.List;

@Data
public class ResponseParameter
{
    private Parameter data;

    private String message;

    private String status;

}
