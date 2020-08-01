package com.zeyufu.example;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.zeyufu.scalabledottedbar.R;
import com.zeyufu.scalabledottedbar.ScalableDottedBar;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    private ScalableDottedBar barDotted;
    private Button btnBack;
    private Button btnNext;
    private Button btnRefresh;

    private SeekBar barNumDots;
    private SeekBar barMaxInit;
    private SeekBar barLevel;
    private SeekBar barSpacing;
    private SeekBar barRadius;

    private TextView txtNumDots;
    private TextView txtMaxInit;
    private TextView txtLevel;
    private TextView txtSpacing;
    private TextView txtRadius;

    DecimalFormat df;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        barDotted = findViewById(R.id.barDotted);
        btnBack = findViewById(R.id.btnBack);
        btnNext = findViewById(R.id.btnNext);
        btnRefresh = findViewById(R.id.btnRefresh);

        barNumDots = findViewById(R.id.barNumDots);
        barMaxInit = findViewById(R.id.barMaxInit);
        barLevel = findViewById(R.id.barLevel);
        barSpacing = findViewById(R.id.barSpacing);
        barRadius = findViewById(R.id.barRadius);

        txtNumDots = findViewById(R.id.txtNumDots);
        txtMaxInit = findViewById(R.id.txtMaxInit);
        txtLevel = findViewById(R.id.txtLevel);
        txtSpacing = findViewById(R.id.txtSpacing);
        txtRadius = findViewById(R.id.txtRadius);

        btnBack.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        btnRefresh.setOnClickListener(this);

        barNumDots.setOnSeekBarChangeListener(this);
        barMaxInit.setOnSeekBarChangeListener(this);
        barLevel.setOnSeekBarChangeListener(this);
        barSpacing.setOnSeekBarChangeListener(this);
        barRadius.setOnSeekBarChangeListener(this);

        df = new DecimalFormat("#.#");
        updateSettingsBar();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnBack:
                barDotted.back();
                break;
            case R.id.btnNext:
                barDotted.next();
                break;
            case R.id.btnRefresh:
                updateDottedBar();
                break;
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        switch (seekBar.getId()) {
            case R.id.barNumDots:
                txtNumDots.setText(String.valueOf(i));
                break;
            case R.id.barMaxInit:
                txtMaxInit.setText(String.valueOf(i + 4));
                break;
            case R.id.barLevel:
                txtLevel.setText(String.valueOf(i + 2));
                break;
            case R.id.barSpacing:
                float sp = ((float) i / (float) seekBar.getMax()) * 72;
                txtSpacing.setText(df.format(sp));
                break;
            case R.id.barRadius:
                float r = ((float) i / (float) seekBar.getMax()) * 18;
                txtRadius.setText(df.format(r));
                break;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    private void updateSettingsBar() {
        barNumDots.setProgress(barDotted.getNumberOfDots());
        barMaxInit.setProgress(barDotted.getMaxInitialDots() - 4);
        barLevel.setProgress(barDotted.getLevelOfScale() - 2);
        int sp = (int) ((barDotted.getSpacing() / 72) * (float) barSpacing.getMax());
        barSpacing.setProgress(sp);
        int r = (int) ((barDotted.getRadius() / 18) * (float) barRadius.getMax());
        barRadius.setProgress(r);

        txtNumDots.setText(String.valueOf(barDotted.getNumberOfDots()));
        txtMaxInit.setText(String.valueOf(barDotted.getMaxInitialDots()));
        txtLevel.setText(String.valueOf(barDotted.getLevelOfScale()));
        txtSpacing.setText(df.format(barDotted.getSpacing()));
        txtRadius.setText(df.format(barDotted.getRadius()));
    }

    private void updateDottedBar() {
        barDotted.setNumDots(barNumDots.getProgress());
        barDotted.setMaxInitial(barMaxInit.getProgress() + 4);
        barDotted.setScaleLevel(barLevel.getProgress() + 2);
        float sp = ((float) barSpacing.getProgress() / (float) barSpacing.getMax()) * 72;
        barDotted.setSpacing(sp);
        float r = ((float) barRadius.getProgress() / (float) barRadius.getMax()) * 18;
        barDotted.setRadius(r);
        barDotted.refresh();
    }
}
