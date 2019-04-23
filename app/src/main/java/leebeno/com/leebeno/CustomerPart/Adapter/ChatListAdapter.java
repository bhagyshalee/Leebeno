package leebeno.com.leebeno.CustomerPart.Adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import leebeno.com.leebeno.Api.WebUrls;
import leebeno.com.leebeno.ChatRoom;
import leebeno.com.leebeno.R;
import leebeno.com.leebeno.Services.DatabaseLocal;

import static leebeno.com.leebeno.Services.Config.getContactNameLi;
import static leebeno.com.leebeno.Services.Config.getLeeContactId;
import static leebeno.com.leebeno.Services.Config.getLeeOnlineStatus;
import static leebeno.com.leebeno.Services.Config.getLeeProfileImg;


public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ViewHolder> {
    private List<String> additionalServices = null;
    private List<String> additionalServicesD = null;
    private List<String> additionalServicesT = null;
    private Context context;


    public ChatListAdapter(List<String> services, Context context, List<String> additionalServicesD, List<String> additionalServicesT) {
        super();
        this.additionalServices = services;
        this.additionalServicesT = additionalServicesT;
        this.additionalServicesD = additionalServicesD;
        this.context = context;
    }

    @Override
    public ChatListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_design, parent, false);
        ChatListAdapter.ViewHolder viewHolder = new ChatListAdapter.ViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final ChatListAdapter.ViewHolder holder, final int position) {

//        Log.e("jkjkjkksdd",getContactNameLi.get(position)+" "+position);

        if (getContactNameLi!=null && getLeeOnlineStatus!=null && getLeeProfileImg!=null) {
            holder.chatName.setText(getContactNameLi.get(position));
            if (getLeeOnlineStatus.get(position).compareTo("true") == 0) {
                holder.onlineImg.setVisibility(View.VISIBLE);
            } else {
                holder.onlineImg.setVisibility(View.GONE);
            }
            Picasso.get()
                    .load(getLeeProfileImg.get(position))
                    .placeholder(R.drawable.user)
                    .error(R.drawable.user)
                    .into(holder.imgChatProfile);

        }

        holder.chatDescription.setText(additionalServicesD.get(position));
        holder.chatTiming.setText(additionalServicesT.get(position));

        holder.cardChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatRoom.class);
                intent.putExtra("contact_id",getLeeContactId.get(position));
                intent.putExtra("contact_name",getContactNameLi.get(position));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return additionalServices.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView chatName, chatDescription, chatTiming;
        ImageView imgChatProfile, onlineImg;
        CardView cardChat;

        public ViewHolder(View itemView) {
            super(itemView);

            chatName = (TextView) itemView.findViewById(R.id.chatName);
            chatDescription = (TextView) itemView.findViewById(R.id.chatDescription);
            chatTiming = (TextView) itemView.findViewById(R.id.chatTiming);
            imgChatProfile = (ImageView) itemView.findViewById(R.id.imgChatProfile);
            onlineImg = (ImageView) itemView.findViewById(R.id.onlineImg);
            cardChat = (CardView) itemView.findViewById(R.id.cardChat);


        }
    }
}