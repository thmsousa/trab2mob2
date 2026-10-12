package com.example.trabalho2_150926.View.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.trabalho2_150926.R;
import com.example.trabalho2_150926.ViewModel.HomepageViewModel;

public class HomepageFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_homepage, container, false);
        TextView tv = v.findViewById(R.id.tvBoasVindas);
        HomepageViewModel vm = new ViewModelProvider(this).get(HomepageViewModel.class);
        vm.getMensagem().observe(getViewLifecycleOwner(), tv::setText);
        return v;
    }
}