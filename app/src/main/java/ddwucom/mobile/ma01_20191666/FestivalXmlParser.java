package ddwucom.mobile.ma01_20191666;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class FestivalXmlParser {

    private enum TagType { NONE, NAME, OPAR, STARTDAY, ENDDAY, LATITUDE, LONGITUDE, INTRO };
    // 해당없음, name, opar ...

    private final static String FAULT_RESULT = "faultResult";
    private final static String ITEM_TAG = "item";
    private final static String NAME_TAG = "fstvlNm";
    private final static String OPAR_TAG = "opar";
    private final static String STARTDAY_TAG = "fstvlStartDate";
    private final static String ENDDAY_TAG = "fstvlEndDate";
    private final static String LAT_TAG = "latitude";
    private final static String LON_TAG = "longitude";
    private final static String INTRO_TAG = "fstvlCo";

    private XmlPullParser parser;

    public FestivalXmlParser() {
        //xml 파서 관련 변수들은 필요에 따라 멤버변수로 선언 후 생성자에서 초기화
        //파서 준비
        XmlPullParserFactory factory = null;

        //파서 생성
        try {
            factory = XmlPullParserFactory.newInstance();
            parser = factory.newPullParser();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
    }

    public List<FestivalDTO> parse(String xml) {
        ArrayList<FestivalDTO> resultList = new ArrayList();
        FestivalDTO fstvl = null; //원하는 박스오피스 결과를 담는 객체
        TagType tagType = TagType.NONE;     //  태그를 구분하기 위한 enum 변수 초기화

        try {
            // 파싱 대상 지정
            parser.setInput(new StringReader(xml)); //바로 InputStream을 넣어도 됨
            int eventType = parser.getEventType();  // 태그 유형 구분 변수 준비

            // parsing 수행
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        String tag = parser.getName(); //parser에서 읽은 태그 이름 읽고 원하는 내용이면 조건문 시작
                        if (tag.equals(ITEM_TAG)) {     //새로운 항목을 표현하는 태그를 만났을 경우 dto 객체 생성
                            fstvl = new FestivalDTO();
                        } else if (tag.equals(NAME_TAG)) {
                            tagType = TagType.NAME;
                        } else if (tag.equals(OPAR_TAG)) {
                            tagType = TagType.OPAR;
                        } else if (tag.equals(STARTDAY_TAG)) {
                            tagType = TagType.STARTDAY;
                        } else if (tag.equals(ENDDAY_TAG)) {
                            tagType = TagType.ENDDAY;
                        } else if (tag.equals(LAT_TAG)) {
                            tagType = TagType.LATITUDE;
                        } else if (tag.equals(LON_TAG)) {
                            tagType = TagType.LONGITUDE;
                        } else if (tag.equals(INTRO_TAG)) {
                            tagType = TagType.INTRO;
                        } else if (tag.equals("relateInfo")) {  //Intro 태그에 설명이 없고 해당태그에 설명이 있을 수 있음
                            tagType = TagType.INTRO;
                        } else if (tag.equals(FAULT_RESULT)) {
                            return null;
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equals(ITEM_TAG)) {
                            resultList.add(fstvl);
                        }
                        break;
                    case XmlPullParser.TEXT:
                        switch (tagType) {              //태그의 유형에 따라 dto에 값 저장
                            case NAME:
                                fstvl.setName(parser.getText());
                                break;
                            case OPAR:
                                fstvl.setLoc(parser.getText());
                                break;
                            case STARTDAY:
                                fstvl.setStartDate(parser.getText());
                                break;
                            case ENDDAY:
                                fstvl.setEndDate(parser.getText());
                                break;
                            case LATITUDE:
                                fstvl.setLatitude(parser.getText());
                                break;
                            case LONGITUDE:
                                fstvl.setLongitude(parser.getText());
                                break;
                            case INTRO:
                                fstvl.setIntroduction(parser.getText());
                                break;
                        }
                        tagType = TagType.NONE; //관심 없는 상태 혹은 작업 완료 후 none 처리
                        break;
                }
                eventType = parser.next();
            }

        } catch (Exception e) {

        }
        return resultList;
    }

}
