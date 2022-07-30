package ru.tubi.project.utilites;

import android.content.Context;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import static ru.tubi.project.free.AllText.ERROR_BIG;
import static ru.tubi.project.free.AllText.NUM_EXCEED;

public class CheckPhoneNumberInput {
    private Context context;
    private EditText etPhone;
    private CharSequence charSequence;
    private int i;
    private int i1;
    private int i2;
    //etPhone.addTextChangedListener(new TextWatcher() слушатель
    //Activity activity = (Activity) this;

    /*//очистить номер от скобок и тире
        CheckPhoneNumberInput num = new CheckPhoneNumberInput();
        String phoneNumStr = num.clearPhoneNumber(etPhone);
        if(phoneNumStr.length() != 11 ){
            Toast.makeText(this, ""+ENTER_PHONE_NUM_ALL_TEXT, Toast.LENGTH_SHORT).show();
            return;
        }*/
    public  CheckPhoneNumberInput() {
        //полученный очищенный номер проверить на колличество цифр
    }
    //показать номер для пользователя, вернуть со скобками
    //String numberStr =  num.PhoneNumWhithBrackets(phoneNumStr);

    //заполнить номер телефона скобками и тире
    //new CheckPhoneNumberInput(activity, etPhone
    //                        , charSequence, i, i1, i2);
    public CheckPhoneNumberInput(Context context, EditText etPhone
            , CharSequence charSequence, int i, int i1, int i2) {
        //(CharSequence s, int start, int before, int count)
        this.context = context;
        this.etPhone = etPhone;
        this.charSequence = charSequence;
        this.i = i;
        this.i1 = i1;
        this.i2 = i2;

        Log.d("A111","test 1");

        int deleteSimbol = i1;
        //если символ удален то не проверять
        if(deleteSimbol == 1){
            return;
        }
        String phone = etPhone.getText().toString().trim();
        Log.d("A111","test 2");
        char [] phoneChar = phone.toCharArray();
        //предварительно проверить скобки и тире на месте(не удалены)
        char bracketOpen = '(';
        char bracketClose = ')';
        char dash ='-';
        for(int j=0;j < phoneChar.length;j++){
            if(j==0 ){
                try{
                    int firstSimbol = Integer.parseInt(String.valueOf(phoneChar[0]));
                    if(firstSimbol != 8){
                        etPhone.setText("");
                        Toast.makeText(context, "Номер телефона должен начинаться с 8", Toast.LENGTH_SHORT).show();
                        break;
                    }
                }catch (Exception ex){
                    etPhone.setText("");
                    Toast.makeText(context, "Номер телефона должен начинаться с цифры", Toast.LENGTH_SHORT).show();
                    break;
                }
            }else if(j==1 && phoneChar[1] != bracketOpen){
                etPhone.setText(""+phone.substring(0,1)+"("+phoneChar[1]);
                break;
            }else if(j==5 && phoneChar[5] != bracketClose){
                etPhone.setText(""+phone.substring(0,5)+")"+phoneChar[5]);
                break;
            }else if(j==9 && phoneChar[9] != dash){
                etPhone.setText(""+phone.substring(0,9)+"-"+phoneChar[9]);
                break;
            }else if(j==12 && phoneChar[12] != dash){
                etPhone.setText(""+phone.substring(0,12)+"-"+phoneChar[12]);
                break;
            }else if(j==2||j==3||j==4||j==6||j==7||j==8||j==10||j==11||j==13||j==14){
                try{
                    int x = Integer.parseInt(String.valueOf(phoneChar[j]));
                    //Log.d("A111"," x = "+x+"; j="+j);
                }catch (Exception ex){
                    etPhone.setText(phone.substring(0,(j-1)));
                    Toast.makeText(context, "ERROR_BIG", Toast.LENGTH_SHORT).show();
                    Log.d("A111"," error ");
                }

            } //максимум 15 символов
            else if(j == 15){
                etPhone.setText(phone.substring(0,15));
                Toast.makeText(context, ""+ERROR_BIG+"\n"+NUM_EXCEED, Toast.LENGTH_SHORT).show();
            }
        }
        //курсор в конец текста
        etPhone.setSelection(etPhone.getText().length());
    }

    //очистить номер от скобок и тире
    public String clearPhoneNumber(EditText etPhone) {

        String phoneNum = etPhone.getText().toString().trim();
        char [] phoneChar = phoneNum.toCharArray();
        String phoneStr = "";
        for(int i=0;i < phoneChar.length;i++){
            try{
                int x = Integer.parseInt(String.valueOf(phoneChar[i]));
                phoneStr += x;
                Log.d("A111"," xx = "+x+"; num="+String.valueOf(phoneChar[i]));
            }catch (Exception ex){
                Log.d("A111","ERROR char="+String.valueOf(phoneChar[i]));
            }
        }
        //tvClearPhoneNum.setText(phoneStr);
        return phoneStr;
    }

    //показать номер для пользователя, вернуть со скобками
    public String PhoneNumWhithBrackets(String phoneNumStr){
        char [] phoneChar = phoneNumStr.toCharArray();

        String number = "";

        for(int i=0;i < phoneChar.length;i++){
            if(i==0)      number += phoneChar[0]+"(";
            else if(i==1) number += phoneChar[1];
            else if(i==2) number += phoneChar[2];
            else if(i==3) number += phoneChar[3]+")";
            else if(i==4) number += phoneChar[4];
            else if(i==5) number += phoneChar[5];
            else if(i==6) number += phoneChar[6]+"-";
            else if(i==7) number += phoneChar[7];
            else if(i==8) number += phoneChar[8]+"-";
            else if(i==9) number += phoneChar[9];
            else if(i==10) number += phoneChar[10];
            else if(i > 10){
                number += ":"+phoneChar[i];
            }
        }
        return number;
    }

}
