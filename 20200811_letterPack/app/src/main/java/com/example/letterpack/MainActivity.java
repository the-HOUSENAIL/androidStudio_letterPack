package com.example.letterpack;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Environment;
import android.provider.Contacts;
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
import android.provider.Contacts.People;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Locale;
import android.view.Menu;
import android.view.MenuItem;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Contacts;
import android.provider.Contacts.ContactMethods;
import android.provider.Contacts.ContactMethodsColumns;
import android.provider.Contacts.People;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_PICK_CONTACT = 1;
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
    private Button toBtn;
    private Button forBtn;
    private int referenceBtn;

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

        toBtn = (Button) findViewById(R.id.toAddBtn);
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
                values.put(ContactsContract.CommonDataKinds.StructuredPostal.TYPE, ContactsContract.CommonDataKinds.StructuredPostal.FORMATTED_ADDRESS);
                values.put(ContactsContract.CommonDataKinds.StructuredPostal.IS_SUPER_PRIMARY, 1);
                values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE);
                values.put(ContactsContract.CommonDataKinds.StructuredPostal.STREET, String.valueOf(toAddText.getText()));
                getContentResolver().insert(addressUri, values);

            }
        });

        // intentは明示的
        // startActivityForResult(Intent intent, int requestCode)
        // REQUEST_PICK_CONTACT = 1
        // 宛先用検索
        toBtn = (Button) findViewById(R.id.toAddBtn);
        toBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                referenceBtn = 1;
                Intent intent = new Intent(Intent.ACTION_PICK, People.CONTENT_URI);

                //startActivityForResult()で起動すると、選択されたデータのURIがonActivityResult()で取得できます
                startActivityForResult(intent, REQUEST_PICK_CONTACT);
            }
        });

        //送り主用検索
        forBtn = (Button) findViewById(R.id.forAddBtn);
        forBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                referenceBtn = 2;
                Intent intent = new Intent(Intent.ACTION_PICK, People.CONTENT_URI);
                startActivityForResult(intent, REQUEST_PICK_CONTACT);
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
    // アクティビティからの結果の取得
    protected void onActivityResult(int requestCode, int resultCode, Intent returnedIntent) {
        super.onActivityResult(requestCode, resultCode, returnedIntent);
        //結果コードは RESULT_OK か RESULT_CANCELED のどちらかで、結果を返す側の Activity にて適切な値が返るようにする
        if (requestCode == REQUEST_PICK_CONTACT && resultCode == Activity.RESULT_OK) {
            Uri uri = returnedIntent.getData();
            //query(問合せ)の管理  = 今回はデータを取得
            Cursor personCursor = managedQuery(uri, null, null, null, null);
            DatabaseUtils.dumpCursor(personCursor);
            //カーソルを頭に持ってくる
            if (personCursor.moveToFirst()) {
                //連絡先の中身を指定(ID,氏名,電話番号)※住所はこの後
                int idIndex = personCursor.getColumnIndexOrThrow(People._ID);
                int nameIndex = personCursor.getColumnIndexOrThrow(People.NAME);
                int numberIndex = personCursor.getColumnIndexOrThrow(People.NUMBER);


                String id = personCursor.getString(idIndex);
                String name = personCursor.getString(nameIndex);
                String number = personCursor.getString(numberIndex);
                String address = "";

                //ここで住所指定
                //文字列操作
                StringBuilder where = new StringBuilder();
                //ID番号？を設定している？
                where.append(ContactMethods.PERSON_ID).append(" == ? AND ");
                //種類？を設定している？
                where.append(ContactMethods.KIND).append(" == ?");
                //検索条件
                String selection = where.toString();
                //selectionに含まれる"?"が指定された値に置き換えられる PERSON_ID = id, KIND = String.valueOf(Contacts.KIND_POSTAL)
                String[] selectionArgs = {id, String.valueOf(Contacts.KIND_POSTAL)};
                Cursor addressCursor = managedQuery(ContactMethods.CONTENT_URI, null, selection, selectionArgs, null);
                if (addressCursor.moveToFirst()) {
                    //カラムDATAを指定
                    int addressIndex = addressCursor.getColumnIndexOrThrow(ContactMethodsColumns.DATA);
                    address = addressCursor.getString(addressIndex);
                }

                switch (referenceBtn) {
                        case 1:
                            toNameText.setText(name);
                            toTelText.setText(number);
                            toAddText.setText(address);
                            break;
                        case 2:
                            forNameText.setText(name);
                            forTelText.setText(number);
                            forAddText.setText(address);
                            break;
                    }
            }
        }
    }
}
