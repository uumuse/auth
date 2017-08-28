/**
 * 
 */
package com.kuke.pay.service;

import java.util.Map;

/**
 *@Description: TODO(pay调用其他的service)
 * @author lyf
 * @date 2013-4-26 上午10:23:53
 * @version V1.0 
 */
public interface CommentService {
	/**
	 * 得到用户的余额：账户个人余额，账户机构余额
	 * @param uid
	 * @return
	 */
	public Map<String, String> getUserMoneyByUid(String uid);
	/**
	 * 得到机构的余额
	 * @param uid
	 * @return
	 */
	public String getOrgMoney(String org_id);
	/**
	 * 
	 * @param uid
	 * @param points
	 */
	public void updateUserPoints(String uid, String points);
	/**
	 * 判断是否属于spoken
	 */
	public String getChannleIdByItemCode(String itemCode);
	

}
