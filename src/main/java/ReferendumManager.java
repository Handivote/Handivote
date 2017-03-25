import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private static Logger LOGGER = LoggerFactory.getLogger(ReferendumManager.class);
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
                    LOGGER.info("Created referendum : " + referendum.getRefID().toString());
                } catch (Exception e) {
                    LOGGER.error(e.getMessage());
                    LOGGER.error(e.toString());
                    LOGGER.debug(String.valueOf(e.getStackTrace()));
                    // scheduling lib. silences exceptions

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
                    LOGGER.info("Completed referendum : " + referendum.getRefID().toString());
                } catch (Exception e) {
                    LOGGER.error(e.getMessage());
                    LOGGER.error(e.toString());
                    LOGGER.debug(String.valueOf(e.getStackTrace()));
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
            System.out.println("opening " + fName);

        } else {
            //todo usage instructions passwords etc,.
            fName = "src/main/resources/test.properties";
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
