import weka.core.Instances;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.classifiers.trees.J48;
import java.util.ArrayList;

public class WekaExample {
    
    public static void main(String[] args) {
        System.out.println("Weka Integration Example - Working Version");
        System.out.println("========================================\n");
        
        try {
            // 1. Define attributes
            ArrayList<Attribute> attributes = new ArrayList<>();
            attributes.add(new Attribute("pixel_0"));
            attributes.add(new Attribute("pixel_1"));
            
            ArrayList<String> classValues = new ArrayList<>();
            classValues.add("trois");
            classValues.add("cinq");
            attributes.add(new Attribute("class", classValues));
            
            // 2. Create dataset
            Instances data = new Instances("TestRelation", attributes, 0);
            data.setClassIndex(data.numAttributes() - 1);
            
            // 3. Add a dummy instance
            double[] vals = new double[data.numAttributes()];
            vals[0] = 255;
            vals[1] = 0;
            vals[2] = 0; // "trois"
            data.add(new DenseInstance(1.0, vals));
            
            // 4. Build a simple J48 classifier
            J48 tree = new J48();
            tree.buildClassifier(data);
            
            System.out.println("✓ Success: Weka library is correctly integrated!");
            System.out.println("✓ Classifier built successfully.");
            System.out.println("\nModel summary:");
            System.out.println(tree.toString());
            
        } catch (Exception e) {
            System.out.println("✗ Weka integration failed: " + e.getMessage());
            System.out.println("\nEnsure lib/weka-3-8-6/weka.jar exists and pom.xml is configured.");
        }
    }
}
