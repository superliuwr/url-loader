package marvin.liu.fileloader.files.result;

import marvin.liu.fileloader.files.result.DownloadResult;
import org.easybatch.core.record.GenericRecord;
import org.easybatch.core.record.Header;

/**
 * @author marvin.liu
 */
public class DownloadResultRecord extends GenericRecord<DownloadResult> {
    public DownloadResultRecord(final Header header, final DownloadResult payload) {
        super(header, payload);
    }

    @Override
    public String toString() {
        return payload.toString();
    }
}
