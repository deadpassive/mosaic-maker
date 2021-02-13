/*
 * Copyright (c) Dotted Eyes Ltd 2021.
 * All Rights Reserved.
 *
 * This Software is the confidential information of
 * Dotted Eyes Ltd. 67-71 Northwood St,
 * Birmingham, B3 1TX, United Kingdom.
 *
 * The software may be used only in accordance with the terms
 * of the licence agreement made with Dotted Eyes Ltd.
 *
 */
package jb.optaplanner.score;

import jb.optaplanner.ImageMakerSolution;
import org.optaplanner.core.api.score.buildin.hardsoftlong.HardSoftLongScore;
import org.optaplanner.core.api.score.calculator.EasyScoreCalculator;

import java.awt.image.BufferedImage;

public class ImageMakerEasyScoreCalculator implements EasyScoreCalculator<ImageMakerSolution, HardSoftLongScore> {

    @Override
    public HardSoftLongScore calculateScore(ImageMakerSolution imageMakerSolution) {
        BufferedImage targetImage = imageMakerSolution.getTargetImage();
        BufferedImage generatedImage = imageMakerSolution.getGeneratedImage();

        long diff = compareImages(targetImage, generatedImage);

        return HardSoftLongScore.ofSoft(-diff);
    }

    public static long compareImages(BufferedImage imageA, BufferedImage imageB) {

        long totalError = 0;

        for (int x = 0; x < imageA.getWidth(); x++) {
            for (int y = 0; y < imageA.getHeight(); y++) {
                // Compare pixels
                final int imageARgb = imageA.getRGB(x, y);
                final int imageARed = (imageARgb & 0x00ff0000) >> 16;
                final int imageAGreen = (imageARgb & 0x0000ff00) >> 8;
                final int imageABlue = imageARgb & 0x000000ff;

                final int imageBRgb = imageB.getRGB(x, y);
                final int imageBRed = (imageBRgb & 0x00ff0000) >> 16;
                final int imageBGreen = (imageBRgb & 0x0000ff00) >> 8;
                final int imageBBlue = imageBRgb & 0x000000ff;

                int redDiff = Math.abs(imageARed - imageBRed);
                int greenDiff = Math.abs(imageAGreen - imageBGreen);
                int blueDiff = Math.abs(imageABlue - imageBBlue);

//                int redDiff = (imageARed - imageBRed) * (imageARed - imageBRed);
//                int greenDiff = (imageAGreen - imageBGreen) * (imageAGreen - imageBGreen);
//                int blueDiff = (imageABlue - imageBBlue) * (imageABlue - imageBBlue);


                totalError += redDiff + greenDiff + blueDiff;
            }
        }
        return totalError;
    }
}
