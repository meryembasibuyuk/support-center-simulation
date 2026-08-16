package com.service;

import java.util.UUID;

/**
 * Onceki kodda UUID.randomUUID().toString().substring(0, 4) kullaniliyordu:
 * sadece 4 hex karakter (~65536 olasilik) oldugu icin orta olcekli trafikte
 * dogum gunu paradoksuyla ID cakismasi neredeyse garantiydi. Burada tam UUID
 * kullaniliyor; ID uretim stratejisini degistirmek (orn. dagitik sistemde
 * Snowflake ID) IdGenerator arayuzu sayesinde diger kodu etkilemeden yapilabilir.
 */
public class UuidIdGenerator implements IdGenerator {
    @Override
    public String nextId() {
        return "S-" + UUID.randomUUID();
    }
}
