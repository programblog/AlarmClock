package com.liwenquan.sl;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.List;

public class SetAlarmActivity extends AppCompatActivity {
    public static final String EXTAR_TIME = "com.lwq.getTime";
    public static final String EXTAR_POSITON = "com.lwq.position";
    private static final int Alarm = 1;
    private static final String KEY = "alarmList";
    SharedPreferences.Editor editor;
    StringBuffer sb;
    Uri ringUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
    private TextView mtvalarmlable;
    private AudioManager audiomanger;
    private int maxVolume, currentVolume;
    private SeekBar seekBar;
    private int hour, minute;
    private TextView mtvLable;
    private String mTime;
    private EditText metLable;
    private Clock mclock;
    private String lable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_alarm);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("设置闹钟");//设置主标题
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        setSupportActionBar(toolbar);

        String clockId= getIntent().getStringExtra(ClockListActivity.EXTRA_CRIME_ID);
        mclock=ClockLab.get(SetAlarmActivity.this).getClock(clockId);
        TimePicker timePicker = (TimePicker) findViewById(R.id.timePicker);


        String s = getIntent().getStringExtra("闹钟时间");
        //String mLable = getIntent().getStringExtra("闹钟标签");
        mtvalarmlable = (TextView) findViewById(R.id.tvalarmlable);
        mtvalarmlable.setText(mclock.getLable());
        final int position = getIntent().getIntExtra(EXTAR_POSITON, 0);
        //Log.e("检查点", "点击位置的时间是"+s);
        String timelist[] = s.split(":");
        timePicker.setCurrentHour(Integer.valueOf(timelist[0]));
        timePicker.setCurrentMinute(Integer.valueOf(timelist[1]));

        hour = timePicker.getCurrentHour();
        minute = timePicker.getCurrentMinute();

        mTime = formattime(hour, minute);

        timePicker.setIs24HourView(true);//是否显示24小时制？默认false
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                mTime = formattime(hourOfDay, minute);

            }
        });
        findViewById(R.id.chooseSong).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setAction(RingtoneManager.ACTION_RINGTONE_PICKER);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, false);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "设置闹玲铃声");
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_ALL);
                Uri pickedUri = RingtoneManager.getActualDefaultRingtoneUri(SetAlarmActivity.this, RingtoneManager.TYPE_ALARM);
                if (pickedUri != null) {
                    intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, pickedUri);
                    ringUri = pickedUri;
                }
                startActivityForResult(intent, 1);


            }
        });
        findViewById(R.id.lllable).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(SetAlarmActivity.this);
                builder.setTitle("标签");
                //    通过LayoutInflater来加载一个xml的布局文件作为一个View对象
                View view = LayoutInflater.from(SetAlarmActivity.this).inflate(R.layout.dialog_lable, null);
                //    设置我们自己定义的布局文件作为弹出框的Content
                builder.setView(view);
                metLable = (EditText) view.findViewById(R.id.etLable);

                //metLable.setText();
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        lable = metLable.getText().toString().trim();
                        mclock.setLable(lable);
                        mtvalarmlable.setText(lable);
                        Toast.makeText(SetAlarmActivity.this, "已设定标签：" + lable, Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
            }
        });
        findViewById(R.id.btnDelClock).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
                MainActivity.list.remove(position);
                saveAlarmList(MainActivity.list);
                finish();

            }
        });
        findViewById(R.id.btnSaveClock).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SetAlarmActivity.this, ClockListActivity.class);
                startActivity(intent);
                ClockLab.get(SetAlarmActivity.this).saveClocks();
                SetAlarmActivity.this.finish();
            }
        });
        audiomanger = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        maxVolume = audiomanger.getStreamMaxVolume(AudioManager.STREAM_ALARM);
        seekBar = (SeekBar) findViewById(R.id.seekbar);
        seekBar.setMax(maxVolume);
        seekBar.setProgress(currentVolume);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                audiomanger.setStreamVolume(AudioManager.STREAM_ALARM, progress, 0);
                currentVolume = audiomanger.getStreamVolume(AudioManager.STREAM_ALARM);
                seekBar.setProgress(currentVolume);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private String formattime(int hour, int minute) {
        String mTime, mhour, mminute;
        if (hour < 10)
            mhour = "0" + hour;
        else mhour = "" + hour;
        if (minute < 10)
            mminute = "0" + minute;
        else mminute = "" + minute;
        mTime = mhour + ":" + mminute;
        return mTime;
    }

    public void saveAlarmList(List<String> list) {
        editor = getSharedPreferences(AddAlarmActivity.class.getName(), MODE_PRIVATE).edit();
        sb = new StringBuffer();
        for (int i = 0; i < list.size(); i++) {
            sb.append(MainActivity.list.get(i)).append(",");
        }
        String content;
        if (list.size() == 0)
            content = null;
        else
            content = sb.toString().substring(0, sb.length() - 1);
        editor.putString(KEY, content);
        editor.commit();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode != RESULT_OK) {
//            return;
//        } else {
//            Uri uri = data
//                    .getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
//            if (uri != null) {
//                RingtoneManager.setActualDefaultRingtoneUri(this,
//                        RingtoneManager.TYPE_ALARM, uri);
//                Toast.makeText(getApplicationContext(), "设置闹钟铃声成功！", Toast.LENGTH_SHORT).show();
//            }
//        }

        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case 1:
                //获取选中的铃声的URI
                Uri pickedURI = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);

                RingtoneManager.setActualDefaultRingtoneUri(this, RingtoneManager.TYPE_ALARM, pickedURI);
                getName(RingtoneManager.TYPE_ALARM);
                Toast.makeText(getApplicationContext(), "设置闹钟铃声成功！", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }

    }

    private void getName(int type) {
        Uri pickedUri = RingtoneManager.getActualDefaultRingtoneUri(this, type);

        Cursor cursor = this.getContentResolver().query(pickedUri, new String[]{MediaStore.Audio.Media.TITLE}, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                String ring_name = cursor.getString(0);

                String[] c = cursor.getColumnNames();

            }
            cursor.close();
        }
    }
}


