package org.example.javacv;

import org.bytedeco.opencv.opencv_core.*;
import org.bytedeco.opencv.opencv_imgproc.*;
import static org.bytedeco.opencv.global.opencv_core.*;
import static org.bytedeco.opencv.global.opencv_imgproc.*;
import static org.bytedeco.opencv.global.opencv_imgcodecs.*;

public class Smoother {

    public static void main(String[] args) {
        Mat image = imread("data/baboon1.jpg");
        if (image != null) {
            // 高斯滤波
            GaussianBlur(image, image, new Size(3, 3), 0);
            imwrite("data/baboon1_smoother.jpg", image);
        }
    }

}
