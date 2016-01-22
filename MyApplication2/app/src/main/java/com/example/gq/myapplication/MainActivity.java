package com.example.gq.myapplication;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.gq.myapplication.adapter.MyAdapter;
import com.example.gq.myapplication.adapter.ViewHolder;
import com.example.gq.myapplication.entity.Book;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements RefreshLayout.OnLoadListener, SwipeRefreshLayout.OnRefreshListener, AdapterView.OnItemClickListener {

    private ListView listviewReport;
    private RefreshLayout refreshReport;
    private ProgressBar mProgressBar;
    private int start = 0;
    private List<Book> list;
    private MyAdapter<Book> myAdapter;
    private int userid;
    private boolean isResh = false;//是否刷新了
    private static final String REFRESHED = "已刷新！";
    private static final String NEW = "已是最新！";
    private static final String LOADED = "加载完毕！";
    private int si;//判断加载时条目是否<20条
    private boolean isfresh = false;//判断是否刷新了

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        listviewReport = (ListView) findViewById(R.id.listView);
        refreshReport = (RefreshLayout) findViewById(R.id.refreshlayout);
        mProgressBar = (ProgressBar) findViewById(R.id.progressbar);
        setSupportActionBar(toolbar);
        list = list == null ? new ArrayList<Book>() : list;
        initUi();

       FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void initUi() {
        refreshReport.setColorSchemeResources(R.color.green, R.color.red,
                R.color.yellow, R.color.black);
        refreshReport.setOnLoadListener(this);
        refreshReport.setOnRefreshListener(this);
        listviewReport.setOnItemClickListener(this);
        getData();
    }

    private void getData() {
        if (isfresh) {
            if (list.isEmpty()) return;
            list.clear();
            isfresh = false;
        }

        for(int i = 0;i<20;i++){
            Book book = new Book("BOOK"+i);
            list.add(book);
        }
        si = list.size();

        if(myAdapter == null){
            getAdapter();
            listviewReport.setAdapter(myAdapter);
        }else {
            myAdapter.notifyDataSetChanged();
            if (refreshReport != null) {
                refreshReport.setLoading(false);
                refreshReport.setRefreshing(false);
            }
        }

        mProgressBar.setVisibility(View.INVISIBLE);
    }

    private void getAdapter() {

        myAdapter = new MyAdapter<Book>(MainActivity.this, list, R.layout.listview_item) {

            @Override
            public void convert(ViewHolder helper, Book item) {
                helper.setText(R.id.textview, item.getBookname());
            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
///
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Toast.makeText(MainActivity.this,"点击"+position,Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onLoad() {
        if (refreshReport == null) return;
        refreshReport.postDelayed(new Runnable() {
            @Override
            public void run() {
                    if (si == 20) {
                        start += 20;
                        getData();
                    } else if (si < 20) {
                        refreshReport.setLoading(false);
                        Toast.makeText(MainActivity.this,LOADED,Toast.LENGTH_SHORT).show();
                    }

            }
        }, 0);
    }

    @Override
    public void onRefresh() {
        if (refreshReport == null) return;
        refreshReport.post(new Runnable() {
            @Override
            public void run() {
                if (refreshReport == null) return;
                start = 0;
                isResh = !isResh;
                isfresh = true;
                getData();
            }
        });
    }
}
