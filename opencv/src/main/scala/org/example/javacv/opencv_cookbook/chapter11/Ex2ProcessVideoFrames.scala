//package org.example.javacv.opencv_cookbook.chapter11
//
//import org.bytedeco.opencv.global.opencv_imgproc._
//import org.bytedeco.opencv.opencv_core._
//
//object Ex2ProcessVideoFrames {
//  /** Method for processing video frames, that is passed to `VideoProcessor`. */
//  def canny(src: Mat, dest: Mat): Unit = {
//    // Convert to gray
//    cvtColor(src, dest, COLOR_BGR2GRAY)
//    // Compute Canny edges
//    Canny(dest, dest, 100, 200, 3, true)
//    // Invert the image
//    threshold(dest, dest, 128, 255, THRESH_BINARY_INV)
//  }
//
//  // Create video processor instance
//  val processor = new VideoProcessor()
//  processor.input = "data/bike.avi"
//
//  // Declare a window to display the input and output video
//  processor.displayInput = "Input Video"
//  processor.displayOutput = "Output Video"
//
//  // Play the video at the original frame rate
//  processor.delay = math.round(1000d / processor.frameRate)
//
//  // Set the frame processing method
//  processor.frameProcessor = canny
//
//  // stop the process at this frame
//  processor.stopAtFrameNo = -1
//
//  // Start the processing loop
//  processor.run()
//}
