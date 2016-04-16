package binea.reporters

import java.util.concurrent.TimeUnit;

/**
 * Created by xubinggui on 4/8/16.
 //                            _ooOoo_  
 //                           o8888888o  
 //                           88" . "88  
 //                           (| -_- |)  
 //                            O\ = /O  
 //                        ____/`---'\____  
 //                      .   ' \\| |// `.  
 //                       / \\||| : |||// \  
 //                     / _||||| -:- |||||- \  
 //                       | | \\\ - /// | |  
 //                     | \_| ''\---/'' | |  
 //                      \ .-\__ `-` ___/-. /  
 //                   ___`. .' /--.--\ `. . __  
 //                ."" '< `.___\_<|>_/___.' >'"".  
 //               | | : `- \`.;`\ _ /`;.`/ - ` : | |  
 //                 \ \ `-. \_ __\ /__ _/ .-` / /  
 //         ======`-.____`-.___\_____/___.-`____.-'======  
 //                            `=---='  
 //  
 //         .............................................  
 //                  佛祖镇楼                  BUG辟易 

 */
public class FormattingUtils {
    static String formatDuration(long ms) {
        def hours = TimeUnit.MILLISECONDS.toHours(ms)
        def minutes = TimeUnit.MILLISECONDS.toMinutes(ms) - TimeUnit.HOURS.toMinutes(hours)
        def seconds = TimeUnit.MILLISECONDS.toSeconds(ms) - TimeUnit.MINUTES.toSeconds(minutes) -
                TimeUnit.HOURS.toSeconds(hours)
        def millis = ms - TimeUnit.MINUTES.toMillis(minutes) -
                TimeUnit.SECONDS.toMillis(seconds) -
                TimeUnit.HOURS.toMillis(hours)
        if (hours > 0) {
            String.format("%d:%02d:%02d",
                    hours,
                    minutes,
                    seconds)
        } else {
            String.format("%d:%02d.%03d",
                    minutes,
                    seconds,
                    millis)
        }
    }
}
