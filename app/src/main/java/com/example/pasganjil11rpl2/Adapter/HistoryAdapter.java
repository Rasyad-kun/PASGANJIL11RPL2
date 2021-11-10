package com.example.pasganjil11rpl2.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.pasganjil11rpl2.Model.HistoryModel;
import com.example.pasganjil11rpl2.R;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {
    private final Context haContext;
    private final List<HistoryModel> haHistoryList;
    //realm
    Realm realm;

    //OnItemClickListener
    private OnItemClickListener haListener;

    public interface OnItemClickListener {
        void onItemClick(int position);

        void onDeleteClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        haListener = listener;
    }

    //RecyclerView
    public HistoryAdapter(Context haContext, List<HistoryModel> haHistoryList) {
        this.haContext = haContext;
        this.haHistoryList = haHistoryList;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(haContext).inflate(R.layout.row_history_item, parent, false);
        return new HistoryViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(HistoryAdapter.HistoryViewHolder holder, int position) {
        final HistoryModel model = haHistoryList.get(position);
        holder.haTitle.setText(model.getTitle());
        holder.haCurrentTime.setText(model.getCurrentTime());
        holder.haName.setText(model.getName());
        Glide.with(haContext)
                .load(model.getUrlToImage())
                .thumbnail(Glide.with(haContext).load(R.drawable.loader).centerCrop())
                .centerCrop()
                .error(Glide.with(haContext).load(R.drawable.error).centerCrop())
                .into(holder.haUrlToImage);
    }

    @Override
    public int getItemCount() {
        return haHistoryList.size();
    }

    public class HistoryViewHolder extends RecyclerView.ViewHolder {
        public TextView haTitle, haName, haCurrentTime;
        public ImageView haUrlToImage, haDelete;

        public HistoryViewHolder(View itemView) {
            super(itemView);
            haUrlToImage = itemView.findViewById(R.id.iv_urlToImage_rowHistory);
            haTitle = itemView.findViewById(R.id.tv_title_rowHistory);
            haName = itemView.findViewById(R.id.tv_name_rowHistory);
            haCurrentTime = itemView.findViewById(R.id.tv_currentTime_rowHistory);
            haDelete = itemView.findViewById(R.id.iv_delete_rowHistory);


            Realm.init(haContext);
            RealmConfiguration configuration = new RealmConfiguration.Builder().build();
            realm = Realm.getInstance(configuration);

            //OnItemClickListener
            itemView.setOnClickListener(v -> {
                if (haListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        haListener.onItemClick(position);
                    }
                }
            });

            //specific item OnItemClickListener
            haDelete.setOnClickListener(v -> {
                if (haListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        haListener.onDeleteClick(position);
                    }
                }
            });

        }
    }

}
