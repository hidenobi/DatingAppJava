package com.hidenobi.datingapp.Chat;

import static com.hidenobi.datingapp.R.drawable.back_shadow_item_profile;
import static com.hidenobi.datingapp.R.layout.item_chat;
import static com.hidenobi.datingapp.R.layout.item_matches;
import  static com.hidenobi.datingapp.R.drawable.back_pink_shadow_button;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hidenobi.datingapp.R;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatViewHolders> {

    private List<ChatObject> chatObjectList;
    private Context context;
    public ChatAdapter(List<ChatObject> chatObjectList, Context context){
        this.chatObjectList = chatObjectList;
        this.context=context;
    }
    @NonNull
    @Override
    public ChatViewHolders onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(item_chat,null,false);
        ViewGroup.LayoutParams layoutParams =new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(layoutParams);
        ChatViewHolders chatViewHolders = new ChatViewHolders((layoutView));
        return chatViewHolders;

    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolders holder, int position) {

        holder.mMessage.setText(chatObjectList.get(position).getMessage());
        if(chatObjectList.get(position).getCurrentUser()){
            holder.mLinearLayout.setGravity(Gravity.END);
            holder.mLinearLayout.setPadding(150,0,0,0);
            holder.mMessage.setTextColor(Color.parseColor("#FFFFFF"));
            holder.linearLayout.setBackgroundResource(back_pink_shadow_button);
        }
        else{
            holder.mLinearLayout.setGravity(Gravity.START);
            holder.mLinearLayout.setPadding(0,0,150,0);
            holder.mMessage.setTextColor(Color.parseColor("#FF000000"));
            holder.linearLayout.setBackgroundResource(back_shadow_item_profile);
        }
    }

    @Override
    public int getItemCount() {
        return this.chatObjectList.size();
    }
}
