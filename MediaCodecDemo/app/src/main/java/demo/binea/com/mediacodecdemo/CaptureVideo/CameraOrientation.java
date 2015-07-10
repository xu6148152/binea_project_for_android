package demo.binea.com.mediacodecdemo.CaptureVideo;

/**
 * Created by xubinggui on 15/3/4.
 */
public enum CameraOrientation {
    BACK, FRONT;

    public static CameraOrientation toCameraOrientation(int result) {
        if (result == 0) {
            return BACK;
        } else{
            return FRONT;
        }
    }

    public static int toInt(CameraOrientation value){
        if(value == BACK){
            return 0;
        }else{
            return 1;
        }
    }
}
