package com.example.diceout;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    /* @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    } */

    @Test
    public void simpleAddTest(){
        MainActivity m = new MainActivity();
        assertEquals(m.simpleAdd(1,1),2);
    }

    @Test
    public void pokerCardTest(){

        pokerCard tmpCard=new pokerCard();
        tmpCard.setVal(4);
        tmpCard.setSuit(1);
        assertEquals(tmpCard.getSuit(),1);
    }

    @Test
    public void pokerCardSuitStrTest(){
        pokerCard tmpCard=new pokerCard();
        tmpCard.setVal(4);
        tmpCard.setSuit(1);
        assertEquals(tmpCard.getSuitStr(),"c");
    }
}