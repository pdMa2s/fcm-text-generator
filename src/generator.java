import java.io.File;

public class generator {
    public static void main(String[] args){
        checkParameter(args);
        File contextFile = new File(args[0]);
        int order = Integer.parseInt(args[1]);
        double alpha = Double.parseDouble(args[2]);
        int genLength = Integer.parseInt(args[3]);

        TextGenerator generator;
        ContextFileParser parser;

        if(args.length == 5)
            parser = chooseParser(args[4]);
        else
            parser = new LiberalParser();
        String context = parser.parse(contextFile);

        ContextModel contextModel = new ContextModel(order, context);
        ProbabilityModel probabilityModel = new ProbabilityModel(contextModel, alpha);
        ContextModel contextModelBackup;
        ProbabilityModel probabilityModelBackup;
        if(order > 0){
            if(order == 1)
                contextModelBackup = new ContextModel(0, context);
            else
                contextModelBackup = new ContextModel(1, context);
            probabilityModelBackup = new ProbabilityModel(contextModelBackup, alpha);
            generator = new TextGenerator(probabilityModel,probabilityModelBackup,genLength);
        }
        else
            generator = new TextGenerator(probabilityModel, genLength);
        System.out.println(generator.generateText());

    }

    private static void checkParameter(String[] args){
        if(args.length < 4 || args.length > 5){
            printUsage();
        }
        int order = Integer.parseInt(args[1]);
        if(order < 0){
            throw new IllegalArgumentException("ERROR: Order cant be less than zero");
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
        System.out.println("USAGE: java generator <contextFile> <order> <alpha> <genTextLength> <parserType>(Optional)\n"+
                            "<contextFile> - The which contains the context model\n"+
                            "<order> - The order of the finite-context model\n"+
                            "<alpha> - The level of creativity of the text generator\n"+
                            "<lengthText> - The size of the text generated\n"+
                            "<parserType> - This parameter is optional, chose a type of parser for the context file\n"+
                            "lp - A Liberal that does not filter the input text\n"+
                            "rp - A Restrictive parser that removes some punctuation, all the text is upper case. This is the parser used by default"
                            );
        System.exit(1);
    }


}
