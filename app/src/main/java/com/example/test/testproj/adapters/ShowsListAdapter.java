package com.example.test.testproj.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

//import com.bumptech.glide.Glide;
import com.bumptech.glide.Glide;
import com.example.test.testproj.R;
import com.example.test.testproj.models.Show;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Custom RecyclerView adapter extends {@link RecyclerView.Adapter}
 *
 *
 *
 *
 * @author Ruslan Zosimov
 * @version 1.0
 */


public class ShowsListAdapter extends RecyclerView.Adapter<ShowsListAdapter.ShowViewHolder> {
    private LayoutInflater inflater;
    private Context context;
    List<Show> showList = Collections.emptyList();
    ShowClickListener showClickListener;

    public ShowsListAdapter(Context context, List<Show> showList) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.showList = showList;
    }

    @Override
    public ShowViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.show_item, parent, false);
        ShowViewHolder holder = new ShowViewHolder(view);
        return holder;
    }

    public void setShowClickListener(ShowClickListener showClickListener) {
        this.showClickListener = showClickListener;
    }

    @Override
    public void onBindViewHolder(ShowsListAdapter.ShowViewHolder holder, int position) {
        Show currentShow = showList.get(position);
        Glide.with(context).load(currentShow.getImage()).into(holder.showImage);
        holder.showName.setText(currentShow.getName());
        holder.showDescription.setText(currentShow.getDescription());
        holder.showRate.setText(String.valueOf(currentShow.getRate()));
        if (currentShow.getFav() == 1){ holder.showFavorite.setImageResource(R.drawable.heart_like);}
        else {holder.showFavorite.setImageResource(R.drawable.heart_unlike);}
    }

    @Override
    public int getItemCount() {
        return showList.size();
    }

    public class ShowViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView showImage;
        TextView showName;
        TextView showDescription;
        TextView showRate;
        ImageButton showFavorite;
        RelativeLayout showItemLayout;

        public ShowViewHolder(View itemView) {
            super(itemView);
            showItemLayout = (RelativeLayout) itemView.findViewById(R.id.showItemLayout);
            showImage = (ImageView) itemView.findViewById(R.id.showImage);
            showName = (TextView) itemView.findViewById(R.id.showName);
            showDescription = (TextView) itemView.findViewById(R.id.showDescription);
            showRate = (TextView) itemView.findViewById(R.id.showRate);
            showFavorite = (ImageButton) itemView.findViewById(R.id.showFavorite);
            showItemLayout.setOnClickListener(this);
            showFavorite.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            if (showClickListener != null) {
                showClickListener.showClicked(view, getPosition());
            }
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public void setFilter(List<Show> filteShowList) {
        showList = new ArrayList<Show>();
        showList.addAll(filteShowList);
        notifyDataSetChanged();

    }

    //Inteface for event handling in fragments
    public interface ShowClickListener {
        public void showClicked(View view, int position);
    }
}
