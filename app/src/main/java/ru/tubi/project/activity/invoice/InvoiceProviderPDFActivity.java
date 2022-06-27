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
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.moneytostr.MoneyToStr;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

import ru.tubi.project.R;
import ru.tubi.project.activity.ActivityCatalog;
import ru.tubi.project.activity.MainActivity;
import ru.tubi.project.activity.MenuActivity;
import ru.tubi.project.activity.ShopingBox;
import ru.tubi.project.models.InvoiceModel;
import ru.tubi.project.utilites.Constant;
import ru.tubi.project.utilites.FirstSimbolMakeBig;
import ru.tubi.project.utilites.InitialData;

import static ru.tubi.project.free.AllText.DOCUMENT;
import static ru.tubi.project.free.AllText.DOCUMENT_PROVIDER;
import static ru.tubi.project.free.AllText.LOAD_TEXT;

public class InvoiceProviderPDFActivity extends AppCompatActivity {

    private String docName, out_companyInfoString, in_companyInfoString,
            out_warehouseInfoString, in_warehouseInfoString, date_created_doc;
    private int docNum, order_partner_id=0, invoice_key_id;
    Intent intent, takeit;
    private ArrayList<InvoiceModel> invoice_list = new ArrayList<>();

    private PDFView pdfView;
    private File file;
    private Bitmap bmt, scaledbmt;
    private int pageWidth = 1240;
    private int pageHeght = 1754;
    private int pageNumber = 1;
    private int positionCount = 0, allProductQuantity=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_provider_p_d_f);
        setTitle(DOCUMENT_PROVIDER);//документ поставщика

        pdfView=findViewById(R.id.pdfView);
        bmt = BitmapFactory.decodeResource(getResources(),R.drawable.tubi_logo_200ps);
        scaledbmt = Bitmap.createScaledBitmap(bmt, 100,90,false);


        //разрешения
        ActivityCompat.requestPermissions(this,new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);
        ActivityCompat.requestPermissions(this,new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);


        takeit = getIntent();
        order_partner_id = takeit.getIntExtra("order_partner_id",0);
        invoice_key_id = takeit.getIntExtra("invoice_key_id",0);
        docName = takeit.getStringExtra("docName");
        docNum = takeit.getIntExtra("docNum",0);

        //показать документ
        showDocument();
    }
    //показать документ
    private void showDocument(){
        String url = Constant.PROVIDER_OFFICE;
        url += "show_document";
        url += "&"+"order_partner_id="+order_partner_id;
        url += "&"+"invoice_key_id="+invoice_key_id;
        url += "&"+"docName="+docName;
        url += "&"+"docNum="+docNum;
        String whatQuestion = "show_document";
        setInitialData(url, whatQuestion);

       // Log.d("A111","InvoiceProviderPDFActivity / showDocument \n " +url);
        //Toast.makeText(this, "InvoicePDFActivity / showDocument \n " +url, Toast.LENGTH_SHORT).show();
    }
    private void setInitialData(String url_get, String whatQuestion) {
        ProgressDialog asyncDialog = new ProgressDialog(this);
        //проверка соединения интернета
        if ( !isOnline() ){
            Toast.makeText(getApplicationContext(),
                    "Нет соединения с интернетом!",Toast.LENGTH_LONG).show();
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
                if(whatQuestion.equals("show_document")){
                    splitInvoiceResult(result);
                }
                //hide the dialog
                asyncDialog.dismiss();
            }
        };
        task.execute(url_get);
    }
    //получить данные документа
    private void splitInvoiceResult(String result){
        invoice_list.clear();
        Log.d("A111","InvoiceProviderPDFActivity / splitInvoiceResult \n res: " +result);
        //Toast.makeText(this, "res\n"+result, Toast.LENGTH_SHORT).show();
        try{
            String[] res = result.split("<br>");
            String[] one_temp = res[0].split("&nbsp");
            if (one_temp[0].equals("error") || one_temp[0].equals("messege")) {
                Toast.makeText(this, "" + one_temp[1], Toast.LENGTH_LONG).show();
                return;
            }
            else {
                for (int i = 0; i < res.length; i++) {
                    String[] temp = res[i].split("&nbsp");
                    if (temp[0].equals("out_companyInfoString")){
                        out_companyInfoString = temp[1];
                    } else if (temp[0].equals("in_companyInfoString")){
                        in_companyInfoString = temp[1];
                    } else if (temp[0].equals("out_warehouseInfoString")){
                        out_warehouseInfoString = temp[1];
                    } else if (temp[0].equals("in_warehouseInfoString")){
                        in_warehouseInfoString = temp[1];
                    } else if (temp[0].equals("date_created_doc")){
                        date_created_doc = temp[1];
                    }else{

                        String description_docs = temp[0];
                        double quantity = Double.parseDouble(temp[1]);
                        double full_price = Double.parseDouble(temp[2]);
                        String product_name_from_provider = temp[3];
                        int quantity_package = Integer.parseInt(temp[4]);

                        InvoiceModel order_info = new InvoiceModel(description_docs
                                ,quantity, full_price, product_name_from_provider
                                ,quantity_package);
                        invoice_list.add(order_info);
                    }
                }
            }
            createdPDF();

            pdfView.fromFile(file).load();

        }catch(Exception ex){
            Toast.makeText(this, "Exception: "+ex, Toast.LENGTH_SHORT).show();
        }
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
        canvas.drawText("Заказ № "+order_partner_id, pageWidth-70,60,myPaint);
        /* canvas.drawText("8-985-138-00-xx]", 1160,80,myPaint);*/

        titlePaint.setTextAlign(Paint.Align.CENTER);
        titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.BOLD));
        titlePaint.setTextSize(25f);
        //dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        String myDocName = new FirstSimbolMakeBig().firstSimbolMakeBig(docName);
        canvas.drawText(myDocName+" № "+docNum+" от "+date_created_doc+" г."
                , pageWidth/2,150,titlePaint);

        myPaint.setStrokeWidth(2f);
        canvas.drawLine(70,155,pageWidth-60,155,myPaint);

        myPaint.setTextAlign(Paint.Align.LEFT);
        myPaint.setTextSize(18f);
        myPaint.setColor(Color.BLACK);
        canvas.drawText("Поставщик (представитель поставщика): ", 70,180,myPaint);
        titlePaint.setTextAlign(Paint.Align.LEFT);
        titlePaint.setTextSize(18f);
        canvas.drawText(out_companyInfoString,100,205,titlePaint);
        canvas.drawText(out_warehouseInfoString,100,235,titlePaint);

        myPaint.setTextAlign(Paint.Align.LEFT);
        myPaint.setTextSize(18f);
        myPaint.setColor(Color.BLACK);
        canvas.drawText("Покупатель: ", 70,270,myPaint);

        canvas.drawText(in_companyInfoString,100,295,titlePaint);
        canvas.drawText(in_warehouseInfoString,100,320,titlePaint);


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
        canvas.drawText("к-во уп",850,360,titlePaint);
        canvas.drawText("цена",950,360,titlePaint);
        canvas.drawText("сумма",1050,360,titlePaint);

        canvas.drawLine(140,335,140,365,myPaint);
        canvas.drawLine(700,335,700,365,myPaint);
        canvas.drawLine(840,335,840,365,myPaint);
        canvas.drawLine(930,335,930,365,myPaint);
        canvas.drawLine(1030,335,1030,365,myPaint);


        //заполняем список товаров
        double total = 0, totalSumm=0;
        int y=395;
        myPaint.setTextAlign(Paint.Align.LEFT);
        myPaint.setStyle(Paint.Style.FILL);
        myPaint.setTextSize(18f);
        for(int i=0;i<invoice_list.size();i++){
            positionCount=i;
            allProductQuantity+=invoice_list.get(i).getQuantity();
            String productDescription = invoice_list.get(i).getDescription_docs()
                    +" - ("+invoice_list.get(i).getProduct_name_from_provider()+")";

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
            canvas.drawText(String.valueOf(invoice_list.get(i).getQuantity()),800,y,myPaint);

            myPaint.setTextAlign(Paint.Align.LEFT);
            myPaint.setTextSize(14f);
            canvas.drawText("шт",805,y,myPaint);

            myPaint.setTextAlign(Paint.Align.RIGHT);
            double quantity_to_package = invoice_list.get(i).getQuantity() / invoice_list.get(i).getQuantity_package();
            canvas.drawText("("+String.format("%.2f",quantity_to_package)+")",925,y,myPaint);

            myPaint.setTextAlign(Paint.Align.RIGHT);
            myPaint.setTextSize(18f);
            canvas.drawText(String.format("%.2f",invoice_list.get(i).getPrice()),1025,y,myPaint);

            total = invoice_list.get(i).getPrice() * invoice_list.get(i).getQuantity();
            //total = productList.get(i).getPrice() * productList.get(i).getQuantity();
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

        //создать директорию если нет
        final String folderPath = giveFolderPath();
        //final String folderPath = Environment.getExternalStorageDirectory() + "/tubi/documents";
        File folder = new File(folderPath);
        if (!folder.exists()) {
            File wallpaperDirectory = new File(folderPath);
            wallpaperDirectory.mkdirs();
        }

        String filePath = makeFilePath();
        //String filePath = "invoice_see_"+111+".pdf";
        String fileDir = makeFileDir();
        //String fileDir = "tubi/documents/";

        //запись файла в директорию(память телефона)
        File fileWrite = new File(Environment.getExternalStorageDirectory(),"/"+fileDir+filePath);
        //File fileWrite = new File(Environment.getExternalStorageDirectory(),"/PDFpage2.pdf");
        try {
            myPdfDocument.writeTo(new FileOutputStream(fileWrite));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //просмотр файла
        file = new File(this.getExternalFilesDir(fileDir),filePath);
        //file = new File(this.getExternalFilesDir("/"),"PDFpage2.pdf");
        try {
            myPdfDocument.writeTo(new FileOutputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }

        myPdfDocument.close();

    }
   /* private void goShareFile(){

        String filePath = makeFilePath();
        String fileDir = makeFileDir();
        //Uri screenshotUri = Uri.parse(this.getExternalFilesDir(fileDir)+"/"+filePath);
        Uri screenshotUri = Uri.parse("android.resource://"+fileDir+filePath);

        Log.d("A111","InvoiceProviderPDFActivity / goShareFile uri= "+ screenshotUri.toString());
        //screenshotUri = Uri.withAppendedPath (screenshotUri, filePath);

        String[] emailArr = {"kent7755@ya.ru"};
        ArrayList<Uri> imageUris = new ArrayList<Uri>();
        imageUris.add(screenshotUri);

        Intent sharingIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
        //sharingIntent.putExtra(Intent.EXTRA_EMAIL, emailArr);
       // sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Your subject here");
        //sharingIntent.putExtra(Intent.EXTRA_TEXT, "shareBody");
        sharingIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris);
        sharingIntent.setType("application/pdf");
        startActivity(Intent.createChooser(sharingIntent, null));
    }*/
    //найти или создать директорию хранения
    private String giveFolderPath(){
        String folderPath="";
        if(docName.equals("товарная накладная")){
            folderPath = Environment.getExternalStorageDirectory() + "/tubi/nakladnie";
        }else if(docName.equals("товарный чек")){
            folderPath = Environment.getExternalStorageDirectory() + "/tubi/cheki";
        }
        //final String folderPath = Environment.getExternalStorageDirectory() + "/tubi/nakladnie";

        return folderPath;
    }
    //создать имя файла
    private String makeFilePath(){
        String filePath="", fileName="see_";
        if(docName.equals("товарная накладная")){
            fileName = "tovar_nak_";
        }else if(docName.equals("товарный чек")){
            fileName = "tov_chek_";
        }
        filePath = fileName+docNum+".pdf";
        //String filePath = "invoice_see_"+111+".pdf";

        return filePath;
    }
    // директорию хранения файла
    private String makeFileDir(){
        String fileDir = "tubi/documents/";
        if(docName.equals("товарная накладная")){
            fileDir = "tubi/nakladnie/";
        }else if(docName.equals("товарный чек")){
            fileDir = "tubi/cheki/";
        }
        //String fileDir = "tubi/documents/";

        return fileDir;
    }
    //проверка соединения интернета
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

    public boolean onOptionsItemSelected(MenuItem item){
        int itemID=item.getItemId();

        if(itemID==R.id.main){
            intent=new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        if(itemID==R.id.catalog){
            intent=new Intent(this, ActivityCatalog.class);
            startActivity(intent);
        }
        if(itemID==R.id.menu){
            intent=new Intent(this, MenuActivity.class);
            startActivity(intent);
        }
        if(itemID==R.id.menu_item_share){

            //goShareFile();
            Toast.makeText(this, "Раздел в разработке", Toast.LENGTH_SHORT).show();

        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.invoice_menu,menu);
        return true;
    }
}