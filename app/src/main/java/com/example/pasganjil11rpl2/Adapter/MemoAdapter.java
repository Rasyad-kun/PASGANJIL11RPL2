package com.example.pasganjil11rpl2.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pasganjil11rpl2.Model.MemoModel;
import com.example.pasganjil11rpl2.R;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MemoAdapter extends RecyclerView.Adapter<MemoAdapter.MemoViewHolder>{
    private final Context maContext;
    private final List<MemoModel> maMemoList;
    //realm
    Realm realm;

    //OnItemClickListener
    private OnItemClickListener maListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        maListener = listener;
    }

    //RecyclerView
    public MemoAdapter(Context maContext, List<MemoModel> maMemoList) {
        this.maContext = maContext;
        this.maMemoList = maMemoList;
    }

    @NonNull
    @Override
    public MemoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(maContext).inflate(R.layout.row_memo_item, parent, false);
        return new MemoViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(MemoViewHolder holder, int position) {
        final MemoModel model = maMemoList.get(position);
        holder.maTitle.setText(model.getTitle());
        holder.maContent.setText(model.getContent());
//        Glide.with(maContext)
//                .load(model.getUrlToImage())
//                .thumbnail(Glide.with(maContext).load(R.drawable.loader).centerCrop())
//                .centerCrop()
//                .error(Glide.with(maContext).load(R.drawable.error).centerCrop())
//                .into(holder.haUrlToImage);
    }

    @Override
    public int getItemCount() {
        return maMemoList.size();
    }

    public class MemoViewHolder extends RecyclerView.ViewHolder {
        public TextView maTitle, maContent;
//        public ImageView haUrlToImage, haDelete;

        public MemoViewHolder(View itemView) {
            super(itemView);
//            haUrlToImage = itemView.findViewById(R.id.iv_urlToImage_rowHistory);
            maTitle = itemView.findViewById(R.id.tv_title_rowMemo);
            maContent = itemView.findViewById(R.id.tv_content_rowMemo);

            Realm.init(maContext);
            RealmConfiguration configuration = new RealmConfiguration.Builder().allowWritesOnUiThread(true).build();
            realm = Realm.getInstance(configuration);

            //OnItemClickListener
            itemView.setOnClickListener(v -> {
                if (maListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        maListener.onItemClick(position);
                    }
                }
            });

        }
    }
}
