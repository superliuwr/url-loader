package marvin.liu.fileloader.files.download;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author marvin.liu
 */
public interface StreamCopyer {
    void copy(InputStream inputStream, OutputStream outputStream) throws IOException;
}
