package de.ben.ui.menu.carousel;

public class RotationAnimation {

    private int currentFrame = 0;
    private int direction = 0;
    private int startAnimationFrame = 1; //TODO: ändern zu "startFrame"
    private final int ANIMATION_SPEED = 10;
    private final int ANIMATION_DELAY = 1;
    private boolean isAnimating = false;

    public int getCurrentFrame() {
        return currentFrame;
    }

    public int getDirection() {
        return direction;
    }

    public int getStartAnimationFrame() {
        return startAnimationFrame;
    }

    public boolean isAnimating() {
        return isAnimating;
    }

    public void setAnimatingState(boolean animating) {
        isAnimating = animating;
    }

    public void setCurrentFrame(int currentFrame) {
        this.currentFrame = currentFrame;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public void doStep() {
        currentFrame = currentFrame + ANIMATION_SPEED;
    }
}