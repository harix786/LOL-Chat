package com.tesfayeabel.lolchat.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tesfayeabel.lolchat.LOLChatApplication;
import com.tesfayeabel.lolchat.Message;
import com.tesfayeabel.lolchat.R;

import java.util.ArrayList;
import java.util.List;

public class MessageAdapter extends BaseAdapter {
    public static final int DIRECTION_INCOMING = 0;
    public static final int DIRECTION_OUTGOING = 1;
    private List<Message> messages;
    private Context context;
    private int friendProfileIcon;

    public MessageAdapter(Context con) {
        this(con, new ArrayList<Message>());
    }

    public MessageAdapter(Context con, List<Message> list) {
        context = con;
        messages = list;
    }

    public void setFriendProfileIcon(int friendProfileIcon) {
        this.friendProfileIcon = friendProfileIcon;
    }

    //Gets called every time you update the view with an
    //incoming or outgoing message
    public void addMessage(Message message) {
        messages.add(message);
        notifyDataSetChanged();
    }

    //Returns how many messages are in the list
    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public Message getItem(int i) {
        return messages.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    //Tells your app how many possible layouts there are
    //In our case, right and left messages are our only 2 options
    @Override
    public int getViewTypeCount() {
        return 2;
    }

    //This returns either DIRECTION_INCOMING or DIRECTION_OUTGOING
    @Override
    public int getItemViewType(int i) {
        return messages.get(i).getDirection();
    }

    public List<Message> getMessages() {
        return messages;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        int direction = getItemViewType(i);
        ViewHolder holder;
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            holder = new ViewHolder();
            if (direction == DIRECTION_INCOMING) {
                convertView = mInflater.inflate(R.layout.message_left, viewGroup, false);
            } else if (direction == DIRECTION_OUTGOING) {
                convertView = mInflater.inflate(R.layout.message_right, viewGroup, false);
            }
            holder.view = (TextView) convertView.findViewById(R.id.text1);
            holder.imageView = (ImageView) convertView.findViewById(R.id.gameavatar);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Message message = getItem(i);
        if (holder.imageView != null)
            Picasso.with(context.getApplicationContext()).load(LOLChatApplication.getRiotResourceURL() + "/img/profileicon/" + friendProfileIcon + ".png").into(holder.imageView);
        holder.view.setText(message.getSender() + ": " + message.getMessage());
        return convertView;
    }

    public void clear() {
        messages.clear();
        notifyDataSetChanged();
    }

    private class ViewHolder {
        TextView view;
        ImageView imageView;
    }
}