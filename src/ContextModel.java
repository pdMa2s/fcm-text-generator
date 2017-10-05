import java.util.*;

public class ContextModel {

    private Map<String, Map<Character, Integer>> multiDimensionalModel;
    private Map<Character, Integer> uniDimensionalModel;
    private int order;
    private String textModel;
    private Set<String> dictionary;

    public ContextModel(int order, String textModel){
        this.order = order;
        this.textModel = textModel;
        this.dictionary = new TreeSet<>();
        if(order < 0)
            throw new IllegalArgumentException("The order can't be less than zero");

        this.uniDimensionalModel = new HashMap<>();
        createUniDimensionalModel();

        this.multiDimensionalModel = new HashMap<>();
        createMultiDimensionalModel();

    }

    public int getOrder() {
        return order;
    }

    public Set<String> getTermsForOrderHigherThanZero(){
        return multiDimensionalModel.keySet();
    }

    public Set<Character> getTermsForOrderEqualToZero(){
        return uniDimensionalModel.keySet();
    }

    public  Map<Character, Integer> getOcurrencesForOrderHigherThanZero(String term){
        return multiDimensionalModel.get(term);
    }
    public  Map<Character, Integer> getOcurrencesForOrderEqualToZero(){
        return uniDimensionalModel;
    }
    public int totalContextOcurrences(){
        int totalOcurrences = 0;
        if(order == 0){
            for(Character c: uniDimensionalModel.keySet()){
                totalOcurrences += uniDimensionalModel.get(c);
            }
        }
        else{
            for(String term : multiDimensionalModel.keySet()){
                for(Character c: multiDimensionalModel.get(term).keySet()){
                    totalOcurrences += multiDimensionalModel.get(term).get(c);
                }
            }
        }
        return totalOcurrences;
    }
    public Set<String> getDictionary(){
        return dictionary;
    }
    protected void createUniDimensionalModel(){
        for(int i = 0; i< textModel.length()-1; i++ ){
            char followingChar = textModel.charAt(i+1);
            incrementCharOcurrence(uniDimensionalModel, followingChar);
            dictionary.add(followingChar+"");
        }
    }

    protected void createMultiDimensionalModel(){
        for (int i = 0; i < textModel.length() - order; i += order){
            String term = textModel.substring(i,i+order);
            char nextChar = textModel.charAt(i+order);
            addFollowingCharOcurrence(term, nextChar);
        }
    }

    protected void addFollowingCharOcurrence(String term, char followingChar){
        if(!multiDimensionalModel.containsKey(term))
            multiDimensionalModel.put( term,new HashMap<>());

        Map<Character, Integer> termEntrys = multiDimensionalModel.get(term);
        incrementCharOcurrence(termEntrys, followingChar);
    }
    protected void incrementCharOcurrence(Map<Character,Integer> termEntrys, char c){
        if(termEntrys.get(c) == null)
            termEntrys.put(c,1);
        else {
            int nrOcurrences = termEntrys.get(c);
            nrOcurrences += 1;
            termEntrys.put(c,nrOcurrences);
        }
    }
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        if(this.order == 0)
            return uniDimensionalModel.toString();
        for(String key : multiDimensionalModel.keySet()){
            sb.append(key + ":"+ multiDimensionalModel.get(key) +"\n");
        }
        return sb.toString();
    }

}
