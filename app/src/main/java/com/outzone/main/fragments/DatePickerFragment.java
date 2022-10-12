package com.outzone.main.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.fragment.app.DialogFragment;

import com.outzone.main.dialogs.NewMemberDialog;
import com.outzone.main.dialogs.NewEventDialog;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    int year, mont,day;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Fecha actual como predeterminada
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(getActivity(), this, year, month, day);
        //return new DatePickerDialog(getActivity(), AlertDialog.THEME_HOLO_DARK,this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {

        switch (getTag()) {
            case "check_in":
                CheckInFragment.setDateButton(year, month+1, day);
                break;
            case "new_date":
                NewEventDialog.setDateButton(year, month+1, day);
                break;
            case "new_member":
                NewMemberDialog.setDateButton(year,month+1,day);
                break;
        }
        this.year = year;
        this.mont = month;
        this.day = day;
    }

    private int[] getNextSaturday()
    {
        int[] fecha={1,2,3};
        return fecha;
    }
}
