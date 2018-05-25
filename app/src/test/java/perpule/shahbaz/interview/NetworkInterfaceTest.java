package perpule.shahbaz.interview;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import okhttp3.ResponseBody;
import perpule.shahbaz.interview.models.Mp3Response;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface NetworkInterfaceTest {

    @GET("bins/mxcsl")
    Flowable<Mp3Response> getMp3s();

    @GET
    Flowable<ResponseBody> downloadFile(@Url String fileUrl);

}
