package onetwopunch.seoulinsangshot.com.seoulinsangshot.View;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import onetwopunch.seoulinsangshot.com.seoulinsangshot.Controller.BottomNavigationViewHelper;
import onetwopunch.seoulinsangshot.com.seoulinsangshot.R;

public class NotifyDetailActivity extends AppCompatActivity {
    Intent home;
    Intent primary;
    Intent album;
    Intent notification;

    private BottomNavigationView navigation;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    setIntentFlag(home);
                    startActivity(home);
                    return true;
                case R.id.navigation_search:
                    setIntentFlag(primary);
                    startActivity(primary);
                    return true;
                case R.id.navigation_album:
                    setIntentFlag(album);
                    startActivity(album);
                    return true;
                case R.id.navigation_login:
                    CustomDialog customDialog=new CustomDialog();
                    FragmentManager fm = getSupportFragmentManager();
                    customDialog.show(fm, "Login Dialog");
                    return true;
                case R.id.navigation_notifications:

                    return true;

            }
            return false;
        }

    };

    TextView topicView;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify_detail);

        Drawable d = getResources().getDrawable(R.drawable.actionbar);
        getSupportActionBar().setBackgroundDrawable(d);

        topicView=(TextView)findViewById(R.id.notifyTopic);
        textView=(TextView)findViewById(R.id.notfiyText);
        String topic=getIntent().getExtras().getString("topic");
        String text=getIntent().getExtras().getString("text");
        topicView.setText(topic);
        textView.setText(text);

        home = new Intent(getApplicationContext(), MainActivity.class);
        primary = new Intent(getApplicationContext(), PrimaryActivity.class);
        album = new Intent(getApplicationContext(), AlbumActivity.class);

        navigation = (BottomNavigationView) findViewById(R.id.navigation_detail);
        navigation.setSelectedItemId(R.id.navigation_notifications);
        BottomNavigationViewHelper.disableShiftMode(navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }
    @Override
    protected void onResume() {
        super.onResume();

        navigation.setSelectedItemId(R.id.navigation_notifications);

    }

    public void setIntentFlag(Intent intent){
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
    }
}
