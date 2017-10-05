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
            parser = new RestrictiveParser();

        ContextModel contextModel;



        String context = parser.parse(contextFile);
        //System.out.println(context);
        contextModel = new ContextModel(order, context);
        ProbabilityModel probabilityModel = new ProbabilityModel(contextModel, alpha);
        //System.out.println(probabilityModel);

        System.out.println(probabilityModel.entropy());
        
        TextGenerator textGenerator = new TextGenerator(probabilityModel, 100);
        String text = textGenerator.generateText();
        System.out.println(text);
    }

    private static void checkParameterLength(String[] args){
        if(args.length < 4 || args.length > 5){
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
        System.out.println("USAGE: java fcm <contextFile> <order> <alpha> <genTextLength> <parserType>(Optional)\n"+
                            "<contextFile> - The which contains the context model\n"+
                            "<order> - The order of the finite-context model\n"+
                            "<alpha> - The level of creativity of the text generator\n"+
                            "<genTextLength> - The of character to be generated\n"+
                            "<parserType> - This parameter is optional, chose a type of parser for the context file\n"+
                            "   lp - A Liberal that does not filter the input text"+
                            "   rp - A Restrictive parser that removes some punctuation, all the text is upper case. This is the parser used by default"
                            );
        System.exit(1);
    }


}
