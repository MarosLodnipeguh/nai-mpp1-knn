import java.util.ArrayList;
import java.util.List;

public class KNN {

    private int k;

    private List<List<String>> trainset;
    private List<List<String>> testset;
    private List<String> attributes;



    public KNN(int k, List<List<String>> trainset, List<List<String>> testset, List<String> attributes) {
        this.k = k;

        this.trainset = trainset;
        this.testset = testset;
        this.attributes = attributes;
    }

    public void knnstart () {
        List<Entry> entries = new ArrayList<>();

        for (List<String> row : trainset) {
            entries.add(createEntry(row));
        }

        for (Entry entry : entries) {

            fillNeighbors(entry, entries);
            getKNeighbors(entry, k);
            System.out.println("Entry: " + entry.getVectors() + " - " + entry.getName());
            List<Entry> neighbors = entry.getkNeighbors();
            for (Entry neighbor : neighbors) {
                System.out.println("Neighbor: " + neighbor.getVectors() + " - " + neighbor.getName());
            }
            System.out.println("---------------------------------------------------");
        }


    }

    // fill the distancesToAll map of each entry - the distance to all other entries
    public void fillNeighbors (Entry entry, List<Entry> entries) {
        for (Entry neighbor : entries) {
            if (entry != neighbor) {
                double dist = calculateDistance(entry, neighbor);
                entry.getDistancesToAll().put(neighbor, dist);
            }
        }
    }

    // calculate the distance between two entries
    public double calculateDistance (Entry starting, Entry neighbor) {
        List <String> vectors = starting.getVectors();
        List <String> neighborVectors = neighbor.getVectors();
        double distance = 0;

        for (int i = 0; i < vectors.size(); i++) {
            double a = Double.parseDouble(vectors.get(i));
            double b = Double.parseDouble(neighborVectors.get(i));
            double diff = a - b;
            distance += Math.pow(diff, 2);
        }

        return Math.sqrt(distance);
    }

    // set the k nearest neighbors
    public void getKNeighbors (Entry entry, int k) {
        entry.getDistancesToAll().entrySet().stream()
                .sorted((e1, e2) -> e1.getValue().compareTo(e2.getValue()))
                .limit(k)
                .forEach(e -> entry.getkNeighbors().add(e.getKey()));
    }

    public Entry createEntry(List<String> row) {
        int size = row.size();

        List<String> vectors = row.subList(0, size-2);
        String name = row.getLast();
        return new Entry(vectors, name, k);
    }



    public void setK(int k) {
        this.k = k;
    }

    public void classify() {
        System.out.println("Classify");
    }


}
