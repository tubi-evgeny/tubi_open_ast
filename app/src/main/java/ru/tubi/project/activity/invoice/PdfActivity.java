package ru.tubi.project.activity.invoice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.moneytostr.MoneyToStr;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import ru.tubi.project.R;
import ru.tubi.project.models.CarrierPanelModel;
import ru.tubi.project.models.CounterpartyModel;
import ru.tubi.project.models.OrderModel;
import ru.tubi.project.models.UserModel;
import ru.tubi.project.utilites.FirstSimbolMakeBig;
import ru.tubi.project.utilites.UserDataRecovery;

import static ru.tubi.project.free.AllText.DOCUMENT;
import static ru.tubi.project.free.AllText.IN_PACKAGE;
import static ru.tubi.project.free.AllText.TAX_ID;
import static ru.tubi.project.free.AllText.TAX_ID_SMALL;

public class PdfActivity extends AppCompatActivity {

    private int order_id, key;
    private CarrierPanelModel partnerWarehousMod;
    private CounterpartyModel buyerCompanyMod;
    private ArrayList<OrderModel> productList = new ArrayList<>();
    private Intent takeit;

    private PDFView pdfView;
    private File file;
    private Bitmap bmt, scaledbmt;
    //private String [] infoArray = new String[]{"Name","Company name", "Address","Phone", "Email"};
    private int pageWidth = 1240;
    private int pageHeght = 1754;
    private int pageNumber = 1;
    private int positionCount = 0, allProductQuantity=0;

    private UserModel userDataModel;
    private FirstSimbolMakeBig simbolMakeBig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf);
        setTitle("PDF");
        getSupportActionBar().setSubtitle(DOCUMENT);

        pdfView=findViewById(R.id.pdfView);

        //получить из sqlLite данные пользователя и компании
        UserDataRecovery userDataRecovery = new UserDataRecovery();
        userDataModel = userDataRecovery.getUserDataRecovery(this);


        bmt = BitmapFactory.decodeResource(getResources(),R.drawable.tubi_logo_200ps);
        scaledbmt = Bitmap.createScaledBitmap(bmt, 100,90,false);

        //разрешения
        ActivityCompat.requestPermissions(this,new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);
        ActivityCompat.requestPermissions(this,new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

        takeit = getIntent();
        key = takeit.getIntExtra("key",0);
        order_id = takeit.getIntExtra("order_id",0);
        partnerWarehousMod = (CarrierPanelModel)
                takeit.getSerializableExtra("partnerWarehousMod");
        buyerCompanyMod=(CounterpartyModel)
                takeit.getSerializableExtra("buyerCompanyMod");
        productList=(ArrayList<OrderModel>)takeit.getSerializableExtra("productList");

        //file = new File ("/storage/emulated/0/openPDF.pdf");

        createdPDF();

        pdfView.fromFile(file).load();
    }
    private void createdPDF(){
        Date date = new Date();
        DateFormat dateFormat;
        PdfDocument myPdfDocument = new PdfDocument();
        Paint myPaint = new Paint();
        Paint titlePaint = new Paint();

        PdfDocument.PageInfo myPageInfo1 =
                new PdfDocument.PageInfo.Builder(pageWidth,pageHeght,pageNumber).create();
        PdfDocument.Page myPage1 = myPdfDocument.startPage(myPageInfo1);
        Canvas canvas = myPage1.getCanvas();

        canvas.drawBitmap(scaledbmt,70,30,myPaint);

        //посмотреть границы страницы
       /* canvas.drawText("+", 10,10,myPaint);
        canvas.drawText("+", pageWidth-10,10,myPaint);
        canvas.drawText("+", 10,pageHeght-10,myPaint);
        canvas.drawText("+", pageWidth-10,pageHeght-10,myPaint);*/

        myPaint.setColor(Color.parseColor("#5c5c5c"));
        myPaint.setTextSize(18f);
        myPaint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText("Заказ № "+order_id, pageWidth-70,60,myPaint);
       /* canvas.drawText("8-985-138-00-22", 1160,80,myPaint);*/

        titlePaint.setTextAlign(Paint.Align.CENTER);
        titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.BOLD));
        titlePaint.setTextSize(25f);
        dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        canvas.drawText("Товарный чек           "+" от "+dateFormat.format(date)+" г.", pageWidth/2,150,titlePaint);

        myPaint.setStrokeWidth(2f);
        canvas.drawLine(70,155,pageWidth-60,155,myPaint);

        String partner_info = userDataModel.getAbbreviation()+" "
                +new FirstSimbolMakeBig().firstSimbolMakeBig(userDataModel.getCounterparty());
        myPaint.setTextAlign(Paint.Align.LEFT);
        myPaint.setTextSize(18f);
        myPaint.setColor(Color.BLACK);
        canvas.drawText("Поставщик (представитель поставщика): ", 70,180,myPaint);
        titlePaint.setTextAlign(Paint.Align.LEFT);
        titlePaint.setTextSize(18f);
        canvas.drawText(partner_info,100,205,titlePaint);
        canvas.drawText(TAX_ID_SMALL+" "+userDataModel.getCompany_tax_id(),100,235,titlePaint);

        String buyer_info = buyerCompanyMod.getAbbreviation()+" "
                +new FirstSimbolMakeBig().firstSimbolMakeBig(buyerCompanyMod.getCounterparty());
        myPaint.setTextAlign(Paint.Align.LEFT);
        myPaint.setTextSize(18f);
        myPaint.setColor(Color.BLACK);
        canvas.drawText("Покупатель: ", 70,270,myPaint);

        canvas.drawText(buyer_info,100,295,titlePaint);
        canvas.drawText(TAX_ID_SMALL+" "+buyerCompanyMod.getTaxpayer_id(),100,320,titlePaint);


        myPaint.setStyle(Paint.Style.STROKE);
        myPaint.setStrokeWidth(3f);
        canvas.drawRect(70,330,pageWidth-60,370,myPaint);

        titlePaint.setTextSize(18f);
        titlePaint.setTextAlign(Paint.Align.LEFT);
        titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.BOLD));
        //titlePaint.setStyle(Paint.Style.FILL);
        canvas.drawText("№",90,360,titlePaint);
        canvas.drawText("Описание товара",160,360,titlePaint);
        canvas.drawText("кол-во",720,360,titlePaint);
        canvas.drawText("цена",900,360,titlePaint);
        canvas.drawText("сумма",1050,360,titlePaint);

        canvas.drawLine(140,335,140,365,myPaint);
        canvas.drawLine(700,335,700,365,myPaint);
        canvas.drawLine(880,335,880,365,myPaint);
        canvas.drawLine(1030,335,1030,365,myPaint);

        //заполняем список товаров
        double total = 0, totalSumm=0;
        int y=395;
        myPaint.setTextAlign(Paint.Align.LEFT);
        myPaint.setStyle(Paint.Style.FILL);
        myPaint.setTextSize(18f);
        for(int i=0;i<productList.size();i++){
            positionCount=i;
            allProductQuantity+=productList.get(i).getQuantity_to_order();
            String productDescription = productList.get(i).getCategory()+" "
                    +productList.get(i).getCharacteristic()+" "+productList.get(i).getBrand()+" "
                    +productList.get(i).getType_packaging()+" "+productList.get(i).getWeight_volume()+" "
                    +productList.get(i).getUnit_measure()+" "+IN_PACKAGE+" "
                    +productList.get(i).getQuantity_package();
            //получить длинну строки и при привышении максимума сделать перенос строки
            ArrayList<String> stringList = new ArrayList<>();
            if(productDescription.length() > 54){
                String[]wordsList = productDescription.split(" ");

                String stringLine = "";
                int s=0;
                int wordLength = 0, stringLenght=0;
                for(int j=0;j < wordsList.length; j++){
                    wordLength = wordsList[j].length();
                    if(stringLenght+wordLength+1 < 54){
                        stringLenght +=wordLength+1;
                        stringLine += wordsList[j]+" ";
                    }else{
                        stringLenght =wordLength+1;
                        stringList.add(stringLine);
                        stringLine = wordsList[j]+" ";
                    }
                }
                stringList.add(stringLine);
               // stTest.addAll(stringList);
            }else{
                stringList.add(productDescription);
            }

            myPaint.setTextAlign(Paint.Align.LEFT);
            canvas.drawText(String.valueOf(i+1),90,y,myPaint);

            myPaint.setTextAlign(Paint.Align.RIGHT);
            canvas.drawText(String.valueOf(productList.get(i).getQuantity_to_order()),840,y,myPaint);
            myPaint.setTextAlign(Paint.Align.LEFT);
            canvas.drawText("шт.",850,y,myPaint);
            myPaint.setTextAlign(Paint.Align.RIGHT);
            canvas.drawText(String.format("%.2f",productList.get(i).getPrice()),1010,y,myPaint);
            total = productList.get(i).getPrice() * productList.get(i).getQuantity_to_order();
            myPaint.setTextAlign(Paint.Align.RIGHT);
            canvas.drawText(String.format("%.2f",total),pageWidth-70,y,myPaint);
            //строка с переносом строки
            myPaint.setTextAlign(Paint.Align.LEFT);
            for(int j=0;j < stringList.size();j++){
                canvas.drawText(stringList.get(j).toString(),150,y,myPaint);
                y+=20;
            }
            y+=15;
            myPaint.setStrokeWidth(2f);
            canvas.drawLine(70,y-25,pageWidth-60,y-25,myPaint);

            totalSumm +=total;

            if(y > pageHeght-60){
                myPdfDocument.finishPage(myPage1);
                pageNumber+=1;
                myPageInfo1 =
                        new PdfDocument.PageInfo.Builder(pageWidth,pageHeght,pageNumber).create();
                myPage1 = myPdfDocument.startPage(myPageInfo1);
                canvas = myPage1.getCanvas();

                y = 60;

                myPaint.setStrokeWidth(2f);
                canvas.drawLine(70,y-25,pageWidth-60,y-25,myPaint);
            }
        }
      /*  String st = "";
        for(int i = 0;i < stTest.size();i++){
            st+= ""+i+": "+stTest.get(i).toString()+"\n";
        }
        Toast.makeText(this, ""+st, Toast.LENGTH_SHORT).show();*/

        // Toast.makeText(this, "res\n"+stringLength, Toast.LENGTH_SHORT).show();
        if(y > pageHeght-370){
            myPdfDocument.finishPage(myPage1);
            pageNumber+=1;
            myPageInfo1 =
                    new PdfDocument.PageInfo.Builder(pageWidth,pageHeght,pageNumber).create();
            myPage1 = myPdfDocument.startPage(myPageInfo1);
            canvas = myPage1.getCanvas();

            y = 60;
        }

        y+=20;
        myPaint.setStrokeWidth(3f);
        canvas.drawLine(870,y,pageWidth-60,y,myPaint);
        y+=35;
        //myPaint.setTextAlign(Paint.Align.LEFT);
        myPaint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText("Итoго ",1020,y,myPaint);
        canvas.drawText(":",1030,y,myPaint);
        //myPaint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText(String.format("%.2f",totalSumm),pageWidth-70,y,myPaint);

        y+=40;
        //myPaint.setTextAlign(Paint.Align.LEFT);
        canvas.drawText("Без налога(НДС)",1020,y,myPaint);
        canvas.drawText(":",1030,y,myPaint);
        //myPaint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText("-",pageWidth-70,y,myPaint);//String.valueOf(totalSumm*10/100)

        y+=10;
        myPaint.setTextAlign(Paint.Align.LEFT);
        myPaint.setColor(Color.parseColor("#efeded" ));
        canvas.drawRect(870,y,pageWidth-60,y+60,myPaint);

        y+=40;
        myPaint.setColor(Color.BLACK);
        myPaint.setTextSize(25f);
        myPaint.setTextAlign(Paint.Align.LEFT);
        canvas.drawText("Сумма",880,y,myPaint);
        myPaint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText(String.format("%.2f",totalSumm ),pageWidth-110,y,myPaint);//+(totalSumm*10/100)
        //myPaint.setTextAlign(Paint.Align.RIGHT);
        myPaint.setTextSize(18f);
        canvas.drawText("руб.",pageWidth-70,y,myPaint);

        y+=60;
        myPaint.setTextAlign(Paint.Align.LEFT);
        canvas.drawText("Всего наименований "+(positionCount+1)
                +", единиц товара "+allProductQuantity
                +", на сумму "+String.format("%.2f",totalSumm )+" руб.",80,y,myPaint);

        MoneyToStr moneyToStr = new MoneyToStr(MoneyToStr.Currency.RUR, MoneyToStr.Language.RUS,
                MoneyToStr.Pennies.NUMBER);
        Toast.makeText(this, "test\n"
                +new FirstSimbolMakeBig().firstSimbolMakeBig(moneyToStr.convert(totalSumm)), Toast.LENGTH_SHORT).show();
        y+=25;
        canvas.drawText(new FirstSimbolMakeBig().firstSimbolMakeBig(moneyToStr.convert(totalSumm))
                ,80,y,titlePaint);

        y+=15;
        myPaint.setStrokeWidth(3f);
        canvas.drawLine(70,y,pageWidth-60,y,myPaint);
        y+=50;
        myPaint.setTextAlign(Paint.Align.LEFT);
        canvas.drawText("Выдал",80,y,myPaint);

        myPaint.setStrokeWidth(1f);
        canvas.drawLine(170,y+2,370,y+2,myPaint);
        canvas.drawLine(390,y+2,670,y+2,myPaint);
        canvas.drawLine(700,y+2,1050,y+2,myPaint);

        myPaint.setTextSize(14f);
        canvas.drawText("Должность",230,y+17,myPaint);
        canvas.drawText("Подпись",500,y+17,myPaint);
        canvas.drawText("Расшифровка",820,y+17,myPaint);

        y+=40;
        myPaint.setTextSize(18f);
        myPaint.setTextAlign(Paint.Align.LEFT);
        canvas.drawText("Получил",80,y,myPaint);

        myPaint.setStrokeWidth(1f);
        canvas.drawLine(170,y+2,370,y+2,myPaint);
        canvas.drawLine(390,y+2,670,y+2,myPaint);
        //canvas.drawLine(700,y+2,1050,y+2,myPaint);

        myPaint.setTextSize(14f);
        canvas.drawText("Расшифровка",220,y+17,myPaint);
        canvas.drawText("Подпись",500,y+17,myPaint);

        myPdfDocument.finishPage(myPage1);

        final String folderPath = Environment.getExternalStorageDirectory() + "/tubi/documents";
        File folder = new File(folderPath);
        if (!folder.exists()) {
            File wallpaperDirectory = new File(folderPath);
            wallpaperDirectory.mkdirs();
        }

        String filePath = "order_"+order_id+".pdf";
        String fileDir = "tubi/documents/";
        file = new File(this.getExternalFilesDir(fileDir),filePath);
        //file = new File(this.getExternalFilesDir("/"),"PDFpage2.pdf");
        try {
            myPdfDocument.writeTo(new FileOutputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }

        File fileWrite = new File(Environment.getExternalStorageDirectory(),"/"+fileDir+filePath);
        //File fileWrite = new File(Environment.getExternalStorageDirectory(),"/PDFpage2.pdf");
        try {
            myPdfDocument.writeTo(new FileOutputStream(fileWrite));
        } catch (IOException e) {
            e.printStackTrace();
        }

        myPdfDocument.close();

    }
}