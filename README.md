# contactlib
[![](https://jitpack.io/v/abhishekborikar/contactlib.svg)](https://jitpack.io/#abhishekborikar/contactlib)      
Library for Android to access and handle contacts.


## Features

1) Access all contacts.  
2) Access contact details like all numbers(with their types).  
3) Access the other account details linked with contact like email, whatsapp.  
4) Access the recent call log with complete details.
5) Various Contact Operations Add or Delete.  

## Docs

**Steps to Implement Project**  
1)  Project Level --> build.gradle     
```gradle    
    allprojects{
        repositories {
             maven { url 'https://jitpack.io' }
        }
    }        
```
2) app level --> build.gradle       
```gradle
    dependencies {
        implementation 'com.github.abhishekborikar:contactlib:v0.1.0'
    }       
```
**Usage**     
1) Access Contacts (Names and Profile Pic)
```java    
    LoadContacts contacts = new LoadContacts(getContentResolver(),getApplicationContext());
```
To get Contact List
```java
    final ArrayList<HashMap> contact_list = contacts.getAllContacts(R.drawable.default_contact_image);
    //  HashMap keys --> { "display_name", "contact_photo"}
    for(HashMap item: contact_list){
          String display_name = item.get("display_name");
          Bitmap image = item.get("contact_photo");
     }   
```
To get Recent Call Log
```java
    final ArrayList<HashMap> recent_call_log = contacts.getRecentContacts();
    //  time format DD/MM/YYYY HH:MM:SS
    //  type --> "OUTGOING", "INCOMING", "MISSED"
    //  HashMap keys --> {{"display_name", "phone_number", "time","type"}
    for(HashMap item: recent_call_log){
          String display_name = item.get("display_name");
          String number       = item.get("phone_number");
          String time         = item.get("time");
          String type         = item.get("type");
     }
```
2) Access the Contact Detail Information
```java
    ContactOperation operation = new ContactOperation(getContentResolver(),getApplicationContext());
```    
To get all the numbers
```java
    ArrayList<HashMap> contact = operation.getNumber(name);
    for (HashMap   item: contact) {
           //All Numbers saved with their types
           String number = item.get("number").toString();
           String type = item.get("type").toString();
    }
```
To get Whatsapp Number with display_name
```java  
    String whatsapp_number = operation.getWhatsAppNumber(display_name);
```
To get Email_ID
```java
    String email =  operation.getEmail(display_name);
```
3) Modifiy Contact   
Add New Contact
```java
    boolean isSaved = operation.addNewContact(display_name,phone_number);    
```
Delete Contact
```java
    boolean isDeleted = operation.deleteContact(display_name);
```
# Happy Coding ;)

<sub>Note : If any issue please post. And also if someone wants to contribute, then always welcome.</sub>
