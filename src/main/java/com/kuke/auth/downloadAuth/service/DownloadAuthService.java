
package com.kuke.auth.downloadAuth.service;


import java.util.Map;

import com.kuke.auth.downloadAuth.bean.OrgDownLog;
import com.kuke.auth.downloadAuth.bean.OrgDownMoney;
import com.kuke.auth.downloadAuth.bean.OrgDownUser;
import com.kuke.auth.ssologin.bean.OrgDownIp;


public interface DownloadAuthService {
	

	public Map getDownloadItemDesc(String item_id, String pay_pro_price_id);
	
	public String getChannleIdByItemCode(String itemcode);
	
	public OrgDownMoney getOrgDownMoney(String org_id);
	
	public OrgDownUser getDownUserByUP(String u, String p, String org_id);
	
	public int updateOrgDownUser(String id,Double finalPrice);

	public OrgDownIp getDownIpById(String start_ip,String end_ip);

	public int updateOrgDownIp(int id, Double d);

	public void recordOrgDownLog(OrgDownLog orgDownLog);
	
	
}
