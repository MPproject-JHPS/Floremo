package org.androidtown.Floremo;

import java.util.Date;

//database에 넣을 메모 class
public class Memo {
    private String txt;
    private Date createDate, updateDate;
    public String getTxt(){
        return txt;
    }

    public void setTxt(String txt){
        this.txt=txt;
    }
    public Date getCreateDate(){
        return createDate;
    }
    public void setCreateDate(Date createDate){
        this.createDate = createDate;
    }
    public Date getUpdateDate(){
        return updateDate;
    }
    public void setUpdateDate(Date updateDate){
        this.updateDate = updateDate;
    }
}
