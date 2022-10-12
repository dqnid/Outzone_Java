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
import com.outzone.main.ddbb.member.Member;
import com.outzone.main.ddbb.member.MemberViewModel;
import com.outzone.main.tools.DateManager;
import com.outzone.main.fragments.DatePickerFragment;

public class NewMemberDialog extends Dialog implements View.OnClickListener{
    Context context;
    Fragment fragment;
    static Button buttonDate;
    Button buttonCancel;
    private Button buttonAdd;
    private MemberViewModel mMemberViewModel;
    private static String date;

    public NewMemberDialog(@NonNull Context context, Fragment fragment, MemberViewModel mMemberViewModel) {
        super(context);
        this.context = context;
        this.fragment = fragment;
        this.mMemberViewModel = mMemberViewModel;
    }

    @Override
    public void onClick(View view) {
        dismiss();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_member_new);

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

        buttonAdd = findViewById(R.id.add_member_button);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addEvent();
            }
        });

        buttonCancel = findViewById(R.id.cancel_add_member_button);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(fragment.getParentFragmentManager(), "new_member");
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
        TextInputEditText name = findViewById(R.id.edit_name);
        TextInputEditText sur1 = findViewById(R.id.edit_surname_1);
        TextInputEditText sur2 = findViewById(R.id.edit_surname_2);
        TextInputEditText dni = findViewById(R.id.edit_dni);

        if (name.getText() == null || date == null || sur1.getText() == null || sur2.getText() == null || dni.getText() == null) {
            Toast.makeText(fragment.getContext(), R.string.failed_event_add, Toast.LENGTH_LONG).show();
            dismiss();
            return;
        } else if(name.getText().length() < 1 || date.length() < 1 || sur1.length() < 1 || sur2.length() < 1 || dni.length() <1)
        {
            Toast.makeText(fragment.getContext(), R.string.failed_event_add, Toast.LENGTH_LONG).show();
            dismiss();
            return;
        } else {
            Member member = new Member(dni.getText().toString(),name.getText().toString(),sur1.getText().toString(),sur2.getText().toString(),date);
            mMemberViewModel.insert(member);
        }

        dismiss();
    }
}
