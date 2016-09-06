package marvin.liu.fileloader.files.download;

import marvin.liu.fileloader.files.result.DownloadResult;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;
import static org.mockito.Matchers.any;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * @author marvin.liu
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({URL.class, DefaultFileDownloader.class})
public class DefaultFileDownloaderTest {
    @Mock
    private URL url;

    @Mock
    private HttpURLConnection httpConn;

    @Mock
    private InputStream inputStream;

    @Mock
    private StreamCopyer streamCopyer;

    private FileDownloader fileDownloader;

    @Before
    public void setUp() throws Exception {
        fileDownloader = new DefaultFileDownloader(streamCopyer);

        when(url.openConnection()).thenReturn(httpConn);
        when(httpConn.getInputStream()).thenReturn(inputStream);
    }

    @Test
    public void shouldReturnSuccessResultWhenHttpOK() throws Exception {
        when(httpConn.getResponseCode()).thenReturn(HttpURLConnection.HTTP_OK);

        DownloadResult result = fileDownloader.download(url, ".", "sample.txt");

        assertFalse(result.isFailed());
    }

    @Test
    public void shouldReturnSuccessResultWhenNotHttpOK() throws Exception {
        when(httpConn.getResponseCode()).thenReturn(HttpURLConnection.HTTP_BAD_GATEWAY);

        DownloadResult result = fileDownloader.download(url, ".", "sample.txt");

        assertTrue(result.isFailed());
    }

    @Test
    public void shouldReturnSuccessResultWhenIOException() throws Exception {
        when(url.openConnection()).thenThrow(new IOException());
        when(httpConn.getResponseCode()).thenReturn(HttpURLConnection.HTTP_OK);

        DownloadResult result = fileDownloader.download(url, ".", "sample.txt");

        assertTrue(result.isFailed());
    }
}