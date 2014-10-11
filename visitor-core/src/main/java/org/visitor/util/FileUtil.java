/**
 * 
 */
package org.visitor.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;

/**
 * @author mengw
 *
 */
public abstract class FileUtil {

	/**
	 * Writes a String to a file creating the file if it does not exist.
	 * @param file
	 * @param data
	 * @param encoding
	 * @param append append or write overwrite old content.
	 * @throws IOException
	 */
	public static void writeStringToFile(File file, String data,
			String encoding, boolean append) throws IOException {
        OutputStream out = null;
        try {
            out = openOutputStream(file, append);
            IOUtils.write(data, out, encoding);
        } finally {
            IOUtils.closeQuietly(out);
        }		
	}

	/**
     * Opens a {@link FileOutputStream} for the specified file, checking and
     * creating the parent directory if it does not exist.
     * <p>
     * At the end of the method either the stream will be successfully opened,
     * or an exception will have been thrown.
     * <p>
     * The parent directory will be created if it does not exist.
     * The file will be created if it does not exist.
     * An exception is thrown if the file object exists but is a directory.
     * An exception is thrown if the file exists but cannot be written to.
     * An exception is thrown if the parent directory cannot be created.
	 * @param file the file to open for output, must not be <code>null</code>
	 * @param append if true, then bytes will be written to the end of the file rather than the beginning
	 * @return
	 * @throws IOException
	 */
	public static OutputStream openOutputStream(File file, boolean append) throws IOException{
        if (file.exists()) {
            if (file.isDirectory()) {
                throw new IOException("File '" + file + "' exists but is a directory");
            }
            if (file.canWrite() == false) {
                throw new IOException("File '" + file + "' cannot be written to");
            }
        } else {
            File parent = file.getParentFile();
            if (parent != null && parent.exists() == false) {
                if (parent.mkdirs() == false) {
                    throw new IOException("File '" + file + "' could not be created");
                }
            }
        }
        return new FileOutputStream(file, append);
	}
}
