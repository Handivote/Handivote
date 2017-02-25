package FilterPattern;

public class ReferendumFactory {

    public Referendum createReferendum(String type){
        switch (type){
            case "simple":
                SimpleReferendum simpleReferendum = new SimpleReferendum();
                break;
            default:
                throw new IllegalArgumentException("Invalid Referendum type: " + type);

        }


        return null;
    }

}
