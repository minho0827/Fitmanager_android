package com.fitmanager.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fitmanager.app.R;
import com.fitmanager.app.model.CommentVO;
import com.fitmanager.app.util.ImageUtils;
import com.fitmanager.app.util.LoginUtils;
import com.fitmanager.app.util.Utils;

import java.util.List;

public class CommentReplyAdapter extends RecyclerView.Adapter<CommentReplyAdapter.ViewHolder> {
    Context mContext;
    List<CommentVO> mCommentReplyItems = null;
    CommentVO mCommentVO = null;
    final static String TAG = "CommentReplyAdapter";
    static final int TYPE_HEADER = 0;
    static final int TYPE_CELL = 1;

    public CommentReplyAdapter(Context context, List<CommentVO> mCommentItems) {
        mContext = context;
        mCommentReplyItems = mCommentItems;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        if (viewType == TYPE_HEADER) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.comment_header_row, parent, false);

        } else {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recyclerview_comment_reply_row, parent, false);

        }
        return new ViewHolder(view, viewType);
    }


    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        if (viewHolder.isHeader) {
            if (mCommentVO != null) {
                viewHolder.mTvHeaderDate.setText(Utils.getReplyDisplayTime(mCommentVO.getCreated()));
                viewHolder.tvHeaderContent.setText(mCommentVO.getContent());
                viewHolder.tvHeaderNickname.setText(mCommentVO.getNickname());
                viewHolder.tvHeaderCmtCount.setText(mCommentReplyItems.size() + "");
                ImageUtils.getProfileImage(mContext,
                        viewHolder.tvHeaderProfileImg,
                        LoginUtils.getLoginUserVO().getProfileImg());
            }


        } else {
            if (viewHolder.isCell) {
                final CommentVO commentVO = getItem(position);
                if (commentVO != null) {
                    viewHolder.tvCellDate.setText(Utils.getReplyDisplayTime(commentVO.getCreated()));
                    viewHolder.tvCellContent.setText(commentVO.getContent());
                    viewHolder.tvCellNickname.setText(commentVO.getNickname());
                    ImageUtils.getProfileImage(mContext,
                            viewHolder.imgCellProfile,
                            LoginUtils.getLoginUserVO().getProfileImg()
                    );

                }
            }

        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        } else {
            return TYPE_CELL;
        }
    }

    private CommentVO getItem(int position) {
        if (position < 0) {
            return null;
        } else if (getItemViewType(position) == TYPE_HEADER) {
            return null;
        } else {
            if (mCommentReplyItems.size() <= position - 1) {
                return null;
            }
            return mCommentReplyItems.get(position - 1);
        }
    }

    @Override
    public int getItemCount() {
        return mCommentReplyItems.size() + 1;

    }

    public void setData(List<CommentVO> commentList) {
        this.mCommentReplyItems = commentList;
        Log.i(TAG, "mCommentItems size : " + mCommentReplyItems.size());
        notifyDataSetChanged();
    }

    public void setFirstCommentData(CommentVO commentVO) {
        if (commentVO != null) {
            this.mCommentVO = commentVO;
            notifyDataSetChanged();
        }
        Log.i(TAG, "commentVO getCreated :" + commentVO.getCreated());

    }

    public CommentVO getIteamAt(int position) {
        return mCommentReplyItems.get(position - 1);

    }



    class ViewHolder extends RecyclerView.ViewHolder {
        //cell
        ImageView imgCellProfile;
        TextView tvCellNickname;
        TextView tvCellContent;
        TextView tvCellDate;
        TextView tvCellCommentCount;
        boolean isCell;

        //header
        TextView mTvHeaderDate;
        TextView tvHeaderContent;
        TextView tvHeaderNickname;
        ImageView tvHeaderProfileImg;
        TextView tvHeaderCmtCount;
        boolean isHeader;


        public ViewHolder(View itemView, int viewType) {
            super(itemView);
            if (viewType == TYPE_CELL) {
                isCell = true;
                tvCellNickname = itemView.findViewById(R.id.tv_nickname);
                tvCellContent = itemView.findViewById(R.id.tv_content);
                tvCellDate = itemView.findViewById(R.id.tv_date);
                imgCellProfile = itemView.findViewById(R.id.img_profile);
                tvCellCommentCount = itemView.findViewById(R.id.comment_count);

            } else if (viewType == TYPE_HEADER) {
                isHeader = true;
                tvHeaderProfileImg = itemView.findViewById(R.id.img_profile);
                mTvHeaderDate = itemView.findViewById(R.id.tv_date);
                tvHeaderContent = itemView.findViewById(R.id.tv_content);
                tvHeaderNickname = itemView.findViewById(R.id.tv_nickname);
                tvHeaderProfileImg = itemView.findViewById(R.id.img_profile);
                tvHeaderCmtCount = itemView.findViewById(R.id.comment_count);
            }
        }
    }
}
