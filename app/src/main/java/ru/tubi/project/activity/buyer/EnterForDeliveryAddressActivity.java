package ru.tubi.project.activity.buyer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import ru.tubi.project.R;

import static ru.tubi.project.free.AllText.IN_YOUR_CITY_IS_NOT_DELIVERY;
import static ru.tubi.project.free.VariablesHelpers.MY_CITY;
import static ru.tubi.project.free.VariablesHelpers.MY_REGION;

public class EnterForDeliveryAddressActivity extends AppCompatActivity {

    private TextView tvRegionDistrictCity;
    private EditText etStreet, etHause, etBuilding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_for_delivery_address);

        tvRegionDistrictCity = findViewById(R.id.tvRegionDistrictCity);
        etStreet = findViewById(R.id.etStreet);
        etHause = findViewById(R.id.etHause);
        etBuilding = findViewById(R.id.etBuilding);

        if(MY_CITY.equals("Другой город")){
            tvRegionDistrictCity.setText(""+IN_YOUR_CITY_IS_NOT_DELIVERY);
        }else{
            tvRegionDistrictCity.setText(""+MY_REGION+" "+MY_CITY);
        }
    }
}