package com.example.sunqi.securityking.permission.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.sunqi.securityking.R;
import com.example.sunqi.securityking.utils.ViewUtils;


/**
 * 魅族flyme6后台运行权限的引导
 */
public class Flyme6AutoSetupPermissionTutorialActivity extends Activity {

    private final static int START_DELAYED_ANIMATION_TIME = 800;
    private final static int TRANSLATE_FINGER_DURATION = 500;
    private final static float TRANSLATE_FINGER_MORE_SIZE = 25f;
    private final static int FINGER_CLICK_DURATION = 300;
    private final static int TRANSLATE_GETDOWN_DURATION = 1000;
    private final static float TRANSLATE_GETDOWN_MORE_SIZE = 60f;
    private final static int ALPHA_SHOW_OPEN_VIEW_DURATION = 1000;

    private View mRootView = null;
    private ImageView mFingerImgv;
    private ImageView mArrowImgv;
    private ImageView mOpenImgv;
    private RelativeLayout mGetDownLayout;
    private Handler mHandler = new Handler();

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.nipt_container:
                    finish();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.permission_flyme_tutorail_layout);

        initView();
        initAnimation();
    }

    private void initView() {
        mRootView = findViewById(R.id.nipt_container);
        mRootView.setOnClickListener(mOnClickListener);

        mFingerImgv = (ImageView)findViewById(R.id.img_finger);
        mArrowImgv = (ImageView)findViewById(R.id.img_arrow);
        mOpenImgv = (ImageView)findViewById(R.id.img_open);

        mGetDownLayout = (RelativeLayout)findViewById(R.id.rv_get_down);
    }

    private void initAnimation() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                translateFinger();
            }
        }, START_DELAYED_ANIMATION_TIME);
    }

    private void translateFinger() {
        if (mFingerImgv == null) {
            return;
        }
        int values1 = 0;
        int values2 = ViewUtils.dip2px(Flyme6AutoSetupPermissionTutorialActivity.this, TRANSLATE_FINGER_MORE_SIZE);
        ObjectAnimator animator = ObjectAnimator.ofFloat(mFingerImgv, "translationY", values1, -values2);
        animator.setDuration(TRANSLATE_FINGER_DURATION);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                fingerclickAnimation();
            }
        });
        animator.start();
    }

    private void fingerclickAnimation() {
        mFingerImgv.setBackgroundResource(R.drawable.finger_click);
        mArrowImgv.setBackgroundResource(R.drawable.permissions_pack_up_arrow1);

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mFingerImgv != null) {
                    mFingerImgv.setBackgroundResource(R.drawable.finger_normal);
                    mArrowImgv.setBackgroundResource(R.drawable.permissions_pack_up_arrow2);

                    translateGetDown();
                }
            }
        }, FINGER_CLICK_DURATION);
    }

    private void translateGetDown() {
        if (mGetDownLayout == null) {
            return;
        }
        mGetDownLayout.setVisibility(View.VISIBLE);
        int values1 = ViewUtils.dip2px(Flyme6AutoSetupPermissionTutorialActivity.this, TRANSLATE_GETDOWN_MORE_SIZE);
        int values2 = 0;
        ObjectAnimator animator = ObjectAnimator.ofFloat(mGetDownLayout, "translationY", -values1, values2);
        animator.setDuration(TRANSLATE_GETDOWN_DURATION);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                alphaShowOpenView();
            }
        });
        animator.start();
    }

    private void alphaShowOpenView() {
        if (mOpenImgv == null) {
            return;
        }
        mOpenImgv.setVisibility(View.VISIBLE);
        ObjectAnimator animator = ObjectAnimator.ofFloat(mOpenImgv, "alpha", 0f, 1f);
        animator.setDuration(ALPHA_SHOW_OPEN_VIEW_DURATION);
        animator.start();
    }
}

