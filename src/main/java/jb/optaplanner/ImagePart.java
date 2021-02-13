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

import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

@PlanningEntity
public class ImagePart {
    private Integer sourceX;
    private Integer sourceY;
    private Integer targetX;
    private Integer targetY;
    private Integer rotation;

    @PlanningVariable(valueRangeProviderRefs = {"sourceXRange"})
    public Integer getSourceX() {
        return sourceX;
    }

    public void setSourceX(Integer sourceX) {
        this.sourceX = sourceX;
    }

    @PlanningVariable(valueRangeProviderRefs = {"sourceYRange"})
    public Integer getSourceY() {
        return sourceY;
    }

    public void setSourceY(Integer sourceY) {
        this.sourceY = sourceY;
    }

    @PlanningVariable(valueRangeProviderRefs = {"targetXRange"})
    public Integer getTargetX() {
        return targetX;
    }

    public void setTargetX(Integer targetX) {
        this.targetX = targetX;
    }

    @PlanningVariable(valueRangeProviderRefs = {"targetXRange"})
    public Integer getTargetY() {
        return targetY;
    }

    public void setTargetY(Integer targetY) {
        this.targetY = targetY;
    }

    public boolean isFullyInitialised() {
        return null != sourceX && null != sourceY & null != targetX && null != targetY && null != rotation;
    }

    @PlanningVariable(valueRangeProviderRefs = {"rotationRange"})
    public Integer getRotation() {
        return rotation;
    }

    public void setRotation(Integer rotation) {
        this.rotation = rotation;
    }
}
