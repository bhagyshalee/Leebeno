package leebeno.com.leebeno;

import java.util.List;

public interface MyJobInterface {

    String jobTitle="",description="",paymentType="",status="",JOBID="",jobType="",bidders="";
    int noOfLabours=0,jobHours=0,amount=0,min=0,max=0,bidCount=0,bidAmount=0;
    String id="";
    String customerId="",applierName="",completedDate="",bidStatus="",myBid="",reasonEscalation="";
     int getBidAmount();


    public String getBidStatus();

    public String getReasonEscalation();

    public void setReasonEscalation(String reasonEscalation);

    public String getMyBid();

    public void setMyBid(String myBid);

    public void setBidStatus(String bidStatus);

    public void setBidAmount(int bidAmount);

    public String getCompletedDate();

    public void setCompletedDate(String completedDate);

    public String getApplierName();

    public void setApplierName(String applierName);

    String startDate=null,createdJob = null;

    public int getMin();

    public String getCreatedJob();

    public void setCreatedJob(String createdJob);

    public void setMin(int min);

    public int getMax();

    public void setMax(int max);

     List<String> skillIds = null;


    public String getDescription();

    public void setDescription(String description);

    public String getPaymentType();

    public void setPaymentType(String paymentType);

    public String getStatus();

    public void setStatus(String status);

    public String getJOBID();

    public void setJOBID(String JOBID);


    public int getBidCount();

    public void setBidCount(int bidCount);

    public String getJobType();

    public void setJobType(String jobType);

    public int getAmount();

    public void setAmount(int amount);

    public String getBidders();

    public void setBidders(String bidders);

    public int getNoOfLabours();

    public void setNoOfLabours(int noOfLabours);

    public int getJobHours();

    public void setJobHours(int jobHours);

    public String getId();

    public void setId(String id);

    public String getCustomerId();

    public void setCustomerId(String customerId);

    public String getJobTitle();

    public void setJobTitle(String jobTitle);

    public String getStartDate();

    public void setStartDate(String startDate);

    public List<String> getSkillIds();

    public void setSkillIds(List<String> skillIds);


}
