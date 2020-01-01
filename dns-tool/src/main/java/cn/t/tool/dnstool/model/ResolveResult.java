package cn.t.tool.dnstool.model;

import lombok.Data;

import java.util.List;

/**
 * @author yj
 * @since 2020-01-01 15:01
 **/
@Data
public class ResolveResult extends Message {
    private List<ResolvedRecord> resolvedRecordList;
}
