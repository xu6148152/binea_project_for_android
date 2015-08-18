package com.example.android.model;

import com._94fifty.model.DribblingRecord;
import java.io.Serializable;

/**
 * Created by xubinggui on 8/18/15.
 */
public class DribblingRecordWrapper implements BaseData, Serializable {
    private boolean isLastNotification;
    private long activityId;
    private int recordId;
    private long sampleCountToFirstDribble;
    private long sampleCountToLastDribble;
    private long sampleCountToBufferEnd;
    private long sampleCountInControl;
    private long totalDribbles;
    private int maxConsecutiveDribbles;
    private int currentConsecutiveDribbles;
    private int averageConsecutiveDribbles;
    private int averageDribbleSpeed;
    private int dribbleIntensityMovingAverage;
    private int dribbleIntensityActivityAverage;
    private int dribbleRestartCount;

    public DribblingRecordWrapper(DribblingRecord record) {
        isLastNotification = record.isLastNotification();
        activityId = record.getActivityId();
        recordId = record.getRecordId();
        sampleCountToFirstDribble = record.getSampleCountToFirstDribble();
        sampleCountToLastDribble = record.getSampleCountToLastDribble();
        sampleCountToBufferEnd = record.getSampleCountToBufferEnd();
        sampleCountInControl = record.getSampleCountInControl();
        totalDribbles = record.getTotalDribbles();
        maxConsecutiveDribbles = record.getMaxConsecutiveDribbles();
        currentConsecutiveDribbles = record.getCurrentConsecutiveDribbles();
        averageConsecutiveDribbles = record.getAverageConsecutiveDribbles();
        averageDribbleSpeed = record.getAverageDribbleSpeed();
        dribbleIntensityMovingAverage = record.getDribbleIntensityMovingAverage();
        dribbleIntensityActivityAverage = record.getDribbleIntensityActivityAverage();
        dribbleRestartCount = record.getDribbleRestartCount();
    }

    public boolean isLastNotification() {
        return this.isLastNotification;
    }

    public void setLastNotification(boolean isLastNotification) {
        this.isLastNotification = isLastNotification;
    }

    public long getActivityId() {
        return this.activityId;
    }

    public void setActivityId(long activityId) {
        this.activityId = activityId;
    }

    public int getRecordId() {
        return this.recordId;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    public long getSampleCountToFirstDribble() {
        return this.sampleCountToFirstDribble;
    }

    public void setSampleCountToFirstDribble(long sampleCountToFirstDribble) {
        this.sampleCountToFirstDribble = sampleCountToFirstDribble;
    }

    public long getSampleCountToLastDribble() {
        return this.sampleCountToLastDribble;
    }

    public void setSampleCountToLastDribble(long sampleCountToLastDribble) {
        this.sampleCountToLastDribble = sampleCountToLastDribble;
    }

    public long getSampleCountToBufferEnd() {
        return this.sampleCountToBufferEnd;
    }

    public void setSampleCountToBufferEnd(long sampleCountToBufferEnd) {
        this.sampleCountToBufferEnd = sampleCountToBufferEnd;
    }

    public long getSampleCountInControl() {
        return this.sampleCountInControl;
    }

    public void setSampleCountInControl(long sampleCountInControl) {
        this.sampleCountInControl = sampleCountInControl;
    }

    public long getTotalDribbles() {
        return this.totalDribbles;
    }

    public void setTotalDribbles(long totalDribbles) {
        this.totalDribbles = totalDribbles;
    }

    public int getMaxConsecutiveDribbles() {
        return this.maxConsecutiveDribbles;
    }

    public void setMaxConsecutiveDribbles(int maxConsecutiveDribbles) {
        this.maxConsecutiveDribbles = maxConsecutiveDribbles;
    }

    public int getCurrentConsecutiveDribbles() {
        return this.currentConsecutiveDribbles;
    }

    public void setCurrentConsecutiveDribbles(int currentConsecutiveDribbles) {
        this.currentConsecutiveDribbles = currentConsecutiveDribbles;
    }

    public int getAverageConsecutiveDribbles() {
        return this.averageConsecutiveDribbles;
    }

    public void setAverageConsecutiveDribbles(int averageConsecutiveDribbles) {
        this.averageConsecutiveDribbles = averageConsecutiveDribbles;
    }

    public int getAverageDribbleSpeed() {
        return this.averageDribbleSpeed;
    }

    public void setAverageDribbleSpeed(int averageDribbleSpeed) {
        this.averageDribbleSpeed = averageDribbleSpeed;
    }

    public int getDribbleIntensityMovingAverage() {
        return this.dribbleIntensityMovingAverage;
    }

    public void setDribbleIntensityMovingAverage(int dribbleIntensityMovingAverage) {
        this.dribbleIntensityMovingAverage = dribbleIntensityMovingAverage;
    }

    public int getDribbleIntensityActivityAverage() {
        return this.dribbleIntensityActivityAverage;
    }

    public void setDribbleIntensityActivityAverage(int dribbleIntensityActivityAverage) {
        this.dribbleIntensityActivityAverage = dribbleIntensityActivityAverage;
    }

    public int getDribbleRestartCount() {
        return this.dribbleRestartCount;
    }

    public void setDribbleRestartCount(int dribbleRestartCount) {
        this.dribbleRestartCount = dribbleRestartCount;
    }

    public String toString() {
        return String.format("activityId=%d recordID=%d isLast=%s", new Object[]{Long.valueOf(this.activityId), Integer.valueOf(this.recordId), Boolean.valueOf(this.isLastNotification)});
    }

}
