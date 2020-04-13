package org.example.javacv.opencv_cookbook.chapter2

import java.io.File

import org.example.javacv.opencv_cookbook.OpenCVUtils._
import org.bytedeco.javacpp.indexer.UByteIndexer
import org.bytedeco.opencv.global.opencv_imgcodecs._
import org.bytedeco.opencv.opencv_core._

import scala.util.Random

/**
 * 演示使用“ ByteIndexer”将随机选择的单个像素设置为固定值。
 * @author gujun@qiyi.com
 * @since 2020/4/13 1:00 下午
 */
object Ex1Salt {

  def main(args: Array[String]): Unit = {
    // 读取输入图片
    val image = loadAndShowOrExit(new File("data/boldt.jpg"), IMREAD_COLOR)
    // 添加椒盐噪点
    val dest = salt(image, 2000)
    // 显示
    show(dest, "Salted")
  }

  /**
   * 添加椒盐噪点
   * @param image input image.
   * @param n     number of 'salt' grains.
   */
  def salt(image: Mat, n: Int): Mat = {
    // 随机数发生器
    val random = new Random()
    // 获取图像数据
    val indexer = image.createIndexer().asInstanceOf[UByteIndexer] // 把Indexer转成UByteIndexer对象
    // 将n粒放在随机位置
    val nbChannels = image.channels // 图像通道数
    for (_ <- 1 to n) {
      // 创建像素的随机索引
      val row: Long = random.nextInt(image.rows)
      val col: Long = random.nextInt(image.cols)
      // 通过将每个通道设置为最大值（255）将其设置为白色
      for (i <- 0 until nbChannels) {
        // 多通道图像
        // (列，行，通道，像素值)
        indexer.put(row, col, i.toLong, 255.toByte)
      }
    }
    image
  }

}
