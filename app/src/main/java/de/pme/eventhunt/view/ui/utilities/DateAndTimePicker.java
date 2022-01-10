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

public class DateAndTimePicker implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    Context context;
    TextInputEditText editText;

    DateAndTime dateAndTime;

    int day = 0;
    int month = 0;
    int year = 0;
    int hour = 0;
    int minute = 0;


    public DateAndTimePicker(Context context, TextInputEditText textInputEditText)
    {
        this.context = context;
        dateAndTime = new DateAndTime();
        editText = textInputEditText;
    }

    public DateAndTime getDateAndTime()
    {
        return dateAndTime;
    }

    private void getDateTimeCalendar() {
        Calendar cal = Calendar.getInstance();
        day = cal.get(Calendar.DAY_OF_MONTH);
        month = cal.get(Calendar.MONTH);
        year = cal.get(Calendar.YEAR);
        hour = cal.get(Calendar.HOUR);
        minute = cal.get(Calendar.MINUTE);
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
    getDateTimeCalendar();
    new DatePickerDialog(context, this, year, month, day).show();
    }

    @SuppressLint("ClickableViewAccessibility")
    public void pickOnlyDate()
    {
        getDateCalendar();
        new DatePickerDialog(context, this, year, month, day).show();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        if(day < 10) dateAndTime.day = "0"+day;
        else dateAndTime.day = String.valueOf(day);
        if(month < 10) dateAndTime.month = "0"+(month+1);
        else dateAndTime.month = String.valueOf(month+1);
        dateAndTime.year = String.valueOf(year);

        getDateTimeCalendar();

        new TimePickerDialog(context, this, hour, minute, true).show();

    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
        if(hour < 10) dateAndTime.hour = "0"+String.valueOf(hour);
        else dateAndTime.hour = String.valueOf(hour);

        if(minute < 10) dateAndTime.minute = "0"+(minute);
        else dateAndTime.minute = String.valueOf(minute);

        showDate();
    }

    private void showDate() {
        this.editText.setText(new String(dateAndTime.day+"."
                        + dateAndTime.month + "."
                        + dateAndTime.year + "  "
                        + dateAndTime.hour + ":"
                        + dateAndTime.minute));
    }
}
