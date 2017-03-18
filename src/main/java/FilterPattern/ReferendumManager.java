package FilterPattern;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class ReferendumManager {

    private static Referendum referendum;
    private static final ScheduledExecutorService scheduler =
            Executors.newScheduledThreadPool(1);

    public static void setSchedule(ReferendumFactory rf, Properties properties, Date startDate, Date endDate) {
        long delay = startDate.getTime() - System.currentTimeMillis();
        long endDelay = endDate.getTime() - System.currentTimeMillis();
        System.out.println("time til start : " + delay + " : time til end : " + endDelay + " time now = " + System.currentTimeMillis());
        final Runnable ref = new Runnable() {
            public void run() {
                System.out.println("starting" + referendum.toString());
                referendum = rf.buildReferendum(properties);

            }
        };


        final ScheduledFuture<?> refHandle = scheduler.scheduleWithFixedDelay(ref, delay, delay, MILLISECONDS);
        scheduler.schedule(() -> {

            referendum.publishResults(referendum.getRefID());
            refHandle.cancel(true);
            System.out.println("stopping" + referendum.toString());
            System.exit(0);
        }, endDelay, MILLISECONDS);

    }


    public static void main(String[] args) {
        Date startDate = null;
        Date endDate = null;
        ReferendumFactory rf = new ReferendumFactory();
        String fName = "";

        if (args.length > 0) {
            fName = args[0];
        } else {
            fName = "test.properties";
        }

        Properties properties = getProperties(fName);

        String pattern = (String) properties.get("dates.pattern");
        startDate = DateConverter.convertDates(pattern, (String) properties.get("startDate"));
        endDate = DateConverter.convertDates(pattern, (String) properties.get("endDate"));
        setSchedule(rf, properties, startDate, endDate);
    }

    private static Properties getProperties(String fName) {
        Properties properties = new Properties();

        File file = new File(fName);
        FileInputStream fileInput = null;
        try {
            fileInput = new FileInputStream(file);
            properties.load(fileInput);
            fileInput.close();
        } catch (IOException e) {
            e.getStackTrace();
        }
        return properties;
    }
}
