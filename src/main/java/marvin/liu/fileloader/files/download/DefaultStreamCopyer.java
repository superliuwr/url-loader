package marvin.liu.fileloader.files.download;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author marvin.liu
 */
public class DefaultStreamCopyer implements StreamCopyer {
    private static final int DEFAULT_BUFFER_SIZE = 4096;

    private int bufferSize;

    public DefaultStreamCopyer() {
        this.bufferSize = DEFAULT_BUFFER_SIZE;
    }

    @Override
    public void copy(InputStream inputStream, OutputStream outputStream) throws IOException {
        int bytesRead;
        byte[] buffer = new byte[bufferSize];

        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }

        outputStream.close();
        inputStream.close();
    }
}
