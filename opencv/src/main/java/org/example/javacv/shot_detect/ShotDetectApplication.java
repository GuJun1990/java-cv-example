package org.example.javacv.shot_detect;



import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FrameGrabber;

public class ShotDetectApplication {

    public static void main(String[] args) throws FrameGrabber.Exception  {
        FFmpegFrameGrabber grabber = new FFmpegFrameGrabber("data/bike.avi");
        grabber.start();
        System.out.println("height: " + grabber.getImageHeight());
        System.out.println("width: " + grabber.getImageWidth());
        System.out.println("fps: " + grabber.getFrameRate());
        System.out.println("frame number: " + grabber.getLengthInFrames());
        grabber.release();
    }
}
