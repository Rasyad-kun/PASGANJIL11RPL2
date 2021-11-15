package com.example.pasganjil11rpl2.Fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.pasganjil11rpl2.Adapter.MemoAdapter;
import com.example.pasganjil11rpl2.ItemMemoActivity;
import com.example.pasganjil11rpl2.Model.MemoModel;
import com.example.pasganjil11rpl2.R;
import com.example.pasganjil11rpl2.Realm.MemoHelper;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MemoFragment extends Fragment implements MemoAdapter.OnItemClickListener {
    private TextView tv_empty_memo;
    private RecyclerView rv_memo;
    private List<MemoModel> memoList;
    private LinearProgressIndicator pb_memo;
    private MemoAdapter memoAdapter;
    Realm realm;
    MemoHelper memoHelper;
    View v;
    SwipeRefreshLayout refreshLayout_memo;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_memo, container, false);

        refreshLayout_memo = v.findViewById(R.id.srl_refreshLayout_memo);
        tv_empty_memo = v.findViewById(R.id.tv_empty_memo);
        pb_memo = v.findViewById(R.id.pb_memo);
        rv_memo = v.findViewById(R.id.rv_memo);
        rv_memo.setHasFixedSize(true);
        rv_memo.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
        pb_memo.setVisibility(View.VISIBLE);

        // Setup Realm
        Realm.init(v.getContext());
//        RealmConfiguration configuration = new RealmConfiguration.Builder().allowWritesOnUiThread(true).build();
        RealmConfiguration configuration = new RealmConfiguration.Builder()
                .allowWritesOnUiThread(true)
                .build();
        realm = Realm.getInstance(configuration);

        memoHelper = new MemoHelper(realm);
        memoList = new ArrayList<>();

        memoList = memoHelper.getAllHistory();

        show();

        refreshLayout_memo.setProgressBackgroundColorSchemeResource(R.color.def_header);
        refreshLayout_memo.setColorSchemeResources(
                android.R.color.holo_blue_dark, android.R.color.holo_orange_dark, android.R.color.holo_red_dark);
        refreshLayout_memo.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pb_memo.setVisibility(View.VISIBLE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refresh();
                        progress();
                        Toast.makeText(v.getContext(), "Reloading now!", Toast.LENGTH_SHORT).show();
                        refreshLayout_memo.setRefreshing(false);
                    }
                }, 2000);
            }
        });

        return v;
    }

    private void show() {
//        DividerItemDecoration itemDecorator = new DividerItemDecoration(v.getContext(), DividerItemDecoration.VERTICAL);
//        itemDecorator.setDrawable(getResources().getDrawable(R.drawable.item_divider));
//        rv_memo.addItemDecoration(itemDecorator);
        memoAdapter = new MemoAdapter(v.getContext(), memoList);
        rv_memo.setAdapter(memoAdapter);
        memoAdapter.setOnItemClickListener(this);

        progress();
    }

    private void progress() {
        if (memoList.isEmpty()) {
            tv_empty_memo.setVisibility(View.VISIBLE);
        } else {
            tv_empty_memo.setVisibility(View.INVISIBLE);
        }
        pb_memo.setVisibility(View.INVISIBLE);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(getActivity(), ItemMemoActivity.class);
        MemoModel clickedRow = memoList.get(position);

        intent.putExtra("id", clickedRow.getId().toString());
        intent.putExtra("title", clickedRow.getTitle());
        intent.putExtra("content", clickedRow.getContent());

        Toast.makeText(v.getContext(), "" + clickedRow.getTitle(), Toast.LENGTH_SHORT).show();
        startActivity(intent);
        refresh();
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
        inflater.inflate(R.menu.menu_fragment_memo, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.nav_newMemo) {
            Intent intent = new Intent(getActivity(), ItemMemoActivity.class);

            Toast.makeText(v.getContext(), "Creating New Memo", Toast.LENGTH_SHORT).show();
            startActivity(intent);
            refresh();

//            historyHelper.deleteAll();
//            historyAdapter.notifyDataSetChanged();
//            progress();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void refresh() {
        memoAdapter.notifyDataSetChanged();
    }
}
