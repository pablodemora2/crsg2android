//package com.istudio.supertoast;
//
///**
// * Copyright 2013 John Persano
// * <p/>
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// * <p/>
// * http://www.apache.org/licenses/LICENSE-2.0
// * <p/>
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//import android.annotation.SuppressLint;
//import android.app.Activity;
//import android.content.Context;
//import android.graphics.Color;
//import android.graphics.PixelFormat;
//import android.graphics.Typeface;
//import android.graphics.drawable.Drawable;
//import android.util.TypedValue;
//import android.view.Gravity;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.WindowManager;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import com.istudio.R;
//
///**
// * SuperToasts are designed to replace stock Android Toasts. If you need to
// * display a SuperToast inside of an Activity please see the class
// * SuperActivityToast.
// */
//public class SuperToast {
//
//    /**
//     * Can only be used in the SuperToast class.
//     */
//    public static final int ANIMATION_FADE = (android.R.style.Animation_Toast);
//    /**
//     * Can only be used in the SuperToast class.
//     */
//    public static final int ANIMATION_FLYIN = (android.R.style.Animation_Translucent);
//    /**
//     * Can only be used in the SuperToast class.
//     */
//    public static final int ANIMATION_SCALE = (android.R.style.Animation_Dialog);
//    /**
//     * Can only be used in the SuperToast class.
//     */
//    public static final int ANIMATION_POPUP = (android.R.style.Animation_InputMethod);
//    public static final int BACKGROUND_BLACK = (R.drawable.background_black);
//    public static final int BACKGROUND_BLACKTRANSLUCENT = (R.drawable.background_blacktranslucent);
//    public static final int BACKGROUND_BLUE = (R.drawable.background_blue);
//    public static final int BACKGROUND_BLUETRANSLUCENT = (R.drawable.background_bluetranslucent);
//    // (R.drawable.background_bluetranslucent);
//    // public static final int BACKGROUND_GREEN = (R.drawable.background_green);
//    // public static final int BACKGROUND_GREENTRANSLUCENT =
//    // (R.drawable.background_greentranslucent);
//    public static final int BACKGROUND_GREY = (R.drawable.background_grey);
//    public static final int BACKGROUND_GREYTRANSLUCENT = (R.drawable.background_greytranslucent);
//    // public static final int BACKGROUND_PURPLE =
//    // (R.drawable.background_purple);
//    // public static final int BACKGROUND_PURPLETRANSLUCENT =
//    // (R.drawable.background_purpletranslucent);
//    // public static final int BACKGROUND_RED = (R.drawable.background_red);
//    // public static final int BACKGROUND_REDTRANSLUCENT =
//    // (R.drawable.background_redtranslucent);
//    public static final int BACKGROUND_WHITE = (R.drawable.background_white);
//    public static final int BACKGROUND_WHITETRANSLUCENT = (R.drawable.background_whitetranslucent);
//    public static final int DURATION_SHORT = (2000);
//    public static final int DURATION_MEDIUM = (2750);
//    // (R.drawable.background_whitetranslucent);
//    // public static final int BACKGROUND_ORANGE =
//    // (R.drawable.background_orange);
//    // public static final int BACKGROUND_ORANGETRANSLUCENT =
//    // (R.drawable.background_orangetranslucent);
//    // public static final int BACKGROUND_TURQUOISE =
//    // (R.drawable.background_turquoise);
//    // public static final int BACKGROUND_TURQUOISETRANSLUCENT =
//    // (R.drawable.background_turquoisetranslucent);
//    //
//    // public static final int BUTTON_DARK_EDIT = (R.drawable.icon_dark_edit);
//    // public static final int BUTTON_DARK_EXIT = (R.drawable.icon_dark_exit);
//    // public static final int BUTTON_DARK_INFO = (R.drawable.icon_dark_info);
//    // public static final int BUTTON_DARK_REDO = (R.drawable.icon_dark_redo);
//    // public static final int BUTTON_DARK_SAVE = (R.drawable.icon_dark_save);
//    // public static final int BUTTON_DARK_SHARE = (R.drawable.icon_dark_share);
//    // public static final int BUTTON_DARK_UNDO = (R.drawable.icon_dark_undo);
//    // public static final int BUTTON_LIGHT_EDIT = (R.drawable.icon_light_edit);
//    // public static final int BUTTON_LIGHT_EXIT = (R.drawable.icon_light_exit);
//    // public static final int BUTTON_LIGHT_INFO = (R.drawable.icon_light_info);
//    // public static final int BUTTON_LIGHT_REDO = (R.drawable.icon_light_redo);
//    // public static final int BUTTON_LIGHT_SAVE = (R.drawable.icon_light_save);
//    // public static final int BUTTON_LIGHT_SHARE =
//    // (R.drawable.icon_light_share);
//    // public static final int BUTTON_LIGHT_UNDO = (R.drawable.icon_light_undo);
//    public static final int DURATION_LONG = (3500);
//    public static final int DURATION_XLONG = (4500);
//    public static final int TEXTSIZE_SMALL = (14);
//    public static final int TEXTSIZE_MEDIUM = (18);
//    public static final int TEXTSIZE_LARGE = (22);
//    private static final String ERROR_CONTEXTNULL = "The Context that you passed was null! (SuperToast)";
//    private static SuperToast instance = null;
//    private int mAnimation = ANIMATION_FADE;
//    private int mDuration = DURATION_SHORT;
//    private Context mContext;
//    private LayoutInflater mLayoutInflater;
//    private WindowManager mWindowManager;
//    private LinearLayout mRootLayout;
//    private WindowManager.LayoutParams mWindowManagerParams;
//    private View mToastView;
//    private TextView mMessageTextView;
//    private int mSdkVersion = android.os.Build.VERSION.SDK_INT;
//    private int mGravity = Gravity.BOTTOM | Gravity.CENTER;
//    private int mXOffset = 0;
//    private int mYOffset = 0;
//    private OnDismissListener mOnDismissListener;
//
//    Typeface mTypefaceStyle;
//
//    /**
//     * Instantiates a new SuperToast. <br>
//     *
//     * @param context
//     */
//    protected SuperToast(Activity pAct, Context context) {
//
//        if (context != null) {
//
//            this.mContext = context;
//
//            mYOffset = context.getResources().getDimensionPixelSize(
//                    R.dimen.toast_yoffset);
//
//            mLayoutInflater = (LayoutInflater) context
//                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//
//            mToastView = mLayoutInflater.inflate(R.layout.supertoast, null);
//
//            mWindowManager = (WindowManager) mToastView.getContext()
//                    .getApplicationContext()
//                    .getSystemService(Context.WINDOW_SERVICE);
//
//            mRootLayout = (LinearLayout) mToastView
//                    .findViewById(R.id.root_layout);
//
//            mMessageTextView = (TextView) mToastView
//                    .findViewById(R.id.message_textView);
//
//        } else {
//
//            throw new IllegalArgumentException(ERROR_CONTEXTNULL);
//
//        }
//
//    }
//
//    public static void setInstance(SuperToast instance) {
//
//        SuperToast.instance = instance;
//
//    }
//
//    public static SuperToast getInstance(Activity pAct, Context pCon) {
//
//        if (instance == null) {
//
//            instance = new SuperToast(pAct, pCon);
//
//        }
//        return instance;
//
//    }
//
//    /**
//     * Creates a dark theme SuperToast. Don't forget to call {@link #show()}.
//     * <p/>
//     * <br>
//     *
//     * @param context
//     * @param textCharSequence
//     * @param durationInteger
//     * @return SuperToast
//     */
//    public static SuperToast createDarkSuperToast(Context context, Activity pAct, CharSequence textCharSequence, int durationInteger) {
//
//        SuperToast superToast = new SuperToast(pAct, context);
//        superToast.setText(textCharSequence);
//        superToast.setDuration(durationInteger);
//
//        return superToast;
//
//    }
//
//    /**
//     * Creates a light theme SuperToast. Don't forget to call {@link #show()}.
//     * <p/>
//     * <br>
//     *
//     * @param context
//     * @param textCharSequence
//     * @param durationInteger
//     * @return SuperToast
//     */
//    public static SuperToast createLightSuperToast(Context context, Activity pAct,
//                                                   CharSequence textCharSequence, int durationInteger) {
//
//        SuperToast superToast = new SuperToast(pAct, context);
//        superToast.setText(textCharSequence);
//        superToast.setDuration(durationInteger);
//        // superToast.setBackgroundResource(SuperToast.BACKGROUND_WHITE);
//        superToast.setTextColor(Color.BLACK);
//
//        return superToast;
//
//    }
//
//    /**
//     * Creates a dark theme SuperToast. Don't forget to call {@link #show()}.
//     * <p/>
//     * <br>
//     *
//     * @param context
//     * @param textCharSequence
//     * @param durationInteger
//     * @param animation
//     * @return SuperToast
//     */
//    public static SuperToast createDarkSuperToast(Context context, Activity pAct,
//                                                  CharSequence textCharSequence, int durationInteger, int animation) {
//
//        SuperToast superToast = new SuperToast(pAct, context);
//        superToast.setText(textCharSequence);
//        superToast.setDuration(durationInteger);
//        superToast.setAnimation(animation);
//
//        return superToast;
//
//    }
//
//    /**
//     * Creates a light theme SuperToast. Don't forget to call {@link #show()}.
//     * <p/>
//     * <br>
//     *
//     * @param context
//     * @param textCharSequence
//     * @param durationInteger
//     * @param animation
//     * @return SuperToast
//     */
//    public static SuperToast createLightSuperToast(Context context, Activity pAct,
//                                                   CharSequence textCharSequence, int durationInteger, int animation) {
//
//        SuperToast superToast = new SuperToast(pAct, context);
//        superToast.setText(textCharSequence);
//        superToast.setDuration(durationInteger);
//        // superToast.setBackgroundResource(SuperToast.BACKGROUND_WHITE);
//        superToast.setTextColor(Color.BLACK);
//        superToast.setAnimation(animation);
//
//        return superToast;
//
//    }
//
//    /**
//     * Dismisses and removes all showing/pending SuperActivityToasts.
//     */
//    public static void cancelAllSuperToasts() {
//
//        ManagerSuperToast.getInstance().clearQueue();
//
//    }
//
//    /**
//     * Shows the SuperToast.
//     */
//    public void show() {
//
//        mWindowManagerParams = new WindowManager.LayoutParams();
//
//        mWindowManagerParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
//        mWindowManagerParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
//        mWindowManagerParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
//                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
//                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
//        mWindowManagerParams.format = PixelFormat.TRANSLUCENT;
//        mWindowManagerParams.windowAnimations = mAnimation;
//        mWindowManagerParams.type = WindowManager.LayoutParams.TYPE_TOAST;
//        mWindowManagerParams.gravity = mGravity;
//        mWindowManagerParams.x = mXOffset;
//        mWindowManagerParams.y = mYOffset;
//
//        ManagerSuperToast.getInstance().add(this);
//
//    }
//
//    /**
//     * Sets the message text of the SuperToast. <br>
//     *
//     * @param text
//     */
//    public void setText(CharSequence text) {
//
//        mMessageTextView.setText(text);
//
//    }
//
//    /**
//     * Sets the message text color of the SuperToast. <br>
//     *
//     * @param textColor
//     */
//    public void setTextColor(int textColor) {
//
//        mMessageTextView.setTextColor(textColor);
//
//    }
//
//    /**
//     * Sets the text size of the SuperToast. This method will automatically
//     * convert the integer parameter to scaled pixels. <br>
//     *
//     * @param textSize
//     */
//    public void setTextSize(float textSize) {
//
//        mMessageTextView.setTextSize(textSize);
//
//    }
//
//    /**
//     * Sets an icon Drawable to the SuperToast with a position. <br>
//     *
//     * @param iconDrawable
//     * @param iconPosition
//     */
//    public void setIconDrawable(Drawable iconDrawable, IconPosition iconPosition) {
//
//        if (iconPosition == IconPosition.BOTTOM) {
//
//            mMessageTextView.setCompoundDrawablesWithIntrinsicBounds(null,
//                    null, null, iconDrawable);
//
//        } else if (iconPosition == IconPosition.LEFT) {
//
//            mMessageTextView.setCompoundDrawablesWithIntrinsicBounds(
//                    iconDrawable, null, null, null);
//
//        } else if (iconPosition == IconPosition.RIGHT) {
//
//            mMessageTextView.setCompoundDrawablesWithIntrinsicBounds(null,
//                    null, iconDrawable, null);
//
//        } else if (iconPosition == IconPosition.TOP) {
//
//            mMessageTextView.setCompoundDrawablesWithIntrinsicBounds(null,
//                    iconDrawable, null, null);
//
//        }
//
//    }
//
//    /**
//     * Sets an icon resource to the SuperToast with a position. <br>
//     *
//     * @param iconResource
//     * @param iconPosition
//     */
//    public void setIconResource(int iconResource, IconPosition iconPosition) {
//
//        if (iconPosition == IconPosition.BOTTOM) {
//
//            mMessageTextView.setCompoundDrawablesWithIntrinsicBounds(null,
//                    null, null,
//                    mContext.getResources().getDrawable(iconResource));
//
//        } else if (iconPosition == IconPosition.LEFT) {
//
//            mMessageTextView
//                    .setCompoundDrawablesWithIntrinsicBounds(mContext
//                                    .getResources().getDrawable(iconResource), null,
//                            null, null);
//
//        } else if (iconPosition == IconPosition.RIGHT) {
//
//            mMessageTextView.setCompoundDrawablesWithIntrinsicBounds(null,
//                    null, mContext.getResources().getDrawable(iconResource),
//                    null);
//
//        } else if (iconPosition == IconPosition.TOP) {
//
//            mMessageTextView.setCompoundDrawablesWithIntrinsicBounds(null,
//                    mContext.getResources().getDrawable(iconResource), null,
//                    null);
//
//        }
//
//    }
//
//    /**
//     * Sets the Gravity of the SuperToast. <br>
//     *
//     * @param gravity
//     */
//    public void setGravity(int gravity) {
//
//        this.mGravity = gravity;
//
//    }
//
//    /**
//     * Sets the background resource of the SuperToast. <br>
//     *
//     * @param backgroundResource
//     */
//    public void setBackgroundResource(int backgroundResource) {
//
//        mRootLayout.setBackgroundResource(backgroundResource);
//
//    }
//
//    /**
//     * Sets the background Drawable of the SuperToast. <br>
//     *
//     * @param backgroundDrawable
//     */
//    @SuppressWarnings("deprecation")
//    @SuppressLint("NewApi")
//    public void setBackgroundDrawable(Drawable backgroundDrawable) {
//
//        if (mSdkVersion < android.os.Build.VERSION_CODES.JELLY_BEAN) {
//
//            mRootLayout.setBackgroundDrawable(backgroundDrawable);
//
//        } else {
//
//            mRootLayout.setBackground(backgroundDrawable);
//
//        }
//
//    }
//
//    /**
//     * Sets the Typeface of the SuperToast TextView. <br>
//     *
//     * @param typeface
//     */
//    public void setTypeface(Typeface typeface) {
//
//        mTypefaceStyle = typeface;
//        mMessageTextView.setTypeface(mTypefaceStyle);
//
//    }
//
//    /**
//     * Sets the Animation of the SuperToast. This is limited to the constants of
//     * this class. <br>
//     *
//     * @param animation
//     */
//    public void setAnimation(int animation) {
//
//        this.mAnimation = animation;
//
//    }
//
//    /**
//     * Sets the X and Y offsets of the SuperToast. <br>
//     *
//     * @param xOffset
//     * @param yOffset
//     */
//    public void setXYCoordinates(int xOffset, int yOffset) {
//
//        this.mXOffset = xOffset;
//        this.mYOffset = yOffset;
//
//    }
//
//    // XXX: Getter methods
//
//    /**
//     * Dismisses the SuperToast.
//     */
//    public void dismiss() {
//
//        ManagerSuperToast.getInstance().removeSuperToast(this);
//
//    }
//
//    /**
//     * Returns the SuperToast TextView. <br>
//     *
//     * @return TextView <br>
//     */
//    public TextView getTextView() {
//
//        return mMessageTextView;
//
//    }
//
//    /**
//     * Returns the SuperToast View. <br>
//     *
//     * @return View <br>
//     */
//    public View getView() {
//
//        return mToastView;
//
//    }
//
//    /**
//     * Returns true if the SuperToast is showing. <br>
//     *
//     * @return boolean <br>
//     */
//    public boolean isShowing() {
//
//        if (mToastView != null) {
//
//            return mToastView.isShown();
//
//        } else {
//
//            return false;
//
//        }
//
//    }
//
//    /**
//     * Returns the set duration of the SuperToast. <br>
//     *
//     * @return long <br>
//     */
//    public long getDuration() {
//
//        return mDuration;
//
//    }
//
//    /**
//     * Sets the duration of the SuperToast. <br>
//     *
//     * @param duration
//     */
//    public void setDuration(int duration) {
//
//        this.mDuration = duration;
//
//    }
//
//    /**
//     * Returns the OnDismissListener of the SuperToast. <br>
//     *
//     * @return OnDismissListener <br>
//     */
//    public OnDismissListener getOnDismissListener() {
//
//        return mOnDismissListener;
//
//    }
//
//    // XXX: Static methods.
//
//    /**
//     * Sets an OnDismissListener defined in this library to the SuperToast. <br>
//     *
//     * @param onDismissListener
//     */
//    public void setOnDismissListener(OnDismissListener onDismissListener) {
//
//        this.mOnDismissListener = onDismissListener;
//
//    }
//
//    /**
//     * Returns the WindowManager that the SuperToast is attached to. <br>
//     *
//     * @return ViewGroup <br>
//     */
//    public WindowManager getWindowManager() {
//
//        return mWindowManager;
//
//    }
//
//    /**
//     * Returns the WindowManager.Params of the SuperToast. <br>
//     *
//     * @return ViewGroup <br>
//     */
//    public WindowManager.LayoutParams getWindowManagerParams() {
//
//        return mWindowManagerParams;
//
//    }
//
//    /**
//     * Specifies the type of SuperToast.
//     */
//    public enum Type {
//
//        /**
//         * Standard Toast type used for displaying messages.
//         */
//        STANDARD,
//
//        /**
//         * Progress type used for showing progress.
//         */
//        PROGRESS,
//
//        /**
//         * Progress type used for showing progress.
//         */
//        PROGRESS_HORIZONTAL,
//
//        /**
//         * Button type used for user interaction.
//         */
//        BUTTON
//
//    }
//
//    /**
//     * Specifies the position of a supplied icon.
//     */
//    public enum IconPosition {
//
//        /**
//         * Set the icon to the left of the text.
//         */
//        LEFT,
//
//        /**
//         * Set the icon to the right of the text.
//         */
//        RIGHT,
//
//        /**
//         * Set the icon on top of the text.
//         */
//        TOP,
//
//        /**
//         * Set the icon on the bottom of the text.
//         */
//        BOTTOM
//
//    }
//
//}

/**
 *  Copyright 2014 John Persano
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *	you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *	Unless required by applicable law or agreed to in writing, software
 *	distributed under the License is distributed on an "AS IS" BASIS,
 *	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *	See the License for the specific language governing permissions and
 *	limitations under the License.
 *
 */

package com.istudio.supertoast;

import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Typeface;
import android.os.Parcelable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.istudio.supertoast.util.Style;
import com.istudio.R;

@SuppressWarnings("UnusedDeclaration")
public class SuperToast {

    private static SuperToast instance = null;
    private static final String TAG = "SuperToast";

    private static final String ERROR_CONTEXTNULL = " - You cannot use a null context.";
    private static final String ERROR_DURATIONTOOLONG = " - You should NEVER specify a duration greater than " +
            "four and a half seconds for a SuperToast.";


        public static SuperToast getInstance(Context pCon) {

        if (instance == null) {

            instance = new SuperToast(pCon);

        }
        return instance;

    }

    public interface OnClickListener {

        public void onClick(View view, Parcelable token);

    }


    public interface OnDismissListener {

        public void onDismiss(View view);

    }

    /**
     * Backgrounds for all types of SuperToasts.
     */
    public static class Background {

        public static final int BLACK = Style.getBackground(Style.BLACK);
        public static final int BLUE = Style.getBackground(Style.BLUE);
        public static final int GRAY = Style.getBackground(Style.GRAY);
        public static final int GREEN = Style.getBackground(Style.GREEN);
        public static final int ORANGE = Style.getBackground(Style.ORANGE);
        public static final int PURPLE = Style.getBackground(Style.PURPLE);
        public static final int RED = Style.getBackground(Style.RED);
        public static final int WHITE = Style.getBackground(Style.WHITE);

    }

    /**
     * Animations for all types of SuperToasts.
     */
    public enum Animations {

        FADE,
        FLYIN,
        SCALE,
        POPUP

    }

    /**
     * Icons for all types of SuperToasts.
     */
    public static class Icon {

        /**
         * Icons for all types of SuperToasts with a dark background.
         */
        public static class Dark {

//            public static final int EDIT = (R.drawable.icon_dark_edit);
//            public static final int EXIT = (R.drawable.icon_dark_exit);
//            public static final int INFO = (R.drawable.icon_dark_info);
//            public static final int REDO = (R.drawable.icon_dark_redo);
//            public static final int REFRESH = (R.drawable.icon_dark_refresh);
//            public static final int SAVE = (R.drawable.icon_dark_save);
//            public static final int SHARE = (R.drawable.icon_dark_share);
//            public static final int UNDO = (R.drawable.icon_dark_undo);

        }

        /**
         * Icons for all types of SuperToasts with a light background.
         */
        public static class Light {

//            public static final int EDIT = (R.drawable.icon_light_edit);
//            public static final int EXIT = (R.drawable.icon_light_exit);
//            public static final int INFO = (R.drawable.icon_light_info);
//            public static final int REDO = (R.drawable.icon_light_redo);
//            public static final int REFRESH = (R.drawable.icon_light_refresh);
//            public static final int SAVE = (R.drawable.icon_light_save);
//            public static final int SHARE = (R.drawable.icon_light_share);
//            public static final int UNDO = (R.drawable.icon_light_undo);

        }

    }

    /**
     * Durations for all types of SuperToasts.
     */
    public static class Duration {

        public static final int VERY_SHORT = (1500);
        public static final int SHORT = (2000);
        public static final int MEDIUM = (2750);
        public static final int LONG = (3500);
        public static final int EXTRA_LONG = (4500);

    }

    /**
     * Text sizes for all types of SuperToasts.
     */
    public static class TextSize {

        public static final int EXTRA_SMALL = (12);
        public static final int SMALL = (14);
        public static final int MEDIUM = (16);
        public static final int LARGE = (18);

    }

    /**
     * Types for SuperActivityToasts and SuperCardToasts.
     */
    public enum Type {

        /**
         * Standard type used for displaying messages.
         */
        STANDARD,

        /**
         * Progress type used for showing progress.
         */
        PROGRESS,

        /**
         * Progress type used for showing progress.
         */
        PROGRESS_HORIZONTAL,

        /**
         * Button type used for receiving click actions.
         */
        BUTTON

    }

    /**
     * Positions for icons used in all types of SuperToasts.
     */
    public enum IconPosition {

        /**
         * Set the icon to the left of the text.
         */
        LEFT,

        /**
         * Set the icon to the right of the text.
         */
        RIGHT,

        /**
         * Set the icon on top of the text.
         */
        TOP,

        /**
         * Set the icon on the bottom of the text.
         */
        BOTTOM

    }

    private Animations mAnimations = Animations.FADE;
    private Context mContext;
    private int mGravity = Gravity.BOTTOM | Gravity.CENTER;
    private int mDuration = Duration.SHORT;
    private Typeface mTypefaceStyle;
    private int mBackground;
    private int mXOffset = 0;
    private int mYOffset = 0;
    private LinearLayout mRootLayout;
    private OnDismissListener mOnDismissListener;
    private TextView mMessageTextView;
    private View mToastView;
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mWindowManagerParams;


    protected SuperToast(Context context) {

          if (context == null) {

            throw new IllegalArgumentException(TAG + ERROR_CONTEXTNULL);

        }

        this.mContext = context;

        mYOffset = context.getResources().getDimensionPixelSize(R.dimen.toast_hover);

        final LayoutInflater layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        mToastView = layoutInflater.inflate(R.layout.supertoast, null);

        mWindowManager = (WindowManager) mToastView.getContext()
                .getApplicationContext().getSystemService(Context.WINDOW_SERVICE);

        mRootLayout = (LinearLayout) mToastView.findViewById(R.id.root_layout);

        mMessageTextView = (TextView)
                mToastView.findViewById(R.id.message_textview);

    }

    /**
     * Instantiates a new {@value #TAG}.
     *
     * @param context {@link android.content.Context}
     */
//    public SuperToast(Context context) {
//
//        if (context == null) {
//
//            throw new IllegalArgumentException(TAG + ERROR_CONTEXTNULL);
//
//        }
//
//        this.mContext = context;
//
//        mYOffset = context.getResources().getDimensionPixelSize(R.dimen.toast_hover);
//
//        final LayoutInflater layoutInflater = (LayoutInflater) context
//                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//
//        mToastView = layoutInflater.inflate(R.layout.supertoast, null);
//
//        mWindowManager = (WindowManager) mToastView.getContext()
//                .getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
//
//        mRootLayout = (LinearLayout) mToastView.findViewById(R.id.root_layout);
//
//        mMessageTextView = (TextView)
//                mToastView.findViewById(R.id.message_textview);
//
//    }


    public SuperToast(Context context, Style style) {

        if (context == null) {

            throw new IllegalArgumentException(TAG + ERROR_CONTEXTNULL);

        }

        this.mContext = context;

//        mYOffset = context.getResources().getDimensionPixelSize(R.dimen.toast_hover);

        final LayoutInflater layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        mToastView = layoutInflater.inflate(R.layout.supertoast, null);

        mWindowManager = (WindowManager) mToastView.getContext()
                .getApplicationContext().getSystemService(Context.WINDOW_SERVICE);

        mRootLayout = (LinearLayout)
                mToastView.findViewById(R.id.root_layout);

        mMessageTextView = (TextView)
                mToastView.findViewById(R.id.message_textview);

        this.setStyle(style);

    }

    /**
     * Shows the {@value #TAG}. If another {@value #TAG} is showing than
     * this one will be added to a queue and shown when the previous {@value #TAG}
     * is dismissed.
     */
    public void show() {

        mWindowManagerParams = new WindowManager.LayoutParams();

        mWindowManagerParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mWindowManagerParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mWindowManagerParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
        mWindowManagerParams.format = PixelFormat.TRANSLUCENT;
        mWindowManagerParams.windowAnimations = getAnimation();
        mWindowManagerParams.type = WindowManager.LayoutParams.TYPE_TOAST;
        mWindowManagerParams.gravity = mGravity;
        mWindowManagerParams.x = mXOffset;
        mWindowManagerParams.y = mYOffset;

        ManagerSuperToast.getInstance().add(this);

    }

    /**
     * Sets the message text of the {@value #TAG}.
     *
     * @param text {@link CharSequence}
     */
    public void setText(CharSequence text) {

        mMessageTextView.setText(text);

    }

    /**
     * Returns the message text of the {@value #TAG}.
     *
     * @return {@link CharSequence}
     */
    public CharSequence getText() {

        return mMessageTextView.getText();

    }

    /**
     * Sets the message typeface style of the {@value #TAG}.
     *
     * @param typeface {@link android.graphics.Typeface} int
     */
    public void setTypefaceStyle(Typeface typeface) {

        mTypefaceStyle = typeface;

        mMessageTextView.setTypeface(typeface);

    }

    /**
     * Returns the message typeface style of the {@value #TAG}.
     *
     * @return {@link android.graphics.Typeface} int
     */
    public Typeface getTypefaceStyle() {

        return mTypefaceStyle;

    }

    /**
     * Sets the message text color of the {@value #TAG}.
     *
     * @param textColor {@link android.graphics.Color}
     */
    public void setTextColor(int textColor) {

        mMessageTextView.setTextColor(textColor);

    }

    /**
     * Returns the message text color of the {@value #TAG}.
     *
     * @return int
     */
    public int getTextColor() {

        return mMessageTextView.getCurrentTextColor();

    }

    /**
     * Sets the text size of the {@value #TAG} message.
     *
     * @param textSize int
     */
    public void setTextSize(int textSize) {

        mMessageTextView.setTextSize(textSize);

    }

    /**
     * Returns the text size of the {@value #TAG} message in pixels.
     *
     * @return float
     */
    public float getTextSize() {

        return mMessageTextView.getTextSize();

    }


    public void setDuration(int duration) {

        if(duration > Duration.EXTRA_LONG) {

            this.mDuration = Duration.EXTRA_LONG;

        } else {

            this.mDuration = duration;

        }

    }

    /**
     * Returns the duration of the {@value #TAG}.
     *
     * @return int
     */
    public int getDuration() {

        return this.mDuration;

    }


    public void setIcon(int iconResource, IconPosition iconPosition) {

        if (iconPosition == IconPosition.BOTTOM) {

            mMessageTextView.setCompoundDrawablesWithIntrinsicBounds(null, null,
                    null, mContext.getResources().getDrawable(iconResource));

        } else if (iconPosition == IconPosition.LEFT) {

            mMessageTextView.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources()
                    .getDrawable(iconResource), null, null, null);

        } else if (iconPosition == IconPosition.RIGHT) {

            mMessageTextView.setCompoundDrawablesWithIntrinsicBounds(null, null,
                    mContext.getResources().getDrawable(iconResource), null);

        } else if (iconPosition == IconPosition.TOP) {

            mMessageTextView.setCompoundDrawablesWithIntrinsicBounds(null,
                    mContext.getResources().getDrawable(iconResource), null, null);

        }

    }

    public void setBackground(int background) {

        this.mBackground = background;

        mRootLayout.setBackgroundResource(background);

    }

    /**
     * Returns the background resource of the {@value #TAG}.
     *
     * @return int
     */
    public int getBackground() {

        return this.mBackground;

    }

    /**
     * Sets the gravity of the {@value #TAG} along with x and y offsets.
     *
     * @param gravity {@link android.view.Gravity} int
     * @param xOffset int
     * @param yOffset int
     */
    public void setGravity(int gravity, int xOffset, int yOffset) {

        this.mGravity = gravity;
        this.mXOffset = xOffset;
        this.mYOffset = yOffset;

    }


    public void setAnimations(Animations animations) {

        this.mAnimations = animations;

    }

    public Animations getAnimations() {

        return this.mAnimations;

    }


    public void setOnDismissListener(OnDismissListener onDismissListener) {

        this.mOnDismissListener = onDismissListener;

    }

    public OnDismissListener getOnDismissListener() {

        return mOnDismissListener;

    }

    /**
     * Dismisses the {@value #TAG}.
     */
    public void dismiss() {

        ManagerSuperToast.getInstance().removeSuperToast(this);

    }

    /**
     * Returns the {@value #TAG} message textview.
     *
     * @return {@link android.widget.TextView}
     */
    public TextView getTextView() {

        return mMessageTextView;

    }

    /**
     * Returns the {@value #TAG} view.
     *
     * @return {@link android.view.View}
     */
    public View getView() {

        return mToastView;

    }

    /**
     * Returns true if the {@value #TAG} is showing.
     *
     * @return boolean
     */
    public boolean isShowing() {

        return mToastView != null && mToastView.isShown();

    }

    /**
     * Returns the window manager that the {@value #TAG} is attached to.
     *
     * @return {@link android.view.WindowManager}
     */
    public WindowManager getWindowManager() {

        return mWindowManager;

    }

    /**
     * Returns the window manager layout params of the {@value #TAG}.
     *
     * @return {@link android.view.WindowManager.LayoutParams}
     */
    public WindowManager.LayoutParams getWindowManagerParams() {

        return mWindowManagerParams;

    }

    /**
     * Private method used to return a specific animation for a animations enum
     */
    private int getAnimation() {

        if (mAnimations == Animations.FLYIN) {

            return android.R.style.Animation_Translucent;

        } else if (mAnimations == Animations.SCALE) {

            return android.R.style.Animation_Dialog;

        } else if (mAnimations == Animations.POPUP) {

            return android.R.style.Animation_InputMethod;

        } else {

            return android.R.style.Animation_Toast;

        }

    }

    /**
     * Private method used to set a default style to the {@value #TAG}
     */
    private void setStyle(Style style) {

        this.setAnimations(style.animations);
//        this.setTypefaceStyle(Constante);
        this.setTextColor(style.textColor);
        this.setBackground(style.background);

    }


    public static SuperToast create(Context context, CharSequence textCharSequence,
                                    int durationInteger) {

        SuperToast superToast = new SuperToast(context);
        superToast.setText(textCharSequence);
        superToast.setDuration(durationInteger);

        return superToast;

    }

    public static SuperToast create(Context context, CharSequence textCharSequence,
                                    int durationInteger, Animations animations) {

        final SuperToast superToast = new SuperToast(context);
        superToast.setText(textCharSequence);
        superToast.setDuration(durationInteger);
        superToast.setAnimations(animations);

        return superToast;

    }

    public static SuperToast create(Context context, CharSequence textCharSequence, int durationInteger, Style style) {

        final SuperToast superToast = new SuperToast(context);
        superToast.setText(textCharSequence);
        superToast.setDuration(durationInteger);
        superToast.setStyle(style);

        return superToast;

    }

    public static void cancelAllSuperToasts() {

        ManagerSuperToast.getInstance().cancelAllSuperToasts();

    }

}


