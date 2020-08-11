package com.example.letterpack;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.Editable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap;

import java.io.File;
import java.io.FileOutputStream;
import android.app.Activity;
import android.graphics.Bitmap.CompressFormat;
import android.os.Environment;
import android.view.MotionEvent;
import android.widget.EditText;
import android.widget.Toast;

public class CanvasView extends View {

    private Paint paint;
    private Boolean viewflg = false;
    private Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.letterpacklight);
    private Bitmap bmp2 = Bitmap.createScaledBitmap(bmp, 1020, 1400, false);

    //郵便番号文字列
    private String toPC;
    private String arrayToPC[];
    private String forPC;
    private String arrayForPC[];

    private float oldx = 0f;
    private float oldy = 0f;
    private Canvas bmpCanvas;
    private Activity _context;
    private String toPC1;
    private String toPC2;
    private String toPC3;
    private String toPC4;
    private String toPC5;
    private String toPC6;
    private String toPC7;
    private String toAdd;
    private String toBuilding;
    private String toName;
    private String toTel1;
    private String toTel2;
    private String toTel3;
    private String forPC1;
    private String forPC2;
    private String forPC3;
    private String forPC4;
    private String forPC5;
    private String forPC6;
    private String forPC7;
    private String forAdd;
    private String forBuilding;
    private String forName;
    private String forTel1;
    private String forTel2;
    private String forTel3;
    private String contents;

    private boolean bFirst = true;

    public CanvasView(Context context, AttributeSet attrs, String toPCText, String toAddText, String toBuildingText, String toNameText, String toTelText, String forPCText, String forAddText, String forBuildingText, String forNameText, String forTelText, String contents) {
        super(context, attrs);
        paint = new Paint();
        viewflg = true;

        setText(toPCText, toAddText, toBuildingText, toNameText, toTelText, forPCText, forAddText, forBuildingText, forNameText, forTelText, contents);
    }

//    public void showCanvas(boolean flg){
//        viewflg = flg;
//        invalidate();
//    }

    @Override
    protected void onDraw(Canvas canvas) {

        // Bitmap 画像を表示
        //if(viewflg){
        canvas.drawBitmap(bmp2, 0, 0, paint);
        //}

        //郵便番号配列確認用
        toPC = "1234567";
        arrayToPC = toPC.split("");
        Log.v("宛先郵便番号", "配列0:" + arrayToPC[0] + ":" + arrayToPC[6]);
        forPC = "1234567";
        arrayForPC = forPC.split("");

        if (bFirst) {

            //宛先
            toPC1 = arrayToPC[0];
            toPC2 = arrayToPC[1];
            toPC3 = arrayToPC[2];
            toPC4 = arrayToPC[3];
            toPC5 = arrayToPC[4];
            toPC6 = arrayToPC[5];
            toPC7 = arrayToPC[6];
            toAdd = "東京都oooooo";
            toBuilding = "〇〇ビル";
            toName = "山田太郎";
            toTel1 = "090";
            toTel2 = "1234";
            toTel3 = "5678";

            //依頼主
            forPC1 = arrayForPC[0];
            forPC2 = arrayForPC[1];
            forPC3 = arrayForPC[2];
            forPC4 = arrayForPC[3];
            forPC5 = arrayForPC[4];
            forPC6 = arrayForPC[5];
            forPC7 = arrayForPC[6];
            forAdd = "東京都oooooo";
            forBuilding = "〇〇ビル";
            forName = "鈴木次郎";
            forTel1 = "090";
            forTel2 = "1234";
            forTel3 = "5678";
//        //品名
            contents = "品物";
        }



        Paint paint = new Paint();

        //文字サイズを50に設定
        paint.setTextSize(50);
        paint.setStrokeWidth(5);
        paint.setColor(Color.BLACK);
        //[x:100,y:100]から描画する。
        canvas.drawText(toPC1,500,255, paint);
        canvas.drawText(toPC2,560,255, paint);
        canvas.drawText(toPC3,620,255, paint);
        canvas.drawText(toPC4,678,255, paint);
        canvas.drawText(toPC5,738,255, paint);
        canvas.drawText(toPC6,798,255, paint);
        canvas.drawText(toPC7,858,255, paint);

        paint.setTextSize(30);
        canvas.drawText(toAdd,300,430, paint);
        canvas.drawText(toBuilding,300,500, paint);
        canvas.drawText(toName,350,610, paint);
        canvas.drawText(toTel1,500,665, paint);
//        canvas.drawText(toTel2,615,665, paint);
//        canvas.drawText(toTel3,738,665, paint);


        canvas.drawText(forName,370,870, paint);
        canvas.drawText(forTel1,500,920, paint);
//        canvas.drawText(forTel2,615,920, paint);
//        canvas.drawText(forTel3,738,920, paint);

        canvas.drawText(contents,300,1030, paint);

        paint.setTextSize(25);
        canvas.drawText(forAdd + forBuilding ,300,802, paint);
        canvas.drawText(forPC1,380,715, paint);
        canvas.drawText(forPC2,406,715, paint);
        canvas.drawText(forPC3,432,715, paint);
        canvas.drawText(forPC4,460,715, paint);
        canvas.drawText(forPC5,487,715, paint);
        canvas.drawText(forPC6,513,715, paint);
        canvas.drawText(forPC7,539,715, paint);

    }

    public CanvasView(Context context) {
        super(context);
        _context = (Activity)context;
        paint = new Paint();
        paint.setColor(Color.MAGENTA);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(6);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeJoin(Paint.Join.ROUND);
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w,h,oldw,oldh);
        //32ビットのARGBデータでBitmapを作成
        bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        bmpCanvas = new Canvas(bmp);

    }


    public boolean onTouchEvent(MotionEvent e){
        switch(e.getAction()){
            case MotionEvent.ACTION_DOWN: //最初のポイント
                oldx = e.getX();
                oldy = e.getY();
                break;
            case MotionEvent.ACTION_MOVE: //途中のポイント
                bmpCanvas.drawLine(oldx, oldy, e.getX(), e.getY(), paint);
                oldx = e.getX();
                oldy = e.getY();
                invalidate();
                break;
            default:
                break;
        }
        return true;
    }

    public void clearDrawList(){
        bmpCanvas.drawColor(Color.BLACK);
        invalidate();
    }

    public void saveToFile(){
        if(!sdcardWriteReady()){
            Toast.makeText(_context, "SDcardが認識されません。", Toast.LENGTH_SHORT).show();
            return;
        }
        File file = new File(Environment.getExternalStorageDirectory().getPath()+"/drawbm/");
        try{
            if(!file.exists()){
                file.mkdir();
            }
        }catch(SecurityException e){}

        String AttachName = file.getAbsolutePath() + "/";
        AttachName += System.currentTimeMillis()+".jpg";
        File saveFile = new File(AttachName);
        while(saveFile.exists()) {
            AttachName = file.getAbsolutePath() + "/" + System.currentTimeMillis() +".jpg";
            saveFile = new File(AttachName);
        }
        try {
            FileOutputStream out = new FileOutputStream(AttachName);
            bmp2.compress(CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            Toast.makeText(_context, "保存されました。", Toast.LENGTH_SHORT).show();
        } catch(Exception e) {
            Toast.makeText(_context, "例外発生", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean sdcardWriteReady(){
        String state = Environment.getExternalStorageState();
        return (Environment.MEDIA_MOUNTED.equals(state));
    }

    public void setText (String toPCText, String toAddText, String toBuildingText, String toNameText, String toTelText, String forPCText, String forAddText, String forBuildingText, String forNameText, String forTelText, String contentsText) {
        //郵便番号配列確認用

        if (toPC == null){
            toPC = "0123456";
        } else if (toPC == "") {
            toPC = "0123456";
        } else {
            toPC = toPCText;
        }

        if (forPC == null){
            forPC = "0123456";
        } else if (toPC == "") {
            forPC = "0123456";
        } else {
            forPC = toPCText;
        }

        arrayToPC = toPC.split("");
        //Log.v("宛先郵便番号", "配列0:" + arrayToPC[0] + ":" + arrayToPC[6]);
        arrayForPC = forPC.split("");
        //宛先
        toPC1 = arrayToPC[0];
        toPC2 = arrayToPC[1];
        toPC3 = arrayToPC[2];
        toPC4 = arrayToPC[3];
        toPC5 = arrayToPC[4];
        toPC6 = arrayToPC[5];
        toPC7 = arrayToPC[6];
        toAdd = toAddText;
        toBuilding = toBuildingText;
        toName = toNameText;
        toTel1 = toTelText;
//        toTel2 = "1234";
//        toTel3 = "5678";

        //依頼主
        forPC1 = arrayForPC[0];
        forPC2 = arrayForPC[1];
        forPC3 = arrayForPC[2];
        forPC4 = arrayForPC[3];
        forPC5 = arrayForPC[4];
        forPC6 = arrayForPC[5];
        forPC7 = arrayForPC[6];
        forAdd = forAddText;
        forBuilding = forBuildingText;
        forName = forNameText;
        forTel1 = forTelText;
//        forTel2 = "1234";
        //forTel3 = "5678";
        //品名
        contents = contentsText;

        bFirst = false;
    }
}
