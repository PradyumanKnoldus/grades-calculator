
package com.knoldus

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.io.Source

object GradesCalculator {

  // This method takes a file path as input and returns a Future containing a List of Maps representing the CSV data
  def parseCsv(filePath: String): Future[List[Map[String, String]]] = {
    val sourceFile = Future(Source.fromFile(filePath))

    sourceFile.flatMap { source =>
      val lines = source.getLines().toList
      val header = lines.head.split(",").toList
      val data = lines.tail.map(line => (header zip line.split(",").toList).toMap)
      source.close()
      Future.successful(data)
    }.recoverWith {
      case exception: Exception =>
        Future.failed(new Exception(s"Failed to parse CSV file: ${exception.getMessage}"))
    }
  }

  // This method takes a Future containing a List of Maps representing the CSV data and returns a Future containing a List of tuples representing the student ID and their average grade
  def calculateStudentAverages(parsedData: Future[List[Map[String, String]]]): Future[List[(String, Double)]] = {
    parsedData.map { rows =>
      rows.map { row =>
        val id = row("StudentID")
        val english = row("English").toDouble
        val physics = row("Physics").toDouble
        val chemistry = row("Chemistry").toDouble
        val maths = row("Maths").toDouble
        val average = (english + physics + chemistry + maths) / 4
        (id, average)
      }
    }.recoverWith {
      case exception: Exception =>
        Future.failed(new Exception(s"Failed to calculate Student's average :  ${exception.getMessage}"))
    }
  }

  // This method takes a Future containing a List of tuples representing the student ID and their average grade and returns a Future containing the average grade for the entire class
  def calculateClassAverage(studentAverages: Future[List[(String, Double)]]): Future[Double] = {
    studentAverages.map { averages =>

      // Calculate the total grade across all students and divide by the number of students
      val total = averages.map(_._2).sum
      total / averages.length
    }.recoverWith {
      case exception: Exception =>
        Future.failed(new Exception(s"Failed to calculate Class average :  ${exception.getMessage}"))
    }
  }

  // This method takes a file path as input and returns a Future containing the average grade for the entire class
  def calculateGrades(filePath: String): Future[Double] = {
    parseCsv(filePath).flatMap { parsedData =>
      calculateStudentAverages(Future.successful(parsedData)).flatMap { studentAverages =>
        calculateClassAverage(Future.successful(studentAverages))
      }
    }.recoverWith {
      case exception: Exception =>
        Future.failed(new Exception(s"Failed to calculate grades: ${exception.getMessage}"))
    }
  }

}