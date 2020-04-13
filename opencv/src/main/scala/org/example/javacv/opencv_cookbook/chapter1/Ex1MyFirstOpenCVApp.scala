package org.example.javacv.opencv_cookbook.chapter1

import javax.swing.WindowConstants
import org.bytedeco.javacv.{CanvasFrame, OpenCVFrameConverter}
import org.bytedeco.opencv.global.opencv_imgcodecs.imread

/**
 * 使用JavaCV API加载和显示图像的示例
 * 注意，在Scala示例代码中，如何使用JavaCV API中的CanvasFrame来显示图像
 */
object Ex1MyFirstOpenCVApp {
  def main(args: Array[String]): Unit = {
    // 读取图片
    val image = imread("data/boldt.jpg");
    if (image.empty()) {
      println("读取图像失败！")
      System.exit(-1);
    }
    // 创建图片窗口命名为"My Image"
    // 设置CanvasFrame不要应用gama校正
    // 通过将gamma设置为1，否则图像将看起来不正确。
    val canvas = new CanvasFrame("My Image", 1)
    // 关闭图像窗口时请求关闭应用程序
    canvas.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE)
    // 从OpenCV Mat转换为Java缓冲图像以进行显示
    val converter = new OpenCVFrameConverter.ToMat()
    // 在窗口上显示图像
    canvas.showImage(converter.convert(image))
  }
}
