package com.clue.service;

public interface GeoIPService extends ServiceBase {
    String getCountryCode(String ipAddress);
}
