package cn.t.tool.nettytool.watersystem.entity;

public class ReadRegisterCommand {

    //仪表地址码
    private byte address;

    //功能码
    private byte func;

    //寄存器开始地址
    private short registerStartAddress;

    //寄存器结束地址
    private short registerCount;

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

    public short getRegisterStartAddress() {
        return registerStartAddress;
    }

    public void setRegisterStartAddress(short registerStartAddress) {
        this.registerStartAddress = registerStartAddress;
    }

    public short getRegisterCount() {
        return registerCount;
    }

    public void setRegisterCount(short registerCount) {
        this.registerCount = registerCount;
    }


}
