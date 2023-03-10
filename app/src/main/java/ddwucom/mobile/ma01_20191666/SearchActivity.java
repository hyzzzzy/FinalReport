package ddwucom.mobile.ma01_20191666;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    String apiAddress;
    FestivalAdapter adapter;
    NetworkManager networkManager;
    ArrayList<FestivalDTO> resultList;
    FestivalXmlParser parser;
    ListView lvList;
    EditText etSearchName;

    final int DETAIL_CODE_IN_SEARCH = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        lvList = findViewById(R.id.lvList);
        etSearchName = findViewById(R.id.etSearchName);

        networkManager = new NetworkManager(this);
        apiAddress = getResources().getString(R.string.festival_name_search);
        resultList = new ArrayList();
        parser = new FestivalXmlParser();

        adapter = new FestivalAdapter(this, R.layout.listview_festival, resultList);
        lvList.setAdapter(adapter);

        lvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FestivalDTO festivalDTO = resultList.get(position);
                Intent intent = new Intent(SearchActivity.this, DetailActivity.class);
                intent.putExtra("festival", festivalDTO);
                startActivityForResult(intent, DETAIL_CODE_IN_SEARCH);
            }
        });
    }
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSearch:
                String targetName = etSearchName.getText().toString();

                //if (targetName.equals("")) searchAddress = apiAddress;  // ???????????? ?????? ?????? ?????? ??????????????????
                new NetworkAsyncTask().execute(apiAddress, targetName);
                adapter.notifyDataSetChanged();
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
            progressDlg = ProgressDialog.show(SearchActivity.this, "Wait", "Downloading...");     // ???????????? ??????????????? ??????
        }

        @Override
        protected String doInBackground(String... strings) {
            String address = strings[0];
            String query = strings[1];
            String apiURL = null;

            try {
                apiURL = address + URLEncoder.encode(query, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            String result = networkManager.downloadContents(apiURL);
            if (result == null || result.contains("<resultCode>99</resultCode>")) {
                cancel(true);
                return NETWORK_ERR_MSG;
            }
            // parsing - ??????????????? ?????? ?????? ?????? ??????(????????? ??????)?????? ???????????? ?????? ??????
            // parsing ??? ???????????? ????????? ?????? AsyncTask??? ??????????????? ????????? ??????
            Log.d(TAG, result);
            return result;
        }

        @Override
        protected void onCancelled(String result) {
            super.onCancelled(result);
            Log.d(TAG, result);
            progressDlg.dismiss(); //dlg ?????????
        }

        @Override
        protected void onPostExecute(String result) {
            // parsing - ??????????????? ?????? ?????? ??? ???????????? ???????????? ?????? ??????
            progressDlg.dismiss(); //dlg ?????????
            ArrayList<FestivalDTO> parsedList = (ArrayList<FestivalDTO>) parser.parse(result); //parsing??? ??????

            if (resultList == null) {       // ????????? ????????? ???????????? ???????????? ?????? ??????
                Toast.makeText(SearchActivity.this, "???????????? ????????? ?????? ????????? ???????????? ????????????.", Toast.LENGTH_SHORT).show();
            } else {
                resultList.clear();
                resultList.addAll(parsedList);
                adapter.notifyDataSetChanged();
            }
        }
    }

}
