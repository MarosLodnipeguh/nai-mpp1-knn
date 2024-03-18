import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

public class KNN {

    private int k;
    private List<List<String>> trainSetText;
    private List<List<String>> testSetText;
    private List<Entry> trainSetEntries;
    private List<Entry> testSetEntries;
    private List<String> attributes;
    int vectorCount;


    public KNN(int k, List<List<String>> trainset, List<List<String>> testset, List<String> attributes) {
        this.k = k;
        this.trainSetText = trainset;
        this.testSetText = testset;
        this.attributes = attributes;
        this.trainSetEntries = new ArrayList<>();
        this.testSetEntries = new ArrayList<>();
    }

    public void knnstart () {
        initEntries();
        calculateEntries();
        promptUserEntries();
    }

    public double knnChart() {
        initEntries();
        return calculateEntries();
    }

    // create Entry objects for each row in the trainset and testset + fill the lists
    public void initEntries () {
        for (List<String> row : trainSetText) {
            trainSetEntries.add(createEntry(row));
        }

        for (List<String> row : testSetText) {
            testSetEntries.add(createEntry(row));
        }

        vectorCount = trainSetEntries.getFirst().getVectors().size();
    }

    // create Entry object from a row
    public Entry createEntry(List<String> row) {
        int size = row.size(); // 5
        List<String> vectors = row.subList(0, size-1); // 0 - 4 (ostatniego nie bierze)
//        List<String> vectors = row.stream().limit(size-1).toList();
//        System.out.println(vectors.toString());
        String name = row.get(size-1);
        return new Entry(vectors, name);
    }

    private double calculateEntries () {
        int hit = 0;
        int miss = 0;
        int total = testSetEntries.size();
        int counter = 1;

        for (Entry entry : testSetEntries) {
            System.out.print(counter + ". Expected: " + ANSI_GREEN + entry.getName() + ANSI_RESET);

            String classification = classifyEntry(entry);

            // check if the classification is correct
            if (classification.equals(entry.getName())) {
                hit++;
                System.out.print("  Calculated: " + ANSI_PURPLE + classification + ANSI_RESET);
            } else {
                miss++;
                System.out.print("  Calculated: " + ANSI_RED + classification + ANSI_RESET);
            }

            counter++;
            System.out.println();
        }

        System.out.println("-----------------------");
        System.out.println("Nearest neighbors considered (k): " + k);
        System.out.println("Correct: " + hit + "/" + total);

        double accuracy = ((double) hit / total)*100;
        DecimalFormat df = new DecimalFormat("#.##");

        System.out.println(ANSI_BLUE + "Accuracy: " + ANSI_RESET + df.format(accuracy) + "%");

        return accuracy;
    }

    private String classifyEntry (Entry entry) {
        // fill the distancesToAll map of the entry - the distance to all other entries
        fillEntryDistancesToAllMap(entry, testSetEntries);

        // get the k nearest neighbors
        List<Entry> kNeighbors = get_K_neighbors(entry, k);

        List<String> kNeighborsNames = new ArrayList<>();
        for (Entry n : kNeighbors) {
            kNeighborsNames.add(n.getName());
        }

        // Count occurrences of each item using streams
        Map<String, Long> itemCounts = kNeighborsNames.stream()
                .collect(Collectors.groupingBy(e -> e, Collectors.counting()));

        // Find the item with the maximum count
        String mostCommonItem = itemCounts.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);


//        Map<String, Integer> classifications = new LinkedHashMap<>();
//
//        // count the classifications of the k nearest neighbors
//        for (Entry n : kNeighbors) {
//            classifications.put(n.getName(), classifications.getOrDefault(n.getName(), 0) + 1);
//        }
//
//        // get the classification with the most occurrences (map value)
//        String classification = classifications.entrySet().stream()
//                .max(Map.Entry.comparingByValue())
//                .get().getKey();

//        return classification;

        return mostCommonItem;
    }

    // fill the distancesToAll map of each entry - the distance to all other entries
    public void fillEntryDistancesToAllMap (Entry origin, List<Entry> neighbors) {
        for (Entry neighbor : neighbors) {
            if (origin != neighbor) {
                double dist = calculateDistance(origin, neighbor);
                origin.getDistancesToAllMap().put(neighbor, dist);
            }
        }
    }

    // calculate the distance between two entries
    public double calculateDistance (Entry origin, Entry neighbor) {

        double distance = 0;

        for (int i = 0; i < vectorCount; i++) {
            double a = Double.parseDouble(origin.getVectors().get(i));
            double b = Double.parseDouble(neighbor.getVectors().get(i));
            double diff = a - b;
            distance += Math.pow(diff, 2);
        }

        return Math.sqrt(distance);
    }

    // get the k nearest neighbors
    public List<Entry> get_K_neighbors(Entry entry, int k) {
        List<Entry> kNeighbors = new ArrayList<>();

        // sort the distancesToAll map of the entry and get the k nearest neighbors
        entry.getDistancesToAllMap().entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .limit(k)
                .forEach(e -> kNeighbors.add(e.getKey()));

        return kNeighbors;
    }


    private void promptUserEntries () {
        while (true) {
            System.out.println("-----------------------");
            System.out.println("Enter " + vectorCount + " attributes of the entry you want to classify: ");

            List<String> userVectors = new ArrayList<>();

            Scanner scanner = new Scanner(System.in);

            for (int i = 0; i < vectorCount; i++) {
                System.out.print("Vector " + (i+1) + ": ");
                userVectors.add(scanner.nextLine());
            }

            Entry userEntry = new Entry(userVectors, "unknown");

            String classification = classifyEntry(userEntry);

            System.out.println(ANSI_PURPLE + "Calculated classification: " + ANSI_RESET + classification);
        }
    }



    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_BLUE = "\u001B[34m";



}
