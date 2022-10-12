package com.outzone.main.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.outzone.main.R;
import com.outzone.main.ddbb.member.Member;

import java.util.List;

public class MemberListAdapter extends RecyclerView.Adapter<MemberListAdapter.MemberViewHolder> {

    private final LayoutInflater mInflater;
    private List<Member> mMembers; // Cached copy of words

    public MemberListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public MemberViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recyclerview_item, parent, false);
        return new MemberViewHolder(itemView);
    }

    /**
     * Aquí es dónde escribimos sobre el ViewHolder
     * */
    @Override
    public void onBindViewHolder(MemberViewHolder holder, int position) {
        if (mMembers != null) {
            Member current = mMembers.get(position);
            holder.MemberItemView.setText(current.getMember());
        } else {
            //En caso de que los datos no estén listos aún.
            holder.MemberItemView.setText(R.string.no_Member);
        }
    }

    /**
     * Para asociar una lista de usuarios con este adaptador
     * se le llama en la actividad
     */
    public void setMembers(List<Member> members) {
        mMembers = members;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mMembers != null)
            return mMembers.size();
        else return 0;
    }

    /**
     * Retorna el usuario en una posición concreta, para la selección y borrado.
     */
    public Member getMemberAtPosition(int position) {
        return mMembers.get(position);
    }


    class MemberViewHolder extends RecyclerView.ViewHolder {
        private final TextView MemberItemView;

        private MemberViewHolder(View itemView) {
            super(itemView);
            MemberItemView = itemView.findViewById(R.id.textView);
        }
    }

}
