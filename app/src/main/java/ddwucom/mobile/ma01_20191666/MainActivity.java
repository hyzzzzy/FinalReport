package ddwucom.mobile.ma01_20191666;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView lvList;
    String apiAddress;

    FestivalAdapter adapter;
    ArrayList<FestivalDTO> resultList;
    FestivalXmlParser parser;
    NetworkManager networkManager;

    final int DETAIL_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvList = findViewById(R.id.lvList);

        resultList = new ArrayList();
        adapter = new FestivalAdapter(this, R.layout.listview_festival, resultList);
        lvList.setAdapter(adapter);

        apiAddress = getResources().getString(R.string.festival_list);
        parser = new FestivalXmlParser();
        networkManager = new NetworkManager(this);

        lvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FestivalDTO festivalDTO = resultList.get(position);
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra("festival", festivalDTO);
                startActivityForResult(intent, DETAIL_CODE);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        NetworkAsyncTask festival = new NetworkAsyncTask();
        festival.execute(apiAddress);
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) { //사용할 메뉴 첫 등록
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void onMenuItemClick (MenuItem item) {
        switch (item.getItemId()) {
            case R.id.LikedFestival:
                Intent likedIntent = new Intent(MainActivity.this, LikedFestivalActivity.class);
                startActivity(likedIntent);
                break;
            case R.id.SearchFestival:
                Intent searchIntent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(searchIntent);
                break;
        }
    }


    class NetworkAsyncTask extends AsyncTask<String, Void, String> {

        final static String NETWORK_ERR_MSG = "Server Error!";
        public final static String TAG = "NetworkAsyncTask";
        ProgressDialog progressDlg;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDlg = ProgressDialog.show(MainActivity.this, "Wait", "Downloading...");     // 진행상황 다이얼로그 출력
        }

        @Override
        protected String doInBackground(String... strings) {
            String result = networkManager.downloadContents(apiAddress);
            if (result == null || result.contains("<resultCode>99</resultCode>")) {
                cancel(true);
                return NETWORK_ERR_MSG;
            }
            // parsing - 수행시간이 많이 걸릴 경우 이곳(스레드 내부)에서 수행하는 것을 고려
            // parsing 을 이곳에서 수행할 경우 AsyncTask의 반환타입을 적절히 변경
            Log.d(TAG, result);
            return result;
        }

        @Override
        protected void onCancelled(String result) {
            super.onCancelled(result);
            Log.d(TAG, result);
            progressDlg.dismiss(); //dlg 사라짐
        }

        @Override
        protected void onPostExecute(String result) {
            // parsing - 수행시간이 짧을 경우 이 부분에서 수행하는 것을 고려
            resultList = (ArrayList<FestivalDTO>) parser.parse(result); //parsing한 결과
            adapter.setList(resultList);    // Adapter 에 결과 List 를 설정 후 notify
            progressDlg.dismiss(); //dlg 사라짐
        }
    }
}
