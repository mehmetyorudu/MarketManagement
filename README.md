In the scope of the project, you are expected to use a hash table to organize market transactions. You will use your own hash table implementation in Java programming language. The aim is to access specified customer records rapidly. (e.g., return all transactions of a customer with the UUID “11c34489-f95a-45ec-a833-8a329e4d1710” in reverse chronological order).
At the beginning you will read the whole dataset and store all transaction in a hash table. The structure of the hash table should look like as shown in Figure 2. You can use a sorted list data structure to store customer transactions in reverse chronological order.
3.	Functionalities
•	put(Key k, Value v)
If a customer UUID (k) is already present, then add a given transaction to the list of customer transactions, otherwise create a new entry. You should store the date of each transaction with the purchased product name.
•	Value get(Key k)
Search the given customer UUID (k) in the hash table. If the customer is available in the table, then return an output as given in Figure 3, otherwise return a “not found” message to the user.
