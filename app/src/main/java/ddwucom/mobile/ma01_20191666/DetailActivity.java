package ddwucom.mobile.ma01_20191666;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQ_LOC = 100;
    public static final String TAG = "DetailActivity";

    private GoogleMap mGoogleMap;
    private LocationManager locationManager;
    private Marker centerMarker;
    private ArrayList<Marker> markerArrayList;

    FestivalDTO Festival;
    TextView etName, etIntro, etLoc;
    EditText etMemo;
    FestivalDBManager festivalDBManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(mapReadyCallBack);  //1. 맵로딩

        markerArrayList = new ArrayList<Marker>();

        Festival = (FestivalDTO) getIntent().getSerializableExtra("festival");
        etName = findViewById(R.id.dtFstvlName);
        etIntro = findViewById(R.id.dtFstvlIntro);
        etLoc = findViewById(R.id.dtFstvlLoc);
        etMemo = findViewById(R.id.dtMemo);

        etName.setText(Festival.getName());
        etIntro.setText(Festival.getIntroduction());
        etLoc.setText(Festival.getLoc());
        festivalDBManager = new FestivalDBManager(this);
    }
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAdd:
                if (festivalDBManager.getFestivalName(Festival.getName()) != null) {
                    Toast.makeText(this, "이미 좋아요 한 축제", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_CANCELED);
                    finish();
                    break;
                } else {
                    Log.d(TAG, etMemo.getText().toString());
                    boolean result = festivalDBManager.addNewFestival(new FestivalDTO(Festival.getName(), Festival.getStartDate(),
                            Festival.getEndDate(), Festival.getLoc(), Festival.getLatitude(), Festival.getLongitude(), etMemo.getText().toString()));
                    if (result) {
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("festival", Festival.getName());
                        setResult(RESULT_OK, resultIntent);
                    } else {
                        Toast.makeText(this, "축제 추가 실패", Toast.LENGTH_SHORT).show();
                    }
                    finish();
                    break;
                }
            case R.id.btnCancel:
                setResult(RESULT_CANCELED);
                finish();
                break;
        }
    }


    private void locationUpdate() {     //4. 버튼 클릭시 수행됨
        if (checkPermission()) {    //5. 퍼미션 체크 10. 승인 시 true로 통과됨
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 0, locationListener);
            //11. gps로 부터 5초마다 혹은 0m를 움직일 때마다 listener 동작
        }
    }

    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(@NonNull Location location) { //12. 리스너 동작시 수행되면서 location이 들어오면 현재 위치로 바꾸고 바뀐 장소로 animate로 보여줌
            LatLng currentLoc = new LatLng(location.getLatitude(), location.getLongitude());
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLoc, 17));

        }
    };

    OnMapReadyCallback mapReadyCallBack = new OnMapReadyCallback() {    //2. 맵 로딩시 수행됨 (latlng에 정의된 고정위치로 화면을 옮김) -> 화면에 표시됨
        @Override
        public void onMapReady(GoogleMap googleMap) {
            mGoogleMap = googleMap;

            LatLng currentLoc = new LatLng(Double.parseDouble(Festival.getLatitude()), Double.parseDouble(Festival.getLongitude()));
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLoc, 17));

            MarkerOptions options = new MarkerOptions();
            options.position(currentLoc);
            options.title(Festival.getLoc());
            options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

            centerMarker = mGoogleMap.addMarker(options);
            centerMarker.showInfoWindow();

            markerArrayList.add(centerMarker);
        }
    };

    //현재 Focus를 받고 있는 View의 영역이 아닌 다른 곳에 터치 이벤트가 일어나면 InputMethodManager을 통해 키보드를 내리는 코드
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View focusView = getCurrentFocus();
        if (focusView != null) {
            Rect rect = new Rect();
            focusView.getGlobalVisibleRect(rect);
            int x = (int) ev.getX(), y = (int) ev.getY();
            if (!rect.contains(x, y)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if (imm != null)
                    imm.hideSoftInputFromWindow(focusView.getWindowToken(), 0);
                focusView.clearFocus();
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /*위치 관련 권한 확인 메소드 - 필요한 부분이 여러 곳이므로 메소드로 구성*/
    /*ACCESS_FINE_LOCATION - 상세 위치 확인에 필요한 권한
    ACCESS_COARSE_LOCATION - 대략적 위치 확인에 필요한 권한
    AndroidManifest.xml에 꼭 등록하기*/
    private boolean checkPermission() {		//한 번 승인되면 쭉가능      //6. 퍼미션 체크했는데 없으면 requestpermission으로 dialog 띄움
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) { //gps 권한 (FINE을 포함할 경우 COARSE는 기본적으로 허용됨)
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION} //권한이 승인이 안되었을 경우 요청      //7. 퍼미션 쓸 건지 요청
                        , MY_PERMISSIONS_REQ_LOC);
                return false;
            } else
                return true;
        }
        return false;
    }

    /*권한승인 요청에 대한 사용자의 응답 결과에 따른 수행*/
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQ_LOC:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {  //8. 사용자가 승인하면 다시 locationUpdate 수행
                    /*권한을 승인받았을 때 수행하여야 하는 동작 지정*/
                    locationUpdate();   //다시 수행

                } else {
                    /*사용자에게 권한 제약에 따른 안내*/
                    Toast.makeText(this, "Permissions are not granted.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
