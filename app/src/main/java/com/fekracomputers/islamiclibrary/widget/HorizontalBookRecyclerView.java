package com.fekracomputers.islamiclibrary.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fekracomputers.islamiclibrary.R;
import com.fekracomputers.islamiclibrary.browsing.adapters.BookListRecyclerViewAdapter;
import com.fekracomputers.islamiclibrary.browsing.interfaces.BookCardEventsCallback;
import com.fekracomputers.islamiclibrary.databases.BooksInformationDBContract;
import com.fekracomputers.islamiclibrary.utility.Util;

/**
 * بسم الله الرحمن الرحيم
 * Created by Mohammd Yahia on 23/4/2017.
 */

public class HorizontalBookRecyclerView extends RelativeLayout {

    private BookListRecyclerViewAdapter mBookListRecyclerViewAdapter;

    public HorizontalBookRecyclerView(Context context) {
        this(context, null);
    }

    public HorizontalBookRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HorizontalBookRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public HorizontalBookRecyclerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initialize();
    }

    public void initialize() {
        inflate(getContext(), R.layout.horizontal_book_listing, this);
    }

    public void setupRecyclerView(BookCardEventsCallback mListener, Cursor bookListCursor, boolean isGrey) {
        setBackgroundResource(isGrey ?
                R.color.infoPage_details_gray :
                R.color.infoPage_details_white);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        String idCoulmnName = BooksInformationDBContract.BooksCategories.COLUMN_NAME_BOOK_ID;
        mBookListRecyclerViewAdapter = new BookListRecyclerViewAdapter(
                getContext(),
                bookListCursor,
                idCoulmnName,
                mListener);
        mBookListRecyclerViewAdapter.setLayoutManagerType(BookListRecyclerViewAdapter.LINEAR_HORIZONTAL_LAYOUT_MANAGER);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(mBookListRecyclerViewAdapter);
        recyclerView.addItemDecoration(
                new EndItemsPaddingDecoration(
                        Util.dpToPxOffset(getContext(), 8),
                        Util.dpToPxOffset(getContext(), 8),
                        //       ViewCompat.getLayoutDirection(recyclerView) == ViewCompat.LAYOUT_DIRECTION_RTL,
                        true,
                        bookListCursor.getCount() - 1)
        );

    }

    public void setTitleText(CharSequence charSequence) {
        ((TextView) findViewById(R.id.title_tv)).setText(charSequence);
    }

    public void setMoreTextViewOnClickListener(View.OnClickListener onClickListener) {
        findViewById(R.id.more_tv).setOnClickListener(onClickListener);
    }

    public void changeCursor(Cursor newCursor) {
        mBookListRecyclerViewAdapter.changeCursor(newCursor);
    }

    public void closeCursor() {
        mBookListRecyclerViewAdapter.getCursor().close();
    }

    public void notifyDatasetChanged() {
        mBookListRecyclerViewAdapter.notifyDataSetChanged();
    }

    private class EndItemsPaddingDecoration extends RecyclerView.ItemDecoration {
        private int paddingStart = 0;
        private int paddingEnd = 0;
        private int mLastItemIndex;
        private boolean mIsRtl;

        EndItemsPaddingDecoration(int paddingStart, int paddingEnd, boolean isRtl, int lastItemIndex) {
            this.paddingStart = paddingStart;
            this.paddingEnd = paddingEnd;
            this.mIsRtl = isRtl;
            this.mLastItemIndex = lastItemIndex;

        }


        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            int position = parent.getChildAdapterPosition(view);


            if (position == 0) {
                if (!mIsRtl) {
                    outRect.left += paddingStart;
                } else {
                    outRect.right += paddingStart;
                }
            }
            if (position == mLastItemIndex) {
                if (!mIsRtl) {
                    outRect.right += paddingEnd;
                } else {
                    outRect.left += paddingEnd;
                }
            }

        }

    }
}
