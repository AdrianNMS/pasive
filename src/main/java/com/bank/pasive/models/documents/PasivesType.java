package com.bank.pasive.models.documents;

import com.bank.pasive.models.enums.PasiveNameType;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor
public class PasivesType {

    private PasiveNameType name;

    private String comission;

    private String transactionDay;

    private Integer maxMovements;
}
