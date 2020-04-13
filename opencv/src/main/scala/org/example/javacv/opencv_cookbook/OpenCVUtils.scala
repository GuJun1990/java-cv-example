/*
 * Copyright (c) 2011-2019 Jarek Sacha. All Rights Reserved.
 *
 * Author's e-mail: jpsacha at gmail.com
 */

package org.example.javacv.opencv_cookbook

import java.awt._
import java.awt.image.BufferedImage
import java.io.File
import java.nio.IntBuffer

import javax.swing.WindowConstants
import org.bytedeco.javacpp.indexer.FloatIndexer
import org.bytedeco.javacpp.{DoublePointer, IntPointer}
import org.bytedeco.javacv.OpenCVFrameConverter.ToMat
import org.bytedeco.javacv.{CanvasFrame, Java2DFrameConverter}
import org.bytedeco.opencv.global.opencv_core._
import org.bytedeco.opencv.global.opencv_imgcodecs._
import org.bytedeco.opencv.global.opencv_imgproc._
import org.bytedeco.opencv.opencv_core.{Point, _}

import scala.math.round


/** 简化OpenCV API使用的辅助方法. */
object OpenCVUtils {

  /** 加载图像并在CanvasFrame中显示。如果无法加载图像，则应用程序将退出并显示代码1
    *
    * @param flags 指定已加载图像的颜色类型的标志：
    *              <ul>
    *              <li> `>0` 返回三通道彩色图像</li>
    *              <li> `=0` 返回灰度图像</li>
    *              <li> `<0` 照原样返回加载的图像。注意，在当前实现中从输出图像中剥离了Alpha通道（如果有）。例如，如果`flags'大于0，则将4通道RGBA图像加载为RGB。</li>
    *              </ul>
    *              默认值为彩色图像。
    *              @return loaded image
   * */
  def loadAndShowOrExit(file: File, flags: Int = IMREAD_COLOR): Mat = {
    // 读取输入图像
    val image = loadOrExit(file, flags)
    show(image, file.getName)
    image
  }

  /** 加载图像。如果无法加载图像，则应用程序将退出并显示代码1。
    *
    * @param flags 指定已加载图像的颜色类型的标志：
    *              <ul>
    *              <li> `>0` 返回三通道彩色图像</li>
    *              <li> `=0` 返回灰度图像</li>
    *              <li> 照原样返回加载的图像。注意，在当前实现中从输出图像中剥离了Alpha通道（如果有）。例如，如果`flags'大于0，则将4通道RGBA图像加载为RGB。</li>
    *              </ul>
    *              默认值为彩色图像。
    * @return loaded image
   * */
  def loadOrExit(file: File, flags: Int = IMREAD_COLOR): Mat = {
    // 读取输入图像
    val image = imread(file.getAbsolutePath, flags)
    if (image.empty()) {
      println("Couldn't load image: " + file.getAbsolutePath)
      sys.exit(1)
    }
    image
  }

  /** 在窗口中显示图像。关闭窗口将退出应用程序。 */
  def show(mat: Mat, title: String): Unit = {
    val converter = new ToMat()
    val canvas = new CanvasFrame(title, 1)
    canvas.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE)
    canvas.showImage(converter.convert(mat))
  }

  /** 在窗口中显示图像。关闭窗口将退出应用程序。 */
  def show(image: Image, title: String): Unit = {
    val canvas = new CanvasFrame(title, 1)
    canvas.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE)
    canvas.showImage(image)
  }


  /** 在图像上的点位置绘制红色圆圈。 */
  def drawOnImage(image: Mat, points: Point2fVector): Mat = {
    val dest = image.clone()
    val radius = 5
    val red = new Scalar(0, 0, 255, 0)
    for (i <- 0 until points.size.toInt) {
      val p = points.get(i)
      circle(dest, new Point(round(p.x), round(p.y)), radius, red)
    }

    dest
  }

  /** 在图像上绘制形状。
    *
    * @param image   input image
    * @param overlay shape to draw
    * @param color   color to use
    * @return 绘制叠加层的新图像
    */
  def drawOnImage(image: Mat, overlay: Rect, color: Scalar): Mat = {
    val dest = image.clone()
    rectangle(dest, overlay, color)
    dest
  }

  /** 将图像保存到指定的文件。
   *
   * 根据文件扩展名选择图像格式（有关扩展名列表，请参见OpenCV文档中的“ imread（）”）。
   * 使用此功能只能保存8位（如果是PNG，JPEG 2000和TIFF，则为16位）单通道或3通道（“ BGR”通道顺序）图像。
   * 如果格式，深度或通道顺序不同，请在保存之前使用Mat :: convertTo（）和cvtColor（）进行转换。
   *
   * @param file 文件保存路径。文件扩展名决定输出图像格式。
    * @param image 待保存的图片
    */
  def save(file: File, image: Mat): Unit = {
    imwrite(file.getAbsolutePath, image)
  }

  /** 将Native vector 转换为JVM数组。
    *
    * @param keyPoints 指向包含KeyPoints的Native vector的指针
    */
  def toArray(keyPoints: KeyPoint): Array[KeyPoint] = {
    val oldPosition = keyPoints.position()
    // 将keyPoints转换为Scala序列
    val points = for (i <- Array.range(0, keyPoints.capacity.toInt)) yield new KeyPoint(keyPoints.position(i))
    // 明确重置位置，以避免此基于位置的容器的其他用途引起问题。
    keyPoints.position(oldPosition)

    points
  }

  /** 将Native vector 转换为JVM数组。
    *
    * @param keyPoints 指向包含KeyPoints的Native vector的指针
    */
  def toArray(keyPoints: KeyPointVector): Array[KeyPoint] = {
    // 为了简化实施，我们将假定关键点的数量在Int范围内。
    require(keyPoints.size() <= Int.MaxValue)
    val n = keyPoints.size().toInt
    // 将keyPoints转换为Scala序列
    for (i <- Array.range(0, n)) yield new KeyPoint(keyPoints.get(i))
  }

  /** 将Native vector 转换为JVM数组。
    *
    * @param matches 指向包含DMatches的Native vector的指针。
    * @return
    */
  def toArray(matches: DMatchVector): Array[DMatch] = {
    // 为了简化实施，我们将假定关键点的数量在Int范围内。
    require(matches.size() <= Int.MaxValue)
    val n = matches.size().toInt

    // 将keyPoints转换为Scala序列
    for (i <- Array.range(0, n)) yield new DMatch(matches.get(i))
  }

  def toBufferedImage(mat: Mat): BufferedImage = {
    val openCVConverter = new ToMat()
    val java2DConverter = new Java2DFrameConverter()
    java2DConverter.convert(openCVConverter.convert(mat))
  }


  def toPoint(p: Point2f): Point = new Point(round(p.x), round(p.y))


  /**
    * 将Mat转换为1，其中像素以8位无符号整数（CV_8U）表示
    * 它创建输入图像的副本。
    *
    * @param src input image.
    * @return 输入的副本，像素值表示为8位无符号整数。
    */
  def toMat8U(src: Mat, doScaling: Boolean = true): Mat = {
    val minVal = new DoublePointer(Double.MaxValue)
    val maxVal = new DoublePointer(Double.MinValue)
    minMaxLoc(src, minVal, maxVal, null, null, new Mat())
    val min = minVal.get(0)
    val max = maxVal.get(0)
    val (scale, offset) = if (doScaling) {
      val s = 255d / (max - min)
      (s, -min * s)
    } else (1d, 0d)

    val dest = new Mat()
    src.convertTo(dest, CV_8U, scale, offset)
    dest
  }

  def toMatPoint2f(points: Seq[Point2f]): Mat = {
    // 创建代表Point3f向量的Mat
    val dest = new Mat(1, points.size, CV_32FC2)
    val indx = dest.createIndexer().asInstanceOf[FloatIndexer]
    for (i <- points.indices) {
      val p = points(i)
      indx.put(0, i, 0, p.x)
      indx.put(0, i, 1, p.y)
    }
    require(dest.checkVector(2) >= 0)
    dest
  }

  /**
    * 将Point3D的序列转换为表示Point3f的向量的Mat。
    * 在返回值上调用`checkVector（3）`将返回非负值，表明它是具有3个通道的向量。
    */
  def toMatPoint3f(points: Seq[Point3f]): Mat = {
    // 创建代表Point3f向量的Mat
    val dest = new Mat(1, points.size, CV_32FC3)
    val indx = dest.createIndexer().asInstanceOf[FloatIndexer]
    for (i <- points.indices) {
      val p = points(i)
      indx.put(0, i, 0, p.x)
      indx.put(0, i, 1, p.y)
      indx.put(0, i, 2, p.z)
    }
    dest
  }

  def toPoint2fArray(mat: Mat): Array[Point2f] = {
    require(mat.checkVector(2) >= 0, "Expecting a vector Mat")

    val indexer = mat.createIndexer().asInstanceOf[FloatIndexer]
    val size = mat.total.toInt
    val dest = new Array[Point2f](size)

    for (i <- 0 until size) dest(i) = new Point2f(indexer.get(0, i, 0), indexer.get(0, i, 1))
    dest
  }

  /**
    * 将Point2f的向量转换为表示Points2f的向量的Mat。
    */
  def toMat(points: Point2fVector): Mat = {
    // 创建代表Point3f向量的Mat
    val size: Int = points.size.toInt
    // Mat构造函数的参数必须为“ Int”以表示大小，否则可能被解释为内容。
    val dest = new Mat(1, size, CV_32FC2)
    val indx = dest.createIndexer().asInstanceOf[FloatIndexer]
    for (i <- 0 until size) {
      val p = points.get(i)
      indx.put(0, i, 0, p.x)
      indx.put(0, i, 1, p.y)
    }
    dest
  }


  /** 将Scala集合转换为JavaCV“向量”。
    *
    * @param src Scala collection
    * @return JavaCV/native collection
    */
  def toVector(src: Array[DMatch]): DMatchVector = {
    val dest = new DMatchVector(src.length)
    for (i <- src.indices) dest.put(i, src(i))
    dest
  }

  /**
    * 创建一个MatVector并将mat作为唯一元素。
    *
    * @return
    */
  def wrapInMatVector(mat: Mat): MatVector = {
    new MatVector(Array(mat): _*)
  }

  /**
    * 创建一个IntBuffer，并将v用作唯一元素。
    *
    * @return
    */
  def wrapInIntBuffer(v: Int): IntBuffer = {
    IntBuffer.wrap(Array(v))
  }

  /**
    * 创建一个“ IntPointer”并将“ v”作为其唯一元素。
    *
    * @return
    */
  def wrapInIntPointer(v: Int): IntPointer = {
    new IntPointer(1L).put(v)
  }

  /**
    * 打印有关`mat`的信息。
    */
  def printInfo(mat: Mat, caption: String = ""): Unit = {
    println(
      caption + "\n" +
        s"  cols:     ${mat.cols}\n" +
        s"  rows:     ${mat.rows}\n" +
        s"  depth:    ${mat.depth}\n" +
        s"  channels: ${mat.channels}\n" +
        s"  type:     ${mat.`type`}\n" +
        s"  dims:     ${mat.dims}\n" +
        s"  total:    ${mat.total}\n"
    )
  }
}