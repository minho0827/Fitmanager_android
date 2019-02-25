package com.fitmanager.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fitmanager.app.R;
import com.fitmanager.app.activity.CommentContentItemActivity;
import com.fitmanager.app.model.CommentHistoryVO;

import java.util.List;


public class MyCommentListAdapater extends RecyclerView.Adapter<MyCommentListAdapater.ViewHolder> {
    Context mContext;
    List<CommentHistoryVO> mMyCommentHistoryList;
    private final static String TAG = "MyCommentListAdapater";


    public MyCommentListAdapater(Context context, List<CommentHistoryVO> mCommentsItems) {
        this.mContext = context;
        this.mMyCommentHistoryList = mCommentsItems;


    }

    public void setData(List<CommentHistoryVO> CommentList) {
        this.mMyCommentHistoryList = CommentList;
        notifyDataSetChanged();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_comment_item_row, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position) {
        final CommentHistoryVO commentHistoryVO = mMyCommentHistoryList.get(position);
        if (commentHistoryVO != null) {
//            viewHolder.tvContent.setText("\"  \"");
            viewHolder.tvContent.setText(commentHistoryVO.getContent());
//            viewHolder.tvName.setText(commentHistoryVO.getCoachName());
//            viewHolder.tvDate.setText(Utils.getReplyDisplayTime(commentHistoryVO.getCreated()));
//            Glide.with(mContext)
//                    .load(commentHistoryVO.getImage())
//                    .centerCrop()
//                    .crossFade()
//                    .into(viewHolder.image);
//            viewHolder.tvTitle.setText(commentHistoryVO.getTitle());
//            viewHolder.tvInfo.setText(getTargetTypeSetText(commentHistoryVO.getTargetType()));

        }
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                callActivity(commentHistoryVO.getCommentId(),
//                        commentHistoryVO.getTargetType(), commentHistoryVO.getTargetId());
            }
        });


    }

    private void callActivity(int commentId, int targetType, int targetid) {
        Log.i(TAG, "videoId: " + commentId);
        Intent intent = new Intent(mContext, CommentContentItemActivity.class);
        Bundle extras = new Bundle();
        extras.putInt("commentId", commentId);
        extras.putInt("targetType", targetType);
        extras.putInt("targetId", targetid);
        intent.putExtras(extras);
        mContext.startActivity(intent);

    }

    private String getTargetTypeSetText(int targetType) {
        String targetStr = "";
        switch (targetType) {
            case 2:
                targetStr = "님의 영상에 댓글을 남겼습니다.";
                break;
            case 3:
                targetStr = "님의 식단에 댓글을 남겼습니다.";
                break;
            default:
                break;
        }

        return targetStr;
    }


    @Override
    public int getItemCount() {
        return mMyCommentHistoryList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvDateMonth, tvTitle, tvDate, tvContent, tvInfo;
        ImageView image;


        public ViewHolder(View view) {
            super(view);
            image = (ImageView) view.findViewById(R.id.image);
            tvDate = (TextView) view.findViewById(R.id.tv_date);
            tvDateMonth = (TextView) view.findViewById(R.id.tv_date_bigsize);
            tvName = (TextView) view.findViewById(R.id.tv_name);
            tvContent = (TextView) view.findViewById(R.id.tv_content);
            tvTitle = (TextView) view.findViewById(R.id.tv_title);
            tvInfo = (TextView) view.findViewById(R.id.tv_info);

        }
    }
}
