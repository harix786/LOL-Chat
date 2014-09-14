package com.tesfayeabel.lolchat.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tesfayeabel.lolchat.LOLChatApplication;
import com.tesfayeabel.lolchat.R;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import jriot.objects.Game;
import jriot.objects.RawStats;

public class RecentGamesAdapter extends BaseExpandableListAdapter {

    private List<Game> games;
    private Context context;

    public RecentGamesAdapter(Context context, List<Game> list) {
        this.context = context;
        games = list;
    }

    @Override
    public int getGroupCount() {
        return games.size();
    }

    @Override
    public Game getChild(int group, int child) {
        return games.get(group);
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public long getChildId(int group, int child) {
        return child;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        final GroupHolder holder;
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.recent_game_item, parent, false);
            holder = new GroupHolder();
            holder.avatar = (ImageView) convertView.findViewById(R.id.gameavatar);
            holder.outcome = (TextView) convertView.findViewById(R.id.labelstatus);
            holder.type = (TextView) convertView.findViewById(R.id.labeltype);
            holder.map = (TextView) convertView.findViewById(R.id.labellocation);
            holder.date = (TextView) convertView.findViewById(R.id.labeldate);
            holder.ip = (TextView) convertView.findViewById(R.id.labelip);
            holder.assists = (TextView) convertView.findViewById(R.id.handval);
            holder.deaths = (TextView) convertView.findViewById(R.id.skullval);
            holder.kills = (TextView) convertView.findViewById(R.id.swordval);
            convertView.setTag(holder);
        } else {
            holder = (GroupHolder) convertView.getTag();
        }
        final Game game = getGroup(groupPosition);
        RawStats stats = game.getStats();
        holder.outcome.setText((stats.getWin() ? "Victory" : "Defeat") + " (" + LOLChatApplication.getGameSubType(game.getSubType()) + ")");
        holder.type.setText(LOLChatApplication.getGameMode(game.getGameMode()));
        holder.avatar.setImageDrawable(LOLChatApplication.loadChampionImage(context, game.getChampionId()));
        holder.map.setText(LOLChatApplication.getMapName(game.getMapId()));
        holder.ip.setText(String.valueOf(game.getIpEarned()) + " IP");
        holder.date.setText(DateFormat.getDateInstance().format(new Date(game.getCreateDate())));
        holder.kills.setText(String.valueOf(stats.getChampionsKilled()));
        holder.deaths.setText(String.valueOf(stats.getNumDeaths()));
        holder.assists.setText(String.valueOf(stats.getAssists()));
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final ChildHolder holder;
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.game_detail_item, parent, false);
            holder = new ChildHolder();
            holder.summonerSpell1 = (ImageView) convertView.findViewById(R.id.summonerSpell1);
            holder.summonerSpell2 = (ImageView) convertView.findViewById(R.id.summonerSpell2);
            holder.item1 = (ImageView) convertView.findViewById(R.id.summonerItem1);
            holder.item2 = (ImageView) convertView.findViewById(R.id.summonerItem2);
            holder.item3 = (ImageView) convertView.findViewById(R.id.summonerItem3);
            holder.item4 = (ImageView) convertView.findViewById(R.id.summonerItem4);
            holder.item5 = (ImageView) convertView.findViewById(R.id.summonerItem5);
            holder.item6 = (ImageView) convertView.findViewById(R.id.summonerItem5);
            convertView.setTag(holder);
        } else {
            holder = (ChildHolder) convertView.getTag();
        }
        Game game = getChild(groupPosition, childPosition);
        holder.item1.setImageResource(context.getResources().getIdentifier("item_" + game.getStats().getItem0(), "drawable", context.getPackageName()));
        holder.item2.setImageResource(context.getResources().getIdentifier("item_" + game.getStats().getItem1(), "drawable", context.getPackageName()));
        holder.item3.setImageResource(context.getResources().getIdentifier("item_" + game.getStats().getItem2(), "drawable", context.getPackageName()));
        holder.item4.setImageResource(context.getResources().getIdentifier("item_" + game.getStats().getItem3(), "drawable", context.getPackageName()));
        holder.item5.setImageResource(context.getResources().getIdentifier("item_" + game.getStats().getItem4(), "drawable", context.getPackageName()));
        holder.item6.setImageResource(context.getResources().getIdentifier("item_" + game.getStats().getItem5(), "drawable", context.getPackageName()));
        return convertView;
    }

    @Override
    public Game getGroup(int groupPosition) {
        return games.get(groupPosition);
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private class GroupHolder {
        ImageView avatar;
        TextView outcome;
        TextView type;
        TextView map;
        TextView date;
        TextView ip;
        TextView assists;
        TextView deaths;
        TextView kills;
    }

    private class ChildHolder {
        ImageView summonerSpell1;
        ImageView summonerSpell2;
        ImageView item1;
        ImageView item2;
        ImageView item3;
        ImageView item4;
        ImageView item5;
        ImageView item6;
    }
}