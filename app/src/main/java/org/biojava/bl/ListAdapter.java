package org.biojava.bl;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.biojava.coreapp.MainActivity;
import org.biojava.coreapp.R;
import java.util.ArrayList;


/**
 * Created by edvinas on 17.5.23.
 */

public class ListAdapter extends ArrayAdapter<String> {

    // This is constructor for MyAdapter : You can edit its second parameter a/c to your requirement
    // I have used Array List of string
    public ListAdapter(Context context, ArrayList<String> records)
    {
        super(context, 0, records);
    }

    @Override
    // This is important method : You can write your own code in this function
    // You can set your textview/ button methods :
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        String item = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_cell, parent, false);
        }

        final TextView list_Txt=(TextView)convertView.findViewById(R.id.List_txt);
        Button list_But=(Button)convertView.findViewById(R.id.List_But);


        list_Txt.setText(item);

        list_But.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get position of button into listView
                View parentRow = (View) v.getParent();
                ListView listView = (ListView) parentRow.getParent();
                final int position = listView.getPositionForView(parentRow);

//                // It will change textview text :
//                Toast.makeText(v.getContext(), "position: "+position, Toast.LENGTH_LONG).show();
//                list_Txt.setText("Clicked Me!");

                View rootView=  (View) v.getRootView();
                TextView outputView = (TextView) rootView.findViewById(R.id.outputView2);
                outputView.setText("");

                //open file
                Intent intent = new Intent();
                intent.setType("*/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                Activity host = (Activity) v.getContext();
                host.startActivityForResult(Intent.createChooser(intent, "Select a file"), (120+position));

            }
        });

        return convertView;
    }
}