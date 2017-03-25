import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.UUID;

public class SMSCollector implements VoteCollector {
    private static Logger LOGGER = LoggerFactory.getLogger(SMSCollector.class);

    public SMSCollector(ArrayList<Question> questions) {
    }

    @Override
    public void collectVotes(UUID refID) {

    }

    @Override
    public void sendAck(String recipient) {

    }
}
