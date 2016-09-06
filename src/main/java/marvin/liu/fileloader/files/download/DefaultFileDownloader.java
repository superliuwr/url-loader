package marvin.liu.fileloader.files.download;

import marvin.liu.fileloader.files.result.DownloadResult;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Default implementation of FileDownloader.
 *
 * @author marvin.liu
 */
public class DefaultFileDownloader implements FileDownloader {
    private StreamCopyer streamCopyer;

    public DefaultFileDownloader(StreamCopyer streamCopyer) {
        this.streamCopyer = streamCopyer;
    }

    @Override
    public DownloadResult download(URL url, String targetDir, String targetFileName) {
        HttpURLConnection httpConn = null;

        try {
            httpConn = (HttpURLConnection) url.openConnection();

            int responseCode = httpConn.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                String saveFilePath = targetFilePath(httpConn, targetDir, targetFileName);

                InputStream inputStream = httpConn.getInputStream();
                FileOutputStream outputStream = new FileOutputStream(saveFilePath);

                streamCopyer.copy(inputStream, outputStream);
            } else {
                return new DownloadResult(url.toString(), "Server replied HTTP code: " + responseCode);
            }
        } catch (IOException e) {
            return new DownloadResult(url.toString(), e);
        } finally {
            if (httpConn != null) {
                httpConn.disconnect();
            }
        }

        return new DownloadResult(url.toString());
    }

    private String targetFilePath(HttpURLConnection httpConn, String targetDir, String fileNameInUrl) {
        String fileName = "";
        String disposition = httpConn.getHeaderField("Content-Disposition");

        if (disposition != null) {
            int index = disposition.indexOf("filename=");
            if (index > 0) {
                fileName = disposition.substring(index + 10, disposition.length() - 1);
            }
        } else {
            fileName = fileNameInUrl;
        }

        return targetDir + File.separator + fileName;
    }
}
