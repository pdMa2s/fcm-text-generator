import sun.security.pkcs11.Secmod;

import java.util.*;

public class ProbabilityModel<T,K> {
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

    public double entropy(char caracter){
        if(probabilityUniModel != null){
            return rowEntropy(probabilityUniModel);
        }

        int totalContextOcurrences = contextModel.totalContextOcurrences();
        double entropy = 0;
        for (String term : dictionary) {
            Map<Character, Double> row = probabilityMultiModel.get(term);
            int totalRowOcurrences = getTotalOcurencesOfCharsAfterTerm(contextModel.getOcurrencesAfterTerm(term));
            entropy += rowEntropy(row)*(totalRowOcurrences/totalContextOcurrences);
        }
        return entropy;
    }

    private double rowEntropy(Map<Character, Double> row){

        double rowEntropy = 0;
        for(Map.Entry<Character, Double> entry: row.entrySet()){
            rowEntropy += charEntropy(entry.getValue());
        }

        return rowEntropy;
    }

    private double charEntropy(double prob){
        return -(prob*log2(prob));
    }
    private void fillProbabilityMultiModel(){
        for(String term : dictionary) {
            Map<Character, Integer> ocurrences = contextModel.getOcurrencesAfterTerm(term);
            int totalOcurrences = getTotalOcurencesOfCharsAfterTerm(ocurrences);
            for (Map.Entry<Character, Integer> entry : ocurrences.entrySet()) {
                fillCharProbabilities(term, entry.getKey(), totalOcurrences, entry.getValue());
            }
        }

    }

    private void fillProbabilityUniModel(){
        Map<Character, Integer> ocurrences = contextModel.getOcurrencesAfterTerm(null);
        for(String term : dictionary) {
            int totalOcurrences = getTotalOcurencesOfCharsAfterTerm(ocurrences);
            for (Map.Entry<Character, Integer> entry : ocurrences.entrySet()) {
                probabilityUniModel.put(term.charAt(0), probabilityOfAChar(totalOcurrences, entry.getValue()));
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
    private int getTotalOcurencesOfCharsAfterTerm(Map<Character, Integer> ocurrences){
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
