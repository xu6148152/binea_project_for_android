package binea

import binea.reporters.AbstractBuildTimeTrackerReporter
import binea.reporters.CSVReporter
import binea.reporters.CSVSummaryReporter
import binea.reporters.SummaryReporter
import org.gradle.api.NamedDomainObjectCollection
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.logging.Logger

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
            summary:SummaryReporter,
            csv: CSVReporter,
            csvSummary: CSVSummaryReporter
    ]

    Logger logger

    NamedDomainObjectCollection<ReporterExtension> reporterExtensions

    @Override public void apply(Project project) {
        this.logger = project.logger
        project.extensions.create("buildtimetracker", BuildTimeTrackerExtension)
        reporterExtensions = project.buildtimetracker.extensions.reporters = project.container(ReporterExtension)
        project.gradle.addBuildListener(new TimingRecorder(this))
    }

    List<AbstractBuildTimeTrackerReporter> getReporters() {
        reporterExtensions.collect { ext ->
            if (REPORTERS.containsKey(ext.name)) {
                return REPORTERS.get(ext.name).newInstance(ext.options, logger)
            }
        }.findAll { ext -> ext != null }
    }
}

class BuildTimeTrackerExtension {
    // Not in use at the moment.
}

class ReporterExtension {
    final String name
    final Map<String, String> options = [:]

    ReporterExtension(String name) {
        this.name = name
    }

    @Override
    String toString() {
        return name
    }

    def methodMissing(String name, args) {
        // I'm feeling really, really naughty.
        if (args.length == 1) {
            options[name] = args[0].toString()
        } else {
            throw new MissingMethodException(name, this.class, args)
        }
    }
}
