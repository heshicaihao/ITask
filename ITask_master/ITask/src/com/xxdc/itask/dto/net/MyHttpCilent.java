package com.xxdc.itask.dto.net;

import java.io.File;
import java.util.Map;

import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.RequestParams;

public interface MyHttpCilent {

	/**
	 * 所有的其他的api接口都会调用的post请求接口
	 * 
	 * @param context
	 * @param callBack
	 * @return
	 */

	<T> HttpHandler<T> post(String url, RequestParams params, MyRequestCallBack<T> callBack);

	<T> HttpHandler<T> get(String url, RequestParams params, MyRequestCallBack<T> callBack);

	/**
	 * 注册
	 * 
	 * @param p
	 * @param u
	 * @param callback
	 */
	void signup(String p, String u,String realName, Callback callback);

	/**
	 * 登录
	 * 
	 * @param p
	 * @param u
	 * @param callback
	 */
	void login(String p, String u, Callback callback);

	/**
	 * 查询事项列表
	 * 
	 * @param updateTime
	 * @param callback
	 */
	void queryTaskList(String updateTime, Callback callback);

	/**
	 * 上传事项
	 * 
	 * @param content
	 * @param voice
	 * @param photo
	 */
	void createTask(String content, String parentTaskId, String voice, String photo, String voiceFile, String photoFile,Callback callback);

	void uploadFile(File file, Callback callback);

	void getNotes(String updateTime, int type, Callback callback);

	void getGroupUsers(Callback callback);

	/**
	 * 获取历史任务列表
	 * 
	 * @param u
	 * @param p
	 * @param pagesize
	 */
	void getHistoryTaskList(String u, String p, String pagesize, String startTime, String endTime, String keyWord, Callback callback);

	/**
	 * 获取历史任务列表测试
	 * 
	 * @param u
	 * @param p
	 * @param pagesize
	 */
	void getHistoryTaskListTest(String token, String year, String month, String offset, String limit, Map<Long, String> childTasks, String parentTaskName,
			Callback callback);

	/**
	 * 获取单个历史任务详细
	 * 
	 * @param u
	 * @param p
	 * @param pagesize
	 */
	void getHistoryTaskListItemDetail(String token, String userTaskId, Callback callback);

	/**
	 * 根据时间查询历史任务
	 * 
	 * @param u
	 * @param p
	 * @param pagesize
	 */
	void findHistoryTaskListItem(String token, String keyword, long startDate, long endDate, int offset, int limit, Callback callback);

	/**
	 * 提交反馈的接口
	 * 
	 * @param u
	 * @param p
	 * @param pagesize
	 */
	void submitFeedBack(String token, String taskId, String context, String feeStatus, String type, String imgs, String voices, String attachs,
			Callback callback);

	/**
	 * 分配给接口
	 * 
	 * @param taskId
	 * @param userIds
	 */
	void arrangeTaskToUser(String taskId, String userIds, Callback callback);
	
	/**
	 * 获取app最新版本号
	 * @param callback
	 */
	void getRemotionVersion(Callback callback);

	
	/**
	 * 更新task状态
	 * @param status 0：已完成；1：待完成；2：已完成
	 * @param userTaskId 
	 */
	void updateTaskStatus(int status, long userTaskId, Callback callback);
	
	void createNotepad(String title, String context, int type, String files, String userTaskId, Callback callback);
	
	/**
	 * 添加联系人
	 */
	void addLinkman(String userNickName,  Callback callback);
	
	/**
	 * 设置闹钟
	 * @param userTaskId
	 * @param userTaskClock
	 */
	void setClock(String userTaskId, String userTaskClock, Callback callback);
}
