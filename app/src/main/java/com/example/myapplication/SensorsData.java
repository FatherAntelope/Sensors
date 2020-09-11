package com.example.myapplication;

public class SensorsData {
    private float X = 0, Y = 0, Z = 0, Brightness = 0;

    public SensorsData(float X, float Y, float Z, float Brightness) {
        this.X = X;
        this.Y = Y;
        this.Z = Z;
        this.Brightness = Brightness;
    }



    public SensorsData(){}

    public float getX() {
        return X;
    }

    public void setX(float x) {
        X = x;
    }

    public float getY() {
        return Y;
    }

    public void setY(float y) {
        Y = y;
    }

    public float getZ() {
        return Z;
    }

    public void setZ(float z) {
        Z = z;
    }

    public float getBrightness() {
        return Brightness;
    }

    public void setBrightness(float brightness) {
        Brightness = brightness;
    }
}
