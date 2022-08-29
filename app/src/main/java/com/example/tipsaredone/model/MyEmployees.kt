package com.example.tipsaredone.model

import com.google.firebase.firestore.FirebaseFirestore


class MyEmployees() {

    private var myEmployees: MutableList<Employee> = mutableListOf()

    fun setMyEmployees(data: MutableList<Employee>) {
        myEmployees = data
    }
    fun getMyEmployees(): MutableList<Employee> {
        return myEmployees
    }

    fun saveIndividualReports(reports: MutableList<IndividualReport>) {
        for (report in reports) {
            for (employee in myEmployees) {
                if (employee.id == report.id) {
                    employee.addTipReport(report)
                }
            }
        }
    }


    fun getEmployeesFromDatabase(database: FirebaseFirestore) {

    }



/*

    fun saveEmployeeNamesToInternalStorage(employees: MutableList<Employee>,context: Context) {        // Called whenever the "CONTINUE" button is pressed @EmployeeListFragment.
        // Converting list of employee objects into list of employee names (string).
        val listOfNames = mutableListOf<String>()
        for (emp in employees) {
            listOfNames.add(emp.name)
        }

        // Converting listOfNames to Json
        val jsonString = MyEmployees().convertEmployeeNamesToJson(listOfNames)
        val jsonFileName = "myEmployees"

        // Saving the Json data to internal storage
        (context as MainActivity).openFileOutput(jsonFileName, Context.MODE_PRIVATE).use {
            it.write(jsonString.toByteArray())
            it.close()
            Log.d(INTERNAL_STORAGE, "Employee names saved as Json:\n${jsonString}")
        }

        storedEmployees = employees

    }
    fun loadEmployeeNamesFromInternalStorage(context: Context) {
        // loading data as Json text
        val fileInputStream: FileInputStream? = context.openFileInput("myEmployees")
        val inputStreamReader = InputStreamReader(fileInputStream)
        val bufferedReader = BufferedReader(inputStreamReader)
        val stringBuilder: StringBuilder = StringBuilder()
        var text: String? = null

        while (run {
                text = bufferedReader.readLine()
                text
            } != null) {
            stringBuilder.append(text)
        }

        val jsonString = stringBuilder.toString()
        Log.d(INTERNAL_STORAGE,"Names loaded as Json string.")

        // Checking if Json is empty
        if (jsonString.isNotBlank() && jsonString != "")  {

            // Converting Json to list of string
            val gson = GsonBuilder().create()
            val listOfNames = gson.fromJson<MutableList<String>>(jsonString, object :
                TypeToken<MutableList<String>>(){}.type)
            Log.d(INTERNAL_STORAGE,"Names converted to mutable list of string.")

            // Initializing a list of objects
            val listOfEmployees = mutableListOf<Employee>()
            for (name in listOfNames) {
                listOfEmployees.add(Employee(name))
                Log.d(INTERNAL_STORAGE,"Employee Loaded: $name")
            }
            storedEmployees = listOfEmployees
        }
        else {
            Log.d(INTERNAL_STORAGE,"Internal storage contained no names.")
            storedEmployees = mutableListOf()
        }
    }

    fun getStoredEmployees(): MutableList<Employee> {
        return storedEmployees
    }

 */
}


