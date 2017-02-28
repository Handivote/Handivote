
package FilterPattern;

import backup.VoteParser;

import javax.mail.*;
import javax.mail.search.FlagTerm;
import java.util.*;

public class EmailCollector implements VoteCollector {

    private static final String SMTP_HOST_NAME = "pop.gmail.com";
    private static final String SMTP_PORT = "995";
    private static final String from = "extensions@dcs.gla.ac.uk";
    private static final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";


    private static boolean VERBOSE = true;
    private static Session session;
    private static Store store;
    private static ArrayList emails;

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
        store.connect("imap.gmail.com", "handivote.testing@gmail.com", "poppop12");
        return session;
    }

    static Message[] readMessages() throws Exception {

        if (session == null) setupSession();
        //System.out.println(store);

        // create the folder object and open it
        Folder inbox = store.getFolder("Inbox");
        inbox.open(Folder.READ_WRITE);
        FlagTerm ft = new FlagTerm(new Flags(Flags.Flag.SEEN), false);
        Message messages[] = new Message[0];
        // Message messages[] =  MockEmailRetriever.getMails(); // remove after testing is complete
        try {
            messages = inbox.search(ft);
            for (Message m : messages) {
                System.out.println(m.getFrom().length + " " + m.getContent());
                emails.add(VoteParser.extractDetails(m.getSubject() + m.getContent()));

            }
        } catch (MessagingException e) {
            e.printStackTrace();
            e.getMessage();
        } finally {
            // retrieve the messages from the folder in an array and print it
            System.out.println(messages.length + " Messages in inbox");
            close();
        }
        return messages;
    }

    private static void close() {
        try {
            store.close();
        } catch (MessagingException ee) {

        }
    }

    public static void main(String[] args) {
        if (args.length > 0) VERBOSE = true;
        try {
            EmailCollector.readMessages();
        } catch (Exception ee) {
            ee.printStackTrace();
        }
        System.exit(0);

    }

    @Override
    public void collectVotes(UUID refID)  {
        RawVoteRecorder rawVoteRecorder = new RawVoteRecorder(refID);
        Message[] ballots = null;
        try {
            ballots = readMessages();
            for (int i=0; i<ballots.length; i++){
                long timestamp = new Date().getTime();
                String[] content = ((String) ballots[i].getContent()).split(" ");
                String[] optionsBallot = Arrays.copyOfRange(content, 3, content.length);
                Vote vote = new Vote(content[1], content[2], timestamp, "", optionsBallot);
                rawVoteRecorder.recordVote(vote);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally{
            rawVoteRecorder.closeDB();
        }
    }


    @Override
    public void sendAck() {
        //TODO

    }
}
