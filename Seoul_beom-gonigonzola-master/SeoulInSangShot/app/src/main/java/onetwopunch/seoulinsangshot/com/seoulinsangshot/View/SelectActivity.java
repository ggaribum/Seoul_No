package onetwopunch.seoulinsangshot.com.seoulinsangshot.View;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import onetwopunch.seoulinsangshot.com.seoulinsangshot.Controller.Adapter_Test;
import onetwopunch.seoulinsangshot.com.seoulinsangshot.Model.Model_Test;
import onetwopunch.seoulinsangshot.com.seoulinsangshot.R;

public class SelectActivity extends AppCompatActivity {

    Intent home;

    RecyclerView rv_select;
    LinearLayoutManager manager;
    Adapter_Test adapter_test;
    List<Model_Test> selectedImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
//        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#050518")));

        Drawable d = getResources().getDrawable(R.drawable.actionbar);
        getSupportActionBar().setBackgroundDrawable(d);

        home = new Intent(this, MainActivity.class);

        rv_select = (RecyclerView) findViewById(R.id.rv_select);
        manager = new LinearLayoutManager(getApplicationContext());


        selectedImg=(ArrayList<Model_Test>)getIntent().getSerializableExtra("imgList");

        if (selectedImg != null) {
            adapter_test = new Adapter_Test(selectedImg, getApplicationContext());
        }

        rv_select.setLayoutManager(manager);
        rv_select.setAdapter(adapter_test);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_primary, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        setIntentFlag(home);
        startActivity(home);

        return super.onOptionsItemSelected(item);
    }

    public void setIntentFlag(Intent intent){
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
    }

}
