package midnight.quizapp;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import it.gmariotti.cardslib.library.internal.Card;

/**
 * Created by Sid on 24-04-2015.
 */
public class upcoming_card extends Card {
    Context cxt;
    TextView title, content,date;
    ImageView image;
    String titleTxt,contentTxt,dateTxt;
    Drawable drawable;
    Typeface font;
    boolean first=false;




    public upcoming_card(Context context) {
        super(context, R.layout.upcoming_card);
        cxt=context;
        font = Typeface.createFromAsset(cxt.getAssets(), "segoeui.ttf");

    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view)
    {
        if(!first)
        {
            title=(TextView)view.findViewById(R.id.title);
            title.setTypeface(font);
            content=(TextView)view.findViewById(R.id.content);
            content.setTypeface(font);
            date=(TextView)view.findViewById(R.id.date);
            date.setTypeface(font);
            image=(ImageView)view.findViewById(R.id.imageView);
            first=true;

        }


        title.setText(titleTxt);
        content.setText(contentTxt);
        image.setImageDrawable(drawable);
        date.setText(dateTxt);


    }
}
