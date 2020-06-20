package com.mobile.fm.main;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.JsonObject;
import com.mobile.fm.R;
import com.mobile.fm.category.activity.CategoryMainActivity;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Nullable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

import static android.content.ContentValues.TAG;

public class ActionHome extends Fragment implements View.OnClickListener, LocationListener {
    ViewGroup viewGroup;

    ContentActivity activity;

    private TextView fragmentUsername;

    private Button musicbtn;
    private Button readingbtn;
    private Button travelbtn;
    private Button exercisebtn;
    private Button tvbtn;
    private Button moviebtn;
    //location
    private TextView locality;

    //weather
    LocationManager locationManager;
    TextView weather;
    TextView temperature;
    TextView max_temp;
    TextView min_temp;
    double lat, lon;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES=1000;
    private static final long MIN_TIME_BW_UPDATES=1000*60*1;
    private String curWeather="clear";

    //time
    long now = System.currentTimeMillis();
    Date date = new Date(now);
    // 시간을 나타냇 포맷을 정한다 ( yyyy/MM/dd 같은 형태로 변형 가능 )
    SimpleDateFormat sdfNow = new SimpleDateFormat("hh");
    // nowDate 변수에 값을 저장한다.
    String sHour = sdfNow.format(date);
    int hour= Integer.parseInt(sHour);

    //background
    private LinearLayout bg;
    private ImageView bg_obj;
    private TextView up;
    private TextView down;



    private FirebaseUser firebaseUser;
    private FirebaseFirestore db;
    private DocumentReference docRef;
    @Nullable
    @Override
    public View onCreateView (@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                              @Nullable Bundle savedInstanceState) {

        viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_home, container,false);

        //initializing username from firebase
        fragmentUsername = (TextView) viewGroup.findViewById(R.id.fragmentHome_UserName);

        musicbtn = (Button) viewGroup.findViewById(R.id.musicbtn);
        readingbtn = (Button) viewGroup.findViewById(R.id.readingbtn);
        travelbtn = (Button) viewGroup.findViewById(R.id.travelbtn);
        exercisebtn = (Button) viewGroup.findViewById(R.id.exercisebtn);
        tvbtn = (Button) viewGroup.findViewById(R.id.tvbtn);
        moviebtn = (Button) viewGroup.findViewById(R.id.moviebtn);

        weather=viewGroup.findViewById(R.id.weather);
        max_temp=viewGroup.findViewById(R.id.max_temper);
        min_temp=viewGroup.findViewById(R.id.min_temper);
        temperature=viewGroup.findViewById(R.id.temper);
        locationManager = (LocationManager) viewGroup.getContext().getSystemService(Context.LOCATION_SERVICE);
        locality=viewGroup.findViewById(R.id.location);
        //bg
        bg=viewGroup.findViewById(R.id.background);
        bg_obj=viewGroup.findViewById(R.id.weather_obj);
        up=viewGroup.findViewById(R.id.up);
        down=viewGroup.findViewById(R.id.down);


        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
        docRef = db.collection("User").document(firebaseUser.getEmail());


        //날씨가져오기.
        if (locationManager != null) {
            requestLocation();
        }

        //ClickListener 처리
        musicbtn.setOnClickListener(this);
        readingbtn.setOnClickListener(this);
        travelbtn.setOnClickListener(this);
        exercisebtn.setOnClickListener(this);
        tvbtn.setOnClickListener(this);
        moviebtn.setOnClickListener(this);

        docRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            Log.d("mingyu",documentSnapshot.getString("username"));
                            fragmentUsername.setText("Hello "+documentSnapshot.getString("username"));

                        } else {
                            Toast.makeText(activity.getApplicationContext(), "No Nickname", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(activity.getApplicationContext(), "Error!", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, e.toString());
                    }
                });


        return viewGroup;
    }

    @Override
    public void onClick(View view) {
        //버튼을 눌러 각 액티비티에 맞는 화면으로 이동한다.
        Intent intent = new Intent(getContext(), CategoryMainActivity.class);
        if (view == musicbtn) {
            intent.putExtra("category","Music");
        }
        else if (view == readingbtn) {
            intent.putExtra("category","Reading");
        }
        else if (view == travelbtn) {
            intent.putExtra("category","Travel");
        }
        else if (view == exercisebtn) {
            intent.putExtra("category","Exercise");
        }
        else if (view == tvbtn) {
            intent.putExtra("category","TV");
        }
        else if (view==moviebtn){
            intent.putExtra("category","Movie");
        }
        System.out.println("여기까지완료");
        startActivity(intent);
    }



    @Override
    public void onLocationChanged(Location location) {
        /*현재 위치에서 위도경도 값을 받아온뒤 우리는 지속해서 위도 경도를 읽어올것이 아니니
        날씨 api에 위도경도 값을 넘겨주고 위치 정보 모니터링을 제거한다.*/
        lat = location.getLatitude();
        lon = location.getLongitude();

        //날씨 가져오기 통신
        getWeather(lat, lon);
        //위치정보 모니터링 제거
        locationManager.removeUpdates(ActionHome.this);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private void requestLocation() {
        //사용자로 부터 위치정보 권한체크
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity)getContext(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 1, this);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 500, 1, this);
        }
    }

    private interface ApiService {
        //베이스 Url
        String API_URL = "http://api.openweathermap.org/";
        String APPKEY = "e699befaf4496f59a242c1ce1e4d8517";

        //get 메소드를 통한 http rest api 통신
        //이부분 수정필요.
        @GET("data/2.5/weather")
        Call<WeatherRepo> getWeather(@Query("lat") double lat, @Query("lon") double lon, @Query("APPID") String app_id);
    }

    private void getWeather(double latitude, double longitude) {
        setLocation(latitude, longitude);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(ApiService.API_URL).addConverterFactory(GsonConverterFactory.create()).build();
        ApiService apiService = retrofit.create(ApiService.class);
        Call<WeatherRepo> call = apiService.getWeather(latitude, longitude, ApiService.APPKEY);


        call.enqueue(new Callback<WeatherRepo>() {
            @Override
            public void onResponse(@NonNull Call<WeatherRepo> call, @NonNull Response<WeatherRepo> response) {
                if (response.isSuccessful()) {
                    //날씨데이터를 받아옴
                    WeatherRepo repo = response.body();
                    if (repo != null) {
                        //데이터가 null 이 아니라면 날씨 데이터를 텍스트뷰로 보여주기
                        curWeather=String.valueOf(repo.getWeather().get(0).getMain());
                        weather.setText(curWeather);
                        max_temp.setText(String.valueOf(Math.round(repo.getMain().getTempMax()-273.15))+"°C");
                        min_temp.setText(String.valueOf(Math.round(repo.getMain().getTempMin()-273.15))+"°C");
                        temperature.setText(String.valueOf(Math.round(repo.getMain().getTemp()-273.15))+"°C");
                    }
                    setBackground(curWeather,hour);
                }
            }
            @Override
            public void onFailure(@NonNull Call<WeatherRepo> call, @NonNull Throwable t) {
                System.out.println("날씨 가져오기 실패 : "+t.getMessage());
            }
        });
    }
    private void setLocation(double latitude, double longitude){
        final Geocoder geocoder = new Geocoder(getContext());
        List<Address> list = null;
        try {
            list = geocoder.getFromLocation(
                    latitude, // 위도
                    longitude, // 경도
                    10); // 얻어올 값의 개수
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("test", "입출력 오류 - 서버에서 주소변환시 에러발생");
        }
        if (list != null) {
            if (list.size() == 0) {
                locality.setText("해당되는 주소 정보가 없습니다");
            } else {
                locality.setText(list.get(0).getLocality());
            }
        }
    }
    private void setBackground(String nowWeather, int time){
        if(nowWeather.equals("Drizzle") || nowWeather.equals("Rain") || nowWeather.equals("Thunderstorm") || nowWeather.equals("Snow")){
            bg_obj.setImageResource(R.drawable.rain);
        }
        else if(nowWeather.equals("Clear")){
            bg_obj.setImageResource(R.drawable.sun);
        }
        else {
            bg_obj.setImageResource(R.drawable.clouds);
        }

        if((6<=time && time<9) || (19<=time && time<21)){
            bg.setBackground(getResources().getDrawable(R.drawable.background_sunny));
        }
        else if(9<=time && time<=18){
            bg.setBackground(getResources().getDrawable(R.drawable.background_clear_sky));
        }
        else if(21<=time && time<23){
            bg.setBackground(getResources().getDrawable(R.drawable.background_evening));
            weather.setTextColor(0xFFFFFFFF);
            max_temp.setTextColor(0xFFFFFFFF);
            min_temp.setTextColor(0xFFFFFFFF);
            locality.setTextColor(0xFFFFFFFF);
            temperature.setTextColor(0xFFFFFFFF);
            fragmentUsername.setTextColor(0xFFFFFFFF);
            up.setTextColor(0xFFFFFFFF);
            down.setTextColor(0xFFFFFFFF);
        }
        else {
            bg.setBackground(getResources().getDrawable(R.drawable.background_night));
            weather.setTextColor(0xFFFFFFFF);
            max_temp.setTextColor(0xFFFFFFFF);
            min_temp.setTextColor(0xFFFFFFFF);
            locality.setTextColor(0xFFFFFFFF);
            temperature.setTextColor(0xFFFFFFFF);
            fragmentUsername.setTextColor(0xFFFFFFFF);
            up.setTextColor(0xFFFFFFFF);
            down.setTextColor(0xFFFFFFFF);

        }
    }
}