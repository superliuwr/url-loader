package marvin.liu.fileloader.files.download;

import marvin.liu.fileloader.files.result.DownloadResult;
import marvin.liu.fileloader.files.result.DownloadResultRecord;
import org.easybatch.core.record.StringRecord;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

/**
 * @author marvin.liu
 */
@RunWith(MockitoJUnitRunner.class)
public class FileDownloadProcessorTest {
    private static final String VALID_URL = "http://www.google.com/download/sample.txt";
    private static final String INVALID_URL = "htp:\\www.google,com/download/sample.txt";

    private FileDownloadProcessor processor;

    @Mock
    private FileDownloader fileDownloader;

    @Mock
    private StringRecord record;

    @Mock
    private DownloadResult result;

    @Before
    public void setUp() throws Exception {
        processor = new FileDownloadProcessor(fileDownloader, "testDir");
    }

    @Test
    public void shouldCallFileDownloaderAndGetResultWhenValidURL() throws Exception {
        when(record.getPayload()).thenReturn(VALID_URL);
        when(fileDownloader.download(any(URL.class), anyString(), anyString())).thenReturn(result);
        when(result.getUrl()).thenReturn(VALID_URL);

        DownloadResultRecord outputRecord = processor.processRecord(record);

        verify(fileDownloader).download(any(URL.class), eq("testDir"), eq("sample.txt"));
        assertEquals(VALID_URL, outputRecord.getPayload().getUrl());
    }

    @Test
    public void shouldNotCallFileDownloaderWhenInValidURL() throws Exception {
        when(record.getPayload()).thenReturn(INVALID_URL);

        DownloadResultRecord outputRecord = processor.processRecord(record);

        verify(fileDownloader, times(0)).download(any(URL.class), eq("testDir"), eq("sample.txt"));
        assertNotNull(outputRecord.getPayload().getException());
    }
}