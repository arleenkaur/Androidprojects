package midnight.quizapp;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import it.gmariotti.cardslib.library.internal.Card;

/**
 * Created by Arleen on 27-04-2015.
 */
public class helpCard extends Card {
    Context cxt;
    TextView question,answer;
    String qText,aText;
    ImageView number;
    Drawable drawable;

    public helpCard(Context context) {
        super(context, R.layout.help_card);
        cxt=context;
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {
        question=(TextView)view.findViewById(R.id.question);
        answer=(TextView)view.findViewById(R.id.answer);
        question.setText(qText);
        answer.setText(aText);
        number=(ImageView)view.findViewById(R.id.number);
        number.setImageDrawable(drawable);



    }

}
