package com.example.nailPoker;

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
    @Test
    public void evaluateHandSumColTest(){
        player p=new player();
        int[][] tmp = new int[13][4];
        tmp[0][0]=1;
        tmp[0][1]=1;
        tmp[0][2]=1;
        tmp[0][3]=1;
        int[] tmpres = {1,1,1,1};
        assertArrayEquals(p.sumCol(tmp),tmpres);
    }
    @Test
    public void evaluateHandSumRowTest(){
        player p=new player();
        int[][] tmp = new int[13][4];
        tmp[0][0]=1;
        tmp[1][0]=1;
        tmp[2][0]=1;
        tmp[3][0]=1;
        tmp[4][0]=1;
        tmp[5][0]=1;
        tmp[6][0]=1;
        tmp[7][0]=1;
        tmp[8][0]=1;
        tmp[9][0]=1;
        tmp[10][0]=1;
        tmp[11][0]=1;
        tmp[12][0]=1;
        int[] tmpres = {1,1,1,1,1,1,1,1,1,1,1,1,1};
        assertArrayEquals(p.sumRow(tmp),tmpres);
    }
}