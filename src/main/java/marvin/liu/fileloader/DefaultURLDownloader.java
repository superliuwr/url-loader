package marvin.liu.fileloader;

import com.google.common.collect.Lists;
import com.typesafe.config.Config;
import marvin.liu.fileloader.files.download.*;
import marvin.liu.fileloader.urls.dispatch.JobDispatcher;
import marvin.liu.fileloader.urls.dispatch.PoisonRecordGenerator;
import marvin.liu.fileloader.files.result.FileDownloadResultLogger;
import marvin.liu.fileloader.urls.validation.URLValidationFilter;
import org.apache.commons.validator.routines.UrlValidator;
import org.easybatch.core.filter.EmptyRecordFilter;
import org.easybatch.core.filter.PoisonRecordFilter;
import org.easybatch.core.job.*;
import org.easybatch.core.reader.BlockingQueueRecordReader;
import org.easybatch.core.record.Record;
import org.easybatch.flatfile.FlatFileRecordReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.concurrent.*;

/**
 * URLDownloader implementation using Easy Batch. It uses one thread reading URLs from the input file and multiple
 * other threads download and store files. Those threads work in a producer/consumer fashion via a BlockingQueue.
 *
 * Easy Batch is used to set up the work flow so we can focus on the processing work itself.
 * @see <a href="http://www.easybatch.org/">Easy Batch</a>
 *
 * @author marvin.liu
 */
public class DefaultURLDownloader implements URLDownloader {
    private BlockingQueue<Record> jobQueue = new LinkedBlockingQueue<>();
    private JobReport report;

    /**
     * Download files from URLs specified in given text file.
     *
     * @param source path of URLs input text file
     * @param target path of download target directory
     * @param config configuration
     * @throws URLDownloaderException when errors happen during batch processing
     */
    @Override
    public void download(String source, String target, Config config) throws URLDownloaderException {
        File urlsFile = sourceURLsFile(source);
        String targetDir = targetDir(target);

        int numberOfDownloaderThread = numberOfDownloaderThread(config);

        long downloaderThreadTimeOut = downloaderThreadTimeOut(config);

        List<Job> jobList = Lists.newArrayList();

        jobList.add(urlsExtractionJob(urlsFile, numberOfDownloaderThread));

        for (int i = 0; i < numberOfDownloaderThread; i ++) {
            jobList.add(fileDownloaderJob("worker-job-" + i, targetDir, downloaderThreadTimeOut));
        }

        executeBatchJobAndReport(numberOfDownloaderThread + 1, jobList);
    }






    private File sourceURLsFile(String source) throws URLDownloaderException {
        File urlsFile = new File(source);

        if (!urlsFile.exists() || urlsFile.isDirectory()) {
            throw new URLDownloaderException("URLs input file path invalid: " + source);
        }

        return urlsFile;
    }

    private String targetDir(String target) throws URLDownloaderException {
        File targetDir = new File(target);

        if (!targetDir.exists() || targetDir.isFile()) {
            throw new URLDownloaderException("Target directory path invalid: " + target);
        }

        return targetDir.getPath();
    }


}
