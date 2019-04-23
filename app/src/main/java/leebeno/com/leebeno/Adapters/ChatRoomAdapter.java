package leebeno.com.leebeno.Adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;

import leebeno.com.leebeno.AccountSetting;
import leebeno.com.leebeno.Api.RequestCode;
import leebeno.com.leebeno.Api.WebTask;
import leebeno.com.leebeno.Api.WebUrls;
import leebeno.com.leebeno.Common.SharedPrefManager;
import leebeno.com.leebeno.ImageShowZoom;
import leebeno.com.leebeno.Model.MsgGetterSetter;
import leebeno.com.leebeno.R;

import static leebeno.com.leebeno.Api.WebUrls.changePassword;

public class ChatRoomAdapter extends RecyclerView.Adapter<ChatRoomAdapter.ViewHolder> {
    List<MsgGetterSetter> services;
    private Context context;
    ImageView deleteItem;


    public ChatRoomAdapter(Context context, List<MsgGetterSetter> services,ImageView deleteItem) {
        super();
        this.services = services;
        this.context = context;
        this.deleteItem = deleteItem;
    }

    @Override
    public ChatRoomAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dsign_chat_sent, parent, false);
        ChatRoomAdapter.ViewHolder viewHolder = new ChatRoomAdapter.ViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final ChatRoomAdapter.ViewHolder holder, final int position) {
//        getUserDetailId(context);

        if (services.get(position).getMessage()!=null)
        {
            holder.text_message_body.setText("" + services.get(position).getMessage());
            holder.text_message_body.setVisibility(View.VISIBLE);
            holder.sentImg.setVisibility(View.GONE);
        }
        else if (services.get(position).getProfileUrl()!=null)
        {
//            holder.sentImg.setImageBitmap(services.get(position).getProfileUrl());
            holder.sentImg.setVisibility(View.VISIBLE);
            holder.text_message_body.setVisibility(View.GONE);
        }
        holder.text_message_time.setText("" + services.get(position).getNickname());

        holder.sentImg.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                deleteItem.setVisibility(View.VISIBLE);
                return true;
            }
        });
        holder.sentImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteItem.setVisibility(View.GONE);
                holder.sentImg.buildDrawingCache();
                final Bitmap image= holder.sentImg.getDrawingCache();
                Dialog dialog;

                try {
                    dialog= new Dialog(context);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
                    dialog.setContentView(R.layout.design_popup_image);
                    dialog.setCanceledOnTouchOutside(true);
                    ImageView photoView = dialog.findViewById(R.id.imagePopup);
                    photoView.setImageBitmap(image);
                    dialog.show();


/*
                    photoView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent=new Intent(context, ImageShowZoom.class);
                            Bundle extras = new Bundle();
                            extras.putParcelable("imagebitmap", image);
                            intent.putExtras(extras);
                            context.startActivity(intent);
                        }
                    });
*/


                } catch (Exception e) {
                    e.printStackTrace();
                }



            }
        });
//        Log.e("jkfdfdbfdbdf",""+services.get(position).getMessage());


    }

    @Override
    public int getItemCount() {

        return services.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView text_message_time, text_message_body;
        RoundedImageView sentImg;
//        LinearLayout Layoutt;

        public ViewHolder(View itemView) {
            super(itemView);

            text_message_body = (TextView) itemView.findViewById(R.id.text_message_body);
            text_message_time = (TextView) itemView.findViewById(R.id.text_message_time);
            sentImg = (RoundedImageView ) itemView.findViewById(R.id.sentImg);

            /*
            notiBody = (TextView) itemView.findViewById(R.id.notiBody);
            notiTitle = (TextView) itemView.findViewById(R.id.notiTitle);
            notiDateTime = (TextView) itemView.findViewById(R.id.notiDateTime);
            Layoutt = (LinearLayout) itemView.findViewById(R.id.Layoutt);*/
        }
    }
}
