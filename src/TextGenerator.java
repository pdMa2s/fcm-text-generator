
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

public class TextGenerator {
    
    private ProbabilityModel probabilityModel;
    private int lengthText;
    private int order;
    private StringBuilder text;
    private Map<String, Map<Character, Double>> probabilityMultiModel;
    private Map<String, Map<Character, Double>> probabilityMultiModelBackup;
    private Map<Character, Double> probabilityUniModel;
    private Map<Character, Double> probabilityUniModelBackup;


    public TextGenerator(ProbabilityModel probabilityModel,int lengthText) {
        if(lengthText <= 0){
            throw new IllegalArgumentException("ERROR The length of the text cant be less or equal to zero");
        }
        this.probabilityModel = probabilityModel;
        this.lengthText = lengthText;
        this.text = new StringBuilder();
        this.order = this.probabilityModel.getOrder();
        if(order > 0){
            probabilityMultiModel = this.probabilityModel.getProbabilityMultiModel();
        }
        else{
            probabilityUniModel = this.probabilityModel.getProbabilityUniModel();
        }
    }
    public TextGenerator(ProbabilityModel probabilityModel,ProbabilityModel probabilityModelBackup,int lengthText) {
        this(probabilityModel, lengthText);
        if(probabilityModelBackup.getOrder() == 0)
            this.probabilityUniModelBackup = probabilityModelBackup.getProbabilityUniModel();
        else
            this.probabilityMultiModelBackup = probabilityModelBackup.getProbabilityMultiModel();

    }
    
    public String generateText() {
        if (order > 0) {
            List<String> keys = new ArrayList<>(probabilityMultiModel.keySet());
            text.append(getInitialTermOrCharacter(keys));
            while (text.length() < lengthText) {
                String term = text.substring(text.length()-order);
                if (probabilityMultiModel.containsKey(term)){
                    text.append(getNextChar(probabilityMultiModel.get(term)));
                }
                else{
                    text.append(getBackupChar(term));
                }
            }
            return text.toString();
        }
        else {
            return generateTextForUniModel();
        }
    }
    private char getBackupChar(String term){
        char c;
        c = term.charAt(term.length()-1);
        if(probabilityUniModelBackup == null)
            return getNextChar(probabilityMultiModelBackup.get(c+""));
        else
            return getNextChar(probabilityUniModelBackup);
    }
    private String generateTextForUniModel(){
        List<String> keys = new ArrayList<>(probabilityUniModel.keySet().stream().
                map(key->key.toString()).collect(Collectors.toList()));
        text.append(getInitialTermOrCharacter(keys));
        while (text.length() < lengthText) {
            text.append(getNextChar(probabilityUniModel));
        }
        return text.toString();
    }

    private String getInitialTermOrCharacter(List<String> keys) {
        int size = keys.size();
        Random random = new Random();
        int startElement = random.nextInt(size);
        return keys.get(startElement);
    }
    
    private char getNextChar(Map<Character, Double> probabilityModel) {
        Random random = new Random();
        double numberToCompare = random.nextDouble();
        double total = 0;
        char nextChar = 0;
        for(Map.Entry<Character,Double> entry:probabilityModel.entrySet()) {
            total += entry.getValue();
            if (total >= numberToCompare) {
                nextChar = entry.getKey();
                break;
            }
        }
        return nextChar;
    }

}