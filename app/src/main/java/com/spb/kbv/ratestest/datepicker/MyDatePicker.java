package com.spb.kbv.ratestest.datepicker;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.spb.kbv.ratestest.R;

public class MyDatePicker extends ListView implements AbsListView.OnScrollListener{

    private MyHeader header;
    int mCentreItemOffset;
    private int mCentralItem;
    private int mCentralIndex;
    private Paint p;

    public MyDatePicker(Context context) {
        super(context);
        setOnScrollListener(this);
        setDivider(null);
        setScrollContainer(false);
        setVerticalScrollBarEnabled(false);
        p = new Paint();
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

        if (scrollState == SCROLL_STATE_IDLE) {
            setToCentralItem(mCentralItem, mCentreItemOffset, 5);
        }
    }

    @Override
    public void onScroll(AbsListView absListView, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {

        //sets header
        if (header == null && getHeight() > 0 && getChildAt(firstVisibleItem) != null) {
            Log.d("myListLogs", " added header/footer ");
            mCentreItemOffset = (getHeight() - getChildAt(1).getHeight()) / 2;
            header = new MyHeader(getContext(), mCentreItemOffset);
            addHeaderView(header);
            addFooterView(header);
        }

        if (absListView.getAdapter() != null) {

            findCentralView();
            refreshViews();
        }
    }

    private void findCentralView(){
        int centre = (getTop() + getBottom()) / 2;
        int merge = 0;
        int newMerge = 0;

        for (int i = 0; i < getChildCount(); i++){
            int centreOfItem = (getChildAt(i).getTop() + getChildAt(i).getBottom()) / 2;
            if (i == 0) {
                merge = Math.abs(centreOfItem - centre);
                continue;
            }
            newMerge = Math.abs(centreOfItem - centre);
            if (newMerge < merge){
                merge = newMerge;
                mCentralIndex = i;
            }
        }
        mCentralItem = getFirstVisiblePosition() + mCentralIndex;
    }

    public void refreshViews() {
        for (int i = 1; i < getChildCount(); i++) {
            View dateView = getChildAt(i);
            /*if ((i > (centralIndex + headerOffset - 3)) || (i < (centralIndex - headerOffset + 3)))
                dateView.setAlpha(0.3f);
            else
                dateView.setAlpha(1);*/

            TextView dateMonthText = (TextView) dateView.findViewById(R.id.date_list_item_month);
            TextView dateYearText = (TextView) dateView.findViewById(R.id.date_list_item_year);
            TextView dateDayText = (TextView) dateView.findViewById(R.id.date_list_item_date);

            // if center view
            if (i == mCentralIndex) {
                dateMonthText.setVisibility(GONE);
                dateYearText.setVisibility(VISIBLE);
                dateDayText.setVisibility(INVISIBLE);
            }
            // if not center view and not footer
            else if (i != getChildCount() - 1) {
                dateYearText.setVisibility(INVISIBLE);
                dateMonthText.setVisibility(VISIBLE);
                dateDayText.setVisibility(VISIBLE);
            }
        }
    }

    public void setToCentralItem(int centralItem, int centreItemOffset, int duration) {
        smoothScrollToPositionFromTop(
                centralItem,
                centreItemOffset,
                duration);
    }

    public class MyHeader extends LinearLayout {
        public MyHeader(Context context, int offset) {
            super(context);
            this.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, offset));
            this.setEnabled(false);
        }
    }

    public int getCentralItem() {
        return mCentralItem;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d("myListLogs", " in on Draw");
        p.setStrokeWidth(5);
        p.setColor(getContext().getResources().getColor(R.color.blue_text_color));
        if (getChildAt(1) != null ) {
            int itemHeight = getChildAt(1).getHeight();
            int x = getWidth();
            int y1 = (getHeight() - itemHeight) / 2;
            int y2 = y1 + itemHeight;
            canvas.drawLine(0, y1, x, y1, p);
            canvas.drawLine(0, y2, x, y2, p);
        }
    }
}
