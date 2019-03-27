package cn.t.tool.nettytool.watersystem.util;

public final class WaterSystemUtil {

    public static short calculateCrc(byte[] bytes) {
        int value = 0xFFFF;
        for(byte b: bytes) {
            for(int i=0; i<8; i++) {
                if((((b>>i) ^ value) & 1) == 1) {
                    value = ((value>>1) ^ 0xA001);
                } else {
                    value>>=1;
                }
            }
        }
        return (short)value;
    }

    private WaterSystemUtil() {}
}
