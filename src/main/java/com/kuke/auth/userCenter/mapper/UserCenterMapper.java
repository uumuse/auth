package com.kuke.auth.userCenter.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.kuke.auth.login.bean.User;
import com.kuke.auth.userCenter.bean.Coupon;
import com.kuke.auth.userCenter.bean.UserAuthorize;
import com.kuke.auth.userCenter.bean.UserFolder;
import com.kuke.auth.userCenter.bean.UserMessage;

@Repository
public interface UserCenterMapper {
	
	public List getOrgExpireTime(@Param("orgId") String orgId);
	
	public List getUserBill(@Param("params") Map<String, String> map,
			@Param("offset") int offset, @Param("rows") int rows);

	public int exchangeCoupon(@Param("couponKey") String couponKey,
			@Param("uid") String uid);

	public List getUserCoupon(@Param("params") Map params,
			@Param("offset") int offset, @Param("rows") int rows);

	public Coupon getCouponDetail(@Param("params") Map<String, String> params);

	public int presentCoupon(@Param("params") Map<String, String> params);

	public List getPointsExchangeRecord(@Param("params") Map<String, String> params,@Param("offset") int offset, @Param("rows") int rows);

	public List getBillInf(@Param("params") Map<String, String> params,@Param("offset") int offset, @Param("rows") int rows);
	
	public List getBillInfIOS(@Param("params") Map<String, String> params,@Param("offset") int offset, @Param("rows") int rows);
	
	/**
	 * 得到用户的服务状态
	 * @param params
	 * @return
	 */
	public UserAuthorize getUserAuthorize(@Param("params") Map<String, String> params);
	/**
	 * 查询单曲
	 * @param l_code
	 * @return
	 */
	public Map<String, Object> getItemcodeByLcode(@Param("l_code") String l_code);
	/**
	 * 查询专辑
	 * @param l_code
	 * @return
	 */
	public Map<String, Object> getItemcode(@Param("item_code") String item_code);
	/**
	 * 查询乐谱 
	 * @param l_code
	 * @return
	 */
	public Map<String, Object> getMusicBook(@Param("id") String id);
	

	public List getCouponBill(@Param("params") Map<String, String> params);

	public UserAuthorize getUserMoney(@Param("params") Map<String, String> params);

	public List<Map<String, String>> getMustFavourite();
	
	public List<String> getUserFavourite(@Param("user_id")String user_id,@Param("type")String type);
	/**
	 * 添加喜欢
	 * @param user_id
	 * @param id
	 * @param type
	 * @return
	 */
	public int addFavourite(@Param("user_id")String user_id, @Param("id")String[] id, @Param("type")String type);
	/**
	 * 判断某一资源用户是否喜欢
	 * @param user_id
	 * @param id
	 * @param type
	 * @return
	 */
	public String getFavourite(@Param("user_id")String user_id, @Param("id")String id, @Param("type")String type);
	
	/**
	 * 收藏的专题
	 * @param user_id
	 * @param source
	 * @return
	 */
	public List<String> getFavouriteTheme(@Param("params")Map params,@Param("offset") int offset, @Param("rows") int rows );
	
	/**
	 * 删除收藏的专题
	 * @param user_id
	 * @param source
	 * @return
	 */
	public int delFavouriteTheme(@Param("params")Map params,@Param("source_ids")List source_ids);
	
	/**
	 * 判断某一资源用户是否已经喜欢
	 * @param uid
	 * @return
	 */
	public List<String> getFavouriteRes(@Param("user_id") String user_id,@Param("source") String[] source);
	/**
	 * 判断某一资源用户是否被订阅
	 * @param user_id
	 * @param id
	 * @param type
	 * @return
	 */
	public String checkSubscribe(@Param("user_id")String user_id, @Param("source_id")String source_id,@Param("type")String type);

	public int getCountFavourite(@Param("user_id")String uid);

	public List<Map<String, String>> userEdit(@Param("userid") String userid,@Param("state") String state);
	
	public int checkBoundPhone(@Param("phoneNum")String phoneNum);
	
	public int checkUserEmail(@Param("userEmail")String userEmail);
	/**
	 * 取消订单
	 * @param params
	 * @return
	 */
	public int cancleBill(@Param("params")Map<String, String> params);
	/**
	 * 失效订单
	 * @param params
	 * @return
	 */
	public int invalidBill(@Param("userid")String userid,@Param("keywords")String[] keywords);
	/**
	 * 删除订单
	 * @param params
	 * @return
	 */
	public int deleteBill(@Param("params")Map<String, String> params);
	/**
	 * 最近播放
	 * @return
	 */
	public List<Map<String, String>> getRecentPlay(@Param("user_id")String uid);
	/**
	 * 我的喜欢：单曲
	 * @param params
	 * @param offset
	 * @param rows
	 * @return
	 */
	public List getFavoriteTrack(@Param("params") Map params,@Param("offset") int offset, @Param("rows") int rows);
	
	
	//获取数量
	public int getFavoriteTrackCount(@Param("userId") String userId);
	public int getFavoriteCatalCount(@Param("userId") String userId);
	public int getFavoriteVedioCount(@Param("userId") String userId);
	/**
	 * 我的喜欢：视频
	 * @param params
	 * @param offset
	 * @param rows
	 * @return
	 */
	public List getFavoriteVedio(@Param("params") Map params,@Param("offset") int offset, @Param("rows") int rows);
	/**
	 * 我的喜欢：唱片（专辑）
	 * @param params
	 * @param offset
	 * @param rows
	 * @return
	 */
	public List getFavoriteCatal(@Param("params") Map params,@Param("offset") int offset, @Param("rows") int rows);
	/**
	 * 取消喜欢
	 * @param uid
	 * @param source_id
	 * @return
	 */
	public void cancleFavorite(@Param("uid") String uid,@Param("array") String[] array,@Param("type") String type);
	/**
	 * 取消订阅
	 * @param uid
	 * @param source_id
	 * @return
	 */
	public void cancleSubscribe(@Param("uid") String uid,@Param("array") String[] array,@Param("type") String type);
	/**
	 * 查询订阅是否含有某一资源
	 * @param uid
	 * @param source_id
	 * @return
	 */
	public List<String> getSubscribe(@Param("uid") String uid,@Param("array") String[] array,@Param("type") String type);
	/**
	 * 增加订阅
	 * @param uid
	 * @param source_id
	 * @return
	 */
	public void addSubscribe(@Param("uid") String uid,@Param("array") List<String> array,@Param("type") String type);
	/**
	 * 我的订阅：厂牌
	 * @param params
	 * @param offset
	 * @param rows
	 * @return
	 */
	public List getSubscribeLabel(@Param("params") Map params,@Param("offset") int offset, @Param("rows") int rows);
	/**
	 * 我的订阅：艺术家
	 * @param params
	 * @param offset
	 * @param rows
	 * @return
	 */
	public List getSubscribeArtist(@Param("params") Map params,@Param("offset") int offset, @Param("rows") int rows);
	/**
	 * 我的夹子：单曲项
	 * @param params
	 * @param offset
	 * @param rows
	 * @return
	 */
	public List getTrackFolderList(@Param("params") Map params,@Param("offset") int offset, @Param("rows") int rows);
	/**
	 * 单个夹子中source_id
	 * @param params
	 * @param offset
	 * @param rows
	 * @return
	 */
	public List<String> getSourceOfFolder(@Param("params") Map params);
	/**
	 * 单个夹子中source_id,夹子type
	 * @param params
	 * @param offset
	 * @param rows
	 * @return
	 */
	public List<Map<String, Object>> getSourTypeOfFolder(@Param("params") Map params);
	/**
	 * 单个夹子中单曲信息
	 * @param params
	 * @param offset
	 * @param rows
	 * @return
	 */
	public List getTracksOfFolder(@Param("params") Map params,@Param("offset") int offset, @Param("rows") int rows);
	/**
	 * 我的夹子：唱片项
	 * @param params
	 * @param offset
	 * @param rows
	 * @return
	 */
	public List getCatalFolderList(@Param("params") Map params,@Param("offset") int offset, @Param("rows") int rows);
	/**
	 * 单个夹子中唱片信息
	 * @param params
	 * @param offset
	 * @param rows
	 * @return
	 */
	public List getCatalsOfFolder(@Param("params") Map params,@Param("offset") int offset, @Param("rows") int rows);
	/**
	 * 得到所有的夹子
	 * @param userEmail
	 * @return
	 */
	public List<UserFolder> queryMyFolders(@Param("params") Map params);
	/**
	 * 得到所有的夹子
	 * @param userEmail
	 * @return
	 */
	public List<UserFolder> queryUserFolders(@Param("params") Map params,@Param("offset") int offset, @Param("rows") int rows);
	/**
	 * 创建夹子
	 * @param userEmail
	 * @return
	 */
	public int createFavoritesFolder(UserFolder userFolder);
	/**
	 * 修改夹子名称
	 * @param userEmail
	 * @return
	 */
	public int editFavFolderName(@Param("params") Map params);
	/**
	 * 保存夹子的imgurl
	 * @param params
	 * @return
	 */
	public int saveFavFolderImg(@Param("params") Map params);
	/**
	 * 删除夹子
	 * @param userEmail
	 * @return
	 */
	public int delFavoritesFolder(@Param("uid") String uid,@Param("musicfolder_ids") String[] musicfolder_ids);
	/**
	 * 删除夹子关系
	 * @param userEmail
	 * @return
	 */
	public int delFavoritesFolderRelation(@Param("uid") String uid,@Param("musicfolder_ids") String[] musicfolder_ids);
	/**
	 * 删除夹子中内容信息
	 * @param userEmail
	 * @return
	 */
	public int delFavoritesSourceOfFolder(@Param("uid") String uid,@Param("musicfolder_id") String musicfolder_id,@Param("source_ids") String[] source_ids);
	/**
	 * 添加到唱片夹
	 * @param userEmail
	 * @return
	 */
	public int addSourceToFolder(@Param("uid") String uid,@Param("musicfolder_id") String musicfolder_id,@Param("source_ids") String[] source_ids);
	/**
	 * 系统消息
	 * @param params
	 * @param offset
	 * @param rows
	 * @return
	 */
	public List<UserMessage> getSysMessageList(@Param("params") Map params,@Param("offset") int offset, @Param("rows") int rows);
	/**
	 * 系统消息数量(未读的)
	 * @param params
	 * @param offset
	 * @param rows
	 * @return
	 */
	public int getNoReadSysMessage(@Param("uid")String uid);
	/**
	 * 系统消息（单个的内容）
	 * @param params
	 * @return
	 */
	public UserMessage getSingleMessage(@Param("params") Map params);
	/**
	 * 删除系统消息（多个）
	 * @param params
	 * @return
	 */
	public int delSysMessage(@Param("params") Map params,@Param("ids") String[] ids);
	/**
	 * 得到办理库客会员的所需要的信息
	 * @param params
	 * @return
	 */
	public List getVIPProInfo(@Param("params")Map params,@Param("channelIDs") String[] channelIDs);
	
	
	public String getNoOffSourceItem(@Param("item_codes") String[] item_codes);
	public String getNoOffFolderLcode(@Param("l_codes") String[] l_codes);
}
