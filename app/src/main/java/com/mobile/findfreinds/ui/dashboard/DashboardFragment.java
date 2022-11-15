package com.mobile.findfreinds.ui.dashboard;

import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.mobile.findfreinds.MainActivity;
import com.mobile.findfreinds.databinding.FragmentDashboardBinding;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.edDemandeDashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String num=binding.edPhoneDashboard.getText().toString();
                //envoi sms
                if (MainActivity.send_sms_permission){
                    SmsManager manager = SmsManager.getDefault();
                    manager.sendTextMessage(num,null,"findFreinds: envoyer moi votre position",null,null);
                }
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}