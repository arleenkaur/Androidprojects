package midnight.quizapp;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;



import it.gmariotti.cardslib.library.internal.Card;

/**
 * Created by Sid on 20-04-2015.
 */
public class TestCard extends Card {

    Context cxt;
    TextView question,number;//,a,b,c,d,number;
    String qtxt,numbertxt;//,atxt,btxt,ctxt,dtxt,numbertxt;
    Typeface font;
    boolean first=false;


    public TestCard(Context context) {
        super(context,R.layout.test_card);
        cxt=context;
        font = Typeface.createFromAsset(cxt.getAssets(), "segoeui.ttf");

    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view)
    {
        if(!first)
        {
            question = (TextView)view.findViewById(R.id.option);
            number = (TextView)view.findViewById(R.id.question_number);
            question.setTypeface(font);
            number.setTypeface(font);
            first=true;
        }

        number.setText(numbertxt);
        question.setText(qtxt);

    }


}
