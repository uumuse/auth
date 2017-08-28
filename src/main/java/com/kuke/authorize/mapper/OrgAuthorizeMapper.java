package com.kuke.authorize.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface OrgAuthorizeMapper {

	public List<Map<String, String>> orgChannelList(@Param("orgId") String orgId);

}
