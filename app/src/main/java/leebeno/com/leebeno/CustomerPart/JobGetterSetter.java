package leebeno.com.leebeno.CustomerPart;

import java.util.ArrayList;
import java.util.List;

import leebeno.com.leebeno.MyJobInterface;

public class JobGetterSetter {

    String jobTitle,description,paymentType,status,JOBID,jobType,bidders;
    int noOfLabours,jobHours,amount,min,max,bidCount,bidAmount;

    String id;
    String customerId,applierName,completedDate,bidStatus,myBid,reasonEscalation,rewardPoint,jobRating,reviewComment,pofilePic;


    public String getPofilePic() {
        return pofilePic;
    }

    public void setPofilePic(String pofilePic) {
        this.pofilePic = pofilePic;
    }

    public String getReviewComment() {
        return reviewComment;
    }

    public void setReviewComment(String reviewComment) {
        this.reviewComment = reviewComment;
    }

    public int getBidAmount() {
        return bidAmount;
    }

    public String getJobRating() {
        return jobRating;
    }

    public void setJobRating(String jobRating) {
        this.jobRating = jobRating;
    }

    public String getRewardPoint() {
        return rewardPoint;
    }

    public void setRewardPoint(String rewardPoint) {
        this.rewardPoint = rewardPoint;
    }

    public String getBidStatus() {
        return bidStatus;
    }

    public String getReasonEscalation() {
        return reasonEscalation;
    }

    public void setReasonEscalation(String reasonEscalation) {
        this.reasonEscalation = reasonEscalation;
    }

    public String getMyBid() {
        return myBid;
    }

    public void setMyBid(String myBid) {
        this.myBid = myBid;
    }

    public void setBidStatus(String bidStatus) {
        this.bidStatus = bidStatus;
    }

    public void setBidAmount(int bidAmount) {
        this.bidAmount = bidAmount;
    }

    public String getCompletedDate() {
        return completedDate;
    }

    public void setCompletedDate(String completedDate) {
        this.completedDate = completedDate;
    }

    public String getApplierName() {

        return applierName;
    }

    public void setApplierName(String applierName) {
        this.applierName = applierName;
    }

    String startDate,createdJob;

    public int getMin() {
        return min;
    }

    public String getCreatedJob() {
        return createdJob;
    }

    public void setCreatedJob(String createdJob) {
        this.createdJob = createdJob;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    private List<String> skillIds = null;


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getJOBID() {
        return JOBID;
    }

    public void setJOBID(String JOBID) {
        this.JOBID = JOBID;
    }


    public int getBidCount() {
        return bidCount;
    }

    public void setBidCount(int bidCount) {
        this.bidCount = bidCount;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getBidders() {
        return bidders;
    }

    public void setBidders(String bidders) {
        this.bidders = bidders;
    }

    public int getNoOfLabours() {
        return noOfLabours;
    }

    public void setNoOfLabours(int noOfLabours) {
        this.noOfLabours = noOfLabours;
    }

    public int getJobHours() {
        return jobHours;
    }

    public void setJobHours(int jobHours) {
        this.jobHours = jobHours;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public List<String> getSkillIds() {
        return skillIds;
    }

    public void setSkillIds(List<String> skillIds) {
        this.skillIds = skillIds;
    }

}
