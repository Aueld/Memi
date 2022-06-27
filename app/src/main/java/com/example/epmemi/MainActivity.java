package com.example.epmemi;

import androidx.navigation.ui.AppBarConfiguration;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends NavUI {

    DatePicker dp;
    EditText edtMemo;
    Button btnWrite;
    String fileName;
    ListView listView;

    static boolean vidia = false;

    ArrayList<ListData> memo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;

        customToolbar();

        dp = (DatePicker) findViewById(R.id.datePicker1);
        edtMemo = (EditText) findViewById(R.id.edtMemo);
        btnWrite = (Button) findViewById(R.id.btnWrite);
        listView = (ListView) findViewById(R.id.listView);
        pLevel = (TextView) findViewById(R.id.PlayerLevel);

        Calendar cal = Calendar.getInstance();
        int cYear = cal.get(Calendar.YEAR);
        int cMonth = cal.get(Calendar.MONTH);
        int cDay = cal.get(Calendar.DAY_OF_MONTH);

        currentActivityName = getClass().getSimpleName().trim();

        dp.init(cYear, cMonth, cDay, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                fileName = Integer.toString(year) + "-"
                        + Integer.toString(monthOfYear + 1) + "-"
                        + Integer.toString(dayOfMonth) + ".txt";
                String str = readMemo(fileName);
                edtMemo.setText(str);
                btnWrite.setEnabled(true);
            }
        });

        ListVF();

        dp.setVisibility(View.GONE);
        edtMemo.setVisibility(View.GONE);
        btnWrite.setVisibility(View.GONE);
        listView.setVisibility(View.VISIBLE);

        btnWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    FileOutputStream outFs = openFileOutput(fileName, Context.MODE_PRIVATE);
                    String str = edtMemo.getText().toString();
                    outFs.write(str.getBytes());
                    outFs.close();
                    Toast.makeText(getApplicationContext(), fileName+" 메모됨", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {

                }

                vidia = false;

                edtMemo.setVisibility(View.GONE);
                btnWrite.setVisibility(View.GONE);
                dp.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);
                finish();//인텐트 종료
                overridePendingTransition(0, 0);//인텐트 효과 없애기
                Intent intent = getIntent(); //인텐트
                startActivity(intent); //액티비티 열기
                overridePendingTransition(0, 0);//인텐트 효과 없애기
            }
        });
    }

    private String readMemo(String fName){
        String MemoStr = null;
        FileInputStream inFs;
        try{
            inFs = openFileInput(fName);
            byte[] txt = new byte[500];
            inFs.read(txt);
            inFs.close();

            MemoStr = (new String(txt).trim());

            btnWrite.setText("수정하기");

        } catch (IOException e) {
            edtMemo.setHint("메모 없음");
            btnWrite.setText("새로 저장");
        }
        return MemoStr;
    }

    private void ListVF() {
        InitializeMemoData();

        final ListAdapter listAdapter = new ListAdapter(this, memo);
        listView.setAdapter(listAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                InputStream in = null;
                OutputStream out = null;

                //Toast.makeText(getApplicationContext(), getFilesDir().getAbsolutePath() + "/" + listAdapter.getItem(position).getFileName() , Toast.LENGTH_SHORT).show();

                try {
                    in = new FileInputStream(getFilesDir().getAbsolutePath() + "/" + listAdapter.getItem(position).getFileName());
                    out = new FileOutputStream(getFilesDir().getAbsolutePath() + "/del/" + listAdapter.getItem(position).getFileName());

                    byte[] buffer = new byte[1024];
                    int read;
                    while((read = in.read(buffer)) != -1){
                        out.write(buffer, 0, read);
                    }
                    in.close();
                    in = null;

                    out.flush();
                    out.close();
                    out = null;

                    // 기존 원본파일 삭제
                    new File(getFilesDir().getAbsolutePath() + "/" + listAdapter.getItem(position).getFileName()).delete();
                    File tmp_file = new File(getFilesDir().getAbsolutePath() + "/del/" + listAdapter.getItem(position).getFileName());

                    context.sendBroadcast(new Intent( Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(tmp_file)));

                } catch (FileNotFoundException e) {
                    Toast.makeText(getApplicationContext(), "파일을 찾을 수 없음", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    Toast.makeText(getApplicationContext(), "입출력 오류", Toast.LENGTH_SHORT).show();
                }

                finish();//인텐트 종료
                overridePendingTransition(0, 0);//인텐트 효과 없애기
                Intent intent = getIntent(); //인텐트
                startActivity(intent); //액티비티 열기
                overridePendingTransition(0, 0);//인텐트 효과 없애기
                //listAdapter.notify(memo);

                //listAdapter.chage(memo);

                //listAdapter.notifyDataSetChanged();

//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        listAdapter.notifyDataSetChanged();
//
//                    }
//                });
//                //listAdapter.chageCursor();
//                listView.setAdapter(listAdapter);
//                listView.refreshDrawableState();

                Toast.makeText(getApplicationContext(), listAdapter.getItem(position).getFileName() + " 휴지통으로 이동 됨", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void InitializeMemoData()
    {
        memo = new ArrayList<ListData>();

        try {
            List<String> filelist = FileList();

            //Toast.makeText(getApplicationContext(), filelist + "", Toast.LENGTH_SHORT).show();

            for(int i = 0; i < filelist.size(); i++) {
                memo.add(new ListData(readMemo(filelist.get(i)), filelist.get(i)));
            }
        }
        catch (Exception e) {

        }
    }

    private List<String> FileList()
    {
        String path = getFilesDir().getAbsolutePath();

        File directory = new File(path);
        File[] files = directory.listFiles();

        List<String> filesNameList = new ArrayList<>();

        for (int i=0; i< files.length; i++) {
            if(!(files[i].getName().equals("del")))
                filesNameList.add(files[i].getName());
        }

        return  filesNameList;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater mInflater = getMenuInflater();
        mInflater.inflate(R.menu.menu1, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.itemDate:
                if(vidia) {
                    vidia = false;

                    edtMemo.setVisibility(View.GONE);
                    btnWrite.setVisibility(View.GONE);
                    dp.setVisibility(View.GONE);
                    listView.setVisibility(View.VISIBLE);
                    finish();//인텐트 종료
                    overridePendingTransition(0, 0);//인텐트 효과 없애기
                    Intent intent = getIntent(); //인텐트
                    startActivity(intent); //액티비티 열기
                    overridePendingTransition(0, 0);//인텐트 효과 없애기

                } else {
                    vidia = true;
                    edtMemo.setVisibility(View.VISIBLE);
                    btnWrite.setVisibility(View.VISIBLE);
                    dp.setVisibility(View.VISIBLE);
                    listView.setVisibility(View.GONE);
                }
                return true;
        }

        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}