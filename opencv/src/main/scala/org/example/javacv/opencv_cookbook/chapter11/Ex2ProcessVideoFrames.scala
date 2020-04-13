package org.example.javacv.opencv_cookbook.chapter11

import org.bytedeco.opencv.global.opencv_imgproc._
import org.bytedeco.opencv.opencv_core._

object Ex2ProcessVideoFrames {

  /** 处理视频帧的方法，该方法传递给“ VideoProcessor”。 */
  def canny(src: Mat, dest: Mat): Unit = {
    // 灰度转换
    cvtColor(src, dest, COLOR_BGR2GRAY)
    // 计算Canny边缘
    Canny(dest, dest, 100, 200, 3, true)
    // 反转图像
    threshold(dest, dest, 128, 255, THRESH_BINARY_INV)
  }

  def main(args: Array[String]): Unit = {
    // 创建视频处理器实例
    val processor = new VideoProcessor()
//    processor.input = "data/bike.avi"
    processor.input = "data/688990700.mp4"
    // 声明一个窗口以显示输入和输出视频
    processor.displayInput = "Input Video"
    processor.displayOutput = "Output Video"
    // 以原始帧速率播放视频
    processor.delay = math.round(1000d / processor.frameRate)
    // 设置帧处理方式
    processor.frameProcessor = canny
    // 在此帧停止进程
    processor.stopAtFrameNo = -1
    // 开始处理循环
    processor.run()
  }

}
