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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;

import ru.tubi.project.R;
import ru.tubi.project.activity.ActivityCatalog;
import ru.tubi.project.activity.MainActivity;
import ru.tubi.project.activity.MenuActivity;
import ru.tubi.project.models.CarrierPanelModel;
import ru.tubi.project.models.InvoiceModel;
import ru.tubi.project.models.UserModel;
import ru.tubi.project.utilites.Constant;
import ru.tubi.project.utilites.FirstSimbolMakeBig;
import ru.tubi.project.utilites.InitialData;
import ru.tubi.project.utilites.UserDataRecovery;

import static ru.tubi.project.activity.Config.PARTNER_COMPANY_TAXPAYER_ID_FOR_AGENT;
import static ru.tubi.project.free.AllText.LOAD_TEXT;
import static ru.tubi.project.free.AllText.PRICE_LIST;
import static ru.tubi.project.free.VariablesHelpers.DELIVERY_TO_BUYER_STATUS;
import static ru.tubi.project.free.VariablesHelpers.MY_CITY;
import static ru.tubi.project.free.VariablesHelpers.MY_REGION;

public class DownloadFullPricePDFActivity extends AppCompatActivity {

    private String docName="lll", out_companyInfoString="aaa", in_companyInfoString="bbb",
            out_warehouseInfoString="ccc", in_warehouseInfoString="ddd", date_created_doc="12345";
    private String catalogName, categoryName;
    private int docNum=999, order_partner_id=0, invoice_key_id;
    private Intent intent, takeit;
    private ArrayList<InvoiceModel> invoice_list = new ArrayList<>();
    private String myDate = new SimpleDateFormat("dd.MM.yyyy").format(Calendar.getInstance().getTime());
    private boolean categoryFlag = false, flag = false;
    private PDFView pdfView;
    private File file;
    private Bitmap bmt, scaledbmt, qr_scaledbmt;
    private int pageWidth = 1240;
    private int pageHeght = 1754;
    private int pageNumber = 1;
    private UserModel userDataModel;
    private int positionCount = 0, allProductQuantity=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_full_price_p_d_f);
        setTitle("?????????? PDF");
       // getSupportActionBar().setSubtitle("");

        pdfView=findViewById(R.id.pdfView);
        bmt = BitmapFactory.decodeResource(getResources(),R.drawable.tubi_logo_200ps);
        scaledbmt = Bitmap.createScaledBitmap(bmt, 100,90,false);
        bmt = BitmapFactory.decodeResource(getResources(),R.drawable.qrc_tubi);
        qr_scaledbmt = Bitmap.createScaledBitmap(bmt, 130,130,false);

        //????????????????????
        ActivityCompat.requestPermissions(this,new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);
        ActivityCompat.requestPermissions(this,new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

        //???????????????? ???? sqlLite ???????????? ???????????????????????? ?? ????????????????
        UserDataRecovery userDataRecovery = new UserDataRecovery();
        userDataModel = userDataRecovery.getUserDataRecovery(this);

        //???????????????? ????????????????
        showDocument();
    }
    //???????????????? ????????????????
    private void showDocument(){
        long tax_id = userDataModel.getCompany_tax_id();
        //?????????????????? ?????? ?????????? ?????????????
        if(userDataModel.getRole().equals("sales_agent")){
            tax_id = PARTNER_COMPANY_TAXPAYER_ID_FOR_AGENT;
            Log.d("A111","ActivityCatalog / startList / agent");
        }
        String url = Constant.USER_OFFICE;
        url += "show_full_price";
        url += "&" + "my_city=" + MY_CITY;
        url += "&" + "my_region=" + MY_REGION;
        url += "&" + "taxpayer_id=" + tax_id;
        String whatQuestion = "show_full_price";
        setInitialData(url, whatQuestion);
         Log.d("A111",getClass()+" / showDocument / url=" +url);
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
                if(whatQuestion.equals("show_full_price")){
                    splitInvoiceResult(result);
                    //Log.d("A111",getClass()+" / setInitialData / result="+result);
                }
                //hide the dialog
                asyncDialog.dismiss();
            }
        };
        task.execute(url_get);
    }
    //???????????????? ???????????? ??????????????????
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void splitInvoiceResult(String result){
        invoice_list.clear();
        Log.d("A111",getClass()+" / splitInvoiceResult \n res=" +result);
        //Toast.makeText(this, "res\n"+result, Toast.LENGTH_SHORT).show();
        try{
            //result=result.trim();
            String[] res = result.split("<br>");
            for (int i = 0; i < res.length; i++) {
                String[] temp = res[i].split("&nbsp");
                int product_id = Integer.parseInt(temp[0]);
                int product_inventory_id = Integer.parseInt(temp[1]);
                double price = Double.parseDouble(temp[2]);
                double process_price = Double.parseDouble(temp[3]);
                int min_sell = Integer.parseInt(temp[4]);
                String product_info= temp[5];
                String catalog = temp[6];
                String category = temp[7];

                InvoiceModel order_info = new InvoiceModel(product_id
                        ,product_inventory_id, price, process_price
                        ,min_sell,product_info,catalog, category);
                invoice_list.add(order_info);
            }
            //Log.d("A111",getClass()+" / splitInvoiceResult \n invoice_list size=" +invoice_list.size());
            //?????????????????? ???????? ???? 2 ?????????? (logistic_product ?? car_or_warehouse_id)
            invoice_list.sort(Comparator.comparing(InvoiceModel::getCatalog)
                    .thenComparing(InvoiceModel::getCategory));
            /*
            invoice_list.sort(Comparator.comparing(InvoiceModel::getCatalog)
                    .thenComparing(InvoiceModel::getWarehouse_info_id));
             */
        }catch(Exception ex){
            Log.d("A111",getClass()+" / splitInvoiceResult \n ex=" +ex);
            ex.printStackTrace();
            Toast.makeText(this, "Exception: "+ex, Toast.LENGTH_SHORT).show();
        }
        try{
            createdPDF();
            pdfView.fromFile(file).load();
        }catch(Exception ex){
            Log.d("A111",getClass()+" / splitInvoiceResult \n ex2=" +ex);
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
        canvas.drawBitmap(qr_scaledbmt,pageWidth-190,25,myPaint);

       /* //???????????????????? ?????????????? ????????????????
        canvas.drawText("+", 10,10,myPaint);
        canvas.drawText("+", pageWidth-10,10,myPaint);
        canvas.drawText("+", 10,pageHeght-10,myPaint);
        canvas.drawText("+", pageWidth-10,pageHeght-10,myPaint);*/

        myPaint.setColor(Color.parseColor("#000000"));
        //myPaint.setColor(Color.parseColor("#5c5c5c"));
        myPaint.setTextSize(16f);
        myPaint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText("?????? ??????????????", pageWidth-195,145,myPaint);
         /*canvas.drawText("8-985-138-00-xx]", 1160,80,myPaint);*/

        titlePaint.setTextAlign(Paint.Align.CENTER);
        titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.BOLD));
        titlePaint.setTextSize(25f);

        canvas.drawText(PRICE_LIST+" ???? "+myDate+" ??."
                , pageWidth/2,145,titlePaint);
        int y=155;
       // myPaint.setStrokeWidth(2f);
        //canvas.drawLine(70,155,pageWidth-60,155,myPaint);

       /* myPaint.setTextAlign(Paint.Align.LEFT);
        myPaint.setTextSize(18f);
        myPaint.setColor(Color.BLACK);
        canvas.drawText("?????????????????? (?????????????????????????? ????????????????????): ", 70,180,myPaint);
        titlePaint.setTextAlign(Paint.Align.LEFT);
        titlePaint.setTextSize(18f);
        canvas.drawText(out_companyInfoString,100,205,titlePaint);
        canvas.drawText(out_warehouseInfoString,100,235,titlePaint);

        myPaint.setTextAlign(Paint.Align.LEFT);
        myPaint.setTextSize(18f);
        myPaint.setColor(Color.BLACK);
        canvas.drawText("????????????????????: ", 70,270,myPaint);

        canvas.drawText(in_companyInfoString,100,295,titlePaint);
        canvas.drawText(in_warehouseInfoString,100,320,titlePaint);*/


        myPaint.setStyle(Paint.Style.STROKE);
        myPaint.setStrokeWidth(3f);
        canvas.drawRect(70,y,pageWidth-60,y+40,myPaint);

        titlePaint.setTextSize(18f);
        titlePaint.setTextAlign(Paint.Align.LEFT);
        titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.BOLD));
        titlePaint.setColor(Color.parseColor("#000000"));
        //titlePaint.setStyle(Paint.Style.FILL);

        canvas.drawText("???",90,y+30,titlePaint);
        canvas.drawText("id",160,y+30,titlePaint);
        canvas.drawText("???????????????? ????????????",230,y+30,titlePaint);
        canvas.drawText("?? ????????",920,y+30,titlePaint);
        canvas.drawText("????????",1010,y+30,titlePaint);
        canvas.drawText("???????? ????.",1100,y+30,titlePaint);

        canvas.drawLine(140,y+5,140,y+35,myPaint);
        canvas.drawLine(210,y+5,210,y+35,myPaint);
        //canvas.drawLine(700,y+5,700,y+35,myPaint);
        canvas.drawLine(900,y+5,900,y+35,myPaint);
        canvas.drawLine(990,y+5,990,y+35,myPaint);
        canvas.drawLine(1080,y+5,1080,y+35,myPaint);

       // try{

            //?????????????????? ???????????? ??????????????
        double total = 0, totalSumm=0;
        y+=40;//y+=65;
        myPaint.setTextAlign(Paint.Align.LEFT);
        //myPaint.setStyle(Paint.Style.FILL);
        myPaint.setTextSize(18f);
        myPaint.setStyle(Paint.Style.STROKE);
        myPaint.setStrokeWidth(2f);
        /*if(invoice_list.size() > 0){
            catalogName = invoice_list.get(0).getCatalog().toString();
        }*/
        for(int i=0;i<invoice_list.size();i++){
            if(!invoice_list.get(i).getCatalog().toString().equals(catalogName)){
                titlePaint.setTextSize(18f);
                titlePaint.setTextAlign(Paint.Align.CENTER);
                titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.BOLD));
                catalogName=invoice_list.get(i).getCatalog().toString();
                String myCatalogName=new FirstSimbolMakeBig()
                                        .firstSimbolMakeBig(invoice_list.get(i).getCatalog().toString());
                if(i==0){
                    canvas.drawText(""+myCatalogName,pageWidth/2,y+19,titlePaint);
                    canvas.drawLine(140,y,140,y+30,myPaint);
                    canvas.drawLine(900,y,900,y+30,myPaint);
                    canvas.drawLine(990,y,990,y+30,myPaint);
                    canvas.drawLine(1080,y,1080,y+30,myPaint);
                    canvas.drawLine(pageWidth-60,y,pageWidth-60,y+31,myPaint);
                }else{
                    canvas.drawText(""+myCatalogName,pageWidth/2,y,titlePaint);
                    canvas.drawLine(140,y-17,140,y+5,myPaint);
                    canvas.drawLine(900,y-17,900,y+5,myPaint);
                    canvas.drawLine(990,y-17,990,y+5,myPaint);
                    canvas.drawLine(1080,y-17,1080,y+5,myPaint);
                    canvas.drawLine(pageWidth-60,y-17,pageWidth-60,y+6,myPaint);
                }
                //?????????????? ???????????? ?? ?????????? ???????????????? ???? ????????????????
                y+=23;
                if(i == 0)y+=23;
                //canvas.drawLine(70,y-17,pageWidth-60,y-17,myPaint);

                titlePaint.setTextSize(15f);
                titlePaint.setTextAlign(Paint.Align.LEFT);
                titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.NORMAL));
               // flag = true;
                //categoryFlag = true;
            }
            if(!invoice_list.get(i).getCategory().equals(categoryName)){
                //flag = false;
                titlePaint.setTextSize(18f);
                titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.BOLD));
                categoryName=invoice_list.get(i).getCategory();
                String myCategoryName=new FirstSimbolMakeBig()
                        .firstSimbolMakeBig(invoice_list.get(i).getCategory().toString());

                    canvas.drawText(""+myCategoryName,240,y,titlePaint);
                    canvas.drawLine(140,y-17,140,y+5,myPaint);
                    canvas.drawLine(900,y-17,900,y+5,myPaint);
                    canvas.drawLine(990,y-17,990,y+5,myPaint);
                    canvas.drawLine(1080,y-17,1080,y+5,myPaint);
                    canvas.drawLine(pageWidth-60,y-17,pageWidth-60,y+6,myPaint);

                //?????????????? ???????????? ?? ?????????? ???????????????? ???? ????????????????
                y+=23;
              //  if(i == 0)y+=23;
                canvas.drawLine(70,y-17,pageWidth-60,y-17,myPaint);

                titlePaint.setTextSize(15f);
                titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.NORMAL));
            }
            positionCount=i;
            allProductQuantity+=invoice_list.get(i).getMin_sell();
            String productDescription = invoice_list.get(i).getProduct_info();
            //???????????????? ???????????? ???????????? ?? ?????? ???????????????????? ?????????????????? ?????????????? ?????????????? ????????????
            ArrayList<String> stringList = new ArrayList<>();
            int stringLength = 83;//70;
            if(productDescription.length() > stringLength){//54
                String[]wordsList = productDescription.split(" ");
                String stringLine = "";
                int s=0;
                int wordLength = 0, stringLenght=0;
                for(int j=0;j < wordsList.length; j++){
                    wordLength = wordsList[j].length();
                    if(stringLenght+wordLength+1 < stringLength){//54
                        stringLenght +=wordLength+1;
                        stringLine += wordsList[j]+" ";
                    }else{
                        stringLenght =wordLength+1;
                        stringList.add(stringLine);
                        stringLine = wordsList[j]+" ";
                    }
                }
                stringList.add(stringLine);
            }else{
                stringList.add(productDescription);
            }
            myPaint.setTextAlign(Paint.Align.LEFT);
            titlePaint.setTextAlign(Paint.Align.LEFT);
            canvas.drawText(String.valueOf(i+1),75,y,titlePaint);
            myPaint.setStrokeWidth(2f);
            //???????????????????????? ????????????
            canvas.drawLine(140,y-19,140,y+3,myPaint);
            canvas.drawLine(900,y-19,900,y+3,myPaint);
            canvas.drawLine(990,y-19,990,y+3,myPaint);
            canvas.drawLine(1080,y-19,1080,y+3,myPaint);
            canvas.drawLine(pageWidth-60,y-19,pageWidth-60,y+5,myPaint);
            canvas.drawText(String.valueOf(invoice_list.get(i).getProductInventory_id()),145,y,titlePaint);

            myPaint.setTextAlign(Paint.Align.RIGHT);
            titlePaint.setTextAlign(Paint.Align.RIGHT);
            myPaint.setTextSize(18f);
            canvas.drawText(String.format("%d",invoice_list.get(i).getMin_sell()),980,y,titlePaint);

            total = invoice_list.get(i).getPrice() + invoice_list.get(i).getProcess_price();
            myPaint.setTextAlign(Paint.Align.RIGHT);
            canvas.drawText(String.format("%.2f",total),1070,y,titlePaint);

            double minSellTotalPrice=invoice_list.get(i).getMin_sell()*total;
            canvas.drawText(String.format("%.2f",minSellTotalPrice),pageWidth-65,y,titlePaint);
            //???????????? ?? ?????????????????? ????????????
            myPaint.setTextAlign(Paint.Align.LEFT);
            titlePaint.setTextAlign(Paint.Align.LEFT);
            for(int j=0;j < stringList.size();j++){
                String myText="";
                if(j==0)myText=new FirstSimbolMakeBig().firstSimbolMakeBig(stringList.get(j).toString());
                else myText=stringList.get(j).toString();
                canvas.drawText(""+myText,220,y,titlePaint);
                //???????????????????????? ???????????? ????????????????????
                canvas.drawLine(140,y-15,140,y+3,myPaint);
                canvas.drawLine(900,y-15,900,y+3,myPaint);
                canvas.drawLine(990,y-15,990,y+3,myPaint);
                canvas.drawLine(1080,y-15,1080,y+3,myPaint);
                canvas.drawLine(pageWidth-60,y-15,pageWidth-60,y+5,myPaint);
                //?????????????? ???????????? ?? ?????????? ???????????????? ???? ????????????????
                y+=15;
            }
            y+=5;
            //?????????? ?????? ??????????????
            canvas.drawLine(70,y-16,pageWidth-60,y-16,myPaint);
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
        if(y > pageHeght-370){
            myPdfDocument.finishPage(myPage1);
            pageNumber+=1;
            myPageInfo1 =
                    new PdfDocument.PageInfo.Builder(pageWidth,pageHeght,pageNumber).create();
            myPage1 = myPdfDocument.startPage(myPageInfo1);
            //canvas = myPage1.getCanvas();
            y = 60;
        }
       // y+=20;
        myPdfDocument.finishPage(myPage1);
        //?????????????? ???????????????????? ???????? ??????
        final String folderPath = giveFolderPath();
        File folder = new File(folderPath);
        if (!folder.exists()) {
            File wallpaperDirectory = new File(folderPath);
            wallpaperDirectory.mkdirs();
        }
        String filePath = makeFilePath();
        String fileDir = makeFileDir();
        //???????????? ?????????? ?? ????????????????????(???????????? ????????????????)
        File fileWrite = new File(Environment.getExternalStorageDirectory(),"/"+fileDir+filePath);
        try {
            myPdfDocument.writeTo(new FileOutputStream(fileWrite));
            Toast.makeText(this, "?????????? ????????????????, ?????????? ?????? ?????????? -> ???????????? ?????????????????? -> ?????????? tubi", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //???????????????? ??????????
        file = new File(this.getExternalFilesDir(fileDir),filePath);
        try {
            myPdfDocument.writeTo(new FileOutputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
        myPdfDocument.close();

    }
    //?????????? ?????? ?????????????? ???????????????????? ????????????????
    private String giveFolderPath(){
        String folderPath="";
        folderPath = Environment.getExternalStorageDirectory() + "/tubi/price";
       /* if(docName.equals("???????????????? ??????????????????")){
            folderPath = Environment.getExternalStorageDirectory() + "/tubi/nakladnie";
        }else if(docName.equals("???????????????? ??????")){
            folderPath = Environment.getExternalStorageDirectory() + "/tubi/cheki";
        }*/
        //final String folderPath = Environment.getExternalStorageDirectory() + "/tubi/nakladnie";

        return folderPath;
    }
    //?????????????? ?????? ??????????
    private String makeFilePath(){
        String filePath="", fileName="price_";
        /*if(docName.equals("???????????????? ??????????????????")){
            fileName = "tovar_nak_";
        }else if(docName.equals("???????????????? ??????")){
            fileName = "tov_chek_";
        }*/
        filePath = fileName+myDate+".pdf";
        //String filePath = "invoice_see_"+111+".pdf";

        return filePath;
    }
    // ???????????????????? ???????????????? ??????????
    private String makeFileDir(){
        String fileDir = "tubi/price/";
      /*  if(docName.equals("???????????????? ??????????????????")){
            fileDir = "tubi/nakladnie/";
        }else if(docName.equals("???????????????? ??????")){
            fileDir = "tubi/cheki/";
        }*/
        //String fileDir = "tubi/documents/";

        return fileDir;
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
            Toast.makeText(this, "???????????? ?? ????????????????????", Toast.LENGTH_SHORT).show();

        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.invoice_menu,menu);
        return true;
    }

}