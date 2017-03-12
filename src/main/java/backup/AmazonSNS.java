package backup;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.MessageAttributeValue;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;

import java.util.HashMap;
import java.util.Map;


public class AmazonSNS {
    public static void main(String[] args) {
        AWSCredentials awsCredentials = new BasicAWSCredentials("YOUR_Client_ID", "YOUR_Client_secret");
        final AmazonSNSClient client = new AmazonSNSClient(awsCredentials);
        client.setRegion(Region.getRegion(Regions.EU_WEST_1));

        AmazonSNSClient snsClient = new AmazonSNSClient(awsCredentials);
        String message = "My SMS message";
        String phoneNumber = "+1XXX5550100";
        Map<String, MessageAttributeValue> smsAttributes =
                new HashMap<String, MessageAttributeValue>();
        //<set SMS attributes>
        sendSMSMessage(snsClient, message, phoneNumber, smsAttributes);
    }

    public static void sendSMSMessage(AmazonSNSClient snsClient, String message,
                                      String phoneNumber, Map<String, MessageAttributeValue> smsAttributes) {
        PublishResult result = snsClient.publish(new PublishRequest()
                .withMessage(message)
                .withPhoneNumber(phoneNumber)
                .withMessageAttributes(smsAttributes));
        System.out.println(result); // Prints the message ID.
    }
}
