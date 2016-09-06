package marvin.liu.fileloader.urls.dispatch;

import org.easybatch.core.job.JobParameters;
import org.easybatch.core.job.JobReport;
import org.easybatch.core.listener.JobListener;
import org.easybatch.core.record.PoisonRecord;
import org.easybatch.core.record.Record;

import java.util.concurrent.BlockingQueue;

/**
 * Generate a PoisonRecord in the BlockingQueue for each downloader thread to indicate there is no more job
 * to do. URLDownloader thread will stop once PoisonRecord is processed.
 *
 * @author marvin.liu
 */
public class PoisonRecordGenerator implements JobListener {
    private int numberOfWorkers;
    private BlockingQueue<Record> queue;

    public PoisonRecordGenerator(BlockingQueue<Record> queue, int numberOfWorkers) {
        this.numberOfWorkers = numberOfWorkers;
        this.queue = queue;
    }

    @Override
    public void beforeJobStart(JobParameters jobParameters) {

    }

    @Override
    public void afterJobEnd(JobReport jobReport) {
        try {
            for (int i = 0; i < numberOfWorkers; i ++) {
                queue.put(new PoisonRecord());
                System.out.println("PoisonRecord put in queue: " + i);
            }
        } catch (InterruptedException e) {
            //TODO
        }
    }
}
