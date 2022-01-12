/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package put.ai.games.engine.loaders;

import put.ai.games.game.Player;

public abstract class AbstractPlayerLoader implements PlayerLoader {

    protected ClassLoader loader;
    protected String name;


    protected Class<? extends Player> load()
            throws PlayerLoadingException {
        Class<?> playerClass;
        try {
            playerClass = loader.loadClass(name);
        } catch (ClassNotFoundException ex) {
            throw new PlayerLoadingException(ex);
        }
        if (playerClass == null) {
            throw new PlayerLoadingException("Class not found");
        }
        if (!Player.class.isAssignableFrom(playerClass)) {
            throw new PlayerLoadingException("Not a Player");
        }
        return (Class<? extends Player>) playerClass;
    }
}
