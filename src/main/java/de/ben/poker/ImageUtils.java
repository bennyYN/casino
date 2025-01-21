package de.ben.poker;

import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;

public class ImageUtils {

    public static BufferedImage blurImage(BufferedImage image, int blurStrength) {
        if (blurStrength < 1) {
            return image;
        }

        int radius = blurStrength;
        int size = radius * 2 + 1;
        float[] data = new float[size];

        float sigma = radius / 3.0f;
        float twoSigmaSquare = 2.0f * sigma * sigma;
        float sigmaRoot = (float) Math.sqrt(twoSigmaSquare * Math.PI);
        float total = 0;

        for (int i = -radius; i <= radius; i++) {
            float distance = i * i;
            data[i + radius] = (float) Math.exp(-distance / twoSigmaSquare) / sigmaRoot;
            total += data[i + radius];
        }

        for (int i = 0; i < data.length; i++) {
            data[i] /= total;
        }

        Kernel kernel = new Kernel(size, 1, data);
        ConvolveOp horizontalOp = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
        BufferedImage tempImage = horizontalOp.filter(image, null);

        kernel = new Kernel(1, size, data);
        ConvolveOp verticalOp = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
        return verticalOp.filter(tempImage, null);
    }
}