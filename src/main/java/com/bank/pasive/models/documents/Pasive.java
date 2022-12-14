package com.bank.pasive.models.documents;

import com.bank.pasive.models.enums.PasiveNameType;
import com.bank.pasive.models.utils.Audit;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
@Document(collection = "pasives")
public class Pasive extends Audit {

    @Id
    private String id;
    @NotNull(message = "clientId must not be null")
    private String clientId;
    @NotNull(message = "pasivesType must not be null")
    private PasiveNameType pasivesType;

    @NotNull(message = "mont must not be null")
    @Min(0)
    private Float mont;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy",timezone = "GMT-05:00")
    private Date specificDay;
}
