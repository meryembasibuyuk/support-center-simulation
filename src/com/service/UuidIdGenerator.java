package com.service;

import java.util.UUID;


public class UuidIdGenerator implements IdGenerator {
    @Override
    public String nextId() {
        return "S-" + UUID.randomUUID();
    }
}
