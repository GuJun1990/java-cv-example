package org.example.javacv.opencv_cookbook.chapter11

import java.io.File

import org.bytedeco.opencv.global.opencv_imgproc.{COLOR_BGR2GRAY, Canny, THRESH_BINARY_INV, cvtColor, threshold}
import org.bytedeco.opencv.opencv_core.Mat

import scala.Console.print

object Ex3WriteVideoSequence {
  // Define a method for processing video frames
  def canny(src: Mat, dest: Mat): Unit = {
    // Convert to gray
    cvtColor(src, dest, COLOR_BGR2GRAY)
    // Compute Canny edges
    Canny(dest, dest, 100, 200, 3, true)
    // Invert the image
    threshold(dest, dest, 128, 255, THRESH_BINARY_INV)
    // Indicate processing progress
    print(".")
  }

  def main(args: Array[String]): Unit = {
    val inputFile = new File("data/bike.avi")
    println("Processing video file: " + inputFile.getCanonicalPath)
    // Create video processor instance
    val processor = new VideoProcessor()
    processor.input = inputFile.getCanonicalPath
    // Do not display video while processing
    processor.displayInput = ""
    processor.displayOutput = ""
    // Play the video at the original frame rate
    processor.delay = 0
    // Set the frame processor callback function
    processor.frameProcessor = canny
    // Decide which codec to use for output video
    val codec = 13 // XVID MPEG-4
    // Indicate file name and coded to use to write video
    val outputFile = new File("data/bikeOut.avi")
    println("Processing video file: " + outputFile.getCanonicalPath)
    processor.setOutput(outputFile.getCanonicalPath, codec = codec)
    // Start the process
    processor.run()
    // Close the video file
    println("\nVideo processing done.")
  }
}
