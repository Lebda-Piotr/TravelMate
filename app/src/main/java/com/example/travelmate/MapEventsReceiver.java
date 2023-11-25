package com.example.travelmate;

import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.events.MapEvent;
import android.view.MotionEvent;
import org.osmdroid.views.Projection;
import org.osmdroid.views.MapView;



public class MapEventsReceiver extends MapEventsOverlay {
    public interface OnMapClickListener {
        void onMapClick(GeoPoint point);
    }

    private OnMapClickListener mapClickListener;

    public MapEventsReceiver(OnMapClickListener listener) {
        super(null);
        this.mapClickListener = listener;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e, MapView mapView) {
        Projection proj = mapView.getProjection();
        GeoPoint p = (GeoPoint) proj.fromPixels((int) e.getX(), (int) e.getY());
        mapClickListener.onMapClick(p);
        return true;
    }
}

