package marvin.liu.fileloader;

/**
 * Exception represents URLDownloader related errors.
 *
 * @author marvin.liu
 */
public class URLDownloaderException extends Exception {
    public URLDownloaderException(String msg) {
        super(msg);
    }

    public URLDownloaderException(String msg, Exception e) {
        super(msg, e);
    }
}
