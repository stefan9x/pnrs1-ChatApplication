package stefan.jovanovic.chatapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

public class MessageListAdapter extends BaseAdapter {

    private Context cContext;
    private ArrayList<MessageClass> arLstMessages;

    private static final String MY_PREFS_NAME = "PrefsFile";

    public MessageListAdapter(Context context) {
        cContext = context;
        arLstMessages = new ArrayList<MessageClass>();
    }

    public void update(MessageClass[] messages) {
        arLstMessages.clear();
        if (messages != null) {
            Collections.addAll(arLstMessages, messages);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return arLstMessages.size();
    }

    @Override
    public Object getItem(int position) {
        Object contact = null;
        try {
            contact = arLstMessages.get(position);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        return contact;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) cContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.layout_message, null);

            MessageListAdapter.MessageHolder holder = new MessageListAdapter.MessageHolder();
            holder.tvMessage = view.findViewById(R.id.tv_message);
            holder.tvSender = view.findViewById(R.id.message_sender);

            view.setTag(holder);
        }

        MessageClass messageclass = (MessageClass) getItem(position);
        MessageListAdapter.MessageHolder holder = (MessageListAdapter.MessageHolder) view.getTag();

        // Setting message to text view
        holder.tvMessage.setText(messageclass.getsMessage());

        // Setting time
        holder.tvSender.setText(messageclass.getsSender());

        // Getting logged in user userid from shared preference file
        SharedPreferences prefs = cContext.getSharedPreferences(MY_PREFS_NAME, cContext.MODE_PRIVATE);
        String loggedinUsername = prefs.getString("loggedinUsername", null);

        // Setting text and background colors and gravity based on users
        if ((messageclass.getsSender().compareTo(loggedinUsername) == 0)) {
            holder.tvMessage.setGravity(Gravity.RIGHT | Gravity.CENTER);
            holder.tvMessage.setTextColor(Color.rgb(255, 255, 255));
            holder.tvMessage.setBackgroundColor(Color.argb(50, 173, 173, 173));
            holder.tvSender.setGravity(Gravity.RIGHT);
            holder.tvSender.setTextColor(Color.rgb(255, 255, 255));
            holder.tvSender.setBackgroundColor(Color.argb(50, 173, 173, 173));
        } else {
            holder.tvMessage.setGravity(Gravity.LEFT | Gravity.CENTER);
            holder.tvMessage.setTextColor(Color.rgb(234, 117, 0));
            holder.tvMessage.setBackgroundColor(Color.argb(0, 255, 255, 255));
            holder.tvSender.setGravity(Gravity.LEFT);
            holder.tvSender.setTextColor(Color.rgb(234, 117, 0));
            holder.tvSender.setBackgroundColor(Color.argb(0, 255, 255, 255));
        }
        return view;
    }

    private class MessageHolder {
        private TextView tvMessage = null;
        private TextView tvSender = null;
    }
}


