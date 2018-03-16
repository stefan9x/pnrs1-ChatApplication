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

        tvContact1 = findViewById(R.id.contact1);
        btnLogout = findViewById(R.id.logout);
        tvContact1.setOnClickListener(this);
        btnLogout.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.logout){
            Intent intMainactivity = new Intent(ContactsActivity.this, MainActivity.class);
            startActivity(intMainactivity);
        }

        if (view.getId() == R.id.contact1){
            Intent intMessageActivity = new Intent(ContactsActivity.this, MessageActivity.class);
            startActivity(intMessageActivity);
        }

    }
}
