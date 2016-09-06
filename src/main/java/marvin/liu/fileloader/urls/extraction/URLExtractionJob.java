package marvin.liu.fileloader.urls.extraction;

import marvin.liu.fileloader.URLDownloaderException;
import marvin.liu.fileloader.urls.dispatch.JobDispatcher;
import marvin.liu.fileloader.urls.dispatch.PoisonRecordGenerator;
import marvin.liu.fileloader.urls.validation.URLValidationFilter;
import org.apache.commons.validator.routines.UrlValidator;
import org.easybatch.core.filter.EmptyRecordFilter;
import org.easybatch.core.job.Job;
import org.easybatch.core.job.JobBuilder;
import org.easybatch.core.record.Record;
import org.easybatch.flatfile.FlatFileRecordReader;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * @author marvin.liu
 */
public class URLExtractionJob {
    private Job urlsExtractionJob(File urlsFile, int numberOfDownloaderThread) throws URLDownloaderException {
        JobDispatcher<Record> jobDispatcher = new JobDispatcher<>(jobQueue);

        String[] schemes = {"http","https"};
        UrlValidator urlValidator = new UrlValidator(schemes);

        try {
            return JobBuilder.aNewJob()
                    .named("file-reader-job")
                    .reader(new FlatFileRecordReader(urlsFile))
                    .filter(new EmptyRecordFilter())
                    .filter(new URLValidationFilter(urlValidator))
                    .dispatcher(jobDispatcher)
                    .jobListener(new PoisonRecordGenerator(jobQueue, numberOfDownloaderThread))
                    .build();
        } catch (FileNotFoundException e) {
            throw new URLDownloaderException("Can't read from source URLs file", e);
        }
    }
}
