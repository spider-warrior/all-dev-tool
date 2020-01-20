package cn.t.tool.dhcptool.constants;

/**
 * 硬件类型
 *
 * @author yj
 * @since 2020-01-05 11:13
 **/
public enum  HardwareType {

    /**
     * 以太网(最常见)
     */
    ETHER_NET((byte)1),

    /**
     * IEEE802 NETWORKS
     */
    IEEE_802_NETWORKS((byte)6),

    /**
     * ARC NET
     */
    ARC_NET((byte)7),

    /**
     * FRAME RELAY
     */
    FRAME_RELAY((byte)15),

    /**
     * ASYNCHRONOUS TRANSFER MODE(ATM)
     */
    ASYNCHRONOUS_TRANSFER_MODE_16((byte)16),

    /**
     * HDLC
     */
    HDLC((byte)17),

    /**
     * FIBRE CHANNEL
     */
    FIBRE_CHANNEL((byte)18),

    /**
     * ASYNCHRONOUS TRANSFER MODE(ATM)
     */
    ASYNCHRONOUS_TRANSFER_MODE_19((byte)19),

    /**
     * SERIAL LINE
     */
    SERIAL_LINE((byte)20)
    ;
    public final byte value;

    HardwareType(byte value) {
        this.value = value;
    }

    public static HardwareType getHardwareType(byte value) {
        for(HardwareType type: values()) {
            if(type.value == value) {
                return type;
            }
        }
        return null;
    }
}
