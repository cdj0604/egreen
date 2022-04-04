package com.egreen2.egeen2.Data;

public class AttendStateData {
    int jucha;
    int minJucha, maxJucha;
    double attenRatio;

    public AttendStateData(int jucha, int minJucha, int maxJucha, double attenRatio) {
        this.jucha = jucha;
        this.minJucha = minJucha;
        this.maxJucha = maxJucha;
        this.attenRatio = attenRatio;
    }

    public int getjucha() {
        return jucha;
    }

    public int getMinJucha() {
        return minJucha;
    }

    public int getMaxJucha() {
        return maxJucha;
    }

    public double getAttenRatio() {
        return attenRatio;
    }
}
