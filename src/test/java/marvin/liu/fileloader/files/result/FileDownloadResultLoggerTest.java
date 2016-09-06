package marvin.liu.fileloader.files.result;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

/**
 * @author marvin.liu
 */
@RunWith(MockitoJUnitRunner.class)
public class FileDownloadResultLoggerTest {
    private FileDownloadResultLogger fileDownloadResultLogger;

    @Mock
    private DownloadResultRecord resultRecord;

    @Mock
    private DownloadResult downloadResult;

    @Before
    public void setUp() throws Exception {
        fileDownloadResultLogger = new FileDownloadResultLogger();
        when(resultRecord.getPayload()).thenReturn(downloadResult);
    }

    @Test
    public void shouldReturnOriginalRecordWhenSuccessfualResult() throws Exception {
        when(downloadResult.isFailed()).thenReturn(false);

        DownloadResultRecord outputRecord = fileDownloadResultLogger.processRecord(resultRecord);

        assertEquals(resultRecord, outputRecord);
    }

    @Test
    public void shouldReturnNullWhenFailedResult() throws Exception {
        when(downloadResult.isFailed()).thenReturn(true);

        DownloadResultRecord outputRecord = fileDownloadResultLogger.processRecord(resultRecord);

        assertNull(outputRecord);
    }
}