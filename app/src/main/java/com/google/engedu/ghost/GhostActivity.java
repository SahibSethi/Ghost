package com.google.engedu.ghost;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;


public class GhostActivity extends ActionBarActivity {
    private static final String COMPUTER_TURN = "Computer's turn";
    private static final String USER_TURN = "Your turn";
    private GhostDictionary dictionary;
    private boolean userTurn = false;
    private TextView wordFragment;
    private TextView resultTextView;
    private TextView gameStatus;
    private Random random = new Random();
    private boolean finished;
    InputStream input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ghost);
        wordFragment = (TextView) findViewById(R.id.ghostText);
        resultTextView = (TextView) findViewById(R.id.resultTextView);
        gameStatus = (TextView) findViewById(R.id.gameStatus);
        try {
            input = getAssets().open("words.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            dictionary = new SimpleDictionary(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
        onStart(null);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ghost, menu);
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

    private void computerTurn() {
        TextView label = (TextView) findViewById(R.id.gameStatus);
        // Do computer turn stuff then make it the user's turn again
        String word = (String) wordFragment.getText();
        String anyWord = null;
        if(dictionary.isWord(word)){
            gameStatus.setText(R.string.valid_word);
            declareResult(false);
        }
        else{
            anyWord = dictionary.getAnyWordStartingWith(word);
            if(anyWord == null){
                onChallenge(null);
            }else{
                int len = word.length();
                String ch = anyWord.substring(len, len + 1);
                wordFragment.setText(word + ch);
            }

        }


        userTurn = true;
        label.setText(USER_TURN);
    }

    public void onChallenge(View view){
        String anyWord;
        if(!userTurn){
            declareResult(false);
        }else {
            String word = (String) wordFragment.getText();
            if (dictionary.isWord(word)) {
                declareResult(true);
            }
            else{
                anyWord = dictionary.getAnyWordStartingWith(word);
                if(anyWord != null){
                    wordFragment.setText(anyWord);
                    declareResult(false);
                }
                else{
                    declareResult(true);
                }

            }
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {

        String word;

       // if((keyCode > 64 && keyCode < 91) || (keyCode > 96 && keyCode < 123)){
            word = wordFragment.getText()+""+ ((char) event.getUnicodeChar());
            wordFragment.setText(word);
        //}

        if(dictionary.isWord(word)){
            gameStatus.setText(R.string.valid_word);
            declareResult(false);
        }
        userTurn = false;
        if(!finished){
            gameStatus.setText(USER_TURN);
            computerTurn();
        }

        return super.onKeyUp(keyCode, event);
    }

    public void declareResult(boolean userTurn){
        if(userTurn){
            resultTextView.setText("User won");
        }else{
            resultTextView.setText("Computer won");
        }
        finished = true;
    }

    /**
     * Handler for the "Reset" button.
     * Randomly determines whether the game starts with a user turn or a computer turn.
     * @param view
     * @return true
     */
    public boolean onStart(View view) {
        finished = false;
        userTurn = random.nextBoolean();
        TextView text = (TextView) findViewById(R.id.ghostText);
        resultTextView.setText("");
        text.setText("");
        TextView label = (TextView) findViewById(R.id.gameStatus);
        if (userTurn) {
            label.setText(USER_TURN);
        } else {
            label.setText(COMPUTER_TURN);
            computerTurn();
        }
        return true;
    }
}
