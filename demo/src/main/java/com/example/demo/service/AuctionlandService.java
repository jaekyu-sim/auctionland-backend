package com.example.demo.service;

import com.example.demo.entity.LocationCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.http.HttpResponse;
import java.util.List;


public interface AuctionlandService {
    public ResponseEntity<List<String>> getLocationName(String parentData, String depth);
    ResponseEntity<List<String>> getAuctionData(String daepyoSidoCd, String daepyoSiguCd, String daepyoSidongCd, String daepyoSiriCd);
    //List<LocationCode> findLocationCodeByCriteria(String SidoCd, String SiguCd, String SidongCd, String SiriCd);
}
