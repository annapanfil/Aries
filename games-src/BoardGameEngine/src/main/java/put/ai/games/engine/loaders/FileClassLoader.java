/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package put.ai.games.engine.loaders;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.CodeSource;
import java.security.SecureClassLoader;
import java.security.cert.Certificate;

public class FileClassLoader extends SecureClassLoader {

    private final File file;
    private final ClassLoader parent;


    public FileClassLoader(String file, ClassLoader parent) {
        this.file = new File(file);
        this.parent = parent;
    }


    @Override
    public Class<?> loadClass(String name)
            throws ClassNotFoundException {
        if (name == null) {
            try (FileInputStream s = new FileInputStream(file)) {
                FileChannel ch = s.getChannel();
                MappedByteBuffer buffer = ch.map(FileChannel.MapMode.READ_ONLY, 0, ch.size());
                return defineClass(null, buffer, new CodeSource(file.toURI().toURL(), new Certificate[0]));
            } catch (IOException ex) {
                throw new ClassNotFoundException("File is not accessible", ex);
            }
        } else {
            return parent.loadClass(name);
        }
    }

}
