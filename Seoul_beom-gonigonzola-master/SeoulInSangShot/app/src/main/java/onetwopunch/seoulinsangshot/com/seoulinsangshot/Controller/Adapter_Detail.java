package onetwopunch.seoulinsangshot.com.seoulinsangshot.Controller;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.github.oliveiradev.image_zoom.lib.widget.ImageZoom;
import com.squareup.picasso.Picasso;

import java.util.List;

import onetwopunch.seoulinsangshot.com.seoulinsangshot.Model.Model_Detail;
import onetwopunch.seoulinsangshot.com.seoulinsangshot.R;

/**
 * Created by kwakgee on 2017. 9. 17..
 */

public class Adapter_Detail extends RecyclerView.Adapter<Adapter_Detail.ViewHolder>{

    List<Model_Detail> tempArr;
    Context adapterContext;
    public Adapter_Detail(Context context , List<Model_Detail> tempArr) {
        this.adapterContext=context;
        this.tempArr = tempArr;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_detail, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Picasso.with(adapterContext).load(tempArr.get(position).getUrl()).into(holder.iv_detail_main);
        /* holder.iv_detail_main.setImageBitmap(); */ // handling...

    }

    @Override
    public int getItemCount() {
        return tempArr.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        //사진확대 기능 코드
        public ImageZoom iv_detail_main;

        public ViewHolder(View itemView){
            super(itemView);

            //사진확대 기능 코드
            iv_detail_main = (ImageZoom) itemView.findViewById(R.id.iv_detail_main);

        }


    }

}
