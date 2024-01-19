package com.example.project;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture; // 이 부분이 누락되어 있을 수 있습니다.
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.media.Image;
import android.media.ImageReader;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.TextureView;
import android.widget.Toast;
import android.Manifest;
import android.content.pm.PackageManager;
import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Comparator;

public class Camera2Helper {

    private static final String TAG = "Camera2Helper";
    private static final int MAX_IMAGES = 2; // 필요에 따라 조절

    private Context context;
    private CameraDevice cameraDevice;
    private CameraCaptureSession cameraCaptureSession;
    private ImageReader imageReader;
    private TextureView textureView;
    private CameraCallback cameraCallback;
    private Surface previewSurface;

    private HandlerThread backgroundThread;
    private Handler backgroundHandler;

    public Camera2Helper(Context context, TextureView textureView) {
        this.context = context;
        this.textureView = textureView;
    }
    public interface CameraCallback {
        void onFrameCaptured(Bitmap bitmap);
    }

    public void setCameraCallback(CameraCallback callback) {
        this.cameraCallback = callback;
    }

    public void setPreviewSurface(Surface surface) {
        this.previewSurface = surface;
    }

    public void openCamera() {
        android.hardware.camera2.CameraManager manager =
                (android.hardware.camera2.CameraManager) context.getSystemService(Context.CAMERA_SERVICE);

        try {
            String cameraId = getFrontCameraId(manager);

            CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);
            Size[] jpegSizes = null;

            if (characteristics != null) {
                jpegSizes = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
                        .getOutputSizes(ImageFormat.JPEG);
            }

            int width = 640;
            int height = 480;

            if (jpegSizes != null && jpegSizes.length > 0) {
                width = jpegSizes[0].getWidth();
                height = jpegSizes[0].getHeight();
            }

            configureImageReader(width, height);

            if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                // 카메라 권한이 허용되지 않은 경우 처리
                return;
            }

            manager.openCamera(cameraId, stateCallback, null);

        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void configureImageReader(int width, int height) {
        imageReader = ImageReader.newInstance(width, height, ImageFormat.JPEG, MAX_IMAGES);
        imageReader.setOnImageAvailableListener(onImageAvailableListener, backgroundHandler);
    }

    private ImageReader.OnImageAvailableListener onImageAvailableListener =
            new ImageReader.OnImageAvailableListener() {
                @Override
                public void onImageAvailable(ImageReader reader) {
                    Image image = null;
                    try {
                        image = reader.acquireLatestImage();
                        ByteBuffer buffer = image.getPlanes()[0].getBuffer();
                        byte[] bytes = new byte[buffer.capacity()];
                        buffer.get(bytes);

                        if (cameraCallback != null) {
                            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            cameraCallback.onFrameCaptured(bitmap);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if (image != null) {
                            image.close();
                        }
                    }
                }
            };

    private String getFrontCameraId(android.hardware.camera2.CameraManager manager) throws CameraAccessException {
        for (String cameraId : manager.getCameraIdList()) {
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);
            Integer facing = characteristics.get(CameraCharacteristics.LENS_FACING);

            if (facing != null && facing == CameraCharacteristics.LENS_FACING_FRONT) {
                return cameraId;
            }
        }
        return manager.getCameraIdList()[0]; // front camera가 없으면 default로 첫 번째 카메라 사용
    }

    private final CameraDevice.StateCallback stateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NonNull CameraDevice camera) {
            cameraDevice = camera;
            createCameraPreviewSession();
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice camera) {
            if (cameraDevice != null) {
                cameraDevice.close();
            }
        }

        @Override
        public void onError(@NonNull CameraDevice camera, int error) {
            if (cameraDevice != null) {
                cameraDevice.close();
                cameraDevice = null;
            }
        }
    };

    private void createCameraPreviewSession() {
        try {
            // TextureView에서 SurfaceTexture 가져오기
            SurfaceTexture texture = textureView.getSurfaceTexture();
            assert texture != null;

            // 기존 코드 유지
            texture.setDefaultBufferSize(imageReader.getWidth(), imageReader.getHeight());
            Surface surface = new Surface(texture);

            final CaptureRequest.Builder previewRequestBuilder =
                    cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            previewRequestBuilder.addTarget(surface);

            // 새로운 코드: 추가된 부분
            if (previewSurface != null) {
                previewRequestBuilder.addTarget(previewSurface);
            }

            cameraDevice.createCaptureSession(Arrays.asList(surface, imageReader.getSurface()),
                    new CameraCaptureSession.StateCallback() {
                        @Override
                        public void onConfigured(@NonNull CameraCaptureSession session) {
                            if (cameraDevice == null) {
                                return;
                            }

                            cameraCaptureSession = session;
                            updatePreview(previewRequestBuilder);
                        }

                        @Override
                        public void onConfigureFailed(@NonNull CameraCaptureSession session) {
                            Toast.makeText(context, "Configuration change", Toast.LENGTH_SHORT).show();
                        }
                    }, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void updatePreview(CaptureRequest.Builder previewRequestBuilder) {
        if (cameraDevice == null) {
            return;
        }

        try {
            previewRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE,
                    CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);

            cameraCaptureSession.setRepeatingRequest(previewRequestBuilder.build(),
                    null, backgroundHandler);

        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    public Bitmap captureStillPicture() {
        if (cameraDevice == null) {
            return null;
        }

        try {
            final CaptureRequest.Builder captureBuilder =
                    cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            captureBuilder.addTarget(imageReader.getSurface());

            int rotation = ((Activity) context).getWindowManager().getDefaultDisplay().getRotation();
            captureBuilder.set(CaptureRequest.JPEG_ORIENTATION, getOrientation(rotation));

            CameraCaptureSession.CaptureCallback captureCallback =
                    new CameraCaptureSession.CaptureCallback() {

                        @Override
                        public void onCaptureCompleted(@NonNull CameraCaptureSession session,
                                                       @NonNull CaptureRequest request,
                                                       @NonNull TotalCaptureResult result) {
                            super.onCaptureCompleted(session, request, result);
                            // 캡처 완료
                        }
                    };

            cameraCaptureSession.stopRepeating();
            cameraCaptureSession.capture(captureBuilder.build(), captureCallback, null);

        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

        return null; // 필요에 따라 캡처된 이미지를 Bitmap으로 반환할 수 있습니다.
    }

    private int getOrientation(int rotation) {
        int sensorOrientation = 0;
        try {
            android.hardware.camera2.CameraManager manager =
                    (android.hardware.camera2.CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
            String cameraId = getFrontCameraId(manager);
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);
            sensorOrientation = characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        return (sensorOrientation + ORIENTATIONS.get(rotation) + 270) % 360;
    }

    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();

    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }

    public void closeCamera() {
        if (cameraDevice != null) {
            cameraDevice.close();
            cameraDevice = null;
        }

        if (cameraCaptureSession != null) {
            cameraCaptureSession.close();
            cameraCaptureSession = null;
        }

        if (imageReader != null) {
            imageReader.close();
            imageReader = null;
        }
    }

    public void startBackgroundThread() {
        backgroundThread = new HandlerThread("CameraBackground");
        backgroundThread.start();
        backgroundHandler = new Handler(backgroundThread.getLooper());
    }

    public void stopBackgroundThread() {
        if (backgroundThread != null) {
            backgroundThread.quitSafely();
            try {
                backgroundThread.join();
                backgroundThread = null;
                backgroundHandler = null;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private class CompareSizesByArea implements Comparator<Size> {
        @Override
        public int compare(Size lhs, Size rhs) {
            return Long.signum((long) lhs.getWidth() * lhs.getHeight() -
                    (long) rhs.getWidth() * rhs.getHeight());
        }
    }
}
