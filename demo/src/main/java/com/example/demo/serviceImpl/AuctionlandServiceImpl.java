package com.example.demo.serviceImpl;

import com.example.demo.entity.LocationCode;
import com.example.demo.service.AuctionlandService;
import com.example.demo.utils.UtilFunc;

import java.net.http.HttpResponse;

import com.example.demo.repository.LocationCodeRepository;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class AuctionlandServiceImpl implements AuctionlandService {
    //commit test
    private final LocationCodeRepository locationCodeRepository;

    public AuctionlandServiceImpl(LocationCodeRepository locationCodeRepository) {
        this.locationCodeRepository = locationCodeRepository;
    }

    public ResponseEntity<List<String>> getSidoLocationNameList(){

        return ResponseEntity.ok(locationCodeRepository.findDistinctLocationSido());
    }
    public ResponseEntity<List<String>> getSiguLocationNameList(String sidoData){

        return ResponseEntity.ok(locationCodeRepository.findDistinctLocationSigu(sidoData));
    }

    public ResponseEntity<List<String>> getSidongLocationNameList(String sidoData, String siguData){

        return ResponseEntity.ok(locationCodeRepository.findDistinctLocationSidong(sidoData, siguData));
    }

    public ResponseEntity<List<String>> getSiriLocationNameList(String sidoData, String siguData, String sidongData){

        return ResponseEntity.ok(locationCodeRepository.findDistinctLocationSiri(sidoData, siguData, sidongData));
    }

    @Override
    public ResponseEntity<List<String>> getAuctionData(String daepyoSidoCd, String daepyoSiguCd, String daepyoSidongCd, String daepyoSiriCd) {
        // 0. parameter 로 들어온 값을 바탕으로 H2 DB 뒤져서 법정동 코드 불러오는 부분
        Optional<LocationCode> locationCodeFinded = locationCodeRepository.findByLocationSidoAndLocationSiguAndLocationSidongAndLocationSiri(daepyoSidoCd, daepyoSiguCd, daepyoSidongCd, daepyoSiriCd);
        String SidoCd = "";
        String SiguCd = "";
        String SidongCd = "";
        String SiriCd = "";
        if (locationCodeFinded.isPresent()) {
            LocationCode locationCodeData = locationCodeFinded.get();
            String tmpLocationCode = locationCodeData.getLocationCode();
            SidoCd = tmpLocationCode.substring(0, 2);
            if(SidoCd == "00") {
                SidoCd = "";
            }
            SiguCd = tmpLocationCode.substring(2, 5);
            if(SiguCd.equals("000"))
            {
                SiguCd = "";
            }
            SidongCd = tmpLocationCode.substring(5, 8);
            if(SidongCd.equals("000"))
            {
                SidongCd = "";
            }
        } else {
            // 값이 없는 경우에는 원하는 처리를 수행합니다.
            System.out.println("Location Code 값이 없습니다.");
            return null;
        }

        System.out.println("test");



        // 1. 경매 검색 값을 저장할 변수.
        ArrayList<String> auctionDataList = new ArrayList<>();

        // 2. 법원 URI 호출을 위한 HTTP Header 셋팅 부분
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://www.courtauction.go.kr/RetrieveRealEstMulDetailList.laf"))
                .POST(BodyPublishers.ofString("srnID=PNO102000&jiwonNm=&bubwLocGubun=2&jibhgwanOffMgakPlcGubun=&mvmPlaceSidoCd=&mvmPlaceSiguCd=&roadPlaceSidoCd=&roadPlaceSiguCd=&daepyoSidoCd="+SidoCd+"&daepyoSiguCd="+SiguCd+"&daepyoDongCd="+SidongCd+"&rd1Cd=&rd2Cd=&rd3Rd4Cd=&roadCode=&notifyLoc=1&notifyRealRoad=1&notifyNewLoc=1&mvRealGbncd=1&jiwonNm1=%C0%FC%C3%BC&jiwonNm2=%BC%AD%BF%EF%C1%DF%BE%D3%C1%F6%B9%E6%B9%FD%BF%F8&mDaepyoSidoCd="+SidoCd+"&mvDaepyoSidoCd=&mDaepyoSiguCd="+SiguCd+"&mvDaepyoSiguCd=&realVowel=00000_55203&vowelSel=00000_55203&mDaepyoDongCd="+SidongCd+"&mvmPlaceDongCd=&_NAVI_CMD=&_NAVI_SRNID=&_SRCH_SRNID=PNO102000&_CUR_CMD=RetrieveMainInfo.laf&_CUR_SRNID=PNO102000&_NEXT_CMD=RetrieveRealEstMulDetailList.laf&_NEXT_SRNID=PNO102002&_PRE_SRNID=PNO102001&_LOGOUT_CHK=&_FORM_YN=Y"))
                .setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7")
                .setHeader("Accept-Language", "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7")
                .setHeader("Cache-Control", "max-age=0")
                .setHeader("Connection", "keep-alive")
                .setHeader("Content-Type", "application/x-www-form-urlencoded")
                .setHeader("Cookie", "WMONID=-khWaP--I50; mvmPlaceSidoCd=; mvmPlaceSiguCd=; realVowel=35207_45207; roadPlaceSidoCd=; roadPlaceSiguCd=; vowelSel=35207_45207; realJiwonNm=%C3%E1%C3%B5%C1%F6%B9%E6%B9%FD%BF%F8; 350=Y; JSESSIONID=63Y9Oq4cBG0lGw111mzjVaIxvhq88x1vFQD0RzxGEyogSgK6xs0LcgoB4fF7A2XD.amV1c19kb21haW4vYWlzMg==; daepyoSidoCd="+SidoCd+"; rd1Cd=; rd2Cd=; daepyoSiguCd="+SiguCd)
                .setHeader("Origin", "https://www.courtauction.go.kr")
                .setHeader("Referer", "https://www.courtauction.go.kr/RetrieveMainInfo.laf")
                .setHeader("Sec-Fetch-Dest", "frame")
                .setHeader("Sec-Fetch-Mode", "navigate")
                .setHeader("Sec-Fetch-Site", "same-origin")
                .setHeader("Sec-Fetch-User", "?1")
                .setHeader("Upgrade-Insecure-Requests", "1")
                .setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36")
                .setHeader("sec-ch-ua", "\"Not.A/Brand\";v=\"8\", \"Chromium\";v=\"114\", \"Google Chrome\";v=\"114\"")
                .setHeader("sec-ch-ua-mobile", "?0")
                .setHeader("sec-ch-ua-platform", "\"Windows\"")
                .build();


        try {
            // 2-1. 초기 1페이지 값 가져오는 부분.
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String bodyValue = response.body();
            int firstPageTBodyStartIdx = bodyValue.indexOf("<tbody>");
            int firstPageTBodyEndIdx = bodyValue.indexOf("</tbody>");
            if(firstPageTBodyStartIdx == -1 && firstPageTBodyEndIdx == -1)
            {
                System.out.println("대법원 경매 사이트 점검 중으로 값 불러오기 실패.");
                return ResponseEntity.noContent().build();
            }
            String firstPageBodyString = bodyValue.substring(firstPageTBodyStartIdx, firstPageTBodyEndIdx);

            ////
            ArrayList<String> tmpAuctionDataList = new ArrayList<>();
            tmpAuctionDataList = UtilFunc.integrateAuctionData(firstPageBodyString);

            if(tmpAuctionDataList.size() == 0)
            {
                return null;
            }

            System.out.println(tmpAuctionDataList);
            tmpAuctionDataList.forEach((element) -> {
                auctionDataList.add(element);
            });
            // 2-2. 총 몇페이지 까지 존재하는지 paging 값 받아오는 부분
            int startPgIdx = bodyValue.indexOf("<div class=\"page2\">");
            int endPgIdx = bodyValue.indexOf("<div class=\"page3\">");

            String pageString = bodyValue.substring(startPgIdx, endPgIdx);

            //System.out.println("==================================================================");
            //System.out.println(bodyValue);
            //System.out.println("==================================================================");

            int lastItemStartIdx = pageString.indexOf("다음") + 50;
            int lastItemEndIdx = pageString.indexOf("<img src=\"/images/ic_arrow_last.gif\" alt=\"마지막 쪽\" border=\"0\" />") - 19;

            String lastItemNumString = "";
            int numOfPage = 0;

            if(lastItemStartIdx == 49)
            {
                numOfPage = UtilFunc.countSubstringOccurrences(pageString, "</span>");
            }
            else
            {
                lastItemNumString = pageString.substring(lastItemStartIdx, lastItemEndIdx);
                int lastItemNumInt = Integer.parseInt(lastItemNumString);
                //System.out.println("==================================================================");
                //System.out.println(lastItemNumInt);
                //System.out.println("==================================================================");


                if(lastItemNumInt%20 == 0)
                {
                    numOfPage = (int)(lastItemNumInt/20);
                }
                else
                {
                    numOfPage = (int)(lastItemNumInt/20) + 1;
                }
            }

            //System.out.println("num of page : " + numOfPage);

            // 3. 만약 페이지가 1페이지 보다 많이 존재한다면, 위에서 뽑아낸 numOfPage 만큼 반복하여 body 값 추출
            if(numOfPage != 1)
            {
                for(int i = 1 ; i < numOfPage ; i++)
                {
                    int targetRow = 20 * i + 1;
                    HttpRequest request2 = HttpRequest.newBuilder()
                            .uri(URI.create("https://www.courtauction.go.kr/RetrieveRealEstMulDetailList.laf"))
                            .POST(BodyPublishers.ofString("page=default20&page=default20&srnID=PNO102000&jiwonNm=&bubwLocGubun=2&jibhgwanOffMgakPlcGubun=&mvmPlaceSidoCd=&mvmPlaceSiguCd=&roadPlaceSidoCd=&roadPlaceSiguCd=&daepyoSidoCd="+SidoCd+"&daepyoSiguCd="+SiguCd+"&daepyoDongCd="+SidongCd+"&rd1Cd=&rd2Cd=&rd3Rd4Cd=&roadCode=&notifyLoc=1&notifyRealRoad=1&notifyNewLoc=1&mvRealGbncd=1&jiwonNm1=%C0%FC%C3%BC&jiwonNm2=%BC%AD%BF%EF%C1%DF%BE%D3%C1%F6%B9%E6%B9%FD%BF%F8&mDaepyoSidoCd="+SidoCd+"&mvDaepyoSidoCd=&mDaepyoSiguCd="+SiguCd+"&mvDaepyoSiguCd=&realVowel=00000_55203&vowelSel=00000_55203&mDaepyoDongCd="+SidongCd+"&mvmPlaceDongCd=&_NAVI_CMD=&_NAVI_SRNID=&_SRCH_SRNID=PNO102000&_CUR_CMD=RetrieveMainInfo.laf&_CUR_SRNID=PNO102000&_NEXT_CMD=&_NEXT_SRNID=PNO102002&_PRE_SRNID=PNO102001&_LOGOUT_CHK=&_FORM_YN=Y&PNIPassMsg=%C1%A4%C3%A5%BF%A1+%C0%C7%C7%D8+%C2%F7%B4%DC%B5%C8+%C7%D8%BF%DCIP+%BB%E7%BF%EB%C0%DA%C0%D4%B4%CF%B4%D9.&pageSpec=default20&pageSpec=default20&targetRow="+ targetRow + "&lafjOrderBy="))
                            .setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7")
                            .setHeader("Accept-Language", "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7")
                            .setHeader("Cache-Control", "max-age=0")
                            .setHeader("Connection", "keep-alive")
                            .setHeader("Content-Type", "application/x-www-form-urlencoded")
                            .setHeader("Cookie", "WMONID=-khWaP--I50; mvmPlaceSidoCd=; mvmPlaceSiguCd=; realVowel=35207_45207; roadPlaceSidoCd=; roadPlaceSiguCd=; vowelSel=35207_45207; realJiwonNm=%C3%E1%C3%B5%C1%F6%B9%E6%B9%FD%BF%F8; 350=Y; daepyoSidoCd=11; rd1Cd=; rd2Cd=; page=default20; daepyoSiguCd=; JSESSIONID=bpmEchwyiqRBuXBxuhABgIIulVx6vTTaaUqLFVVR0rQjgKs3xNJxc9Pr7KNbAqOz.amV1c19kb21haW4vYWlzMg==")
                            .setHeader("Origin", "https://www.courtauction.go.kr")
                            .setHeader("Referer", "https://www.courtauction.go.kr/RetrieveRealEstMulDetailList.laf")
                            .setHeader("Sec-Fetch-Dest", "frame")
                            .setHeader("Sec-Fetch-Mode", "navigate")
                            .setHeader("Sec-Fetch-Site", "same-origin")
                            .setHeader("Sec-Fetch-User", "?1")
                            .setHeader("Upgrade-Insecure-Requests", "1")
                            .setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36")
                            .setHeader("sec-ch-ua", "\"Not.A/Brand\";v=\"8\", \"Chromium\";v=\"114\", \"Google Chrome\";v=\"114\"")
                            .setHeader("sec-ch-ua-mobile", "?0")
                            .setHeader("sec-ch-ua-platform", "\"Windows\"")
                            .build();

                    HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());

                    //System.out.println("API Call Test Success2" + response2.body());
                    String bodyValue2 = response2.body();

                    int eachPageTBodyStartIdx = bodyValue2.indexOf("<tbody>");
                    int eachPageTBodyEndIdx = bodyValue2.indexOf("</tbody>");
                    String eachPageBodyString = bodyValue2.substring(eachPageTBodyStartIdx, eachPageTBodyEndIdx);


                    tmpAuctionDataList = UtilFunc.integrateAuctionData(eachPageBodyString);

                    System.out.println(tmpAuctionDataList);
                    tmpAuctionDataList.forEach((element) -> {
                        auctionDataList.add(element);
                    });


                }
            }
            for(int idx = 0 ; idx < auctionDataList.size() ; idx++)
            {

            }
            return ResponseEntity.ok(auctionDataList);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
