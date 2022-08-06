package com.bank.pasive.models.documents;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor
public class PasivesType {

    @Id
    private String id;

    private String name;

    private String transactionDay;

    private Integer maxMovements;
}
