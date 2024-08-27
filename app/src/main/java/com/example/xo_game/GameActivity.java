package com.example.xo_game;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class GameActivity extends AppCompatActivity {
    TextView player1,player2,score1,score2,drawScore;
    ConstraintLayout rootElement;
    ImageView exit;
    ArrayList<String>boardState;
    int counter=0,player1_score=0,player2_score=0,draw_score=0;
    String playerSympol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        initValues();
        //get the names of player from namesActivity
        player1.setText(getIntent().getStringExtra("firstName"));
        player2.setText(getIntent().getStringExtra("secondName"));

        //initialize the buttons at the beginning of game or when someone won or draw
        initBoardState();

        //return to homeActivity
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(GameActivity.this,HomeActivity.class);
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
        exit=findViewById(R.id.exit);
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
            }
        }
    }
    public void onPlayerClick(View v) //error
    {
        Log.e("button",""+counter);

        if (v instanceof Button) {
           Button vv = (Button) v;
            if (!(vv.getText().toString().isEmpty())) {
                return;
            }
            if (counter % 2 == 0)//player 1 x turn
            {
                (vv).setText("X");
                playerSympol = "X";
            } else // player 2 y turn
            {
                (vv).setText("O");
                playerSympol = "O";
            }
            writePlayerSympol(vv.getId(), playerSympol);
            counter++;

            if (checkWinner("X")) //player 1 win
            {
                player1_score += 1;
                counter = 0;
                initBoardState();// reinitialize
                playerSympol = "";
            } else if (checkWinner("O")) //player 2 win
            {
                player2_score += 1;
                counter = 0;
                initBoardState();// reinitialize
                playerSympol = "";

            } else if (counter == 9) // draw
            {
                counter = 0;
                initBoardState();
                draw_score += 1;
                playerSympol = "";
            }
        }
        score1.setText(player1_score);
        score2.setText(player2_score);
        drawScore.setText(draw_score);
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
}