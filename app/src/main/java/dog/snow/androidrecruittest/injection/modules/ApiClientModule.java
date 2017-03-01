package dog.snow.androidrecruittest.injection.modules;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dog.snow.androidrecruittest.models.Item;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import rx.Observable;
@Module
public class ApiClientModule {
    private static ApiClient apiClient = null;

    private static final String BASE_URL = "http://192.168.1.231:8080/api/";

    public interface ApiClient {
        @GET("items")
        Observable<List<Item>> getItems();
    }
    @Provides
    @Singleton
    public ApiClient provideApiClient(Gson gson, OkHttpClient httpClient) {
        if(apiClient!=null)
            return apiClient;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(httpClient)
                .build();
        apiClient = retrofit.create(ApiClient.class);
        return apiClient;
    }
    @Provides
    @Singleton
    public OkHttpClient provideHttpClient() {

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        Interceptor connectionInterceptor = chain -> {
            try {
                return chain.proceed(chain.request());
            } catch (IOException e) {
                Log.e("Error", "Connection Error");
                throw e;
            }

        };
        return new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(connectionInterceptor)
                .build();
    }

    @Provides
    @Singleton
    public Gson getGson() {
        return new GsonBuilder().disableHtmlEscaping()
                .registerTypeAdapter(Date.class, new JsonDateDeserializer())
                .create();
    }
    private class JsonDateDeserializer implements JsonDeserializer<Date> {
        public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            String s = json.getAsJsonPrimitive().getAsString();
            long l = Long.parseLong(s.substring(6, s.length() - 2));
            return new Date(l);
        }
    }
}
