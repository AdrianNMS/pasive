package com.bank.pasive.models.utils;

import lombok.Data;

import java.util.List;

@Data
public class ResponseDebitCard
{
    private List<String> data;
    private String message;

    private String status;
}
