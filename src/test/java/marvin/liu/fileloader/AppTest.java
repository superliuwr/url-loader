package marvin.liu.fileloader;

import com.typesafe.config.Config;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

/**
 * @author marvin.liu
 */
@RunWith(MockitoJUnitRunner.class)
public class AppTest {
    @Mock
    private Config config;

    @Mock
    private URLDownloader urlDownloader;

    App app;

    @Before
    public void setUp() throws Exception {
        app = new App(urlDownloader, config);
    }

    @Test
    public void run() throws Exception {
        app.run("source", "target");

        verify(urlDownloader).download(eq("source"), eq("target"), eq(config));
        verify(urlDownloader).result();
    }

}