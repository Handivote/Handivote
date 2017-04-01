import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public class foo {
/*
    public static void main(final String... args) throws InterruptedException, ExecutionException {
        // creates thread pool with 2 thread
        final ScheduledExecutorService schExService = Executors.newScheduledThreadPool(8);
        // Object creation of runnable thread.
        final Runnable ob = new foo().new DemoThread();
        // Thread scheduling
        schExService.scheduleWithFixedDelay(ob, 2, 3, TimeUnit.SECONDS);
        // waits for termination for 30 seconds only
        schExService.awaitTermination(10, TimeUnit.SECONDS);
        // shutdown now.
        schExService.shutdownNow();
        System.out.println("Shutdown Complete");
    }
    // Runnable thread
    class DemoThread implements Runnable {
        @Override
        public void run() {
            int cnt = 0;
            for (; cnt < 8; cnt++) {

            }
            System.out.println("Done");
        }
    }
*/

        // Find your Account Sid and Token at twilio.com/user/account
        public static final String ACCOUNT_SID = "ACee19eb4135726410964f0789c6e6af80";
        public static final String AUTH_TOKEN = "004f9a0b44f125d559aa3d277fd4d880";

        public static void main(String[] args) {
            Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

            Message message = Message.creator(new PhoneNumber("+15558675309"),
                    new PhoneNumber("+15017250604"),
                    "This is the ship that made the Kessel Run in fourteen parsecs?").create();

            System.out.println(message.getSid());
        }
    }

