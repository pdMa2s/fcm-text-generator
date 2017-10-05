
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
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
    private Map<Character, Double> probabilityUniModel;
    
    public TextGenerator(ProbabilityModel probabilityModel, int lengthText) {
        this.probabilityModel = probabilityModel;
        this.lengthText = lengthText;
        this.text = new StringBuilder();
        this.order = this.probabilityModel.getOrder();
        probabilityMultiModel = this.probabilityModel.getProbabilityMultiModel();
        probabilityUniModel = this.probabilityModel.getProbabilityUniModel();
    }
    
    public String generateText() {
        if (order > 0) {
            List<String> keys = new ArrayList<>(probabilityMultiModel.keySet());
            text.append(getInitialTermOrCharacter(keys));
            while (text.length() < lengthText) {
                String term = text.substring(text.length()-order);
                if (probabilityMultiModel.containsKey(term))
                    text.append(getNextChar(probabilityMultiModel.get(term)));
                else
                    text.append(getNextChar());
            }
            return text.toString();
        }
        else {
            List<String> keys = new ArrayList<>(probabilityUniModel.keySet().stream().
                    map(key->key.toString()).collect(Collectors.toList()));
            text.append(getInitialTermOrCharacter(keys));
            while (text.length() < lengthText) {
                text.append(getNextChar());
            }
            return text.toString();
        }
    }

    private String getInitialTermOrCharacter(List<String> keys) {
        int size = keys.size();
        Random random = new Random();
        int startElement = random.nextInt(size);
        return keys.get(startElement);
    }

    private char getNextChar() {
        Random random = new Random();
        double numberToCompare = random.nextDouble();
        double total = 0;
        char nextChar = 0;
        for(Map.Entry<Character,Double> entry:probabilityUniModel.entrySet()) {
            total += entry.getValue();
            if (total >= numberToCompare) {
                nextChar = entry.getKey();
                break;
            }
        }
        return nextChar;
    }
    
    private char getNextChar(Map<Character, Double> probabilityModel) {
        Map<Character, Double> sorted = probabilityModel.entrySet().stream()
                    .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (e1, e2) -> e1, LinkedHashMap::new));     
        Map.Entry<Character,Double> entry=sorted.entrySet().iterator().next();
        return entry.getKey();
    }

}