package com.outzone.main.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.outzone.main.R;
import com.outzone.main.ddbb.event.Event;
import com.outzone.main.ddbb.event.EventViewModel;
import com.outzone.main.tools.DateManager;
import com.outzone.main.fragments.DatePickerFragment;

public class NewEventDialog extends Dialog implements View.OnClickListener{
    Context context;
    Fragment fragment;
    static Button buttonDate;
    Button buttonCancel;
    private Button buttonAdd;
    private EventViewModel mEventViewModel;
    private static String date;

    public NewEventDialog(@NonNull Context context, Fragment fragment, EventViewModel mEventViewModel) {
        super(context);
        this.context = context;
        this.fragment = fragment;
        this.mEventViewModel = mEventViewModel;
    }

    @Override
    public void onClick(View view) {
        dismiss();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_event_new);

        //Resultado de que sea estática, posible fallo de diseño inicial
        date = null;

        /**
         * Gestiono los botones
         **/
        buttonDate = findViewById(R.id.add_select_date);
        buttonDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(view);
            }
        });

        buttonAdd = findViewById(R.id.add_event_button);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addEvent();
            }
        });

        buttonCancel = findViewById(R.id.cancel_add_event_button);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(fragment.getParentFragmentManager(), "new_date");
    }

    public static void setDateButton(int year, int month, int day)
    {
        if (buttonDate != null)
        {
            buttonDate.setText(String.format("%d/%d/%d", day, month, year));
            date = DateManager.formatDate(year,month,day);
        }
    }

    public void addEvent()
    {
        TextInputEditText name;
        TextInputEditText desc;
        TextInputEditText url;
        TextInputEditText location;

        name = findViewById(R.id.edit_name);
        desc = findViewById(R.id.edit_description);
        url = findViewById(R.id.edit_url);
        location = findViewById(R.id.edit_location);

        if (name.getText() == null || date == null) {
            Toast.makeText(fragment.getContext(), R.string.failed_event_add, Toast.LENGTH_LONG).show();
            dismiss();
            return;
        } else if(name.getText().length() < 1 || date.length() < 1)
        {
            Toast.makeText(fragment.getContext(), R.string.failed_event_add, Toast.LENGTH_LONG).show();
            dismiss();
            return;
        } else {
            Event event = new Event("0", date, name.getText().toString(), desc.getText().toString(), url.getText().toString(), location.getText().toString());
            mEventViewModel.insert(event);
        }

        dismiss();
    }
}
