package cs121.team5.com.licenseplate;
/**
 * Created by cui_cindy on 10/6/14.
 */
public class PhotoAttributes {

    public String name_;

    public String directory_;
    public String state_;
    public String number_;
    public Boolean special_;
    public Double longtitude_;
    public Double latitude_;

    public PhotoAttributes(){
        // Do nothing.
    }

    public PhotoAttributes(String directory, Boolean special, String state, String number) {
        this.directory_ = directory;
        this.special_ = special;
        this.state_ = state;
        this.number_ = number;
        // Parse and fill out dirRoot_, name_
    }

    public PhotoAttributes(String directory, Boolean special) {
        this.directory_ = directory;
        this.special_ = special;
        this.state_ = "UnknownState";
        this.number_ = "UnknownNumber";
        // Parse and fill out dirRoot_, name_
    }

    public PhotoAttributes(PhotoAttributes newPhoto) {
        this.directory_ = newPhoto.directory_;
        this.special_ = newPhoto.special_;
        this.state_ = newPhoto.state_;
        this.number_ = newPhoto.number_;
    }

    public String composeName() {
        StringBuilder builder = new StringBuilder();
        builder.append(this.state_);
        builder.append("*");
        builder.append(this.number_);
        builder.append("*");
        builder.append(this.special_);
        builder.append("*");
        builder.append(this.latitude_);
        builder.append("*");
        builder.append(this.longtitude_);
        builder.append(".jpg");
        this.name_ = builder.toString();
        return this.name_;
    }


}
