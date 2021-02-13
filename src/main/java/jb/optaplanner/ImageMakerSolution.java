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
package jb.optaplanner;


import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningScore;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.valuerange.CountableValueRange;
import org.optaplanner.core.api.domain.valuerange.ValueRangeFactory;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.score.buildin.hardsoftlong.HardSoftLongScore;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

@PlanningSolution
public class ImageMakerSolution {

    private BufferedImage targetImage;
    private BufferedImage sourceImage;
    private List<ImagePart> imageParts;
    private final int partWidth = 60;
    private final int partHeight = 60;

    private HardSoftLongScore score;

    public BufferedImage getTargetImage() {
        return targetImage;
    }

    public void setTargetImage(BufferedImage targetImage) {
        this.targetImage = targetImage;
    }

    public int getPartWidth() {
        return partWidth;
    }

    public int getPartHeight() {
        return partHeight;
    }

    @PlanningScore
    public HardSoftLongScore getScore() {
        return score;
    }

    @ValueRangeProvider(id = "sourceXRange")
    public CountableValueRange<Integer> getSourceXRange() {
        return ValueRangeFactory.createIntValueRange(0, sourceImage.getWidth() - partWidth);
    }

    @ValueRangeProvider(id = "sourceYRange")
    public CountableValueRange<Integer> getSourceYRange() {
        return ValueRangeFactory.createIntValueRange(0, sourceImage.getHeight() - partHeight);
    }

    @ValueRangeProvider(id = "targetXRange")
    public CountableValueRange<Integer> getTargetXRange() {
        return ValueRangeFactory.createIntValueRange(0, targetImage.getWidth() - partWidth);
    }

    @ValueRangeProvider(id = "targetYRange")
    public CountableValueRange<Integer> getTargetYRange() {
        return ValueRangeFactory.createIntValueRange(0, targetImage.getHeight() - partHeight);
    }

    public void setScore(HardSoftLongScore score) {
        this.score = score;
    }

    public BufferedImage getSourceImage() {
        return sourceImage;
    }

    public void setSourceImage(BufferedImage sourceImage) {
        this.sourceImage = sourceImage;
    }

    @PlanningEntityCollectionProperty
    public List<ImagePart> getImageParts() {
        return imageParts;
    }

    public void setImageParts(List<ImagePart> imageParts) {
        this.imageParts = imageParts;
    }

    public BufferedImage getGeneratedImage() {
        BufferedImage generatedImage = new BufferedImage(targetImage.getWidth(), targetImage.getHeight(), BufferedImage.TYPE_INT_RGB);

        Graphics2D g = generatedImage.createGraphics();

        getImageParts().stream().filter(ImagePart::isFullyInitialised).forEach(imagePart -> {
            BufferedImage cropped = cropImage(sourceImage, imagePart);
            g.drawImage(cropped, imagePart.getTargetX(), imagePart.getTargetY(), null);
        });

        g.dispose();

        return generatedImage;
    }

    public BufferedImage getSourceSquaresImage() {
        BufferedImage generatedImage = new BufferedImage(sourceImage.getWidth(), sourceImage.getHeight(), BufferedImage.TYPE_INT_RGB);

        Graphics2D g = generatedImage.createGraphics();
        g.drawImage(sourceImage, 0, 0, null);

        getImageParts().stream().filter(ImagePart::isFullyInitialised).forEach(imagePart -> {
            g.setColor(Color.red);
            g.drawRect(imagePart.getSourceX(), imagePart.getSourceY(), partWidth, partHeight);
        });

        g.dispose();

        return generatedImage;
    }

    private BufferedImage cropImage(BufferedImage src, ImagePart imagePart) {
        return src.getSubimage(imagePart.getSourceX(), imagePart.getSourceY(), partWidth, partHeight);
    }


    public BufferedImage getDifferenceImage() {
        BufferedImage differenceImage = new BufferedImage(targetImage.getWidth(), targetImage.getHeight(), BufferedImage.TYPE_INT_RGB);
        BufferedImage generatedImage = getGeneratedImage();

        for (int x = 0; x < targetImage.getWidth(); x++) {
            for (int y = 0; y < targetImage.getHeight(); y++) {
                // Compare pixels
                final int imageARgb = targetImage.getRGB(x, y);
                final int imageARed = (imageARgb & 0x00ff0000) >> 16;
                final int imageAGreen = (imageARgb & 0x0000ff00) >> 8;
                final int imageABlue = imageARgb & 0x000000ff;

                final int imageBRgb = generatedImage.getRGB(x, y);
                final int imageBRed = (imageBRgb & 0x00ff0000) >> 16;
                final int imageBGreen = (imageBRgb & 0x0000ff00) >> 8;
                final int imageBBlue = imageBRgb & 0x000000ff;

                int redDiff = Math.abs(imageARed - imageBRed);
                int greenDiff = Math.abs(imageAGreen - imageBGreen);
                int blueDiff = Math.abs(imageABlue - imageBBlue);

                int diffRgb = redDiff;
                diffRgb = (diffRgb << 8) + greenDiff;
                diffRgb = (diffRgb << 8) + blueDiff;
                differenceImage.setRGB(x, y, diffRgb);
            }
        }
        return differenceImage;
    }
}
