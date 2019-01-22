package com.hussam.fproject.hsrw.myapplication.ui.lib;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hussam.fproject.hsrw.myapplication.R;
import com.hussam.fproject.hsrw.myapplication.ui.chat.ChatActivity;

import java.util.List;

public class LibGroupAdapter extends RecyclerView.Adapter<LibGroupAdapter.ViewHolder> {
    private static final int ROW_GROUP_TYPE = R.layout.row_groups;
    private List<String> majorList;
    private Context context;

    public LibGroupAdapter(List<String> queues) {
        this.majorList = queues;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        context = recyclerView.getContext();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvName.setText(majorList.get(position));
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ChatActivity.class);
            intent.putExtra("group", majorList.get(position));
            intent.putExtra("type", "Topic");
            context.startActivity(intent);
        });
    }


    @Override
    public int getItemCount() {
        return majorList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return ROW_GROUP_TYPE;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName;

        public ViewHolder(View view) {
            super(view);
            tvName = view.findViewById(R.id.tv_name);
        }
    }
}
