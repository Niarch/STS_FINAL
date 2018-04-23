package com.example.niarch.sts_final;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.provider.CalendarContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class MainActivity extends AppCompatActivity {

    private Spinner mNumberOfDaysSpinner;
    private Button mSubmitbutton;
    private Button mSetDateButton;
    private Button mSetTimeButton;
    private TextView mDateTimeTextView;
    private Spinner mBudgetSpinner;
    private RatingBar mReligiousRatingBar, mHistoricalRatingBar, mArtRatingBar, mMarketRatingBar, mScienceRatingBar, mNatureRatingBar;


    private int mNumberOfDays,mBudget;
    private float mReligiousRating,mHistoricalRating,mArtRating,mMarketRating,mScienceRating,mNatureRating;
    public int mYear,mMonth,mDay,mHour,mMinute;


    Calendar dateTime = Calendar.getInstance();
    DateFormat formatDateTime = DateFormat.getDateTimeInstance();


    public static final String baseUrl = "http://10.0.2.2:3001/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mNumberOfDaysSpinner = (Spinner) findViewById(R.id.numberOfDaysSpinner);
        setupNumberOfDaysSpinner();


        mSetDateButton = (Button) findViewById(R.id.setDateButton);
        mSetDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDate();
                validaitonCheck();
            }
        });


        mSetTimeButton = (Button) findViewById(R.id.setTimeButton);
        mSetTimeButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                setTime();
            }
        });


        mDateTimeTextView = (TextView) findViewById(R.id.dateTimeTextView);
        setDateTime();


        mBudgetSpinner = (Spinner) findViewById(R.id.budgetSpinner);
        setupBudgetSpinner();


        mReligiousRatingBar = (RatingBar) findViewById(R.id.religiousRating);
        mHistoricalRatingBar = (RatingBar) findViewById(R.id.historicalRating);
        mArtRatingBar = (RatingBar) findViewById(R.id.artRating);
        mMarketRatingBar = (RatingBar) findViewById(R.id.marketRating);
        mScienceRatingBar = (RatingBar) findViewById(R.id.scienceRating);
        mNatureRatingBar = (RatingBar) findViewById(R.id.natureRating);


        mSubmitbutton = (Button) findViewById(R.id.submitButton);
        mSubmitbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitData();
            }
        });

    }


    private void setupNumberOfDaysSpinner() {


        ArrayAdapter numberOfDaysSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_number_of_day_options, android.R.layout.simple_spinner_item);


        // Specify dropdown layout style - simple list view with 1 item per line
        numberOfDaysSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        // Apply the adapter to the spinner
        mNumberOfDaysSpinner.setAdapter(numberOfDaysSpinnerAdapter);


        // Set the integer mSelected to the constant values
        mNumberOfDaysSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.one_day))) {
                        mNumberOfDays = 1;
                    } else if (selection.equals(getString(R.string.two_day))) {
                        mNumberOfDays=2;
                    } else if (selection.equals(getString(R.string.three_day))) {
                        mNumberOfDays=3;
                    }else if (selection.equals(getString(R.string.four_day))) {
                        mNumberOfDays=4;
                    }else if(selection.equals(getString(R.string.five_day))){
                        mNumberOfDays=5;
                    }
                }
            }


            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mNumberOfDays=1;
            }
        });
    }


    public void setDate(){
        new DatePickerDialog(this,d,dateTime.get(Calendar.YEAR),dateTime.get(Calendar.MONTH),dateTime.get(Calendar.DAY_OF_MONTH)).show();


    }

    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
            dateTime.set(Calendar.YEAR,year);
            dateTime.set(Calendar.MONTH,monthOfYear);
            dateTime.set(Calendar.DAY_OF_MONTH,dayOfMonth);


            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;


            setDateTime();
        }
    };


    public void setTime(){

        new TimePickerDialog(this,t,dateTime.get(Calendar.HOUR),dateTime.get(Calendar.MINUTE),true).show();

    }

    TimePickerDialog.OnTimeSetListener t = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
            dateTime.set(Calendar.HOUR,hourOfDay);
            dateTime.set(Calendar.MINUTE,minute);


            mHour = hourOfDay;
            mMinute = minute;

            setDateTime();

        }
    };


    private  void setDateTime(){

        mDateTimeTextView.setText(formatDateTime.format(dateTime.getTime()));

    }


    private void setupBudgetSpinner() {

        ArrayAdapter budgetSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_budget_options, android.R.layout.simple_spinner_item);


        // Specify dropdown layout style - simple list view with 1 item per line
        budgetSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        // Apply the adapter to the spinner
        mBudgetSpinner.setAdapter(budgetSpinnerAdapter);


        // Set the integer mSelected to the constant values
        mBudgetSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.one_h))) {
                        mBudget = 100;
                    } else if (selection.equals(getString(R.string.two_h))) {
                        mBudget=200;
                    } else if (selection.equals(getString(R.string.three_h))) {
                        mBudget=300;
                    }else if (selection.equals(getString(R.string.four_h))) {
                        mBudget=400;
                    }else if(selection.equals(getString(R.string.five_h))){
                        mBudget=500;
                    }
                }
            }


            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mBudget=200;
            }
        });

    }

    public void validaitonCheck(){
        Long tsLong = System.currentTimeMillis()/1000;
        Timestamp timeStamp = new Timestamp(tsLong);
    }



    public void submitData() {

        String mStartDate = mDay+"-"+mMonth+"-"+mYear;
        String mStartTime = mHour+":"+mMinute;
        mReligiousRating = mReligiousRatingBar.getRating();
        mHistoricalRating = mHistoricalRatingBar.getRating();
        mArtRating = mArtRatingBar.getRating();
        mMarketRating = mMarketRatingBar.getRating();
        mScienceRating = mScienceRatingBar.getRating();
        mNatureRating = mNatureRatingBar.getRating();


        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(baseUrl)
                .build();


        APIInterface api = adapter.create(APIInterface.class);

        api.insertInput(mNumberOfDays,mStartDate,mStartTime,mBudget,mReligiousRating,mHistoricalRating,mArtRating,mMarketRating,mScienceRating,mNatureRating,
                new Callback<Response>() {
                    @Override
                    public void success(Response result, Response response2) {}

                    @Override
                    public void failure(RetrofitError error) {}
                }
        );
    }

}
