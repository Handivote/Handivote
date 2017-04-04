import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
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
    private static final Boolean TESTING = true;

    public void setSchedule(ReferendumFactory rf, Properties properties, Date startDate, Date endDate) {

        Date today = Calendar.getInstance().getTime();
        long delay = startDate.getTime() - System.currentTimeMillis();
        long endDelay = endDate.getTime() - System.currentTimeMillis();
        if(!TESTING) {
            if (startDate.compareTo(endDate) > 0 || startDate.compareTo(today) < 0) {
                LOGGER.error("Invalid Dates --  start date: " + startDate + " end date: " + endDate);
                System.exit(1);
            }
            final Runnable ref = new Runnable() {
                @Override
                public void run() {
                    try {
                        // scheduling lib. silences exceptions
                        referendum = rf.buildReferendum(properties);
                        LOGGER.info("Created referendum : " + referendum.getRefID().toString() + " @" + System.currentTimeMillis());
                    } catch (Exception e) {
                        LOGGER.error(e.getMessage());
                    }
                }
            };
            final ScheduledFuture<?> refHandle = scheduler.schedule(ref, delay, MILLISECONDS);
            scheduler.schedule(new Runnable() {
                @Override
                public void run() {
                    try {
                        // scheduling lib. silences exceptions
                        refHandle.cancel(false);
                        referendum.publishResults(referendum.getRefID());
                        LOGGER.info("Completed referendum : " + referendum.getRefID().toString() + " @" + System.currentTimeMillis());
                    } catch (Exception e) {
                        LOGGER.error(e.getMessage());
                    } finally {
                        scheduler.shutdown();
                    }
                }
            }, endDelay, MILLISECONDS);
        }
        else{
            referendum = rf.buildReferendum(properties);
            LOGGER.info("Created referendum : " + referendum.getRefID().toString() + " @" + System.currentTimeMillis());
            referendum.publishResults(referendum.getRefID());
            LOGGER.info("Completed referendum : " + referendum.getRefID().toString() + " @" + System.currentTimeMillis());
        }
    }


    public static void main(String[] args) {
        ReferendumManager referendumManager = new ReferendumManager();
        Date startDate;
        Date endDate;
        ReferendumFactory rf = new ReferendumFactory();
        String fName = "";

        if (args.length > 0) {
            fName = args[0];
            LOGGER.info("opening " + fName);

        } else {
            //todo usage instructions passwords etc,.
            fName = "src/main/resources/test.properties";
        }

        Properties properties = PropsLoader.getProperties(fName);
        String pattern = (String) properties.get("dates.pattern");
        startDate = DateConverter.convertDates(pattern, (String) properties.get("startDate"));
        endDate = DateConverter.convertDates(pattern, (String) properties.get("endDate"));
        referendumManager.setSchedule(rf, properties, startDate, endDate);
    }

}
