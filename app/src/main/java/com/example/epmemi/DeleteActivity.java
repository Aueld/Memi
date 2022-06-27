package com.example.epmemi;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DeleteActivity extends NavUI {

    String fileName;
    ListView listView;

    ArrayList<ListDataDel> memo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deleter);
        context = this;
        currentActivityName = getClass().getSimpleName().trim();

        listView = (ListView)findViewById(R.id.DelListView);

        ListVF();

        customToolbar();
    }

    private String readMemo(String fName){
        String MemoStr = null;
        FileInputStream inFs;
        try{
            inFs = new FileInputStream(getFilesDir().getAbsolutePath() + "/del/" + fName);
            byte[] txt = new byte[1000];
            inFs.read(txt);
            inFs.close();

            MemoStr = (new String(txt).trim());

        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), MemoStr + " memo", Toast.LENGTH_SHORT).show();
        }

        return MemoStr;
    }

    private void ListVF() {
        InitializeMemoData();

        final ListAdapterDel listAdapter = new ListAdapterDel(this, memo);
        listView.setAdapter(listAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                Toast.makeText(getApplicationContext(),
                        listAdapter.getItem(position).getFileName() + " 영구 삭제됨", Toast.LENGTH_SHORT).show();

                level++;

                new File(getFilesDir().getAbsolutePath() + "/del/" + listAdapter.getItem(position).getFileName()).delete();

                finish();//인텐트 종료
                overridePendingTransition(0, 0);//인텐트 효과 없애기
                Intent intent = getIntent(); //인텐트
                startActivity(intent); //액티비티 열기
                overridePendingTransition(0, 0);//인텐트 효과 없애기
            }
        });
    }

    public void InitializeMemoData()
    {
        memo = new ArrayList<ListDataDel>();

        try {
            List<String> filelist = FileList();

            //Toast.makeText(getApplicationContext(), filelist + "", Toast.LENGTH_SHORT).show();

            for(int i = 0; i < filelist.size(); i++) {
                memo.add(new ListDataDel(readMemo(filelist.get(i)), filelist.get(i)));
            }
        }
        catch (Exception e) {

        }
    }

    private List<String> FileList()
    {
        String path = getFilesDir().getAbsolutePath() + "/del/";

        File directory = new File(path);

        if(!directory.exists())
            directory.mkdir();

        File[] files = directory.listFiles();

        List<String> filesNameList = new ArrayList<>();

        for (int i=0; i< files.length; i++) {
            filesNameList.add(files[i].getName());
        }

        return  filesNameList;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
