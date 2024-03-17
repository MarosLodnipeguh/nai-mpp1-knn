import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Entry {

    private final List<String> vectors;
    private final String name;
    int k;
    private List<Entry> kNeighbors;
    private Map<Entry, Double> distancesToAll;
    private String classificationGroup;

    public Entry(List<String> vectors, String name, int k) {
        this.vectors = vectors;
        this.name = name;
        this.k = k;
        this.kNeighbors = new ArrayList<>();
        this.distancesToAll = new HashMap<>();
    }

    public List<String> getVectors() {
        return vectors;
    }

    public String getName () {
        return name;
    }

    public Map<Entry, Double> getDistancesToAll () {
        return distancesToAll;
    }

    public List<Entry> getkNeighbors () {
        return kNeighbors;
    }

}
