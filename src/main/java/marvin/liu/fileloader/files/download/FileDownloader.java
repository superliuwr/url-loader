package marvin.liu.fileloader.files.download;

import marvin.liu.fileloader.files.result.DownloadResult;

import java.net.URL;

/**
 * Download file from a given URL and save it to given directory with given name.
 *
 * @author marvin.liu
 */
public interface FileDownloader {
    DownloadResult download(URL url, String targetDir, String targetFileName);
}
