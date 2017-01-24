package com.example.diceout;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;
import java.util.StringTokenizer;

class pokerCard {
     // basic class for a poker card
     // value is 2-14 (14=ace, 13=king, 12=queen, 11=jack
     private int val;
     private int suit;
    String[] valStr  = {"2","3","4","5","6","7","8","9","10","J","Q","K","A"};
    String[] suitStr = {"c","s","h","d"};

     public int getSuit() {
         return suit;
     }

    public String getSuitStr() {
        return(suitStr[suit-1]);
    }
    public String getValStr() {
        return(valStr[val-2]);
    }
     public int getVal() {
         return val;
     }

     public void setSuit(int suit) {
         this.suit = suit;
     }

     public void setVal(int val) {
         this.val = val;
     }
    public String toString()
    {
        return(valStr[val-2]+suitStr[suit-1]);
    }
 }
class pokerHand{
    private ArrayList<pokerCard> hand;
    // add cards to hand
    public pokerHand(pokerDeck d)
    {
        hand=new ArrayList<pokerCard>();
        hand.add(d.getCard());
        hand.add(d.getCard());
    }

    public ArrayList<pokerCard> getHand() {
        return hand;
    }

    public String toString()
    {
        return(hand.get(0).toString()+" "+hand.get(1).toString());
    }
}

class pokerDeck{
    private ArrayList<pokerCard> deck;
    public pokerDeck()
    {
        deck = new ArrayList<pokerCard>();
        // create cards and put them in deck
        for(int i=2; i<=14;i++)
        {
            for(int j=1; j<=4; j++)
            {
               pokerCard tmpCard=new pokerCard();
                tmpCard.setVal(i);
                tmpCard.setSuit(j);
                deck.add(tmpCard);
            }
        }
    }

    public pokerCard getCard()
    {
        // get length of deck and generate random int
        int deckSize = this.deck.size();
        Random rand = new Random();
        int cardInd = rand.nextInt(deckSize);

        // get card in random position and remove it from deck
        pokerCard tmpCard = this.deck.get(cardInd);
        this.deck.remove(cardInd);

        // return card
        return(tmpCard);
    }
}

public class MainActivity extends AppCompatActivity {

    //poker decks
    pokerDeck masterDeck;
    pokerDeck currentDeck;

    // poker hands, indices are players
    ArrayList<pokerHand> handList;

    //number of players
    int numPlayers;

    // Field to hold the roll result text
    TextView rollResult;
    TextView scoreText;
    TextView card1Text;
    TextView card2Text;

    //field for reset button
    Button resetButton;

    // field to hold the score
    int score;
    int numClicks;
    int total;
    int scoreDelta = 0;

    // random instance
    Random rand;

    // field to hold the vie value
    int die1;
    int die2;
    int die3;

    // list for dice
    ArrayList<Integer> dice;


    // make array list for them
    ArrayList<ImageView> cardImageViews;

    @Override
    // activates when activity runs
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // defautl action button
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rollDice(view);
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show(); */
            }
        });

        // set initial score
        score = 0;
        numClicks = 0;
        total=0;
        scoreDelta =0;
        // link layout to java
        // get roll result text
        rollResult = (TextView)findViewById(R.id.rollResult);

        // get initial roll button
        resetButton = (Button)findViewById(R.id.resetButton);

        // link score text
        scoreText = (TextView)findViewById(R.id.scoreText);

        //initialize random instance
        rand = new Random();

        // create array list container for dice
        dice = new ArrayList<Integer>();

        ImageView card1Image = (ImageView)findViewById(R.id.card1Image);
        ImageView card2Image = (ImageView)findViewById(R.id.card2Image);
        //ImageView die3Image = (ImageView)findViewById(R.id.die3Image);

        cardImageViews = new ArrayList<ImageView>();
        cardImageViews.add(card1Image);
        cardImageViews.add(card2Image);
        //diceImageViews.add(die3Image);

        // create greeting
        Toast.makeText(getApplicationContext(),"Welcome to DiceOut!",Toast.LENGTH_SHORT).show();

        /// poker stuff here
        masterDeck=new pokerDeck();
        currentDeck=masterDeck;

        card1Text = (TextView)findViewById(R.id.card1Text);
        card2Text = (TextView)findViewById(R.id.card2Text);

        handList = new ArrayList<pokerHand>();

        // set number of players
        numPlayers = 1;


    }

    public void getHands()
    {
        // for each player, create a new hand
        for(int i=0; i<numPlayers;i++)
        {
            pokerHand tmpHand=new pokerHand(currentDeck);
            handList.add(tmpHand);

        }

    }

    public void displayHand(int playerID)
    {
        for(int j=0;j<2;j++)
        {
            String tmpSuit = handList.get(playerID).getHand().get(j).getSuitStr();
            String tmpVal = handList.get(playerID).getHand().get(j).getValStr();
            String fname =  tmpSuit + ".png";
            if(j==0)
            {
                card1Text.setText(tmpVal);
            }
            else if(j==1)
            {
                card2Text.setText(tmpVal);
            }
            try
            {
                InputStream stream = getAssets().open(fname);
                Drawable d = Drawable.createFromStream(stream,null);
                cardImageViews.get(j).setImageDrawable(d);
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }

        }
    }

    public void shuffleDeck()
    {
        // set the currentDeck to fresh deck
        currentDeck=new pokerDeck();


        // clear the player hands
        handList.clear();
    }

    public void rollDice(View v)
    {
      /*  numClicks+=1;
       rollResult.setText("Clicked "+numClicks+" Times!"); */

        // 0 - 6 + 1
        /* die1 = rand.nextInt(6)+1;
        die2 = rand.nextInt(6)+1;
        die3 = rand.nextInt(6)+1;

        // update dice array list
        dice.clear();
        dice.add(die1);
        dice.add(die2);
        dice.add(die3); */

       /* for (int dieOfSet = 0; dieOfSet < 3; dieOfSet++)
        {
            String fname = "die_" + dice.get(dieOfSet) + ".png";
            try
            {
                InputStream stream = getAssets().open(fname);
                Drawable d = Drawable.createFromStream(stream,null);
                diceImageViews.get(dieOfSet).setImageDrawable(d);
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        } */

        // general rules
        // 100 * value of triples
        // 50 for doubles
        // build message
        String msg;
     /*   scoreDelta =0;
        if (die1 == die2 && die1 == die3)
        {
            // trips
            scoreDelta = die1 * 100;
            msg = "You rolled triple " + die1 +"'s! You score " + scoreDelta + " points!";
        }
        else if (die1 == die2 || die1 == die3 || die2 == die3)
        {
            scoreDelta = 50;
            msg = "You rolled doubles! You score 50 points!";
        }
        else
        {
            msg = "No points, try again!";
        }
        */

       // score = score + scoreDelta;
       // scoreText.setText("Score: "+score);
/*
        String randomValue = "Number Generated: " + num;
        Toast.makeText(getApplicationContext(),randomValue,Toast.LENGTH_SHORT).show(); */


        // poker stuff here
        // generate hands, print hand, shuffle
        getHands();
        displayHand(0);
        msg = "You were dealt: " + handList.get(0).getHand().toString();
        //update app to display result
        rollResult.setText(msg);
       // scoreText.setText(handList.get(0).toString());

        shuffleDeck();

    }

    public void resetScore(View v)
    {
        // set score to 0
        score = 0;
        scoreText.setText("");
        rollResult.setText("Let's Play!");

        // get initial dice state
       /* String fname = "die_1.png";
        try {
            InputStream stream = getAssets().open(fname);

            Drawable d = Drawable.createFromStream(stream,null);
            // set all dice to 1
            diceImageViews.get(0).setImageDrawable(d);
            diceImageViews.get(1).setImageDrawable(d);
            diceImageViews.get(2).setImageDrawable(d);
        }
        catch(IOException e){e.printStackTrace();} */

        for(int j=0;j<2;j++)
        {
            String tmpSuit = "d";
            String tmpVal = "A";
            String fname =  tmpSuit + ".png";
            if(j==0)
            {
                card1Text.setText(tmpVal);
            }
            else if(j==1)
            {
                card2Text.setText(tmpVal);
            }
            try
            {
                InputStream stream = getAssets().open(fname);
                Drawable d = Drawable.createFromStream(stream,null);
                cardImageViews.get(j).setImageDrawable(d);
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }

        }

        // poker stuff
        shuffleDeck();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
