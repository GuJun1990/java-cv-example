package org.example.javacv.function

import org.bytedeco.javacv.FFmpegFrameGrabber
import org.opencv.imgproc.Imgproc

import scala.collection.Iterator.continually

object ShotDetect {

  def main(args: Array[String]): Unit = {
    val grabber = new FFmpegFrameGrabber("data/bike.avi")
    grabber.start()
    val width = grabber.getImageWidth
    val height = grabber.getImageHeight
    val fps = grabber.getFrameRate
    val frameNum = grabber.getLengthInFrames
    println(s"width: $width, height: $height, fps: $fps, frame num: $frameNum")
    var frameIndex = 0;
    for (frame <- continually(grabber.grab()).takeWhile(_ != null)) {
      frameIndex += 1
      val ts = frame.timestamp
      val imageChannels = frame.imageChannels
      val imageDepth = frame.imageDepth
      val imageWidth = frame.imageWidth
      val imageHeight = frame.imageHeight
      val imageStride = frame.imageStride
      println(s"index: $frameIndex, ts: $ts, channels: $imageChannels, width: $imageWidth, height: $imageHeight, depth: $imageDepth, stride: $imageStride")
    }
    grabber.release()
  }

}
