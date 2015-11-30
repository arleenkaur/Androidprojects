package midnight.quizapp;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

import it.gmariotti.cardslib.library.internal.Card;

/**
 * Created by Arleen on 25-04-2015.
 */
public class MarksCard extends Card {

    Context cxt;
    Typeface font;
    boolean first=false;
    TextView title,date;
    BarChart graph;
    ArrayList<BarEntry> entries = new ArrayList<>();
    ArrayList<String> labels = new ArrayList<String>();
    String titletxt,datetxt;


    public MarksCard(Context context) {
        super(context,R.layout.marks_card);
        cxt=context;
        font = Typeface.createFromAsset(cxt.getAssets(), "segoeui.ttf");

    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view)
    {
          title=(TextView)view.findViewById(R.id.title);
            date=(TextView)view.findViewById(R.id.date);
            title.setTypeface(font);
            date.setTypeface(font);
            graph = (BarChart)view.findViewById(R.id.graph);
            title.setText(titletxt);
            date.setText(datetxt);

            BarDataSet dataset = new BarDataSet(entries, "Marks");
             dataset.setColors(ColorTemplate.COLORFUL_COLORS);
             BarData data = new BarData(labels, dataset);
            graph.setData(data);
        graph.animateXY(2000, 2000);
        graph.setDescription("Marks of all students");
        Log.v("Labels", "This should be the size of labels "+labels.size() );






    }


}
