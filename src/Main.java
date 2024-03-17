import java.util.List;

public class Main {

    public static void main (String[] args) {


        // read csv
        // read attributes
        // knn

        String trainsetPath = "Resources/iris.csv";
        String testsetPath = null;
        String attributesPath = "Resources/Attribute Information.txt";

        List<List<String>> trainset;
        List<List<String>> testset = null;
        List<String> attributes = null;

        Reader_CSV reader_csv = new Reader_CSV();

        trainset = reader_csv.read(trainsetPath);

//        for (List row : trainset) {
//            System.out.println(row);
//        }

        KNN knn = new KNN(3, trainset, testset, attributes);
        knn.knnstart();


    }
}
