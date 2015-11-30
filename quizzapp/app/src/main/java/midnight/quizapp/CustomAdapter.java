package midnight.quizapp;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Arleen on 23-04-2015.
 */
public class CustomAdapter extends BaseAdapter {
    ArrayList<String> message = new ArrayList<String>();
    Context cxt;
    private static LayoutInflater inflater=null;
    Typeface custom_font;
    ArrayList<Integer>type= new ArrayList<>();

    public CustomAdapter(Context context,ArrayList<String> msg,ArrayList<Integer>types) {
        // TODO Auto-generated constructor stub
        message=msg;
        cxt=context;
        type=types;
        custom_font = Typeface.createFromAsset(cxt.getAssets(), "segoeui.ttf");
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return message.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public class Holder
    {
        TextView messageBox,messageBox2;
        ImageView img,img2;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub


        Holder holder=new Holder();
        View rowView=convertView;

        if (type.get(position)==1)
            {
                rowView = inflater.inflate(R.layout.chat_row, null);
                holder.messageBox=(TextView) rowView.findViewById(R.id.message);
                holder.messageBox.setTypeface(custom_font);
                holder.messageBox.setText(message.get(position));
            }

        else
        {
            rowView = inflater.inflate(R.layout.chat_row2, null);
            holder.messageBox2=(TextView)rowView.findViewById(R.id.message2);
            holder.messageBox2.setTypeface(custom_font);
            holder.messageBox2.setText(message.get(position));
        }



        return rowView;
    }


}