package com.example.parking;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyViewHolder> {
    private List<ChatData> mDataset;
    private String myNickname;
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        //each data item is just a string in this case
        public TextView TextView_nickname;
        public TextView TextView_msg;
        public LinearLayout TextView_set;
        public View rootView;

        public MyViewHolder(View v) {
            super(v);
            TextView_nickname = v.findViewById(R.id.TextView_nickname);
            TextView_msg = v.findViewById(R.id.TextView_msg);
            TextView_set = v.findViewById(R.id.TextView_set);
            rootView = v;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ChatAdapter(List<ChatData> myDataset, Context context, String myNickname) {
        mDataset = myDataset;
        this.myNickname = myNickname;
    }

//    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public ChatAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //create a new view
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_chat, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    //Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        ChatData chat = mDataset.get(position);

//        holder.TextView_nickname.setText(chat.getNickname()); //Data Transfer Object
//        holder.TextView_msg.setText(chat.getMsg()); //Data Transfer Object

        if(chat.getNickname().equals(this.myNickname)) {
            holder.TextView_set.setGravity(Gravity.END);
            holder.TextView_msg.setText(chat.getMsg());

//            holder.TextView_msg.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
//            holder.TextView_msg.setGravity(Gravity.END);
//            holder.TextView_nickname.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
//            holder.TextView_msg.setGravity(View.LAYOUT_DIRECTION_RTL);
        }
        else {
            holder.TextView_nickname.setText(chat.getNickname()); //Data Transfer Object
            holder.TextView_msg.setText(chat.getMsg()); //Data Transfer Object
            holder.TextView_set.setGravity(Gravity.START);
//            holder.TextView_msg.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
//            holder.TextView_nickname.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
//            holder.TextView_nickname.setGravity(Gravity.END);
//            holder.TextView_msg.setGravity(Gravity.END);
        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        //?????? ?????????
        return mDataset == null ? 0 : mDataset.size();
    }

    public ChatData getChat(int position) {
        return mDataset != null ? mDataset.get(position) : null;
    }

    public void addChat(ChatData chat) {
        mDataset.add(chat);
        notifyItemInserted(mDataset.size()-1); //??????
    }
}
