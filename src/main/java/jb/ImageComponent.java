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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.Serial;

public class ImageComponent extends JComponent implements ActionListener {

    @Serial
    private static final long serialVersionUID = 1L;

    Timer timer=new Timer(200, this);

    private Image image;
    public ImageComponent(){
        timer.start();// Start the timer here.
    }

    public void setImage(Image image) {
        this.image = image;
    }

    @Override
    public void paintComponent (Graphics g){
        if(this.image == null) {
            return;
        }
        int imageWidth = image.getWidth(this);
        int imageHeight = image.getHeight(this);

        g.drawImage(image, 0, 0, this);

//        for (int i = 0; i*imageWidth <= getWidth(); i++) {
//            for(int j = 0; j*imageHeight <= getHeight();j++) {
//                if(i+j>0) {
//                    g.copyArea(0, 0, imageWidth, imageHeight, i*imageWidth, j*imageHeight);
//                }
//            }
//        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==timer){
            repaint();// this will call at every 1 second
        }
    }
}
