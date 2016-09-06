package marvin.liu.fileloader.files.download;

import marvin.liu.fileloader.files.result.DownloadResult;
import marvin.liu.fileloader.files.result.DownloadResultRecord;
import org.easybatch.core.processor.RecordProcessingException;
import org.easybatch.core.processor.RecordProcessor;
import org.easybatch.core.record.StringRecord;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * The Processor which actually do the download job.
 * It gets one Record from BlockingQueue at a time and download the file from the given URL.
 * It stops once PoisonRecord is processed.
 *
 * @author marvin.liu
 */
public class FileDownloadProcessor implements RecordProcessor<StringRecord, DownloadResultRecord> {
    private FileDownloader fileDownloader;

    private String targetDir;

    public FileDownloadProcessor(FileDownloader fileDownloader, String targetDir) {
        this.fileDownloader = fileDownloader;
        this.targetDir = targetDir;
    }

    @Override
    public DownloadResultRecord processRecord(StringRecord record) throws RecordProcessingException {
        DownloadResult result;
        String payload = record.getPayload();

        try {
            URL url = new URL(payload);

            String targetFileName = payload.substring(payload.lastIndexOf("/") + 1, payload.length());

            result = fileDownloader.download(url, targetDir, targetFileName);
        } catch (MalformedURLException e) {
            result = new DownloadResult(payload, e);
        }

        return new DownloadResultRecord(record.getHeader(), result);
    }
}
