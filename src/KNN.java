import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KNN {

    private int k;

    private List<List<String>> trainset;
    private List<List<String>> testset;
    private List<Entry> trainSetEntries;
    private List<Entry> testSetEntries;
    private List<String> attributes;



    public KNN(int k, List<List<String>> trainset, List<List<String>> testset, List<String> attributes) {
        this.k = k;
        this.trainset = trainset;
        this.testset = testset;
        this.attributes = attributes;
        this.trainSetEntries = new ArrayList<>();
        this.testSetEntries = new ArrayList<>();
    }

    // create Entry objects for each row in the trainset and testset + fill the lists
    public void initEntries () {
        for (List<String> row : trainset) {
            trainSetEntries.add(createEntry(row));
        }

        int count = 0;

        for (List<String> row : testset) {
            testSetEntries.add(createEntry(row));
            count++;
        }
        System.out.println("Testset entries: " + count);
    }

    // create Entry object from a row
    public Entry createEntry(List<String> row) {
        int size = row.size();

        List<String> vectors = row.subList(0, size-2);
        String name = row.get(size-1);
        return new Entry(vectors, name);
    }

    public void knnstart () {

        initEntries();

        double hit = 0;
        double miss = 0;


        for (Entry entry : testSetEntries) {

            fillNeighborsMap(entry, testSetEntries);
            System.out.println("Entry: " + entry.getName());

            List<Entry> kNeighbors = get_K_neighbors(entry, k);
            Map<String, Integer> classifications = new HashMap<>();

            for (Entry n : kNeighbors) {
//                System.out.println("Neighbor: " + n.getVectors() + " - " + n.getName());

                classifications.put(n.getName(), classifications.getOrDefault(n.getName(), 0) + 1);

            }
            String classification = classifications.entrySet().stream()
                    .max((e1, e2) -> e1.getValue().compareTo(e2.getValue()))
                    .get().getKey();

            if (classification.equals(entry.getName())) {
                hit++;
            } else {
                miss++;
            }

            System.out.println("Classification: " + classification);

            System.out.println("-----------------------");
        }

        double total = hit + miss;

        System.out.println(hit + "/" + total);

        double accuracy = (hit / total)*100;
        System.out.println("Accuracy: " + accuracy + "%");

    }

    // fill the distancesToAll map of each entry - the distance to all other entries
    public void fillNeighborsMap(Entry entry, List<Entry> entries) {
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
//        return distance;
    }

    // set the k nearest neighbors
    public List<Entry> get_K_neighbors(Entry entry, int k) {
        List<Entry> kNeighbors = new ArrayList<>();

        entry.getDistancesToAll().entrySet().stream()
                .sorted((e1, e2) -> e1.getValue().compareTo(e2.getValue()))
                .limit(k)
                .forEach(e -> kNeighbors.add(e.getKey()));

        return kNeighbors;
    }






}
