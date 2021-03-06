package org.example.javacv.opencv_cookbook.chapter3

import java.awt.Color

/**
 *
 * @author gujun@qiyi.com
 * @since 2020/4/13 8:19 下午
 */
class ColorRGB {

  def fromBGR(b: Array[Byte]) : ColorRGB = {
    require(b.length == 3)
    ColorRGB(b(2) & 0xFF, b(1) & 0xFF, b(0) & 0xFF)
  }

  /** Factory method for creating a color from a 3 int array. */
  def fromBGR(b: Array[Int]): ColorRGB = {
    require(b.length == 3)
    ColorRGB(b(2), b(1), b(0))
  }

  /**
   * Represents a color in RGB color space. Component values are expected to be in range [0-255]
   */
  case class ColorRGB(red: Int, green: Int, blue: Int) {

    def this(color: Color) = this(color.getRed, color.getGreen, color.getBlue)

    def toColor = new Color(red, green, blue)
  }
}
