package dog.snow.androidrecruittest.models;

        import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

        import java.util.Date;

        import lombok.Data;

@Data
public class Item {

    @SerializedName("id")
    @Expose
    public int id;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("description")
    @Expose
    public String description;
    @SerializedName("icon")
    @Expose
    public String icon;
    @SerializedName("timestamp")
    @Expose
    public Date timestamp;
    @SerializedName("url")
    @Expose
    public String url;

}