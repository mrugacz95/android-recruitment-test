package dog.snow.androidrecruittest.models;

        import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

import io.realm.RealmObject;
        import io.realm.annotations.Ignore;
        import io.realm.annotations.Index;
        import io.realm.annotations.PrimaryKey;
        import lombok.EqualsAndHashCode;
        import lombok.Getter;

@Getter
@EqualsAndHashCode
public class Item extends RealmObject{

    @SerializedName("id")
    @Expose
    @PrimaryKey
    public int id;
    @SerializedName("name")
    @Expose
    @Index
    public String name;
    @SerializedName("description")
    @Expose
    @Index
    public String description;
    @SerializedName("icon")
    @Expose
    public String icon;
    @SerializedName("timestamp")
    @Expose
    @Ignore
    public Date timestamp;
    @SerializedName("url")
    @Expose
    @Ignore
    public String url;

}