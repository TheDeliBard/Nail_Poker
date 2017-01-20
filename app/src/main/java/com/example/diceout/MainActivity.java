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

public class MainActivity extends AppCompatActivity {

    // Field to hold the roll result text
    TextView rollResult;
    TextView scoreText;

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
    ArrayList<ImageView> diceImageViews;

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

        ImageView die1Image = (ImageView)findViewById(R.id.die1Image);
        ImageView die2Image = (ImageView)findViewById(R.id.die2Image);
        ImageView die3Image = (ImageView)findViewById(R.id.die3Image);

        diceImageViews = new ArrayList<ImageView>();
        diceImageViews.add(die1Image);
        diceImageViews.add(die2Image);
        diceImageViews.add(die3Image);

        // create greeting
        Toast.makeText(getApplicationContext(),"Welcome to DiceOut!",Toast.LENGTH_SHORT).show();
    }

    public void rollDice(View v)
    {
      /*  numClicks+=1;
       rollResult.setText("Clicked "+numClicks+" Times!"); */

        // 0 - 6 + 1
        die1 = rand.nextInt(6)+1;
        die2 = rand.nextInt(6)+1;
        die3 = rand.nextInt(6)+1;

        // update dice array list
        dice.clear();
        dice.add(die1);
        dice.add(die2);
        dice.add(die3);

        for (int dieOfSet = 0; dieOfSet < 3; dieOfSet++)
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
        }

        // general rules
        // 100 * value of triples
        // 50 for doubles
        // build message
        String msg;
        scoreDelta =0;
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

        //update app to display result
        rollResult.setText(msg);
        score = score + scoreDelta;
        scoreText.setText("Score: "+score);
/*
        String randomValue = "Number Generated: " + num;
        Toast.makeText(getApplicationContext(),randomValue,Toast.LENGTH_SHORT).show(); */

    }

    public void resetScore(View v)
    {
        // set score to 0
        score = 0;
        scoreText.setText("Score: 0");
        rollResult.setText("Let's Play!");

        // get initial dice state
        String fname = "die_1.png";
        try {
            InputStream stream = getAssets().open(fname);

            Drawable d = Drawable.createFromStream(stream,null);
            // set all dice to 1
            diceImageViews.get(0).setImageDrawable(d);
            diceImageViews.get(1).setImageDrawable(d);
            diceImageViews.get(2).setImageDrawable(d);
        }
        catch(IOException e){e.printStackTrace();}

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
