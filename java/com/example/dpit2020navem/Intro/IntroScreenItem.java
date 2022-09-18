package com.example.dpit2020navem.Intro;

public class IntroScreenItem {

    String introText;
    int introPicture;

    public IntroScreenItem(String introText, int introPicture) {
        this.introText = introText;
        this.introPicture = introPicture;
    }

    public String getIntroText() {
        return introText;
    }

    public void setIntroText(String introText) {
        this.introText = introText;
    }

    public int getIntroPicture() {
        return introPicture;
    }

    public void setIntroPicture(int introPicture) {
        this.introPicture = introPicture;
    }
}
