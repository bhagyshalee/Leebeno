package leebeno.com.leebeno.Model;

import java.io.Serializable;

public class RejectModel implements Serializable {
    String id,jobid,peopleid,jobtile,rejected_date,cus_name,usd,comment,placeddate;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJobid() {
        return jobid;
    }

    public void setJobid(String jobid) {
        this.jobid = jobid;
    }

    public String getPeopleid() {
        return peopleid;
    }

    public void setPeopleid(String peopleid) {
        this.peopleid = peopleid;
    }

    public String getJobtile() {
        return jobtile;
    }

    public void setJobtile(String jobtile) {
        this.jobtile = jobtile;
    }

    public String getRejected_date() {
        return rejected_date;
    }

    public void setRejected_date(String rejected_date) {
        this.rejected_date = rejected_date;
    }

    public String getCus_name() {
        return cus_name;
    }

    public void setCus_name(String cus_name) {
        this.cus_name = cus_name;
    }

    public String getUsd() {
        return usd;
    }

    public void setUsd(String usd) {
        this.usd = usd;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getPlaceddate() {
        return placeddate;
    }

    public void setPlaceddate(String placeddate) {
        this.placeddate = placeddate;
    }
}
