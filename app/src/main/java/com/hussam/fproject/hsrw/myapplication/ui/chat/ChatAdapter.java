package com.hussam.fproject.hsrw.myapplication.ui.chat;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hussam.fproject.hsrw.myapplication.R;
import com.hussam.fproject.hsrw.myapplication.model.Chat;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    private static final int ROW_CAR_TYPE = R.layout.row_chat_client;
    private List<Chat> chatList;
    private Context context;

    public ChatAdapter(List<Chat> chatList) {
        this.chatList = chatList;
    }

    @NonNull
    @Override
    public ChatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        context = recyclerView.getContext();
    }

    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.ViewHolder holder, int position) {
        holder.tvUserName.setText(chatList.get(position).getName());
        holder.tvMessage.setText(chatList.get(position).getMessage());
        holder.tvTime.setText(chatList.get(position).getTime());

    }


    @Override
    public int getItemCount() {
        return chatList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return ROW_CAR_TYPE;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvUserName;
        private TextView tvMessage;
        private TextView tvTime;

        public ViewHolder(View view) {
            super(view);
            tvUserName = view.findViewById(R.id.tv_user_name);
            tvMessage = view.findViewById(R.id.tv_message);
            tvTime = view.findViewById(R.id.tv_time);
        }
    }
}
