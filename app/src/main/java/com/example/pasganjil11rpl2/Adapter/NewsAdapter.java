package com.example.pasganjil11rpl2.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.pasganjil11rpl2.Model.NewsModel;
import com.example.pasganjil11rpl2.R;

import java.util.ArrayList;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> implements Filterable {
    private final Context naContext;
    private final ArrayList<NewsModel> naNewsList;
    private final ArrayList<NewsModel> naNewsListFull; // SearchView

    //OnItemClickListener
    private OnItemClickListener naListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        naListener = listener;
    }

    //RecyclerView
    public NewsAdapter(Context aContext, ArrayList<NewsModel> aNewsList) {
        this.naContext = aContext;
        this.naNewsList = aNewsList;
        naNewsListFull = new ArrayList<>(naNewsList); // SearchView
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(naContext).inflate(R.layout.row_news_item, parent, false);
        return new NewsViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        final NewsModel model = naNewsList.get(position);
        holder.naTitle.setText(model.getTitle());
        holder.naPublishedAt.setText(model.getPublishedAt());
        holder.naName.setText(model.getName() + "  -");
        Glide.with(naContext)
                .load(model.getUrlToImage())
                .thumbnail(Glide.with(naContext).load(R.drawable.loader).centerCrop())
                .fitCenter()
                .centerCrop()
                .error(Glide.with(naContext).load(R.drawable.error).centerCrop())
                .into(holder.naUrlToImage);
    }

    @Override
    public int getItemCount() {
        return naNewsList.size();
    }

    //SearchView
    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private final Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<NewsModel> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(naNewsListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (NewsModel item : naNewsListFull) {
                    boolean t = item.getTitle().toLowerCase().contains(filterPattern);
                    boolean n = item.getName().toLowerCase().contains(filterPattern);
                    boolean p = item.getPublishedAt().toLowerCase().contains(filterPattern);
                    if (t || n || p){
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @SuppressLint("NotifyDataSetChanged")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            naNewsList.clear();
            naNewsList.addAll((ArrayList) results.values);
            notifyDataSetChanged();
        }
    };

    public class NewsViewHolder extends RecyclerView.ViewHolder {
        public ImageView naUrlToImage;
        public TextView naTitle, naPublishedAt, naName;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            naUrlToImage = itemView.findViewById(R.id.iv_urlToImage_rowNews);
            naTitle = itemView.findViewById(R.id.tv_title_rowNews);
            naPublishedAt = itemView.findViewById(R.id.tv_publishedAt_rowNews);
            naName = itemView.findViewById(R.id.tv_name_rowNews);

            //OnItemClickListener
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (naListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            naListener.onItemClick(position);
                        }
                    }
                }
            });

        }
    }
}
