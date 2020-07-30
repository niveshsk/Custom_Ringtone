package com.nivesh.custom_ringtone;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class Display_ALL_Contact_adapter extends ArrayAdapter {
    ArrayList<HashMap<String,String>>arrayList;
    Context context;
    public Display_ALL_Contact_adapter(@NonNull Context context, int resource,ArrayList<HashMap<String,String>> arrayList) {
        super(context, resource,arrayList);
        this.arrayList=arrayList;
        this.context=context;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view= LayoutInflater.from(context).inflate(R.layout.contact_view,null);
        TextView name=view.findViewById(R.id.name);
        TextView number=view.findViewById(R.id.number);
        CircleImageView imageView=view.findViewById(R.id.profile_image);
        Bitmap bitmap=Bitmap.createBitmap(80,80, Bitmap.Config.ARGB_8888);
        Canvas canvas=new Canvas(bitmap);
        Paint paint=new Paint();
        paint.setColor(Color.BLACK);
paint.setTextSize(80);
paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawBitmap(bitmap, 0, 10, paint);

        canvas.drawText(arrayList.get(position).get("letter").toUpperCase(), 40, 65, paint);
        Drawable d = new BitmapDrawable(context.getResources(), bitmap);

        imageView.setImageDrawable(d);
        name.setText(Html.fromHtml(arrayList.get(position).get("name")));
        number.setText(Html.fromHtml(arrayList.get(position).get("number")));
        ImageButton custom_ringtone=view.findViewById(R.id.set_music);
        custom_ringtone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Choose_Music.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("name",arrayList.get(position).get("name"));
                intent.putExtra("number",arrayList.get(position).get("number"));
                intent.putExtra("ID",arrayList.get(position).get("ID"));
                context.startActivity(intent);
                /*  ContentValues values = new ContentValues();

                ContentResolver resolver = context.getContentResolver();

                File file = new File(Environment.getExternalStorageDirectory() + "/abcd.mp3");
                if(file.exists()) {

                    Uri oldUri = MediaStore.Audio.Media.getContentUriForPath(file.getAbsolutePath());
                    resolver.delete(oldUri, MediaStore.MediaColumns.DATA + "=\"" + file.getAbsolutePath() + "\"", null);


                    String contact_number = arrayList.get(position).get("number");
                    Uri lookupUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, contact_number);

                    // The columns used for `Contacts.getLookupUri`
                    String[] projection = new String[]{
                            ContactsContract.Contacts._ID, ContactsContract.Contacts.LOOKUP_KEY
                    };

                    Cursor data = context.getContentResolver().query(lookupUri, projection, null, null, null);

                    if (data != null && data.moveToFirst()) {
                        data.moveToFirst();
                        // Get the contact lookup Uri
                        long contactId = data.getLong(0);
                        String lookupKey = data.getString(1);
                        Uri contactUri = ContactsContract.Contacts.getLookupUri(contactId, lookupKey);

                        values.put(MediaStore.MediaColumns.DATA, file.getAbsolutePath());
                        values.put(MediaStore.MediaColumns.TITLE, "Beautiful");
                        values.put(MediaStore.MediaColumns.MIME_TYPE, "audio/mp3");
                        values.put(MediaStore.Audio.Media.IS_RINGTONE, true);

                        Uri uri = MediaStore.Audio.Media.getContentUriForPath(file.getAbsolutePath());
                        assert uri != null;
                        Uri newUri = resolver.insert(uri, values);

                        if(newUri != null){
                            String uriString = newUri.toString();
                            values.put(ContactsContract.Contacts.CUSTOM_RINGTONE, uriString);
                            Log.e("Uri String for " + ContactsContract.Contacts.CONTENT_URI, uriString);
                            long updated = resolver.update(contactUri, values,null, null);

                            Toast.makeText(getContext(), "Updated : " + updated, Toast.LENGTH_LONG).show();
                        }

                        data.close();
                    }


                } else {
                    Toast.makeText(getContext(), "File does not exist", Toast.LENGTH_LONG).show();
                }*/}
        });
        return view;
    }
}
