package com.example.pasganjil11rpl2;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.pasganjil11rpl2.Adapter.NewsAdapter;
import com.example.pasganjil11rpl2.Model.NewsModel;

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
        String publishedAt = beforePublishedAt.replace("T", " | ");
        StringTokenizer splitTime = new StringTokenizer(publishedAt, ":");
        String dateAndHour = splitTime.nextToken();
        String minute = splitTime.nextToken();

//        StringTokenizer split = new StringTokenizer(beforePublishedAt, "T");
//        String date = split.nextToken();
//        String time = split.nextToken().replace("Z", "");
//
//        StringTokenizer splitTime = new StringTokenizer(time, ":");
//        String hour = splitTime.nextToken();
//        String minute = splitTime.nextToken();
//
//        return date + " | " + hour + ":" + minute;

        return dateAndHour + ":" + minute;
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

                                String title, publishedAt, author, description, urlToImage, url;
                                title = articles.getString("title");
                                publishedAt = articles.getString("publishedAt");
                                publishedAt = FormatedDateAndTime(publishedAt);
                                author = articles.getString("author");
                                description = articles.getString("description");
                                urlToImage = articles.getString("urlToImage");
                                url = articles.getString("url");

                                newsList.add(new NewsModel(title, publishedAt, author, description, urlToImage, url));
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

    private void ShowData() {
        NewsAdapter newsAdapter = new NewsAdapter(v.getContext(), newsList);
        rv_news.setAdapter(newsAdapter);
        newsAdapter.setOnItemClickListener(this);

        pb_news.setVisibility(View.GONE);
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(getActivity(), WebViewActivity.class);
        NewsModel clickedRow = newsList.get(position);

        intent.putExtra("url", clickedRow.getUrl());
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
