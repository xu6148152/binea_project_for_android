package binea.reporters

import groovy.transform.Memoized;

/**
 * Created by xubinggui on 4/14/16.
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
public class TerminalInfo {
    @Memoized
    public int getWidth(int fallback) {
        def cols = System.getenv("COLUMNS")
        if (cols != null) {
            return Integer.parseInt(cols, 10)
        }

        if (System.getenv("TERM") == null) {
            return fallback
        }

        try {
            Process p = Runtime.getRuntime().
                    exec(["bash", "-c", "tput cols 2> /dev/tty"] as String[])
            p.waitFor()
            def reader = new BufferedReader(new InputStreamReader(p.getInputStream()))
            def line = reader.readLine()?.trim()
            if (line != null) Integer.valueOf(line) else fallback
        } catch (IOException ignore) {
            fallback
        }
    }
}