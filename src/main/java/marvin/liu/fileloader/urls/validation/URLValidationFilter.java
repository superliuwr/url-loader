package marvin.liu.fileloader.urls.validation;

import org.apache.commons.validator.routines.UrlValidator;
import org.easybatch.core.filter.RecordFilter;
import org.easybatch.core.record.StringRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Custom RecordFilter to filter out invalid URLs before sending to BlockingQueue for downloaders to process.
 *
 * @author marvin.liu
 */
public class URLValidationFilter implements RecordFilter<StringRecord> {
    private static final Logger LOG = LoggerFactory.getLogger(URLValidationFilter.class);

    private UrlValidator urlValidator;

    public URLValidationFilter(UrlValidator urlValidator) {
        this.urlValidator = urlValidator;
    }

    @Override
    public StringRecord processRecord(final StringRecord record) {
        String url = record.getPayload();

        if (urlValidator.isValid(url)) {
            return record;
        } else {
            LOG.error("Invalid URL filtered out: " + url);
            return null;
        }
    }
}
