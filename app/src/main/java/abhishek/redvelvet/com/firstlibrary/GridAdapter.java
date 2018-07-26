package abhishek.redvelvet.com.firstlibrary;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.zip.InflaterOutputStream;

/**
 * Created by abhishek on 26/7/18.
 */

public class GridAdapter extends BaseAdapter {

    ArrayList<HashMap> contact_list;
    private  static Context context;

    public GridAdapter(ArrayList<HashMap> contact_list, Context context){
        this.contact_list = contact_list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return contact_list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View gridView = new View(context);

        try {

            //-------------fetch data from arraylist-----------
            HashMap contact_details = contact_list.get(position);
            Bitmap profile_image = (Bitmap)contact_details.get("contact_photo");
            String display_name = (String)contact_details.get("display_name");
            //-------------------------------------------------

            //-----------get the layout-------------
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);



            if (convertView==null){


                gridView = inflater.inflate(R.layout.listview_style,null);

                // set value into textview
                TextView textView = (TextView) gridView
                        .findViewById(R.id.displayName);
                textView.setText(display_name);

//                // set image based on selected text
                ImageView imageView = (ImageView) gridView
                        .findViewById(R.id.profileImage);

                imageView.setImageBitmap(profile_image);
            }
            else{
                gridView = (View)convertView;
            }
            //--------------------------------------

        }
        catch (Exception e){

        }
        return gridView;
    }
}
