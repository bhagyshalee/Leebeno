package leebeno.com.leebeno.Services;

import android.content.SharedPreferences;

import java.util.ArrayList;

public class Config
{
  public static SharedPreferences.Editor editor;
  public static String getLoginStatus="";
  public static String GCM_TOKEN="";
  public static ArrayList<String> skillsLabour;
  public static ArrayList<String> skillsLabourSave;
  public static ArrayList<String> skillsLabourSaveID;

  /*contact detail lists*/
  public static ArrayList<String> getContactIdLi;
  public static ArrayList<String> getContactNumberLi;
  public static ArrayList<String> getContactNameLi;
  public static ArrayList<String> getContactProfileLi;


  /*chat available detail list*/
  public static ArrayList<String> getLeeContactId;
  public static ArrayList<String> getLeeProfileImg;
  public static ArrayList<String> getLeeChatStatus;
  public static ArrayList<String> getLeeBlockStatus;
  public static ArrayList<String> getLeeLastSeen;
  public static ArrayList<String> getLeeOnlineStatus;

  /*chat msg detail*/
  public static ArrayList<String> getMessageId;
  public static ArrayList<String> getChatType;
  public static ArrayList<String> getGroupId;
  public static ArrayList<String> getSenderMessage;
  public static ArrayList<String> getReceiverMsg;
  public static ArrayList<String> getMessageType;
  public static ArrayList<String> getMessageReadStatus;
  public static ArrayList<String> getMessageTimeStamp;
  public static ArrayList<String> getLeeContactIdMsg;

}
