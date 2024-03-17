import java.util.List;

public class Main {

    public static void main (String[] args) {


        // read csv
        // read attributes
        // knn


        String trainsetPath = "Resources/train-set.csv";
        String testsetPath = "Resources/test-set.csv";
        String attributesPath = "Resources/Attribute Information.txt";

        List<List<String>> trainset;
        List<List<String>> testset;
        List<String> attributes = null;

        Reader_CSV reader_csv = new Reader_CSV();

        trainset = reader_csv.read(trainsetPath);

        reader_csv = new Reader_CSV();

        testset = reader_csv.read(testsetPath);

//        System.out.println(trainset.size());
//        System.out.println(testset.size());

//        for (List row : trainset) {
//            System.out.println(row);
//        }
//
//        for (List row : testset) {
//            System.out.println(row);
//        }

        KNN knn = new KNN(3, trainset, testset, attributes);
        knn.knnstart();


    }
}
