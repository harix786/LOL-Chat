package com.tesfayeabel.lolchat;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;

import com.github.theholywaffle.lolchatapi.ChatServer;
import com.github.theholywaffle.lolchatapi.FriendRequestPolicy;
import com.github.theholywaffle.lolchatapi.LolChat;
import com.github.theholywaffle.lolchatapi.LolStatus;
import com.github.theholywaffle.lolchatapi.listeners.ChatListener;
import com.github.theholywaffle.lolchatapi.wrapper.Friend;

import java.util.ArrayList;
import java.util.List;

import jriot.main.JRiot;
import jriot.main.JRiotException;
import jriot.objects.Summoner;

public class ChatService extends Service{
    private LolChat lolChat;
    private final IBinder mBinder = new LocalBinder();
    public ChatListener chatListener;
    private ArrayList<Message> missedMessages = new ArrayList<Message>();

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
    public void connectLOLChat(final String username, final String password, final String server, final LoginCallBack callBack)
    {
        final Notification.Builder notification = new Notification.Builder(this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                lolChat = new LolChat(ChatServer.getChatServerByName(server), FriendRequestPolicy.REJECT_ALL, "99a0d299-2476-4539-901f-0fdd0598bcf8");
                if(lolChat.login(username, password))
                {
                    Intent mainIntent = new Intent(getApplicationContext(), LOLChatMain.class);
                    notification
                            .setSmallIcon(R.drawable.ic_launcher)
                            .setContentText(getString(R.string.app_name) + " is running")
                            .setContentTitle(lolChat.getConnectedUsername())
                            .setTicker(getString(R.string.app_name) + " is now running")
                            .setContentIntent(PendingIntent.getActivity(getApplicationContext(), 0, mainIntent, PendingIntent.FLAG_UPDATE_CURRENT))
                            .setDefaults(Notification.DEFAULT_VIBRATE);
                    startForeground(69, notification.getNotification());
                    LolStatus lolStatus = new LolStatus();
                    try {
                        JRiot riot = lolChat.getRiotApi();
                        Summoner connected = riot.getSummoner(lolChat.getConnectedUsername());
                        lolStatus.setLevel((int)connected.getSummonerLevel());
                        lolStatus.setProfileIconId(connected.getProfileIconId());
                    }catch(JRiotException e)
                    {
                        e.printStackTrace();
                    }
                    lolStatus.setStatusMessage("USING BETA ABEL CHAT APP");
                    lolChat.setStatus(lolStatus);
                    lolChat.addChatListener(new ChatListener() {
                        @Override
                        public void onMessage(Friend friend, String message) {
                            missedMessages.add(new Message(friend.getName(), message, MessageAdapter.DIRECTION_INCOMING));
                            PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(),
                                    0, new Intent(getApplicationContext(), LOLChatMain.class), 0);
                            Notification notification = new Notification.Builder(ChatService.this)
                                    .setContentTitle("New message")
                                    .setContentText("Message from " + friend.getName())
                                    .setSmallIcon(R.drawable.ic_launcher)
                                    .setStyle(getStyle())
                                    .setContentIntent(contentIntent)
                                    .getNotification();
                            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                            mNotificationManager.notify(69, notification);
                            if(chatListener != null)
                            {
                                chatListener.onMessage(friend, message);
                            }else {
                                SharedPreferences sharedPreferences = getSharedPreferences("messageHistory", Context.MODE_PRIVATE);
                                String messages = sharedPreferences.getString(friend.getName() + "History", "");
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                if(!messages.equals(""))
                                {
                                    messages = messages + "\n";
                                }
                                editor.putString(friend.getName() + "History", messages  + new Message(friend.getName(), message, MessageAdapter.DIRECTION_INCOMING));
                                editor.apply();
                            }
                        }
                    });
                }
                callBack.onLogin(lolChat.isAuthenticated());
            }
        }).start();
    }
    public Notification.InboxStyle getStyle()
    {
        Notification.InboxStyle style =  new Notification.InboxStyle();
        List<Message> list = missedMessages.subList(Math.max(missedMessages.size() - 3, 0), missedMessages.size());//get list of last three messages
        for(Message m: list)
        {
            style.addLine(m.getSender() + ": " + m.getMessage());
        }
        style.setSummaryText(missedMessages.size() + " more");
        return style;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if(lolChat != null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    lolChat.disconnect();
                }
            }).start();
        }
    }
    public class LocalBinder extends Binder {
        ChatService getService() {
            // Return this instance of LocalService so clients can call public methods
            return ChatService.this;
        }
    }
    public LolChat getLolChat()
    {
        return lolChat;
    }

}