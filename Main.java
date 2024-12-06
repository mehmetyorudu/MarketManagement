import java.io.*;
import java.util.*;

public class Main {
    public static final String delimiter = ",";

    public static void main(String[] args) {


        String csvFile = "supermarket_dataset_50K.csv";
        DictionaryInterface<String, Customer> hashTable = new HashedDictionary<>();
        long start ;
        long end;
        Scanner myObj = new Scanner(System.in);
        while(true) {

            System.out.println("Choose collision handling: 1) Linear Probing 2) Double Hashing");
            String answer = myObj.nextLine();
            if (answer.equals("1") ) {
                start= System.nanoTime();
                try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
                    br.readLine(); // Skip header
                    String line;
                    while ((line = br.readLine()) != null) {
                        String[] tempArr = line.split(delimiter);
                        Customer customer = new Customer(tempArr[1], tempArr[2], tempArr[3]);
                        hashTable.addLinear(tempArr[0], customer);
                    }
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
                end = System.nanoTime();
                System.out.println("Collision count: " + HashedDictionary.linearCollisionsCount);
                break;
            } else if (answer.equals("2")) {
                start= System.nanoTime();
                try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
                    br.readLine(); // Skip header
                    String line;
                    while ((line = br.readLine()) != null) {
                        String[] tempArr = line.split(delimiter);
                        Customer customer = new Customer(tempArr[1], tempArr[2], tempArr[3]);
                        hashTable.addDouble(tempArr[0], customer);
                    }
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
                end = System.nanoTime();
                System.out.println("Collision count: " + HashedDictionary.doubleCollisionsCount);
                break;
            } else {
                System.out.println("invalid value!");
            }
        }



        double time = (end-start)/1000000;
        System.out.println("Indexing time: "+time+" ms");

        long startSearch=0;
        long endSearch=0;
        int keyCount=0;
        try {
            File file = new File("customer_1K.txt");
            Scanner myReader = new Scanner(file);

            startSearch=System.nanoTime();
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                Iterator<String> it = hashTable.getKeyIterator();

                while(it.hasNext()){
                    if (it.next().equals(data)){
                        keyCount++;
                        break;
                    }
                }
            }
            endSearch=System.nanoTime();
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        double searchTime=(endSearch-startSearch)/1000000;
        System.out.println("Search Time: "+searchTime+" ms");
        System.out.println("Average Search Time: "+searchTime/1000+" ms");
        System.out.println("Count of match keys: "+keyCount);

        search(hashTable);



    }
    public static void search(DictionaryInterface<java.lang.String, Customer> hashTable){
        Scanner myObj=new Scanner(System.in);
        System.out.println("Do you want to see the transactions of key which want ?");
        String answer2=myObj.nextLine();
        while(true){
            if ((answer2.equals("yes"))||(answer2.equals("YES"))){
                System.out.println("Search|>");
                String id = myObj.nextLine();
                Object[] objects = hashTable.getAllValues(id);
                Customer[] customers = Arrays.copyOf(objects, objects.length, Customer[].class);
                if (objects.length==0){
                    System.out.println("Customer not found!");
                }
                if (customers != null) {
                    for (Customer customer : customers) {
                        Customer.displayCustomer(customer);
                    }
                }
                break;

            }
            else
                break;
        }
    }
}
