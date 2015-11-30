package midnight.quizapp;

import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import java.util.ArrayList;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.view.CardListView;


public class Help extends ActionBarActivity {

    String[] questions = new String[]{"My test is cancelled, what should i do?","I am getting error 300!","I am getting error 500!","Got any thing else?"};
    String[] answers = new String[]{"You had used home button or back button multiple times, which is violation of test rules, so your test has been cancelled. " +
            "If you think this is a system error. Contact your system admin or examiner","Looks like test format is not correct and this error should be on all student's device," +
            " so please contact system admin or examiner to start test correctly","This comes when there is connectivity issue, so please check your internet connection","Please contact your examiner or system admin"};
ArrayList<Card> cards = new ArrayList<>();
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        mToolbar = (Toolbar) findViewById(R.id.my_toolbar_actionbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Help");
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        for (int i=0;i<questions.length;i++)
        {
            helpCard card = new helpCard(this);
            card.qText=questions[i];
            card.aText=answers[i];
            ColorGenerator generator = ColorGenerator.DEFAULT;
            int color2 = generator.getRandomColor();
            TextDrawable drawable2 = TextDrawable.builder()
                    .buildRound(String.valueOf(i+1), color2);

            card.drawable=drawable2;
            cards.add(card);
        }

        CardArrayAdapter mCardArrayAdapter = new CardArrayAdapter(this,cards);

        CardListView list = (CardListView)findViewById(R.id.cards);
        list.setBackgroundColor(Color.parseColor("#fafafa"));
        if (list!=null){
            list.setAdapter(mCardArrayAdapter);
        }

    }



}
