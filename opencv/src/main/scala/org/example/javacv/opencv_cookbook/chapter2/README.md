## 访问像素
在JavaCV中需要使用`Indexer`来访问图像(`Mat`)的像素值。

`Indexer`有助于在`Mat`对象中定位（索引）和访问像素。
根据图像的类型，图像中的各个像素可以用不同的方式表示，例如，它们可以是字节，整数，双精度数。
每个图像类型都有专门的`Indexer`，例如`ByteIndexer`，`IntIndexer`，`DoubleIndexer`。

还可以通过其他方式索引（访问）图像中的像素:
* 通过指定（x，y）或（column，row）坐标以用于灰度图像，例如put（x，y，value），
* 通过为多通道图像（例如彩色图像）（例如put（x，y，channel，value））指定[x，y，channel）或（column，row，channel），
* 通过假设一个图像是像素的一个长集合，例如put（i，value），可以通过单个一维坐标（i）直接索引存储的值，则该图像可以与任何图像一起使用。

前两种方法很简单。
要遍历像素和通道值，需要知道列数（Mat.cols），行数（Mat.rows）和通道数（Mat.channels）。
在下面的示例中使用第三种方法，直接索引存储的值。
需要记住的关键是像素值是逐行存储的。
例如，如果需要计算特定列和行中像素的索引，则将行索引乘以列数（Mat.cols），然后添加列索引：
```scala
val index = (row*image.cols) + column
val v = image.get(index)
```
这对于灰度图像效果很好。对于彩色图像和一般的多通道图像，还需要考虑要访问的通道值。
每个通道的像素值彼此相邻存储，因此要计算到Mat中的偏移量以提取（行，列，通道）值，使用以下命令：
```scala
val index = (row * image.cols) + column
val offset = index * image.channels + channel
val v = image.get(offset)
```