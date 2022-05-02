package com.example.ecomapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.ecomapplication.R;
import com.google.firebase.storage.FirebaseStorage;

import java.util.List;

public class HistorySearchAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final List<String> history;
    private final FirebaseStorage storage;

    public HistorySearchAdapter(@NonNull Context context, List<String> history) {
        super(context, R.layout.history_search_item, history);
        this.context = context;
        this.history = history;
        this.storage = FirebaseStorage.getInstance();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.history_search_item, parent, false);

        TextView historyResult = view.findViewById(R.id.historySearch);
        historyResult.setText(history.get(position));

        return view;
    }
}
