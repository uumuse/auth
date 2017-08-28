package com.kuke.pay.mapper;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.junit.runners.Parameterized.Parameters;

import com.kuke.auth.downloadAuth.bean.OrgDownLog;
import com.kuke.auth.ssologin.bean.OrgDownIp;
import com.kuke.pay.bean.PayBill;
import com.kuke.pay.bean.PayBillItem;
import com.kuke.pay.bean.PayBillMsg;
import com.kuke.pay.bean.PayBillNotifyResult;
import com.kuke.pay.bean.PayProPrice;


public interface PayBillMapper {
	/***
	 * 根据资源Id检测是否被成功下载
	 * @param itemId
	 * @return
	 */
	
	public List getPayStatusByItemId(@Param("itemId")String itemId,@Param("uid")String uid,@Param("lId")String lId);
	
	/***
	 * 根据临时Id查询机构下载订单
	 * @param temp_down_id
	 * @return
	 */
	public List<Map<String,String>> getPayBillByTempId(@Param("temp_down_id")String temp_down_id,@Param("item_id")String item_id,
			@Param("item_code")String item_code,@Param("f")String flag);
	
	/***
	 * 更新订单状态为隐藏
	 */
	public void updateOrgBillStatusByTempId(@Param("temp_down_id")String temp_down_id,@Param("itemid")String itemid);
	/**
	 * 根据uid得到订单
	 * @param uid
	 * @return
	 */
	public List<Map<String,String>> getUserBillByUserId(String uid);
	
	
	public List<Map<String,String>> getUserBillCheck(@Param("user_id")String user_id,
			@Param("item_id")String item_id,@Param("item_code")String item_code,@Param("f") String f);
	
	
	public List<Map<String,String>> getIosUserBillCheck(@Param("user_id")String user_id,
			@Param("item_id")String item_id,@Param("item_code")String item_code,@Param("f") String f);
	
	/**
	 * 根据ID得到支付项目名称及价格
	 * @param id
	 * @return
	 */
	public PayProPrice getPayProPriceById(@Param("id") int id);
	/**
	 * 建立订单PayBill
	 * @param payBill
	 */
	public void insertPayBill(PayBill payBill);
	/**
	 * 建立订单PayBillItem
	 * @param payBillItem
	 */
	public void insertPayBillItem(PayBillItem payBillItem);
	/**
	 * 通过keyword得到PayBill详细信息
	 * @param keyword
	 * @return
	 */
	public PayBill getPayBillPriceByKeyword(@Param("keyword") String keyword);
	/**
	 * 通过keyword得到PayBillItems详细信息
	 * @param keyword
	 * @return
	 */
	public List getPayBillItemsByKeyword(@Param("keyword") String keyword);
	/**
	 * 支付方式 paybillmsg
	 * @param keyword
	 * @return
	 */
	public List<Map> getPayBillMsgsByKeyword(@Param("keyword") String keyword);
	/**
	 * 支付方式 paybillmsg
	 * @param keyword
	 * @return
	 */
	public List<Map> getPayBillMsgDesc(@Param("keyword") String keyword);
	/**
	 * 查询用户moeny表,得到moeny信息
	 * @param uid
	 * @return
	 */
	public Map<String, String> getUserMoneyDB(@Param("uid") String uid);
	/**
	 * remain_money:得到账户余额
	 * @param uid
	 * @return
	 */
	public Map<String, String> getUserMoney(@Param("uid") String uid);
	/**
	 * 得到机构的余额
	 * @param uid
	 * @return
	 */
	public String getOrgMoney(@Param("org_id") String org_id);
	/**
	 * 修改用户个人余额
	 * @param uid
	 * @param remainMoney
	 */
	public void updateUserMoneyByUid(@Param("uid") String uid, @Param("remain_money") double remainMoney);
	/**
	 * 修改用户机构余额
	 * @param uid
	 * @param remainMoney
	 */
	public void updateUserOrg_MoneyByUid(@Param("uid") String uid, @Param("org_money") double org_money);
	/**
	 * 修改用户个人余额
	 * @param uid
	 * @param remainMoney
	 */
	public void updateOrgMoney(@Param("org_id") String org_id, @Param("remain_money") double remainMoney);
	/**
	 * 添加该用户数据
	 */
	public void insertUserMoney(@Param("map") Map<String, String> map);
	/**
	 * 修改支付状态为2 已支付
	 * @param keyword
	 * @param cdate
	 */
	public void setBillStatusByKeyword(@Param("keyword") String keyword,@Param("cDate") Date cdate,@Param("trade_no") String trade_no);
	/**
	 * 修改支付状态为2 已支付
	 * @param keyword
	 */
	public void setBilItemStatusBykeyword(@Param("keyword") String keyword);
	/**
	 * 修改支付状态为2 已支付
	 * @param keyword
	 */
	public void updateBillMsgStatusByKeyword(@Param("keyword") String keyword);
	/**
	 * 添加result信息
	 * @param payBillNotifyResult
	 */
	public void insertPayBillNotifyResult(PayBillNotifyResult payBillNotifyResult);
	/**
	 * 判断是否属于spoken
	 */
	public List getCategoryListById();
	public List getCategoryByItemcodeId(@Param("itemcode") String itemCode);
	/**
	 * 删除paybillmsg
	 */
	public void delPayBillMsg(@Param("keyword") String keyword);
	/**
	 * 添加paybillmsg记录
	 * @param payBillMsg
	 */
	public void insertPayBillMsg(PayBillMsg payBillMsg);
	/***
	 * 根据苹果交易号得到订单
	 * @param trade_no
	 * @return
	 */
	public PayBill selectPayBillByTradeNo(@Param("trade_no")String trade_no);
	/**
	 * 根据channelId获取pay_pro_price信息
	 * @param id
	 * @return
	 */
	public List getProPriceByChannelId(@Param("channelId") int channelId);
	/**
	 * 根据用户名和订单号 查询订单
	 * @param keyword
	 * @param uid
	 * @return
	 */
	public PayBill getPayBillByKeywordUid(@Param("keyword") String keyword);
	/**
	 * 带CD数量的Price
	 * @param item_code
	 * @return
	 */
	public Map<String,String> getPayProPriceNoCD(@Param("item_code") String item_code);
	/**
	 * 根据id查询乐谱
	 */
	public Map<String, String> getMusicBookObjById(@Param("id") String id);
	/**
	 * 根据id查询spoken
	 */
	public Map<String, String> getSpokenById(@Param("id") String id );
	
	
	/**
	 * 记录IP下载订单
	 * @param orgDownLog
	 * @return
	 */
	public int buildOrgIPDownLog(OrgDownLog orgDownLog);
//	public int buildOrgIPDownLog(
//	@Param(value = "org_id")String org_id,@Param(value = "type") String pay_pro_price_id,
//	@Param(value = "price") String price,@Param(value = "pay_detail_id")String pay_detail_id,@Param(value ="down_id")String item_id,
//	@Param(value = "account_name")String account_name,@Param(value = "ip")String ip,@Param(value = "cname")String cname,
//	@Param(value = "ename")String ename,@Param(value = "image")String image,@Param(value = "from_client")String from_client,
//	@Param(value = "ip_acc_id")String ip_acc_id,@Param(value = "account_type")String account_type,@Param(value = "temp_down_id")String temp_down_id);
	/**
	 * 得到机构的开发余额
	 * @param params
	 * @return
	 */
	public Map<String, Object> getOrgMoneys(@Param("params") Map<String, String> params);
	/**
	 * 得到IP的余额
	 * @param params
	 * @return
	 */
	public List<OrgDownIp> getOrgIPMoney(@Param("params") Map<String, String> params);
	/**
	 * 得到机构账户小的共享余额
	 * @param params
	 * @return
	 */
	public Map<String,Object> getOrgUserMoney(@Param("params") Map<String, String> params);
	
	public int updateOrgIPMoney(@Param("money")double money,@Param("id")String id);
	public int updateOrgMoneys(@Param("money")double money,@Param("orgid")String orgid,@Param("productid")String productid);
	public int updateOrgUserMoney(@Param("money")double money,@Param("orgid")String orgid,@Param("productid")String productid);
}
