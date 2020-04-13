package org.example.javacv.opencv_cookbook.chapter2

import java.io.File

import org.bytedeco.javacpp.indexer.FloatIndexer
import org.bytedeco.opencv.global.opencv_core._
import org.bytedeco.opencv.global.opencv_imgcodecs._
import org.bytedeco.opencv.global.opencv_imgproc._
import org.bytedeco.opencv.opencv_core._
import org.example.javacv.opencv_cookbook.OpenCVUtils._
/**
 * 遍历图像和领域操作
 * 在图像处理中，通过当前位置的相邻像素计算新的像素值是很常见的操作。
 * 当领域包含图像的前几行和后几行时，就需要同时扫描图像的若干行。
 * 本例子对图像进行锐化，它基于拉普拉斯算子。
 * 总所周知，将一副图像减去它经过拉普拉斯绿波之后的图像，这幅图像的边缘部分将得到放大，
 * 即细节部分更加锐利。锐化算子的计算方法如下：
 * sharpened_pixel = 5*current-left-right-up-down;
 * @author gujun@qiyi.com
 * @since 2020/4/13 7:28 下午
 */
object Ex3Sharpen {
  def main(args: Array[String]): Unit = {
    // 读取图像
    val image = loadAndShowOrExit(new File("data/boldt.jpg"), IMREAD_COLOR)
    // 定义输出图像
    val dest = new Mat();
    // 构建锐化内核，所有未分配值均为0
    val kernel = new Mat(3, 3, CV_32F, new Scalar(0))

    val ki = kernel.createIndexer().asInstanceOf[FloatIndexer]
    ki.put(1, 1, 5)
    ki.put(0, 1, -1)
    ki.put(2, 1, -1)
    ki.put(1, 0, -1)
    ki.put(1, 2, -1)
    // 过滤图像
    filter2D(image, dest, image.depth(), kernel)
    // 显示
    show(dest, "Sharpened")
  }
}
