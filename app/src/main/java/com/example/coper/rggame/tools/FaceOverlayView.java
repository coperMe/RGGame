package com.example.coper.rggame.tools;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.hardware.camera2.params.Face;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.face.FaceDetector;

/**
 * Created by David García Molino on 22/05/16.
 */
public class FaceOverlayView extends View {

    private Bitmap bitmap;
    private SparseArray<Face> faces;

    public FaceOverlayView(Context context) {
        this(context, null);
    }

    public FaceOverlayView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FaceOverlayView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setBitmap(Bitmap newBitMap){
        FaceDetector detector = new FaceDetector.Builder( getContext() )
                .setTrackingEnabled(false)
                .setLandmarkType(FaceDetector.ALL_LANDMARKS)
                .setMode(FaceDetector.FAST_MODE)
                .build();

        CameraSource cameraSource = new CameraSource.Builder(getContext(), detector)
                .setFacing(CameraSource.CAMERA_FACING_FRONT)
                .setAutoFocusEnabled(true)
                .build();
        /*detector.setProcessor(new Detector.Processor<com.google.android.gms.vision.face.Face>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<com.google.android.gms.vision.face.Face> detections) {

            }
        });*/

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(this.bitmap != null && faces != null)
            drawBitmap(canvas);
    }

    private double drawBitmap(Canvas canvas) {
        return 0;
    }
}
