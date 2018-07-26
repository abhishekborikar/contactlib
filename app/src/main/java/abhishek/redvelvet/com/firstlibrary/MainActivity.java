package abhishek.redvelvet.com.firstlibrary;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import abhishek.redvelvet.com.contactlib.ContactDetails;
import abhishek.redvelvet.com.contactlib.LoadContacts;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    GridAdapter gridAdapter;

    @BindView(R.id.contact)
    GridView gridView;

    @BindView(R.id.new_contact_button)
    Button new_contact_btn;

    @BindView(R.id.et_name)
    EditText name;
    @BindView(R.id.et_number)
    EditText number;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //--------Bind to butter knife-----------
        ButterKnife.bind(this);

        gridView = (GridView)findViewById(R.id.contact);

    }

    @Override
    protected void onStart() {
        super.onStart();

        //--------get contact using module------------------
        LoadContacts contacts = new LoadContacts(getContentResolver(),getApplicationContext());
        final ArrayList<HashMap> contact_list = contacts.getAllContacts(R.drawable.default_contact);

        Log.e("contact length",""+contact_list.size());
        //--------------------------------------------------

        gridAdapter = new GridAdapter(contact_list,getApplicationContext());
        gridView.setAdapter(gridAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = (String)(((HashMap)contact_list.get(position)).get("display_name"));

                //-------retrive all details-----------
                ContactDetails details = new ContactDetails(getContentResolver(),getApplicationContext());
//                String mail = details.getEmail(name).get(0);
                String number = (String)((HashMap)details.getNumber(name).get(0)).get("number");
                String type = (String)((HashMap)details.getNumber(name).get(0)).get("type");
                //-------------------------------------

                Toast.makeText(MainActivity.this, name+" "+number+" "+details.getWhatsAppNumber(name)+" "+type, Toast.LENGTH_SHORT).show();
            }
        });


        //----------------------add new contact-----------------------
        ContactDetails contactDetails = new ContactDetails(getContentResolver(),getApplicationContext());
        contactDetails.addNewContact(name.getText().toString(),number.getText().toString());
        //------------------------------------------------------------
    }


}
