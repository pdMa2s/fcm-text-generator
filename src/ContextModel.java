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
        switch (order){
            case 0:
                this.uniDimensionalModel = new HashMap<>();
                createUniDimensionalModel();
                break;
            case 1:
                this.multiDimensionalModel = new HashMap<>();
                createMultiDimensionalModel();
                break;
            default:
                throw new IllegalArgumentException();
        }
    }

    public int getOrder() {
        return order;
    }


    public  Map<Character, Integer> getOcurrencesAfterTerm(String term){
        return uniDimensionalModel == null ? multiDimensionalModel.get(term) : uniDimensionalModel;
    }
    public int totalContextOcurrences(){
        int totalOcurrences = 0;
        if(uniDimensionalModel != null){
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
            dictionary.add(term);
        }
    }

    protected void addFollowingCharOcurrence(String term, char followingChar){
        if(multiDimensionalModel.containsKey(term)){
            Map<Character, Integer> termEntrys = multiDimensionalModel.get(term);
            incrementCharOcurrence(termEntrys,followingChar);
        }
        else{
            multiDimensionalModel.put( term,new HashMap<>());
            Map<Character, Integer> termEntrys = multiDimensionalModel.get(term);
            incrementCharOcurrence(termEntrys, followingChar);
        }
    }
    protected void incrementCharOcurrence(Map<Character,Integer> termEntrys, char c){
        if(termEntrys.get(c) == null)
            termEntrys.put(c,1);
        else {
            int nrOcurrences = termEntrys.get(c);
            termEntrys.put(c,nrOcurrences++);
        }
    }


}
