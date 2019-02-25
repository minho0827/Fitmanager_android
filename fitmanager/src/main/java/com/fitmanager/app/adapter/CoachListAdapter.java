package com.fitmanager.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fitmanager.app.R;
import com.fitmanager.app.model.CoachVO;
import com.fitmanager.app.util.Utils;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class CoachListAdapter extends RecyclerView.Adapter<CoachListAdapter.ViewHolder> {
    private static final String TAG = "CoachListAdapter";
    private List<CoachVO> mCoachItems = null;
    private SparseBooleanArray selectedItems;
    int totalCoachCount = 0;
    public static final int ADAPTER_TYPE_FILTER = 0;
    public static final int ADAPTER_TYPE_SEARCH = 1;
    private int mAdapterType = ADAPTER_TYPE_FILTER;

    static final int TYPE_HEADER = 0;
    static final int TYPE_CELL = 1;

    private EndlessScrollListener mEndlessScrollListener;
    private CoachListListener mCoachListListener;
    private Context mContext;

    private void setEndlessScrollListener(EndlessScrollListener endlessScrollListener) {
        this.mEndlessScrollListener = endlessScrollListener;
    }

    private void setCoachListListener(CoachListListener coachListListener) {
        this.mCoachListListener = coachListListener;
    }

    public CoachListAdapter(CoachListListener coachListListener,
                            List<CoachVO> profileData, int adapterType) {
        if (profileData == null) {
            throw new IllegalArgumentException("modelData must not be null");
        }
//        mHomeFragment = coachListListener;
        mAdapterType = adapterType;
        mCoachItems = profileData;
        selectedItems = new SparseBooleanArray();
        setEndlessScrollListener((EndlessScrollListener) coachListListener);
        setCoachListListener(coachListListener);

        mContext = mCoachListListener.getCoachContext();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 && mAdapterType != ADAPTER_TYPE_SEARCH) {
            return TYPE_HEADER;
        } else {
            return TYPE_CELL;
        }
    }

    public void setTotalCoachCount(int totalCoachCount) {
        this.totalCoachCount = totalCoachCount;
    }

    public int getTotalCoachCount() {
        return totalCoachCount;
    }

    private CoachVO getItem(int position) {
        int headerAdjust = (mAdapterType == ADAPTER_TYPE_SEARCH) ? 0 : 1;

        if (position < 0) {
            return null;
        } else if (getItemViewType(position) == TYPE_HEADER) {
            return null;
        } else {
            if (mCoachItems.size() <= position - headerAdjust) {
                return null;
            }
            return mCoachItems.get(position - headerAdjust);
        }
    }

    public void setData(List<CoachVO> coachList) {
        this.mCoachItems = coachList;
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        if (mAdapterType == ADAPTER_TYPE_SEARCH) {
            return mCoachItems.size();
        }
        return mCoachItems.size() + 1;
    }


    @Override

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        if (viewType == TYPE_HEADER) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.coach_list_header, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.coach_list_item_row, parent, false);
        }
        return new ViewHolder(view, viewType);
    }


    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        if (viewHolder.isHeader) {


        } else if (viewHolder.isCell) {
            if (position == getItemCount() - 1) {
                if (mEndlessScrollListener != null) {
                    mEndlessScrollListener.onLoadMore(position);
                }
            }

            final CoachVO coachVO = getItem(position);
            if (coachVO != null) {
                String sbAppendHeight = coachVO.getHeight().toString() + "cm";
                String sbAppendWeight = coachVO.getWeight().toString() + "kg";
                Glide.with(mContext)
                        .load(coachVO.getProfileImg())
                        .centerCrop()
                        .bitmapTransform(new CropCircleTransformation(mContext))
                        .crossFade()
                        .into(viewHolder.imgProfile);

                viewHolder.tvProfileName.setText(coachVO.getName());
                viewHolder.tvExerciseType.setText(Utils.getExerciseType(coachVO.getExerciseType()));
                viewHolder.tvExerciseType.setBackgroundResource
                        (Utils.getExerciseByColorType(coachVO.getExerciseType()));
                viewHolder.tvCompany.setText(strInsertBlank(coachVO.getCompany()));
                viewHolder.tvCompany.setBackgroundResource(R.drawable.xml_colorbg_main);

                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        mCoachListListener.callCoachActivity(coachVO.getCoachId());
                        Log.i(TAG, "onBindViewHolder > getCoachId : " + coachVO.getCoachId());
                    }
                });
            }


        }

    }


    private String strInsertBlank(String getStrData) {
        return "  " + StringUtils.defaultString(getStrData, "") + "  ";
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        // Cell
        TextView tvProfileName;
        //        TextView tvHeight;
//        TextView tvWeight;
        TextView tvExerciseType;
        ImageView imgProfile;
        TextView tvCoachCount;
        TextView tvCompany;
        boolean isHeader;
        boolean isCell;

        // header
        Button btnFilter;
        Button btnCoachSearch;

        private Context mContext;

        public ViewHolder(View itemView, int viewType) {
            super(itemView);
            mContext = itemView.getContext();

            if (viewType == TYPE_CELL) {
                isCell = true;
                tvProfileName = itemView.findViewById(R.id.tv_name);
//                tvHeight = (TextView) itemView.findViewById(R.id.tv_height);
//                tvWeight = (TextView) itemView.findViewById(R.id.tv_weight);
                tvExerciseType = itemView.findViewById(R.id.tv_exerciseType);
                imgProfile = itemView.findViewById(R.id.img_profile);
                tvCompany = itemView.findViewById(R.id.tv_company);

            } else if (viewType == TYPE_HEADER) {
                if (mAdapterType == ADAPTER_TYPE_SEARCH) {
                    itemView.setVisibility(View.GONE);

                } else if (mAdapterType == ADAPTER_TYPE_FILTER) {
                    itemView.setVisibility(View.VISIBLE);
                    isHeader = true;
                    btnFilter = (Button) itemView.findViewById(R.id.btnFilter);
//                    btnCoachSearch = (Button) itemView.findViewById(R.id.btnCoachSearch);
//                    tvCoachCount = (TextView) itemView.findViewById(R.id.tv_coach_count);

                    btnFilter.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mCoachListListener.callCoachFilterActivity();

                        }
                    });
//                    TODO: 이름으로 검색
//                    btnCoachSearch.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            mCoachListListener.callSearchActivity();
//                        }
//                    });
                }

            }

        }
    }

    public interface EndlessScrollListener {
        /**
         * Loads more data.
         *
         * @param position
         * @return true loads data actually, false otherwise.
         */
        boolean onLoadMore(int position);
    }

    public interface CoachListListener {
        void callCoachFilterActivity();

        void callSearchActivity();

        void callCoachActivity(int coachId);

        Context getCoachContext();
    }
}


