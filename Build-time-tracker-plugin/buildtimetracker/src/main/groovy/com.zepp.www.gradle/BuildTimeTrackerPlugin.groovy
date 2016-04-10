package com.zepp.www.gradle

import org.gradle.api.NamedDomainObjectCollection;
import org.gradle.api.Plugin;
import org.gradle.api.Project
import org.gradle.api.reporting.ReportingExtension
import org.gradle.wrapper.Logger;

/**
 * Created by xubinggui on 4/5/16.
 * //                            _ooOoo_
 * //                           o8888888o
 * //                           88" . "88
 * //                           (| -_- |)
 * //                            O\ = /O
 * //                        ____/`---'\____
 * //                      .   ' \\| |// `.
 * //                       / \\||| : |||// \
 * //                     / _||||| -:- |||||- \
 * //                       | | \\\ - /// | |
 * //                     | \_| ''\---/'' | |
 * //                      \ .-\__ `-` ___/-. /
 * //                   ___`. .' /--.--\ `. . __
 * //                ."" '< `.___\_<|>_/___.' >'"".
 * //               | | : `- \`.;`\ _ /`;.`/ - ` : | |
 * //                 \ \ `-. \_ __\ /__ _/ .-` / /
 * //         ======`-.____`-.___\_____/___.-`____.-'======
 * //                            `=---='
 * //
 * //         .............................................
 * //                  佛祖镇楼                  BUG辟易
 */
public class BuildTimeTrackerPlugin implements Plugin<Project> {

    def REPORTERS = [

    ]

    Logger logger

    NamedDomainObjectCollection<ReportingExtension> reportingExtensions

    @Override public void apply(Project project) {
        this.logger = project.logger
        project.extensions.create("buildtimetracker", BuildTimeTrackerExtension)
        reportingExtensions = project.buildtimetracker.extensions.reporters = project.container(ReportingExtension)
        project.gradle.addBuildListener(new TimingRecorder(this))
    }
}
