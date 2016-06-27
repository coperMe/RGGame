package com.example.coper.rggame.tools;

import android.content.Context;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;

/**
 * Created by coper on 25/05/16.
 */
class CameraPreview extends ViewGroup implements SurfaceHolder.Callback {

    SurfaceView svMask;
    SurfaceHolder shHolder;

    public CameraPreview(Context context) {
        super(context);

        svMask = new SurfaceView(context);
        addView(svMask);

        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        shHolder = svMask.getHolder();
        shHolder.addCallback(this);

    }

    public void setCamera(Camera camera) {
        /*
        if (svMask == camera) { return; }

        stopPreviewAndFreeCamera();

        svMask = camera;

        if (svMask != null) {
            List<Size> localSizes = svMask.getParameters().getSupportedPreviewSizes();
            mSupportedPreviewSizes = localSizes;
            requestLayout();

            try {
                svMask.setPreviewDisplay(mHolder);
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Important: Call startPreview() to start updating the preview
            // surface. Preview must be started before you can take a picture.
            svMask.startPreview();
        }
        */
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        /*
        // Now that the size is known, set up the camera parameters and begin
        // the preview.
        Camera.Parameters parameters = mCamera.getParameters();
        parameters.setPreviewSize(mPreviewSize.width, mPreviewSize.height);
        requestLayout();
        mCamera.setParameters(parameters);

        // Important: Call startPreview() to start updating the preview surface.
        // Preview must be started before you can take a picture.
        mCamera.startPreview();*/
    }


    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {/*
        // Surface will be destroyed when we return, so stop the preview.
        if (mCamera != null) {
            // Call stopPreview() to stop updating the preview surface.
            mCamera.stopPreview();
        }
        */
    }

    /**
     * When this function returns, mCamera will be null.
     */
    private void stopPreviewAndFreeCamera() {
/*
        if (svMask != null) {
            // Call stopPreview() to stop updating the preview surface.
            svMask.stopPreview();

            // Important: Call release() to release the camera for use by other
            // applications. Applications should release the camera immediately
            // during onPause() and re-open() it during onResume()).
            svMask.release();

            svMask = null;
        }*/
    }




}
