package binea.reporters

import au.com.bytecode.opencsv.CSVWriter
import binea.Timing
import org.gradle.api.logging.Logger
import org.gradle.internal.TrueTimeProvider

import java.text.DateFormat
import java.text.SimpleDateFormat

/**
 * Created by xubinggui on 4/5/16.
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
class CSVReporter extends AbstractBuildTimeTrackerReporter{

    CSVReporter(Map<String, String> options,
            Logger logger) {
        super(options, logger)
    }

    @Override
    def run(List<Timing> timings) {
        long timestamp = new TrueTimeProvider().getCurrentTime()
        String output = getOption("output", "")
        boolean append = getOption("append", "false").toBoolean()
        TimeZone tz = TimeZone.getTimeZone("UTC")
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss,SSS'Z'")
        df.setTimeZone(tz)

        File file = new File(output)
        file.getParentFile()?.mkdirs()

        CSVWriter writer = new CSVWriter(new BufferedWriter(new FileWriter(file, append)))

        if(getOption("header", "true").toBoolean()) {
            String[] headers = ["timestamp", "order", "task", "success", "did_work", "skipped", "ms", "date",
                                "cpu", "memory", "os"]
            writer.writeNext(headers)
        }

        def info = new SysInfo()
        def osId = info.getOSIdentifier()
        def cpuId = info.getCPUIdentifier()

        timings.eachWithIndex{timing, idx ->
            String[] line = [
                    timestamp.toString(),
                    idx.toString(),
                    timing.path,
                    timing.success.toString(),
                    timing.didWork.toString(),
                    timing.skipped.toString(),
                    timing.ms.toString(),
                    df.format(new Date(timestamp)),
                    cpuId,
                    osId
            ].toArray()
            writer.writeNext(line)
        }

        writer.close()
    }
}
