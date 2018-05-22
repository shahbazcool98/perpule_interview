package perpule.shahbaz.interview.network;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import perpule.shahbaz.interview.models.Mp3Response;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface NetworkInterface {

    @GET("bins/mxcsl")
    Observable<Mp3Response> getMp3s();

    @GET
    Observable<ResponseBody> downloadFile(@Url String fileUrl);

}
