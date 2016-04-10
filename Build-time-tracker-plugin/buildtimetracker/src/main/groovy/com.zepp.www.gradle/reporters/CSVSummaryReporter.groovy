package com.zepp.www.gradle.reporters

import au.com.bytecode.opencsv.CSVReader
import com.zepp.www.gradle.Timing
import org.gradle.wrapper.Logger
import org.ocpsoft.prettytime.PrettyTime;

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
public class CSVSummaryReporter extends AbstractBuildTimeTrackerReporter {

    DateUtils dateUtils

    CSVSummaryReporter(Map<String, String> options,
            Logger logger) {
        super(options, logger)
        dateUtils = new DateUtils()
    }

    @Override
    def run(List<Timing> timings) {
        def csv = getOption("csv", "")
        def csvFile = new File(csv)

        if (csv.isEmpty()) {
        }

        if (!csvFile.exists() || !csvFile.isFile()) {
        }
    }

    void printReport(CSVReader reader) {
        def lines = reader.readAll()
        if (lines.size() == 0) return

        logger.quiet "== CSV Build Time Summary =="

        Map<Long, Long> times = lines.groupBy { it[0] }.
                collectEntries { k, v -> [Long.valueOf(k), v.collect { Long.valueOf(it[6]) }.sum()]
                }


    }

    void printTotal(Map<Long, Long> times) {
        long total = times.collect {it.value}.sum()
        def prettyTime = new PrettyTime()
        def first = new Date((Long)times.keySet().min())
        logger.quie
    }
}
