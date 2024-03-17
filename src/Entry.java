import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Entry {

    private final List<String> vectors;
    private final String name;
    private Map<Entry, Double> distancesToAll;
    private String classificationGroup;

    public Entry(List<String> vectors, String name) {
        this.vectors = vectors;
        this.name = name;
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


}
