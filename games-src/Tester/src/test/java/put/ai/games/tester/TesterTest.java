/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package put.ai.games.tester;

import put.ai.games.tester.Tester;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * 
 * @author smaug
 */
public class TesterTest {

    @Test
    public void test1() {
        assertTrue(Tester.isValidName("Jędrzej Potoniec 84868"));
    }


    @Test
    public void test2() {
        assertTrue(Tester.isValidName("Jędrzej Potoniec 84868 Jan Kowalski 12345"));
    }


    @Test
    public void test3() {
        assertFalse(Tester.isValidName("Jędrzej Potoniec 84868 Jan Kowalski 12345 Jane Doe 543210"));
    }


    @Test
    public void middleName() {
        assertFalse(Tester.isValidName("Jędrzej F. Potoniec 84868"));
    }


    @Test
    public void tooOld() {
        assertFalse(Tester.isValidName("Jędrzej Potoniec 2603"));
    }


    @Test
    public void dash() {
        assertTrue(Tester.isValidName("Jane Nigdzie-Badz 12345"));
    }


    @Test
    public void strange() {
        assertTrue(Tester.isValidName("Alojzy Grzegżółka-Brzęczyszczykiewicz 12345"));
    }

}
