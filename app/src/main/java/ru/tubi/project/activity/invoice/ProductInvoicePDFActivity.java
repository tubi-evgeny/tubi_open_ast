package ru.tubi.project.activity.invoice;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.net.ConnectivityManager;
import android.os.Build;
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
import ru.tubi.project.models.InvoiceModel;
import ru.tubi.project.models.OrderModel;
import ru.tubi.project.models.TransferModel;
import ru.tubi.project.models.UserModel;
import ru.tubi.project.utilites.Constant;
import ru.tubi.project.utilites.FirstSimbolMakeBig;
import ru.tubi.project.utilites.InitialData;
import ru.tubi.project.utilites.UserDataRecovery;

import static ru.tubi.project.free.AllText.INVOICE_I_AM;
import static ru.tubi.project.free.AllText.IN_PACKAGE;
import static ru.tubi.project.free.AllText.LOAD_TEXT;
import static ru.tubi.project.free.AllText.PRODUCTS;
import static ru.tubi.project.free.AllText.TAX_ID_SMALL;

public class ProductInvoicePDFActivity extends AppCompatActivity {

    private Intent takeit;
    private String invoiceName, stDate, myWarehousInfo, in_WarehousInfo, car_info_string,buyerCompanyInfoString;
    private int out_warehouse_id, in_warehouse_id, car_id, invoiceNumber;

    private int document_num = 0, invoiceKey, order_id=000, saveDocInfo, key, priceTest=222;
    private ArrayList<InvoiceModel> product_list = new ArrayList<>();
    private CarrierPanelModel partnerWarehousMod;
    //private CounterpartyModel buyerCompanyMod;
    //private ArrayList<TransferModel> productList = new ArrayList<>();

    private PDFView pdfView;
    private File file;
    private Bitmap bmt, scaledbmt;
    //private String [] infoArray = new String[]{"Name","Company name", "Address","Phone", "Email"};
    private int pageWidth = 1240;
    private int pageHeght = 1754;
    private int pageNumber = 1;
    private int positionCount = 0, allProductQuantity=0;

    private UserModel userDataModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_invoice_pdf);
        setTitle(INVOICE_I_AM);
        getSupportActionBar().setSubtitle(PRODUCTS);

        takeit = getIntent();
        invoiceKey = takeit.getIntExtra("invoiceKey",0);
        invoiceName = takeit.getStringExtra("invoiceName");
        //productList=(ArrayList<TransferModel>)takeit.getSerializableExtra("productList");
        out_warehouse_id = takeit.getIntExtra("out_warehouse_id", 0);
        in_warehouse_id = takeit.getIntExtra("in_warehouse_id", 0);
        car_id = takeit.getIntExtra("car_id",0);
        myWarehousInfo = takeit.getStringExtra("myWarehousInfo");
        in_WarehousInfo = takeit.getStringExtra("in_WarehousInfo");
        car_info_string = takeit.getStringExtra("car_info_string");
        saveDocInfo = takeit.getIntExtra("saveDocInfo",0);

        pdfView=findViewById(R.id.pdfView);

        //???????????????? ???? sqlLite ???????????? ???????????????????????? ?? ????????????????
        UserDataRecovery userDataRecovery = new UserDataRecovery();
        userDataModel = userDataRecovery.getUserDataRecovery(this);


        bmt = BitmapFactory.decodeResource(getResources(),R.drawable.tubi_logo_200ps);
        scaledbmt = Bitmap.createScaledBitmap(bmt, 100,90,false);

        //????????????????????
        ActivityCompat.requestPermissions(this,new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);
        ActivityCompat.requestPermissions(this,new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

        //???????????????? ???????????? ???? ???????????????? ????????????????????
        receiveBuyerCompanyInfo();
        //????????????????/?????????????? ?????????? ???????????????? ??????????????????
        receiveOrCreateInvoiceProductNumber();
        //???????????????? ???????????? ???? ???????????? ?? ???????????????? ??????????????????
        receiveInvoiceProductInfo();

        //createdPDF();

        //pdfView.fromFile(file).load();

        Toast.makeText(this, ""+invoiceName, Toast.LENGTH_SHORT).show();
    }
    //???????????????? ???????????? ???? ???????????????? ????????????????????
    private void receiveBuyerCompanyInfo(){
        String url = Constant.API;
        url += "receive_company_info";
        url += "&"+"in_warehouse_id="+in_warehouse_id;
        String whatQuestion = "receive_company_info";
        setInitialData(url, whatQuestion);
    }

    //???????????????? ???????????? ???? ???????????? ?? ???????????????? ??????????????????
    private void receiveInvoiceProductInfo(){
        String url = Constant.API;
        url += "receive_invoice_product_info";
        url += "&"+"invoiceKey="+invoiceKey;
        String whatQuestion = "receive_invoice_product_info";
        setInitialData(url, whatQuestion);
    }
    //????????????????/?????????????? ?????????? ???????????????? ??????????????????
    private void receiveOrCreateInvoiceProductNumber(){
        String url = Constant.API;
        url += "receive_or_create_invoice_product_number";
        url += "&"+"invoiceKey="+invoiceKey;
        String whatQuestion = "receive_or_create_invoice_product_number";
        setInitialData(url, whatQuestion);
    }
    private void setInitialData(String url_get, String whatQuestion) {
        ProgressDialog asyncDialog = new ProgressDialog(this);
        //???????????????? ???????????????????? ??????????????????
        if ( !isOnline() ){
            Toast.makeText(getApplicationContext(),
                    "?????? ???????????????????? ?? ????????????????????!",Toast.LENGTH_LONG).show();
            return;
        }
        InitialData task=new InitialData(){
            @Override
            protected void onPreExecute() {
                asyncDialog.setMessage(LOAD_TEXT);
                asyncDialog.show();
                super.onPreExecute();
            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            protected void onPostExecute(String result) {
                if(whatQuestion.equals("receive_or_create_invoice_product_number")){
                    splitInvoiceNumberResult(result);
                }else if(whatQuestion.equals("receive_invoice_product_info")){
                    splitInvoiceResult(result);
                }
                else if(whatQuestion.equals("receive_company_info")){
                    splitCompanyInfoResult(result);
                }
                //???????????? ???????????????????? ????????
                asyncDialog.dismiss();
            }
        };
        task.execute(url_get);
    }
    private void splitCompanyInfoResult(String result){
        buyerCompanyInfoString = result;
    }
    private void splitInvoiceNumberResult(String result) {
        //Toast.makeText(this, "result\n"+result, Toast.LENGTH_SHORT).show();
        try {
            String[] res = result.split("<br>");
            String[] temp = res[0].split("&nbsp");
            document_num = Integer.parseInt(temp[0]);
            stDate = temp[1];

        } catch (Exception ex) {
            Toast.makeText(this, "Exception: " + ex, Toast.LENGTH_SHORT).show();
        }
    }
    private void splitInvoiceResult(String result) {
        //Toast.makeText(this, "result\n"+result, Toast.LENGTH_SHORT).show();
        try {
            String[] res = result.split("<br>");
            String[] one_temp = res[0].split("&nbsp");
            if (one_temp[0].equals("error") || one_temp[0].equals("messege")) {
                Toast.makeText(this, "" + one_temp[1], Toast.LENGTH_LONG).show();
                return;
            } else {
                for (int i = 0; i < res.length; i++) {
                    String[] temp = res[i].split("&nbsp");
                    if (temp[0].equals("messege")) {
                        Toast.makeText(this, "" + temp[1], Toast.LENGTH_LONG).show();
                    } else {
                        String description_docs = temp[0];
                        double quantity = Double.parseDouble(temp[1]);
                        double price = Double.parseDouble(temp[2]);

                        InvoiceModel product = new InvoiceModel( description_docs
                                , quantity, price);

                        product_list.add(product);
                    }
                }
            }
        } catch (Exception ex) {
            Toast.makeText(this, "Exception: " + ex, Toast.LENGTH_SHORT).show();
        }
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

        //???????????????????? ?????????????? ????????????????
       /* canvas.drawText("+", 10,10,myPaint);
        canvas.drawText("+", pageWidth-10,10,myPaint);
        canvas.drawText("+", 10,pageHeght-10,myPaint);
        canvas.drawText("+", pageWidth-10,pageHeght-10,myPaint);*/

        if(saveDocInfo == 0){
            myPaint.setColor(Color.BLACK);
            myPaint.setTextSize(30f);
            myPaint.setTextAlign(Paint.Align.LEFT);
            canvas.drawText("???????????????? ???? ????????????????, ???? ?????????????? ??????????????????!", 300,30,myPaint);
            myPaint.setStrokeWidth(2f);
            canvas.drawLine(320,38,pageWidth-320,38,myPaint);
        }

        myPaint.setColor(Color.parseColor("#5c5c5c"));
        myPaint.setTextSize(18f);
        myPaint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText("?????????? ??? "+order_id, pageWidth-70,60,myPaint);
        /* canvas.drawText("8-985-138-00-xx]", 1160,80,myPaint);*/

        titlePaint.setTextAlign(Paint.Align.CENTER);
        titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.BOLD));
        titlePaint.setTextSize(25f);
        //dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        invoiceName = new FirstSimbolMakeBig().firstSimbolMakeBig(invoiceName);
        canvas.drawText(invoiceName+" ??? "+document_num+" ???? "+stDate+" ??.", pageWidth/2,150,titlePaint);

        myPaint.setStrokeWidth(2f);
        canvas.drawLine(70,155,pageWidth-60,155,myPaint);

        String partner_info = userDataModel.getAbbreviation()+" "
                +new FirstSimbolMakeBig().firstSimbolMakeBig(userDataModel.getCounterparty());
        myPaint.setTextAlign(Paint.Align.LEFT);
        myPaint.setTextSize(18f);
        myPaint.setColor(Color.BLACK);
        canvas.drawText("?????????????????? (?????????????????????????? ????????????????????): ", 70,180,myPaint);
        titlePaint.setTextAlign(Paint.Align.LEFT);
        titlePaint.setTextSize(18f);
        canvas.drawText(""+partner_info+" "+TAX_ID_SMALL+" "+userDataModel.getCompany_tax_id() ,100,205,titlePaint);
        canvas.drawText(""+myWarehousInfo,100,235,titlePaint);

        myPaint.setTextAlign(Paint.Align.LEFT);
        myPaint.setTextSize(18f);
        myPaint.setColor(Color.BLACK);
        canvas.drawText("????????????????????: ", 70,270,myPaint);

        canvas.drawText(""+buyerCompanyInfoString ,100,295,titlePaint);
        canvas.drawText(""+in_WarehousInfo,100,320,titlePaint);


        myPaint.setStyle(Paint.Style.STROKE);
        myPaint.setStrokeWidth(3f);
        canvas.drawRect(70,330,pageWidth-60,370,myPaint);

        titlePaint.setTextSize(18f);
        titlePaint.setTextAlign(Paint.Align.LEFT);
        titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.BOLD));
        //titlePaint.setStyle(Paint.Style.FILL);
        canvas.drawText("???",90,360,titlePaint);
        canvas.drawText("???????????????? ????????????",160,360,titlePaint);
        canvas.drawText("??????-????",720,360,titlePaint);
        canvas.drawText("????????",900,360,titlePaint);
        canvas.drawText("??????????",1050,360,titlePaint);

        canvas.drawLine(140,335,140,365,myPaint);
        canvas.drawLine(700,335,700,365,myPaint);
        canvas.drawLine(880,335,880,365,myPaint);
        canvas.drawLine(1030,335,1030,365,myPaint);

        //?????????????????? ???????????? ??????????????
        double total = 0, totalSumm=0;
        int y=395;
        myPaint.setTextAlign(Paint.Align.LEFT);
        myPaint.setStyle(Paint.Style.FILL);
        myPaint.setTextSize(18f);
        for(int i=0;i<product_list.size();i++){
            positionCount=i;
            allProductQuantity+=product_list.get(i).getQuantity();
            String productDescription = product_list.get(i).getDescription_docs();

            //???????????????? ???????????? ???????????? ?? ?????? ???????????????????? ?????????????????? ?????????????? ?????????????? ????????????
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
            canvas.drawText(String.valueOf(product_list.get(i).getQuantity()),840,y,myPaint);
            myPaint.setTextAlign(Paint.Align.LEFT);
            canvas.drawText("????.",850,y,myPaint);
            myPaint.setTextAlign(Paint.Align.RIGHT);
            canvas.drawText(String.format("%.2f",product_list.get(i).getPrice()),1010,y,myPaint);
            //canvas.drawText(String.format("%.2f",productList.get(i).getPrice()),1010,y,myPaint);
            total = product_list.get(i).getPrice() * product_list.get(i).getQuantity();
            //total = productList.get(i).getPrice() * productList.get(i).getQuantity();
            myPaint.setTextAlign(Paint.Align.RIGHT);
            canvas.drawText(String.format("%.2f",total),pageWidth-70,y,myPaint);
            //???????????? ?? ?????????????????? ????????????
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

         //Toast.makeText(this, "res\n"+stringLength, Toast.LENGTH_SHORT).show();
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
        canvas.drawText("????o???? ",1020,y,myPaint);
        canvas.drawText(":",1030,y,myPaint);
        //myPaint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText(String.format("%.2f",totalSumm),pageWidth-70,y,myPaint);

        y+=40;
        //myPaint.setTextAlign(Paint.Align.LEFT);
        canvas.drawText("?????? ????????????(??????)",1020,y,myPaint);
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
        canvas.drawText("??????????",880,y,myPaint);
        myPaint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText(String.format("%.2f",totalSumm ),pageWidth-110,y,myPaint);//+(totalSumm*10/100)
        //myPaint.setTextAlign(Paint.Align.RIGHT);
        myPaint.setTextSize(18f);
        canvas.drawText("??????.",pageWidth-70,y,myPaint);

        y+=60;
        myPaint.setTextAlign(Paint.Align.LEFT);
        canvas.drawText("?????????? ???????????????????????? "+(positionCount+1)
                +", ???????????? ???????????? "+allProductQuantity
                +", ???? ?????????? "+String.format("%.2f",totalSumm )+" ??????.",80,y,myPaint);

        MoneyToStr moneyToStr = new MoneyToStr(MoneyToStr.Currency.RUR, MoneyToStr.Language.RUS,
                MoneyToStr.Pennies.NUMBER);
        //Toast.makeText(this, "test\n"
        //        +new FirstSimbolMakeBig().firstSimbolMakeBig(moneyToStr.convert(totalSumm)), Toast.LENGTH_SHORT).show();
        y+=25;
        canvas.drawText(new FirstSimbolMakeBig().firstSimbolMakeBig(moneyToStr.convert(totalSumm))
                ,80,y,titlePaint);

        y+=15;
        myPaint.setStrokeWidth(3f);
        canvas.drawLine(70,y,pageWidth-60,y,myPaint);
        y+=50;
        myPaint.setTextAlign(Paint.Align.LEFT);
        canvas.drawText("??????????",80,y,myPaint);

        myPaint.setStrokeWidth(1f);
        canvas.drawLine(170,y+2,370,y+2,myPaint);
        canvas.drawLine(390,y+2,670,y+2,myPaint);
        canvas.drawLine(700,y+2,1050,y+2,myPaint);

        myPaint.setTextSize(14f);
        canvas.drawText("??????????????????",230,y+17,myPaint);
        canvas.drawText("??????????????",500,y+17,myPaint);
        canvas.drawText("??????????????????????",820,y+17,myPaint);

        y+=40;
        myPaint.setTextSize(18f);
        myPaint.setTextAlign(Paint.Align.LEFT);
        canvas.drawText("??????????????",80,y,myPaint);

        myPaint.setStrokeWidth(1f);
        canvas.drawLine(170,y+2,370,y+2,myPaint);
        canvas.drawLine(390,y+2,670,y+2,myPaint);
        //canvas.drawLine(700,y+2,1050,y+2,myPaint);

        myPaint.setTextSize(14f);
        canvas.drawText("??????????????????????",220,y+17,myPaint);
        canvas.drawText("??????????????",500,y+17,myPaint);

        myPdfDocument.finishPage(myPage1);

        String folderPath;
        String filePath;
        String fileDir;
        if(saveDocInfo == 0){
            folderPath = Environment.getExternalStorageDirectory() + "/tubi/documents";
            filePath = "invoice_see"+".pdf";
            fileDir = "tubi/documents/";
        }else{
            folderPath = Environment.getExternalStorageDirectory() + "/tubi/nakladnie";
            filePath = "tovar_nak_"+document_num+".pdf";
            fileDir = "tubi/nakladnie/";
        }

        File folder = new File(folderPath);
        if (!folder.exists()) {
            File wallpaperDirectory = new File(folderPath);
            wallpaperDirectory.mkdirs();
        }


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
    //???????????????? ???????????????????? ??????????????????
    protected boolean isOnline() {
        String cs = Context.CONNECTIVITY_SERVICE;
        ConnectivityManager cm = (ConnectivityManager)
                getSystemService(cs);
        if (cm.getActiveNetworkInfo() == null) {
            return false;
        } else {
            return true;
        }
    }
}