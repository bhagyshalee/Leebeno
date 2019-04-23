package leebeno.com.leebeno.Model;

import android.graphics.Bitmap;

public class MsgGetterSetter {
    String message;
    String messageR;
    String msgType;
    String createdAt;
    String nickname;
    String onlineStatus;
    String profileUrl;
    String msgTimeDate;

    public String getChatType() {
        return chatType;
    }

    public void setChatType(String chatType) {
        this.chatType = chatType;
    }

    String lastSeen,sender_msg,receiver_msg,msg_time_stamp,message_id,chatType;

    public String getSender_msg() {
        return sender_msg;
    }

    public void setSender_msg(String sender_msg) {
        this.sender_msg = sender_msg;
    }

    public String getReceiver_msg() {
        return receiver_msg;
    }

    public void setReceiver_msg(String receiver_msg) {
        this.receiver_msg = receiver_msg;
    }

    public String getMsg_time_stamp() {
        return msg_time_stamp;
    }

    public void setMsg_time_stamp(String msg_time_stamp) {
        this.msg_time_stamp = msg_time_stamp;
    }

    public String getMessage_id() {
        return message_id;
    }

    public void setMessage_id(String message_id) {
        this.message_id = message_id;
    }

    public String getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(String lastSeen) {
        this.lastSeen = lastSeen;
    }
    public String getOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(String onlineStatus) {
        this.onlineStatus = onlineStatus;
    }
    public String getMsgTimeDate() {
        return msgTimeDate;
    }

    public void setMsgTimeDate(String msgTimeDate) {
        this.msgTimeDate = msgTimeDate;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    String getMsgStatus;

    public String getMessageR() {
        return messageR;
    }

    public void setMessageR(String messageR) {
        this.messageR = messageR;
    }

    public String getGetMsgStatus() {
        return getMsgStatus;
    }

    public void setGetMsgStatus(String getMsgStatus) {
        this.getMsgStatus = getMsgStatus;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    String userId;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }
}
