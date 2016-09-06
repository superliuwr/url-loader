package marvin.liu.fileloader;

import com.typesafe.config.Config;

/**
 * File downloader interface.
 *
 * @author marvin.liu
 */
public interface URLDownloader {
    void download(String source, String target, Config config) throws URLDownloaderException;
}
