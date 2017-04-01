import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.*;
import javax.mail.internet.MimeMultipart;
import javax.mail.search.FlagTerm;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class EmailCollector implements VoteCollector {

    private static final String SMTP_HOST_NAME = "pop.gmail.com";
    private static final String SMTP_PORT = "995";
    private static final String from = "extensions@dcs.gla.ac.uk";
    private static final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";


    private static boolean VERBOSE = true;
    private static boolean TESTING = true;
    private static Session session;
    private static Store store;
    private static ArrayList emails;
    private ArrayList questions;
    private UUID refID;
    private static Logger LOGGER = LoggerFactory.getLogger(SimpleTestCollector.class);


    public EmailCollector(ArrayList<Question> questions) {
        this.questions = questions;
    }


    private static Session setupSession() throws Exception {
        //create properties field
        Properties props = new Properties();
        props.put("mail.smtp.host", SMTP_HOST_NAME);
        props.put("mail.smtp.auth", "true");

        props.put("mail.smtp.port", SMTP_PORT);
        props.put("mail.smtp.socketFactory.port", SMTP_PORT);
        props.put("mail.smtp.socketFactory.class", SSL_FACTORY);
        props.put("mail.smtp.socketFactory.fallback", "false");

        props.setProperty("mail.store.protocol", "imaps");

        session = Session.getDefaultInstance(props, null);
        //session.setDebug(true);

        //create the IMAPS store object and connect with the smtp server
        store = session.getStore("imaps");
        // todo encrypt password in config files
        store.connect("imap.gmail.com", "handivote.testing@gmail.com", "poppoppop");
        return session;
    }

     Message[] readMessages(UUID refID) throws Exception {

        if (session == null) setupSession();
        // create the folder object and open it
        Folder inbox = store.getFolder("Inbox");
        inbox.open(Folder.READ_WRITE);
        FlagTerm ft = new FlagTerm(new Flags(Flags.Flag.SEEN), false);
        Message messages[] = new Message[0];
        try {
            messages = inbox.search(ft);
            for (Message m : messages) {
                LOGGER.info(m.getSubject());
                sendAck(m.getSubject().toString());
            }
        } catch (MessagingException e) {
            LOGGER.error( " " + e.getMessage());
        } finally {
            close();
        }
        return messages;
    }

    private static void close() {
        try {
            store.close();
        } catch (MessagingException ee) {
            LOGGER.error(ee.getMessage());
        }
    }


    public void collectEmails(UUID refID)  {
        VoteRecorder voteRecorder = new VoteRecorder(refID, questions);
        Message[] ballots = null;
        try {
            ballots = readMessages(refID);
            for (int i=0; i<ballots.length; i++){
                long timestamp = new Date().getTime();
                String[] content = (getTextFromMessage(ballots[i])).split(" ");
                String[] optionsBallot = Arrays.copyOfRange(content, 3, content.length);
                Vote vote = new Vote(content[0], content[1], timestamp, "1", optionsBallot);
                voteRecorder.recordVote(vote);
            }
        } catch (Exception e) {
            LOGGER.error("Error collecting votes:  " + e.getMessage());
        }
        finally{
            voteRecorder.closeDB();
        }
    }
    private String getTextFromMessage(Message message) throws Exception {
        String result = "";
        if (message.isMimeType("text/plain")) {
            result = message.getContent().toString();
        } else if (message.isMimeType("multipart/*")) {
            MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
            result = getTextFromMimeMultipart(mimeMultipart);
        }
        return result;
    }

    private String getTextFromMimeMultipart(
            MimeMultipart mimeMultipart) throws Exception{
        StringBuilder result = new StringBuilder();
        int count = mimeMultipart.getCount();
        for (int i = 0; i < count; i++) {
            BodyPart bodyPart = mimeMultipart.getBodyPart(i);
            if (bodyPart.isMimeType("text/plain")) {
                result.append("\n").append(bodyPart.getContent());
                break;
            } else if (bodyPart.isMimeType("text/html")) {
                String html = (String) bodyPart.getContent();
                result.append("\n").append(org.jsoup.Jsoup.parse(html).text());
            } else if (bodyPart.getContent() instanceof MimeMultipart){
                result.append(getTextFromMimeMultipart((MimeMultipart) bodyPart.getContent()));
            }
        }
        return result.toString();
    }

    @Override
    public void sendAck(String recipient) {
        if (VERBOSE) {
            LOGGER.info("Sending ACK to :" + recipient);
        }
        try {
            new sendKapowAck(recipient);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }

    @Override
    public void collectVotes(UUID refID) {
        ScheduledExecutorService scheduledExecutorService =
                Executors.newScheduledThreadPool(1);
        scheduledExecutorService.scheduleAtFixedRate(() -> {
            //The repetitive task, say to update Database
            collectEmails(refID);
            Validator validator = new Validator(refID, questions);
            validator.validateVoterPIN();
         }, 0, 2, TimeUnit.MINUTES);
    }
}


