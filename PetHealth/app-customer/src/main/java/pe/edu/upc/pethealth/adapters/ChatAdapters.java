package pe.edu.upc.pethealth.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import pe.edu.upc.pethealth.R;
import pe.edu.upc.pethealth.models.Chat;

/**
 * Created by genob on 29/09/2017.
 */

public class ChatAdapters extends RecyclerView.Adapter<ChatAdapters.ViewHolder> {

    private List<Chat> chats;

    public ChatAdapters() {
    }

    public ChatAdapters(List<Chat> chats) {
        this.chats = chats;
    }

    public List<Chat> getChats() {
        return chats;
    }

    public ChatAdapters setChats(List<Chat> chats) {
        this.chats = chats;
        return this;
    }

    @Override
    public ChatAdapters.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_chat,parent,false));
    }

    @Override
    public void onBindViewHolder(ChatAdapters.ViewHolder holder, int position) {

        final Chat chat = chats.get(position);
        holder.chatNameTextView.setText(chat.getContact());
        holder.messageTextView.setText(chat.getMessage());
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView chatNameTextView;
        TextView messageTextView;
        public ViewHolder(View itemView) {
            super(itemView);
            chatNameTextView = (TextView) itemView.findViewById(R.id.chatNameTextView);
            messageTextView = (TextView) itemView.findViewById(R.id.messageTextView);
        }
    }
}
