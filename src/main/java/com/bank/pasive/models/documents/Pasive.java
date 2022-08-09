package com.bank.pasive.models.documents;

import com.bank.pasive.models.utils.Audit;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@Document(collection = "pasives")
public class Pasive extends Audit {

    @Id
    private String id;

    private String clientId;

    private PasivesType pasivesType;

    private Double mont;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date specificDay;

}
