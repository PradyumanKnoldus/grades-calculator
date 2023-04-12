# Grades Calculator

Grades Calculator is a Scala program that calculates the average grade for a class of students given a CSV file containing their grades in various subjects. It uses the Scala standard library and " scala.concurrent.Future " to process the CSV file and calculate the grades.

## Methodology

The GradesCalculator object provides three methods:

    parseCsv(filePath: String): Future[List[Map[String, String]]]: 
    
This method takes a file path as input and returns a Future containing a List of Maps representing the CSV data.

    calculateStudentAverages(parsedData: Future[List[Map[String, String]]]): Future[List[(String, Double)]] 
    
This method takes a Future containing a List of Maps representing the CSV data and returns a Future containing a List of tuples representing the student ID and their average grade.

    calculateClassAverage(studentAverages: Future[List[(String, Double)]]): Future[Double] 
    
This method takes a Future containing a List of tuples representing the student ID and their average grade and returns a Future containing the average grade for the entire class.

Additionally, there is a convenience method calculateGrades(filePath: String): Future[Double] that takes a file path as input and returns a Future containing the average grade for the entire class. It chains together the other three methods to perform the entire calculation.

## Error Handling

If any error occurs during the parsing, calculating student averages or calculating class averages, the program will return a Future containing the error message as a string.
