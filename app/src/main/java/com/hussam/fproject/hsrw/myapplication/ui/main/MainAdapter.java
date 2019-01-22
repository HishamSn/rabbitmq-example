package com.hussam.fproject.hsrw.myapplication.ui.main;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hussam.fproject.hsrw.myapplication.R;
import com.hussam.fproject.hsrw.myapplication.prefs.PrefsUtils;
import com.hussam.fproject.hsrw.myapplication.ui.chat.ChatActivity;
import com.hussam.fproject.hsrw.myapplication.ui.faplab.FablabActivity;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {
    private static final int ROW_NAME_TYPE = R.layout.row_main;
    private Context context;

    public MainAdapter() {

    }

    @NonNull
    @Override
    public MainAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        context = recyclerView.getContext();
    }

    @Override
    public void onBindViewHolder(@NonNull MainAdapter.ViewHolder holder, int position) {

        switch (position) {

            case 0:
                holder.tvName.setText("Fablab");
                holder.tvDesc.setText("let's talk now");
                holder.ivPhoto.setImageResource(R.drawable.ic_user);
                holder.itemView.setOnClickListener(v -> {
                    Intent intent = new Intent(context, FablabActivity.class);
                    context.startActivity(intent);
                });
                break;

            case 1:
                holder.tvName.setText("Library");
                holder.tvDesc.setText("let's talk now");
                holder.ivPhoto.setImageResource(R.drawable.ic_group);
                holder.itemView.setOnClickListener(v -> {
                    Intent intent = new Intent(context, ChatActivity.class);
                    intent.putExtra("type", "Topic");
                    String[] userName = PrefsUtils.getInstance().getUserName().split("_");
                    intent.putExtra("group", userName[0]);//Major

                    context.startActivity(intent);
                });

                break;

            case 2:
                holder.tvName.setText("Mensa");
                holder.tvDesc.setText("let's talk now");
                holder.ivPhoto.setImageResource(R.drawable.ic_reply_all);

                holder.itemView.setOnClickListener(v -> {
                    Intent intent = new Intent(context, ChatActivity.class);
                    intent.putExtra("type", "Fanout");
                    context.startActivity(intent);
                });
                break;
        }


    }


    @Override
    public int getItemCount() {
        return 3;
    }

    @Override
    public int getItemViewType(int position) {
        return ROW_NAME_TYPE;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName;
        private TextView tvDesc;
        private ImageView ivPhoto;

        public ViewHolder(View view) {
            super(view);
            tvName = view.findViewById(R.id.tv_name);
            tvDesc = view.findViewById(R.id.tv_desc);
            ivPhoto = view.findViewById(R.id.iv_user);
        }
    }
}
