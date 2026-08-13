package com.model;

public enum AgentStatus {
    ONLINE,   // Müsait / Aktif
    BUSY,     // Dolu / Kapasitesi Dolmuş
    OFFLINE,  // Çevrimdışı / Mesai Bitmiş
    ONBREAK   // Molada (Temsilciye yeni iş atanmaz)
}