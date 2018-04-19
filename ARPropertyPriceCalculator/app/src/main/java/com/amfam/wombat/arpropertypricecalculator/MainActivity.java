package com.amfam.wombat.arpropertypricecalculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /** Called when the user clicks the Next button on home screen */
    public void goToScreen2(View view) {
        // Do something in response to button
        Button button = (Button) view;
        ((Button) view).setText("clicked");
    }

}
