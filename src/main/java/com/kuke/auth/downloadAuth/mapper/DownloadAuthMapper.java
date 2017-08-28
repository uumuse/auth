
package com.kuke.auth.downloadAuth.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.kuke.auth.downloadAuth.bean.OrgDownLog;
import com.kuke.auth.downloadAuth.bean.OrgDownMoney;
import com.kuke.auth.downloadAuth.bean.OrgDownUser;
import com.kuke.auth.ssologin.bean.OrgDownIp;


public interface DownloadAuthMapper {


	public Map getDownloadItemDesc(@Param("item_id") String item_id, @Param("pay_pro_price_id")String pay_pro_price_id);
	
	public List<String> getCategoryListById();
	public List<String> getCategoryByItemcodeId(@Param("itemcode") String id);

	public OrgDownMoney getOrgDownMoney(@Param("org_id") String org_id);

	public OrgDownUser getDownUserByUP(@Param("u")String u, @Param("p")String p,@Param("org_id")String org_id);

	public int updateOrgDownUser(@Param("id") String id, @Param("money")Double money);

	public OrgDownIp getDownIpById(@Param("start_ip")String start_ip,@Param("end_ip")String end_ip);

	public int updateOrgDownIp(@Param("id") int id, @Param("money") Double finalPrice);

	public void recordOrgDownLog(OrgDownLog orgDownLog);

	
	
}

