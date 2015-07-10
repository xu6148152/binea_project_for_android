package demo.binea.com.mediacodecdemo.CaptureVideo;

import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.util.Log;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CameraUtils {
  // based on ApiDemos
  private static final String TAG = CameraUtils.class.getCanonicalName();

  private static final double ASPECT_TOLERANCE=0.1;

  public static Camera.Size getOptimalPreviewSize(int displayOrientation,
                                                  int width,
                                                  int height,
                                                  Camera.Parameters parameters) {
    double targetRatio=(double)width / height;
    List<Camera.Size> sizes=parameters.getSupportedPreviewSizes();
    Camera.Size optimalSize=null;
    double minDiff=Double.MAX_VALUE;
    int targetHeight=height;

    if (displayOrientation == 90 || displayOrientation == 270) {
      targetRatio=(double)height / width;
    }

    // Try to find an size match aspect ratio and size

    for (Camera.Size size : sizes) {
      double ratio=(double)size.width / size.height;

      if (Math.abs(ratio - targetRatio) <= ASPECT_TOLERANCE) {
        if (Math.abs(size.height - targetHeight) < minDiff) {
          optimalSize=size;
          minDiff=Math.abs(size.height - targetHeight);
        }
      }
    }

    // Cannot find the one match the aspect ratio, ignore
    // the requirement

    if (optimalSize == null) {
      minDiff=Double.MAX_VALUE;

      for (Camera.Size size : sizes) {
        if (Math.abs(size.height - targetHeight) < minDiff) {
          optimalSize=size;
          minDiff=Math.abs(size.height - targetHeight);
        }
      }
    }

    return(optimalSize);
  }

  public static Camera.Size getBestAspectPreviewSize(int displayOrientation,
                                                     int width,
                                                     int height,
                                                     Camera.Parameters parameters) {
    return(getBestAspectPreviewSize(displayOrientation, width, height,
                                    parameters, 0.0d));
  }

  public static Camera.Size getBestAspectPreviewSize(int displayOrientation,
                                                     int width,
                                                     int height,
                                                     Camera.Parameters parameters,
                                                     double closeEnough) {
    double targetRatio=(double)width / height;
    Camera.Size optimalSize=null;
    double minDiff=Double.MAX_VALUE;

    if (displayOrientation == 90 || displayOrientation == 270) {
      targetRatio=(double)height / width;
    }

    List<Camera.Size> sizes=parameters.getSupportedPreviewSizes();

    Collections.sort(sizes, Collections.reverseOrder(new SizeComparator()));

    for (Size size : sizes) {
      double ratio=(double)size.width / size.height;

      if (Math.abs(ratio - targetRatio) < minDiff) {
        optimalSize=size;
        minDiff=Math.abs(ratio - targetRatio);
      }

      if (minDiff < closeEnough) {
        break;
      }
    }

    return(optimalSize);
  }

  public static Camera.Size getLargestPictureSize(CameraHost host,
                                                  Camera.Parameters parameters) {
    return(getLargestPictureSize(host, parameters, true));
  }

  public static Camera.Size getLargestPictureSize(CameraHost host,
                                                  Camera.Parameters parameters,
                                                  boolean enforceProfile) {
    Camera.Size result=null;

    for (Camera.Size size : parameters.getSupportedPictureSizes()) {

      // android.util.Log.d("CWAC-Camera",
      // String.format("%d x %d", size.width, size.height));

      if (!enforceProfile
          || (size.height <= host.getDeviceProfile()
                                 .getMaxPictureHeight() && size.height >= host.getDeviceProfile()
                                                                              .getMinPictureHeight())) {
        if (result == null) {
          result=size;
        }
        else {
          int resultArea=result.width * result.height;
          int newArea=size.width * size.height;

          if (newArea > resultArea) {
            result=size;
          }
        }
      }
    }

    if (result == null && enforceProfile) {
      result=getLargestPictureSize(host, parameters, false);
    }

    return(result);
  }

  public static Camera.Size getSmallestPictureSize(Camera.Parameters parameters) {
    Camera.Size result=null;

    for (Camera.Size size : parameters.getSupportedPictureSizes()) {
      if (result == null) {
        result=size;
      }
      else {
        int resultArea=result.width * result.height;
        int newArea=size.width * size.height;

        if (newArea < resultArea) {
          result=size;
        }
      }
    }

    return(result);
  }

  public static String findBestFlashModeMatch(Camera.Parameters params,
                                              String... modes) {
    String match=null;

    List<String> flashModes=params.getSupportedFlashModes();

    if (flashModes != null) {
      for (String mode : modes) {
        if (flashModes.contains(mode)) {
          match=mode;
          break;
        }
      }
    }

    return(match);
  }

  private static class SizeComparator implements Comparator<Camera.Size> {
    @Override
    public int compare(Size lhs, Size rhs) {
      int left=lhs.width * lhs.height;
      int right=rhs.width * rhs.height;

      if (left < right) {
        return(-1);
      }
      else if (left > right) {
        return(1);
      }

      return(0);
    }
  }

  /**
   * Attempts to find a preview size that matches the provided width and height (which
   * specify the dimensions of the encoded video).  If it fails to find a match it just
   * uses the default preview size for video.
   * <p>
   * TODO: should do a best-fit match, e.g.
   * https://github.com/commonsguy/cwac-camera/blob/master/camera/src/com/commonsware/cwac/camera/CameraUtils.java
   */
  public static void choosePreviewSize(Camera.Parameters parms, int width, int height) {
    // We should make sure that the requested MPEG size is less than the preferred
    // size, and has the same aspect ratio.
    Camera.Size ppsfv = parms.getPreferredPreviewSizeForVideo();
    if (ppsfv != null) {
      Log.d(TAG, "Camera preferred preview size for video is " +
              ppsfv.width + "x" + ppsfv.height);
    }

    //for (Camera.Size size : parms.getSupportedPreviewSizes()) {
    //    Log.d(TAG, "supported: " + size.width + "x" + size.height);
    //}

    for (Camera.Size size : parms.getSupportedPreviewSizes()) {
      if (size.width == width && size.height == height) {
        parms.setPreviewSize(width, height);
        return;
      }
    }

    Log.w(TAG, "Unable to set preview size to " + width + "x" + height);
    if (ppsfv != null) {
      parms.setPreviewSize(ppsfv.width, ppsfv.height);
    }
    // else use whatever the default size is
  }

  /**
   * Attempts to find a fixed preview frame rate that matches the desired frame rate.
   * <p>
   * It doesn't seem like there's a great deal of flexibility here.
   * <p>
   * TODO: follow the recipe from http://stackoverflow.com/questions/22639336/#22645327
   *
   * @return The expected frame rate, in thousands of frames per second.
   */
  public static int chooseFixedPreviewFps(Camera.Parameters parms, int desiredThousandFps) {
    List<int[]> supported = parms.getSupportedPreviewFpsRange();

    for (int[] entry : supported) {
      //Log.d(TAG, "entry: " + entry[0] + " - " + entry[1]);
      if ((entry[0] == entry[1]) && (entry[0] == desiredThousandFps)) {
        parms.setPreviewFpsRange(entry[0], entry[1]);
        return entry[0];
      }
    }

    int[] tmp = new int[2];
    parms.getPreviewFpsRange(tmp);
    int guess;
    if (tmp[0] == tmp[1]) {
      guess = tmp[0];
    } else {
      guess = tmp[1] / 2;     // shrug
    }

    Log.d(TAG, "Couldn't find match for " + desiredThousandFps + ", using " + guess);
    return guess;
  }
}
