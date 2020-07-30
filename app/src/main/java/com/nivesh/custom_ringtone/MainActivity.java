package com.nivesh.custom_ringtone;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SeekBar;


import com.divyanshu.colorseekbar.ColorSeekBar;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
String current_state;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final ListView contact=findViewById(R.id.contact);

        final SharedPreferences sharedPreferences=getSharedPreferences("resource",MODE_PRIVATE);
        final SharedPreferences.Editor editor=sharedPreferences.edit();
        final ColorSeekBar colorSeekBar=findViewById(R.id.change_color);

colorSeekBar.setVisibility(View.INVISIBLE);
        final Toolbar toolbar = findViewById(R.id.toolbar);
        if(sharedPreferences.getString("search_bar",null)!=null){
            toolbar.setBackgroundColor(Integer.parseInt(sharedPreferences.getString("search_bar",null)));
        }
        if(sharedPreferences.getString("list_background",null)!=null){
            contact.setBackgroundColor(Integer.parseInt(sharedPreferences.getString("list_background",null)));
        }
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
int selected=item.getItemId();
switch (selected){
    case R.id.search_bar_background:
        colorSeekBar.setVisibility(View.VISIBLE);
        current_state="search_bar";

        break;

    case R.id.list_background:
        colorSeekBar.setVisibility(View.VISIBLE);
        current_state="list_background";

        break;

}
                return false;
            }
        });

        colorSeekBar.setOnColorChangeListener(new ColorSeekBar.OnColorChangeListener() {
            @Override
            public void onColorChangeListener(int i) {
                System.out.println("dsfsdfsdf"+i);
                if(current_state.equals("search_bar")){

                    toolbar.setBackgroundColor(i);
                }
                else if(current_state.equals("list_background")){
                    System.out.println(i);
                    contact.setBackgroundColor(i);
                }

                editor.putString(current_state, String.valueOf(i));
                editor.apply();
            }
        });

        int resourceID = getResources().getIdentifier("green", "color", getPackageName());
System.out.println(resourceID);
        SQLiteDatabase sqLiteDatabase=openOrCreateDatabase("contact",MODE_PRIVATE,null);
        sqLiteDatabase.execSQL("create table if not exists contact(id varchar,name varchar,number varchar)");
        final ArrayList<HashMap<String,String>>arrayList=new ArrayList<>();
        Cursor cursor=getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,null,null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME +" ASC");

        assert cursor != null;
        while (cursor.moveToNext()){
            HashMap<String,String>hashMap=new HashMap<>();
            hashMap.put("ID",cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID)));

            hashMap.put("name",cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)));
            char letter=cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)).charAt(0);
            hashMap.put("number",cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
            hashMap.put("letter", String.valueOf(letter));
            arrayList.add(hashMap);

    }
        System.out.println(arrayList);

Display_ALL_Contact_adapter display_all_contact_adapter=new Display_ALL_Contact_adapter(getApplicationContext(),R.layout.contact_view,arrayList);
    contact.setAdapter(display_all_contact_adapter);

        SearchView search=findViewById(R.id.contact_search);
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                try{
                    int num=Integer.parseInt(newText);
                    ArrayList<HashMap<String,String>>searched_list=new ArrayList<>();

                    for(HashMap<String,String> name:arrayList){

                        if(name.get("number").contains(Integer.toString(num))){
                            searched_list.add(name);
                        }
                    }
                    Display_ALL_Contact_adapter display_all_contact_adapter=new Display_ALL_Contact_adapter(getApplicationContext(),R.layout.contact_view,searched_list);
                    contact.setAdapter(display_all_contact_adapter);
                }
                catch (NumberFormatException e){
                     if(newText.equals("")){
                        Display_ALL_Contact_adapter display_all_contact_adapter=new Display_ALL_Contact_adapter(getApplicationContext(),R.layout.contact_view,arrayList);
                        contact.setAdapter(display_all_contact_adapter);
                    }else{
                        ArrayList<HashMap<String,String>>searched_list=new ArrayList<>();
                        for(HashMap<String,String> name:arrayList){
                            if(name.get("name").toLowerCase().contains(newText)){
                                HashMap<String,String> name_buf= (HashMap<String, String>) name.clone();
                                name_buf.put("name",name_buf.get("name").replace(newText,"<font color=#388e3c>"+newText+"</font>"));
                                searched_list.add(name_buf);
                            }
                        }
                        Display_ALL_Contact_adapter display_all_contact_adapter=new Display_ALL_Contact_adapter(getApplicationContext(),R.layout.contact_view,searched_list);
                        contact.setAdapter(display_all_contact_adapter);}
                }

                return false;
            }
        });
    }


}