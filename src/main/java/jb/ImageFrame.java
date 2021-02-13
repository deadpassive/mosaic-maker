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

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class ImageFrame extends JFrame {

    ImageComponent targetComponent = new ImageComponent();

    public ImageFrame() throws IOException {

        setTitle("ImageTest");

        targetComponent = new ImageComponent();
        add(targetComponent);
        setSize(1000, 1000);
        getContentPane().validate();
        getContentPane().repaint();
    }

    public void setImage(Image image) {
//        removeAll();
        int imageWidth = image.getWidth(this);
        int imageHeight = image.getHeight(this);
        setSize(imageWidth, imageHeight);

        targetComponent.setImage(image);
//        repaint();
    }
}
