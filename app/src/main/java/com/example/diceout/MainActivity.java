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
import java.util.Arrays;
import java.util.Random;
import java.util.Stack;
import java.util.StringTokenizer;

/*
      basic class for a poker card
      value is 2-14 (14=ace, 13=king, 12=queen, 11=jack
      suit values are set as clubs==1, spades==2, hearts==3, diamonds==4
      when acessing the values later, card value needs to be reduced by 2
      since java addressing starts at 0. Suit value should be reduced by1
      is stored primarily in array lists located in pokerHand and tableHand
 */
class pokerCard {
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

/*
    tableHands represents the 5 neutral cards on a texas holdem table
    the 5 cards are stored in an array list
    when created, the cards are dealt using standard poker form, "burning" a card
    between deals

 */
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

    // clear hand array when finished
    public void clearHand(){
        hand.clear();
    }

    // simple string printing, probably not actively used besides debugging
    public String toString()
    {
        return(hand.get(0).toString()+" "+hand.get(1).toString()+" "+hand.get(2).toString()+" "+hand.get(3).toString()+" "+hand.get(4).toString());
    }
}

/*
    pokerHand refers to the 2 card hand available to a player
    the two cards are stored in an array list
 */
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

    // clear array list when finished with a hand
    public void clearHand(){
        hand.clear();
    }

    // simple to string for debugging purposes
    public String toString()
    {
        return(hand.get(0).toString()+" "+hand.get(1).toString());
    }
}

/*
    pokerDeck refers to the total set of cards before/during dealing
    initially 52, the deck is passed as a variable to pokerHand and tableHand constructors
    which proceed to remove entries when "dealing" or "burning" a card
    the deck itself is initially a 52 entry array list
 */
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

    //returns card when requested and removes it from the deck
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

/*
    The player class holds information for a given user
    They have a playerHand, 2 cards which is the pokerHand type above
    Currently only decoding the "class" of hand is implemented, we can tell what type of
    hand it is, but not the exact value. That is next on the to do list

    the arraylist subsets is used to hold the various combinations of cards, basically the result
    from an N Choose K algorithm (7 cards total, pick 5). We assign each hand a class type and
    store the best result in handClass variable

 */
class player{
    private pokerHand playerHand;
    private int handClass;
    private String[] handClassStr={"Straight Flush","4 of a Kind","Full House","Flush","Straight","3 of a Kind","Two Pair","Pair","High Card"};
    private ArrayList<int[]> subsets;
    public player(){
        // for each player, create a new hand
            //playerHand=new pokerHand(d);
            handClass = 0;
            subsets= new ArrayList<int[]>();
    }

    // read the hand class
    public String getHandClassStr()
    {
        // the class str list is backwards. subtract from 9 for now
        return(handClassStr[9-handClass]);
    }
    // set the player's hand
    public void dealCards(pokerDeck d){playerHand = new pokerHand(d);}

    public pokerHand getPlayerHand() {
        return playerHand;
    }

    // clear the list when finished with a hand, reset its value to 0
    public void clearHand(){
        playerHand.clearHand();
        handClass=0;
    }

    /*
        This is the function to check what kind of hand the player has. Given 7 cards, which 5
        represent the highest value.
        To evaluate, we use a 13 x 4 int array (card value by suit value). Each card in a
        possible hand combination is set to "1" in the array. This allows us to simply
        sum across rows/columns to find results. E.g. a flush will have a column sum of "5",
        because all 5 cards will be in that column.
     */
    public void evaluateHand(tableHand tHand, int tableState)
    {
        ArrayList<pokerCard> totalCards = new ArrayList<pokerCard>();
        //add players cards to total hand
        totalCards.add(this.playerHand.getHand().get(0));
        totalCards.add(this.playerHand.getHand().get(1));
        // add table cards to total hand
        totalCards.add(tHand.getHand().get(0));
        totalCards.add(tHand.getHand().get(1));
        totalCards.add(tHand.getHand().get(2));
        totalCards.add(tHand.getHand().get(3));
        totalCards.add(tHand.getHand().get(4));

        // table state represents the state of the board
        // it can be preflop, flop, turn, or river
        if(tableState==0){
            // pre flop
        }
        else if(tableState==1){
            // flop
            int[] input={0,1,2,3,4};
            int k = 5;
            int n=input.length;

            getSubsetIndices(input,k);
        }
        else if(tableState==2){
            // turn
            int[] input={0,1,2,3,4,5};
            int k = 5;
            int n=input.length;

            getSubsetIndices(input,k);
        }
        else if(tableState==3){
            // river
            int[] input={0,1,2,3,4,5,6};
            int k = 5;
            int n=input.length;

            getSubsetIndices(input,k);

            //-------------------------------
        }

        // Now that we have a list of possible hand subsets we can go into
        // the actual card values and find the base class of the hand
        // handClass == 1 == High Card
        // handClass == 2 == Pair
        // handClass == 3 == 2 Pair
        // handClass == 4 == 3 of a kind
        // handClass == 5 == Straight
        // handClass == 6 == Flush
        // handClass == 7 == Full House
        // handClass == 8 == 4 of a kind
        // handClass == 9 == Straight Flush

        int[][] scoreArray=new int[13][4];
        for(int i=0;i<subsets.size();i++){
            int[] ind=subsets.get(i);
            for(int j=0;j<5;j++){
                // values are offset by 2 to initially make more mental sense
                // i.e. value == 2 == "2 card", value == 14 == ace
                // suits are 1-4, so decrement by 1
                int tmpVal = totalCards.get(ind[j]).getVal()-2;
                int tmpSuit= totalCards.get(ind[j]).getSuit()-1;
                scoreArray[tmpVal][tmpSuit]=1;
            }
            int[] colScore = new int[4];
            int[] rowScore = new int[13];

            colScore = sumCol(scoreArray);
            rowScore = sumRow(scoreArray);
            boolean isFlush = checkFlush(colScore);
            boolean isStraight = checkStraight(rowScore);
            boolean is4OfKind = check4OfKind(rowScore);
            boolean is3OfKind = check3OfKind(rowScore);
            boolean isFullHouse = checkFullHouse(rowScore);
            boolean is2Pair = check2Pair(rowScore);
            boolean isPair = checkPair(rowScore);
            int newhandClass=0;

            // super simplified way to determine what class a hand is
            // this will need expanded upon next with actual card values
            if(isFlush==true && isStraight==true)
                newhandClass=9;
            else if(is4OfKind==true)
                newhandClass=8;
            else if(isFullHouse==true)
                newhandClass=7;
            else if(isFlush == true)
                newhandClass=6;
            else if(isStraight==true)
                newhandClass=5;
            else if(is3OfKind==true)
                newhandClass=4;
            else if(is2Pair==true)
                newhandClass=3;
            else if(isPair==true)
                newhandClass=2;
            else
                newhandClass=1;

            // if this subset is the best hand, set it to be the hand class
            if(newhandClass>handClass) {
                handClass = newhandClass;
                //NEED TO ADD REFERENCE TO ACTUAL HAND HERE
            }
        }
        subsets.clear();
    }
    // check for a flush draw
    public boolean checkFlush(int[] colScore){
        boolean res = false;
        for(int i=0; i<4;i++){
            if(colScore[i]==5){
                res=true;
                break;
            }
        }
        return(res);
    }
    // check for a 4 of a kind draw
    public boolean check4OfKind(int[] rowScore){
        boolean res = false;
        for(int i=0; i<13;i++){
            if(rowScore[i]==4){
                res=true;
                break;
            }
        }
        return(res);
    }
    // check for a fullhouse draw
    public boolean checkFullHouse(int[] rowScore){
        boolean res = false;
        boolean set = false;
        boolean pair= false;
        for(int i=0; i<13;i++){
            if(rowScore[i]==3){
                set=true;
            }
            else if(rowScore[i]==2){
                pair=true;
            }
            if(set==true && pair==true){
                res=true;
                break;
            }
        }
        return(res);
    }
    // check for a 3of a kind draw
    public boolean check3OfKind(int[] rowScore){
        boolean res = false;
        for(int i=0; i<13;i++){
            if(rowScore[i]==3){
                res=true;
                break;
            }
        }
        return(res);
    }
    // check for a 2pair draw
    public boolean check2Pair(int[] rowScore){
        boolean res = false;
        boolean pair1= false;
        boolean pair2= false;
        for(int i=0; i<13;i++){
            if(rowScore[i]==2){
                if(pair1==false){
                    pair1=true;
                }
                else{
                    pair2=true;
                }
            }
            if(pair1==true && pair2==true){
                res=true;
                break;
            }
        }
        return(res);
    }
    // check for a pair draw
    public boolean checkPair(int[] rowScore){
        boolean res = false;
        for(int i=0; i<13;i++){
            if(rowScore[i]==2){
                res=true;
                break;
            }
        }
        return(res);
    }
    // check for a straight draw
    public boolean checkStraight(int[] rowScore){
        boolean res = false;
        for(int i=0; i<9;i++){
            if (rowScore[i]>0 && rowScore[i+1]>0 && rowScore[i+2]>0 && rowScore[i+3]>0 && rowScore[i+4]>0) {
                res=true;
                break;
            }
        }
        // special case, low ace straight
        if(rowScore[12]>0 && rowScore[0]>0 && rowScore[1]>0 && rowScore[2]>0 && rowScore[3]>0){
            res=true;
        }
        return(res);
    }

    // function to sum the columns of score array
    public int[] sumCol(int[][] input){
        int[] res = new int[4];
        for(int i=0;i<4;i++){
            for(int j=0;j<13;j++){
                res[i]=res[i]+input[j][i];
            }
        }

        return(res);
    }
    // function to sum the rows of score array
    public int[] sumRow(int[][] input){
        int[] res = new int[13];
        for(int i=0;i<13;i++){
            for(int j=0;j<4;j++){
                res[i]=res[i]+input[i][j];
            }
        }

        return(res);
    }

    /*
        This function peforms a N Choose K algorithm. It adds the possible combinations
        of cards to the list subsets
     */
    public void getSubsetIndices(int[] input,int k){
        int[] s = new int[k];
        if (k <= input.length) {

            for(int i=0;i<k;i++){
                s[i]=i;
            }

            // first index sequence: 0, 1, 2, ...
            // for loop, sets s array to 0 to k-1
            for (int i = 0; (s[i] = i) < k - 1; i++);
            // get the actual values from input since we are tracking indices
            subsets.add(getSubset(input, s));

            while(true) {
                int i;
                // find position of item that can be incremented
                // initially i = last position in new array
                // if the last position == full array -1 (last indice) then move to next lower index
                // if we run out of indices, we are done totally. break the while loop
                //
                // Had assistance from stack overflow on this one. Interesting use of a for loop
                // that I will remember to make use of in the future
                for (i = k - 1; i >= 0 && s[i] == input.length - k + i; i--);

                if (i < 0) {
                    break;
                }
                else {
                    // increment last position possible
                    s[i]++;
                    // fill up the following indices in an incremental fashion
                    // we pre increment initially so it fails if we are already at the last position
                    for (++i; i < k; i++) {
                        s[i] = s[i - 1] + 1;
                    }
                    // get the actual subset, we are working with indices here
                    subsets.add(getSubset(input, s));
                }
            }
        }
    }
    // generate actual subset by index sequence
    int[] getSubset(int[] input, int[] subset) {
        int[] result = new int[subset.length];
        for (int i = 0; i < subset.length; i++)
            result[i] = input[subset[i]];
        return result;
    }

}

/*
    Main Activity, created by android studio initially. Sets up the basic values
 */
public class MainActivity extends AppCompatActivity {

    //poker decks
    pokerDeck masterDeck;
    pokerDeck currentDeck;

    //poker hands, indices are players
    //ArrayList<pokerHand> handList;
    //ArrayList<tableHand> tableHandList;
    tableHand tablesHand;

    ArrayList<player> playerList;
    //number of players
    int numPlayers;

    // Field to hold the result text
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
    //Random rand;



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
        // created by android studio
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
        //rand = new Random();

        ImageView card1Image = (ImageView)findViewById(R.id.card1Image);
        ImageView card2Image = (ImageView)findViewById(R.id.card2Image);

        ImageView tableHand1Image = (ImageView)findViewById(R.id.tableHand1Image);
        ImageView tableHand2Image = (ImageView)findViewById(R.id.tableHand2Image);
        ImageView tableHand3Image = (ImageView)findViewById(R.id.tableHand3Image);
        ImageView tableHand4Image = (ImageView)findViewById(R.id.tableHand4Image);
        ImageView tableHand5Image = (ImageView)findViewById(R.id.tableHand5Image);

        cardImageViews = new ArrayList<ImageView>();
        cardImageViews.add(card1Image);
        cardImageViews.add(card2Image);

        // initialize table hand cards
        tableHandImageViews = new ArrayList<ImageView>();
        tableHandImageViews.add(tableHand1Image);
        tableHandImageViews.add(tableHand2Image);
        tableHandImageViews.add(tableHand3Image);
        tableHandImageViews.add(tableHand4Image);
        tableHandImageViews.add(tableHand5Image);

        // create greeting
        Toast.makeText(getApplicationContext(),"Welcome to Nail Poker!",Toast.LENGTH_SHORT).show();

        /// create deck here
        masterDeck=new pokerDeck();
        currentDeck=masterDeck;

        // assign strings, get them by id given in xml
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

        /*
            We aren't evaluatin the different phases seperately yet.
            Eventually we will, but now focus on the river
         */

        // scoreText.setText(handList.get(0).toString());
      //  playerList.get(0).evaluateHand(tablesHand,0);
        //flop
        displayFlop();
      //  playerList.get(0).evaluateHand(tablesHand,1);

        //turn
        displayTurn();
       // playerList.get(0).evaluateHand(tablesHand,2);

        //river
        displayRiver();
        playerList.get(0).evaluateHand(tablesHand,3);


        msg = "Your best hand is a : " + playerList.get(0).getHandClassStr();
        //update app to display result
        rollResult.setText(msg);

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
