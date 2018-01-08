package com.xxdc.itask.dto.net;

import com.xxdc.itask.Config;

public class UrlConsts {
	
	/**
	 * 登录
	 */
	public static final String COMMIT_USER_LOGIN = Config.SERVER + "/Task/rest/ws/user/login";

	/**
	 * 注册
	 */
	public static final String COMMIT_USER_SIGNUP = Config.SERVER + "/task/rest/ws/user/register";

	/**
	 * 获取事件列表
	 */
	public static final String QUERY_TASK_LIST = Config.SERVER + "/task/rest/ws/task/list";

	/**
	 * 上传事项
	 */
	public static final String UPLOAD_TASK = Config.SERVER + "/Task/rest/ws/user/createUserTask";

	/** 获取记事本内容 */
	public static final String GET_USER_NOTES = Config.SERVER + "/Task/rest/ws/notepad/getNotepad";

	/** 获取联系人信息 */
	public static final String GET_GROUPUSERS_LIST = Config.SERVER + "/Task/rest/ws/groups/groupUsers";

	/**
	 * 获历史任务内容 author:yxb
	 * */
	public static final String GET_USER_HISTORY_TASK = Config.SERVER + "/server_url/task/getHistoryTask";

	/**
	 * 获历史任务内容 author:yxb
	 * */
	public static final String GET_USER_HISTORY_TASK_TEST = Config.SERVER + "/Task/rest/ws/user/getHistoryUserTaskList";

	/**
	 * 获历史单个历史任务的详细内容 author:yxb
	 * */
	public static final String GET_USER_HISTORY_TASK_ITEM_DETAIL = Config.SERVER + "/Task/rest/ws/user/getHistoryUserTaskDetail";
	/**
	 * 根据条件查询历史任务 author:yxb
	 * */
	public static final String FIND_USER_HISTORY_ITEM = Config.SERVER + "/Task/rest/ws/user/findHistoryUserTaskList";
	
	/**
	 * 提交反馈的接口URL author:yxb
	 * */
	public static final String SUBMIT_FEEDBACK_URL = Config.SERVER + "/Task/rest/ws/task/postFeeback";
	
	/**
	 * 上传文件
	 */
	public static final String UPLOAD_FILE = Config.SERVER + "/Task/ws/photo";
	
	/**
	 * 分配给
	 */
	public static final String  ARRANGE_TASKTOUSER= Config.SERVER+"/Task/rest/ws/task/arrangeTaskToUser";
	
	/**
	 * 版本更新
	 */
	public static final String GET_REMOTING = Config.SERVER + "/Task/rest/ws/task/getRemote";
	
	/** 创建记事本 */
	public static final String CREATE_NOTEPAD = Config.SERVER+"/Task/rest/ws/notepad/createNotepad";
	
	/**
	 * 修改事项
	 */
	public static final String UPDATE_TASK_STATUS = Config.SERVER + "/Task/rest/ws/user/setTaskStatus";
	
	/**
	 * 创建联系人
	 */
	public static final String CREATE_LINKMAN = Config.SERVER + "";
	
	/**
	 * 设置闹钟
	 */
	public static final String SET_CLOCK = Config.SERVER + "/Task/rest/ws/user/setTaskClock";
}
