package com.outzone.main.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.outzone.main.R;
import com.outzone.main.dialogs.NewMemberDialog;
import com.outzone.main.ddbb.member.Member;
import com.outzone.main.adapters.MemberListAdapter;
import com.outzone.main.ddbb.member.MemberViewModel;

import java.util.List;

public class MemberListFragment extends Fragment {

    private MemberViewModel mMemberViewModel;
    View rootView;

    public MemberListFragment() {
    }

    public static MemberListFragment newInstance() {
        return new MemberListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        /**
         * Inflo el layout general (calendario)
         **/
        rootView = inflater.inflate(R.layout.fragment_usr_list, container, false);

        /**
         * Inflo el reclycreview
         **/
        RecyclerView recyclerView = rootView.findViewById(R.id.recyclerview);
        final MemberListAdapter adapter = new MemberListAdapter(getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Setup the WordViewModel
        mMemberViewModel = ViewModelProviders.of(this).get(MemberViewModel.class);
        // Get all the words from the database
        // and associate them to the adapter
        mMemberViewModel.getAllMembers().observe(getViewLifecycleOwner(),
                new Observer<List<Member>>() {
                    @Override
                    public void onChanged(@Nullable List<Member> members) {
                        adapter.setMembers(members);
                    }
                });

        // Floating action button setup
        FloatingActionButton fab = rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NewMemberDialog tempNDialog = new NewMemberDialog(view.getContext(), MemberListFragment.this, mMemberViewModel);
                tempNDialog.show();
            }
        });

        /**
         * Borrado de elementos por deslizamiento
         **/
        ItemTouchHelper helper = new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(0,
                        ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    @Override
                    // We are not implementing onMove() in this app
                    public boolean onMove(RecyclerView recyclerView,
                                          RecyclerView.ViewHolder viewHolder,
                                          RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                        int position = viewHolder.getAdapterPosition();
                        Member myMember = adapter.getMemberAtPosition(position);

                        /**
                         * Diálogo de confirmación
                         **/
                        AlertDialog.Builder confirmAlertBuilder = new AlertDialog.Builder(getContext());
                        confirmAlertBuilder.setTitle(getResources().getString(R.string.confirm_delete_title));
                        confirmAlertBuilder.setMessage(getResources().getString(R.string.confirm_delete_message));
                        confirmAlertBuilder.setPositiveButton(getResources().getString(R.string.positive), new
                                DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        mMemberViewModel.deleteMember(myMember);
                                        Toast.makeText(getContext(),
                                                getString(R.string.delete_word_preamble) + " " +
                                                        myMember.getMember(), Toast.LENGTH_LONG).show();
                                    }
                                });
                        confirmAlertBuilder.setNegativeButton(getResources().getString(R.string.negative), new
                                DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        Toast.makeText(getContext(),getResources().getString(R.string.negative_message),Toast.LENGTH_SHORT).show();
                                        adapter.notifyDataSetChanged();
                                    }
                                });
                        confirmAlertBuilder.show();
                    }
                });
        // Attach the item touch helper to the recycler view
        helper.attachToRecyclerView(recyclerView);
        return rootView;
    }
}