package com.bank.pasive.models.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PasiveNameType {

    SAVING(1000),
    ACCOUNT(1001),
    FIXEDTERM(1002),
    SAVINGVIP(1003),
    ACCOUNTPYME(1004);

    private final int value;
}
