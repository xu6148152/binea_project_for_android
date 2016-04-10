package com.zepp.www.gradle.reporters

import com.zepp.www.gradle.Timing
import org.gradle.BuildResult
import org.gradle.wrapper.Logger;

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
abstract class AbstractBuildTimeTrackerReporter {
    Map<String, String> options
    Logger logger

    AbstractBuildTimeTrackerReporter(Map<String, String> options, Logger logger) {
        this.options = options
        this.logger = logger
    }

    abstract run(List<Timing> timings)

    String getOption(String name, String defaultVal) {
        options[name] == null ? defaultVal : options[name]
    }

    void onBuildResult(BuildResult result) {}
}
