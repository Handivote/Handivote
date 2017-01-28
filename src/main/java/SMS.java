public class SMS {

    private String card="", PIN="", option="";
    private String fromnumber="";
    private boolean validSMS=false;
    private String originalsms="";


    public SMS() {
    }

    public SMS(String _from, String _card, String _PIN, String _option, String _orig) {
        card = _card;
        fromnumber=_from;
        PIN = _PIN;
        option = _option;
        originalsms=_orig;
        validSMS=true;
    }

    public boolean isNumber(String n) {
        try {
            Integer i = new Integer(n);
            return true;
        }
        catch (Exception ee) {
            return false;
        }
    }

    public void print() {
        if (validSMS)
            System.out.println("card="+card+",pin="+PIN + " option= " + option);
        else System.out.println("Invalid message");

    }
}