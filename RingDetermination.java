/* This class houses our OpenCV vision algorithm. This is a modified version of an open-source
  algorithm from the creators of EasyOpenCV. See engineering notebook/portfolio */
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

    public class RingDetermination extends OpenCvPipeline
    {
        // all possible starting stacks
        public enum RingPosition
        {
            FOUR,
            ONE,
            NONE
        }

         // constants and objects for creating our image submatrix
         final Scalar BLUE = new Scalar(0, 0, 255);
         final Scalar GREEN = new Scalar(0, 255, 0);
         final Point REGION1_TOP_LEFT_ANCHOR_POINT = new Point(285,155);
         final int REGION_WIDTH = 120;
         final int REGION_HEIGHT = 130;
         final int FOUR_RING_THRESHOLD = 140;
         final int ONE_RING_THRESHOLD = 128;

         Point region1_pointA = new Point( REGION1_TOP_LEFT_ANCHOR_POINT.x,
                 REGION1_TOP_LEFT_ANCHOR_POINT.y);
         Point region1_pointB = new Point(REGION1_TOP_LEFT_ANCHOR_POINT.x + REGION_WIDTH,
                 REGION1_TOP_LEFT_ANCHOR_POINT.y + REGION_HEIGHT);

         /* objects for converting the stream to chromatic blue and getting a value for its color
         saturation */
         Mat region1_Cb;
         Mat YCrCb = new Mat();
         Mat Cb = new Mat();
         int avg1;

         // volatile since accessed between two asynchronous threads
         protected volatile RingPosition position = RingPosition.FOUR;

        void inputToCb(Mat input)
        {
            Imgproc.cvtColor(input, YCrCb, Imgproc.COLOR_RGB2YCrCb);
            Core.extractChannel(YCrCb, Cb, 1);
        }
        public void init(Mat firstFrame)
        {
            inputToCb(firstFrame);
            region1_Cb = Cb.submat(new Rect(region1_pointA, region1_pointB));
        }
        public Mat processFrame(Mat input)
        {
            inputToCb(input); // converts camera stream to chromatic blue image

            avg1 = (int) Core.mean(region1_Cb).val[0]; // measures color saturation

            Imgproc.rectangle(
                    input, // Buffer to draw on
                    region1_pointA, // First point which defines the rectangle
                    region1_pointB, // Second point which defines the rectangle
                    BLUE, // The color the rectangle is drawn in
                    2); // Thickness of the rectangle lines

            position = RingPosition.FOUR;
            if(avg1 > FOUR_RING_THRESHOLD){
                position = RingPosition.FOUR;
            }
            else if (avg1 > ONE_RING_THRESHOLD){
                position = RingPosition.ONE;
            }
            else {
                position = RingPosition.NONE;
            }

            Imgproc.rectangle(
                    input, // Buffer to draw on
                    region1_pointA, // First point which defines the rectangle
                    region1_pointB, // Second point which defines the rectangle
                    GREEN, // The color the rectangle is drawn in
                    -1); // Negative thickness means solid fill

            return input;
        }
        public int getAnalysis()
        {
            return avg1;
        }
    }