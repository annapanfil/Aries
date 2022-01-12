/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package put.ai.games.engine.loaders;

import put.ai.games.game.Player;

public class FilePlayerLoader extends AbstractPlayerLoader {

    @Override
    public Class<? extends Player> load(String file)
            throws PlayerLoadingException {
        this.loader = new FileClassLoader(file, FilePlayerLoader.class.getClassLoader());
        this.name = null;
        return load();
    }

}
