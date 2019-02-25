package com.fitmanager.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fitmanager.app.R;
import com.fitmanager.app.model.NotiVO;
import com.fitmanager.app.util.Utils;

import java.util.List;


public class MyNoticeHistoryAdapter extends RecyclerView.Adapter<MyNoticeHistoryAdapter.ViewHolder> {
    Context mContext;
    List<NotiVO> mNotiListItems;
    private final static String TAG = "MyNoticeHistoryAdapter";

    public MyNoticeHistoryAdapter(Context context, List<NotiVO> notiListItems) {
        this.mContext = context;
        this.mNotiListItems = notiListItems;

    }


    public void setData(List<NotiVO> notiListItems) {
        this.mNotiListItems = notiListItems;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_noti_item_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        final NotiVO notiVO = mNotiListItems.get(position);

        if (notiVO.getNotiType().equals("reply_comment")) {
            viewHolder.linearComment.setVisibility(View.VISIBLE);
            viewHolder.linearNoti.setVisibility(View.GONE);
            viewHolder.tvContentTitle.setText(notiVO.getTitle());
            viewHolder.tvTitle.setText(notiVO.getTitle());
            viewHolder.tvDate.setText(Utils.getReplyDisplayTime(notiVO.getCreated()));

        } else if (notiVO.getNotiType().equals("comment")) {
            viewHolder.linearComment.setVisibility(View.VISIBLE);
            viewHolder.linearNoti.setVisibility(View.GONE);
            viewHolder.tvContentTitle.setText(notiVO.getTitle());
            viewHolder.tvTitle.setText(notiVO.getTitle());
            viewHolder.tvDate.setText(Utils.getReplyDisplayTime(notiVO.getCreated()));

        } else if (notiVO.getNotiType().equals("meal")) {
            viewHolder.linearComment.setVisibility(View.GONE);
            viewHolder.linearNoti.setVisibility(View.VISIBLE);
            viewHolder.tvName.setText("홍길동");
            viewHolder.tvInfo.setText("님이 새로운 식단을 게시하였습니다.");
            viewHolder.tvDate.setText(Utils.getReplyDisplayTime(notiVO.getCreated()));
            viewHolder.tvTitle.setText(notiVO.getTitle());
        } else if (notiVO.getNotiType().equals("video")) {
            viewHolder.linearComment.setVisibility(View.GONE);
            viewHolder.linearNoti.setVisibility(View.VISIBLE);
            viewHolder.tvName.setText("홍길동");
            viewHolder.tvInfo.setText("님이 새로운 동영상을 게시하였습니다.");
            viewHolder.tvDate.setText(Utils.getReplyDisplayTime(notiVO.getCreated()));
            viewHolder.tvTitle.setText(notiVO.getTitle());
        }

//        viewHolder.itemView.setOnClickListener(null);
//        if (Constant.TargetType.MEAL.value == notiVO.getTargetType()) {
//            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    callMealXXX(notiVO.getTargetId());
//                }
//            });
//        } else if (Constant.TargetType.MEAL.value == notiVO.getTargetType()) {
//            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
////                    callCoachActivity(notiVO.target(),notiVO.getTargetId());
//                }
//            });
//        }

    }
//
//    public void callCoachActivity(int targetType, int targetId) {
//        Log.i(TAG, "targetId: " + targetId);
//        Log.i(TAG, "targetType: " + targetType);
//
//        if (targetType )
//        Intent intent = new Intent(mContext, VideoActivity.class);
//        Bundle extras = new Bundle();
//        extras.putInt("videoId", videoId);
//        intent.putExtras(extras);
//        mContext.startActivity(intent);
//    }

    @Override
    public int getItemCount() {
        return mNotiListItems.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        //noti layout
        TextView tvName, tvTitle, tvDate, tvInfo, tvContentTitle;
        ImageView image, prifile_img;
        LinearLayout linearComment;
        LinearLayout linearNoti;


        public ViewHolder(View view) {
            super(view);

            linearComment = view.findViewById(R.id.linear_comment);
            linearNoti = view.findViewById(R.id.linear_noti);
            image = view.findViewById(R.id.image);
            prifile_img = view.findViewById(R.id.prifile_img);
            tvDate = view.findViewById(R.id.tv_date);
            tvContentTitle = view.findViewById(R.id.tv_content_title);
            tvName =  view.findViewById(R.id.tv_name);
            tvInfo =  view.findViewById(R.id.tv_info);
            tvTitle = view.findViewById(R.id.tv_title);
        }
    }
}
