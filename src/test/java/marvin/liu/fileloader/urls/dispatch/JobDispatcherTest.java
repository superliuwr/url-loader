package marvin.liu.fileloader.urls.dispatch;

import org.easybatch.core.record.StringRecord;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import static org.junit.Assert.*;

/**
 * @author marvin.liu
 */
@RunWith(MockitoJUnitRunner.class)
public class JobDispatcherTest {
    private JobDispatcher<StringRecord> jobDispatcher;

    private BlockingQueue<StringRecord> queue;

    @Mock
    private StringRecord record;

    @Before
    public void setUp() throws Exception {
        queue = new LinkedBlockingDeque<>();
        jobDispatcher = new JobDispatcher<>(queue);
    }

    @Test
    public void testDispatchRecord() throws Exception {
        jobDispatcher.processRecord(record);

        assertEquals(1, queue.size());

        jobDispatcher.processRecord(record);

        assertEquals(2, queue.size());
    }

}