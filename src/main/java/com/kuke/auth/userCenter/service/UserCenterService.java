package com.kuke.auth.userCenter.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import com.kuke.auth.login.bean.User;
import com.kuke.auth.userCenter.bean.Coupon;
import com.kuke.auth.userCenter.bean.UserAuthorize;
import com.kuke.auth.userCenter.bean.UserFolder;
import com.kuke.auth.userCenter.bean.UserMessage;
@Service
public interface UserCenterService {
	
	public Map<String,String> getOrgExpireTime(String orgId);
	
	public List getUserBill(Map params,int offset,int rows);

	public List getPointsExchangeRecord(Map<String, String> params,int offset,int rows);

	public List getBillInf(Map<String, String> params, int offset, int rows);
	
	public List getBillInfIOS(Map<String, String> params, int offset, int rows);
	/**
	 * 得到用户的服务状态
	 * @param uid
	 * @return
	 */
	public Map<String, String> getUserAuthorize(Map<String, String> params);
	/**
	 * 根据l_code得到item_code
	 * @param l_code
	 * @return
	 */
	public Map<String, Object> getItemcodeByLcode(String l_code);
	/**
	 * 取消订单
	 * @param params
	 * @return
	 */
	public int cancleBill(Map<String, String> params);
	/**
	 * 失效订单
	 * @param params
	 * @return
	 */
	public int invalidBill(String userid,String[] keywords);
	/**
	 * 删除订单
	 * @param params
	 * @return
	 */
	public int deleteBill(Map<String, String> params);
	
	public List getCouponBill(Map<String, String> params);

	public UserAuthorize getUserMoney(Map<String, String> params);

	public List<Map<String, String>> getMustFavourite();
	
	//
	public List<String> getUserFavourite(String user_id,String type);
	/**
	 * 增加喜欢
	 * @param user_id
	 * @param ids
	 * @return
	 */
	public int addFavourite(String user_id, String id,String type);
	/**
	 * 判断某一资源用户是否已经喜欢
	 * @param uid
	 * @return
	 */
	public String getFavourite(String user_id, String id,String type);
	/**
	 * 得到某一批收藏的资源
	 * @param user_id
	 * @param source
	 * @return
	 */
	public List<String> getFavouriteRes(String user_id, String[] source);
	
	/**
	 * 判断某一资源用户是否已经订阅
	 * @param uid
	 * @return
	 */
	public String checkSubscribe(String user_id, String source_id,String type);
	
	public int getCountFavourite(String uid);
	
	public List<Map<String, String>> userEdit(String userid,String state);
	
	public int checkBoundPhone(String phoneNum);
	
	public int checkUserEmail(String userEmail);
	/**
	 * 最近播放
	 * @return
	 */
	public List<Map<String, String>> getRecentPlay(String uid);
	/**
	 * 我的喜欢：单曲（分页）
	 * @return
	 */
	public List getFavoriteTrack(Map params,int offset,int rows);
	/**
	 * 我的喜欢：唱片（分页）
	 * @return
	 */
	public List getFavoriteCatal(Map params,int offset,int rows);
	/**
	 * 我的喜欢：视频（分页）
	 * @return
	 */
	public List getFavoriteVedio(Map params,int offset,int rows);
	
	//获取数量
	public int getFavoriteTrackCount(String uid);
	public int getFavoriteCatalCount(String uid);
	public int getFavoriteVedioCount(String uid);
	
	/**
	 * 取消喜欢
	 * @param params
	 * @param offset
	 * @param rows
	 * @return
	 */
	public void cancleFavorite(String uid,String[] array,String type);
	/**
	 * 取消订阅
	 * @param params
	 * @param offset
	 * @param rows
	 * @return
	 */
	public void cancleSubscribe(String uid,String[] array,String type);
	/**
	 * 增加订阅
	 * @param params
	 * @param offset
	 * @param rows
	 * @return
	 */
	public void addSubscribe(String uid,List<String> array,String type);
	/**
	 * 查询订阅是否含有某一资源
	 * @param params
	 * @param offset
	 * @param rows
	 * @return
	 */
	public List<String> getSubscribe(String uid,String[] array,String type);
	/**
	 * 我的订阅：厂牌
	 * @return
	 */
	public List getSubscribeLabel(Map params,int offset,int rows);
	/**
	 * 我的喜欢：艺术家
	 * @return
	 */
	public List getSubscribeArtist(Map params,int offset,int rows);
	/**
	 * 我的夹子：单曲
	 * @return
	 */
	public List getTrackFolderList(Map params,int offset,int rows);
	/**
	 * 单个夹子中单曲信息
	 * @return
	 */
	public List getTracksOfFolder(Map params,int offset,int rows);
	/**
	 * 我的夹子：唱片
	 * @return
	 */
	public List getCatalFolderList(Map params,int offset,int rows);
	/**
	 * 单个夹子中唱片信息
	 * @return
	 */
	public List getCatalsOfFolder(Map params,int offset,int rows);
	/**
	 * 得到所有的夹子
	 * @param params
	 * @return
	 */
	public List<UserFolder> queryMyFolders(Map params);
	/**
	 * 得到所有的夹子
	 * @param params
	 * @return
	 */
	public List<UserFolder> queryUserFolders(Map params,int offset,int rows);
	/**
	 * 创建夹子
	 * @param params
	 * @return
	 */
	public UserFolder createFavoritesFolder(Map params);
	/**
	 * 修改夹子名称
	 * @param params
	 * @return
	 */
	public int editFavFolderName(Map params);
	/**
	 * 保存夹子的imgurl
	 * @param params
	 * @return
	 */
	public int saveFavFolderImg(Map params);
	/**
	 * 删除夹子
	 * @param params
	 * @return
	 */
	public int delFavoritesFolder(String uid,String[] musicfolder_ids);
	/**
	 * 删除夹子中内容信息
	 * @param params
	 * @return
	 */
	public int delFavoritesSourceOfFolder(String uid,String musicfolder_id,String[] source_ids);
	/**
	 * 资源添加到唱片夹
	 * @param params
	 * @return
	 */
	public int addSourceToFolder(List<String> list,String uid,String musicfolder_id,String[] source_ids);
	/**
	 * 资源添加到唱片夹
	 * @param params
	 * @return
	 */
	public int addSourceToFolder(String uid,String musicfolder_id,String[] source_ids);
	/**
	 * 唱片夹中内容添加到唱片夹
	 * @param params
	 * @return
	 */
	public int addFolderSourceToFolder(String uid,String musicfolder_id,String premusicfolder_id,String type);
	/**
	 * 删除夹子关系
	 * @param params
	 * @return
	 */
	public int delFavoritesFolderRelation(String uid,String[] musicfolder_ids);
	/**
	 * 系统消息
	 * @return
	 */
	public List<UserMessage> getSysMessageList(Map params,int offset,int rows);
	/**
	 * 系统消息数量(未读的)
	 * @return
	 */
	public int getNoReadSysMessage(String uid);
	/**
	 * 系统消息（单个的内容）
	 * @param params
	 * @return
	 */
	public UserMessage getSingleMessage(Map params);
	/**
	 * 删除系统消息（多个）
	 * @param params
	 * @return
	 */
	public int delSysMessage(Map params,String[] ids);
	/**
	 * 得到办理库客会员的所需要的信息
	 * @param params
	 * @return
	 */
	public List<List<Map<String, String>>> getVIPProInfo(Map params);
	
	/***
	 * 收藏的专题
	 * @param params
	 * @return
	 */
	public List<String>  getFavouriteTheme(Map params,int offset,int rows);
}
