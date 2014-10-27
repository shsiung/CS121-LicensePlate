package cs121.team5.com.licenseplate;
/**
 * Created by cui_cindy on 10/6/14.
 */
public class PhotoAttributes {

    private String directory_;
    private String dirRoot_;
    private String name_;
    private String state_;
    private String number_;
    private Boolean special_;

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
        builder.append(this.number_);
        builder.append(this.special_);
        this.name_ = builder.toString();
        return this.name_;
    }

    public void setState(String state) {
        this.state_ = state;
    }

    public String getState() {
        return this.state_;
    }

    public void setNumber_(String number) {
        this.number_ = number;
    }

    public String getNumber_() {
        return this.number_;
    }


}
