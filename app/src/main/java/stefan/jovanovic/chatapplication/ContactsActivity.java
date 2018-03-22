package stefan.jovanovic.chatapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ContactsActivity extends Activity implements View.OnClickListener {

    private TextView tvContact1;
    private Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        tvContact1 = findViewById(R.id.tv_contact1);
        btnLogout = findViewById(R.id.btn_logout);

        // Adds contacts and logout button listeners
        tvContact1.setOnClickListener(this);
        btnLogout.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        // Starts main activity if logout button is pressed
        if (view.getId() == R.id.btn_logout){
            Intent intMainactivity = new Intent(ContactsActivity.this, MainActivity.class);
            startActivity(intMainactivity);
        }

        // Starts message activity if contact textview is pressed
        if (view.getId() == R.id.tv_contact1){
            Intent intMessageActivity = new Intent(ContactsActivity.this, MessageActivity.class);
            startActivity(intMessageActivity);
        }
    }
}
