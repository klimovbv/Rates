package com.spb.kbv.ratestest.datepicker;

import android.content.Context;
import android.view.View;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.spb.kbv.ratestest.R;

public class MyDatePicker extends ListView implements AbsListView.OnScrollListener{
    private static final int HEADER_SEEN = 0;
    private static final int CLEAR = 1;
    private static final int FOOTER_SEEN = 2;

    private MyHeader header;
    private int listItemHeight;
    private int headerOffset;
    private int centralItem;
    private int centralIndex;

    public MyDatePicker(Context context) {
        super(context);
        setOnScrollListener(this);
        setDivider(null);
        setScrollContainer(false);
        setVerticalScrollBarEnabled(false);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (listItemHeight == 0) {
            listItemHeight = getChildAt(1).getHeight();
        }

        if (scrollState == SCROLL_STATE_IDLE) {
            view.smoothScrollToPositionFromTop(getFirstVisiblePosition() + centralIndex,
                    (getHeight() - listItemHeight) / 2,
                    5);
        }
    }

    @Override
    public void onScroll(AbsListView absListView, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {

        if (headerOffset <= 0 & header != null) {
            headerOffset = getLastVisiblePosition();
        }

        //sets header
        if (header == null && getHeight() > 0 && getChildAt(1) != null) {
            header = new MyHeader(getContext(), (getHeight() - getChildAt(1).getHeight()) / 2);
            addHeaderView(header);
            addFooterView(header);
        }

        if (absListView.getAdapter() != null) {
            int flagHeaderFooter;
            if (firstVisibleItem == 0) {
                centralItem = visibleItemCount - headerOffset;
                flagHeaderFooter = HEADER_SEEN;
            } else if (firstVisibleItem + visibleItemCount == totalItemCount) {
                centralItem = firstVisibleItem - 1 + headerOffset;
                flagHeaderFooter = FOOTER_SEEN;
            } else {
                centralItem = (firstVisibleItem + visibleItemCount / 2);
                flagHeaderFooter = CLEAR;
            }
            centralIndex = centralItem - getFirstVisiblePosition();
            refreshViews(flagHeaderFooter);
        }
    }

    private void refreshViews(int flag) {
        for (int i = 1; i < getChildCount(); i++) {
            View dateView = getChildAt(i);
            if ((i > (centralIndex + headerOffset - 3)) || (i < (centralIndex - headerOffset + 3)))
                dateView.setAlpha(0.3f);
            else
                dateView.setAlpha(1);

            TextView dateMonthText = (TextView) dateView.findViewById(R.id.date_list_item_month);
            TextView dateYearText = (TextView) dateView.findViewById(R.id.date_list_item_year);
            TextView dateDayText = (TextView) dateView.findViewById(R.id.date_list_item_date);

            // if center view
            if (i == centralIndex) {
                dateMonthText.setVisibility(GONE);
                dateYearText.setVisibility(VISIBLE);
                dateDayText.setVisibility(INVISIBLE);
            }
            // if not center view
            else if (flag != FOOTER_SEEN || i != getChildCount() - 1){
                dateYearText.setVisibility(INVISIBLE);
                dateMonthText.setVisibility(VISIBLE);
                dateDayText.setVisibility(VISIBLE);
            }
        }
    }

    public class MyHeader extends LinearLayout {
        public MyHeader(Context context, int offset) {
            super(context);
            this.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, offset));
            this.setEnabled(false);
        }
    }

    public int getCentralItem() {
        return centralItem - 1;
    }
}
