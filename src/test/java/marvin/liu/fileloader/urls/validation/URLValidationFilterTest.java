package marvin.liu.fileloader.urls.validation;

import org.apache.commons.validator.routines.UrlValidator;
import org.easybatch.core.record.Header;
import org.easybatch.core.record.Record;
import org.easybatch.core.record.StringRecord;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

/**
 * @author marvin.liu
 */
public class URLValidationFilterTest {
    private URLValidationFilter filter;

    private Header header;

    @Before
    public void setup() throws Exception {
        String[] schemes = {"http","https"};
        UrlValidator urlValidator = new UrlValidator(schemes);
        filter = new URLValidationFilter(urlValidator);

        header = new Header(1L, "test record", new Date());
    }

    @Test
    public void shouldReturnRecordWhenValidHttpUrl() throws Exception {
        String validHttpUrl = "http://www.google.com/download/sample.txt";
        StringRecord record = new StringRecord(header, validHttpUrl);
        Record outputRecord = filter.processRecord(record);
        assertEquals("Should return original Record when valid HTTP URL", record, outputRecord);
    }

    @Test
    public void shouldReturnRecordWhenValidHttpsUrl() throws Exception {
        String validHttpUrl = "https://www.google.com/download/sample.txt";
        StringRecord record = new StringRecord(header, validHttpUrl);
        Record outputRecord = filter.processRecord(record);
        assertEquals("Should return original Record when valid HTTPS URL", record, outputRecord);
    }

    @Test
    public void shouldReturnNullWhenInvalidHttpUrl() throws Exception {
        String invalidHttpUrl = "http:\\\\www.google,com/download/sample.txt";
        StringRecord record = new StringRecord(header, invalidHttpUrl);
        Record outputRecord = filter.processRecord(record);
        assertEquals("Should return null when invalid HTTP URL", null, outputRecord);
    }

    @Test
    public void shouldReturnNullWhenInvalidHttpsUrl() throws Exception {
        String invalidHttpsUrl = "https:\\\\www.google,com/download/sample.txt";
        StringRecord record = new StringRecord(header, invalidHttpsUrl);
        Record outputRecord = filter.processRecord(record);
        assertEquals("Should return null when invalid HTTPS URL", null, outputRecord);
    }

    @Test
    public void shouldReturnNullWhenEmptyUrl() throws Exception {
        StringRecord record = new StringRecord(header, "");
        Record outputRecord = filter.processRecord(record);
        assertEquals("Should return null when empty URL", null, outputRecord);
    }

    @Test
    public void shouldReturnNullWhenNullUrl() throws Exception {
        StringRecord record = new StringRecord(header, null);
        Record outputRecord = filter.processRecord(record);
        assertEquals("Should return null when null URL", null, outputRecord);
    }

    @Test
    public void shouldReturnNullWhenFtpUrl() throws Exception {
        String ftpUrl = "ftp://ftp.google.com/download/sample.txt";
        StringRecord record = new StringRecord(header, ftpUrl);
        Record outputRecord = filter.processRecord(record);
        assertEquals("Should return null when FTP URL", null, outputRecord);
    }
}