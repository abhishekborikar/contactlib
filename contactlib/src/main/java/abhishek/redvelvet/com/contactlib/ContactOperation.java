package abhishek.redvelvet.com.contactlib;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

/**
 * Created by abhishek on 26/7/18.
 */

public class ContactOperation extends ContactDetails{


    public ContactOperation(ContentResolver contentResolver, Context context) {
        super(contentResolver, context);
    }

    public boolean addNewContact(String display_name, String phone_number){

        //-----------prepare values to add new contact---------
        ContentValues contact_details = new ContentValues();
        contact_details.put(ContactsContract.Data.RAW_CONTACT_ID,001);
        contact_details.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
        contact_details.put(ContactsContract.CommonDataKinds.Phone.NUMBER,phone_number);
        contact_details.put(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_CUSTOM);
        contact_details.put(ContactsContract.CommonDataKinds.Phone.LABEL,display_name);
        //------------------------------------------------------

        Uri contact_status = contentResolver.insert(ContactsContract.Data.CONTENT_URI,contact_details);
        if(contact_status!=null)
            return true;
        return false;
    }

    /*
     *  it is used to delete contact
     *  fetch the display_name from arraylist of getallcontact send one name here
     *  @param  display_name
     *  @return boolean { true ? deleted : not_deleted }
     */

    public boolean deleteContact(String display_name){

        int data_table_status = 0;
        int raw_table_status = 0;
        try{

            Cursor name_cr = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, "DISPLAY_NAME = '" + display_name+ "'", null, null);

            if (name_cr.moveToFirst()){
                String contact_id = name_cr.getString(name_cr.getColumnIndex(ContactsContract.Contacts._ID));

                //------------------delete from data_table------------------------
                data_table_status = contentResolver.delete(
                        ContactsContract.Data.CONTENT_URI,
                        ContactsContract.Data.RAW_CONTACT_ID+" = "+contact_id,
                        null);

                //-----------------------------------------------------------------

                //------------delete from raw_contact table-------------------------
                raw_table_status = contentResolver.delete(
                        ContactsContract.RawContacts.CONTENT_URI,
                        ContactsContract.RawContacts._ID +" = "+contact_id,
                        null
                );
                //------------------------------------------------------------------


                if(data_table_status!=-1 || raw_table_status!=-1){
                    return true;
                }
            }
        }
        catch (Exception e){

            if(data_table_status!=-1 || raw_table_status!=-1){
                return true;
            }

            e.printStackTrace();
        }
        return false;
    }
}
