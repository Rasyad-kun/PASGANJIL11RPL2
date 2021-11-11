package com.example.pasganjil11rpl2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pasganjil11rpl2.Adapter.HistoryAdapter;
import com.example.pasganjil11rpl2.Model.HistoryModel;
import com.example.pasganjil11rpl2.Realm.HistoryHelper;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class HistoryFragment extends Fragment implements HistoryAdapter.OnItemClickListener {
    private TextView tv_empty_history;
    private RecyclerView rv_history;
    private List<HistoryModel> historyList;
    private ProgressBar pb_history;
    private HistoryAdapter historyAdapter;
    Realm realm;
    HistoryHelper historyHelper;
    View v;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_history, container, false);

        tv_empty_history = v.findViewById(R.id.tv_empty_history);
        pb_history = v.findViewById(R.id.pb_history);
        rv_history = v.findViewById(R.id.rv_history);
        rv_history.setHasFixedSize(true);
        rv_history.setLayoutManager(new LinearLayoutManager(v.getContext()));

        // Setup Realm
        Realm.init(v.getContext());
//        RealmConfiguration configuration = new RealmConfiguration.Builder().allowWritesOnUiThread(true).build();
        RealmConfiguration configuration = new RealmConfiguration.Builder()
                .build();
        realm = Realm.getInstance(configuration);
//        realm = Realm.getInstance(Realm.getDefaultConfiguration());

        historyHelper = new HistoryHelper(realm);
        historyList = new ArrayList<>();

        historyList = historyHelper.getAllHistory();

        show();
        return v;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void show() {
        DividerItemDecoration itemDecorator = new DividerItemDecoration(v.getContext(), DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(getResources().getDrawable(R.drawable.item_divider));
        rv_history.addItemDecoration(itemDecorator);
        historyAdapter = new HistoryAdapter(v.getContext(), historyList);
        rv_history.setAdapter(historyAdapter);
        historyAdapter.setOnItemClickListener(this);

        progress();
    }

    private void progress() {
        if (historyList.isEmpty()) {
            tv_empty_history.setVisibility(View.VISIBLE);
        } else {
            tv_empty_history.setVisibility(View.INVISIBLE);
        }
        pb_history.setVisibility(View.INVISIBLE);
    }


    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(getActivity(), WebViewActivity.class);
        HistoryModel clickedRow = historyList.get(position);

        intent.putExtra("url", clickedRow.getUrl());
        intent.putExtra("title", clickedRow.getTitle());
        intent.putExtra("name", clickedRow.getName());
        intent.putExtra("urlToImage", clickedRow.getUrlToImage());

//        Untuk Intent dan intent data
//        Bundle bundle = new Bundle();
//        bundle.putString("url",newsList.get(position).getUrl());
//        WebVIewFragment webVIewFragment = new WebVIewFragment();
//        webVIewFragment.setArguments(bundle);
//
//        getFragmentManager().beginTransaction().replace(R.id.fragment_container,
//                new WebVIewFragment()).commit();

        Toast.makeText(v.getContext(), "" + clickedRow.getTitle(), Toast.LENGTH_SHORT).show();
        startActivity(intent);
        historyAdapter.notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onDeleteClick(int position) {
        Toast.makeText(v.getContext(), historyList.get(position).getTitle() + " has been deleted", Toast.LENGTH_SHORT).show();
        historyHelper.delete(historyList.get(position).getId());
        historyAdapter.notifyDataSetChanged();
        progress();
    }

    //Specific Item Menu Header
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_fragment_history, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.nav_deleteAll) {
            Toast.makeText(v.getContext(), "Delete All", Toast.LENGTH_SHORT).show();
            historyHelper.deleteAll();
            historyAdapter.notifyDataSetChanged();
            progress();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
