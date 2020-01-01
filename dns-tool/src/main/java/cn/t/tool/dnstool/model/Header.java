package cn.t.tool.dnstool.model;

import lombok.Data;

/**
 * @author yj
 * @since 2019-12-31 20:56
 *
 * flag:
 * QR(1比特）：查询/响应的标志位，1为响应报文，0为查询报文。
 * opcode（4比特）：定义查询或响应的类型（若为0则表示是标准的，若为1则是反向的，若为2则是服务器状态请求）。
 * AA（1比特）：当某DNS服务器正好是被请求主机的权威DNS服务器时，1比特的“权威的”标志位被置在回答报文中。
 * TC（1比特）：截断标志位。1表示响应已超过512字节并已被截断。
 * RD（1比特）：这个比特位被请求设置，应答的时候使用的相同的值返回。如果设置了RD，就建议域名服务器进行递归解析，递归查询的支持是可选的。
 * RA（1比特）：这个比特位在应答中设置或取消，用来代表服务器是否支持递归查询。
 * zero（3比特）：保留字段。
 * rcode（4比特）：返回码，表示响应的差错状态，通常为0和3。
 * 0 成功的响应
 * 1 格式错误--域名服务器无法解析请求，因为请求消息格式错误
 * 2 服务器错误--域名服务器因为内部错误无法解析该请求
 * 3 名字错误-- 只在权威域名服务器的响应消息中有效，标示请求中请求的域不存在
 * 4 域名服务器不支持请求的类型
 * 5 域名服务器因为策略的原因拒绝执行请求的操作。例如域名服务器不会为特定的请求者返回查询结果，或者域名服务器不会为特定的请求返回特定的数据
 *
 * ===============================================================================================================================
 *
 * TC（截断标志位）
 * 如何突破DNS报文的512字节限制
 * @link https://blog.csdn.net/yeyiqun/article/details/99310372
 * 根据协议标准文档RFC1035，当封装的DNS响应的长度超过512字节时，协议应采用TCP传输，而不是UDP。（这份RFC文档产生于三十年前）
 * RFC 6891这份标准文档，对DNS进行了扩展，描述了超过512字节的DNS的情况，即EDNS0。详细情况可参考RFC 6891这份文档。
 *
 * DNS协议从UDP切换到TCP的过程如下：
 * 1、客户端向服务器发起UDP DNS请求；
 * 2、如果服务器发现DNS响应数据超过512字节，则返回UDP DNS响应中置truncated位，告知客户端改用TCP进行重新请求；
 * 3、客户端向服务器发起TCP DNS请求；
 * 4、服务器返回TCP DNS响应。
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
    private short additionalRecordsCount;
}
