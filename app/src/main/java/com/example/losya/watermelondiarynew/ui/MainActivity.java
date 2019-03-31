package com.example.losya.watermelondiarynew.ui;

import android.content.Context;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.losya.watermelondiarynew.R;
import com.example.losya.watermelondiarynew.bean.DiaryBean;
import com.example.losya.watermelondiarynew.db.DiaryDatabaseHelper;
import com.example.losya.watermelondiarynew.event.StartUpdateDiaryEvent;
import com.example.losya.watermelondiarynew.utils.AppManager;
import com.example.losya.watermelondiarynew.utils.GetDate;
import com.example.losya.watermelondiarynew.utils.SpHelper;
import com.example.losya.watermelondiarynew.utils.StatusBarCompat;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.common_iv_back)
    ImageView mCommonIvBack;
    @Bind(R.id.common_tv_title)
    TextView mCommonTvTitle;
    @Bind(R.id.common_iv_test)
    ImageView mCommonIvTest;
    @Bind(R.id.common_title_ll)
    LinearLayout mCommonTitleLl;
    @Bind(R.id.main_iv_circle)
    ImageView mMainIvCircle;
    @Bind(R.id.main_tv_date)
    TextView mMainTvDate;
    @Bind(R.id.main_tv_content)
    TextView mMContentainTv;
    @Bind(R.id.item_ll_control)
    LinearLayout mItemLlControl;
    @Bind(R.id.numberPadLayout)
    LinearLayout lay;

    @Bind(R.id.main_rv_show_diary)
    RecyclerView mMainRvShowDiary;
    @Bind(R.id.main_fab_enter_edit)
    FloatingActionButton mMainFabEnterEdit;
    @Bind(R.id.main_fab_set_edit)
    FloatingActionButton mMainFabSetEdit;
    @Bind(R.id.main_rl_main)
    RelativeLayout mMainRlMain;
    @Bind(R.id.item_first)
    LinearLayout mItemFirst;
    @Bind(R.id.main_ll_main)
    LinearLayout mMainLlMain;
    private List<DiaryBean> mDiaryBeanList;

    private DiaryDatabaseHelper mHelper;

    private static String IS_WRITE = "true";

    private int mEditPosition = -1;

    private boolean isWrite = false;
    private static TextView mTvTest;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        boolean themeType = (boolean) SP.getBoolean("btheme", Boolean.parseBoolean("false"));
        RelativeLayout view = (RelativeLayout) findViewById(R.id.main_rl_main);
//        RelativeLayout bbtheme = new RelativeLayout(this);

        if (themeType) {
            view.setBackgroundColor(Color.parseColor("#000000"));
        }
        else {

            view.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }

        AppManager.getAppManager().addActivity(this);
        ButterKnife.bind(this);
        StatusBarCompat.compat(this, Color.parseColor("#161414"));
        mHelper = new DiaryDatabaseHelper(this, "Diary.db", null, 1);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        EventBus.getDefault().register(this);
        SpHelper spHelper = SpHelper.getInstance(this);
        getDiaryBeanList();

        initTitle();
        mMainRvShowDiary.setLayoutManager(new LinearLayoutManager(this));
        mMainRvShowDiary.setAdapter(new DiaryAdapter(this, mDiaryBeanList));
        mTvTest = new TextView(this);
        mTvTest.setText("hello world");
        mMainFabSetEdit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i;
                i =  new Intent(getApplicationContext(),MyPreferencesActivity.class);
                startActivity(i);
            }
        });

    }
    private void initTitle() {

        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String strUserName = SP.getString("username", "User");
        String downloadType = SP.getString("downloadType","1");

        switch (downloadType){
            case "1":
                mCommonTvTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
                mMainTvDate.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
                mMContentainTv.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);

                break;
            case "2":
                mCommonTvTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP,23);
                mMainTvDate.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
                mMContentainTv.setTextSize(TypedValue.COMPLEX_UNIT_SP,22);

            break;
            case "3":
                mCommonTvTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP,30);
                mMainTvDate.setTextSize(TypedValue.COMPLEX_UNIT_SP,26);
                mMContentainTv.setTextSize(TypedValue.COMPLEX_UNIT_SP,28);

            break;
            default:
                mCommonTvTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
                mMainTvDate.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
                mMContentainTv.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
        }
        mMainTvDate.setText("Today " + GetDate.getDate());
        mCommonTvTitle.setText(strUserName+"\'s "+"Diary");
        mCommonIvBack.setVisibility(View.INVISIBLE);
        mCommonIvTest.setVisibility(View.INVISIBLE);

    }

    private List<DiaryBean> getDiaryBeanList() {

        mDiaryBeanList = new ArrayList<>();
        List<DiaryBean> diaryList = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = mHelper.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.query("Diary", null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                String date = cursor.getString(cursor.getColumnIndex("date"));
                String dateSystem = GetDate.getDate().toString();
                if (date.equals(dateSystem)) {
                    mMainLlMain.removeView(mItemFirst);
                    break;
                }
            } while (cursor.moveToNext());
        }


        if (cursor.moveToFirst()) {
            do {
                String date = cursor.getString(cursor.getColumnIndex("date"));
                String title = cursor.getString(cursor.getColumnIndex("title"));
                String content = cursor.getString(cursor.getColumnIndex("content"));
                String tag = cursor.getString(cursor.getColumnIndex("tag"));
                mDiaryBeanList.add(new DiaryBean(date, title, content, tag));
            } while (cursor.moveToNext());
        }
        cursor.close();

        for (int i = mDiaryBeanList.size() - 1; i >= 0; i--) {
            diaryList.add(mDiaryBeanList.get(i));
        }

        mDiaryBeanList = diaryList;
        return mDiaryBeanList;
    }

    @Subscribe
    public void startUpdateDiaryActivity(StartUpdateDiaryEvent event) {
        String title = mDiaryBeanList.get(event.getPosition()).getTitle();
        String content = mDiaryBeanList.get(event.getPosition()).getContent();
        String tag = mDiaryBeanList.get(event.getPosition()).getTag();
        UpdateDiaryActivity.startActivity(this, title, content, tag);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @OnClick(R.id.main_fab_enter_edit)
    public void onClick() {
        AddDiaryActivity.startActivity(this);
    }

//    @OnClick(R.id.main_fab_set_edit)
//    public void onClick() { MyPreferencesActivity.startActivity(this);}


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AppManager.getAppManager().AppExit(this);
    }
}