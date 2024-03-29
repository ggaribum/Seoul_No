package onetwopunch.seoulinsangshot.com.seoulinsangshot.View;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import onetwopunch.seoulinsangshot.com.seoulinsangshot.Controller.Adapter_Comment2;
import onetwopunch.seoulinsangshot.com.seoulinsangshot.Controller.Constants;
import onetwopunch.seoulinsangshot.com.seoulinsangshot.DataManager.Data.Comment2VO;
import onetwopunch.seoulinsangshot.com.seoulinsangshot.DataManager.Data.CommentVO;
import onetwopunch.seoulinsangshot.com.seoulinsangshot.DataManager.Data.LikeCountVO;
import onetwopunch.seoulinsangshot.com.seoulinsangshot.DataManager.Remote.RetrofitClient;
import onetwopunch.seoulinsangshot.com.seoulinsangshot.DataManager.Remote.RetrofitService;
import onetwopunch.seoulinsangshot.com.seoulinsangshot.Model.Model_Comment2;
import onetwopunch.seoulinsangshot.com.seoulinsangshot.Model.Model_LikeCount;
import onetwopunch.seoulinsangshot.com.seoulinsangshot.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Best_ReplyActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView likeIv;
    ImageView dislikeIv;
    TextView likeTv;
    Animation open, close;
    Animation fab_open,fab_close;

    RecyclerView rv_comment;
    LinearLayoutManager manager;
    Adapter_Comment2 adapter_comment;
    EditText commentET;
    FloatingActionButton commentFAB;
    ImageView commentCheck;

    Comment2VO repoList;
    LikeCountVO likerepo;

    ArrayList<Model_Comment2> tempList;
    ArrayList<Model_Comment2> commentList;
    ArrayList<Model_LikeCount> likeTempList;
    public static ArrayList<Model_LikeCount> likeList;

    String url;
    boolean isFabOpen=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_best__reply);
//        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#050518")));

        Drawable d = getResources().getDrawable(R.drawable.actionbar);
        getSupportActionBar().setBackgroundDrawable(d);
        Intent intent = getIntent();


        likeIv = (ImageView)findViewById(R.id.like);
        dislikeIv = (ImageView)findViewById(R.id.dislike);
        likeTv=(TextView)findViewById(R.id.best_reply_likeText);
        open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);

        TextView best_email=(TextView)findViewById(R.id.txt_best_name);
        TextView best_tip=(TextView)findViewById(R.id.txt_best_tip);
        TextView best_phone=(TextView)findViewById(R.id.txt_best_theme);
        TextView best_app=(TextView)findViewById(R.id.txt_best_theme2);
        ImageView best_img=(ImageView)findViewById(R.id.img_best_cover);

        rv_comment=(RecyclerView)findViewById(R.id.rv_reply);
        commentET =(EditText) findViewById(R.id.best_reply_commentET);
        commentFAB=(FloatingActionButton)findViewById(R.id.best_reply_commentFAB);
        commentCheck=(ImageView)findViewById(R.id.best_reply_commentBT);

        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_close);

        //안드로이드 네트워킹 정의부분

        AndroidNetworking.initialize(getApplicationContext());
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //리사이클러뷰를 위한 정의부분
        manager = new LinearLayoutManager(getApplicationContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        //FAB 온클릭리스너달기
        commentFAB.setOnClickListener(this);
        commentCheck.setOnClickListener(this);



        url=intent.getStringExtra("url");  //이 값으로 댓글 확인
        Picasso.with(this).load(intent.getStringExtra("image")).into(best_img);
        best_email.setText(intent.getStringExtra("email"));
        best_tip.setText(intent.getStringExtra("tip"));
        best_phone.setText(intent.getStringExtra("phoneType"));
        best_app.setText(intent.getStringExtra("phoneApp"));

        dislikeIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dislikeIv.startAnimation(close);
                //dislikeIv.setVisibility(View.INVISIBLE);
                dislikeIv.setClickable(false);
                likeIv.setClickable(true);
                likeIv.startAnimation(open);
                likeIv.setVisibility(View.VISIBLE);
                setLikeData(url,"joker1649");

                Log.v("좋아요", "누르기");
            }
        });
        likeIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                likeIv.startAnimation(close);
                likeIv.setClickable(false);
                dislikeIv.setClickable(true);
                dislikeIv.startAnimation(open);
                dislikeIv.setVisibility(View.VISIBLE);
                setDislikeData(url,"joker1649");
                Log.v("좋아요", "취소");
            }
        });
        loadLikeData(url);  ///좋아요 로드.


        //레트로핏
        Retrofit client = new Retrofit.Builder().baseUrl(Constants.TEST_BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        RetrofitService service = client.create(RetrofitService.class);

        Call<Comment2VO> call = service.getComment2Data();
        call.enqueue(new Callback<Comment2VO>() {
            @Override
            public void onResponse(Call<Comment2VO> call, Response<Comment2VO> response) {
                if (response.isSuccessful()) {
                    repoList = response.body();
                    tempList = repoList.getList();
                    commentList=new ArrayList<Model_Comment2>();
                    for (int i = 0; i < tempList.size(); i++) {
                        if (tempList.get(i).getUrl().equals(url)) {
                            String thisUrl = tempList.get(i).getUrl();
                            String id = tempList.get(i).getId();
                            String text = tempList.get(i).getText();
                            String date = tempList.get(i).getDate();
                            commentList.add(new Model_Comment2(id,thisUrl,text,date));
                        }
                    }
                    adapter_comment = new Adapter_Comment2(getApplicationContext(), commentList);
                    rv_comment.setLayoutManager(manager);
                    rv_comment.setAdapter(adapter_comment);
                }
            }

            @Override
            public void onFailure(Call<Comment2VO> call, Throwable t) {
            }
        });
        //OnCreate 종료
    }

    //좋아요 취소 메서드(포스트함수)
    public void setDislikeData(String getUrl,String getId)
    {
        final String url=getUrl;
        String id=getId;

        AndroidNetworking.post("http://13.124.87.34:3000/dellike")
                .addBodyParameter("url", url)
                .addBodyParameter("id", id)
                .addHeaders("Content-Type", "multipart/form-data")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        loadLikeData(url);
                    }

                    @Override
                    public void onError(ANError anError) {
                    }
                });

    }

    //좋아요 누르기 메서드(포스트함수)
    public void setLikeData(String getUrl,String getId)
    {
        final String url=getUrl;
        String id=getId;

        AndroidNetworking.post("http://13.124.87.34:3000/plike")
                .addBodyParameter("url", url)
                .addBodyParameter("id", id)
                .addHeaders("Content-Type", "multipart/form-data")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        loadLikeData(url);
                    }

                    @Override
                    public void onError(ANError anError) {
                    }
                });
    }

    //좋아요 로드 메서드
    public void loadLikeData(final String getUrl) {
        likeList=new ArrayList<Model_LikeCount>();
        RetrofitService retrofitService = RetrofitClient.retrofit.create(RetrofitService.class);
        Call<LikeCountVO> call = retrofitService.getLikeData();
        call.enqueue(new Callback<LikeCountVO>() {
            @Override
            public void onResponse(Call<LikeCountVO> call, Response<LikeCountVO> response) {
                likerepo = response.body();
                likeTempList = likerepo.getList();
                for (int i = 0; i < likeTempList.size(); i++) {
                    if (likeTempList.get(i).getUrl().equals(getUrl)) {
                        String url = likeTempList.get(i).getUrl();
                        String id = likeTempList.get(i).getId();
                        if(likeTempList.get(i).getId().equals("joker1649")) //★☆★☆★☆로그인 정보를 파라미터로!!!!!!!//★☆★☆★☆//★☆★☆★☆
                        {
                            // dislikeIv.setImageResource(R.drawable.likeicon);
                            dislikeIv.setVisibility(View.INVISIBLE);
                            dislikeIv.setClickable(false);
                            likeIv.setVisibility(View.VISIBLE);
                            likeIv.setClickable(true);
                        }
                        likeList.add(new Model_LikeCount(url, id)); //★☆★☆★☆여기서 생성자에 보낼 변수(like count)추가하기
                    }
                }

                if(likeList.size()==0) {
                    likeTv.setText(likeList.size()+" Likes");
                }
                for(int i=0; i<likeList.size();i++)
                {
                    if(likeList.get(i).getUrl().equals(getUrl))
                    {
                        likeTv.setText(likeList.size()+" Likes");//★☆★☆★☆lkeecount값 출력
                    }
                }

            }

            @Override
            public void onFailure(Call<LikeCountVO> call, Throwable t) {

            }
        });
    }
    /////////////이까지 좋아요 /////////////////




    //POST 메서드
    private void postMessage(String message,String getUrl,String getDate)
    {
        final String text= message;
        final String url =getUrl;
        final String date=getDate;
        //id 부분은 넘겨온 값을 이용할 예정.
        final String id="joker1649";

        AndroidNetworking.post("http://13.124.87.34:3000/pviewreply")
                .addBodyParameter("id",id)
                .addBodyParameter("url",url)
                .addBodyParameter("text",text)
                .addBodyParameter("date",date)
                .addHeaders("Content-Type", "multipart/form-data")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        commentList.add(new Model_Comment2(id,url,text,date));
                        adapter_comment = new Adapter_Comment2(getApplicationContext(), commentList);
                        rv_comment.setLayoutManager(manager);
                        rv_comment.setAdapter(adapter_comment);
                    }
                    @Override
                    public void onError(ANError anError) {
                    }
                });
    }

    //다이얼로그 띄우는 메서드
    //setPostiveButton이 "No"로 되어있음 위치때문에 순서 바꿨다.
    private void DialogSimple(final String message , final String url){
        AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
        alt_bld.setMessage("메세지를 전송 하시겠습니까 ? ").setCancelable(
                true).setPositiveButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();

                    }
                }).setNegativeButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String getMessage=message;
                        String geturl=url;

                        //현재날짜 구하기
                        long now = System.currentTimeMillis();
                        java.util.Date date = new Date(now);
                        SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
                        //ex ) 2017.10.02 15:30:23
                        String nowTime=sdfNow.format(date);

                        //메세지 포스팅 함수 호출("여기어때요","YS-1","2017.10.02 15:30:23")
                        postMessage(getMessage,geturl,nowTime);

                    }
                });
        AlertDialog alert = alt_bld.create();
        // 다이얼로그 타이틀 정의
        alert.setTitle("Message");
        // 다이얼로그 아이콘 정의
        alert.setIcon(R.drawable.onetwopunch);
        alert.show();
    }

    ////////////코맨트 부분 ///////////////////
    //코맨트 메서드
    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.best_reply_commentFAB:
                animateFAB();
                break;
            case R.id.best_reply_commentBT:

                DialogSimple(commentET.getText().toString(), url);
                animateFAB();
                break;
        }
    }

    public void animateFAB(){
        if(isFabOpen){

            commentCheck.startAnimation(fab_close);
            commentCheck.setClickable(false);

            commentFAB.startAnimation(fab_open);
            commentFAB.setClickable(true);

            commentET.setVisibility(View.INVISIBLE);
            commentET.setText(null);
            InputMethodManager immhide = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            immhide.hideSoftInputFromWindow(commentET.getWindowToken(),0);
            isFabOpen = false;

        } else {

            commentFAB.startAnimation(fab_close);

            commentCheck.setVisibility(View.VISIBLE);
            commentCheck.startAnimation(fab_open);
            commentCheck.setClickable(true);

            commentFAB.setClickable(false);

            isFabOpen = true;

            commentET.setVisibility(View.VISIBLE);
            commentET.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        }
    }
}
