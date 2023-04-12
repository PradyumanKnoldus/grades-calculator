package com.knoldus

import GradesCalculator._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Await
import scala.concurrent.duration._
import scala.util.{Failure, Success}

object GradesCalculatorDriver extends App {

  // Define the path to the CSV file
  val filePath = "/home/knoldus/IdeaProjects/ParseCSV/src/main/scala/StudentsData.csv"

  //----------------------------------------------------------------------------------------

  // Parsing CSV File
  private val studentData = parseCsv(filePath)

  studentData.onComplete {
    case Success(studentData) =>
      println(s"\nParsed data from csv File :")
      studentData.foreach(println)
    case Failure(exception) =>
      println(s"\nFailed to parse CSV file : ${exception.getMessage}")
  }
  Thread.sleep(1000)

  Await.ready(studentData, Duration.Inf)


  //----------------------------------------------------------------------------------------

  // Calculating Student's Average Individually
  val studentAverages = calculateStudentAverages(studentData)

  studentAverages.onComplete {
    case Success(studentAverages) =>
      println(s"\nCalculated Student's Averages :")
      studentAverages.foreach(println)
    case Failure(exception) =>
      println(s"\nFailed to calculate Student's Averages: ${exception.getMessage}")
  }

  Thread.sleep(1000)

  Await.ready(studentAverages, Duration.Inf)


  //----------------------------------------------------------------------------------------

  // Calculating Class Average
  private val classAverage = calculateClassAverage(studentAverages)

  classAverage.onComplete {
    case Success(classAverage) =>
      println(s"\nCalculated class average: $classAverage")
    case Failure(exception) =>
      println(s"\nFailed to calculate Class Average: ${exception.getMessage}")
  }

  Thread.sleep(1000)

  Await.ready(classAverage, Duration.Inf)


  //----------------------------------------------------------------------------------------

  // Calculating Class Grade
  private val classGrade = calculateGrades(filePath)

  classGrade.onComplete {
    case Success(avg) => println(s"\nClass average is: $avg")
    case Failure(exception) => println(s"\nFailed to calculate class average: ${exception.getMessage}")
  }

  Thread.sleep(1000)

  Await.ready(classGrade, Duration.Inf)
}
