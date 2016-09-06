package marvin.liu.fileloader.files.result;

import org.easybatch.core.processor.RecordProcessingException;
import org.easybatch.core.processor.RecordProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author marvin.liu
 */
public class FileDownloadResultLogger implements RecordProcessor<DownloadResultRecord, DownloadResultRecord> {
    private static final Logger LOG = LoggerFactory.getLogger(FileDownloadResultLogger.class);

    @Override
    public DownloadResultRecord processRecord(DownloadResultRecord record) throws RecordProcessingException {
        if (record.getPayload().isFailed()) {
            LOG.error("Error downloading from URL: " + record.getPayload().getUrl(),
                    record.getPayload().getException());
            return null;
        }

        return record;
    }
}
