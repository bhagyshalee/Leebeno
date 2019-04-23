package leebeno.com.leebeno.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.opengl.Visibility;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.vision.text.Line;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import leebeno.com.leebeno.Api.WebUrls;
import leebeno.com.leebeno.Model.MsgGetterSetter;
import leebeno.com.leebeno.R;
import leebeno.com.leebeno.RecyclerViewItemClickListener;
import leebeno.com.leebeno.Services.DatabaseLocal;

import static leebeno.com.leebeno.Services.Config.getLeeContactIdMsg;
import static leebeno.com.leebeno.Services.Config.getMessageId;
import static leebeno.com.leebeno.Services.Config.getMessageTimeStamp;
import static leebeno.com.leebeno.Services.Config.getMessageType;
import static leebeno.com.leebeno.Services.Config.getReceiverMsg;
import static leebeno.com.leebeno.Services.Config.getSenderMessage;

public class ChatBothSideAdapter extends RecyclerView.Adapter implements View.OnClickListener {
    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;

    private Context mContext;
    private List<MsgGetterSetter> mMessageList;
    ImageView imgDelete;
    LinearLayout profileDetail;
    List<String> getListSelect;
    List<Integer> selectedMsgPosition;
    RecyclerViewItemClickListener itemClickListener;
    int rowIndex = -1;
    DatabaseLocal databaseLocal;

    public ChatBothSideAdapter(Context context, List<MsgGetterSetter> messageList, ImageView imgDeletee, LinearLayout profileDetaill, RecyclerViewItemClickListener recyclerViewItemClickListener) {
        mContext = context;
        mMessageList = messageList;
        imgDelete = imgDeletee;
        profileDetail = profileDetaill;
        itemClickListener = recyclerViewItemClickListener;
        getListSelect = new ArrayList<>();
        selectedMsgPosition = new ArrayList<>();
        databaseLocal = new DatabaseLocal(context);

    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }

    // Determines the appropriate ViewType according to the sender of the message.
    @Override
    public int getItemViewType(int position) {
//        UserMessage message = (UserMessage) mMessageList.get(position);

       /* if (message.getSender().getUserId().equals(SendBird.getCurrentUser().getUserId())) {
            // If the current user is the sender of the message
            return VIEW_TYPE_MESSAGE_SENT;
        } else {
            // If some other user sent the message
            return VIEW_TYPE_MESSAGE_RECEIVED;
        }*/


        if (mMessageList.get(position).getMessage() != null) {
            return VIEW_TYPE_MESSAGE_SENT;
        } else {
            return VIEW_TYPE_MESSAGE_RECEIVED;
        }


    }

    // Inflates the appropriate layout according to the ViewType.
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.dsign_chat_sent, parent, false);
            return new SentMessageHolder(view);
        } else if (viewType == VIEW_TYPE_MESSAGE_RECEIVED) {

            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.dsign_chat_received, parent, false);
            return new ReceivedMessageHolder(view);
        }

        return null;
    }

    // Passes the message object to a ViewHolder so that the contents can be bound to UI.
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MsgGetterSetter message = (MsgGetterSetter) mMessageList.get(position);

        switch (holder.getItemViewType()) {
            case VIEW_TYPE_MESSAGE_SENT:
                getSentMsg((SentMessageHolder) holder, position);
                break;
            case VIEW_TYPE_MESSAGE_RECEIVED:
                getReceivedMsg((ReceivedMessageHolder) holder, position);
                break;

        }
    }

    @Override
    public void onClick(View v) {
       /* if(v.equals(imgViewRemoveIcon)){
            removeAt(getPosition());
        }else if (mItemClickListener != null) {
            mItemClickListener.onItemClick(v, getPosition());
        }*/
    }

    private void removeAt(int position,SentMessageHolder holder, LinearLayout selectLayout) {
        if (getListSelect.size() != 0) {
            String[] arr = new String[getListSelect.size()];

            for (int i = 0; i < getListSelect.size(); i++) {
                arr[i] = getListSelect.get(i);
                getListSelect.remove(selectedMsgPosition.get(i));
                mMessageList.remove(selectedMsgPosition.get(i));
                notifyItemRemoved(selectedMsgPosition.get(i));
                notifyItemRangeChanged(selectedMsgPosition.get(i), mMessageList.size());
                selectedMsgPosition.remove(selectedMsgPosition.get(i));
//                notifyDataSetChanged();
                selectLayout.setBackgroundColor(mContext.getResources().getColor(R.color.white));
                databaseLocal.deleteMessages(arr);
                getLeeContactIdMsg.clear();
                getSenderMessage.clear();
                getReceiverMsg.clear();
                getMessageType.clear();
                getMessageTimeStamp.clear();
                getMessageId.clear();
            }
            imgDelete.setVisibility(View.GONE);
            profileDetail.setVisibility(View.VISIBLE);
//            getSentMsg((SentMessageHolder) holder, position);
//            selectLayout.setBackgroundColor(mContext.getResources().getColor(R.color.white));
//            selectLayout.setBackgroundColor(mContext.getResources().getColor(R.color.white));
//            Log.e("positionOfMSG ", "" + position);
//
        }

    }

    private void getSentMsg(final SentMessageHolder holder, final int pos) {
        holder.messageText.setText(mMessageList.get(pos).getMessage());
        holder.messageText.setVisibility(View.VISIBLE);
        holder.timeText.setText(mMessageList.get(pos).getCreatedAt());


        /*select*/
        holder.layoutChatSend.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                rowIndex = pos;
                profileDetail.setVisibility(View.GONE);
                imgDelete.setVisibility(View.VISIBLE);
                getListSelect.add(mMessageList.get(pos).getMessage_id());
                selectedMsgPosition.add(pos);
                notifyDataSetChanged();
                return false;
            }
        });

        if (imgDelete.getVisibility() == View.VISIBLE) {
            holder.layoutChatSend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*unselect*/

                    ColorDrawable layoutColor = (ColorDrawable) holder.layoutChatSend.getBackground();
                    int colorId = layoutColor.getColor();
                    Log.e("jhfdjhfdsbfdk", colorId + "");
                    if (colorId == mContext.getResources().getColor(R.color.lightGray)) {
                        if (getListSelect.size() == 1) {
                            getListSelect.clear();
                            imgDelete.setVisibility(View.GONE);
                            profileDetail.setVisibility(View.VISIBLE);
                            holder.layoutChatSend.setBackgroundColor(mContext.getResources().getColor(R.color.white));

                        } else {
                            getListSelect.remove(pos);
                            holder.layoutChatSend.setBackgroundColor(mContext.getResources().getColor(R.color.white));
                        }
                    } else {
                        getListSelect.add(mMessageList.get(pos).getMessage_id());
                        selectedMsgPosition.add(pos);
                        holder.layoutChatSend.setBackgroundColor(mContext.getResources().getColor(R.color.lightGray));
                    }

                }
            });
        }

        if (rowIndex == pos) {
            holder.layoutChatSend.setBackgroundColor(mContext.getResources().getColor(R.color.lightGray));
        } else {
            holder.layoutChatSend.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        }



        /*delete section*/
        imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeAt(pos,holder, holder.layoutChatSend);
//                holder.layoutChatSend.setBackgroundColor(mContext.getResources().getColor(R.color.white));
            }
        });
    }


    private void getReceivedMsg(ReceivedMessageHolder holder, int pos) {

    }

    private class SentMessageHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView messageText, timeText,timeDateChange;
        ImageView sentImg;
        LinearLayout layoutChatSend;

        SentMessageHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            messageText = (TextView) itemView.findViewById(R.id.text_message_body);
            timeText = (TextView) itemView.findViewById(R.id.text_message_time);
            timeDateChange = (TextView) itemView.findViewById(R.id.timeDateChange);
            sentImg = (ImageView) itemView.findViewById(R.id.sentImg);
            layoutChatSend = (LinearLayout) itemView.findViewById(R.id.layoutChatSend);
        }

//        void bind(MsgGetterSetter message, final int position) {
//            messageText.setText(message.getMessage());
//            messageText.setVisibility(View.VISIBLE);
//            // Format the stored timestamp into a readable String using method.
//            timeText.setText(message.getCreatedAt());
////            sentImg.setImageBitmap();
//            layoutChatSend.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View v) {
////                    layoutChatSend.setBackgroundColor(mContext.getResources().getColor(R.color.lightGray));
//                    profileDetail.setVisibility(View.GONE);
//                    imgDelete.setVisibility(View.VISIBLE);
//                    getListSelect.add(position);
//
//                    Log.e("getDeleteVisibilityaft ", "" + position);
//                    return false;
//                }
//            });
//
//            if (getListSelect.size() != 0) {
//                int getPosition = getListSelect.get(getListSelect.size() - 1);
//                for (int i = 0; i < getListSelect.size(); i++) {
//                    layoutChatSend.setBackgroundColor(mContext.getResources().getColor(R.color.lightGray));
//                }
//            }
//            Log.e("getListSize ", "" + getListSelect.size());
//            if (imgDelete.getVisibility() == View.VISIBLE) {
//                Log.e("getDeleteVisibility ", "" + imgDelete.getVisibility());
//                layoutChatSend.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                       /* if (getListSelect.size()!=0)
//                        {
//                            getListSelect.get()
//                        }else
//                        {*/
////                        layoutChatSend.setBackgroundColor(mContext.getResources().getColor(R.color.lightGray));
//                        getListSelect.add(position);
//
//
////                        }
//                    }
//                });
//            }
//
//
//        }

        @Override
        public void onClick(View v) {
            itemClickListener.recyclerViewClicked(v, this.getLayoutPosition());
        }
    }

    private class ReceivedMessageHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView messageText, timeText, nameText;
//        ImageView profileImage;

        ReceivedMessageHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            messageText = (TextView) itemView.findViewById(R.id.text_message_body);
            timeText = (TextView) itemView.findViewById(R.id.text_message_time);
            nameText = (TextView) itemView.findViewById(R.id.text_message_name);

//            profileImage = (ImageView) itemView.findViewById(R.id.image_message_profile);
        }

        void bind(MsgGetterSetter message) {
            messageText.setText(message.getMessage());

            // Format the stored timestamp into a readable String using method.
            timeText.setText(message.getCreatedAt());

            nameText.setText(message.getNickname());
            nameText.setVisibility(View.GONE);

            // Insert the profile image from the URL into the ImageView.
/*
            Picasso.get()
                    .load(message.getProfileUrl())
                    .placeholder(R.drawable.default_image)
                    .error(R.drawable.default_image)
                    .into(profileImage);

*/
//            profileImage.setImageDrawable(mContext.getResources().getDrawable(R.drawable.no_image));
//            profileImage.setImageBitmap(message.getProfileUrl());

//            Utils.displayRoundImageFromUrl(mContext, message.getSender().getProfileUrl(), profileImage);
        }

        @Override
        public void onClick(View v) {
            itemClickListener.recyclerViewClicked(v, this.getPosition());
        }
    }
}