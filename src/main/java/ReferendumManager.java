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

    private Referendum referendum;
    private final ScheduledExecutorService scheduler =
            Executors.newScheduledThreadPool(1);

    public void setSchedule(ReferendumFactory rf, Properties properties, Date startDate, Date endDate) {
        //todo error handling
        long delay = startDate.getTime() - System.currentTimeMillis();
        long endDelay = endDate.getTime() - System.currentTimeMillis();
        final Runnable ref = new Runnable() {

            @Override
            public void run() {
                try {

                    referendum = rf.buildReferendum(properties);
                } catch (Exception e) {

                    e.getStackTrace();
                    // scheduling lib. silences exceptions
                    System.out.println(e.toString());
                    System.out.println(e.getMessage());
                }
            }
        };
        final ScheduledFuture<?> refHandle = scheduler.schedule(ref, delay, MILLISECONDS);
        scheduler.schedule(new Runnable() {
            @Override
            public void run() {
                try {
                    referendum.publishResults(referendum.getRefID());
                    refHandle.cancel(true);
                    System.out.println("stopping" + referendum.toString());
                } catch (Exception e) {
                    System.out.println(e.toString());
                    System.out.println(e.getMessage());
                } finally {
                    System.exit(0);
                }

            }
        }, endDelay, MILLISECONDS);

    }


    public static void main(String[] args) {

        ReferendumManager referendumManager = new ReferendumManager();
        Date startDate = null;
        Date endDate = null;
        ReferendumFactory rf = new ReferendumFactory();
        String fName = "";

        if (args.length > 0) {
            fName = args[0];

        } else {
            //todo usage instructions
            fName = "emailtest.properties";
        }

        Properties properties = getProperties(fName);
        String pattern = (String) properties.get("dates.pattern");
        startDate = DateConverter.convertDates(pattern, (String) properties.get("startDate"));
        endDate = DateConverter.convertDates(pattern, (String) properties.get("endDate"));
        referendumManager.setSchedule(rf, properties, startDate, endDate);
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
