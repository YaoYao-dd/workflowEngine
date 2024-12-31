package com.yaod.workflow.engine.core.domain.enums;

public enum ItemStatus implements CodeableEnum {
    PENDING(1),
    PROCESSED(2),
    REJECTED(4),
    FINISHED(8),
    FAIL(16);


    private final int code;

    ItemStatus(int code) {
        this.code = code;
    }

    @Override
    public int getCode() {
        return code;
    }
}
