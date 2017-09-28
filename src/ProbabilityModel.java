import org.omg.CORBA.INTERNAL;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ProbabilityModel {
    private Map<String, Map<Character, Double>> probabilityMultiModel;
    private Map<Character, Double> probabilityUniModel;

    private Set<String> dictionary;
    private ContextModel contextModel;
    private double alpha;

    public ProbabilityModel(ContextModel model, double alpha){
        this.dictionary = model.getDictionary();
        this.contextModel = model;
        this.alpha = alpha;
        if(model.getOrder() > 0) {
            probabilityMultiModel = new HashMap<>();
            fillProbabilityMultiModel();
        }
        else {
            probabilityUniModel = new HashMap<>();
            fillProbabilityUniModel();
        }

    }

    private void fillProbabilityMultiModel(){
        for(String term : dictionary) {
            Map<Character, Integer> ocurrences = contextModel.getOcurrencesAfterTerm(term);
            int totalOcurrences = getTotalOcurencesOfCharAfterTerm(ocurrences);
            for (Map.Entry<Character, Integer> entry : ocurrences.entrySet()) {
                fillCharProbabilities(term, entry.getKey(), totalOcurrences, entry.getValue());
            }
        }

    }

    private void fillProbabilityUniModel(){
        Map<Character, Integer> ocurrences = contextModel.getOcurrencesAfterTerm(null);
        for(String term : dictionary) {
            int totalOcurrences = getTotalOcurencesOfCharAfterTerm(ocurrences);
            for (Map.Entry<Character, Integer> entry : ocurrences.entrySet()) {
                fillCharProbabilities(term, entry.getKey(), totalOcurrences, entry.getValue());
            }
        }
    }

    private void fillCharProbabilities(String term ,char c,int total, int nrOcurrences){
        Map<Character, Double> probabilities;
        if(probabilityMultiModel.containsKey(term)) 
            probabilities = probabilityMultiModel.get(term);
        
        else
            probabilities = new HashMap<>();
        
        probabilities.put(c,probabilityOfAChar(total, nrOcurrences));
        probabilityMultiModel.put(term,probabilities);
    }
    private int getTotalOcurencesOfCharAfterTerm(Map<Character, Integer> ocurrences){
        int sum = 0;
        for(Map.Entry<Character, Integer> entry : ocurrences.entrySet()){
            sum += entry.getValue();
        }
        return sum;
    }


    private double probabilityOfAChar(int total, int nrOcurrences){
        return (nrOcurrences + alpha)/(total +(alpha *dictionary.size()));
    }
    private double log2( double a )
    {
        return logb(a,2);
    }
    private double logb( double a, double b )
    {
        return Math.log(a) / Math.log(b);
    }

}
