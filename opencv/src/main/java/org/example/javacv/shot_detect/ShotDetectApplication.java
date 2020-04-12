package org.example.javacv.shot_detect;



import org.bytedeco.opencv.opencv_videoio.VideoCapture;

import static org.bytedeco.opencv.global.opencv_videoio.CAP_PROP_FRAME_HEIGHT;
import static org.bytedeco.opencv.global.opencv_videoio.CAP_PROP_FRAME_WIDTH;

public class ShotDetectApplication {

    public static void main(String[] args) {
        VideoCapture capture = new VideoCapture("/Users/jun/GitHub/java-cv-example/data/bike.avi");
        System.out.println("height: " + capture.get(CAP_PROP_FRAME_HEIGHT));
        System.out.println("width: " + capture.get(CAP_PROP_FRAME_WIDTH));
        capture.getBackendName();
        capture.release();

    }
}
