package midnight.quizapp;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.afollestad.materialdialogs.MaterialDialog;
import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.melnykov.fab.FloatingActionButton;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardGridArrayAdapter;
import it.gmariotti.cardslib.library.view.CardGridView;


public class TestActivity extends ActionBarActivity {

    String answer="";
    int cur=0;
    boolean first;
    CardGridArrayAdapter mCardArrayAdapter;
    ArrayList<String> questions = new ArrayList<>();
    ArrayList<String> result = new ArrayList<>();
    ArrayList<String>a = new ArrayList<>();
    ArrayList<String>b = new ArrayList<>();
    ArrayList<String>c = new ArrayList<>();
    ArrayList<String>d = new ArrayList<>();
    String path;
    String[] answers;
    boolean noStop=false;
    TextView timer;
    Timer myTimer;
    String time1 = "00:19:00",id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);


        Intent i = getIntent();
        id=i.getStringExtra("id");
        path=i.getStringExtra("path");
        time1 =i.getStringExtra("time");

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        timer=(TextView)findViewById(R.id.timer);
        myTimer = new Timer();
        MyTimerTask myTimerTask = new MyTimerTask();

        myTimer.schedule(myTimerTask, 1000,1000);


        new xmlParser().execute();

        first=true;

    }

    void set()
    {

            answer = answers[cur];

        if(first)
        {
            FloatingActionButton next,prev;


            next = (FloatingActionButton)findViewById(R.id.next);
            prev=(FloatingActionButton)findViewById(R.id.prev);
            next.setColorNormal(getResources().getColor(R.color.pink));
            prev.setColorNormal(getResources().getColor(R.color.pink));
            next.setColorPressed(getResources().getColor(R.color.pink_pressed));
            prev.setColorPressed(getResources().getColor(R.color.pink_pressed));

        }

        final TestCard cardA = new TestCard(this);
        final TestCard cardD = new TestCard(this);
        final TestCard cardB = new TestCard(this);
        final TestCard cardC = new TestCard(this);


        ColorGenerator generator = ColorGenerator.DEFAULT; // or use DEFAULT

        int color2 = generator.getRandomColor();
        TextDrawable drawable2 = TextDrawable.builder()
                .buildRound(String.valueOf(cur+1), color2);

        TextView tv = (TextView)findViewById(R.id.question);
        ImageView number = (ImageView)findViewById(R.id.number);
        Typeface font = Typeface.createFromAsset(getAssets(), "segoeui.ttf");
        tv.setTypeface(font);
        number.setImageDrawable(drawable2);
        tv.setText(questions.get(cur));


        ArrayList<Card> cards = new ArrayList<>();




        cardA.numbertxt="A";
        cardA.qtxt=a.get(cur);

        cardA.setBackgroundColorResourceId(R.color.card_grey);


        cardA.setOnClickListener(new Card.OnCardClickListener() {
            @Override
            public void onClick(Card card, View view) {
                cardA.setBackgroundColorResourceId(R.color.green);
                cardB.setBackgroundColorResourceId(R.color.card_grey);
                cardC.setBackgroundColorResourceId(R.color.card_grey);
                cardD.setBackgroundColorResourceId(R.color.card_grey);
                answer="a";

                mCardArrayAdapter.notifyDataSetChanged();

            }
        });


        cardB.numbertxt="B";
        cardB.qtxt=b.get(cur);
        cardB.setBackgroundColorResourceId(R.color.card_grey);
        cardB.setOnClickListener(new Card.OnCardClickListener() {
            @Override
            public void onClick(Card card, View view) {
                cardB.setBackgroundColorResourceId(R.color.green);
                cardA.setBackgroundColorResourceId(R.color.card_grey);
                cardC.setBackgroundColorResourceId(R.color.card_grey);
                cardD.setBackgroundColorResourceId(R.color.card_grey);
                answer="b";
                mCardArrayAdapter.notifyDataSetChanged();

            }
        });



        cardC.numbertxt="C";
        cardC.setBackgroundColorResourceId(R.color.card_grey);
        cardC.qtxt=c.get(cur);
        cardC.setOnClickListener(new Card.OnCardClickListener() {
            @Override
            public void onClick(Card card, View view) {
                cardC.setBackgroundColorResourceId(R.color.green);
                cardA.setBackgroundColorResourceId(R.color.card_grey);
                cardB.setBackgroundColorResourceId(R.color.card_grey);
                cardD.setBackgroundColorResourceId(R.color.card_grey);
                answer="c";
                mCardArrayAdapter.notifyDataSetChanged();

            }
        });


        cardD.numbertxt="D";
        cardD.qtxt=d.get(cur);
        cardD.setBackgroundColorResourceId(R.color.card_grey);
        cardD.setOnClickListener(new Card.OnCardClickListener() {
            @Override
            public void onClick(Card card, View view) {
                cardD.setBackgroundColorResourceId(R.color.green);
                cardA.setBackgroundColorResourceId(R.color.card_grey);
                cardB.setBackgroundColorResourceId(R.color.card_grey);
                cardC.setBackgroundColorResourceId(R.color.card_grey);
                answer="d";
                mCardArrayAdapter.notifyDataSetChanged();

            }
        });



        if (!answer.equals(""))
        {


            switch (answer)
            {
                case "a" :
                    cardA.setBackgroundColorResourceId(R.color.green);
                    break;
                case "b" :
                    cardB.setBackgroundColorResourceId(R.color.green);
                    break;
                case "c" :
                    cardC.setBackgroundColorResourceId(R.color.green);
                    break;
                case "d" :
                    cardD.setBackgroundColorResourceId(R.color.green);
                    break;
            }
        }


        cards.add(cardA);
        cards.add(cardB);
        cards.add(cardC);
        cards.add(cardD);




        mCardArrayAdapter = new CardGridArrayAdapter(this,cards);

        CardGridView gridView = (CardGridView)findViewById(R.id.AnswerGrid);
        if (gridView!=null){
            gridView.setAdapter(mCardArrayAdapter);
        }





    }


    public void Next(View v)
    {


       if(cur+1== questions.size()) ///Check for the last question
        {
            new MaterialDialog.Builder(this)
                    .title("Submit")
                    .content("This is last question, would you like to submit the test?")
                    .positiveText("Yes")
                    .negativeText("No")
                    .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {

                        Result();

                    } }).show();


        }

       else {

            YoYo.with(Techniques.SlideInRight)
                    .duration(400)
                    .playOn(findViewById(R.id.question));
            YoYo.with(Techniques.SlideInRight)
                    .duration(400)
                    .playOn(findViewById(R.id.number));
            YoYo.with(Techniques.SlideInRight)
                    .duration(400)
                    .playOn(findViewById(R.id.AnswerGrid));


            answers[cur]=answer;


            answer="";
            cur++;
            set();
       }

       //  Toast.makeText(this,"This is last question",Toast.LENGTH_LONG).show();


    }


    public void Previous(View v)
    {



        if(cur==0)
            Toast.makeText(this,"This is first question",Toast.LENGTH_LONG).show();
        else
        {
            YoYo.with(Techniques.SlideInLeft)
                    .duration(400)
                    .playOn(findViewById(R.id.question));
            YoYo.with(Techniques.SlideInLeft)
                    .duration(400)
                    .playOn(findViewById(R.id.number));
            YoYo.with(Techniques.SlideInLeft)
                    .duration(400)
                    .playOn(findViewById(R.id.AnswerGrid));

                answers[cur]=answer;

            answer="";
            cur--;
            set();

        }


    }




    class xmlParser extends AsyncTask<String,Integer,String>
    {
        boolean error=false;
        MaterialDialog mProgressDialog;


        @Override
        protected String doInBackground(String... strings) {
            error=false;
            try{
                publishProgress(0,100);

                File file = new File(path);

                DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
                Document doc = documentBuilder.parse(file);

                doc.getDocumentElement().normalize();

                NodeList nodeList = doc.getElementsByTagName("Question");


                for (int temp = 0; temp < nodeList.getLength(); temp++) {
                    Node node = nodeList.item(temp);

                    if (node.getNodeType() == Node.ELEMENT_NODE) {

                        Element question = (Element) node;


                        questions.add(question.getElementsByTagName("Q").item(0).getTextContent());
                        result.add(question.getElementsByTagName("answer").item(0) .getTextContent());
                        a.add( question.getElementsByTagName("a").item(0) .getTextContent());
                        b.add(question.getElementsByTagName("b").item(0) .getTextContent());
                        c.add(question.getElementsByTagName("c").item(0) .getTextContent());
                        d.add(question.getElementsByTagName("d").item(0) .getTextContent());


                    }}



                answers = new String[questions.size()];
                fill();
            }catch (Exception e )
            {
                error=true;
                Log.e("Error",e.toString());
            }


            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            if(values.length==2) {
                mProgressDialog= new MaterialDialog.Builder(TestActivity.this)
                        .title("Please wait...").progress(true,values[1]).cancelable(false)
                        .content("Please wait while loading test")
                        .show();

            }

        }

        @Override
        protected void onPostExecute(String o) {
            if (!error)
            {
              if(mProgressDialog.isShowing())
               mProgressDialog.dismiss();
                set();

            }
            else
            {
                if(mProgressDialog.isShowing())
                    mProgressDialog.dismiss();

                new MaterialDialog.Builder(TestActivity.this)
                        .title("Error")
                        .content("There is some error occurred while starting test.")
                        .cancelable(false)
                        .positiveText("ok")
                        .show();
            }

        }
    }

    class MyTimerTask extends TimerTask {
        long difference;

        @Override
        public void run() {

            Calendar calendar = Calendar.getInstance();
             SimpleDateFormat simpleDateFormat =
                    new SimpleDateFormat("HH:mm:ss");

            final String strDate = simpleDateFormat.format(calendar.getTime());



            try {

                Date date1 = simpleDateFormat.parse(time1);
                Date date2 = simpleDateFormat.parse(strDate); // Current time

                difference = date1.getTime() - date2.getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }

            runOnUiThread(new Runnable(){

                @Override
                public void run() {
                    difference = difference/60000;
                    if (difference==0)
                        Result();


                    timer.setText(String.valueOf(difference)+" Mins");
                }});
        }

    }

    void Result()
    {


        myTimer.cancel();
        int marks=0;
        int wrong=0;

        for (int i =0; i<answers.length;i++)
        {

            if(result.get(i).equalsIgnoreCase(answers[i]))
                marks=marks+1;
            else
                wrong=wrong+1;

            Log.v("Marks","Answer is "+answers[i]+" Result is "+result.get(i));

        }
        noStop=true;
        Intent intent = new Intent(TestActivity.this,Result.class);
        intent.putExtra("total",String.valueOf(result.size()));
        intent.putExtra("marks",String.valueOf(marks));
        intent.putExtra("id",id);
        intent.putExtra("time",time1);
        startActivity(intent);
        finish();
    }


    void  fill()
    {
        for (int i =0; i<answers.length;i++)
        {
            answers[i]="";
        }
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        if (!noStop)
            {
                Intent i = new Intent(this,Cancelled.class);
                startActivity(i);
                finish();
            }

    }


    public void submit(View v)
    {
        new MaterialDialog.Builder(this)
                .title("Submit")
                .content("This is last question, would you like to submit the test?")
                .positiveText("Yes")
                .negativeText("No")
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {

                        Result();

                    } }).show();
    }

}




