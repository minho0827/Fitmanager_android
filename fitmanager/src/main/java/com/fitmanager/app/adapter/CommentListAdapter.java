package com.fitmanager.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.fitmanager.app.R;
import com.fitmanager.app.activity.CommentReplyActivity;
import com.fitmanager.app.model.CommentVO;
import com.fitmanager.app.util.Utils;

import java.util.HashMap;
import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class CommentListAdapter extends RecyclerView.Adapter<CommentListAdapter.ViewHolder> {

    Context mContext;
    List<CommentVO> mCommentItems;
    private int mTargetId;
    private int mTargetType;
    private final static String TAG = "CommentListAdapter";

    public CommentListAdapter(Context context, List<CommentVO> commentItems) {

        mContext = context;
        mCommentItems = commentItems;

    }

    public void setData(List<CommentVO> commentList, int targetId, int targetType) {
        this.mCommentItems = commentList;
        this.mTargetId = targetId;
        this.mTargetType = targetType;
        Log.i(TAG, "mCommentItems size : " + mCommentItems.size());
        notifyDataSetChanged();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_comment_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {

        viewHolder.tvContent.setText(mCommentItems.get(position).getContent());
        viewHolder.tvNickname.setText(mCommentItems.get(position).getNickname());
        viewHolder.tvDate.setText(Utils.getReplyDisplayTime(mCommentItems.get(position).getCreated()));
        viewHolder.commentCount.setText(mCommentItems.get(position).getCommentCount() + "");

        Glide.with(mContext)
                .load(mCommentItems.get(position).getProfileImg())
                .apply(new RequestOptions()
                        .placeholder(R.drawable.placeholder)
                        .bitmapTransform(new CropCircleTransformation(mContext))
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .centerCrop())
                .transition(withCrossFade())
                .into(viewHolder.imgProfile);

        viewHolder.imgComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                callActivity(mCommentItems.get(position).getCommentId(),
                        mCommentItems.get(position).getParentId(), mTargetId, mTargetType);
            }

        });
        viewHolder.imgbtnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "더보기 버튼 클릭", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void openOptionMenu(View v, final int position) {
        PopupMenu popup = new PopupMenu(v.getContext(), v);
        popup.getMenuInflater().inflate(R.menu.video_history_menu_items, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.delete:

                        HashMap map = new HashMap();
                        break;

                    case R.id.share:
                        Toast.makeText(mContext, "22222222", Toast.LENGTH_SHORT).show();

                        break;
                }
                Toast.makeText(mContext, "You selected the action : " + item.getTitle() + " position " + position, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        popup.show();
    }

    private void callActivity(int commentId, int parentId, int targetId, int targetType) {

        Intent intent = new Intent(mContext, CommentReplyActivity.class);
        Bundle extras = new Bundle();
        Log.i(TAG, "commentId : " + commentId);
        Log.i(TAG, "targetId :" + targetId);
        extras.putInt("commentId", commentId);
        extras.putInt("parentId", parentId);
        extras.putInt("targetId", + targetId);
        extras.putInt("targetType", + targetType);

        intent.putExtras(extras);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return mCommentItems.size();
    }

    public CommentVO getItemAt(int position) {
        return mCommentItems.get(position);
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProfile;
        TextView tvNickname;
        TextView tvContent;
        TextView tvDate;
        TextView commentCount;
        ImageButton imgComment;
        ImageButton imgbtnMore;
        FrameLayout frameLayoutMore;


        public ViewHolder(View view) {
            super(view);
            imgbtnMore = (ImageButton) view.findViewById(R.id.btn_more);
            frameLayoutMore = (FrameLayout) view.findViewById(R.id.frame_layout_more);
            imgProfile = (ImageView) view.findViewById(R.id.img_profile);
            imgComment = (ImageButton) view.findViewById(R.id.img_comment);
            commentCount = (TextView) view.findViewById(R.id.comment_count);
            tvNickname = (TextView) view.findViewById(R.id.tv_nickname);
            tvContent = (TextView) view.findViewById(R.id.tv_content);
            tvDate = (TextView) view.findViewById(R.id.tv_date);
        }

    }
}
