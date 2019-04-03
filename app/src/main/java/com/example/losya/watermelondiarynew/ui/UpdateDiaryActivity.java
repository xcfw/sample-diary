package com.example.losya.watermelondiarynew.ui;

import android.content.*;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.widget.*;

import com.example.losya.watermelondiarynew.R;
import com.example.losya.watermelondiarynew.db.DiaryDatabaseHelper;
import com.example.losya.watermelondiarynew.utils.AppManager;
import com.example.losya.watermelondiarynew.utils.GetDate;
import com.example.losya.watermelondiarynew.utils.StatusBarCompat;
import com.example.losya.watermelondiarynew.widget.LinedEditText;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cc.trity.floatingactionbutton.FloatingActionButton;
import cc.trity.floatingactionbutton.FloatingActionsMenu;


public class UpdateDiaryActivity extends AppCompatActivity {

    @Bind(R.id.update_diary_tv_date)
    TextView mUpdateDiaryTvDate;
    @Bind(R.id.update_diary_et_title)
    EditText mUpdateDiaryEtTitle;
    @Bind(R.id.update_diary_et_content)
    LinedEditText mUpdateDiaryEtContent;
    @Bind(R.id.update_diary_fab_back)
    FloatingActionButton mUpdateDiaryFabBack;
    @Bind(R.id.update_diary_fab_add)
    FloatingActionButton mUpdateDiaryFabAdd;
    @Bind(R.id.update_diary_fab_delete)
    FloatingActionButton mUpdateDiaryFabDelete;
    @Bind(R.id.right_labels)
    FloatingActionsMenu mRightLabels;
    @Bind(R.id.common_tv_title)
    TextView mCommonTvTitle;
    @Bind(R.id.common_title_ll)
    LinearLayout mCommonTitleLl;
    @Bind(R.id.common_iv_back)
    ImageView mCommonIvBack;
    @Bind(R.id.common_iv_test)
    ImageView mCommonIvTest;
    @Bind(R.id.update_diary_tv_tag)
    TextView mTvTag;

    private DiaryDatabaseHelper mHelper;

    public static void startActivity(Context context, String title, String content, String tag) {
        Intent intent = new Intent(context, UpdateDiaryActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("content", content);
        intent.putExtra("tag", tag);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_diary);
        AppManager.getAppManager().addActivity(this);
        ButterKnife.bind(this);
        mHelper = new DiaryDatabaseHelper(this, "Diary.db", null, 1);
        initTitle();
        StatusBarCompat.compat(this, Color.parseColor("#161414"));
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String downloadType = SP.getString("downloadType","1");
        boolean themeType = (boolean) SP.getBoolean("btheme", Boolean.parseBoolean("false"));
        RelativeLayout view = (RelativeLayout) findViewById(R.id.main_rl_main);
        LinearLayout view3 = (LinearLayout) findViewById(R.id.numberPadLayout);
        if (themeType) {
          view.setBackgroundColor(Color.parseColor("#223344"));
            mUpdateDiaryTvDate.setTextColor(Color.parseColor("#d3d3d3"));
            view3.setBackgroundColor(Color.parseColor("#223344"));
//
        }
        else {
            mUpdateDiaryTvDate.setTextColor(Color.parseColor("#808080"));
       view.setBackgroundColor(Color.parseColor("#FFFFFF"));
            view3.setBackgroundColor(Color.parseColor("#FFFFFF"));
//
        }
        switch (downloadType) {
            case "1":
                mCommonTvTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                mUpdateDiaryTvDate.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                mTvTag.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);

                break;
            case "2":
                mCommonTvTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP,23);
                mUpdateDiaryTvDate.setTextSize(TypedValue.COMPLEX_UNIT_SP,19);
                mTvTag.setTextSize(TypedValue.COMPLEX_UNIT_SP,22);
                break;
            case "3":
                mCommonTvTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP,30);
                mUpdateDiaryTvDate.setTextSize(TypedValue.COMPLEX_UNIT_SP,26);
                mTvTag.setTextSize(TypedValue.COMPLEX_UNIT_SP,28);
                break;
            default:
                mCommonTvTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
                mUpdateDiaryTvDate.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
                mTvTag.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
        }
        Intent intent = getIntent();
        mUpdateDiaryTvDate.setText("Today " + GetDate.getDate());
        mUpdateDiaryEtTitle.setText(intent.getStringExtra("title"));
        mUpdateDiaryEtContent.setText(intent.getStringExtra("content"));
        mTvTag.setText(intent.getStringExtra("tag"));



    }

    private void initTitle() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        mCommonTvTitle.setText("Edit note");
    }

    @OnClick({R.id.common_iv_back, R.id.update_diary_tv_date, R.id.update_diary_et_title, R.id.update_diary_et_content, R.id.update_diary_fab_back, R.id.update_diary_fab_add, R.id.update_diary_fab_delete})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.common_iv_back:
                MainActivity.startActivity(this);
            case R.id.update_diary_tv_date:
                break;
            case R.id.update_diary_et_title:
                break;
            case R.id.update_diary_et_content:
                break;
            case R.id.update_diary_fab_back:
                android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(this);
                alertDialogBuilder.setMessage("Are you sure to delete this note?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

//                        String title = mUpdateDiaryEtTitle.getText().toString();
                        String tag = mTvTag.getText().toString();
                        SQLiteDatabase dbDelete = mHelper.getWritableDatabase();
                        dbDelete.delete("Diary", "tag = ?", new String[]{tag});
                        MainActivity.startActivity(UpdateDiaryActivity.this);
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();
                break;
            case R.id.update_diary_fab_add:
                SQLiteDatabase dbUpdate = mHelper.getWritableDatabase();
                ContentValues valuesUpdate = new ContentValues();
                String title = mUpdateDiaryEtTitle.getText().toString();
                String content = mUpdateDiaryEtContent.getText().toString();
                valuesUpdate.put("title", title);
                valuesUpdate.put("content", content);
                dbUpdate.update("Diary", valuesUpdate, "title = ?", new String[]{title});
                dbUpdate.update("Diary", valuesUpdate, "content = ?", new String[]{content});
                MainActivity.startActivity(this);
                break;
            case R.id.update_diary_fab_delete:
                MainActivity.startActivity(this);

                break;
        }
    }

    @OnClick(R.id.common_tv_title)
    public void onClick() {
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MainActivity.startActivity(this);
    }
}