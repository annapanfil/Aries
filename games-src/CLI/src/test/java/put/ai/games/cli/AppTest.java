/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package put.ai.games.cli;

import put.ai.games.cli.App;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import put.ai.games.game.Player;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AppTest {

    @Mock
    private Player p;


    @Test
    public void space() {
        when(p.getName()).thenReturn("proper name");
        assertEquals("\"proper name\"", App.getName(p));
    }


    @Test
    public void eol() {
        when(p.getName()).thenReturn("proper\nname");
        assertEquals("\"proper_name\"", App.getName(p));
    }


    @Test
    public void backspace() {
        when(p.getName()).thenReturn("proper\bname");
        assertEquals("\"proper_name\"", App.getName(p));
    }


    @Test
    public void tab() {
        when(p.getName()).thenReturn("proper\tname");
        assertEquals("\"proper\tname\"", App.getName(p));
    }


    @Test
    public void quot() {
        when(p.getName()).thenReturn("proper\"name\"");
        assertEquals("\"proper\"\"name\"\"\"", App.getName(p));
    }
}
