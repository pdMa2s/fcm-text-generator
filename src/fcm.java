import java.io.File;

public class fcm {
    public static void main(String[] args){
        if(args.length != 3){
            System.err.println("USAGE: java fcm <contextFile> <order> <alpha>");
            System.exit(1);
        }
        File contextFile = new File(args[0]);
        int order = Integer.parseInt(args[1]);
        double alpha = Double.parseDouble(args[2]);


        ContextModel contextModel;

        ContextFileParser parser= new RestrictiveParser();

        String context = parser.parse(contextFile);

        contextModel = new ContextModel(order, context);
        ProbabilityModel probabilityModel = new ProbabilityModel(contextModel, alpha);
        //System.out.println(probabilityModel);

        System.out.println(probabilityModel.entropy());
        
        TextGenerator textGenerator = new TextGenerator(probabilityModel, 100);
        String text = textGenerator.generateText();
        System.out.println(text);
    }


}
