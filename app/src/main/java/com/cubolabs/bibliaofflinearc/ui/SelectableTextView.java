package com.cubolabs.bibliaofflinearc.ui;

import android.content.Context;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

public class SelectableTextView extends TextView {

    public static int _SelectedBackgroundColor = 0xffA6D4E1;
    public static int _SelectedTextColor = 0xff000000;
    private OnTouchListener lastOnTouch;
    protected int textOffsetStart;
    protected int textOffsetEnd;
    private OnLongClickListener lastOnLongClick;
    protected boolean longCliked;
    protected boolean isDowned;
    protected int textSelectedEnd;
    protected int textSelectedStart;
    private static SelectableTextView lastInstance;

    public SelectableTextView(Context context) {
        super(context);
    }

    public SelectableTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SelectableTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setTextIsSelectable(boolean selectable) {
        // TODO:ANDROID3
        // if:androidversion>=3
        // super.setTextIsSelectable(selectable);
        // else
        super.setLongClickable(true);
        super.setOnLongClickListener(getSelectableLongClick());
        super.setOnTouchListener(getSelectableOnTouch());
    }

    private OnLongClickListener getSelectableLongClick() {
        return new OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                longCliked = true;
                disableParentScrolling();
                if (lastOnLongClick != null) {
                    lastOnLongClick.onLongClick(v);
                }
                return true;
            }
        };
    }

    @Override
    public void setOnTouchListener(OnTouchListener l) {
        super.setOnTouchListener(l);
        this.lastOnTouch = l;
    }

    @Override
    public void setOnLongClickListener(OnLongClickListener l) {
        super.setOnLongClickListener(l);
        this.lastOnLongClick = l;
    }

    private OnTouchListener getSelectableOnTouch() {
        return new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();

                if (action == MotionEvent.ACTION_DOWN) {
                    if (lastInstance == null)
                        lastInstance = SelectableTextView.this;
                    if (lastInstance != null && lastInstance != SelectableTextView.this) {
                        lastInstance.clean();
                        lastInstance = SelectableTextView.this;
                    }

                    int offset = getOffset(event);
                    if ((offset < textOffsetEnd && offset > textOffsetStart)
                            || (offset > textOffsetEnd && offset < textOffsetStart)) {
                        if (textOffsetEnd - offset > offset - textOffsetStart)
                            textOffsetStart = textOffsetEnd;
                    } else {
                        clean();
                        textOffsetStart = offset;
                    }
                    isDowned = true;
                } else if (isDowned && longCliked && action == MotionEvent.ACTION_MOVE) {
                    selectTextOnMove(event);
                } else if (action == MotionEvent.ACTION_UP) {
                    isDowned = false;
                    longCliked = false;
                    enableParentScrolling();
                }
                if (lastOnTouch != null)
                    lastOnTouch.onTouch(v, event);
                return false;
            }

        };
    }

    private void selectTextOnMove(MotionEvent event) {
        int offset = getOffset(event);
        if (offset != Integer.MIN_VALUE) {
            String text = getText().toString();
            SpannableStringBuilder sb = new SpannableStringBuilder(text);
            BackgroundColorSpan bgc = new BackgroundColorSpan(_SelectedBackgroundColor);
            ForegroundColorSpan textColor = new ForegroundColorSpan(_SelectedTextColor);

            int start = textOffsetStart;
            textOffsetEnd = offset;
            int end = offset;
            if (start > end) {
                int temp = start;
                start = end;
                end = temp;
            }
            int[] curectStartEnd = curectStartEnd(text, start, end);
            start = curectStartEnd[0];
            end = curectStartEnd[1];
            SelectableTextView.this.textSelectedStart = start;
            SelectableTextView.this.textSelectedEnd = end;
            sb.setSpan(bgc, start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            sb.setSpan(textColor, start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE);

            setText(sb);
        }
    }

    private int[] curectStartEnd(String text, int start, int end) {
        int length = text.length();

        while (start < length && start > 0 && text.charAt(start) != ' ') {
            start--;
        }
        while (end < length && text.charAt(end) != ' ') {
            end++;
        }
        return new int[] { start, end };
    }

    private int getOffset(MotionEvent event) {
        Layout layout = getLayout();
        if (layout == null)
            return Integer.MIN_VALUE;
        float x = event.getX() + getScrollX();
        float y = event.getY() + getScrollY();
        int line = layout.getLineForVertical((int) y);
        int offset = layout.getOffsetForHorizontal(line, x);
        return offset;
    }

    protected void clean() {
        if (this.getText() != null) {
            this.setText(this.getText().toString());
            textSelectedStart = 0;
            textSelectedEnd = 0;
        }
    }

    @Override
    public int getSelectionStart() {
        return textSelectedStart;
    }

    @Override
    public int getSelectionEnd() {
        return textSelectedEnd;
    }

    protected void enableParentScrolling() {
        ViewParent parent = this.getParent();
        if (parent == null) {
            this.setScrollContainer(false);
            return;
        }
        View v = (View) parent;
        while (v != null && !ScrollView.class.isAssignableFrom(v.getClass())
                && !ListView.class.isAssignableFrom(v.getClass())) {
            v = (View) v.getParent();
        }
        if (ScrollView.class.isAssignableFrom(v.getClass())) {
            // TODO:FORM Custom ScrollView set stopMove
        //} else if (SelectableListView.class.isAssignableFrom(v.getClass())) {
        //    ((SelectableListView) v).stopMove = false;
        }
    }

    protected void disableParentScrolling() {
        ViewParent parent = this.getParent();
        if (parent == null) {
            this.setScrollContainer(false);
            return;
        }
        View v = (View) parent;
        while (v != null && !ScrollView.class.isAssignableFrom(v.getClass())
                && !ListView.class.isAssignableFrom(v.getClass())) {
            v = (View) v.getParent();
        }
        if (ScrollView.class.isAssignableFrom(v.getClass())) {
            // TODO:FORM Custom ScrollView set stopMove
        //} else if (SelectableListView.class.isAssignableFrom(v.getClass())) {
        //    ((SelectableListView) v).stopMove = true;
        }
    }
}
