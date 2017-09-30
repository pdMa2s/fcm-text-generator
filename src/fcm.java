import java.io.File;

public class fcm {
    public static void main(String[] args){
        double alpha = 0;
        File contextFile = null;
        int order = -1;
        ContextModel contextModel;

        parseArgs(args, contextFile, order, alpha);
        ContextFileParser parser= null;

        String context = parser.parse(contextFile);
        contextModel = new ContextModel(order, context);
        System.out.println(contextModel);
    }

    static void parseArgs(String[] args , File contextFile, int order, double alpha){
        if(args.length != 3){
            System.err.println("USAGE: java fcm <contextFile> <order> <alpha>");
            System.exit(1);
        }
        contextFile = new File(args[0]);
        order = Integer.parseInt(args[1]);
        alpha = Double.parseDouble(args[2]);

        if(order < 0 || alpha < 0)
            throw new IllegalArgumentException("The order and alpha can't less than 0");
    }
}
