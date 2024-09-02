package com.example.xo_game;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class GameActivity extends AppCompatActivity {
    TextView player1,player2,score1,score2,drawScore;
    ConstraintLayout rootElement;
    ArrayList<String>boardState;
    int counter=0,player1_score=0,player2_score=0,draw_score=0;
    String playerSympol;
    Boolean withAi;
    MediaPlayer media;
    ImageView sound_icon,noSound_icon,language_icon,back_icon;
    String []languages={"English","Arabic","German","French","Italiano"};
    Boolean sound=true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        initValues();

        //set tha language of app
        String lang=SharedPreferenceHelper.getLanguage(getApplicationContext());
        getIconAndLanguage(lang);

        //set the mode of sound
        sound=SharedPreferenceHelper.getSoundMode(getApplicationContext());
        if(sound){
            //play the sound
            media = MediaPlayer.create(GameActivity.this, R.raw.begin_of_game);
            media.start();
            sound_icon.setVisibility(View.VISIBLE);
            noSound_icon.setVisibility(View.INVISIBLE);
        }else{
            sound_icon.setVisibility(View.INVISIBLE);
            noSound_icon.setVisibility(View.VISIBLE);
        }


        //get the names of player from namesActivity
        player1.setText(getIntent().getStringExtra("firstName"));
        player2.setText(getIntent().getStringExtra("secondName"));
        withAi=getIntent().getBooleanExtra("Ai",false);

        //initialize the buttons at the beginning of game or when someone won or draw
        initBoardState();


        sound_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //play the sound
                media=MediaPlayer.create(GameActivity.this,R.raw.click);
                media.start();

                sound_icon.setVisibility(View.INVISIBLE);
                noSound_icon.setVisibility(View.VISIBLE);
                SharedPreferenceHelper.saveSoundMode(getApplicationContext(),false);
                sound=false;

            }
        });

        noSound_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sound_icon.setVisibility(View.VISIBLE);
                noSound_icon.setVisibility(View.INVISIBLE);

                SharedPreferenceHelper.saveSoundMode(getApplicationContext(),true);
                sound=true;

            }
        });

        language_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sound) {
                    //play the sound
                    media = MediaPlayer.create(GameActivity.this, R.raw.click);
                    media.start();
                }

                showLanguagePopupMenu(v);
            }
        });
        back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(GameActivity.this, HomeActivity.class);
                startActivity(i);
            }
        });



    }
    public void initValues(){
        player1=findViewById(R.id.firstPlayer);
        player2=findViewById(R.id.secondPlayer);
        score1=findViewById(R.id.score_1);
        score2=findViewById(R.id.score_2);
        drawScore=findViewById(R.id.score_draw);
        sound_icon=findViewById(R.id.sound_btn);
        noSound_icon=findViewById(R.id.noSound_btn);
        language_icon=findViewById(R.id.language_icon);
        back_icon=findViewById(R.id.back_icon);
        rootElement=findViewById(R.id.root_element);//this contains all elements in the xml , will use it for buttons

    }
    public void initBoardState(){
        boardState=new ArrayList<>();
        for(int i=0;i<=8;i++)
        {
            boardState.add("");
        }
        for(int i=0;i<=rootElement.getChildCount();i++)
        {
            View vv =rootElement.getChildAt(i);
            if(vv instanceof Button)
            {
                ((Button) vv).setText("");
                ((Button) vv).setTextColor(Color.parseColor("#FFFFFFFF"));
            }
        }
    }
    public void onPlayerClick(View v) {
        if(sound) {
            //play the sound
            media = MediaPlayer.create(GameActivity.this, R.raw.click);
            media.start();
        }

        Button vv = (Button) v;
        if (!(vv.getText().toString().isEmpty())) {
            return;
        }
        //play with ai
        if(withAi){
            if (counter % 2 == 0) { // player 1 (X) turn
                vv.setText("X");
                playerSympol = "X";
                writePlayerSympol(vv.getId(), "X");
                counter++;

                if (checkWinner("X")) {
                    player1_score += 1;
                    counter = 0;
                    playerSympol = "";
                    if (sound) {
                        media = MediaPlayer.create(GameActivity.this, R.raw.win_sound);
                        media.start();
                    }
                    initBoardState();
                } else if (counter < 9) {
                    // After player 1's move, AI (O) should make its move if playing against AI
                    if (withAi) {
                        aiMove();
                    }
                } else if (counter == 9) { // draw
                    if (sound) {
                        media = MediaPlayer.create(GameActivity.this, R.raw.draw_sound);
                        media.start();
                    }
                    counter = 0;
                    draw_score += 1;
                    playerSympol = "";
                    initBoardState();
                }
            }

        }else { //play with player

            if (counter % 2 == 0)//player 1 x turn
            {
                (vv).setText("X");
                playerSympol = "X";
            } else // player 2 y turn
            {
                vv.setTextColor(Color.parseColor("#A3F45B"));
                (vv).setText("O");
                playerSympol = "O";
            }
            writePlayerSympol(vv.getId(), playerSympol);
            counter++;

            if (checkWinner("X")) //player 1 win
            {
                Log.e("winner", "win x");
                player1_score += 1;
                counter = 0;
                playerSympol = "";
                if (sound) {
                    //play the sound
                    media = MediaPlayer.create(GameActivity.this, R.raw.win_sound);
                    media.start();
                }
                initBoardState();// reinitialize
            } else if (checkWinner("O")) //player 2 win
            {
                Log.e("winner", "win o");

                player2_score += 1;
                counter = 0;
                playerSympol = "";
                if (sound) {
                    //play the sound
                    media = MediaPlayer.create(GameActivity.this, R.raw.win_sound);
                    media.start();
                }
                initBoardState();// reinitialize

            } else if (counter == 9) // draw
            {
                if (sound) {
                    //play the sound
                    media = MediaPlayer.create(GameActivity.this, R.raw.draw_sound);
                    media.start();
                }
                counter = 0;
                draw_score += 1;
                playerSympol = "";
                initBoardState();
            }
        }

        score1.setText(String.valueOf(player1_score));
        score2.setText(String.valueOf(player2_score));
        drawScore.setText(String.valueOf(draw_score));
    }
    private void aiMove() {
        ArrayList<Integer> availableMoves = new ArrayList<>();
        for (int i = 0; i < boardState.size(); i++) {//get the available places the ai can play
            if (boardState.get(i).isEmpty()) {
                availableMoves.add(i);
            }
        }

        if (availableMoves.size() > 0) {
            int aiMoveIndex = availableMoves.get((int) (Math.random() * availableMoves.size()));//get random number
            Button aiButton = null;

            switch (aiMoveIndex) {
                case 0:
                    aiButton = findViewById(R.id.btn1);
                    break;
                case 1:
                    aiButton = findViewById(R.id.btn2);
                    break;
                case 2:
                    aiButton = findViewById(R.id.btn3);
                    break;
                case 3:
                    aiButton = findViewById(R.id.btn4);
                    break;
                case 4:
                    aiButton = findViewById(R.id.btn5);
                    break;
                case 5:
                    aiButton = findViewById(R.id.btn6);
                    break;
                case 6:
                    aiButton = findViewById(R.id.btn7);
                    break;
                case 7:
                    aiButton = findViewById(R.id.btn8);
                    break;
                case 8:
                    aiButton = findViewById(R.id.btn9);
                    break;
            }

            if (aiButton != null) {
                // AI always plays "O"
                aiButton.setText("O");
                aiButton.setTextColor(Color.parseColor("#A3F45B"));
                writePlayerSympol(aiButton.getId(), "O");
                counter++;

                // Check if AI (O) wins after making the move
                if (checkWinner("O")) {
                    player2_score += 1;
                    counter = 0;
                    playerSympol = "";
                    if (sound) {
                        media = MediaPlayer.create(GameActivity.this, R.raw.win_sound);
                        media.start();
                    }
                    initBoardState();
                } else if (counter == 9) { // draw
                    if (sound) {
                        media = MediaPlayer.create(GameActivity.this, R.raw.draw_sound);
                        media.start();
                    }
                    counter = 0;
                    draw_score += 1;
                    playerSympol = "";
                    initBoardState();
                }


            }
        }
    }

    private void writePlayerSympol(int id,String sympol)
    {
        if(id==R.id.btn1)
        {
            boardState.set(0,sympol);

        }
        else if(id==R.id.btn2)
        {
            boardState.set(1,sympol);
        }
        else if(id==R.id.btn3)
        {
            boardState.set(2,sympol);
        }
        else if(id==R.id.btn4)
        {
            boardState.set(3,sympol);
        }
        else if(id==R.id.btn5)
        {
            boardState.set(4,sympol);
        }
        else if(id==R.id.btn6)
        {
            boardState.set(5,sympol);
        }
        else if(id==R.id.btn7)
        {
            boardState.set(6,sympol);
        }
        else if(id==R.id.btn8)
        {
            boardState.set(7,sympol);
        }
        else if(id==R.id.btn9)
        {
            boardState.set(8,sympol);
        }
    }

    public  Boolean checkWinner(String sympol)
    {

        for(int i=0;i<=8;i+=3)//check horizontal
        {
            if(boardState.get(i).equals(sympol)&&boardState.get(i+1).equals(sympol)&&boardState.get(i+2).equals(sympol))
            {
                return true;
            }
        }
        for(int i=0;i<=2;i++)//check vertical
        {
            if(boardState.get(i).equals(sympol)&&boardState.get(i+3).equals(sympol)&&boardState.get(i+6).equals(sympol))
            {
                return true;
            }
        }
        //check diagonal
        if(boardState.get(0).equals(sympol)&&boardState.get(4).equals(sympol)&&boardState.get(8).equals(sympol))return true;
        if(boardState.get(2).equals(sympol)&&boardState.get(4).equals(sympol)&&boardState.get(6).equals(sympol))return true;

        return false;

    }
    public void showLanguagePopupMenu(View v){
        PopupMenu popupMenu = new PopupMenu(GameActivity.this, v);
        for (int i = 0; i < languages.length; i++) {
            popupMenu.getMenu().add(0, i, i, languages[i]);
        }

        popupMenu.setOnMenuItemClickListener(item -> {
            String selectedLanguage = languages[item.getItemId()];
            Toast.makeText(GameActivity.this, "Selected: " + selectedLanguage, Toast.LENGTH_SHORT).show();
            // Handle language selection here
            switch (selectedLanguage) {
                case "French":
                    setLocale("fr");
                    break;
                case "German":
                    setLocale("de");
                    break;
                case "Arabic":
                    setLocale("ar");

                    break;
                case "Italiano":
                    setLocale("it");
                    break;
                default:
                    setLocale("en"); // Default to English
                    break;
            }
            return true;
        });

        popupMenu.show();
    }
    public void setLocale(String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.setLocale(locale);
        getBaseContext().getResources().updateConfiguration(config,getBaseContext().getResources().getDisplayMetrics());

        // Store the language preference
        SharedPreferenceHelper.saveLanguage(getBaseContext(),languageCode);

        //change the icon of language
        switch (languageCode) {
            case "fr":
                language_icon.setImageResource(R.drawable.french_icon);
                break;
            case "de":
                language_icon.setImageResource(R.drawable.german_icon);
                break;
            case "ar":
                language_icon.setImageResource(R.drawable.arabic_icon);
                break;
            case "it":
                language_icon.setImageResource(R.drawable.italy_icon);
                break;
            default:
                // Default to English
                language_icon.setImageResource(R.drawable.english_icon);
                break;
        }

        // Restart the activity to apply the language change
        Intent refresh =getIntent();
        finish();
        startActivity(refresh);


    }
    public void getIconAndLanguage(String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.setLocale(locale);
        getApplicationContext().getResources().updateConfiguration(config,getApplicationContext().getResources().getDisplayMetrics());


        //change the icon of language
        switch (languageCode) {
            case "fr":
                language_icon.setImageResource(R.drawable.french_icon);
                break;
            case "de":
                language_icon.setImageResource(R.drawable.german_icon);
                break;
            case "ar":
                language_icon.setImageResource(R.drawable.arabic_icon);
                break;
            case "it":
                language_icon.setImageResource(R.drawable.italy_icon);
                break;
            default:
                // Default to English
                language_icon.setImageResource(R.drawable.english_icon);
                break;
        }

    }



}