package stefan.jovanovic.chatapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class ContactsListAdapter extends BaseAdapter implements View.OnClickListener{

    private Context cContext;
    private ArrayList<ContactClass> arlstContacts;

    public ContactsListAdapter(Context context){
        cContext = context;
        arlstContacts = new ArrayList<ContactClass>();
    }

    public void update(ContactClass[] contacts) {
        arlstContacts.clear();
        if(contacts != null) {
            for(ContactClass contact : contacts) {
                arlstContacts.add(contact);
            }
        }

        notifyDataSetChanged();
    }

    public void removecontact(int position){
        arlstContacts.remove(position);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return arlstContacts.size();
    }

    @Override
    public Object getItem(int position) {
        Object contact = null;
        try{
            contact = arlstContacts.get(position);
        } catch (IndexOutOfBoundsException e){
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

        if (view == null){
            LayoutInflater inflater = (LayoutInflater) cContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.layout_contact, null);

            ContactHolder holder = new ContactHolder();
            holder.tvFirstletter = (TextView) view.findViewById(R.id.tv_name_first_letter);
            holder.tvName = (TextView) view.findViewById(R.id.tv_contact_name);
            holder.imgbtnSend = (ImageButton) view.findViewById(R.id.imgbtn_send);
            holder.imgbtnSend.setOnClickListener(this);
            view.setTag(holder);
        }

        ContactClass contactclass = (ContactClass) getItem(position);
        ContactHolder holder = (ContactHolder) view.getTag();

        // Generating random background color
        Random rnd = new Random();
        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));

        // Getting first letter from name
        holder.tvFirstletter.setText(contactclass.getsFirstName().substring(0,1).toUpperCase());
        holder.tvFirstletter.setBackgroundColor(color);

        // Setting text to name
        String name = contactclass.getsFirstName() + " " + contactclass.getsLastName();
        holder.tvName.setText(name);

        // Setting contact name on button tag
        holder.imgbtnSend.setTag(contactclass.getsFirstName() + " " + contactclass.getsLastName());

        return view;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.imgbtn_send){
            Intent intMessageactivity = new Intent(cContext.getApplicationContext(), MessageActivity.class);
            intMessageactivity.putExtra("contact_name", view.getTag().toString());
            cContext.startActivity(intMessageactivity);
        }
    }

    private class ContactHolder{
        public TextView tvFirstletter = null;
        public TextView tvName = null;
        public ImageButton imgbtnSend = null;
    }
}
