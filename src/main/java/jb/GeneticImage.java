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
package jb;

import jb.optaplanner.ImageMakerSolution;
import jb.optaplanner.ImagePart;
import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;
import org.optaplanner.core.api.solver.event.BestSolutionChangedEvent;
import org.optaplanner.core.api.solver.event.SolverEventListener;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GeneticImage {

    public static void main(String[] args) throws IOException {
        System.out.println("woop");

        String targetImagePath = "C:\\Users\\jbritton\\OneDrive - Dotted Eyes Solutions Ltd T A Field Dynamics\\Pictures\\glynderw2.jpg";
        String sourceImagePath = "C:\\Users\\jbritton\\OneDrive - Dotted Eyes Solutions Ltd T A Field Dynamics\\Pictures\\Jon_06.jpg";

        BufferedImage targetImage = ImageIO.read(new File(targetImagePath));
        BufferedImage sourceImage = ImageIO.read(new File(sourceImagePath));

        ImageFrame differenceFrame = new ImageFrame();
        ImageFrame resultFrame = new ImageFrame();
        ImageFrame squaresFrame = new ImageFrame();


        EventQueue.invokeLater(() -> {
//            try {
//                ImageFrame frame = new ImageFrame(targetImage);
                squaresFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                squaresFrame.setVisible(true);

                resultFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                resultFrame.setVisible(true);

                differenceFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                differenceFrame.setVisible(true);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        });

        SolverFactory<ImageMakerSolution> solverFactory = SolverFactory.createFromXmlResource("imageMakerSolver.xml");
        Solver<ImageMakerSolution> solver = solverFactory.buildSolver();

        solver.addEventListener(event -> {
            // Ignore infeasible (including uninitialized) solutions
            if (event.getNewBestSolution().getScore().isFeasible()) {
                resultFrame.setImage(event.getNewBestSolution().getGeneratedImage());
                differenceFrame.setImage(event.getNewBestSolution().getDifferenceImage());
                squaresFrame.setImage(event.getNewBestSolution().getSourceSquaresImage());
            }
        });

        ImageMakerSolution solution = new ImageMakerSolution();
        solution.setSourceImage(sourceImage);
        solution.setTargetImage(targetImage);

        List<ImagePart> imageParts = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 300; i++) {
            ImagePart imagePart = new ImagePart();
            imagePart.setSourceX(random.nextInt(sourceImage.getWidth() - solution.getPartWidth()));
            imagePart.setSourceY(random.nextInt(sourceImage.getHeight() - solution.getPartHeight()));
            imagePart.setTargetX(random.nextInt(targetImage.getWidth() - solution.getPartWidth()));
            imagePart.setTargetY(random.nextInt(targetImage.getHeight() - solution.getPartHeight()));
            imageParts.add(imagePart);
        }

        solution.setImageParts(imageParts);

        solver.solve(solution);




    }

    private static BufferedImage cropImage(BufferedImage src, Rectangle rect) {
        return src.getSubimage(0, 0, rect.width, rect.height);
    }

    public static double compareImages(BufferedImage imageA, BufferedImage imageB, BufferedImage differenceImage) {

        double sumSquaredError = 0;

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

                int diffRgb = redDiff;
                diffRgb = (diffRgb << 8) + greenDiff;
                diffRgb = (diffRgb << 8) + blueDiff;
                differenceImage.setRGB(x, y, diffRgb);

                sumSquaredError += Math.pow((double) redDiff + greenDiff + blueDiff, 2.0);
            }
        }
        return sumSquaredError;
    }


}
