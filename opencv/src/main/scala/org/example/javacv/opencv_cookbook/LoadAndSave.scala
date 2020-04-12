package org.example.javacv.opencv_cookbook

import javax.swing.WindowConstants
import org.bytedeco.javacv.{CanvasFrame, OpenCVFrameConverter}
import org.bytedeco.opencv.global.opencv_core._
import org.bytedeco.opencv.global.opencv_imgcodecs._
import org.bytedeco.opencv.global.opencv_imgproc._
import org.bytedeco.opencv.opencv_core._

/**
 *
 * 读取，保存，显示和在图像上绘制的示例。
 */
object LoadAndSave {
  def main(args: Array[String]): Unit = {
    val image = imread("data/puppy.bmp", IMREAD_COLOR)
    if (image.empty()) {
      println("Error reading image!")
      System.exit(-1)
    }

    println("This image is " + image.rows + " x " + image.cols)
    // 创建名为“我的图像”的图像窗口。
    val canvas = new CanvasFrame("My Image", 1)
    // 关闭图像窗口时请求关闭应用程序
    canvas.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE)
    // 在窗口上显示图像
    val converter = new OpenCVFrameConverter.ToMat()
    canvas.showImage(converter.convert(image))

    // 我们创建另一个空图像
    val result = new Mat();
    // positive for horizontal
    // 0 for vertical,
    // negative for both
    flip(image, result, 1)
    // the output window
    val canvas2 = new CanvasFrame("Output Image", 1)
    // 关闭图像窗口时请求关闭应用程序
    canvas.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE)
    // 在窗口上显示图像
    canvas2.showImage(converter.convert(result))
    // 保存图像
    imwrite("data/output.bmp", result)

    // create another image window
    val canvas3 = new CanvasFrame("Drawing on an Image", 1)
    val image3 = image.clone()
    circle(image3, // destination image
      new Point(155, 110), // center coordinate
      65, // radius
      new Scalar(0), // color (here black)
      3, // thickness
      8, // 8-connected line
      0) // shift
    putText(image3, // destination image
      "This is a dog.", // text
      new Point(40, 200), // text position
      FONT_HERSHEY_PLAIN, // font type
      2.0, // font scale
      new Scalar(255), // text color (here white)
      2, // text thickness
      8, // Line type.
      false) //When true, the image data origin is at the bottom-left corner. Otherwise, it is at the top-left corner.
    canvas3.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE)
    canvas3.showImage(converter.convert(image3))
  }
}
