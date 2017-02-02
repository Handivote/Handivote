
public class VoteParser {

    private static final boolean VERBOSE = false;

    public static Details extractDetails(String str) {

        Details details = new Details();
        str = str.replaceAll("\\p{Cntrl}", "");
        str = str.replaceAll("[^\\p{Print}]", "");
        int pos = str.indexOf(" ");
        while (pos>=0) {
            str = str.replace(" ","");
            pos = str.indexOf(" ");
        }
        str = str.replace("\n", "");
        if (str.length() >= 9) { //todo:  change magic numbers to variables
            details.card = str.substring(0,4);
            details.PIN = str.substring(4,8);
            details.option = str.substring(8,str.length()).trim();
            if ((isNumber(details.card)) &&
                    (isNumber(details.PIN))) details.detailsExtracted=true;
            if (VERBOSE) System.out.println("Card: "+details.card + " PIN: " + details.PIN +
                    " Option: " + details.option + " " + details.detailsExtracted);

        }
        return details;
    }

    public static boolean validateMessageFormat(String from, String subject) {

        int pos = from.indexOf("@kapow.co.uk");

        //System.out.println(from);
        if (pos<0) return false;
        // subject and from must be the same
        // System.out.println(pos);
        pos = from.indexOf(subject);
        //System.out.println(pos);
        if (pos<0) return false;
        if (VERBOSE) System.out.println("numeric"+ isNumber(subject));
        return isNumber(subject);
    }

    private static boolean isNumber(String n) {
        try {
            Integer i = new Integer(n);
            return true;
        }
        catch (NumberFormatException e) {
            return false;
        }
    }

}
