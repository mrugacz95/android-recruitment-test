package dog.snow.androidrecruittest.models;

        import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import lombok.Getter;

@Getter
public class Item extends RealmObject{

    @SerializedName("id")
    @Expose
    @PrimaryKey
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