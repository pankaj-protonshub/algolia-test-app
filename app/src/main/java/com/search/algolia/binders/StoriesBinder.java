package com.search.algolia.binders;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.search.algolia.R;
import com.search.algolia.models.Hits;

import java.util.ArrayList;

public abstract class StoriesBinder extends RecyclerView.Adapter<StoriesBinder.StoriesAdapterViewHolder>{

    private Context context;
    private ArrayList<Hits.Story> stories;

    public StoriesBinder(Context context, ArrayList<Hits.Story> stories){
        this.context = context;
        this.stories = stories;
    }

    @NonNull
    @Override
    public StoriesAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.storiesitem, parent, false);
        StoriesAdapterViewHolder holder = new StoriesAdapterViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull StoriesAdapterViewHolder holder, int position) {
        holder.index.setText((position+1)+".");
        holder.title.setText(stories.get(position).getTitle());
        holder.createdAt.setText(stories.get(position).getCreatedAtAsString());
        if(holder.storySelected){
            holder.toggle.setChecked(true);
            holder.card.setCardBackgroundColor(ContextCompat.getColor(context, R.color.colorAccent));
        }else{
            holder.toggle.setChecked(false);
            holder.card.setCardBackgroundColor(ContextCompat.getColor(context, R.color.colorWhite));
        }

    }

    @Override
    public int getItemCount() {
        return stories.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void resetStories(){
        stories.clear();
        notifyDataSetChanged();
    }

    public void addStories(ArrayList<Hits.Story> stories){
        this.stories.addAll(stories);
        notifyDataSetChanged();
    }

    public abstract void onSwitchToggle(boolean state);


    /**
     *
     */
    class StoriesAdapterViewHolder extends RecyclerView.ViewHolder{

        private CardView card;
        private TextView title;
        private TextView createdAt;
        private Switch toggle;
        private TextView index;
        private boolean storySelected = false;

        public StoriesAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            card = (CardView) itemView.findViewById(R.id.itemStoriesCard);
            index = (TextView) itemView.findViewById(R.id.itemStoriesIndexTV);
            title = (TextView) itemView.findViewById(R.id.itemStoriesTitleTV);
            createdAt = (TextView) itemView.findViewById(R.id.itemStoriesCreatedAtTV);
            toggle = (Switch) itemView.findViewById(R.id.itemStoriesSwitch);

            toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        card.setCardBackgroundColor(ContextCompat.getColor(context, R.color.colorAccent));
                    }else{
                        card.setCardBackgroundColor(ContextCompat.getColor(context, R.color.colorWhite));
                    }
                    storySelected = isChecked;
                    onSwitchToggle(isChecked);
                }
            });
        }
    }

}
