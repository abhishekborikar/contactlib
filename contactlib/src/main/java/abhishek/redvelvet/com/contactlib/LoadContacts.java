package abhishek.redvelvet.com.contactlib;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by abhishek on 25/7/18.
 */

public class LoadContacts {

    //parameter to import
    private ListView lv;
    private ContentResolver contentResolver;
    private Context context;

    public static ArrayList<HashMap> al=null;

    private Cursor cursor_Android_Contacts ;
    public static final String[] loadAllContacts_keys = {"display_name", "contact_photo"};
//    public static final int[] ids = {R.id.name, R.id.number};

    public LoadContacts(ContentResolver contentResolver, Context context) {
        this.contentResolver = contentResolver;
        this.context = context;

        cursor_Android_Contacts = contentResolver.query(ContactsContract.Contacts.CONTENT_URI,
                null,
                null,
                null,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
    }

    /*
     *   Load All contacts with displayName and image releated to display name contact
     *   LoadContacts loadContacts = new LoadContacts(contentResolver, context)
     *   loadContacts.loadAllContact();
     *   @param id ==> resources default image id
     *   @return ArrayList<HashMap>
     *    HashMap key ==>> loadAllContacts_keys
     *
     */

    public ArrayList<HashMap> getAllContacts(int id) {

        al = new ArrayList();

        //to get connection to database in android we use content resolver
        //get all contacts
        try {
            //check if it has contacts
//            if(cursor_Android_Contacts!=null)
            if (cursor_Android_Contacts.getCount() > 0) {


                if (cursor_Android_Contacts.moveToFirst()) {

                    do {

                        HashMap hm = new HashMap();

                        //--------------Contact ID---------------------------------
                        String contactId = null;
                        contactId = cursor_Android_Contacts.getString(cursor_Android_Contacts.getColumnIndex(ContactsContract.Contacts._ID));
                        //---------------------------------------------------------

                        //---------------contact display name----------------------
                        String contact_display_name = cursor_Android_Contacts
                                .getString(
                                        cursor_Android_Contacts.getColumnIndex(
                                                ContactsContract.Contacts.DISPLAY_NAME
                                        )
                                );
                        hm.put(loadAllContacts_keys[0],contact_display_name);
                        //-----------------------------------------------------------

                        //-------------get contact image-----------------------------
                        Bitmap photo = BitmapFactory.decodeResource(context.getResources(), id);

                        InputStream inputStream = ContactsContract.Contacts.openContactPhotoInputStream(
                                                                                context.getContentResolver(),
                                                                                ContentUris
                                                                                        .withAppendedId(ContactsContract.Contacts.CONTENT_URI, Long.parseLong(contactId)));

                        if(inputStream!=null){
                            Log.e("input Stream",inputStream+"");
                            photo = BitmapFactory.decodeStream(inputStream);

                        }
//
                        hm.put(loadAllContacts_keys[1],photo);


                        al.add(hm);

//                        assert inputStream!=null;
//                        inputStream.close();

                        //-----------------------------------------------------------

                    } while (cursor_Android_Contacts.moveToNext());
                }



                return al;
            }
        } catch (Exception e) {

            Log.e("error in contact", e.getMessage());
        }


        return al;
    }

}
