package binea.reporters

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
public class SysInfo {
    String getOSIdentifier() {
        ["os.name", "os.version", "os.arch"].collect { System.getProperty(it) }.join(" ")
    }

    long getMaxMemory() {
        MemoryUtil.physicalMemoryAvailable
    }

    String getCPUIdentifier() {
        def os = System.getProperty("os.name")
        if (os.equalsIgnoreCase("mac os x")) {
            def proc = ["sysctl", "-n", "machdep.cpu.brand_string"].execute()
            proc.waitFor()

            if (proc.exitValue() == 0) {
                return proc.in.text.trim()
            }
        } else if (os.equalsIgnoreCase("linux")) {
            def osName = ""
            new File("/proc/cpuinfo").eachLine {
                if (!osName.isEmpty()) return

                if (it.startsWith("model name")) {
                    osName = it.split(": ")[1]
                }
            }
            return osName
        }

        return ""
    }
}
