package com.example.demo.utils;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;

public class UtilFunc {

    public static int countSubstringOccurrences(String mainString, String subString) {
        String[] parts = mainString.split(subString);
        return parts.length - 1;
    }

    public static Document convertStringToXml(String xmlString) {

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        try {

            // optional, but recommended
            // process XML securely, avoid attacks like XML External Entities (XXE)
            dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);

            DocumentBuilder builder = dbf.newDocumentBuilder();

            Document doc = builder.parse(new InputSource(new StringReader(xmlString)));

            return doc;

        } catch (ParserConfigurationException | IOException | SAXException e) {
            throw new RuntimeException(e);
        }

    }

    public static ArrayList<String> integrateAuctionData(String pageBodyString){
        ArrayList<String> auctionDataList = new ArrayList<>();
        //Start
        int index = 0;
        if(!pageBodyString.contains("<tr class=\"Ltbl_list_lvl0\">"))
        {
            System.out.println("가져올 경매 데이터가 아예 없음.");
        }
        else {

            while(true)
            {
                int targetStartIdx  = pageBodyString.indexOf("<tr", index);
                int targetEndIdx = pageBodyString.indexOf("</tr", index) + 3; //3을 붙인 이유는 </tr> 태그의 끝 index를 잡기 위해서 강제로 3을 더해줌


                String firstPageBodyRowString = pageBodyString.substring(targetStartIdx, targetEndIdx);
                if(!auctionDataList.contains(firstPageBodyRowString))
                {
                    auctionDataList.add(firstPageBodyRowString);

                }



                index = pageBodyString.indexOf("<tr", index + 1);
                if(index == -1)
                {
                    break;
                }
            }

        }//End

//        if(auctionDataList.size() > 1 && (auctionDataList.get(0).equals(auctionDataList.get(1))))
//        {
//            auctionDataList.remove(0);
//        }

        return auctionDataList;

    }
}
