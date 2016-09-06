package marvin.liu.fileloader.files.download;

import marvin.liu.fileloader.files.result.FileDownloadResultLogger;
import org.easybatch.core.filter.PoisonRecordFilter;
import org.easybatch.core.job.Job;
import org.easybatch.core.job.JobBuilder;
import org.easybatch.core.reader.BlockingQueueRecordReader;
import org.easybatch.core.record.StringRecord;

import java.util.concurrent.BlockingQueue;

/**
 * @author marvin.liu
 */
public class FileDownloaderJob {
    private FileDownloader fileDownloader;
    private BlockingQueue<StringRecord> jobQueue;
    private String targetDir;
    private long timeout;

    public FileDownloaderJob(FileDownloader fileDownloader, BlockingQueue<StringRecord> jobQueue,
                             String targetDir, long timeout) {
        this.fileDownloader = fileDownloader;
        this.jobQueue = jobQueue;
        this.targetDir = targetDir;
        this.timeout = timeout;
    }

    private Job getInstance(String jobName) {
        return JobBuilder.aNewJob()
                .named(jobName)
                .timeout(timeout)
                .reader(new BlockingQueueRecordReader<>(jobQueue))
                .filter(new PoisonRecordFilter())
                .processor(new FileDownloadProcessor(fileDownloader, targetDir))
                .processor(new FileDownloadResultLogger())
                .build();
    }
}
