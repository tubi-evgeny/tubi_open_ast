package ru.tubi.project.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import ru.tubi.project.R;
import static ru.tubi.project.free.VariablesHelpers.MESSAGE_FROM_ORDER_ACTIVITY;
import static ru.tubi.project.free.AllText.YOUR_MESSAGE;

public class MessageOrderActivity extends AppCompatActivity {

    EditText etMessageOrder;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_order);
        setTitle(YOUR_MESSAGE);//ВАШИ ПОЖЕЛАНИЯ

        etMessageOrder = findViewById(R.id.etMessageOrder);

        intent = getIntent();

        MESSAGE_FROM_ORDER_ACTIVITY = intent.getStringExtra("message");
        etMessageOrder.setText(""+MESSAGE_FROM_ORDER_ACTIVITY);

       // делаем клавиатуру видимой
        etMessageOrder.requestFocus();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

    }
    // "Вернуть текст" button click
    public void BackMassage(View view) {

        String buyerMessage = etMessageOrder.getText().toString();

        // поместите строку для передачи обратно в intent и закрыть это действие
        intent = new Intent();
        intent.putExtra("message", buyerMessage);
        setResult(RESULT_OK, intent);
        finish();
    }
}