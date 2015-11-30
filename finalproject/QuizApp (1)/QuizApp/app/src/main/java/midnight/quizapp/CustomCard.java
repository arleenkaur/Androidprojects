package midnight.quizapp;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;
import it.gmariotti.cardslib.library.internal.Card;

/**
 * Created by Arleen on 20-04-2015.
 */
public class CustomCard extends Card {

    Context cxt;
    TextView title;
    String text;
    Typeface custom_font;
    CircleImageView imageView;
    int  image = R.mipmap.ic_launcher;

    public CustomCard(Context context) {
        super(context,R.layout.card_layout);
        cxt=context;
        custom_font = Typeface.createFromAsset(cxt.getAssets(), "segoeui.ttf");

    }


    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {


        imageView = (CircleImageView)view.findViewById(R.id.menu_icon);
        title = (TextView)view.findViewById(R.id.text);
        title.setText(text);
        title.setTypeface(custom_font);
        Log.v("Custom Card","Custom Card called and element is setting now");
        imageView.setImageResource(image);
        title.setTextColor(cxt.getResources().getColor(R.color.Black));



    }
}
