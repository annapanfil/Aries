/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package put.ai.games.engine.loaders;

import put.ai.games.game.Player;

public class MetaPlayerLoader implements PlayerLoader {

    private final JarPlayerLoader jarLoader = new JarPlayerLoader();

    public static final PlayerLoader INSTANCE = new MetaPlayerLoader();


    @Override
    public Class<? extends Player> load(String file)
            throws PlayerLoadingException {
        return jarLoader.load(file);
    }

}
