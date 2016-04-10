package binea

import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Created by xubinggui on 4/10/16.
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
public class DateAndTimePlugin implements Plugin<Project>{

    @Override
    void apply(Project project) {
        project.extensions.create("dateAndTime", DateAndTimePluginExtension)

        project.task('showTime') {
            println "Current time is " + new Date().format(project.dateAndTime.timeFormat)
        }

        project.task('showDate') {
            println "Current date is " + new Date().format(project.dateAndTime.dateFormat)
        }
    }
}