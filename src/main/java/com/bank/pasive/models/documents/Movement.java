package com.bank.pasive.models.documents;

import com.bank.pasive.models.enums.MovementType;
import com.bank.pasive.models.utils.Audit;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class Movement extends Audit {

    @Id
    private String id;

    private MovementType typeMovement;

    private Double mont;

    private Double comissionMont;

}
