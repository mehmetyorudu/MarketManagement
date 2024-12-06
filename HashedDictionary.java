import java.util.*;

public class HashedDictionary<K, V> implements DictionaryInterface<K, V> {
    private int numberOfEntries;
    private static final int DEFAULT_CAPACITY = 13;     // Must be prime

    // The hash table:
    private HashNode<K, V>[] hashTable;
    public static long linearCollisionsCount = 0;
    public static long doubleCollisionsCount = 0;

    private boolean integrityOK = false;



    public HashedDictionary() {
        this(DEFAULT_CAPACITY);
    }

    public HashedDictionary(int initialCapacity) {
        numberOfEntries = 0;
        int tableSize = getNextPrime(initialCapacity);
        @SuppressWarnings("unchecked")
        HashNode<K, V>[] temp = (HashNode<K, V>[])new HashNode[tableSize];
        hashTable = temp;
        integrityOK=true;
    }

    public void display(){
        for (int i=0;i< hashTable.length;i++){
            if (hashTable[i]!=null) {
                System.out.println(i + " " + hashTable[i].getKey() + " " + hashTable[i].getValue());
                int hash= i;
                HashNode<K, V> currentNode = hashTable[hash];
                while(currentNode.getNext()!=null) {
                    System.out.println(i + " " + currentNode.getNext().getKey() + " " + currentNode.getNext().getValue());
                    currentNode=currentNode.next;
                }

            }
        }
    }
    //Linear Probing
    boolean checkHash =isItPAF();
    double loadFactor= loadFactor();


    public V addLinear(K key, V value) {
        checkIntegrity();
        if (key == null || value == null) {
            throw new IllegalArgumentException("Cannot add null to a dictionary.");
        } else {
            V oldValue;

            int index;
            if (checkHash){
                index= getHashPAF(key);
            }
            else {
                index = getHashSSF(key);
            }


            while (hashTable[index] != null && !hashTable[index].getKey().equals(key)) {
                index = (index + 1) % hashTable.length; // Linear probing
                linearCollisionsCount++;
            }

            assert (index >= 0) && (index < hashTable.length);

            if (hashTable[index] == null) { // Key not found, so insert new entry
                hashTable[index] = new HashNode<>(key, value);
                numberOfEntries++;
                oldValue = null;
            } else { // Key found, add new node to the chain
                HashNode<K, V> currentNode = hashTable[index];
                while (currentNode.getKey().equals(key) && currentNode.getNext() != null) {
                    currentNode = currentNode.getNext();
                }
                currentNode.setNext(new HashNode<>(key, value));
                oldValue = null;
            }

            //Enlarging hashtable
            if (isHashTableTooFull()) {
                enlargeHashTable();
            }
            return oldValue;
        }
    }



    //Double hashing
    public V addDouble(K key, V value) {
        checkIntegrity();
        if (key == null || value == null) {
            throw new IllegalArgumentException("Cannot add null to a dictionary.");
        } else {
            V oldValue;
            int index;
            if (checkHash){
                index= getHashPAF(key);
            }
            else {
                index = getHashSSF(key);
            }

            int stepSize = getStepSize(key);


            int count=0;
            while (hashTable[index] != null && !hashTable[index].getKey().equals(key)) {
                index = (index +    count *stepSize) % hashTable.length;// Double hashing
                doubleCollisionsCount++;
                count++;
            }

            assert (index >= 0) && (index < hashTable.length);

            if (hashTable[index] == null || hashTable[index].isRemoved()) { // Key not found, so insert new entry
                hashTable[index] = new HashNode<>(key, value);
                numberOfEntries++;
                oldValue = null;
            } else { // Key found, add new node to the chain
                HashNode<K, V> currentNode = hashTable[index];
                while (currentNode.getNext() != null) {
                    currentNode = currentNode.getNext();
                }
                currentNode.setNext(new HashNode<>(key, value));
                oldValue = null;
            }

            // Enlarging hashtable
            if (isHashTableTooFull()) {
                enlargeHashTable();
            }
            return oldValue;
        }
    }
    public boolean isItPAF() {


        while(true) {
            Scanner myObj = new Scanner(System.in);
            System.out.println("PAF OR SSF ?");

            String answer = myObj.nextLine();
            if (answer.equals("PAF") || answer.equals("paf")) {
                return true;
            } else if (answer.equals("SSF") || answer.equals("ssf")) {
                return false;
            } else {
                System.out.println("invalid value!");

            }
        }


    }
    public double loadFactor() {
        while(true) {
            Scanner myObj = new Scanner(System.in);
            System.out.println("Choose a load factor: 1) 0.5 2) 0.8 ");

            String answer = myObj.nextLine();
            if (answer.equals("1") ){
                return 0.5;
            } else if (answer.equals("2")) {
                return 0.8;
            } else {
                System.out.println("invalid value!");

            }
        }
    }


   public V[] getAllValues(K key) {
       checkIntegrity();

       if (key == null) {
           throw new IllegalArgumentException("Key cannot be null.");
       } else {
           List<V> valuesList = new ArrayList<>();

           for (int index = 0; index < hashTable.length; index++) {
               if (hashTable[index] != null && hashTable[index].getKey().equals(key)) {
                   HashNode<K, V> currentNode = hashTable[index];
                   while (currentNode != null && currentNode.getKey().equals(key)) {
                       valuesList.add(currentNode.getValue());
                       currentNode = currentNode.getNext();
                   }
               }
           }

           @SuppressWarnings("unchecked")
           V[] valuesArray = (V[]) valuesList.toArray(new Object[0]);
           return valuesArray;
       }
   }


    private int getStepSize(K key) {

        int index;
        if (checkHash){
            index= getHashPAF(key);
        }
        else {
            index = getHashSSF(key);
        }
        return (7 - (Math.abs(index) % 7));
    }

    public V remove(K key)
    {
        checkIntegrity();
        V removedValue = null;

        int index;
        if (checkHash){
            index= getHashPAF(key);
        }
        else {
            index = getHashSSF(key);
        }

        if (hashTable[index] != null)
        {
            if(hashTable[index].getKey().equals(key))
            {
                removedValue = hashTable[index].getValue();
                hashTable[index] = hashTable[index].getNext();
                numberOfEntries--;
            }
            else
            {
                HashNode<K, V> current = hashTable[index];
                HashNode<K, V> prev = current;
                while(current != null && !current.getKey().equals(key))
                {
                    prev = current;
                    current = current.getNext();
                }

                if(current != null)
                {
                    // Key found; remove
                    removedValue = current.getValue();
                    prev.setNext(current.getNext());
                    numberOfEntries--;
                    current.state= HashNode.States.REMOVED;
                }

            }
        }

        return removedValue;
    } // end remove
    @Override
    public boolean contains(K key) {
        return false;
    }

    public boolean isEmpty()
    {
        return numberOfEntries == 0;
    } // end isEmpty

    public int getSize()
    {
        return numberOfEntries;
    } // end getSize

    public final void clear()
    {
        checkIntegrity();
        for (int index = 0; index < hashTable.length; index++)
            hashTable[index] = null;

        numberOfEntries = 0;
    } // end clear

    public Iterator<K> getKeyIterator()
    {
        return new KeyIterator();
    } // end getKeyIterator

    public Iterator<V> getValueIterator()
    {
        return new ValueIterator();
    } // end getValueIterator

    private int getHashSSF(K key)
    {
        int sum = 0;

        for (int i = 0; i < (key.toString().length()); i++) {
            sum += (int) key.toString().charAt(i);
        }
        return (sum % hashTable.length);
    } // end getHashIndex
    private int getHashPAF(K key) {
        final int BASE = 31;
        int hash = 0;
        int n = key.toString().length();
        for (int i = 0; i < n; i++) {
            hash = (hash * BASE + (int) key.toString().charAt(i)) % hashTable.length;
            if (hash < 0) {
                hash += hashTable.length;
            }
        }
        return hash;
    }




    private void enlargeHashTable()
    {
        HashNode<K, V>[] oldTable = hashTable;
        int oldSize = hashTable.length;
        int newSize = getNextPrime(oldSize + oldSize);


        @SuppressWarnings("unchecked")
        HashNode<K, V>[] tempTable = (HashNode<K, V>[])new HashNode[newSize]; // Increase size of array
        hashTable = tempTable;
        numberOfEntries = 0; // Reset number of dictionary entries

        for (int index = 0; index < oldSize; index++)
        {
            if (oldTable[index] != null &&oldTable[index].isIn())
            {
                    if (linearCollisionsCount!=0)
                         addLinear(oldTable[index].getKey(), oldTable[index].getValue());
                    else
                         addDouble(oldTable[index].getKey(), oldTable[index].getValue());
            }

        } // end for
    } // end enlargeHashTable


    private boolean isHashTableTooFull()
    {
        return numberOfEntries > loadFactor * hashTable.length;
    } // end isHashTableTooFull

    // Returns a prime integer that is >= the given integer.
    private int getNextPrime(int integer)
    {
        if (integer % 2 == 0)
        {
            integer++;
        }


        while (!isPrime(integer))
        {
            integer = integer + 2;
        }

        return integer;
    } // end getNextPrime

    // Returns true if the given intege is prime.
    private boolean isPrime(int integer)
    {
        boolean result;
        boolean done = false;

        if ( (integer == 1) || (integer % 2 == 0) )
        {
            result = false;
        }

        else if ( (integer == 2) || (integer == 3) )
        {
            result = true;
        }

        else
        {
            assert (integer % 2 != 0) && (integer >= 5);

            result = true; // assume prime
            for (int divisor = 3; !done && (divisor * divisor <= integer); divisor = divisor + 2)
            {
                if (integer % divisor == 0)
                {
                    result = false;
                    done = true;
                }
            }
        }

        return result;
    } // end isPrime

    private void checkIntegrity()
    {
        if (!integrityOK)
            throw new SecurityException ("HashedDictionary object is corrupt.");
    } // end checkIntegrity


        private class KeyIterator implements Iterator<K> {
            private int currentIndex;
            private int numberLeft;

            private KeyIterator() {
                currentIndex = 0;
                numberLeft = numberOfEntries;
            }

            public boolean hasNext() {
                return numberLeft > 0;
            }

            public K next() {
                K result = null;

                if (hasNext()) {
                    while (hashTable[currentIndex] == null) {
                        currentIndex++;
                    }

                    result = (K) hashTable[currentIndex].getKey();
                    currentIndex++;
                    numberLeft--;
                } else {
                    throw new NoSuchElementException();
                }

                return result;
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }
        }


    private class ValueIterator implements Iterator<V>
    {
        private int currentIndex;
        private int numberLeft;
        private HashNode<K, V> currentNode;

        private ValueIterator()
        {
            currentIndex = 0;
            numberLeft = numberOfEntries;
        } // end default constructor

        public boolean hasNext()
        {
            return numberLeft > 0;
        } // end hasNext

        public V next()
        {
            V result = null;

            if (hasNext())
            {
                if(currentNode != null)
                {
                    result = currentNode.getValue();
                    numberLeft--;
                    currentNode = currentNode.getNext();
                }
                else
                {
                    // Skip table locations that do not contain a hashnode
                    while (hashTable[currentIndex] == null)
                    {
                        currentIndex++;
                    } // end while

                    currentNode = hashTable[currentIndex];
                    result = currentNode.getValue();
                    numberLeft--;
                    currentNode = currentNode.getNext();
                }
                if(currentNode == null) currentIndex++;
            }
            else
                throw new NoSuchElementException();

            return result;
        } // end next

        public void remove()
        {
            throw new UnsupportedOperationException();
        } // end remove
    } // end ValueIterator

    public static class HashNode<K,V> {
        private K key;
        private V value;
        private States state;

        private enum States {CURRENT,REMOVED}
        private HashNode<K, V> next;

        public HashNode(K key, V value)
        {
            this.key=key;
            this.value=value;
            state=States.CURRENT;
        }
        public boolean isIn() {
            return state == States.CURRENT;
        }

        public boolean isRemoved() {
            return state == States.REMOVED;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        public void setValue(V value) {
            this.value = value;
        }

        public HashNode<K, V> getNext() {
            return this.next;
        }

        public void setNext(HashNode<K, V> next) {
            this.next = next;
        }
    }
}
