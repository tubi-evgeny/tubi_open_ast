package ru.tubi.project.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ru.tubi.project.R;
import ru.tubi.project.models.Catalog;
import ru.tubi.project.models.MessageModel;

public class MessageFeedAdapter
        extends RecyclerView.Adapter<MessageFeedAdapter.ViewHolder> {

    private final LayoutInflater inflater;
    private final List<MessageModel> messageList;

    public MessageFeedAdapter(Context context, List<MessageModel> messageList) {
        this.inflater =  LayoutInflater.from(context);
        this.messageList = messageList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.item_message_feed,parent,false);
        return new MessageFeedAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MessageModel message = messageList.get(position);

        holder.tvMessage.setText(""+message.getMessage_text());
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final TextView tvMessage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMessage=itemView.findViewById(R.id.tvMessage);
        }
    }
}
