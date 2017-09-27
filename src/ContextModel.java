import java.util.*;

public abstract class ContextModel {

    private Map<String, Integer[]> multiDimensionalModel;
    private Integer[] uniDimensionalModel;
    private int order;
    private String textModel;
    protected static String ALPHABET;

    public ContextModel(int order, String textModel){
        this.order = order;
        this.textModel = textModel;
        switch (order){
            case 0:
                this.uniDimensionalModel = new Integer[ALPHABET.length()];
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

    public List<String> getTerms(){
        if (order == 0){
            return Arrays.asList(ALPHABET);
        }
        List<String> terms = new ArrayList<>();
        for(Map.Entry<String,Integer[]> entry: multiDimensionalModel.entrySet()){
            terms.add(entry.getKey());
        }
        return terms;
    }

    public int[] getTermNrOfOcurrences(String term){
        //implement
        return null;
    }
    protected void createUniDimensionalModel(){
        for(int i = 0; i< textModel.length()-1; i++ ){
            char followingChar = textModel.charAt(i+1);
            incrementCharOcurrence(uniDimensionalModel, followingChar);
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
        if(multiDimensionalModel.containsKey(term)){
            Integer[] termEntrys = multiDimensionalModel.get(term);
            incrementCharOcurrence(termEntrys,followingChar);
        }
        else{
            multiDimensionalModel.put(term,new Integer[ALPHABET.length()]);
            Integer[] termEntrys = multiDimensionalModel.get(term);
            incrementCharOcurrence(termEntrys, followingChar);
        }
    }
    protected void incrementCharOcurrence(Integer[] termEntrys, char c){
        if(termEntrys[charIndex(c)] == null)
            termEntrys[charIndex(c)] = 1;
        else
            termEntrys[charIndex(c)]++;

    }
    protected int charIndex(char c){
        int temp = (int)c;
        int temp_integer = 64; //for upper case
        return temp-temp_integer-1;

    }

    @Override
    public String toString() {
        if(order >= 1)
            return multiDimensionalModel.toString();
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < ALPHABET.length(); i++){
            sb.append(ALPHABET.charAt(i)+"   ");
        }
        sb.append("\n");
        for(int i = 0; i < uniDimensionalModel.length; i++){
            sb.append(uniDimensionalModel[i]+"   ");
        }
        return sb.toString();
    }
}
