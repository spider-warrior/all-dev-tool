package cn.t.tool.dhcptool.constants;

/**
 * 选项
 *
 * @author yj
 * @since 2020-01-05 13:37
 **/
public enum OptionType {

    PAD((byte)0),
    /**
     * 子设置子网掩码选项
     */
    SUBNET_MASK((byte)1),
    TIME_OFFSET((byte)2),
    /**
     * 设置网关地址选项
     */
    ROUTER((byte)3),
    TIME_SERVER((byte)4),
    NAME_SERVER((byte)5),
    /**
     * 设置DNS服务器地址选项
     */
    DOMAIN_NAME_SERVER((byte)6),
    LOG_SERVER((byte)7),
    QUOTES_SERVER((byte)8),
    LPR_SERVER((byte)9),
    IMPRESS_SERVER((byte)10),
    RLP_SERVER((byte)11),
    /**
     * Impress Server Option, 一种过时不再生产的的网络打印机???
     * 设置DHCP客户端的主机名选项???
     * 两种不同的说明(frc参考文档和wire shark说明不一致)
     */
    HOST((byte)12),
    BOOT_FILE((byte)13),
    MERIT_DUMP((byte)14),
    /**
     * 设置域名后缀选项
     */
    DOMAIN_NAME((byte)15),
    SWAP_SERVER((byte)16),
    ROOT_PATH((byte)17),
    EXTENSION_FILE((byte)18),
    FORWARD_O((byte)19),
    SRC_RTE_ON_OFF((byte)20),
    POLICY_FILTER((byte)21),
    MAX_DG((byte)22),
    DEFAULT_IP((byte)23),
    MTU_TIMEOUT((byte)24),
    MTU_PLATEAU((byte)25),
    MTU_I((byte)26),
    MTU_SUB((byte)27),
    BROADCAST_ADDRESS((byte)28),
    MASK_DISCOVERY((byte)29),
    MASK_SUPPLIER((byte)30),
    ROUTER_DISCOVERY((byte)31),
    ROUTER_REQUEST((byte)32),
    /**
     * 设置静态路由选项。该选项中包含一组有分类静态路由（即目的地址的掩码固定为自然掩码，不能划分子网），客户端收到该选项后，将在路由表中添加这些静态路由。如果存在Option121，则忽略该选项。
     */
    STATIC_ROUTE((byte)33),
    TRAILERS((byte)34),
    ARP_TIMEOUT((byte)35),
    ETHER((byte)36),
    DEFAULT_TCP((byte)37),
    KEEPALIVE_TIME((byte)38),
    KEEPALIVE_DATA((byte)39),
    NIS_DOMAIN((byte)40),
    NIS_SERVERS((byte)41),
    NTP_SERVERS((byte)42),
    VENDOR_SPECIFIC((byte)43),
    /**
     * 设置NetBios服务器选项
     */
    NET_BIOS_NAME_SRV((byte)44),
    NET_BIOS_DIST((byte)45),
    /**
     * 设置NetBios节点类型选项
     */
    NET_BIOS_NODE_TYPE((byte)46),
    NET_BIOS_SCOPE((byte)47),
    X_WINDOW_FONT((byte)48),
    X_WINDOW_MANAGER((byte)49),
    /**
     * 设置请求IP选项
     */
    ADDRESS_REQUEST((byte)50),
    /**
     * 设置IP地址租约时间选项
     */
    ADDRESS_TIME((byte)51),
    /**
     * 设置Option附加选项
     */
    OVERLOAD((byte)52),
    /**
     * dhcp消息类型
     */
    DHCP_MSG_TYPE((byte)53),
    /**
     * dhcp服务器
     */
    DHCP_SERVER((byte)54),
    /**
     * dhcp消息参数列表
     * 客户端利用该选项指明需要从服务器获取哪些网络配置参数。该选项内容为客户端请求的参数对应的选项值。
     */
    PARAMETER_LIST((byte)55),
    /**
     * dhcp服务器文字消息
     */
    DHCP_MESSAGE((byte)56),
    /**
     * dhcp最大消息字节数
     */
    DHCP_MAX_MSG_SIZE((byte)57),
    /**
     * 客户端过渡到RENEWING状态的时间间隔(单位秒，从分配ip地址时间算起),一般是租期时间的50%。
     */
    RENEWAL_TIME((byte)58),
    /**
     * 客户端过渡到REBINDING状态的时间间隔(单位秒，从分配ip地址时间算起)一般是租期时间的87.5%。
     */
    REBINDING_TIME((byte)59),
    /**
     * 设置厂商分类信息选项，用于标识DHCP客户端的类型和配置
     */
    CLASS_ID((byte)60),
    /**
     * 设置客户端标识选项
     */
    CLIENT_ID((byte)61),
    NET_WARE_IP_DOMAIN((byte)62),
    NET_WARE_IP_OPTION((byte)63),
    NIS_DOMAIN_NAME((byte)64),
    NIS_SERVER_ADDR((byte)65),
    /**
     * 设置TFTP服务器名选项，用来指定为客户端分配的TFTP服务器的域名
     */
    SERVER_NAME((byte)66),
    /**
     * 设置启动文件名选项，用来指定为客户端分配的启动文件名
     */
    BOOT_FILE_NAME((byte)67),
    HOME_AGENT_ADDRS((byte)68),
    SMTP_SERVER((byte)69),
    POP((byte)70),
    NNTP_SERVER((byte)71),
    WWW_SERVER((byte)72),
    FINGER_SERVER((byte)73),
    IRC_SERVER((byte)74),
    STREETTALK_SERVER((byte)75),
    STDA_SERVER((byte)76),
    /**
     * 设置用户类型标识
     */
    USER_CLASS((byte)77),
    DIRECTORY_AGE((byte)78),
    SERVICE_SCOPE((byte)79),
    RAPID_COMMIT((byte)80),
    CLIENT_FQD((byte)81),
    RELAY_AGE((byte)82),
    ISNS((byte)83),
    REMOVED_UNASSIGNED((byte)84),
    NDS_SERVERS((byte)85),
    NDS_TREE((byte)86),
    NDS_CO((byte)87),
    BCMCS_COTROLLER_DOMAIN_NAME_LIST((byte)88),
    BCMCS_COTROLLER_IPV((byte)89),
    AUTHENTICATION((byte)90),
    CLIENT_LAST_TRANSACTION_TIME_OPTION((byte)91),
    ASSOCIATED_IP_OPTION((byte)92),
    CLIENT_SYSTEM((byte)93),
    CLIENT_((byte)94),
    LDAP((byte)95),
    UUID_GUID((byte)97),
    USER_AUTH((byte)98),
    GEOCONF_CIVIC((byte)99),
    PCODE((byte)100),
    TCODE((byte)101),
    UNASSIGNED((byte)109),
    NET_INFO_ADDRESS((byte)112),
    NET_INFO_TAG((byte)113),
    URL((byte)114),
    AUTO_CONFIG((byte)116),
    NAME_SERVICE((byte)117),
    SUBNET_SELECTION_OPTION((byte)118),
    DOMAIN_SEARCH((byte)119),
    SIP_SERVERS((byte)120),
    /**
     * 设置无分类路由选项。该选项中包含一组无分类静态路由（即目的地址的掩码为任意值，可以通过掩码来划分子网），客户端收到该选项后，将在路由表中添加这些静态路由
     */
    CLASSLESS_STATIC((byte)121),
    CCC((byte)122),
    GEO_CONF_OPTION((byte)123),
    REMOTE_STATISTICS((byte)131),
    HTTP_PROXY((byte)135),
    OPTION_PANA_AGENT((byte)136),
    OPTION_V((byte)137),
    PRIVATE_PROXY_AUTO_DISCOVERY((byte)252),
    END((byte)255),
    ;
    public final byte value;

    OptionType(byte value) {
        this.value = value;
    }

    public static OptionType getOptionType(byte value) {
        for(OptionType type: values()) {
            if(type.value == value) {
                return type;
            }
        }
        return null;
    }
}
