package BSPQ25_E6.taskmanager.performance;

import BSPQ25_E6.taskmanager.service.UserService;
import com.github.noconnor.junitperf.*;
import com.github.noconnor.junitperf.reporting.providers.HtmlReportGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@ExtendWith(JUnitPerfInterceptor.class)
public class DurationUserTest 
{

    @Autowired
    private UserService userService;

    @JUnitPerfTestActiveConfig
    private static final JUnitPerfReportingConfig CONFIG =
        JUnitPerfReportingConfig.builder()
            .reportGenerator(new HtmlReportGenerator("target/reports/duration-user-report.html"))
            .build();

    @Test
    @JUnitPerfTest(threads = 10, durationMs = 5000, warmUpMs = 1000)
    @JUnitPerfTestRequirement(percentiles = "95:300ms")
    public void testDurationGetAllUsers() 
    {
        userService.getAllUsers();
    }
}
