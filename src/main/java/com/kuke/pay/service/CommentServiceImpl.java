/**
 * 
 */
package com.kuke.pay.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kuke.pay.mapper.PayBillMapper;

/**
 *@Description: TODO(其他功能service)
 * @author lyf
 * @date 2013-4-26 上午10:26:16
 * @version V1.0 
 */
@Service
public class CommentServiceImpl implements CommentService{
	
	@Autowired
	private PayBillMapper payBillMapper;

	/**
	 * @param @param uid
	 * @return  用户帐户余额
	 */
	@Override
	public Map<String, String> getUserMoneyByUid(String uid) {
		return payBillMapper.getUserMoney(uid);
	}
	
	@Override
	public String getOrgMoney(String org_id) {
		return payBillMapper.getOrgMoney(org_id);
	}
	
	/**
	 * @param @param uid
	 * @param @param points
	 * @return 
	 */
	@Override
	public void updateUserPoints(String uid, String points) {
		
	}
	/**
	 * 判断是否属于spoken
	 * @return 
	 */
	@Override
	public String getChannleIdByItemCode(String itemCode) {
		List categoryList=payBillMapper.getCategoryListById();
		List itemcodeCategory=payBillMapper.getCategoryByItemcodeId(itemCode);
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

}
