package io.bingbin.bingbinandroid.utils;

/**
 * Healper class for avatar
 *
 * @author Junyang HE
 * Created by Junyang HE on 22/02/2018.
 */

public abstract class AvatarHelper {
    final static int[] thresholdRabbit = {3, 10, 25, 75, 150, 233, 666, 1024}; // 8
    final static int[] thresholdLeaf = {1, 3, 6, 10, 15, 25, 50, 75, 100, 125, 150, 175, 200, 233, 666, 1024}; // 16

    public static int getAllowMaxRabbitId(int ecoPoint) {
        for(int i = 0; i < thresholdRabbit.length; i++) {
            if(thresholdRabbit[i] > ecoPoint) {
                return i + 1; // id start with 1 and there is a default rabbit
            }
        }
        return thresholdRabbit.length + 1;
    }

    public static int getAllowMaxLeafId(int ecoPoint) {
        for(int i = 0; i < thresholdLeaf.length; i++) {
            if(thresholdLeaf[i] > ecoPoint) {
                return i + 1; // id start with 1 and there is a default leaf
            }
        }
        return thresholdLeaf.length + 1;
    }
}
