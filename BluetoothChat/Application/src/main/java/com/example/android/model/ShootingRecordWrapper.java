package com.example.android.model;

import com._94fifty.model.ShootingRecord;
import java.io.Serializable;

/**
 * Created by xubinggui on 8/18/15.
 */
public class ShootingRecordWrapper implements BaseData, Serializable {

    private boolean isLastNotification;
    private Boolean smartNetDetected;
    private long activityId;
    private int recordId;
    private long sampleCountToFirstShot;
    private long sampleCountToLastShot;
    private long sampleCountToBufferEnd;
    private short currentShotMade;
    private long sampleCountToShotRelease;
    private long sampleCountToShotRimDetection;
    private int currentShotReleaseTime;
    private int averageShotReleaseTime;
    private int currentShotArc;
    private int averageShotArc;
    private short currentShotSpin;
    private short averageShotSpin;
    private int currentShotLaunchHeight;
    private int averageShotLaunchHeight;
    private int currentShotDistance;

    public ShootingRecordWrapper(ShootingRecord record) {
        isLastNotification = record.isLastNotification();
        smartNetDetected = record.getSmartNetDetected();
        activityId = record.getActivityId();
        recordId = record.getRecordId();
        sampleCountToFirstShot = record.getSampleCountToFirstShot();
        sampleCountToLastShot = record.getSampleCountToLastShot();
        sampleCountToBufferEnd = record.getSampleCountToBufferEnd();
        currentShotMade = record.getCurrentShotMade();
        sampleCountToShotRelease = record.getSampleCountToShotRelease();
        sampleCountToShotRimDetection = record.getSampleCountToShotRimDetection();
        currentShotReleaseTime = record.getCurrentShotReleaseTime();
        averageShotReleaseTime = record.getAverageShotReleaseTime();
        currentShotArc = record.getAverageShotArc();
        averageShotArc = record.getAverageShotArc();
        currentShotSpin = record.getCurrentShotSpin();
        averageShotSpin = record.getAverageShotSpin();
        currentShotLaunchHeight = record.getCurrentShotLaunchHeight();
        averageShotLaunchHeight = record.getAverageShotLaunchHeight();
        currentShotDistance = getCurrentShotDistance();
    }

    public boolean isLastNotification() {
        return this.isLastNotification;
    }

    public void setLastNotification(boolean isLastNotification) {
        this.isLastNotification = isLastNotification;
    }

    public Boolean getSmartNetDetected() {
        return this.smartNetDetected;
    }

    public void setSmartNetDetected(Boolean smartNetDetected) {
        this.smartNetDetected = smartNetDetected;
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

    public long getSampleCountToFirstShot() {
        return this.sampleCountToFirstShot;
    }

    public void setSampleCountToFirstShot(long sampleCountToFirstShot) {
        this.sampleCountToFirstShot = sampleCountToFirstShot;
    }

    public long getSampleCountToLastShot() {
        return this.sampleCountToLastShot;
    }

    public void setSampleCountToLastShot(long sampleCountToLastShot) {
        this.sampleCountToLastShot = sampleCountToLastShot;
    }

    public long getSampleCountToBufferEnd() {
        return this.sampleCountToBufferEnd;
    }

    public void setSampleCountToBufferEnd(long sampleCountToBufferEnd) {
        this.sampleCountToBufferEnd = sampleCountToBufferEnd;
    }

    public short getCurrentShotMade() {
        return this.currentShotMade;
    }

    public void setCurrentShotMade(short currentShotMade) {
        this.currentShotMade = currentShotMade;
    }

    public long getSampleCountToShotRelease() {
        return this.sampleCountToShotRelease;
    }

    public void setSampleCountToShotRelease(long sampleCountToShotRelease) {
        this.sampleCountToShotRelease = sampleCountToShotRelease;
    }

    public long getSampleCountToShotRimDetection() {
        return this.sampleCountToShotRimDetection;
    }

    public void setSampleCountToShotRimDetection(long sampleCountToShotRimDetection) {
        this.sampleCountToShotRimDetection = sampleCountToShotRimDetection;
    }

    public int getCurrentShotReleaseTime() {
        return this.currentShotReleaseTime;
    }

    public void setCurrentShotReleaseTime(int currentShotReleaseTime) {
        this.currentShotReleaseTime = currentShotReleaseTime;
    }

    public int getAverageShotReleaseTime() {
        return this.averageShotReleaseTime;
    }

    public void setAverageShotReleaseTime(int averageShotReleaseTime) {
        this.averageShotReleaseTime = averageShotReleaseTime;
    }

    public int getCurrentShotArc() {
        return this.currentShotArc;
    }

    public void setCurrentShotArc(int currentShotArc) {
        this.currentShotArc = currentShotArc;
    }

    public int getAverageShotArc() {
        return this.averageShotArc;
    }

    public void setAverageShotArc(int averageShotArc) {
        this.averageShotArc = averageShotArc;
    }

    public short getCurrentShotSpin() {
        return this.currentShotSpin;
    }

    public void setCurrentShotSpin(short currentShotSpin) {
        this.currentShotSpin = currentShotSpin;
    }

    public short getAverageShotSpin() {
        return this.averageShotSpin;
    }

    public void setAverageShotSpin(short averageShotSpin) {
        this.averageShotSpin = averageShotSpin;
    }

    public int getCurrentShotLaunchHeight() {
        return this.currentShotLaunchHeight;
    }

    public void setCurrentShotLaunchHeight(int currentShotLaunchHeight) {
        this.currentShotLaunchHeight = currentShotLaunchHeight;
    }

    public int getAverageShotLaunchHeight() {
        return this.averageShotLaunchHeight;
    }

    public void setAverageShotLaunchHeight(int averageShotLaunchHeight) {
        this.averageShotLaunchHeight = averageShotLaunchHeight;
    }

    public int getCurrentShotDistance() {
        return this.currentShotDistance;
    }

    public void setCurrentShotDistance(int currentShotDistance) {
        this.currentShotDistance = currentShotDistance;
    }

    public String toString() {
        return String.format("activityId=%d recordID=%d isLast=%s", new Object[]{Long.valueOf(this.activityId), Integer.valueOf(this.recordId), Boolean.valueOf(this.isLastNotification)});
    }

}
