package com.example.ecomapplication.ui.search;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;

import com.example.ecomapplication.MainActivity;
import com.example.ecomapplication.R;
import com.example.ecomapplication.adapters.CategoryAdapter;
import com.example.ecomapplication.adapters.SearchAdapter;
import com.example.ecomapplication.databinding.ActivitySearchBinding;
import com.example.ecomapplication.models.Category;
import com.example.ecomapplication.models.Product;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    ActivitySearchBinding searchBinding;
    GridView categoryView;
    ListView historyView;
    ListView resultView;
    EditText searchView;
    CategoryAdapter categoryAdapter;
    ArrayAdapter historyAdapter; // Need to change to custom adapter
    SearchAdapter searchAdapter; // Need to change to custom adapter
    FirebaseFirestore firestore;

    List<Product> searchedProduct = new ArrayList<>();
    List<Category> categories;
    List<String> history;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        searchBinding = ActivitySearchBinding.inflate(getLayoutInflater());
        categoryView = searchBinding.categoryList;
        historyView = searchBinding.searchHistory;
        searchView = searchBinding.searchBox;
        resultView = searchBinding.searchResult;
        firestore = FirebaseFirestore.getInstance();

        searchAdapter = new SearchAdapter(getContext(), searchedProduct);
        resultView.setAdapter(searchAdapter);

        getCategoriesFromFireBase();
        categoryAdapter = new CategoryAdapter(getContext(), categories);
        categoryView.setAdapter(categoryAdapter);

        getHistoryFromFireBase();
        historyView.setAdapter(historyAdapter);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_search, container, false);
        firestore = FirebaseFirestore.getInstance();
        categoryView = root.findViewById(R.id.categoryList);
        historyView = root.findViewById(R.id.searchHistory);
        searchView = root.findViewById(R.id.searchBox);
        resultView = root.findViewById(R.id.searchResult);
        searchAdapter = new SearchAdapter(getContext(), searchedProduct);
        resultView.setAdapter(searchAdapter);

        getCategoriesFromFireBase();
        categoryAdapter = new CategoryAdapter(getContext(), categories);
        categoryView.setAdapter(categoryAdapter);

        getHistoryFromFireBase();
        historyAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, history);
        historyView.setAdapter(historyAdapter);
        return root;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onResume() {
        super.onResume();



        searchView.setOnTouchListener((view, motionEvent) -> {
            final int DRAWABLE_LEFT = 0;

            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                if (motionEvent.getRawX() <= (searchView.getLeft() + searchView.getCompoundDrawables()[DRAWABLE_LEFT].getBounds().width())) {
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                    return true;
                }
            }
            return false;
        });

        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
                // searchedProduct.clear();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                history.clear();
                historyAdapter.notifyDataSetChanged();

                if (charSequence.length() != 0) {
                    firestore.collection("Product")
                            .orderBy("name")
                            .startAt(String.valueOf(charSequence))
                            .endAt(charSequence + "\uf8ff")
                            .get().addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            searchedProduct.clear();
                            for (QueryDocumentSnapshot snapshot : task.getResult()) {
                                Product product = snapshot.toObject(Product.class);
                                searchedProduct.add(product);
                            }
                        } else {
                            Log.w("Result", "Error getting documents.", task.getException());
                        }
                    });
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                searchAdapter.notifyDataSetChanged();
            }
        });
    }
    private void getCategoriesFromFireBase() {
        categories = new ArrayList<>();

        firestore.collection("Category").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Category category = document.toObject(Category.class);
                    categories.add(category);
                    categoryAdapter.notifyDataSetChanged();
                }
            } else {
                Log.w(TAG, "Error getting documents.", task.getException());
            }
        });
    }

    private void getHistoryFromFireBase() {
        history = new ArrayList<>();

        history.add("iphone13");
        history.add("samsung S22");
        history.add("quan ao dep");
        history.add("laptop");
    }
}