package com.example.ecomapplication.ui.home;


import static android.content.ContentValues.TAG;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;


import com.example.ecomapplication.R;
import com.example.ecomapplication.adapters.CategoryAdapter;
import com.example.ecomapplication.adapters.NewProductAdapter;
import com.example.ecomapplication.adapters.PopularProductAdapter;
import com.example.ecomapplication.adapters.SlideItemHome;
import com.example.ecomapplication.adapters.SliderAdapter;
import com.example.ecomapplication.databinding.FragmentHomeBinding;
import com.example.ecomapplication.models.Category;
import com.example.ecomapplication.models.Product;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {

    private ViewPager2 viewPager2;
    private List<Category> categoryList;
    private  RecyclerView recyclerViewCategory, newProductRecyclerview, popularRecyclerview ;
    CategoryAdapter categoryAdapter;
    GridView categoryView;
    ProgressDialog progressDialog;
    FirebaseFirestore firestore;

    RecyclerView categoryRecyclerView;
    ListView viewCategory;

    //new product recycler;
    NewProductAdapter newProductAdapter;
    List<Product> newProductList;

    //popular products
    PopularProductAdapter popularProductAdapter;
    List<Product> popularProductsList;

    FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
    CollectionReference applicationsRef;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_home, container, false);
        firestore = FirebaseFirestore.getInstance();

        //        ImageSlider imageSlider = root.findViewById((R.id.image_slider));
        //        List<SlideModel> slideModels = new ArrayList<>();
        //        slideModels.add(new SlideModel(R.drawable.banner1, "Discount On Shoes Items", ImageView.ScaleType.CENTER_CROP));
        //        slideModels.add(new SlideModel(R.drawable.banner2, "Discount On Perfume", ImageView.ScaleType.CENTER_CROP));
        //
        //        slideModels.add(new SlideModel(R.drawable.banner3, "70% OFF", ImageView.ScaleType.CENTER_CROP));
        //        imageSlider.setImageList(slideModels);
        //        recyclerViewCategory = root.findViewById(R.id.rec_category);
        //        newProductRecyclerview = root.findViewById(R.id.new_product_rec);

        //binding
        viewPager2 = root.findViewById(R.id.home_slider);
        categoryView = root.findViewById(R.id.rec_category);
        popularRecyclerview = root.findViewById(R.id.new_product_rec);

        // create slider
        List<SlideItemHome> slideItemHomes = new ArrayList<>();
        slideItemHomes.add(new SlideItemHome(R.drawable.banner1));
        slideItemHomes.add(new SlideItemHome(R.drawable.banner2));
        slideItemHomes.add(new SlideItemHome(R.drawable.banner3))       ;
        viewPager2.setAdapter(new SliderAdapter(slideItemHomes,viewPager2));

        // progress dialog
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Welcome to my website");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(true);
        progressDialog.show();

        // load Category
        getCategoryDataFromFirebase();

        //load product
        getProductDataFromFirebase();
//        popularRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        popularRecyclerview.setLayoutManager(new GridLayoutManager(getContext(),2));
        popularProductAdapter = new PopularProductAdapter(getContext(), popularProductsList);
        popularRecyclerview.setAdapter(popularProductAdapter);

        categoryAdapter = new CategoryAdapter(getContext(), categoryList);
        categoryView.setAdapter(categoryAdapter);

        return root;
    }

    public void getProductDataFromFirebase(){
        popularProductsList = new ArrayList<>();
        firestore.collection("Product").limit(6).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                try {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Product product = document.toObject(Product.class);
                        popularProductsList.add(product);
                        popularProductAdapter.notifyDataSetChanged();
                    }
                }
                catch (Exception e ) {
                    e.printStackTrace();
                }   
            } else {
                Log.w(TAG, "Error getting documents.", task.getException());
            }
        });
    }

    public void getCategoryDataFromFirebase(){
        categoryList = new ArrayList<>();
        int MAX_ITEM = 4;

        firestore.collection("Category").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                int item_num = 0;
                for (QueryDocumentSnapshot document : task.getResult()) {
                    if (item_num < MAX_ITEM) {
                        Category category = document.toObject(Category.class);
                        categoryList.add(category);
                        categoryAdapter.notifyDataSetChanged();
                        item_num++;
                    } else {
                        break;
                    }
                }
            } else {
                Log.w(TAG, "Error getting documents.", task.getException());
            }
        });
    }

}