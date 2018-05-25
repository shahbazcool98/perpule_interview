package perpule.shahbaz.interview;

import com.google.gson.Gson;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;

import org.junit.Before;
import org.junit.Test;

import io.reactivex.subscribers.TestSubscriber;
import perpule.shahbaz.interview.models.Mp3Response;
import perpule.shahbaz.interview.network.NetworkClient;

public class SplashPresenterTest {

    Mp3Response mp3Response;
    MockWebServer mMockWebServer;
    TestSubscriber<Mp3Response> mSubscriber;

    @Before
    public void setUp() {
        mMockWebServer = new MockWebServer();
        mSubscriber = new TestSubscriber<>();
    }

    @Test
    public void serverCallWithError() {
        //Given
        mMockWebServer.enqueue(new MockResponse().setBody(new Gson().toJson(mp3Response)));
        RemoteDataSource remoteDataSource = new RemoteDataSource(NetworkClient.getRetrofit());

        //When
        remoteDataSource.getMp3s().subscribe(mSubscriber);

        //Then
        mSubscriber.assertNoErrors();
        mSubscriber.assertComplete();
    }

    @Test
    public void severCallWithSuccessful() {
        //Given
        mMockWebServer.enqueue(new MockResponse().setBody(new Gson().toJson(mp3Response)));
        RemoteDataSource remoteDataSource = new RemoteDataSource(NetworkClient.getRetrofit());

        //When
        remoteDataSource.getMp3s().subscribe(mSubscriber);

        //Then
        mSubscriber.assertNoErrors();
        mSubscriber.assertComplete();
    }



}
