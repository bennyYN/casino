package de.ben.blackjack;

public class AnimationManager extends Thread {

    private static double animatedAlpha;
    private static boolean alphaAnimation = true;
    private static int cooldown = 0;

    public AnimationManager() {
        super();
    }

    public void run() {
        while (true) {
            try {
                updateAnimatedAlpha();
                if(cooldown > 0){
                    cooldown--;
                }
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateAnimatedAlpha() {
        if(animatedAlpha < 140 && alphaAnimation){
            animatedAlpha += 0.15;
        }else{
            alphaAnimation = false;
            animatedAlpha -= 0.15;
            if(animatedAlpha < 34){
                alphaAnimation = true;
            }
        }
    }

    public static double getAlpha() {
        return animatedAlpha;
    }

    public static void setCooldown(int ms){
        cooldown = ms;
    }

    public static int getCooldown(){
        return cooldown;
    }

    public static boolean onCooldown(){
        return cooldown > 0;
    }
}
