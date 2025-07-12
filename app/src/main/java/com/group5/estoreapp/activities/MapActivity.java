package com.group5.estoreapp.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.group5.estoreapp.R;

import org.json.JSONArray;
import org.json.JSONObject;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MapActivity extends AppCompatActivity {

    private MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Configuration.getInstance().setUserAgentValue(getPackageName());
        setContentView(R.layout.activity_map);

        mapView = findViewById(R.id.map);
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setMultiTouchControls(true);

        double userLat = getIntent().getDoubleExtra("userLat", 0);
        double userLon = getIntent().getDoubleExtra("userLon", 0);
        double storeLat = getIntent().getDoubleExtra("storeLat", 0);
        double storeLon = getIntent().getDoubleExtra("storeLon", 0);

        GeoPoint start = new GeoPoint(userLat, userLon);
        GeoPoint end = new GeoPoint(storeLat, storeLon);

        mapView.getController().setZoom(14.5);
        mapView.getController().setCenter(start);

        // Marker người dùng
        Marker startMarker = new Marker(mapView);
        startMarker.setPosition(start);
        startMarker.setTitle("Vị trí của bạn");
        mapView.getOverlays().add(startMarker);

        // Marker cửa hàng
        Marker endMarker = new Marker(mapView);
        endMarker.setPosition(end);
        endMarker.setTitle("Cửa hàng gần nhất");
        mapView.getOverlays().add(endMarker);

        // Vẽ đường đi theo lộ trình thật sự
        drawRouteViaRoads(start, end);
    }

    private void drawRouteViaRoads(GeoPoint start, GeoPoint end) {
        new Thread(() -> {
            try {
                // 🔑 Thay thế bằng API key thật của bạn
                String apiKey = "eyJvcmciOiI1YjNjZTM1OTc4NTExMTAwMDFjZjYyNDgiLCJpZCI6ImQ3ZTdiYTkzYTFmMzRmMTNhY2RmMTU1YTAxYTEwYTU5IiwiaCI6Im11cm11cjY0In0="; // ← thay bằng key lấy từ openrouteservice.org

                String url = "https://api.openrouteservice.org/v2/directions/driving-car" +
                        "?api_key=" + apiKey +
                        "&start=" + start.getLongitude() + "," + start.getLatitude() +
                        "&end=" + end.getLongitude() + "," + end.getLatitude();

                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url(url).build();
                Response response = client.newCall(request).execute();

                if (response.isSuccessful()) {
                    JSONObject json = new JSONObject(response.body().string());
                    JSONArray coordinates = json.getJSONArray("features")
                            .getJSONObject(0)
                            .getJSONObject("geometry")
                            .getJSONArray("coordinates");

                    List<GeoPoint> geoPoints = new ArrayList<>();
                    for (int i = 0; i < coordinates.length(); i++) {
                        JSONArray coord = coordinates.getJSONArray(i);
                        double lon = coord.getDouble(0);
                        double lat = coord.getDouble(1);
                        geoPoints.add(new GeoPoint(lat, lon));
                    }

                    runOnUiThread(() -> {
                        Polyline polyline = new Polyline();
                        polyline.setPoints(geoPoints);
                        polyline.setColor(0xFF1976D2); // màu xanh
                        polyline.setWidth(8f);
                        mapView.getOverlays().add(polyline);
                        mapView.invalidate();
                    });
                } else {
                    System.out.println("Lỗi API: " + response.code());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
