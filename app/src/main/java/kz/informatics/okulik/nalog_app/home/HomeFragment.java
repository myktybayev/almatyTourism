package kz.informatics.okulik.nalog_app.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import kz.informatics.okulik.R;

public class HomeFragment extends Fragment {

    View root;
    LinearLayout otel1, otel2, otel3, otel4, otel5, otel6, otel7, otel8, otel9, otel10;
    Button searchButton;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_home, container, false);

        searchButton = root.findViewById(R.id.searchButton);
        otel1 = root.findViewById(R.id.otel1);
        otel2 = root.findViewById(R.id.otel2);
        otel3 = root.findViewById(R.id.otel3);
        otel4 = root.findViewById(R.id.otel4);
        otel5 = root.findViewById(R.id.otel5);
        otel6 = root.findViewById(R.id.otel6);
        otel7 = root.findViewById(R.id.otel7);
        otel8 = root.findViewById(R.id.otel8);
        otel9 = root.findViewById(R.id.otel9);
        otel10 = root.findViewById(R.id.otel10);

        View.OnClickListener oneOtelClick = view -> startActivity(new Intent(getActivity(), OneOtelActivity.class));

        searchButton.setOnClickListener(oneOtelClick);
        otel1.setOnClickListener(oneOtelClick);
        otel2.setOnClickListener(oneOtelClick);
        otel3.setOnClickListener(oneOtelClick);
        otel4.setOnClickListener(oneOtelClick);
        otel5.setOnClickListener(oneOtelClick);
        otel6.setOnClickListener(oneOtelClick);
        otel7.setOnClickListener(oneOtelClick);
        otel8.setOnClickListener(oneOtelClick);
        otel9.setOnClickListener(oneOtelClick);
        otel10.setOnClickListener(oneOtelClick);


        return root;
    }
}