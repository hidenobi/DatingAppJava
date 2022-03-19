package com.hidenobi.datingapp.Matches;

import static com.hidenobi.datingapp.R.layout.item_matches;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hidenobi.datingapp.R;

import java.util.List;

public class MatchesAdapter extends RecyclerView.Adapter<MatchesViewHolders> {

    private List<MatchesObject> matchesObjectList;
    private Context context;
    public MatchesAdapter(List<MatchesObject> matchesObjectList,Context context){
        this.matchesObjectList=matchesObjectList;
        this.context=context;
    }
    @NonNull
    @Override
    public MatchesViewHolders onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(item_matches,null,false);
        ViewGroup.LayoutParams layoutParams =new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(layoutParams);
        MatchesViewHolders matchesViewHolders = new MatchesViewHolders((layoutView));
        return matchesViewHolders;

    }

    @Override
    public void onBindViewHolder(@NonNull MatchesViewHolders holder, int position) {
        holder.mMatchId.setText(matchesObjectList.get(position).getUserId());
        holder.mMatchName.setText(matchesObjectList.get(position).getName());
        if(!matchesObjectList.get(position).getProfileImageUrl().equals("default")){
            Glide.with(context).load(matchesObjectList.get(position).getProfileImageUrl()).into(holder.imageView);

        }
    }

    @Override
    public int getItemCount() {
        return this.matchesObjectList.size();
    }
}
