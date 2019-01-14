package com.hussam.fproject.hsrw.myapplication.ui.faplab;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hussam.fproject.hsrw.myapplication.R;
import com.hussam.fproject.hsrw.myapplication.model.Queues;
import com.hussam.fproject.hsrw.myapplication.ui.chat.ChatActivity;

import java.util.List;

public class FablabAdapter extends RecyclerView.Adapter<FablabAdapter.ViewHolder> {
    private static final int ROW_CAR_TYPE = R.layout.row_name;
    private List<Queues> queuesList;
    private Context context;

    public FablabAdapter(List<Queues> queues) {
        this.queuesList = queues;
    }

    @NonNull
    @Override
    public FablabAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        context = recyclerView.getContext();
    }

    @Override
    public void onBindViewHolder(@NonNull FablabAdapter.ViewHolder holder, int position) {
        holder.tvName.setText(queuesList.get(position).getName());
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ChatActivity.class);
            intent.putExtra("user_name", queuesList.get(position).getName());
            context.startActivity(intent);
        });
    }


    @Override
    public int getItemCount() {
        return queuesList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return ROW_CAR_TYPE;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName;

        public ViewHolder(View view) {
            super(view);
            tvName = view.findViewById(R.id.tv_name);
        }
    }
}
