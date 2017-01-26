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
class tableHand{
    private ArrayList<pokerCard> hand;
    // add cards to hand
    // this hand holds the 5 cards on the table
    public tableHand(pokerDeck d)
    {
        hand=new ArrayList<pokerCard>();
        // burn card
        pokerCard burn1=d.getCard();
        hand.add(d.getCard());
        hand.add(d.getCard());
        hand.add(d.getCard());
        // burn card
        pokerCard burn2=d.getCard();
        hand.add(d.getCard());
        //burn card
        pokerCard burn3=d.getCard();
        hand.add(d.getCard());
    }

    public ArrayList<pokerCard> getHand() {
        return hand;
    }
    public void clearHand(){
        hand.clear();
    }
    public String toString()
    {
        return(hand.get(0).toString()+" "+hand.get(1).toString()+" "+hand.get(2).toString()+" "+hand.get(3).toString()+" "+hand.get(4).toString());
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
    public void clearHand(){
        hand.clear();
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

class player{
    private pokerHand playerHand;
    private int handClass;
    public player(){
        // for each player, create a new hand
            //playerHand=new pokerHand(d);
            handClass = 0;
    }
    public void dealCards(pokerDeck d){
        playerHand = new pokerHand(d);
    }
    public pokerHand getPlayerHand() {
        return playerHand;
    }
    public void clearHand(){
        playerHand.clearHand();
    }
    public void evaluateHand(tableHand tHand, int tableState)
    {
        int[][] scoreArray=new int[13][4];

        if(tableState==0){
            // pre flop
        }
        else if(tableState==1){
            // flop
        }
        else if(tableState==2){
            // turn
        }
        else if(tableState==3){
            // river
        }
    }
}

public class MainActivity extends AppCompatActivity {

    //poker decks
    pokerDeck masterDeck;
    pokerDeck currentDeck;

    // poker hands, indices are players
   // ArrayList<pokerHand> handList;
   // ArrayList<tableHand> tableHandList;
    tableHand tablesHand;

    ArrayList<player> playerList;
    //number of players
    int numPlayers;

    // Field to hold the roll result text
    TextView rollResult;
    TextView scoreText;
    TextView card1Text;
    TextView card2Text;
    TextView tableHand1Text;;
    TextView tableHand2Text;;
    TextView tableHand3Text;;
    TextView tableHand4Text;;
    TextView tableHand5Text;

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
    ArrayList<ImageView> tableHandImageViews;

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

        ImageView tableHand1Image = (ImageView)findViewById(R.id.tableHand1Image);
        ImageView tableHand2Image = (ImageView)findViewById(R.id.tableHand2Image);
        ImageView tableHand3Image = (ImageView)findViewById(R.id.tableHand3Image);
        ImageView tableHand4Image = (ImageView)findViewById(R.id.tableHand4Image);
        ImageView tableHand5Image = (ImageView)findViewById(R.id.tableHand5Image);

        cardImageViews = new ArrayList<ImageView>();
        cardImageViews.add(card1Image);
        cardImageViews.add(card2Image);
        //diceImageViews.add(die3Image);

        // initialize table hand cards
        tableHandImageViews = new ArrayList<ImageView>();
        tableHandImageViews.add(tableHand1Image);
        tableHandImageViews.add(tableHand2Image);
        tableHandImageViews.add(tableHand3Image);
        tableHandImageViews.add(tableHand4Image);
        tableHandImageViews.add(tableHand5Image);

        // create greeting
        Toast.makeText(getApplicationContext(),"Welcome to Nail Poker!",Toast.LENGTH_SHORT).show();

        /// poker stuff here
        masterDeck=new pokerDeck();
        currentDeck=masterDeck;

        card1Text = (TextView)findViewById(R.id.card1Text);
        card2Text = (TextView)findViewById(R.id.card2Text);
        tableHand1Text = (TextView)findViewById(R.id.tableHand1Text);
        tableHand2Text = (TextView)findViewById(R.id.tableHand2Text);
        tableHand3Text = (TextView)findViewById(R.id.tableHand3Text);
        tableHand4Text = (TextView)findViewById(R.id.tableHand4Text);
        tableHand5Text = (TextView)findViewById(R.id.tableHand5Text);


        //handList = new ArrayList<pokerHand>();
        //tableHandList = new ArrayList<tableHand>();

        // set number of players
        numPlayers = 1;

        // create players and add them to a list
        playerList= new ArrayList<player>();
        for(int i=0;i<numPlayers;i++){
            player p = new player();
            playerList.add(p);
        }

    }

    // deal cards to players
    public void getHands()
    {
        // old method to deal cards. will be removed.

        /*
        // for each player, create a new hand

        for(int i=0; i<numPlayers;i++)
        {
            pokerHand tmpHand=new pokerHand(currentDeck);
            handList.add(tmpHand);

        }
*/
    }

    public int simpleAdd(int x, int y){
        return(x+y);
    }
    // deal cards to table
    public void getTableHand()
    {
        // for each player, create a new hand
            tablesHand=new tableHand(currentDeck);
          //  tableHandList.add(tmpHand);


    }

    // display table's cards - flop
    public void displayFlop()
    {
        for(int j=0;j<3;j++)
        {
            String tmpSuit = tablesHand.getHand().get(j).getSuitStr();
            String tmpVal = tablesHand.getHand().get(j).getValStr();
            String fname =  tmpSuit + ".png";

            if(j==0){ tableHand1Text.setText(tmpVal); tableHand1Text.setVisibility(View.VISIBLE);}
            else if(j==1){ tableHand2Text.setText(tmpVal); tableHand2Text.setVisibility(View.VISIBLE);}
            else if(j==2){ tableHand3Text.setText(tmpVal); tableHand3Text.setVisibility(View.VISIBLE);}
            try
            {
                InputStream stream = getAssets().open(fname);
                Drawable d = Drawable.createFromStream(stream,null);
                tableHandImageViews.get(j).setImageDrawable(d);
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }

        }
    }
    // display table's cards - turn
    public void displayTurn()
    {
        for(int j=3;j<4;j++)
        {
            String tmpSuit = tablesHand.getHand().get(j).getSuitStr();
            String tmpVal = tablesHand.getHand().get(j).getValStr();
            String fname =  tmpSuit + ".png";

            if(j==3){ tableHand4Text.setText(tmpVal); tableHand4Text.setVisibility(View.VISIBLE);}
            try
            {
                InputStream stream = getAssets().open(fname);
                Drawable d = Drawable.createFromStream(stream,null);
                tableHandImageViews.get(j).setImageDrawable(d);
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }

        }
    }
    // display table's cards - river
    public void displayRiver()
    {
        for(int j=4;j<5;j++)
        {
            String tmpSuit = tablesHand.getHand().get(j).getSuitStr();
            String tmpVal = tablesHand.getHand().get(j).getValStr();
            String fname =  tmpSuit + ".png";
            if(j==4){ tableHand5Text.setText(tmpVal); tableHand5Text.setVisibility(View.VISIBLE);}

            try
            {
                InputStream stream = getAssets().open(fname);
                Drawable d = Drawable.createFromStream(stream,null);
                tableHandImageViews.get(j).setImageDrawable(d);
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }

        }
    }
    // display players cards
    public void displayHand(int playerID)
    {
        for(int j=0;j<2;j++)
        {
            String tmpSuit = playerList.get(playerID).getPlayerHand().getHand().get(j).getSuitStr();
            String tmpVal = playerList.get(playerID).getPlayerHand().getHand().get(j).getValStr();
            String fname =  tmpSuit + ".png";
            if(j==0)
            {
                card1Text.setText(tmpVal); card1Text.setVisibility(View.VISIBLE);
            }
            else if(j==1)
            {
                card2Text.setText(tmpVal); card2Text.setVisibility(View.VISIBLE);
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
        for(int i=0;i<numPlayers;i++){
           playerList.get(i).clearHand();
            tablesHand.clearHand();

        }
    }

    public void rollDice(View v)
    {
        // build message
        String msg;


        // poker stuff here
        // generate hands, print hand, shuffle
       // getHands();
        getTableHand();
        for(int i=0;i<numPlayers;i++){
            playerList.get(i).dealCards(currentDeck);
        }

        //pre flop
        displayHand(0);

        msg = "You were dealt: " + playerList.get(0).getPlayerHand().toString();
        //update app to display result
        rollResult.setText(msg);
        // scoreText.setText(handList.get(0).toString());

        playerList.get(0).evaluateHand(tablesHand,0);
        //flop
        displayFlop();
        playerList.get(0).evaluateHand(tablesHand,1);

        //turn
        displayTurn();
        playerList.get(0).evaluateHand(tablesHand,2);

        //river
        displayRiver();
        playerList.get(0).evaluateHand(tablesHand,3);



        shuffleDeck();

    }

    public void resetScore(View v)
    {
        // set score to 0
        score = 0;
        scoreText.setText("");
        rollResult.setText("Let's Play!");

    // reset hand
        for(int j=0;j<2;j++)
        {
            String tmpSuit = "d";
            String tmpVal = "A";
            String fname =  "cardback.png";
            if(j==0) { card1Text.setVisibility(View.INVISIBLE);  }
            else if(j==1) { card2Text.setVisibility(View.INVISIBLE); }
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

        // reset table
        for(int j=0;j<5;j++)
        {
            String fname =  "cardback.png";

            if(j==0){ tableHand1Text.setVisibility(View.INVISIBLE);}
            else if(j==1){  tableHand2Text.setVisibility(View.INVISIBLE);}
            else if(j==2){ tableHand3Text.setVisibility(View.INVISIBLE);}
            else if(j==3){tableHand4Text.setVisibility(View.INVISIBLE);}
            else if(j==4){ tableHand5Text.setVisibility(View.INVISIBLE);}
            try
            {
                InputStream stream = getAssets().open(fname);
                Drawable d = Drawable.createFromStream(stream,null);
                tableHandImageViews.get(j).setImageDrawable(d);
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
