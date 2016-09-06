package marvin.liu.fileloader.urls.dispatch;

import org.easybatch.core.record.Record;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import static org.junit.Assert.assertEquals;

/**
 * @author marvin.liu
 */
public class PoisonRecordGeneratorTest {
    private PoisonRecordGenerator generator;

    private BlockingQueue<Record> queue;

    @Before
    public void setUp() throws Exception {
        queue = new LinkedBlockingDeque<>();
    }

    @Test
    public void shouldAddGivenNumberOfPoisonRecordToQueue() throws Exception {
        generator = new PoisonRecordGenerator(queue, 5);

        generator.afterJobEnd(null);

        assertEquals("Should have 5 PoisonRecords in queue", 5, queue.size());
    }
}