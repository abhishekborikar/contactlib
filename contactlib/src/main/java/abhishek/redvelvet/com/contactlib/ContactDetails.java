package abhishek.redvelvet.com.contactlib;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by abhishek on 26/7/18.
 */

public class ContactDetails {

    ContentResolver contentResolver;
    Context context;

    public ContactDetails(ContentResolver contentResolver, Context context){
        this.contentResolver = contentResolver;
        this.context = context;
    }

    /*
     *  @param displayName
     *  @return phoneNumber --> contact number with displayName which is whatsapp number
     */
    public String getWhatsAppNumber(String displayName){
        String rawContactId = null;
        Log.e("i","1");
        try {
                Cursor name_cr = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, "DISPLAY_NAME = '" + displayName + "'", null, null);
            Log.e("i","2");
            if (name_cr.moveToFirst()) {
                String contactId = name_cr.getString(name_cr.getColumnIndex(ContactsContract.Contacts._ID));

                //get the rawContactId from Contacts._ID
                String[] projection = new String[]{ContactsContract.RawContacts._ID};
                String selection = ContactsContract.Data.CONTACT_ID + " = ? AND account_type IN (?)";
                String[] selectionArgs = new String[]{contactId, "com.whatsapp"};
                Cursor cursor = contentResolver.query(ContactsContract.RawContacts.CONTENT_URI, projection, selection, selectionArgs, null);
                boolean hasWhatsApp = cursor.moveToNext();
                if (hasWhatsApp) {
                    rawContactId = cursor.getString(0);

                }

                //get the any whatsapp number from rawContactId
                projection = new String[]{ContactsContract.Data.DATA3};
                selection = ContactsContract.Data.MIMETYPE + " = ? AND " + ContactsContract.Data.RAW_CONTACT_ID + " = ? ";
                selectionArgs = new String[]{"vnd.android.cursor.item/vnd.com.whatsapp.profile", rawContactId};
                cursor = contentResolver.query(ContactsContract.Data.CONTENT_URI, projection, selection, selectionArgs, "1 LIMIT 1");
                String phoneNumber = null;
                if (cursor.moveToNext()) {
                    phoneNumber = cursor.getString(0);
                    phoneNumber.replace("Message","");


                }
                return phoneNumber;
            }
        }
        catch (Exception e){
            Toast.makeText(context, "Not a WhatsApp Number", Toast.LENGTH_SHORT).show();
        }
        return null ;
    }

    /*
     *  @param displayName
     *  @return ArrayList<HashMap> --> contain the number and type of number
     *  HashMap key -->
     *      "number" -->  to get the number
     *      "type"   -->  to get the type of number
     */
    public ArrayList<HashMap> getNumber(String displayName){

        ArrayList<HashMap> number_al = new ArrayList();

        try {

            Cursor name_cr = contentResolver.query(ContactsContract.Contacts.CONTENT_URI,null,"DISPLAY_NAME = '"+displayName+"'",null,null);

            if(name_cr.moveToFirst()){
                String contactId = name_cr.getString(name_cr.getColumnIndex(ContactsContract.Contacts._ID));
                Cursor number_cr = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,"CONTACT_ID = "+contactId,null,null);

                if(number_cr.moveToFirst()){
                    do {
                        String number = number_cr.getString(number_cr.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        int type = number_cr.getInt(number_cr.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));

                        HashMap hm = new HashMap();
                        hm.put("number",number);
                        hm.put("type",(type == 1 ? "Home" : "Mobile"));
                        number_al.add(hm);
                    }while (number_cr.moveToNext());
                }
                number_cr.close();
            }
            else{
                return null;
            }
            name_cr.close();

        }
        catch (Exception e){
            Log.e("LoadAllContact",""+e);
        }
        return number_al;
    }

    /*
     *  Get the email of the contact
     *  @param displayName --> Name listed in contact
     *  @return ArrayList<String> --> contains all the mail saved on this displayName
     */

    public ArrayList<String> getEmail(String displayName){

        ArrayList mail_al = new ArrayList();
        String contactId = null;
        Cursor name_cr = contentResolver.query(ContactsContract.Contacts.CONTENT_URI,null,"DISPLAY_NAME = '"+displayName+"'",null,null);

        if(name_cr.moveToFirst())
            contactId = name_cr.getString(name_cr.getColumnIndex(ContactsContract.Contacts._ID));

        Cursor mail_cr = contentResolver.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                null,
                ContactsContract.CommonDataKinds.Email.CONTACT_ID + "=?",
                new String[]{contactId},
                null);
        if(mail_cr!=null)
            if(mail_cr.getCount()>0)
                if(mail_cr.moveToFirst()){
                    do {

                        String emailId = mail_cr.getString(mail_cr.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                        mail_al.add(emailId);
                    }while (mail_cr.moveToNext());
                }
        mail_cr.close();
        return mail_al;
    }
}
