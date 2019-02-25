package com.fitmanager.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
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

public class ContentCommentAdapter extends RecyclerView.Adapter<ContentCommentAdapter.ViewHolder> {
    static final int TYPE_HEADER = 0;
    static final int TYPE_CELL = 1;
    List<CommentVO> mCommentReplyItems = null;
    final static String TAG = "ContentCommentAdapter";
    CommentVO mCommentVO = null;
    Context mContext;

    public ContentCommentAdapter(Context context, List<CommentVO> mCommentItems) {
        this.mContext = context;
        mCommentReplyItems = mCommentItems;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        if (viewType == TYPE_HEADER) {
            view = LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.comment_header_row, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate
                    (R.layout.recyclerview_comment_reply_row, parent, false);
        }

        return new ViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int postion) {
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
                final CommentVO commentVO = getItem(postion);
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

    private CommentVO getItem(int postion) {
        if (postion < 0) {
            return null;

        } else if (getItemViewType(postion) == TYPE_HEADER) {
            return null;
        } else {
            if (mCommentReplyItems.size() <= postion - 1) {
                return null;
            }
            return mCommentReplyItems.get(postion - 1);
        }

    }

    @Override
    public int getItemCount() {
        return mCommentReplyItems.size() + 1;
    }

    public void setFirstCommentData(CommentVO commentVO) {
        if (commentVO != null) {
            this.mCommentVO = commentVO;
            notifyDataSetChanged();
        }
    }

    public void setData(List<CommentVO> commentList) {
        mCommentReplyItems = commentList;
        notifyDataSetChanged();


    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        } else {
            return TYPE_CELL;
        }
    }

    public CommentVO getIteamAt(int position) {
        return mCommentReplyItems.get(position - 1);

    }


    public class ViewHolder extends RecyclerView.ViewHolder {
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
                tvCellNickname = (TextView) itemView.findViewById(R.id.tv_nickname);
                tvCellContent = (TextView) itemView.findViewById(R.id.tv_content);
                tvCellDate = (TextView) itemView.findViewById(R.id.tv_date);
                imgCellProfile = (ImageView) itemView.findViewById(R.id.img_profile);
                tvCellCommentCount = (TextView) itemView.findViewById(R.id.comment_count);

            } else if (viewType == TYPE_HEADER) {
                isHeader = true;
                tvHeaderProfileImg = (ImageView) itemView.findViewById(R.id.img_profile);
                mTvHeaderDate = (TextView) itemView.findViewById(R.id.tv_date);
                tvHeaderContent = (TextView) itemView.findViewById(R.id.tv_content);
                tvHeaderNickname = (TextView) itemView.findViewById(R.id.tv_nickname);
                tvHeaderProfileImg = (ImageView) itemView.findViewById(R.id.img_profile);
                tvHeaderCmtCount = (TextView) itemView.findViewById(R.id.comment_count);
            }
        }
    }
}
