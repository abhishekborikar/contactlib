package abhishek.redvelvet.com.contactlib;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
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
    public static final String[] getRecentContact_keys = {"display_name", "phone_number", "time","type"};

    Cursor cursor_r_contacts;

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


    /*
     *  Return call log present in mobile
     *  @param
     *  @return ArrayList<HashMap> al
     *  keys for hashmap ==>>
     *  1) String display_name = hm.get("display_name"); "No Name" if not exits in contact_list
     *  2) String phone_number = hm.get("phone_number");
     *  3) String time         = hm.get("time");
     *  4) String type         = hm.get("type");
     *
     *  type --> "OUTGOING", "INCOMING", "MISSED"
     *  time format = DD/MM/YYYY HH:MM:SS
     */
    public ArrayList<HashMap> getRecentContacts(){
        ArrayList al = new ArrayList();
        //get the recent call log
        try {

            //--------checks the permission to read contact list and recent contact list------------
            ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE);
            ActivityCompat.checkSelfPermission(context,Manifest.permission.READ_CALL_LOG);
            //--------------------------------------------------------------------------------------

            //-------------get the recent contacts with in the cursor "cursor_r_contact"------------
            cursor_r_contacts = contentResolver.query(CallLog.Calls.CONTENT_URI,
                    null,
                    null,
                    null,
                    CallLog.Calls.DATE+" DESC");

            //--------------------------------------------------------------------------------------

        } catch (Exception e) {
            Log.e("e",e+"");
        }

        //------------------extract all recent contact here-----------------------------------------
        if (cursor_r_contacts != null) {
            if (cursor_r_contacts.getCount() > 0) {
                if (cursor_r_contacts.moveToFirst()) {
                    do {
                        HashMap hm = new HashMap();

                        //---------------get complete details of calls------------------------------
                        String displayName = cursor_r_contacts.getString(cursor_r_contacts.getColumnIndex(CallLog.Calls.CACHED_NAME));
                        String number = cursor_r_contacts.getString(cursor_r_contacts.getColumnIndex(CallLog.Calls.NUMBER));
                        String time = cursor_r_contacts.getString(cursor_r_contacts.getColumnIndex(CallLog.Calls.DATE));
                        String type_int = cursor_r_contacts.getString(cursor_r_contacts.getColumnIndex(CallLog.Calls.TYPE));
                        //--------------------------------------------------------------------------

                        //-----------------Add the call details HashMap-----------------------------

                        //-------1) Add display_name--------
                        if (displayName != null)
                            hm.put(getRecentContact_keys[0],displayName);
                        else
                            hm.put(getRecentContact_keys[0],"No Name");

                        //------2) Add Phone_number----------
                        hm.put(getRecentContact_keys[1],number);

                        //--------------convert millisec time---------------------------------------
                        Calendar cl = Calendar.getInstance();
                        cl.setTimeInMillis(Long.parseLong(time));  //here your time in miliseconds
                        String date = "" + cl.get(Calendar.DAY_OF_MONTH) + "/" + cl.get(Calendar.MONTH) + "/" + cl.get(Calendar.YEAR);
                        String f_time = "" + cl.get(Calendar.HOUR_OF_DAY) + ":" + cl.get(Calendar.MINUTE)+":"+cl.get(Calendar.SECOND) ;
                        //--------------------------------------------------------------------------

                        //-------3) Add Time------------------
                        hm.put(getRecentContact_keys[2],date+" "+f_time);

                        //-------4) Add Type of Call----------
                        String type = null;
                        switch (Integer.parseInt(type_int)) {
                            case CallLog.Calls.OUTGOING_TYPE:
                                type = "OUTGOING";
                                break;

                            case CallLog.Calls.INCOMING_TYPE:
                                type = "INCOMING";
                                break;

                            case CallLog.Calls.MISSED_TYPE:
                                type = "MISSED";
                                break;
                        }
                        hm.put("type",type);
                        al.add(hm);

                        //-------------------------end adding details to HashMap--------------------

                    } while (cursor_r_contacts.moveToNext());
                }
            } else {
                return null;
            }
        } else {
            return null;
        }

        //------------------------------end extraction----------------------------------------------

        return al;
    }

}
