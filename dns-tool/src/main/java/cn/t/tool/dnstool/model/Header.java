package cn.t.tool.dnstool.model;

import lombok.Data;

/**
 * @author yj
 * @since 2019-12-31 20:56
 **/

@Data
public class Header {

    /* 会话标识（2字节）*/
    private short transID;
    /* Flags（2字节）*/
    private short flags;
    /* query domain count（2字节）*/
    private short queryDomainCount;
    /* answer count（2字节）*/
    private short answerCount;
    /* authoritative name server count（2字节）*/
    private short authoritativeNameServerCount;
    /* additional records（2字节）*/
    private short additionalRecords;
}
