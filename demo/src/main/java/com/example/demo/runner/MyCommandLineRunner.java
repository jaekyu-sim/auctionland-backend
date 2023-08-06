package com.example.demo.runner;

import com.example.demo.entity.LocationCode;
import com.example.demo.repository.LocationCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Date;

@Component
public class MyCommandLineRunner implements CommandLineRunner {
    private final ServletContext servletContext;
    private final ResourceLoader resourceLoader;
    private final LocationCodeRepository locationCodeRepository;

    @Autowired
    public MyCommandLineRunner(ServletContext servletContext, ResourceLoader resourceLoader, LocationCodeRepository locationCodeRepository) {
        this.servletContext = servletContext;
        this.resourceLoader = resourceLoader;
        this.locationCodeRepository = locationCodeRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // 초기에 1회 실행되길 원하는 코드를 여기에 작성합니다.
        Date today = new Date();
        System.out.println("애플리케이션 시작 및 법정동 코드 정보 입력 시작.");
        System.out.println("서버 실행 시간 및 법정동 코드 비교 시간 : " + today);
        try {
            // 웹 어플리케이션의 상대경로로 리소스를 읽어옵니다.
            Resource resource = resourceLoader.getResource("classpath:static/location_code_info.csv");

            // 리소스의 InputStream을 얻어와서 CSV 파일을 읽습니다.
            InputStream inputStream = resource.getInputStream();

            // CSV 파일을 문자열로 변환하여 처리합니다.
            Charset charset = Charset.forName("EUC-KR");
            String content = new String(FileCopyUtils.copyToByteArray(inputStream), charset);

            // CSV 파일 처리 로직을 이곳에 작성하면 됩니다.
            String[] splittedContents = content.split("\n");




            for(int i = 1 ; i < splittedContents.length ; i++)
            {
                String[] splittedContentRow = splittedContents[i].split(",");
                String locationCode       = splittedContentRow[0];
                String locationSido       = splittedContentRow[1];
                String locationSigu       = splittedContentRow[2];
                String locationSidong     = splittedContentRow[3];
                String locationSiri       = splittedContentRow[4];
                String locationDeleteDate = splittedContentRow[7];

                //csv 파일 내 자료 중, delete date 가 "0" 인 값은 현재 사용중인 지역 코드. H2 DB 에 넣을것.
                if(locationDeleteDate.equals("0"))
                {
                    //System.out.println(locationCode);
                    LocationCode tmpLocationCodeData = new LocationCode();
                    tmpLocationCodeData.setLocationCode(locationCode);
                    tmpLocationCodeData.setLocationSido(locationSido);
                    tmpLocationCodeData.setLocationSigu(locationSigu);
                    tmpLocationCodeData.setLocationSidong(locationSidong);
                    tmpLocationCodeData.setLocationSiri(locationSiri);

                    locationCodeRepository.save(tmpLocationCodeData);


                }
            }


            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        System.out.println("애플리케이션 법정동 코드 정보 입력 완료.");
    }
}