package com.app.court.residemenu;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Build;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;


import com.app.court.R;
import com.app.court.activities.DockActivity;
import com.app.court.global.SideMenuDirection;
import com.nineoldandroids.view.ViewHelper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


/**
 * User: special
 * Date: 13-12-10
 * Time: 下午10:44
 * Mail: specialcyci@gmail.com
 */
public class ResideMenu extends FrameLayout {


    private static final int PRESSED_MOVE_HORIZONTAL = 2;
    private static final int PRESSED_DOWN = 3;
    private static final int PRESSED_DONE = 4;
    private static final int PRESSED_MOVE_VERTICAL = 5;

    private ImageView imageViewShadow;
    private ImageView imageViewBackground;

  /*  private LinearLayout layoutLeftMenu;
    private LinearLayout layoutRightMenu;*/

    private LinearLayout rightMenuView;
    private LinearLayout leftMenuView;
    private View scrollViewMenu;
    /**
     * Current attaching activity.
     */
    private DockActivity activity;
    /**
     * The DecorView of current activity.
     */
    private ViewGroup viewDecor;
    private TouchDisableView viewActivity;
    /**
     * The flag of menu opening status.
     */
    private boolean isOpened;
    private float shadowAdjustScaleX;
    private float shadowAdjustScaleY;
    /**
     * Views which need stop to intercept touch events.
     */
    private List<View> ignoredViews;

    private DisplayMetrics displayMetrics = new DisplayMetrics();
    private OnMenuListener menuListener;
    private float lastRawX;
    private boolean isInIgnoredView = false;

    private int pressedState = PRESSED_DOWN;
    private List<Integer> disabledSwipeDirection = new ArrayList<Integer>();
    // Valid scale factor is between 0.0f and 1.0f.
    private float mScaleValue;

    private boolean mUse3D;
    private static final int ROTATE_Y_ANGLE = 10;

    private String scaleDirection;

    private DockActivity context;

    //private BlurTask blurTask;



    public ResideMenu(DockActivity context) {
        super(context);
        initViews(context, -1, -1);

        this.context = context;

        //blurTask = new BlurTask(context, i, R.id.container).execute();

    }

    /**
     * This constructor provides you to create menus with your own custom
     * layouts, but if you use custom menu then do not call addMenuItem because
     * it will not be able to find default views
     */
    public ResideMenu(Context context, int customLeftMenuId,
                      int customRightMenuId) {
        super(context);
        initViews(context, customLeftMenuId, customRightMenuId);
    }

    private void initViews(Context context, int customLeftMenuId,
                           int customRightMenuId) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.residemenu_custom, this);


        leftMenuView = (LinearLayout) findViewById(R.id.leftMenuView);
        rightMenuView = (LinearLayout) findViewById(R.id.rightMenuView);


        imageViewShadow = (ImageView) findViewById(R.id.iv_shadow);
        imageViewBackground = (ImageView) findViewById(R.id.iv_background);


    }
    @Override
    protected boolean fitSystemWindows(Rect insets) {
        setMyPadding(insets);
        return true;
    }

    @Override
    public WindowInsets onApplyWindowInsets(WindowInsets insets) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            Rect rect = new Rect(
                    insets.getSystemWindowInsetLeft(),
                    insets.getSystemWindowInsetTop(),
                    insets.getSystemWindowInsetRight(),
                    insets.getSystemWindowInsetBottom()
            );
            setMyPadding(rect);
            return insets.consumeSystemWindowInsets();
        }
        return super.onApplyWindowInsets(insets);
    }

    private void setMyPadding(Rect rect) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (hasNavBar()) {
                WindowManager manager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
                switch (manager.getDefaultDisplay().getRotation()) {
                    case Surface.ROTATION_90:
                        rect.right += viewActivity.getPaddingRight() + getNavBarWidth();
                        break;
                    case Surface.ROTATION_180:
                        rect.top += viewActivity.getPaddingTop() + getNavBarHeight();
                        break;
                    case Surface.ROTATION_270:
                        rect.left += viewActivity.getPaddingLeft() + getNavBarWidth();
                        break;
                    default:
                        rect.bottom += viewActivity.getPaddingBottom() + getNavBarHeight();
                }
            }
        }
        setPadding(rect.left, rect.top, rect.right, rect.bottom);
    }

    private int getNavBarWidth() {
        return getNavBarDimen("navigation_bar_width");
    }

    private int getNavBarHeight() {
        return getNavBarDimen("navigation_bar_height");
    }

    private int getNavBarDimen(String resourceString) {
        Resources r = getResources();
        int id = r.getIdentifier(resourceString, "dimen", "android");
        if (id > 0) {
            return r.getDimensionPixelSize(id);
        } else {
            return 0;
        }
    }

    /**
     * check is system has navigation bar or not* http://stackoverflow.com/a/29120269/3758898
     *
     * @return true if navigation bar is present or false
     */
    boolean hasNavBar() {
        try {
            // check for emulator
            Class<?> serviceManager = Class.forName("android.os.ServiceManager");
            IBinder serviceBinder = (IBinder) serviceManager.getMethod("getService", String.class).invoke(serviceManager, "window");
            Class<?> stub = Class.forName("android.view.IWindowManager$Stub");
            Object windowManagerService = stub.getMethod("asInterface", IBinder.class).invoke(stub, serviceBinder);
            Method hasNavigationBar = windowManagerService.getClass().getMethod("hasNavigationBar");
            return (boolean) hasNavigationBar.invoke(windowManagerService);
        } catch (ClassNotFoundException
                | ClassCastException
                | NoSuchMethodException
                | SecurityException
                | IllegalAccessException
                | IllegalArgumentException
                | InvocationTargetException e) {

            boolean hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
            boolean hasHomeKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_HOME);
            return !hasHomeKey && !hasBackKey;
        }}


   /* @Override
    protected boolean fitSystemWindows(Rect insets) {
        // Applies the content insets to the view's padding, consuming that
        // content (modifying the insets to be 0),
        // and returning true. This behavior is off by default and can be
        // enabled through setFitsSystemWindows(boolean)
        // in api14+ devices.

        // This is added to fix soft navigationBar's overlapping to content above LOLLIPOP
        int bottomPadding = viewActivity.getPaddingBottom() + insets.bottom;
        boolean hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
        boolean hasHomeKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_HOME);
        if (!hasBackKey || !hasHomeKey) {//there's a navigation bar
            bottomPadding += getNavigationBarHeight();
        }

        this.setPadding(viewActivity.getPaddingLeft() + insets.left,
                viewActivity.getPaddingTop() + insets.top,
                viewActivity.getPaddingRight() + insets.right,
                0);
        insets.left = insets.top = insets.right = insets.bottom = 0;
        return true;
    }*/


    private int getNavigationBarHeight() {
        Resources resources = getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    /**
     * Set up the activity;
     *
     * @param activity
     */
    public void attachToActivity(DockActivity activity) {
        initValue(activity);
        setShadowAdjustScaleXByOrientation();
        viewDecor.addView(this, 0);
    }

    private void initValue(DockActivity activity) {
        try {
            this.activity = activity;
            //leftMenuItems = new ArrayList<ResideMenuItem>();
            //rightMenuItems = new ArrayList<ResideMenuItem>();
            ignoredViews = new ArrayList<View>();
            viewDecor = (ViewGroup) activity.getWindow().getDecorView();
            viewActivity = new TouchDisableView(this.activity);
            View mContent = viewDecor.getChildAt(0);
            viewDecor.removeViewAt(0);
            viewActivity.setContent(mContent);
            addView(viewActivity);


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

   /* private void blurView(View view) {
        BlurKit.init(activity);
        BlurKit blurKit = BlurKit.getInstance();
        blurKit.blur(view, 25);
    }
*/

    private void setShadowAdjustScaleXByOrientation() {
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            shadowAdjustScaleX = 0.034f;
            shadowAdjustScaleY = 0.12f;
        } else if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            shadowAdjustScaleX = 0.06f;
            shadowAdjustScaleY = 0.07f;
        }
    }

    /**
     * Set the background image of menu;
     *
     * @param imageResource
     */
    public void setBackground(int imageResource) {
        imageViewBackground.setImageResource(imageResource);
    }

    /**
     * The visibility of the shadow under the activity;
     *
     * @param isVisible
     */


    /**
     * Add a single item to the left menu;
     * <p/>
     * WARNING: It will be removed from v2.0.
     *
     * @param menuItem
     */

    public void addMenuItem(android.support.v4.app.Fragment sideMenuView, String tag, String direction) {


        android.support.v4.app.FragmentTransaction transaction = activity.getSupportFragmentManager()
                .beginTransaction();


        if (direction.equals(SideMenuDirection.LEFT.getValue())) {


            //this.leftMenuItems.add(menuItem);
            transaction.replace(leftMenuView.getId(), sideMenuView);
            transaction.commit();
        }

        if (direction.equals(SideMenuDirection.RIGHT.getValue())) {


            //this.leftMenuItems.add(menuItem);
            transaction.replace(rightMenuView.getId(), sideMenuView, tag);
            transaction.commit();

        }


    }

    /**
     * WARNING: It will be removed from v2.0.
     *
     * @param menuItems
     */


    /**
     * WARNING: It will be removed from v2.0.
     *
     * @return
     */
    /*@Deprecated
    public List<ResideMenuItem> getMenuItems() {
        return leftMenuItems;
    }*/

    /**
     * Return instances of menu items;
     *
     * @return
     */
  /*  public List<ResideMenuItem> getMenuItems(int direction) {
        if (direction == DIRECTION_LEFT)
            return leftMenuItems;
        else
            return rightMenuItems;
    }*/

    /**
     * If you need to do something on closing or opening menu,
     * set a listener here.
     *
     * @return
     */
    public void setMenuListener(OnMenuListener menuListener) {
        this.menuListener = menuListener;
    }


    public OnMenuListener getMenuListener() {
        return menuListener;
    }

    /**
     * Show the menu;
     */
    public void openMenu(final String direction) {


        try {
            setScaleDirection(direction);

            if (direction.equals(SideMenuDirection.LEFT.getValue())) {
                rightMenuView.setVisibility(View.GONE);
                leftMenuView.setVisibility(View.VISIBLE);
            } else {
                leftMenuView.setVisibility(View.GONE);
                rightMenuView.setVisibility(View.VISIBLE);
            }

            isOpened = true;
            AnimatorSet scaleDown_activity = buildScaleDownAnimation(viewActivity, mScaleValue, 0.84f);
            AnimatorSet scaleDown_shadow = buildScaleDownAnimation(imageViewShadow,
                    mScaleValue + shadowAdjustScaleX, mScaleValue + shadowAdjustScaleY);
            AnimatorSet alpha_menu = buildMenuAnimation(scrollViewMenu, 1.0f);
            scaleDown_shadow.addListener(animationListener);
            scaleDown_activity.playTogether(scaleDown_shadow);
            scaleDown_activity.playTogether(alpha_menu);
            scaleDown_activity.start();



        } catch (Exception e) {
            e.printStackTrace();
        }

        //Blurry.with(context).radius(13).sampling(2).onto((ViewGroup) viewActivity );

        /*Blurry.with(context)
                .radius(10)
                .sampling(2)
                .async()
                .onto(context.getMainContentFrame());*/
    }



    /**
     * Close the menu;
     */
    public void closeMenu() {


        try {
            isOpened = false;
            AnimatorSet scaleUp_activity = buildScaleUpAnimation(viewActivity, 1.0f, 1.0f);
            AnimatorSet scaleUp_shadow = buildScaleUpAnimation(imageViewShadow, 1.0f, 1.0f);
            AnimatorSet alpha_menu = buildMenuAnimation(scrollViewMenu, 0.0f);
            scaleUp_activity.addListener(animationListener);
            scaleUp_activity.playTogether(scaleUp_shadow);
            scaleUp_activity.playTogether(alpha_menu);
            scaleUp_activity.start();




        } catch (Exception e) {
            e.printStackTrace();
        }


        // context.getSupportFragmentManager().popBackStack("LeftSideMenuFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);
        //context.getSupportFragmentManager().popBackStack("RightSideMenuFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    @Deprecated
    public void setDirectionDisable(int direction) {
        disabledSwipeDirection.add(direction);
    }

    public void setSwipeDirectionDisable(int direction) {
        disabledSwipeDirection.add(direction);
    }

    private boolean isInDisableDirection(int direction) {
        return disabledSwipeDirection.contains(direction);
    }

    private void setScaleDirection(String direction) {
        try {
            int screenWidth = getScreenWidth();
            float pivotX;
            float pivotY = getScreenHeight() * 0.5f;

            if (direction.equals(SideMenuDirection.LEFT.getValue())) {
                scrollViewMenu = leftMenuView;
                pivotX = screenWidth * 1.5f;

            } else {
                scrollViewMenu = rightMenuView;
                pivotX = screenWidth * -0.5f;
            }

            ViewHelper.setPivotX(viewActivity, pivotX);
            ViewHelper.setPivotY(viewActivity, pivotY);
            ViewHelper.setPivotX(imageViewShadow, pivotX);
            ViewHelper.setPivotY(imageViewShadow, pivotY);
            scaleDirection = direction;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * return the flag of menu status;
     *
     * @return
     */
    public boolean isOpened() {
        return isOpened;
    }

    private OnClickListener viewActivityOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            if (isOpened()) closeMenu();
        }
    };

    private Animator.AnimatorListener animationListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animation) {
            if (isOpened()) {
                showScrollViewMenu(scrollViewMenu);
                if (menuListener != null)
                    menuListener.openMenu();
            }
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            // reset the view;
            if (isOpened()) {
                viewActivity.setTouchDisable(true);
                viewActivity.setOnClickListener(viewActivityOnClickListener);
            } else {
                viewActivity.setTouchDisable(false);
                viewActivity.setOnClickListener(null);


                //Blurry.delete(context.getMainContentFrame());


                //hideScrollViewMenu(leftMenuView);
                // hideScrollViewMenu(rightMenuView);
                if (menuListener != null)
                    menuListener.closeMenu();
            }
        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    };

    /**
     * A helper method to build scale down animation;
     *
     * @param target
     * @param targetScaleX
     * @param targetScaleY
     * @return
     */
    private AnimatorSet buildScaleDownAnimation(View target, float targetScaleX, float targetScaleY) {
        try {
            AnimatorSet scaleDown = new AnimatorSet();
            scaleDown.playTogether(
                    ObjectAnimator.ofFloat(target, "scaleX", targetScaleX),
                    ObjectAnimator.ofFloat(target, "scaleY", targetScaleY)
            );

            if (mUse3D) {
                int angle = scaleDirection == SideMenuDirection.LEFT.getValue() ? -ROTATE_Y_ANGLE : ROTATE_Y_ANGLE;
                scaleDown.playTogether(ObjectAnimator.ofFloat(target, "rotationY", angle));
            }

            scaleDown.setInterpolator(AnimationUtils.loadInterpolator(activity,
                    android.R.anim.decelerate_interpolator));
            scaleDown.setDuration(250);
            return scaleDown;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * A helper method to build scale up animation;
     *
     * @param target
     * @param targetScaleX
     * @param targetScaleY
     * @return
     */
    private AnimatorSet buildScaleUpAnimation(View target, float targetScaleX, float targetScaleY) {
        try {
            AnimatorSet scaleUp = new AnimatorSet();
            scaleUp.playTogether(
                    ObjectAnimator.ofFloat(target, "scaleX", targetScaleX),
                    ObjectAnimator.ofFloat(target, "scaleY", targetScaleY)
            );

            if (mUse3D) {
                scaleUp.playTogether(ObjectAnimator.ofFloat(target, "rotationY", 0));
            }

            scaleUp.setDuration(250);
            return scaleUp;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private AnimatorSet buildMenuAnimation(View target, float alpha) {

        AnimatorSet alphaAnimation = new AnimatorSet();
        alphaAnimation.playTogether(
                ObjectAnimator.ofFloat(target, "alpha", alpha)
        );

        alphaAnimation.setDuration(250);
        return alphaAnimation;
    }

    /**
     * If there were some view you don't want reside menu
     * to intercept their touch event, you could add it to
     * ignored views.
     *
     * @param v
     */
    public void addIgnoredView(View v) {
        ignoredViews.add(v);
    }

    /**
     * Remove a view from ignored views;
     *
     * @param v
     */
    public void removeIgnoredView(View v) {
        ignoredViews.remove(v);
    }

    /**
     * Clear the ignored view list;
     */
    public void clearIgnoredViewList() {
        ignoredViews.clear();
    }

    /**
     * If the motion event was relative to the view
     * which in ignored view list,return true;
     *
     * @param ev
     * @return
     */
    private boolean isInIgnoredView(MotionEvent ev) {
        Rect rect = new Rect();
        for (View v : ignoredViews) {
            v.getGlobalVisibleRect(rect);
            if (rect.contains((int) ev.getX(), (int) ev.getY()))
                return true;
        }
        return false;
    }

    /*private void setScaleDirectionByRawX(float currentRawX) {
        if (currentRawX < lastRawX)
            setScaleDirection(DIRECTION_RIGHT);
        else
            setScaleDirection(DIRECTION_LEFT);
    }

    private float getTargetScale(float currentRawX) {
        float scaleFloatX = ((currentRawX - lastRawX) / getScreenWidth()) * 0.75f;
        scaleFloatX = scaleDirection == DIRECTION_RIGHT ? -scaleFloatX : scaleFloatX;

        float targetScale = ViewHelper.getScaleX(viewActivity) - scaleFloatX;
        targetScale = targetScale > 1.0f ? 1.0f : targetScale;
        targetScale = targetScale < 0.5f ? 0.5f : targetScale;
        return targetScale;
    }*/

    private float lastActionDownX, lastActionDownY;

   /* @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        float currentActivityScaleX = ViewHelper.getScaleX(viewActivity);
        if (currentActivityScaleX == 1.0f)
            setScaleDirectionByRawX(ev.getRawX());

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastActionDownX = ev.getX();
                lastActionDownY = ev.getY();
                isInIgnoredView = isInIgnoredView(ev) && !isOpened();
                pressedState = PRESSED_DOWN;
                break;

            case MotionEvent.ACTION_MOVE:
                if (isInIgnoredView || isInDisableDirection(scaleDirection))
                    break;

                if (pressedState != PRESSED_DOWN &&
                        pressedState != PRESSED_MOVE_HORIZONTAL)
                    break;

                int xOffset = (int) (ev.getX() - lastActionDownX);
                int yOffset = (int) (ev.getY() - lastActionDownY);

                if (pressedState == PRESSED_DOWN) {
                    if (yOffset > 25 || yOffset < -25) {
                        pressedState = PRESSED_MOVE_VERTICAL;
                        break;
                    }
                    if (xOffset < -50 || xOffset > 50) {
                        pressedState = PRESSED_MOVE_HORIZONTAL;
                        ev.setAction(MotionEvent.ACTION_CANCEL);
                    }
                } else if (pressedState == PRESSED_MOVE_HORIZONTAL) {
                    if (currentActivityScaleX < 0.95)
                        showScrollViewMenu(scrollViewMenu);

                    float targetScale = getTargetScale(ev.getRawX());
                    if (mUse3D) {
                        int angle = scaleDirection == DIRECTION_LEFT ? -ROTATE_Y_ANGLE : ROTATE_Y_ANGLE;
                        angle *= (1 - targetScale) * 2;
                        ViewHelper.setRotationY(viewActivity, angle);

                        ViewHelper.setScaleX(imageViewShadow, targetScale - shadowAdjustScaleX);
                        ViewHelper.setScaleY(imageViewShadow, targetScale - shadowAdjustScaleY);
                    } else {
                        ViewHelper.setScaleX(imageViewShadow, targetScale + shadowAdjustScaleX);
                        ViewHelper.setScaleY(imageViewShadow, targetScale + shadowAdjustScaleY);
                    }
                    ViewHelper.setScaleX(viewActivity, targetScale);
                    ViewHelper.setScaleY(viewActivity, targetScale);
                    ViewHelper.setAlpha(scrollViewMenu, (1 - targetScale) * 2.0f);

                    lastRawX = ev.getRawX();
                    return true;
                }

                break;

            case MotionEvent.ACTION_UP:

                if (isInIgnoredView) break;
                if (pressedState != PRESSED_MOVE_HORIZONTAL) break;

                pressedState = PRESSED_DONE;
                if (isOpened()) {
                    if (currentActivityScaleX > 0.56f)
                        closeMenu();
                    else
                        openMenu(scaleDirection);
                } else {
                    if (currentActivityScaleX < 0.94f) {
                        openMenu(scaleDirection);
                    } else {
                        closeMenu();
                    }
                }

                break;

        }
        lastRawX = ev.getRawX();
        return super.dispatchTouchEvent(ev);
    }*/

    public int getScreenHeight() {
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    public int getScreenWidth() {
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    public void setScaleValue(float scaleValue) {
        this.mScaleValue = scaleValue;
    }

    public void setUse3D(boolean use3D) {
        mUse3D = use3D;
    }

    public interface OnMenuListener {

        /**
         * This method will be called at the finished time of opening menu animations.
         */
        public void openMenu();

        /**
         * This method will be called at the finished time of closing menu animations.
         */
        public void closeMenu();

    }

    private void showScrollViewMenu(View scrollViewMenu) {
        if (scrollViewMenu != null && scrollViewMenu.getParent() == null) {
            addView(scrollViewMenu);
        }
    }

    private void hideScrollViewMenu(View scrollViewMenu) {
        if (scrollViewMenu != null && scrollViewMenu.getParent() != null) {
            removeView(scrollViewMenu);
        }
    }
}
