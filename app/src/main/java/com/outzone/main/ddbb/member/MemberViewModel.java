package com.outzone.main.ddbb.member;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class MemberViewModel extends AndroidViewModel {
    private MemberRepository mRepository;

    private LiveData<List<Member>> mAllMembers;

    public MemberViewModel(Application application) {
        super(application);
        mRepository = new MemberRepository(application);
        mAllMembers = mRepository.getAllMembers();
    }

    public LiveData<List<Member>> getAllMembers() {
        return mAllMembers;
    }

    public void insert(Member member) {
        mRepository.insert(member);
    }

    public void deleteAll() {
        mRepository.deleteAll();
    }

    public void deleteMember(Member member) {
        mRepository.deleteMember(member);
    }
}