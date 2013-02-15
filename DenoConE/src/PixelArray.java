/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import org.eclipse.swt.graphics.ImageData;

/**
 *
 * @author vp
 */
public class PixelArray {

    public RGBPixel[][] pixelArray = null;
    public int height = 0;
    public int width = 0;

    public PixelArray(ImageData imageData) {

        if (imageData != null) {
            height = imageData.height;
            width = imageData.width;

            pixelArray = new RGBPixel[height][width];

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    pixelArray[y][x] = new RGBPixel(x, y, imageData);
                }
            }
        }
    }

    public void writeToImageData(ImageData imageData) {
        if (imageData != null && imageData.height == height && imageData.width == width) {
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    pixelArray[y][x].writeToImageData(x, y, imageData);
                }
            }
        }
    }

    public void convertToGrey() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                pixelArray[y][x].convertToGrey();
            }
        }
    }

    public void median(int medianSize) {
        if (medianSize > 2 && medianSize % 2 == 1) {

            int margin = medianSize / 2;

            RGBPixel[][] original = new RGBPixel[height][width];

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    original[y][x] = pixelArray[y][x].clone();
                }
            }

            int medianArraySize = medianSize * medianSize;
            int medianArrayCenter = medianArraySize / 2 + 1;
            int[] tmpR = new int[medianArraySize];
            int[] tmpG = new int[medianArraySize];
            int[] tmpB = new int[medianArraySize];


            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {

                    for (int pY = 0, i = 0; pY < medianSize; pY++) {
                        for (int pX = 0; pX < medianSize; pX++) {
                            if ((y - margin + pY) >= 0 && (x - margin + pX) >= 0 && (y + margin + pY) <= height && (x + margin + pX) <= width) {
                                tmpR[i] = original[y - 1 + pY][x - 1 + pX].r;
                                tmpG[i] = original[y - 1 + pY][x - 1 + pX].g;
                                tmpB[i] = original[y - 1 + pY][x - 1 + pX].b;
                            } else {
                                tmpR[i] = tmpG[i] = tmpB[i] = 127;
                            }
                            i++;
                        }
                    }

                    quickSort(tmpR, 0, medianArraySize - 1);
                    quickSort(tmpG, 0, medianArraySize - 1);
                    quickSort(tmpB, 0, medianArraySize - 1);

                    pixelArray[y][x].r = tmpR[medianArrayCenter];
                    pixelArray[y][x].g = tmpG[medianArrayCenter];
                    pixelArray[y][x].b = tmpB[medianArrayCenter];
                }
            }
        }
    }

    public void convMatrix(int div, int bias, int[][] matrix) {
        if (matrix != null && matrix.length > 1 && matrix[0].length == matrix.length) {

            int margin = matrix.length / 2;

            int divide = 0;
            for (int y = 0; y < matrix.length; y++) {
                for (int x = 0; x < matrix[0].length; x++) {
                    divide += matrix[y][x];
                }
            }

            divide += div;
            if (divide < 1) {
                divide = 1;
            }

            RGBPixel[][] original = new RGBPixel[height][width];

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    original[y][x] = pixelArray[y][x].clone();
                }
            }

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int tmpR = 0, tmpG = 0, tmpB = 0;

                    for (int pY = 0; pY < matrix.length; pY++) {
                        for (int pX = 0; pX < matrix.length; pX++) {
                            if ((y - margin + pY) >= 0 && (x - margin + pX) >= 0 && (y + margin + pY) <= height && (x + margin + pX) <= width) {
                                tmpR += original[y - 1 + pY][x - 1 + pX].r * matrix[pY][pX];
                                tmpG += original[y - 1 + pY][x - 1 + pX].g * matrix[pY][pX];
                                tmpB += original[y - 1 + pY][x - 1 + pX].b * matrix[pY][pX];
                            }
                        }
                    }

                    tmpR /= divide;
                    tmpR += bias;

                    if (tmpR < 0) {
                        tmpR = 0;
                    }
                    if (tmpR > 255) {
                        tmpR = 255;
                    }

                    pixelArray[y][x].r = tmpR;

                    tmpG /= divide;
                    tmpG += bias;

                    if (tmpG < 0) {
                        tmpG = 0;
                    }
                    if (tmpG > 255) {
                        tmpG = 255;
                    }

                    pixelArray[y][x].g = tmpG;

                    tmpB /= divide;
                    tmpB += bias;

                    if (tmpB < 0) {
                        tmpB = 0;
                    }
                    if (tmpB > 255) {
                        tmpB = 255;
                    }

                    pixelArray[y][x].b = tmpB;
                }
            }
        }
    }

    private void quickSort(int[] intCisla, int intLeftB, int intRightB) {

        int intLeft = intLeftB;
        int intRight = intRightB;

        int intMid = intCisla[(intLeft + intRight) / 2];

        do {
            while (intCisla[intLeft] < intMid) {
                intLeft++;
            }

            while (intCisla[intRight] > intMid) {
                intRight--;
            }

            if (intLeft <= intRight) {
                int intTemp = intCisla[intLeft];
                intCisla[intLeft] = intCisla[intRight];
                intCisla[intRight] = intTemp;
                intLeft++;
                intRight--;
            }
        } while (intLeft < intRight);

        if (intRight > intLeftB) {
            quickSort(intCisla, intLeftB, intRight);
        }
        if (intLeft < intRightB) {
            quickSort(intCisla, intLeft, intRightB);
        }
    }
}
