package marvin.liu.fileloader.files.result;

/**
 * @author marvin.liu
 */
public class DownloadResult {
    private boolean failed = false;
    private String url;
    private Exception exception;
    private String reason;

    public DownloadResult(String url) {
        this.failed = false;
        this.url = url;
        this.exception = null;
        this.reason = null;
    }

    public DownloadResult(String url, String reason) {
        this.failed = true;
        this.url = url;
        this.reason = reason;
        this.exception = null;
    }

    public DownloadResult(String url, Exception e) {
        this.failed = true;
        this.url = url;
        this.exception = e;
        this.reason = e.getMessage();
    }

    public boolean isFailed() {
        return failed;
    }

    public String getUrl() {
        return url;
    }

    public Exception getException() {
        return exception;
    }

    public String getReason() {
        return reason;
    }

    @Override
    public String toString() {
        return "DownloadResult{" +
                "failed=" + failed +
                ", url='" + url + '\'' +
                ", exception=" + exception +
                ", reason='" + reason + '\'' +
                '}';
    }
}
