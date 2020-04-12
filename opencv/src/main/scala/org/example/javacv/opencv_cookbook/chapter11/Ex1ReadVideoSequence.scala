package org.example.javacv.opencv_cookbook.chapter11

import javax.swing.WindowConstants
import org.bytedeco.javacv.{CanvasFrame, FFmpegFrameGrabber}

import scala.collection.Iterator.continually

object Ex1ReadVideoSequence {
  def main(args: Array[String]): Unit = {
    val grabber = new FFmpegFrameGrabber("data/bike.avi")
    grabber.start()
    val canvasFrame = new CanvasFrame("Extracted Frame", 1)
    canvasFrame.setCanvasSize(grabber.getImageWidth, grabber.getImageHeight)
    // Exit the example when the canvas frame is closed
    canvasFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE)

    val delay = math.round(1000d / grabber.getFrameRate)

    // Read frame by frame, stop early if the display window is closed
    for (frame <- continually(grabber.grab()).takeWhile(_ != null)
         if canvasFrame.isVisible) {
      // Capture and show the frame
      canvasFrame.showImage(frame)
      // Delay
      Thread.sleep(delay)
    }

    // Close the video file
    grabber.release()
  }

}
