package ddwucom.mobile.ma01_20191666;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class LikedFestivalActivity extends AppCompatActivity {
    ListView lvList;
    MyFestivalAdapter adapter;
    ArrayList<FestivalDTO> resultList;
    FestivalDBManager festivalDBManager;

    final int DETAIL_CODE_IN_LIKED = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liked);

        lvList = findViewById(R.id.lvList);

        resultList = new ArrayList();
        festivalDBManager = new FestivalDBManager(this);

        lvList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final int pos = position;
                AlertDialog.Builder builder = new AlertDialog.Builder(LikedFestivalActivity.this);
                builder.setTitle(resultList.get(pos).getName() + " 삭제")
                        .setMessage(resultList.get(pos).getName() + "을(를) 삭제하시겠습니까?")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (festivalDBManager.removeFestival(resultList.get(pos).get_id())) {
                                    Toast.makeText(LikedFestivalActivity.this, resultList.get(pos).getName() + "삭제 완료", Toast.LENGTH_SHORT).show();
                                    resultList.clear();
                                    resultList.addAll(festivalDBManager.getLikedFestival());
                                    adapter.notifyDataSetChanged();
                                } else {
                                    Toast.makeText(LikedFestivalActivity.this, resultList.get(pos).getName() + "삭제 실패", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("취소", null)
                        .setCancelable(false)
                        .show();
                return true;
            }
        });
    }
    protected void onResume() {
        super.onResume();
        //여기서 DB 가져와서 리스트 보여주기

        resultList = festivalDBManager.getLikedFestival();
        if (resultList.isEmpty()) {
            Toast.makeText(this, "좋아요한 축제가 없습니다.", Toast.LENGTH_SHORT).show();
        } else {
            adapter = new MyFestivalAdapter(this, R.layout.listview_db_festival, resultList);
            lvList.setAdapter(adapter);
        }
    }
}
