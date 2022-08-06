package com.bank.pasive.models.utils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Data
public class Audit {
    @CreatedDate
    @JsonIgnore
    private LocalDateTime createdDate;

    @LastModifiedDate
    @JsonIgnore
    private LocalDateTime updateDate;
}
