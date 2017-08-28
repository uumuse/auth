
package com.kuke.auth.downloadAuth.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kuke.auth.downloadAuth.bean.OrgDownLog;
import com.kuke.auth.downloadAuth.bean.OrgDownMoney;
import com.kuke.auth.downloadAuth.bean.OrgDownUser;
import com.kuke.auth.downloadAuth.mapper.DownloadAuthMapper;
import com.kuke.auth.ssologin.bean.OrgDownIp;

@Service
public class DownloadAuthServiceImpl implements DownloadAuthService {

	@Autowired
	public DownloadAuthMapper downloadAuthMapper;
	
	@Override
	public Map getDownloadItemDesc(String item_id, String pay_pro_price_id) {
		return downloadAuthMapper.getDownloadItemDesc(item_id,pay_pro_price_id);
	}
	
	@Override
	public String getChannleIdByItemCode(String itemcode){
		List categoryList=this.downloadAuthMapper.getCategoryListById();
		List itemcodeCategory=this.downloadAuthMapper.getCategoryByItemcodeId(itemcode);
		for(int i=0;i<itemcodeCategory.size();i++){
			for(int j=0;i<categoryList.size();j++){
				try{
					if(itemcodeCategory.get(i).equals(categoryList.get(j)))
						return "2";
				}catch(Exception e){
					break;
				}
			}
		}
		return "1";
	}

	@Override
	public OrgDownMoney getOrgDownMoney(String org_id) {
		return downloadAuthMapper.getOrgDownMoney(org_id);
	}

	@Override
	public OrgDownUser getDownUserByUP(String u, String p,String org_id) {
		return downloadAuthMapper.getDownUserByUP(u,  p,org_id);
	}

	@Override
	public int updateOrgDownUser(String id,Double money) {
		return downloadAuthMapper.updateOrgDownUser(id,money);
	}

	@Override
	public OrgDownIp getDownIpById(String start_ip,String end_ip) {
		return downloadAuthMapper.getDownIpById(start_ip,end_ip);
	}

	@Override
	public int updateOrgDownIp(int id, Double finalPrice) {
		return downloadAuthMapper.updateOrgDownIp(id,finalPrice);
	}

	@Override
	public void recordOrgDownLog(OrgDownLog orgDownLog) {
		downloadAuthMapper.recordOrgDownLog(orgDownLog);
		
	}

	
}
