import java.io.File;

public class fcm {

    public static void main(String[] args){
        checkParameterLength(args);
        File contextFile = new File(args[0]);
        int order = Integer.parseInt(args[1]);
        double alpha = Double.parseDouble(args[2]);
        ContextFileParser parser;

        if(args.length == 4)
            parser = chooseParser(args[3]);
        else
            parser = new LiberalParser();

        ContextModel contextModel;
        String context = parser.parse(contextFile);
        //System.out.println(context);
        contextModel = new ContextModel(order, context);
        System.out.println(contextModel);
        ProbabilityModel probabilityModel;
        probabilityModel = new ProbabilityModel(contextModel, alpha);
        //System.out.println(probabilityModel);
        System.out.println("Entropy: " + probabilityModel.entropy());
    }

    private static void checkParameterLength(String[] args){
        if(args.length < 3 || args.length > 4){
            printUsage();
        }
    }
    private static ContextFileParser chooseParser(String parserTag){
        switch (parserTag){
            case "lp":
                return new LiberalParser();
            case "rp":
                return new RestrictiveParser();
            default:
                System.err.println("ERROR Unknown parser type");
                printUsage();
        }
        return null;
    }
    private static void printUsage(){
        System.out.println("USAGE: java fcm <contextFile> <order> <alpha> <parserType>(Optional)\n"+
                            "<contextFile> - The which contains the context model\n"+
                            "<order> - The order of the finite-context model\n"+
                            "<alpha> - The level of creativity of the text generator\n"+
                            "<parserType> - This parameter is optional, chose a type of parser for the context file\n"+
                            "   lp - A Liberal that does not filter the input text\n"+
                            "   rp - A Restrictive parser that removes some punctuation, all the text is upper case. This is the parser used by default"
                            );
        System.exit(1);
    }


}
