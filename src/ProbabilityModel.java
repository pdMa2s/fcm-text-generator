
import java.util.*;

public class ProbabilityModel {
    private Map<String, Map<Character, Double>> probabilityMultiModel;
    private Map<Character, Double> probabilityUniModel;

    private Set<Character> alphabet;
    private ContextModel contextModel;
    private double alpha;

    public ProbabilityModel(ContextModel model, double alpha){
        this.alphabet = model.getAlphabet();
        this.contextModel = model;

        if(alpha < 0 || alpha > 1)
            throw new IllegalArgumentException("Alpha cant be less than zero or greater than one");
        this.alpha = alpha;

        if (contextModel.getOrder() == 0) {
            probabilityUniModel = new HashMap<>();
            fillProbabilityUniModel();
        }
        else{
            probabilityMultiModel = new HashMap<>();
            fillProbabilityMultiModel();
        }
    }

    public double entropy(){
        if(contextModel.getOrder() == 0){
            return rowEntropy(probabilityUniModel);
        }

        int totalContextOcurrences = contextModel.totalContextOcurrences();
        double entropy = 0;
        for(String term : probabilityMultiModel.keySet()) {
            Map<Character, Double> row = probabilityMultiModel.get(term);
            int totalRowOcurrences = getTotalOcurencesOfRow(contextModel.getOcurrencesForOrderHigherThanZero(term));

            entropy += rowEntropy(row)*((float)totalRowOcurrences/(float) totalContextOcurrences);
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
        return prob == 0 ? 0 : -(prob*log2(prob));
    }

    private void fillProbabilityMultiModel(){
        for(String term : contextModel.getTermsForOrderHigherThanZero()) {

            Map<Character, Integer> ocurrences = contextModel.getOcurrencesForOrderHigherThanZero(term);
            int totalOcurrences = getTotalOcurencesOfRow(ocurrences);
            for (char c : alphabet) {
                Integer nrOcurrences = ocurrences.get(c);
                if(nrOcurrences == null)
                    nrOcurrences = 0;
                fillCharProbabilities(term, c, totalOcurrences, nrOcurrences);
            }
        }

    }

    private void fillProbabilityUniModel(){
        Map<Character, Integer> ocurrences = contextModel.getOcurrencesForOrderEqualToZero();
        int totalOcurrences = getTotalOcurencesOfRow(ocurrences);
        for(Character term : ocurrences.keySet()) {
            probabilityUniModel.put(term, probabilityOfAChar(totalOcurrences, ocurrences.get(term)));
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
    private int getTotalOcurencesOfRow(Map<Character, Integer> ocurrences){
        int sum = 0;
        for(char k : ocurrences.keySet()){
            sum += ocurrences.get(k);
        }
        return sum;
    }


    private double probabilityOfAChar(int total, int nrOcurrences){
        return (nrOcurrences + alpha)/(total +(alpha *alphabet.size()));
    }
    private double log2( double a )
    {
        return logb(a,2);
    }
    private double logb( double a, double b )
    {
        return Math.log(a) / Math.log(b);
    }
    
    public Map<String, Map<Character, Double>> getProbabilityMultiModel() {
        return probabilityMultiModel;
    }

    public Map<Character, Double> getProbabilityUniModel() {
        return probabilityUniModel;
    }
    
    public int getOrder() {
        return contextModel.getOrder();
    }

    @Override
    public String toString(){
        if(contextModel.getOrder() == 0)
            return probabilityUniModel.toString();
        StringBuilder sb = new StringBuilder();
        for(String k : probabilityMultiModel.keySet()){
            sb.append(k+":"+probabilityMultiModel.get(k)+"\n");
        }
        return sb.toString();
    }
}
