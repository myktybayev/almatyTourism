package kz.informatics.okulik.nalog_app.comnaty;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import kz.informatics.okulik.R;
import kz.informatics.okulik.nalog_app.home.activities.OneOtelActivity;

public class FavoriteFragment extends Fragment {

    View root;
    LinearLayout otel1, otel2, otel3, otel4, otel5;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_fav, container, false);

        otel1 = root.findViewById(R.id.otel1);
        otel2 = root.findViewById(R.id.otel2);
        otel3 = root.findViewById(R.id.otel3);
        otel4 = root.findViewById(R.id.otel4);
        otel5 = root.findViewById(R.id.otel5);

        View.OnClickListener oneOtelClick = view -> startActivity(new Intent(getActivity(), OneOtelActivity.class));

        otel1.setOnClickListener(oneOtelClick);
        otel2.setOnClickListener(oneOtelClick);
        otel3.setOnClickListener(oneOtelClick);
        otel4.setOnClickListener(oneOtelClick);
        otel5.setOnClickListener(oneOtelClick);


        return root;
    }
}