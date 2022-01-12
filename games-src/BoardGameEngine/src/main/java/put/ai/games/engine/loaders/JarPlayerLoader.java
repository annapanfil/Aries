/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package put.ai.games.engine.loaders;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.jar.Attributes;
import put.ai.games.game.Player;

public class JarPlayerLoader extends AbstractPlayerLoader {

    private String getPlayerClassName(URL url)
            throws MalformedURLException, IOException {
        URL u = new URL("jar", "", url + "!/");
        JarURLConnection uc = (JarURLConnection) u.openConnection();
        Attributes attr = uc.getMainAttributes();
        return attr != null ? attr.getValue(Attributes.Name.MAIN_CLASS) : null;
    }


    @Override
    public Class<? extends Player> load(String jarFile)
            throws PlayerLoadingException {
        try {
            URL url = new File(jarFile).toURI().toURL();
            this.name = getPlayerClassName(url);
            if (name == null) {
                throw new PlayerLoadingException(String.format("Can not find class name for %s", jarFile));
            }
            this.loader = new URLClassLoader(new URL[] { url });
            return load();
        } catch (IOException ex) {
            throw new PlayerLoadingException(ex);
        }
    }
}
