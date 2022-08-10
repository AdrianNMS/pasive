package com.bank.pasive.models.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PasiveNameType {

    FIXEDTERM(1001),
    SAVING(1002),
    ACCOUNT(1003);

    private final int value;
}
