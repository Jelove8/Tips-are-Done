package com.example.tipsaredone

class TODO {

    /**
     * Better understand how FirebaseAuth signs in user, and how long they remain signed in.
     */

    /**
     * Understand why in EmployeeListFragment, the adapter needs to be initialized before reading employees from Firebase.
     *      The only way I can get the stored employees into ViewModel is by passing the adapter into the read() method.
     *      I can't return the data as a MutableList<Employee>.
     *      Instead, I have to read each Employee item one by one, instantiating a new Employee object with the same UID,
     *      Then pass that Employee into the adapter, where the list is sorted and automatically added to EmployeesViewModel as MutableLiveData.
     */


}