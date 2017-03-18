import java.util.UUID;

public interface VoteCollector{
    void collectVotes(UUID refID);
    void sendAck(Vote vote);
}
