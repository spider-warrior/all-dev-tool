package cn.t.tool.nettytool.watersystem.entity;

import java.util.ArrayList;
import java.util.List;

public class ReadRegisterCommandResponse {

    //仪表地址码
    private byte address;

    //功能码
    private byte func;

    //byte length
    private byte dataByteLength;

    //寄存器数据
    private List<Short> registerDataList = new ArrayList<>();

    private short crc16;

    public byte getAddress() {
        return address;
    }

    public void setAddress(byte address) {
        this.address = address;
    }

    public byte getFunc() {
        return func;
    }

    public void setFunc(byte func) {
        this.func = func;
    }

    public byte getDataByteLength() {
        return dataByteLength;
    }

    public void setDataByteLength(byte dataByteLength) {
        this.dataByteLength = dataByteLength;
    }

    public List<Short> getRegisterDataList() {
        return registerDataList;
    }

    public void setRegisterDataList(List<Short> registerDataList) {
        this.registerDataList = registerDataList;
    }

    public short getCrc16() {
        return crc16;
    }

    public void setCrc16(short crc16) {
        this.crc16 = crc16;
    }

    @Override
    public String toString() {
        String sb = "ReadRegisterCommandResponse{" + "address=" + address +
            ", func=" + func +
            ", dataByteLength=" + dataByteLength +
            ", registerDataList=" + registerDataList +
            ", crc16=" + crc16 +
            '}';
        return sb;
    }
}
