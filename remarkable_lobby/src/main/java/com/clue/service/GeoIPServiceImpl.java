package com.clue.service;

import com.maxmind.db.CHMCache;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.model.CountryResponse;
import io.vertx.core.Vertx;

import java.io.InputStream;
import java.net.InetAddress;

public class GeoIPServiceImpl implements GeoIPService {
    private boolean ready = false;
    private DatabaseReader reader = null;

    public boolean isReady() {
        return ready;
    }

    public void initialize(Vertx vertx) {
        try {
            InputStream in = getClass().getResourceAsStream("/geoip/GeoLite2-Country.mmdb");
            reader = new DatabaseReader.Builder(in).withCache(new CHMCache()).build();
            ready = true;
        }
        catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    public String getCountryCode(String ipAddress) {
        try {
            InetAddress address = InetAddress.getByName(ipAddress);
            CountryResponse response = reader.country(address);
            return response.getCountry().getIsoCode();
        }
        catch (Exception e) {
            System.out.println(e.toString());
        }
        
        return "UNKNOWN";
    }
}
