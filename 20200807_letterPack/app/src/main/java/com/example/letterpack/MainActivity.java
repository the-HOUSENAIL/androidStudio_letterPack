package com.example.letterpack;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Environment;
import android.provider.ContactsContract;
import android.text.Editable;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.os.Bundle;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Locale;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_WRITE_READ = 1;
    public static int PICK_CONTACT = 1000;

    private static final int REQUEST_SAVE_IMAGE = 1002;
    private TextView textView;
    private  Bitmap bmp;
    private  View view;
    private EditText toPCText;
    private  EditText toAddText;
    private EditText toBuildingText;
    private  EditText toNameText;
    private  EditText toTelText;
    private EditText forPCText;
    private  EditText forAddText;
    private EditText forBuildingText;
    private  EditText forNameText;
    private  EditText forTelText;
    private  EditText contentsText;

    public static final String EXTRA_MESSAGE
//            = "com.example.testactivitytrasdata.MESSAGE";
            = "YourPackageName.MESSAGE";
    static final int RESULT_SUBACTIVITY = 1000;

    CanvasView canvasview;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setScreenMain();

        toPCText = findViewById(R.id.EditToPostalCode);
        toAddText = findViewById(R.id.EditToAddress);
        toBuildingText = findViewById(R.id.EditToIncludeBuilding);
        toNameText = findViewById(R.id.EditToName);
        toTelText = findViewById(R.id.EditToTel);
        forPCText = findViewById(R.id.EditForPostalCode);
        forAddText = findViewById(R.id.EditForAddress);
        forBuildingText = findViewById(R.id.EditForIncludeBuilding);
        forNameText = findViewById(R.id.EditForName);
        forTelText = findViewById(R.id.EditForTel);
        contentsText = findViewById(R.id.EditContents);
    }

    /** メニューの生成イベント */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }
    /** メニューがクリックされた時のイベント */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch ( item.getItemId() ) {
            case R.id.item1:
                setScreenMain();
                break;
            case R.id.item2:
                if (canvasview != null){
                    canvasview.saveToFile();

                } else {
                    Log.v("エラー", "nullです");
                }
                break;
            case R.id.item3:
                finish();
                break;
        }
        return true;
    }


    private void setScreenMain(){
        setContentView(R.layout.activity_main);

        Button saveTo = (Button) findViewById(R.id.saveToBtn);
        saveTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //書き込み検証用
                //名前
                ContentValues values = new ContentValues();
                Uri rawContactUri = getContentResolver().insert(ContactsContract.RawContacts.CONTENT_URI, values);
                long rawContactId = ContentUris.parseId(rawContactUri);
                values.clear();
                values.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
                values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
                values.put(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, String.valueOf(toNameText.getText()));
                getContentResolver().insert(ContactsContract.Data.CONTENT_URI, values);

                // 携帯の電話番号を登録
                Uri mobileUri = Uri.withAppendedPath(rawContactUri, ContactsContract.Contacts.Data.CONTENT_DIRECTORY);
                values.clear();
                values.put(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE);
                values.put(ContactsContract.CommonDataKinds.Phone.IS_SUPER_PRIMARY, 1);
                values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
                values.put(ContactsContract.CommonDataKinds.Phone.NUMBER, String.valueOf(toTelText.getText()));
                getContentResolver().insert(mobileUri, values);

                // 住所登録
                Uri addressUri = Uri.withAppendedPath(rawContactUri, ContactsContract.Contacts.Data.CONTENT_DIRECTORY);
                values.clear();
                values.put(ContactsContract.CommonDataKinds.StructuredPostal.TYPE, ContactsContract.CommonDataKinds.StructuredPostal.STREET);
                values.put(ContactsContract.CommonDataKinds.StructuredPostal.IS_SUPER_PRIMARY, 1);
                values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE);
                values.put(ContactsContract.CommonDataKinds.StructuredPostal.STREET, String.valueOf(toAddText.getText()));
                getContentResolver().insert(addressUri, values);

            }
        });

        //宛先用検索
        Button toBtn = (Button) findViewById(R.id.toAddBtn);
        toBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent生成
                Intent intent = new Intent();
                // Action設定(連絡先)
                intent.setAction(Intent.ACTION_GET_CONTENT);
                // Type設定
                intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
                // 起動
                startActivityForResult(intent, PICK_CONTACT);
            }
        });

        //送り主用検索
        Button forBtn = (Button) findViewById(R.id.forAddBtn);
        toBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent生成
                Intent intent = new Intent();
                // Action設定(連絡先)
                intent.setAction(Intent.ACTION_GET_CONTENT);
                // Type設定
                intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
                // 起動
                startActivityForResult(intent, PICK_CONTACT);
            }
        });

        /// パーミッション許可を取る
        if (Build.VERSION.SDK_INT >= 23) {
            if(ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_CONTACTS)
                    != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.READ_CONTACTS)
                    != PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(this,
                        new String[]{
                                Manifest.permission.READ_CONTACTS,
                                Manifest.permission.WRITE_CONTACTS
                        },
                        PERMISSION_WRITE_READ);
            }
        }

        Button sendButton = findViewById(R.id.ConfirmButton);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setScreenSub();
            }
        });
    }

    private void setScreenSub(){
        String toPC = String.valueOf(toPCText.getText());
        String toAdd = String.valueOf(toAddText.getText());
        String toBuilding = String.valueOf(toBuildingText.getText());
        String toName = String.valueOf(toNameText.getText());
        String toTel = String.valueOf(toTelText.getText());
        String forPC = String.valueOf(forPCText.getText());
        String forAdd = String.valueOf(forAddText.getText());
        String forBuilding = String.valueOf(forBuildingText.getText());
        String forName = String.valueOf(forNameText.getText());
        String forTel = String.valueOf(forTelText.getText());
        String contentsTo = String.valueOf(contentsText.getText());
        canvasview= new CanvasView(this, null, toPC, toAdd, toBuilding, toName, toTel, forPC, forAdd, forBuilding, forName, forTel, contentsTo);
        setContentView(canvasview);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_CONTACT) {
            if (resultCode == Activity.RESULT_OK) {
                Uri contactData = data.getData();
                CursorLoader cursorLoader = new CursorLoader(this, contactData, null, null, null, null);
                Cursor cursor = cursorLoader.loadInBackground();

                if (cursor.moveToFirst()) {
                    // 連絡先ID
                    String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                    // 連絡先名取得
                    String name = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));

                    String addressName = null;

                    String phoneNum = null;

                    Cursor phone = getContentResolver().query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);

                    Cursor address = getContentResolver().query(
                            ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);

                    if (phone.moveToFirst()) {
                        //電話番号取得
                        phoneNum = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                    }
                    // cursor閉じる
                    phone.close();

                    if (address.moveToFirst()) {
                        addressName = address.getString(address.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.STREET));
                    } else {
                        addressName = "通っていない";
                    }
                    address.close();

                    // 画面表示
                    EditText toShow1 = (EditText) findViewById(R.id.EditToAddress);
                    EditText toShow2 = (EditText) findViewById(R.id.EditToName);
                    EditText toShow3 = (EditText) findViewById(R.id.EditToTel);
                    //toShow1.setText(addressName);
                    toShow2.setText(name);
                    //toShow3.setText(phoneNum);

                    if (addressName == null){
                        toShow1.setText("nullだよ");
                    } else if (addressName == ""){
                        toShow1.setText("空欄");
                    } else {
                        toShow1.setText(addressName);
                    }

                    if (phoneNum == null){
                        toShow3.setText("nullだよ");
                    } else if (addressName == ""){
                        toShow3.setText("空欄");
                    } else {
                        toShow3.setText(phoneNum);
                    }
                }
                cursor.close();
            }
        }

        if (requestCode == REQUEST_SAVE_IMAGE && resultCode == Activity.RESULT_OK) {
            if(data.getData() != null){

                Uri uri = data.getData();

                try(OutputStream outputStream =
                            getContentResolver().openOutputStream(uri);) {
                    bmp.compress(Bitmap.CompressFormat.PNG, 100, outputStream);

                } catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
    }
}