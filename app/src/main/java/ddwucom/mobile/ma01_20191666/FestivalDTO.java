package ddwucom.mobile.ma01_20191666;

import java.io.Serializable;

public class FestivalDTO implements Serializable {

    private long _id;               //id
    private String name;            //축제 이름
    private String startDate;       //축제 시작 날짜
    private String endDate;         //축제 종료 날짜
    private String loc;            //축제 위치
    private String latitude;        //위도
    private String longitude;       //경도
    private String introduction;    //소개
    private String memo;

    public FestivalDTO(long id, String name, String startDate, String endDate, String loc, String lat, String lon, String memo) {
        this._id = id;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.loc = loc;
        this.latitude = lat;
        this.longitude = lon;
        this.memo = memo;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
    public FestivalDTO() {}

    public FestivalDTO(String name, String startDate, String endDate, String loc, String latitude, String longitude, String memo) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.loc = loc;
        this.latitude = latitude;
        this.longitude = longitude;
        this.memo = memo;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public long get_id() {
        return _id;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getLoc() {
        return loc;
    }

    public void setLoc(String loc) {
        this.loc = loc;
    }
}
