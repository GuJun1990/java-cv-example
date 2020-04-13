package org.example.javacv.opencv_cookbook.chapter2

import java.io.File

import org.bytedeco.javacpp.indexer.UByteIndexer
import org.bytedeco.opencv.global.opencv_imgcodecs.IMREAD_COLOR
import org.bytedeco.opencv.opencv_core.Mat
import org.example.javacv.opencv_cookbook.OpenCVUtils._

/**
 *
 * @author gujun@qiyi.com
 * @since 2020/4/13 1:15 下午
 */
object Ex2ColorReduce {

  def main(args: Array[String]): Unit = {
    // 读取输入图片
//    val image = loadAndShowOrExit(new File("data/boldt.jpg"), IMREAD_COLOR)
    val image = loadAndShowOrExit(new File("data/baboon1.jpg"), IMREAD_COLOR)
    // 减少颜色数量
    val dest = colorReduce(image,6)// div = (0, 8), 每个通道颜色缩减倍数：2^div
    // 显示
    show(dest, "Reduced colors")
  }

  /**
   * 减少颜色数量。
   * 彩塑图像有三个通道组成，每个通道对应三原色（红/绿/蓝）之一的强度。由于每个强度值都是用一个8位的无符号整数表示，
   * 所以全部可能的颜色数目为256*256*256，大约1670万个。为了降低分析的复杂读，降低图像中的颜色数目有时候是有用的。
   * 一个简单的方法就是将RGB空间划分为同等大小的格子，例如，将每个维度的颜色数降低为原来的1/8，那么总的颜色数就为
   * 32*32*32。原始图像中的每个颜色都替换为它所在格子的中心对应的颜色。因此，这个算法很简单：如果N是颜色缩小的比例，
   * 那么对于图像中每个像素的每个通道，将其除以N（整数除法），舍去余数，然后在乘以N，这样就能得到不大于原始像素值的
   * N的最大倍值。如果对于每个8为通道的值都进行上述操作，那么就可以得到共计256/N*256/N*256/N个颜色值。
   * @param image input image.
   * @param div color reduction factor.
   */
  def colorReduce(image: Mat, div: Int = 5): Mat = {
    // 索引器用于访问图像中的值
    val indexer = image.createIndexer().asInstanceOf[UByteIndexer]
    // 元素总数，合并每个通道的组件
    val nbElements = image.rows * image.cols * image.channels // 将所有像素点拉成一行
    // 与运算掩码
    val mask = 0xff << div
    val off = 0x1 << (div-1)
    for (i <- 0 until nbElements) {
      // 转换为整数，将字节视为无符号值
      val v = indexer.get(i) & 0xFF
      // 使用整数除法以减少值的数量
//      val newV = v / div * div + div // div 为 2^div
      val newV = (v & mask) + off // 位运算操作
      // 重新放回图像
      indexer.put(i, (newV & 0xFF).toByte)
    }
    image
  }
}
