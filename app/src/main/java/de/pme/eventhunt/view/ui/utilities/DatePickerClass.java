package de.pme.eventhunt.view.ui.utilities;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;

public class DatePickerClass implements DatePickerDialog.OnDateSetListener {

    Context context;
    TextInputEditText editText;

    Date date;

    int day = 0;
    int month = 0;
    int year = 0;



    public DatePickerClass(Context context, TextInputEditText textInputEditText)
    {
        this.context = context;
        date = new Date();
        editText = textInputEditText;
    }

    public Date getDate()
    {
        return date;
    }

    private void getDateCalendar() {
        Calendar cal = Calendar.getInstance();
        day = cal.get(Calendar.DAY_OF_MONTH);
        month = cal.get(Calendar.MONTH);
        year = cal.get(Calendar.YEAR);

    }


    @SuppressLint("ClickableViewAccessibility")
    public void pickDate()
    {
        getDateCalendar();
        new DatePickerDialog(context, this, year, month, day).show();

    }



    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        if(dayOfMonth < 10) date.day = "0"+dayOfMonth;
        else date.day = String.valueOf(dayOfMonth);
        if(month < 9) date.month = "0"+(month +1);
        else date.month = String.valueOf(month +1);
        date.year = String.valueOf(year);

        getDateCalendar();

        showDate();
    }

    private void showDate() {
        this.editText.setText(new String(date.day+"."
                + date.month + "."
                + date.year ));
    }
}
