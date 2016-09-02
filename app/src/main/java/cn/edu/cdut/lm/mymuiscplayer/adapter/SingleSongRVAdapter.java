package cn.edu.cdut.lm.mymuiscplayer.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import cn.edu.cdut.lm.mymuiscplayer.R;
import cn.edu.cdut.lm.mymuiscplayer.innerfragment.MoreInfoSingleSongFragment;
import cn.edu.cdut.lm.mymuiscplayer.module.Mp3Info;
import cn.edu.cdut.lm.mymuiscplayer.service.PlayerService;

/**
 * Created by LimiaoMaster on 2016/8/24 18:37
 */
public class SingleSongRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    public static final String UPDATE_UI_ON_LIST_CLICK = "cn.edu.cdut.lm.mymusicplayer.UPDATE_UI_ON_LIST_CLICK";

    private  FragmentActivity fragmentActivity;
    private  Context context;
    private  List<Mp3Info> list;
    private final static int FIRST_LINE = 0;
    private final static int GENERAL_LINES=1;
    private final static int LAST_LINE = 2;

    public SingleSongRVAdapter(FragmentActivity activity, Context context, List<Mp3Info> list) {
        this.context = context;
        this.list = list;
        fragmentActivity = activity;
    }

    /**
     * 根据要渲染行的position 产生类型。
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        if(position == 0 ) return FIRST_LINE;
        else if (position == list.size()+1) return LAST_LINE;
        else return GENERAL_LINES;
    }

    /**
     * 根据行的类型，产生ViewHolder。
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d("SingleSongRVAdapter()","onCreateViewHolder()方法得到执行!  ");
        View viewOfFirstLine = LayoutInflater.from(context).inflate(R.layout.item_localmusic_singlesong_firstline,parent,false);
        View viewOfGeneralLines = LayoutInflater.from(context).inflate(R.layout.item_localmusic_singlesong_generallines,parent,false);
        View viewOfLastLine = LayoutInflater.from(context).inflate(R.layout.item_localmusic_lastline_empty,parent,false);
        if(viewType == FIRST_LINE) return new FirstLineViewHolder(viewOfFirstLine);
        else if (viewType == GENERAL_LINES) return new GeneralLinesViewHolder(viewOfGeneralLines);
        else if (viewType == LAST_LINE) return new LastLinesViewHolder(viewOfLastLine);
        else return null;
    }

    /**
     * 用ViewHolder配置要显示的内容。
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        Mp3Info mp3Info = null;
        if (holder instanceof FirstLineViewHolder) {
            ((FirstLineViewHolder) holder).textView.setText("(共" + list.size() + "首)");
        }
        if (position >= 1 && position <= list.size()) {
            mp3Info = list.get(position - 1);
            ((GeneralLinesViewHolder) holder).title.setText(mp3Info.getTitle());
            ((GeneralLinesViewHolder) holder).artist.setText(mp3Info.getArtist());
            ((GeneralLinesViewHolder) holder).album.setText(mp3Info.getAlbum());

            ((GeneralLinesViewHolder) holder).view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText( context,"您点击了第："+(position-1)+"行",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.putExtra("position", position-1);
                    intent.setClass(context, PlayerService.class);
                    context.startService(intent);
                    Log.e("onItemClick","启动了PlayerService播放服务！");

                    Intent broadCastIntent = new Intent();
                    broadCastIntent.setAction(UPDATE_UI_ON_LIST_CLICK);
                    broadCastIntent.putExtra("position",position-1);
                    context.sendBroadcast(broadCastIntent);
                    Log.e("onItemClick","发送了UPDATE_UI的广播！");

                    /*((LastLinesViewHolder) holder).speaker.setVisibility(View.VISIBLE);*/
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (list.size() == 0 ) return 1;
        else return list.size()+2;
    }

    private class FirstLineViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textView ;
        ImageView imageView;
        public FirstLineViewHolder(View viewOfFist) {
            super(viewOfFist);
            textView = (TextView) viewOfFist.findViewById(R.id.number_of_music);
            imageView = (ImageView) viewOfFist.findViewById(R.id.multi_pick_to_do_someting);
            viewOfFist.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }
    }

    private class GeneralLinesViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView artist;
        TextView album;
        ImageView more;
        ImageView speaker;
        View view;
        public GeneralLinesViewHolder(View viewOfGeneralLines) {
            super(viewOfGeneralLines);
            view = viewOfGeneralLines;
            title = (TextView) viewOfGeneralLines.findViewById(R.id.title_localmusic);
            artist = (TextView) viewOfGeneralLines.findViewById(R.id.artist_localmusic);
            album = (TextView) viewOfGeneralLines.findViewById(R.id.album_localmusic);
            more = (ImageView) viewOfGeneralLines.findViewById(R.id.iv_more_localmusic);
            speaker = (ImageView) viewOfGeneralLines.findViewById(R.id.speaker);

            more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("LastLinesViewHolder()",list.get(getAdapterPosition()-1)+"");
                    MoreInfoSingleSongFragment moreInformationFragment = MoreInfoSingleSongFragment.newInstance(list.get(getAdapterPosition()-1),0);

                    moreInformationFragment.show(fragmentActivity.getSupportFragmentManager(),"music");
                }
            });
        }
    }

    private class LastLinesViewHolder extends RecyclerView.ViewHolder {
        public LastLinesViewHolder(View viewOfGeneralLines) {
            super(viewOfGeneralLines);
        }
    }
}
