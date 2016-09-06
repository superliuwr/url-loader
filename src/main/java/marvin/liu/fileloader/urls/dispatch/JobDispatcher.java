package marvin.liu.fileloader.urls.dispatch;

import org.easybatch.core.dispatcher.AbstractRecordDispatcher;
import org.easybatch.core.record.Record;

import java.util.concurrent.BlockingQueue;

/**
 * Custom RecordDispatcher to put validated URL download requests to BlockingQueue so they can be processed by
 * downloaders.
 *
 * @author marvin.liu
 */
public class JobDispatcher<T extends Record> extends AbstractRecordDispatcher<T> {
    private BlockingQueue<T> queue;

    public JobDispatcher(BlockingQueue<T> queue) {
        this.queue = queue;
    }

    @Override
    protected void dispatchRecord(T record) throws Exception {
        queue.put(record);
    }
}
