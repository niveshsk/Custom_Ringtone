package com.nivesh.custom_ringtone;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;

public class Choose_Music extends AppCompatActivity {
    String number;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose__music);
        TextView set_mu_name=findViewById(R.id.set_ring_name);
        TextView set_mu_num=findViewById(R.id.set_ring_number);
        Button set_music=findViewById(R.id.choose_music_file);
TextView textView=findViewById(R.id.textView);
int id11 =getResources().getColor(R.color.colorAccent);
System.out.println(id11);
textView.setText(Html.fromHtml("<font color=#cc0029>write any thing here</font>"));
        set_music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent1, 10);
            }
        });
        Intent intent = getIntent();
        if (intent != null) {
            String id = intent.getStringExtra("ID");
            String name = intent.getStringExtra("name");
             number= intent.getStringExtra("number");
set_mu_name.setText(name);
set_mu_num.setText(number);
            CircleImageView imageView=findViewById(R.id.set_ring_profile_image);
            Bitmap bitmap=Bitmap.createBitmap(80,80, Bitmap.Config.ARGB_8888);
            Canvas canvas=new Canvas(bitmap);
            Paint paint=new Paint();
            paint.setColor(Color.BLACK);
            paint.setTextSize(80);
            paint.setTextAlign(Paint.Align.CENTER);
            canvas.drawBitmap(bitmap, 0, 10, paint);
            char letter=name.charAt(0);
            canvas.drawText(String.valueOf(letter).toUpperCase(), 40, 60, paint);
            Drawable d = new BitmapDrawable(getResources(), bitmap);

            imageView.setImageDrawable(d);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data1) {


        if( data1 != null){
        Uri uri = data1.getData();

        ContentValues values = new ContentValues();

        ContentResolver resolver = getContentResolver();

        File file = new File(getRealPathFromURI(uri));
        System.out.println(file);

        if(file.exists()) {
            Uri oldUri = MediaStore.Audio.Media.getContentUriForPath(file.getAbsolutePath());
            resolver.delete(oldUri, MediaStore.MediaColumns.DATA + "=\"" + file.getAbsolutePath() + "\"", null);


            String contact_number = number;
            Uri lookupUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, contact_number);

            // The columns used for `Contacts.getLookupUri`
            String[] projection = new String[]{
                    ContactsContract.Contacts._ID, ContactsContract.Contacts.LOOKUP_KEY
            };

            Cursor data = getContentResolver().query(lookupUri, projection, null, null, null);

            if (data != null && data.moveToFirst()) {
                data.moveToFirst();
                // Get the contact lookup Uri
                long contactId = data.getLong(0);
                String lookupKey = data.getString(1);
                Uri contactUri = ContactsContract.Contacts.getLookupUri(contactId, lookupKey);

                values.put(MediaStore.MediaColumns.DATA, file.getAbsolutePath());
                values.put(MediaStore.MediaColumns.TITLE, file.getName());
                values.put(MediaStore.MediaColumns.MIME_TYPE, "audio/mp3");
                values.put(MediaStore.Audio.Media.IS_RINGTONE, true);

                Uri uri1 = MediaStore.Audio.Media.getContentUriForPath(file.getAbsolutePath());
                assert uri1 != null;
                Uri newUri = resolver.insert(uri1, values);

                if(newUri != null){
                    String uriString = newUri.toString();
                    values.put(ContactsContract.Contacts.CUSTOM_RINGTONE, uriString);
                    Log.e("Uri String for " + ContactsContract.Contacts.CONTENT_URI, uriString);
                    long updated = resolver.update(contactUri, values,null, null);

                    Toast.makeText(getApplicationContext(), "Updated : " + updated, Toast.LENGTH_LONG).show();
                }

                data.close();
            }


        } else {
            Toast.makeText(getApplicationContext(), "File does not exist", Toast.LENGTH_LONG).show();
        }}else{
            Toast.makeText(this, "cancel", Toast.LENGTH_SHORT).show();
        }
        super.onActivityResult(requestCode, resultCode, data1);

    }
    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Audio.Media.DATA };
        CursorLoader loader = new CursorLoader(getApplicationContext(), contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }
}
