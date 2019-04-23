package leebeno.com.leebeno.Model;

import java.io.Serializable;

public class SentRequestModel implements Serializable {

    String id,name,image,waitresponse;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getWaitresponse() {
        return waitresponse;
    }

    public void setWaitresponse(String waitresponse) {
        this.waitresponse = waitresponse;
    }
}
