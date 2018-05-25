package perpule.shahbaz.interview;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import okhttp3.ResponseBody;
import perpule.shahbaz.interview.models.Mp3Response;
import perpule.shahbaz.interview.network.NetworkInterface;
import retrofit2.Retrofit;
import retrofit2.http.Url;

public class RemoteDataSource implements NetworkInterfaceTest {

    private NetworkInterfaceTest api;

    public RemoteDataSource(Retrofit retrofit) {
        this.api = retrofit.create(NetworkInterfaceTest.class);
    }


    @Override
    public Flowable<Mp3Response> getMp3s()
    {
        return api.getMp3s();
    }

    @Override
    public Flowable<ResponseBody> downloadFile(@Url String fileUrl)
    {
        return api.downloadFile(fileUrl);
    }

}
