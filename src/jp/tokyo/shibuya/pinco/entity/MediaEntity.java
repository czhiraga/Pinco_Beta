package jp.tokyo.shibuya.pinco.entity;

public class MediaEntity {
    private int id;
    private String apiType;
    private String date;
    private String mediaID;
    private String userID;
    private String url;
    private int status;
    private String comment;
    private String sub1;
    private String sub2;

    public MediaEntity(int id,String apiType,String date,String mediaID,String userID,String url,String comment,String sub1,String sub2, int status){
        this.id =id;
        this.apiType=apiType;
        this.date=date;
        this.mediaID=mediaID;
        this.userID=userID;
        this.url=url;
        this.comment=comment;
        this.sub1=sub1;
        this.sub2=sub2;
        this.status=status;
    }

    public MediaEntity(int id,String apiType,String date,String mediaID,String userID,String url,String comment,int status){
        this.id =id;
        this.apiType=apiType;
        this.date=date;
        this.mediaID=mediaID;
        this.userID=userID;
        this.url=url;
        this.comment=comment;
        this.sub1="";
        this.sub2="";
        this.status=status;
    }

    public MediaEntity(int id,String apiType,String date,String mediaID,String userID,String url,String comment){
        this.id =id;
        this.apiType=apiType;
        this.date=date;
        this.mediaID=mediaID;
        this.userID=userID;
        this.url=url;
        this.comment=comment;
        this.sub1="";
        this.sub2="";
        this.status=1;
    }

    public MediaEntity(String apiType, String mediaID,String userID,String url,String comment){
        this.id =0;
        this.apiType=apiType;
        this.date="";
        this.mediaID=mediaID;
        this.userID=userID;
        this.url=url;
        this.comment=comment;
        this.sub1="";
        this.sub2="";
        this.status=1;
    }

    public MediaEntity(String apiType, String mediaID,String userID,String url,String comment,String sub1,String sub2,int status){
        this.id =0;
        this.apiType=apiType;
        this.date="";
        this.mediaID=mediaID;
        this.userID=userID;
        this.url=url;
        this.comment=comment;
        this.sub1="";
        this.sub2="";
        this.status=status;
    }

    public String getAPIType(){
        return apiType;
    }

    public int getID(){
        return id;
    }

    public String getDate(){
        return date;
    }

    public String getMediaID(){
        return mediaID;
    }
    public String getUserID(){
        return userID;
    }

    public String getURL(){
        return url;
    }

    public int getStatus(){
        return status;
    }

    public String getComment(){
        return comment;
    }

    public String getSub1(){
        return sub1;
    }

    public String getSub2(){
        return sub2;
    }
}
