package org.example.javacv.opencv_cookbook.chapter11

import java.io.File

object Ex4TrackingFeatures {
  def main(args: Array[String]): Unit = {
    // Open video file
    val inputFile = new File("data/bike.avi")
    println("Processing video file: " + inputFile.getCanonicalPath)
    // Feature tracker
    val tracker = new FeatureTracker()
    // Create video processor instance
    val processor = new VideoProcessor()
    processor.input = inputFile.getCanonicalPath
    // Declare a window to display input and output the video
    processor.displayInput = "Input Video"
    processor.displayOutput = "Output Video"
    // Play the video at the original frame rate
    processor.delay = math.round(1000d / processor.frameRate)
    // Set the frame processor callback function (pass FeatureTracker `process` method as a closure)
    processor.frameProcessor = tracker.process
    // Start the process
    processor.run()
    println("Done.")
  }
}
