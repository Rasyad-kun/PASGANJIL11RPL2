package com.example.pasganjil11rpl2.Fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.pasganjil11rpl2.Activities.WebViewActivity;
import com.example.pasganjil11rpl2.Adapter.NewsAdapter;
import com.example.pasganjil11rpl2.Model.NewsModel;
import com.example.pasganjil11rpl2.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class NewsFragment extends Fragment implements NewsAdapter.OnItemClickListener {
    private RecyclerView rv_news;
    private ArrayList<NewsModel> newsList;
    private ProgressBar pb_news;
    View v;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_news, container, false);

        pb_news = v.findViewById(R.id.pb_news);
        rv_news = v.findViewById(R.id.rv_news);
        rv_news.setHasFixedSize(true);
        rv_news.setLayoutManager(new LinearLayoutManager(v.getContext()));

        AddData();
        return v;
    }

    private String FormatedDateAndTime(String beforePublishedAt) {
//        String publishedAt = beforePublishedAt.replace("T", " | ");
//        StringTokenizer splitTime = new StringTokenizer(publishedAt, ":");
//        String dateAndHour = splitTime.nextToken();
//        String minute = splitTime.nextToken();

//        Versi lainnya
        StringTokenizer split = new StringTokenizer(beforePublishedAt, "T");

        //date
        String dates = split.nextToken();
        StringTokenizer splitDate = new StringTokenizer(dates, "-");
        String year = splitDate.nextToken();
        String month = splitDate.nextToken();
        String date = splitDate.nextToken();
        String fixDate = date + "/" + month + "/" + year;

        //time
        String times = split.nextToken();
        StringTokenizer splitTime = new StringTokenizer(times, ":");
        String hour = splitTime.nextToken();
        String minute = splitTime.nextToken();
        String fixTime = hour + ":" + minute;

        return fixTime + ", " + fixDate;
    }

    private void AddData() {
        String url = "https://newsapi.org/v2/top-headlines?country=id&apiKey=8f262d0799014142b64adecb44015eab";
        AndroidNetworking.get(url)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            newsList = new ArrayList<>();
                            JSONArray jsonArray = response.getJSONArray("articles");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject articles = jsonArray.getJSONObject(i);

                                String title, publishedAt, name, description, urlToImage, url;
                                title = articles.getString("title");
                                publishedAt = articles.getString("publishedAt");
                                publishedAt = FormatedDateAndTime(publishedAt);
                                name = articles.getJSONObject("source").getString("name");
                                description = articles.getString("description");
                                urlToImage = articles.getString("urlToImage");
                                url = articles.getString("url");

                                newsList.add(new NewsModel(title, publishedAt, name, description, urlToImage, url));
                            }

                            ShowData();

                        } catch (JSONException e) {
                            Log.d("error", e.toString());
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("error", anError.toString());
                    }
                });
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void ShowData() {
        try {
            DividerItemDecoration itemDecorator = new DividerItemDecoration(v.getContext(), DividerItemDecoration.VERTICAL);
            itemDecorator.setDrawable(getResources().getDrawable(R.drawable.item_divider));
            rv_news.addItemDecoration(itemDecorator);
            NewsAdapter newsAdapter = new NewsAdapter(v.getContext(), newsList);
            rv_news.setAdapter(newsAdapter);
            newsAdapter.setOnItemClickListener(this);
        } catch (Exception e){
            Toast.makeText(v.getContext(), "Refreshing!", Toast.LENGTH_SHORT).show();
            AddData();
        }

        pb_news.setVisibility(View.GONE);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(getActivity(), WebViewActivity.class);
        NewsModel clickedRow = newsList.get(position);

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
    }
}
