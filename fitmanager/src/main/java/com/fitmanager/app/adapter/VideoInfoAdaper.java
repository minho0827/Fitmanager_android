package com.fitmanager.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fitmanager.app.R;
import com.fitmanager.app.activity.CommentActivity;
import com.fitmanager.app.activity.VideoActivity;
import com.fitmanager.app.model.VideoVO;
import com.fitmanager.app.util.Constant;
import com.fitmanager.app.util.ImageUtils;
import com.fitmanager.app.util.LoginUtils;
import com.fitmanager.app.util.Utils;

import java.util.ArrayList;
import java.util.List;

public class VideoInfoAdaper extends RecyclerView.Adapter<VideoInfoAdaper.ViewHolder> {
    Context mContext;
    private final static String TAG = "VideoInfoAdaper";
    VideoVO mVideoHeaderData = null;
    List<VideoVO> mCellDataList = new ArrayList<>();
    static final int TYPE_HEADER = 0;
    static final int TYPE_CELL = 1;
    static int TYPE_FOOTER = 2;
    private int mVideoId;
    private boolean mVideoIsBookmarked = false;
    private boolean mCoachIsBookmarked = false;
    BookmarkClickListener mCallback;


    public VideoInfoAdaper(Context context, VideoVO mVideoVO, int videoId) {
        this.mContext = context;
        this.mVideoHeaderData = mVideoVO;
        this.mVideoId = videoId;
        mCallback = (BookmarkClickListener) context;
        Log.i(TAG, "videoId : " + mVideoId);

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        if (viewType == TYPE_HEADER) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.videoinfo_item_header, parent, false);

        } else if (viewType == TYPE_FOOTER) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_videoinfo_footer, parent, false);

        } else {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_videoinfo_row, parent, false);
        }
        return new ViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position) {

        if (viewHolder.isHeader) {
            if (mVideoHeaderData != null) {
                if (LoginUtils.isLoggedIn() && mVideoIsBookmarked) {
                    // 로그인 되어 있으며, 북마크 했을경우.
                    viewHolder.btnVideoBookmark.setBackgroundResource(R.drawable.bookmark_purple);
                } else {
                    viewHolder.btnVideoBookmark.setBackgroundResource(R.drawable.bookmark_star);
                }
                if (LoginUtils.isLoggedIn() && mCoachIsBookmarked) {
                    // 로그인 되어 있으며, 북마크 했을경우.
                    Log.i(TAG, "onBindViewHolder mCoachIsBookmarked if :" + mCoachIsBookmarked);
                    viewHolder.tvCoachBookmark.setVisibility(View.VISIBLE);
                    viewHolder.check.setVisibility(View.VISIBLE);
                    viewHolder.tvNotCoachBookmark.setVisibility(View.GONE);
                } else {
                    Log.i(TAG, "onBindViewHolder mCoachIsBookmarked else:" + mCoachIsBookmarked);
                    viewHolder.tvCoachBookmark.setVisibility(View.GONE);
                    viewHolder.check.setVisibility(View.GONE);
                    viewHolder.tvNotCoachBookmark.setVisibility(View.VISIBLE);
                }
//                Glide.with(mContext)
//                        .load(mVideoHeaderData.getProfileImg())
//                        .centerCrop()
//                        .bitmapTransform(new CropCircleTransformation(mContext))
//                        .crossFade()
//                        .into(viewHolder.profileImg);
//
                ImageUtils.getProfileImage(mContext, viewHolder.profileImg,
                        mVideoHeaderData.getProfileImg()
                );

//
//
//                ImageUtils.getProfileImage(mContext,
//                        viewHolder.imgProfile,
//                        LoginUtils.getLoginUserVO().getMemberId());

                viewHolder.tvExercisetype.setText(Utils.getExerciseType
                        (mVideoHeaderData.getExerciseType()));
                viewHolder.tvBodytype01.setText(Utils.getBodyType(mVideoHeaderData.getBodypart01()));
                viewHolder.name.setText(mVideoHeaderData.getName());
                viewHolder.tvBodytype01.setBackgroundResource
                        (Utils.getBodyByColorType(mVideoHeaderData.getBodypart01()));
                viewHolder.tvBodytype02.setText(Utils.getBodyType(mVideoHeaderData.getBodypart02()));
                viewHolder.tvBodytype02.setBackgroundResource
                        (Utils.getBodyByColorType(mVideoHeaderData.getBodypart02()));
                viewHolder.tvTitle.setText(mVideoHeaderData.getTitle());
                viewHolder.tvContent.setText(mVideoHeaderData.getContent());
                viewHolder.tvHit.setText(mVideoHeaderData.getViewHistoryCount() + "");
                viewHolder.videoBookmarkCount.setText(mVideoHeaderData.getBookmarkCount() + "");
                viewHolder.coachBookmarkCount.setText("구독" + mVideoHeaderData.getCoachBookmarkCount() + "명");
                viewHolder.tvCommentCount.setText(mVideoHeaderData.getCommentCount() + "");
                getExerciseLevelDrawble(mVideoHeaderData.getLevel(), viewHolder);

                viewHolder.linearComment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, CommentActivity.class);
                        Bundle extras = new Bundle();
                        extras.putInt("targetId", mVideoId);
                        extras.putInt("targetType", Constant.TargetType.VIDEO.value);
                        intent.putExtras(extras);
                        mContext.startActivity(intent);
                    }
                });

            }
        } else if (viewHolder.isCell) {
            final VideoVO videoVO = getItem(position);
            if (videoVO != null) {
                if (videoVO.getCellType() == 0) {   // 0: normal
                    viewHolder.cellLevel.setText(Utils.getLevelType(videoVO.getLevel()));
                    viewHolder.cellExerciseType.setText(Utils.getExerciseType(videoVO.getExerciseType()));
                    viewHolder.cellExerciseType.setBackgroundResource
                            (Utils.getExerciseByColorType(videoVO.getExerciseType()));
                    viewHolder.cellBodyPart01.setText
                            (Utils.getBodyType(videoVO.getBodypart01()));
                    viewHolder.cellBodyPart01.setBackgroundResource
                            (Utils.getBodyByColorType(videoVO.getBodypart01()));
                    viewHolder.cellBodyPart02.setText
                            (Utils.getBodyType(videoVO.getBodypart02()));
                    viewHolder.cellBodyPart02.setBackgroundResource
                            (Utils.getBodyByColorType(videoVO.getBodypart02()));
                    viewHolder.cellTitle.setText(videoVO.getTitle());

                    viewHolder.cardView.setVisibility(View.VISIBLE);
                    viewHolder.layoutNone.setVisibility(View.GONE);
//                viewHolder.btnBookmark.setChecked(videoVO.);
                } else {   // 1: empty
//                    viewHolder.layoutRow.setVisibility(View.GONE);
                    viewHolder.cardView.setVisibility(View.GONE);
                    viewHolder.layoutNone.setVisibility(View.VISIBLE);
                }

                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        ((VideoActivity) mContext).callVideoActivity(videoVO.getVideoId());
                        Log.i(TAG, "onBindViewHolder > getVideoId : " + videoVO.getVideoId());
                    }
                });
            }
        }
    }


    private void getExerciseLevelDrawble(int level, VideoInfoAdaper.ViewHolder viewHolder) {
        switch (level) {
            case 1:
                viewHolder.imgLevel01.setVisibility(View.VISIBLE);
                viewHolder.imgLevel02.setVisibility(View.INVISIBLE);
                viewHolder.imgLevel03.setVisibility(View.INVISIBLE);
                break;
            case 2:
                viewHolder.imgLevel01.setVisibility(View.VISIBLE);
                viewHolder.imgLevel02.setVisibility(View.VISIBLE);
                viewHolder.imgLevel03.setVisibility(View.INVISIBLE);
                break;
            case 3:
                viewHolder.imgLevel01.setVisibility(View.VISIBLE);
                viewHolder.imgLevel02.setVisibility(View.VISIBLE);
                viewHolder.imgLevel03.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }

    }


    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        } else if (position == mCellDataList.size() + 1) {
            return TYPE_FOOTER;
        } else {
            return TYPE_CELL;
        }
    }

    private VideoVO getItem(int position) {
        if (position < 0) {
            return null;
        } else if (getItemViewType(position) == TYPE_HEADER
                || getItemViewType(position) == TYPE_FOOTER) {
            return null;
        } else {
            if (mCellDataList.size() <= position - 1) {
                return null;
            }
            return mCellDataList.get(position - 1);
        }
    }

    @Override
    public int getItemCount() {
        return mCellDataList.size() + 2;
    }


    public void setCellData(List<VideoVO> cellDataList) {
        if (cellDataList != null) {
            this.mCellDataList = cellDataList;
            Log.i(TAG, "mCellDataList size : " + cellDataList.size());
            notifyDataSetChanged();
        }

    }

    public void setHeaderData(VideoVO mVideoVO) {

        if (mVideoVO != null) {
            mVideoHeaderData = mVideoVO;
            notifyDataSetChanged();
        }
        Log.i(TAG, "mVideoHeaderData getCreated :" + mVideoVO.getCreated());
    }

    public void updateBookmarkCount(int bookmarkCount) {
        getHeaderData().setBookmarkCount(bookmarkCount);
        notifyDataSetChanged();
    }

    public void updateCoachBookmarkCount(int bookmarkCount) {
        getHeaderData().setCoachBookmarkCount(bookmarkCount);
        notifyDataSetChanged();
    }

    public VideoVO getHeaderData() {
        return mVideoHeaderData;
    }

    public void setVideoBookmarkResultData(boolean isBookmarked) {
        Log.d(TAG, "onResponse: 북마크 안되어 있음.");
        mVideoIsBookmarked = isBookmarked;
        notifyDataSetChanged();
    }

    public void setCoachBookmarkResultData(boolean isBookmarked) {
        Log.d(TAG, "onResponse: 북마크 안되어 있음.");
        mCoachIsBookmarked = isBookmarked;
        notifyDataSetChanged();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        // Cell
        boolean isCell;
        TextView cellExerciseType, cellBodyPart01, cellBodyPart02, cellTitle, cellLevel;
        ImageView imgLevel01, imgLevel02, imgLevel03;
        CardView cardView;

        // header
        boolean isHeader;
        boolean isFooter;
        ImageView profileImg;
        ImageView check;
        TextView tvLevel, tvExercisetype, tvBodytype01, tvBodytype02,
                tvContent, tvHit, tvRecommend, tvCoachBookmark, tvNotCoachBookmark, tvTitle, videoBookmarkCount;
        TextView name;
        ImageButton btnComment, btnVideoBookmark;
        TextView coachBookmarkCount;
        TextView tvCommentCount;
        LinearLayout linearVideoBookmark, linearComment;
        View layoutRow, layoutNone;

        //footer
        TextView tvFooter;

        public ViewHolder(View itemView, int viewType) {
            super(itemView);

            if (viewType == TYPE_CELL) {
                isCell = true;
                cardView = (CardView) itemView.findViewById(R.id.card_view);
                cellTitle = (TextView) itemView.findViewById(R.id.cell_tv_title);
                cellLevel = (TextView) itemView.findViewById(R.id.cell_tv_level);
                imgLevel01 = (ImageView) itemView.findViewById(R.id.img_level_01);
                imgLevel02 = (ImageView) itemView.findViewById(R.id.img_level_02);
                imgLevel03 = (ImageView) itemView.findViewById(R.id.img_level_03);
                cellExerciseType = (TextView) itemView.findViewById(R.id.cell_exercise_type);
                cellBodyPart01 = (TextView) itemView.findViewById(R.id.cell_bodyPart_01);
                cellBodyPart02 = (TextView) itemView.findViewById(R.id.cell_bodyPart_02);
                layoutRow = itemView.findViewById(R.id.layout_row);
                layoutNone = itemView.findViewById(R.id.layout_none);

            } else if (viewType == TYPE_HEADER) {
                isHeader = true;
                tvRecommend = (AppCompatTextView) itemView.findViewById(R.id.tv_recommend);
                check = (AppCompatImageView) itemView.findViewById(R.id.icon_bookmark_checek);
                profileImg = (ImageView) itemView.findViewById(R.id.img_profile);
                name = (AppCompatTextView) itemView.findViewById(R.id.tv_name);
                linearComment = (LinearLayout) itemView.findViewById(R.id.linear_comment);
                linearVideoBookmark = (LinearLayout) itemView.findViewById(R.id.linear_video_bookmark);
                coachBookmarkCount = (AppCompatTextView) itemView.findViewById(R.id.coach_bookmark_count);
                videoBookmarkCount = (AppCompatTextView) itemView.findViewById(R.id.tv_video_bookmark_count);
                tvCommentCount = (AppCompatTextView) itemView.findViewById(R.id.comment_count);
                tvCoachBookmark = (AppCompatTextView) itemView.findViewById(R.id.tv_coach_bookmark);
                tvNotCoachBookmark = (AppCompatTextView) itemView.findViewById(R.id.tv_not_coach_bookmark);
                btnVideoBookmark = (ImageButton) itemView.findViewById(R.id.btn_video_bookmark);
                btnComment = (ImageButton) itemView.findViewById(R.id.btn_comment);
                tvLevel = (AppCompatTextView) itemView.findViewById(R.id.tv_level);
                imgLevel01 = (ImageView) itemView.findViewById(R.id.img_level_01);
                imgLevel02 = (ImageView) itemView.findViewById(R.id.img_level_02);
                imgLevel03 = (ImageView) itemView.findViewById(R.id.img_level_03);
                tvExercisetype = (AppCompatTextView) itemView.findViewById(R.id.tv_exercisetype);
                tvBodytype01 = (AppCompatTextView) itemView.findViewById(R.id.tv_bodytype01);
                tvBodytype02 = (AppCompatTextView) itemView.findViewById(R.id.tv_bodytype02);
                tvTitle = (AppCompatTextView) itemView.findViewById(R.id.tv_title);
                tvContent = (AppCompatTextView) itemView.findViewById(R.id.tv_content);
                tvHit = (AppCompatTextView) itemView.findViewById(R.id.tv_hit);


                linearVideoBookmark.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (LoginUtils.isLoggedIn()) {
                            mCallback.videoOnBookmarkClicked(!mVideoIsBookmarked, mVideoId);
                        } else {
                            Utils.hideKeyboard(v, mContext);
                            LoginUtils.showLoginDialog(mContext);
                        }
                    }
                });
                //북마크가 안되있을경우 : +구독
                tvNotCoachBookmark.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (LoginUtils.isLoggedIn()) {
                            mCallback.coachOnBookmarkClicked(!mCoachIsBookmarked, mVideoHeaderData.getCoachId());
                            Log.i(TAG, "mCoachIsBookmarked :" + mCoachIsBookmarked);
                            Log.i(TAG, "coachId :" + mVideoHeaderData.getCoachId());
                        } else {
                            Utils.hideKeyboard(v, mContext);
                            LoginUtils.showLoginDialog(mContext);
                        }
                    }
                });
                //북마크가 되어있을경우 : 구독중
                tvCoachBookmark.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (LoginUtils.isLoggedIn()) {
                            mCallback.coachOnBookmarkClicked(!mCoachIsBookmarked, mVideoHeaderData.getCoachId());
                            Log.i(TAG, "mCoachIsBookmarked :" + mCoachIsBookmarked);
                            Log.i(TAG, "coachId :" + mVideoHeaderData.getCoachId());
                        }
                    }
                });

            } else if (viewType == TYPE_FOOTER) {

                isFooter = true;
                tvFooter = (TextView) itemView.findViewById(R.id.tv_footer);
            }

        }
    }

    public interface BookmarkClickListener {
        void videoOnBookmarkClicked(boolean isSelected, int videoId);
        void coachOnBookmarkClicked(boolean isSelected, int coachId);

    }


}