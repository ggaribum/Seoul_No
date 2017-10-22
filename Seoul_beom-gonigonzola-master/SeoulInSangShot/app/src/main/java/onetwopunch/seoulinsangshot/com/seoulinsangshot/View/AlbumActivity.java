package onetwopunch.seoulinsangshot.com.seoulinsangshot.View;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import java.util.ArrayList;

import it.moondroid.coverflow.components.ui.containers.FeatureCoverFlow;
import onetwopunch.seoulinsangshot.com.seoulinsangshot.Controller.Adapter_Best;
import onetwopunch.seoulinsangshot.com.seoulinsangshot.Controller.BottomNavigationViewHelper;
import onetwopunch.seoulinsangshot.com.seoulinsangshot.Controller.CoverFlowAdapter;
import onetwopunch.seoulinsangshot.com.seoulinsangshot.DataManager.Data.Best2VO;
import onetwopunch.seoulinsangshot.com.seoulinsangshot.DataManager.Remote.RetrofitClient;
import onetwopunch.seoulinsangshot.com.seoulinsangshot.DataManager.Remote.RetrofitService;
import onetwopunch.seoulinsangshot.com.seoulinsangshot.Model.Model_Best;
import onetwopunch.seoulinsangshot.com.seoulinsangshot.Model.Model_Best2;
import onetwopunch.seoulinsangshot.com.seoulinsangshot.Model.Model_Image;
import onetwopunch.seoulinsangshot.com.seoulinsangshot.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AlbumActivity extends AppCompatActivity {

    Intent home;
    Intent primary;
    Intent notify;


    int BestCount =0 ;
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
                    return true;
                case R.id.navigation_notifications:
                    setIntentFlag(notify);
                    startActivity(notify);
                    return true;
                case R.id.navigation_login:
                    CustomDialog customDialog=new CustomDialog();
                    FragmentManager fm = getSupportFragmentManager();
                    customDialog.show(fm, "Login Dialog");
                    return true;
            }
            return false;
        }

    };

    private FeatureCoverFlow mCoverFlow;
    private CoverFlowAdapter mAdapter;
    TextView idView;
    TextView countView;
    ArrayList<Model_Image> mData;
    String myAlbumID="KwakGee";

    Best2VO repo;
    ArrayList<Model_Best2> tempList;
    ArrayList<Model_Best2> mData2;


    private TextSwitcher switcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_album);
//        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#050518")));

        Drawable d = getResources().getDrawable(R.drawable.actionbar);
        getSupportActionBar().setBackgroundDrawable(d);

        home = new Intent(getApplicationContext(), MainActivity.class);
        primary = new Intent(getApplicationContext(), PrimaryActivity.class);
        notify = new Intent(getApplicationContext(), NotifyActivity.class);

        //  context=getApplication();
        mData=new ArrayList<>();


        //       idView.setText(myAlbumID+"의 앨범"); // 아이디

        for(int i=0; i< BaseActivity.imageList.size();i++){
            String id=BaseActivity.imageList.get(i).getId();
            //Log.d("what is id",BaseActivity.imageList.get(i).getID)
            String url= BaseActivity.imageList.get(i).getUrl();
            String tip= BaseActivity.imageList.get(i).getTip();
            String likecount=BaseActivity.imageList.get(i).getLikecount();
            // String view=BaseActivity.imageList.get(i).getView();
            String nowdate= BaseActivity.imageList.get(i).getNowdate();
            String area= BaseActivity.imageList.get(i).getArea();
            String phoneApp=BaseActivity.imageList.get(i).getPhoneApp();
            String phoneType=BaseActivity.imageList.get(i).getPhoneType();
            String time=BaseActivity.imageList.get(i).getTime();
            if(id.equals(myAlbumID)) {

                mData.add(new Model_Image(url,id,tip,likecount, nowdate,area, phoneApp,phoneType,time));

            }
        }
        //만약 아무것도 사진이 올린 적이없을 때만!! 초기사진을 데이터에 강제로 넣으줌
        if(mData.size()==0)
        {
                mData.add(new Model_Image(BaseActivity.imageList.get(0).getUrl(),BaseActivity.imageList.get(0).getId(),BaseActivity.imageList.get(0).getTip(),BaseActivity.imageList.get(0).getLikecount(),BaseActivity.imageList.get(0).getNowdate(),BaseActivity.imageList.get(0).getArea(),BaseActivity.imageList.get(0).getPhoneApp(),BaseActivity.imageList.get(0).getPhoneType(),BaseActivity.imageList.get(0).getTime()));
        }


        if(mData.size()!=0)
        {

        for(int i=0 ; i <mData.size();i++){
            for(int j=0 ; j< BaseActivity.testArr.size();j++)
            {
                if(mData.get(i).getUrl().equals(BaseActivity.testArr.get(j).getUrl())){
                    BestCount=BestCount+1;
                }
            }
        }

        String count=String.valueOf(mData.size());


        countView=(TextView)findViewById(R.id.count);
        countView.setText("Total  : " + count + "   Best  : " + BestCount );

        switcher=(TextSwitcher)findViewById(R.id.text);
        switcher.setFactory(new ViewSwitcher.ViewFactory() {

            @Override
            public View makeView() {
                LayoutInflater inflater = LayoutInflater.from(AlbumActivity.this);
                TextView textView=(TextView) inflater.inflate(R.layout.item_text, null);
                return textView;
            }
        });
        Animation in = AnimationUtils.loadAnimation(this, R.anim.slide_in_top);
        Animation out = AnimationUtils.loadAnimation(this, R.anim.slide_out_bottom);
        switcher.setInAnimation(in);
        switcher.setOutAnimation(out);



            mAdapter = new CoverFlowAdapter(getApplicationContext(), mData);
            mCoverFlow = (FeatureCoverFlow) findViewById(R.id.coverflow);
            mCoverFlow.setAdapter(mAdapter);

            mCoverFlow.setOnScrollPositionListener(new FeatureCoverFlow.OnScrollPositionListener() {
                @Override
                public void onScrolledToPosition(int position) {
                    switcher.setText(mData.get(position).getNowdate()+"    "+ mData.get(position).getLikecount()+" likes"  );
                }

                @Override
                public void onScrolling() {
                    switcher.setText("");
                }
            });

            navigation = (BottomNavigationView) findViewById(R.id.navigation);
            navigation.setSelectedItemId(R.id.navigation_album);
            BottomNavigationViewHelper.disableShiftMode(navigation);
            navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        }






      /*  mCoverFlow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), Best_ReplyActivity.class);
                intent.putExtra("email",mData.get(i).getId());
                intent.putExtra("image",mData.get(i).getUrl());
                intent.putExtra("tip",mData.get(i).getTip());
                intent.putExtra("phoneApp",mData.get(i).getPhoneApp());
                intent.putExtra("phoneType",mData.get(i).getPhoneType());
                setIntentFlag(intent);
                getApplicationContext().startActivity(intent);
            }
        });*/



    }

    @Override
    protected void onResume() {
        super.onResume();
            navigation.setSelectedItemId(R.id.navigation_album);
            loadData();


    }



    public void loadData()
    {
        RetrofitService retrofitService = RetrofitClient.retrofit.create(RetrofitService.class);
        Call<Best2VO> call = retrofitService.getBest2Data();
        call.enqueue(new Callback<Best2VO>() {
            @Override
            public void onResponse(Call<Best2VO> call, Response<Best2VO> response) {
                repo = response.body();
                tempList = repo.getList();

                for (int i = 0; i < tempList.size(); i++) {
                    for(int j=0;j<mData.size();j++) {
                        if (tempList.get(i).getUrl().equals(mData.get(j).getUrl())) {
                            //ex) 만약 area가 YS-1 이라면
                            String url = tempList.get(i).getUrl();
                            String likes = tempList.get(i).getLikes();
                            // mData2.add(new Model_Best2(url, likes));
                            mData.get(j).setLikecount(likes);
                        }
                    }
                }
            }
            @Override
            public void onFailure(Call<Best2VO> call, Throwable t) {

            }
        });



    }

    public void setIntentFlag(Intent intent){
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
    }

}
