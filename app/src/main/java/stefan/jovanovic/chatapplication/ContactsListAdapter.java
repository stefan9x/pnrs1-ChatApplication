package stefan.jovanovic.chatapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

import static android.content.Context.MODE_PRIVATE;

public class ContactsListAdapter extends BaseAdapter implements View.OnClickListener {

    private Context cContext;
    private ArrayList<ContactClass> arLstContacts;

    public static final String MY_PREFS_NAME = "PrefsFile";

    public ContactsListAdapter(Context context) {
        cContext = context;
        arLstContacts = new ArrayList<ContactClass>();
    }

    // Update contacts list
    public void update(ContactClass[] contacts) {
        arLstContacts.clear();
        SharedPreferences prefs = cContext.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

        String loggedinUsername = prefs.getString("loggedinUsername", null);

        if (contacts != null) {
            for (ContactClass contact : contacts){
                if (!contact.getsUserName().equals(loggedinUsername))
                    arLstContacts.add(contact);
            }
        }
        notifyDataSetChanged();
    }

    public void updateOne(ContactClass oldContact, ContactClass newContact) {

        int index = arLstContacts.indexOf(oldContact);
        arLstContacts.set(index, newContact);

        notifyDataSetChanged();
    }

    // Remove contact from list
    public void removecontact(int position) {
        arLstContacts.remove(position);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return arLstContacts.size();
    }

    @Override
    public Object getItem(int position) {
        Object contact = null;
        try {
            contact = arLstContacts.get(position);
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
            view = inflater.inflate(R.layout.layout_contact, null);

            ContactHolder holder = new ContactHolder();
            holder.tvFirstletter = view.findViewById(R.id.tv_name_first_letter);
            holder.tvName = view.findViewById(R.id.tv_contact_name);
            holder.imgBtnSend = view.findViewById(R.id.imgbtn_send);
            holder.tvLastMsg = view.findViewById(R.id.tv_contact_last_msg);
            holder.imgBtnSend.setOnClickListener(this);
            view.setTag(holder);
        }

        ContactClass contactclass = (ContactClass) getItem(position);
        ContactHolder holder = (ContactHolder) view.getTag();

        // Generating random background color
        Random rnd = new Random();
        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));

        // Getting first letter from name
        holder.tvFirstletter.setText(contactclass.getsUserName().substring(0, 1).toUpperCase());
        holder.tvFirstletter.setBackgroundColor(color);

        // Setting text to name
        String name = contactclass.getsUserName();
        if (name.length() > 20) {
            String trimmedName = name.substring(0,20)+"...";
            holder.tvName.setText(trimmedName);
        } else {
            holder.tvName.setText(name);
        }
        String lastMsg = contactclass.getsLastMsg();
        holder.tvLastMsg.setText(lastMsg);

        // Setting contact id on button tag
        holder.imgBtnSend.setTag(contactclass.getsUserName());

        return view;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.imgbtn_send) {

            // Putting receiver userid into SharedPreference file
            SharedPreferences.Editor editor = cContext.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
            editor.putString("receiverUsername", view.getTag().toString());
            editor.apply();

            // Starting message activity
            cContext.startActivity(new Intent(cContext.getApplicationContext(), MessageActivity.class));
        }
    }

    private class ContactHolder {
        private TextView tvFirstletter = null;
        private TextView tvName = null;
        private TextView tvLastMsg = null;
        private ImageButton imgBtnSend = null;
    }
}
