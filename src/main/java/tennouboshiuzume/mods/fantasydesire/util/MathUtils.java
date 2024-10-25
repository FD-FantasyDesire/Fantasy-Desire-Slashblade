package tennouboshiuzume.mods.fantasydesire.util;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

import java.util.Random;

/**
 * 数値計算関係
 */
public class MathUtils
{
    /**
     * sin値テーブル.
     *
     * 360°を分解能65536で事前計算してある。
     */
    public static final float[] SIN_TABLE = new float[65536];
    // MathHelper.SIN_TABLE が private なので、
    // 同じものをコッチも用意する。
    // 256Kbyteほど浪費するが、
    // Reflectionでprivateのものは あまり参照したくない.

    static
    {
        for (int i = 0; i < 65536; i++) {
            SIN_TABLE[i] = (float)Math.sin(i * Math.PI * 2.0 / 65536.0);
        }
    }


    /**
     * sin
     *
     * @param deg 角度(単位：度)
     * @return sin値
     */
    public static float sin(float deg)
    {
        // ラジアンで指定する場合は、MathHelper.sin()を使う

        return SIN_TABLE[(int)(65536.0/360.0*deg) & 0xffff];
    }

    /**
     * cos
     *
     * @param deg 角度(単位：度)
     * @return cos値
     */
    public static float cos(float deg)
    {
        return SIN_TABLE[((int)(65536.0/360.0*deg) + 65536/4) & 0xffff];
    }



    /**
     * 度 → ラジアン 変換
     * @param angdeg 度で表した角度
     * @return ラジアンで表した角度
     */
    public static float toRadians(float angdeg)
    {
        // Math.toRadians() のfloat版
        return angdeg * (float)Math.PI / 180.0f;
    }

    static Random random = new Random();
    public static boolean randomCheck(int chance){
        // 生成 0 到 99 之间的随机数，检查是否小于等于 chance
        return random.nextInt(100) < chance;
    }
    static double getDistancePos(double startX,double startY,double startZ,double endX,double endY,double endZ) {
        double f = startX - endX;
        double f1 = startY - endY;
        double f2 = startZ - endZ;
        return MathHelper.sqrt(f * f + f1 * f1 + f2 * f2);
    }

};