package com.example.testwhatatrip;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Search_Activity extends AppCompatActivity {
    private TextView searchText;
    private RecyclerView SearchRecyclerView;
    private androidx.appcompat.widget.SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        searchText = findViewById(R.id.Search_text);
        searchView = findViewById(R.id.Search_view);
        SearchRecyclerView = findViewById(R.id.Search_recycler);
        SearchRecyclerView.setVisibility(View.VISIBLE);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Search_Activity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        SearchRecyclerView.setLayoutManager(linearLayoutManager);

        final List<FavoritesModel> list = new ArrayList<>();
        final List<String> ids = new ArrayList<>();
        final Adapter adapter = new Adapter(list);
        SearchRecyclerView.setAdapter(adapter);
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                list.clear();
//                ids.clear();
//                final String[] tags = query.toLowerCase().split(" ");
//                for(final String tag : tags ){
//                    tag.trim();
//                    FirebaseFirestore.getInstance().collection("TOURS").whereArrayContains("tags",tag).get().
//                            addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                                @Override
//                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                                    if(task.isSuccessful())
//                                    {
//                                        for(DocumentSnapshot documentSnapshot : task.getResult().getDocuments()){
//                                            FavoritesModel model = new FavoritesModel(documentSnapshot.get("product_image_1").toString()
//                                                    , documentSnapshot.get("product_title").toString(),
//                                                    documentSnapshot.get("product_description").toString().substring(0,120),
//                                                    Integer.parseInt(documentSnapshot.get("price").toString()));
//
//                                            if(!ids.contains(model.getTitle()))
//                                            {
//                                                list.add(model);
//                                                ids.add(model.getTitle());
//                                            }
//                                            if(tag.equals(tags[tags.length-1]))
//                                            {
//                                                if(list.size()==0)
//                                                {
//                                                    searchText.setVisibility(View.VISIBLE);
//                                                    SearchRecyclerView.setVisibility(View.GONE);
//                                                }
//                                                else {
//                                                    searchText.setVisibility(View.GONE);
//                                                    SearchRecyclerView.setVisibility(View.VISIBLE);
//                                                }
//                                                adapter.getFilter().filter(query);
//                                            }
//                                        }
//
//                                    }
//
//                                    else
//                                    {
//                                        String error = task.getException().getMessage();
//                                        Toast.makeText(Search_Activity.this,error,Toast.LENGTH_SHORT).show();
//                                    }
//                                }
//                            });
//                }
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                return false;
//            }
//        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                list.clear();
                ids.clear();
                final String[] tags = query.toLowerCase().split(" ");
                for(final String tag : tags ){
                    tag.trim();
                    FirebaseFirestore.getInstance().collection("TOURS").whereArrayContains("tags",tag).get().
                            addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if(task.isSuccessful())
                                    {
                                        for(DocumentSnapshot documentSnapshot : task.getResult().getDocuments()){
                                            FavoritesModel model = new FavoritesModel(documentSnapshot.get("product_image_1").toString()
                                                    , documentSnapshot.get("product_title").toString(),
                                                    documentSnapshot.get("product_description").toString().substring(0,120),
                                                    Integer.parseInt(documentSnapshot.get("price").toString()));

                                            if(!ids.contains(model.getTitle()))
                                            {
                                                list.add(model);
                                                ids.add(model.getTitle());
                                            }
                                            if(tag.equals(tags[tags.length-1]))
                                            {
                                                if(list.size()==0)
                                                {
                                                    searchText.setVisibility(View.VISIBLE);
                                                    SearchRecyclerView.setVisibility(View.GONE);
                                                }
                                                else {
                                                    searchText.setVisibility(View.GONE);
                                                    SearchRecyclerView.setVisibility(View.VISIBLE);
                                                }
                                                adapter.getFilter().filter(query);
                                            }
                                            if(list.size()==0)
                                            {
                                                searchText.setVisibility(View.VISIBLE);
                                                SearchRecyclerView.setVisibility(View.GONE);
                                            }
                                            else {
                                                searchText.setVisibility(View.GONE);
                                                SearchRecyclerView.setVisibility(View.VISIBLE);
                                            }
                                        }

                                    }

                                    else
                                    {
                                        String error = task.getException().getMessage();
                                        Toast.makeText(Search_Activity.this,error,Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

    }

    class Adapter extends FavoriteRecyclerAdapter implements Filterable{

        public Adapter(List<FavoritesModel> favoritesModelList) {
            super(favoritesModelList);
        }

        @Override
        public Filter getFilter() {
            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {

                    //// search logic


                    return null;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    notifyDataSetChanged();
                }
            };
        }
    }
}