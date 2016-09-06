package marvin.liu.fileloader;

import com.google.common.base.Preconditions;
import com.typesafe.config.Config;

/**
 * @author marvin.liu
 */
public class AppConfig {
    private Config config;

    public AppConfig(Config config) {
        this.config = config;
    }

    public int numberOfDownloaderThreads() throws URLDownloaderException {
        Preconditions.checkNotNull(config);

        int numberOfWorkers = config.getInt("numberOfWorkers");

        if (numberOfWorkers <= 0) {
            throw new URLDownloaderException("Config: Invalid numberOfWorkers value: " + numberOfWorkers);
        }

        return numberOfWorkers;
    }

    public long downloaderThreadTimeOut() throws URLDownloaderException {
        Preconditions.checkNotNull(config);

        long downloaderThreadTimeOut = config.getLong("downloaderThreadTimeOut");

        if (downloaderThreadTimeOut <= 0) {
            throw new URLDownloaderException("Config: Invalid downloaderThreadTimeOut value: "
                    + downloaderThreadTimeOut);
        }

        return downloaderThreadTimeOut;
    }
}
