#include <jni.h>
#include <opencv2/core.hpp>
#include <opencv2/objdetect.hpp>
#include <opencv2/imgproc.hpp>
#include <opencv2/imgcodecs.hpp>
#include <vector>


using namespace cv;
using namespace std;

#ifdef __cplusplus

extern "C"{
#endif

JNIEXPORT jbyteArray JNICALL Java_com_example_mrzreaderlibrary_DetectionBasedTracker_detectMrzRegion
        (JNIEnv *env, jclass , jlong frameAddress){

    Mat rectKernel = getStructuringElement(MORPH_RECT, Size(13, 5));
    Mat sqKernel = getStructuringElement(MORPH_RECT, Size(34, 34));

    Mat& image = *(Mat*) frameAddress;

//argc != 2 ||
    if (image.rows <= 0 ||image.empty() || !image.data) {
        return NULL;
    }

    cv:rotate(image, image, ROTATE_90_COUNTERCLOCKWISE);

    Mat gray;
    cvtColor(image, gray, COLOR_BGR2GRAY);
//resize(image, outimage, cv__So)

    GaussianBlur(gray, gray, Size(3, 3), 0);
    Mat blackHat;
    morphologyEx(gray, blackHat, MORPH_BLACKHAT, rectKernel);

//compute the Scharr gradient of the blackhat image and scale the
//result into the range[0, 255]
    Mat gradX;
    Mat abs_grad_x;
    int scale = 1;
    int delta = 0;
    Sobel( blackHat, gradX, CV_8UC1, 1, 0,-1, scale, delta, BORDER_DEFAULT );
    convertScaleAbs( gradX, abs_grad_x );

//apply a closing operation using the rectangular kernel to close
// gaps in between letters — then apply Otsu’s thresholding method
    double threshValue = 0;
    double maxValue = 255;
    Mat thresh;
    morphologyEx(gradX, gradX, MORPH_CLOSE, rectKernel);
    threshold(gradX, thresh, threshValue, maxValue, THRESH_BINARY | THRESH_OTSU);
// perform another closing operation, this time using the square
// kernel to close gaps between lines of the MRZ, then perform a
// serieso of erosions to break apart connected components
    Mat thresh2;
    morphologyEx(thresh, thresh2, MORPH_CLOSE, sqKernel);
    erode(thresh2, thresh, NULL, Point(-1,-1), 4);

// find contours in the thresholded image and sort them by their
// sizes
    vector<vector<Point> > cnts;
    try{
        findContours( thresh, cnts, RETR_EXTERNAL, CHAIN_APPROX_SIMPLE);
    }
    catch (exception& e) {
//        cout << "Exception" << e.what() << endl;
    }

    Mat roi;
// loop over the contours
    for (size_t i = 0; i< cnts.size(); i++){
        Rect rect = boundingRect(Mat(cnts[i]));
        float ar = rect.width / float(rect.height);
        float crWidth = rect.width / float(gray.size[1]);

// check to see if the aspect ratio and coverage width are within
// acceptable criteria
        if (ar > 5 && crWidth > 0.75) {
// pad the bounding box since we applied erosions and now need
// to re – grow it
            int pX = int((rect.x + rect.width) * 0.03);
            int pY = int((rect.y + rect.height) * 0.03);
            int x = rect.x - pX;
            int y = rect.y - pY;
            int w = rect.width + (pX * 2);
            int h = rect.height + (pY * 2);

// extract the ROI from the image and draw a bounding box
// surrounding the MRZ
            try{
                image(Range(y, y + h), Range(x, x + w)).copyTo(roi);
                rectangle(image, Point(x, y), Point(x + w, y + h), Scalar(0, 255, 0), 2, LINE_8, 0);
            }
            catch (exception& e) {
            }
            break;
        }
    }

//return to java crop image
    if(!roi.empty() && roi.cols > 0){
        Mat outImg;
//0.75
        rotate(roi, roi, ROTATE_180);

        resize(roi, outImg, Size(roi.cols * 0.99,roi.rows * 0.99), 0, 0, INTER_CUBIC);

        vector<unsigned char> imageDesV;
        imencode(".bmp", outImg, imageDesV);
//convert vector<char> to jbyteArray
        jbyte* result_e = new jbyte[imageDesV.size()];
        jbyteArray result = env->NewByteArray(imageDesV.size());
        for (int i = 0; i < imageDesV.size(); i++) {
            result_e[i] = (jbyte)imageDesV[i];
        }
        env->SetByteArrayRegion(result, 0, imageDesV.size(), result_e);
        return (jbyteArray) result;
    }else{
        return NULL;
    }
}
#ifdef __cplusplus
}
#endif