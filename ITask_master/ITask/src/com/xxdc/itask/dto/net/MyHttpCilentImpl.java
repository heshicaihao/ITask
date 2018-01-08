package com.xxdc.itask.dto.net;

import java.io.File;
import java.util.Map;

import android.content.Context;
import android.util.Log;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.util.LogUtils;
import com.xxdc.itask.Config;
import com.xxdc.itask.dto.ResponseDTO;
import com.xxdc.itask.dto.response.ArrangTaskToUserResponse;
import com.xxdc.itask.dto.response.CopyOfLoginResponse;
import com.xxdc.itask.dto.response.CreatNotepadResponse;
import com.xxdc.itask.dto.response.FileResponse;
import com.xxdc.itask.dto.response.GroupUsersListResponse;
import com.xxdc.itask.dto.response.HistoryItemDetailResponse;
import com.xxdc.itask.dto.response.HistoryTaskResponse;
import com.xxdc.itask.dto.response.LoginResponse;
import com.xxdc.itask.dto.response.NoteListResponse;
import com.xxdc.itask.dto.response.RemoteResponse;
import com.xxdc.itask.dto.response.Response;
import com.xxdc.itask.dto.response.SubmitFeedbackResponse;
import com.xxdc.itask.dto.response.TaskListResponse;

public class MyHttpCilentImpl implements MyHttpCilent {

	private HttpUtils mHttpUtils;

	private Context mContext;

	public MyHttpCilentImpl(Context context) {
		mHttpUtils = new HttpUtils();
		mContext = context;
	}

	@Override
	public <T> HttpHandler<T> post(String url, RequestParams params, MyRequestCallBack<T> callBack) {
		Log.i("url", "url=" + url);
		return mHttpUtils.send(HttpMethod.POST, url, params, callBack);
	}

	@Override
	public <T> HttpHandler<T> get(String url, RequestParams params, MyRequestCallBack<T> callBack) {
		return mHttpUtils.send(HttpMethod.GET, url, params, callBack);
	}

	@Override
	public void signup(String u, String p, String realName,Callback callBack) {
		RequestParams params = new RequestParams();
		params.addBodyParameter("u", u);
		params.addBodyParameter("p", p);
		params.addBodyParameter("realName", realName);
		post(UrlConsts.COMMIT_USER_SIGNUP, params, new MyRequestCallBack<Response>(mContext, callBack, Response.class));
	}

	@Override
	public void login(String p, String u, Callback callback) {
		Log.i("login", "p=" + p + " u=" + u);
		RequestParams params = new RequestParams();
		params.addBodyParameter("u", u);
		params.addBodyParameter("p", p);
		post(UrlConsts.COMMIT_USER_LOGIN, params, new MyRequestCallBack<CopyOfLoginResponse>(mContext, callback, CopyOfLoginResponse.class));
	}

	@Override
	public void queryTaskList(String updateTime, Callback callback) {
		RequestParams params = new RequestParams();
		if (updateTime == null || updateTime.equals("")) {
			updateTime = String.valueOf(0);
		}
		params.addBodyParameter("updateTime", updateTime);
		params.addBodyParameter("token", Config.token);
		post(UrlConsts.QUERY_TASK_LIST, params, new MyRequestCallBack<TaskListResponse>(mContext, callback, TaskListResponse.class));
	}

	@Override
	public void createTask(String content, String parentTaskId, String voice, String photo, String voiceFile, String photoFile, Callback callback) {
		RequestParams params = new RequestParams();
		params.addBodyParameter("taskName", content);
		if (voice != null) {
			params.addBodyParameter("voice", voice);
		}

		if (photo != null) {
			params.addBodyParameter("photo", photo);
		}

		if (voiceFile != null) {
			params.addBodyParameter("voiceFile", voiceFile);
		}

		if (photoFile != null) {
			params.addBodyParameter("photoFile", photoFile);
		}

		params.addBodyParameter("parentTaskId", parentTaskId);
		params.addBodyParameter("token", Config.token);
		post(UrlConsts.UPLOAD_TASK, params, new MyRequestCallBack<TaskListResponse>(mContext, callback, TaskListResponse.class));
	}

	@Override
	public void uploadFile(File file, Callback callback) {
		RequestParams params = new RequestParams();
		params.addBodyParameter("file", file);
		params.addBodyParameter("token", Config.token);
		post(UrlConsts.UPLOAD_FILE, params, new MyRequestCallBack<FileResponse>(mContext, callback, FileResponse.class));
	}

	@Override
	public void getNotes(String updateTime, int type, Callback callback) {
		RequestParams params = new RequestParams();
		params.addBodyParameter("token", Config.token);
		params.addBodyParameter("updateTime", updateTime);
		params.addBodyParameter("type", type + "");
		post(UrlConsts.GET_USER_NOTES, params, new MyRequestCallBack<NoteListResponse>(mContext, callback, NoteListResponse.class));
	}

	@Override
	public void getHistoryTaskList(String u, String p, String pagesize, String startTime, String endTime, String keyWord, Callback callback) {
		RequestParams params = new RequestParams();
		Log.i("history", "u=" + u + "  p=" + p + "pageSize=" + pagesize + " startTime=" + startTime + " endTime=" + endTime + " keyWord=" + keyWord);
		params.addBodyParameter("u", u);
		params.addBodyParameter("p", p);
		params.addBodyParameter("pageSize", pagesize);
		params.addBodyParameter("startTime", startTime);
		params.addBodyParameter("endTime", endTime);
		params.addBodyParameter("keyWord", keyWord);
		post(UrlConsts.GET_USER_NOTES, params, new MyRequestCallBack<TaskListResponse>(mContext, callback, TaskListResponse.class));

	}

	@Override
	public void getHistoryTaskListTest(String token, String year, String month, String offset, String limit, Map<Long, String> childTasks,
			String parentTaskName, Callback callback) {
		RequestParams params = new RequestParams();
		Log.i("history",
				"token=" + token + " year=" + year + " month=" + month + " offset=" + offset + " limit=" + limit + "chidlTasks=" + childTasks.toString()
						+ "parentTaskName=" + parentTaskName);
		params.addBodyParameter("token", token);
		params.addBodyParameter("year", year);
		params.addBodyParameter("month", month);
		params.addBodyParameter("offset", offset);
		params.addBodyParameter("limit", limit);
		post(UrlConsts.GET_USER_HISTORY_TASK_TEST, params, new MyRequestCallBack<HistoryTaskResponse>(mContext, callback, HistoryTaskResponse.class));
	}

	@Override
	public void getHistoryTaskListItemDetail(String token, String userTaskId, Callback callback) {
		RequestParams params = new RequestParams();
		Log.i("history", "token=" + token + " userTaskId=" + userTaskId);
		params.addBodyParameter("token", token);
		params.addBodyParameter("userTaskId", userTaskId);
		post(UrlConsts.GET_USER_HISTORY_TASK_ITEM_DETAIL, params, new MyRequestCallBack<HistoryItemDetailResponse>(mContext, callback,
				HistoryItemDetailResponse.class));
	}

	@Override
	public void getGroupUsers(Callback callback) {
		RequestParams params = new RequestParams();
		Log.i("getGroupUsers", "token=" + Config.token);
		params.addBodyParameter("token", "NzkyfDZ4NTQ1ZXF2bmZ8YWRtaW4xfHJycg==" /*
																				 * Config
																				 * .
																				 * token
																				 */);
		post(UrlConsts.GET_GROUPUSERS_LIST, params, new MyRequestCallBack<GroupUsersListResponse>(mContext, callback, GroupUsersListResponse.class));
	}

	@Override
	public void findHistoryTaskListItem(String token, String keyword, long startDate, long endDate, int offset, int limit, Callback callback) {
		Log.i("httpClient", "token=" + token + "  keyword=" + keyword + " startDate=" + startDate + "  endDate=" + endDate + "  offset=" + offset + "  limit="
				+ limit);
		RequestParams params = new RequestParams();
		// params.addBodyParameter("u", u);
		params.addBodyParameter("token", token);
		params.addBodyParameter("startDate", startDate + "");
		params.addBodyParameter("endDate", endDate + "");
		params.addBodyParameter("keyword", keyword);
		params.addBodyParameter("offset", offset + "");
		params.addBodyParameter("limit", limit + "");
		post(UrlConsts.FIND_USER_HISTORY_ITEM, params, new MyRequestCallBack<HistoryTaskResponse>(mContext, callback, HistoryTaskResponse.class));
	}

	@Override
	public void submitFeedBack(String token, String taskId, String context, String feeStatus, String type, String imgs, String voices, String attachs,
			Callback callback) {
		Log.i("httpClient", "token=" + token + "  taskId=" + taskId + "  context=" + context + " feeStatus=" + feeStatus + "  type=" + type + "  imgs=" + imgs);
		RequestParams params = new RequestParams();
		// params.addBodyParameter("u", u);
		params.addBodyParameter("token", token);
		params.addBodyParameter("taskId", taskId);
		params.addBodyParameter("context", context);
		params.addBodyParameter("feeStatus", feeStatus);
		params.addBodyParameter("imgs", imgs);
		params.addBodyParameter("voices", voices);
		params.addBodyParameter("attachs,", attachs);
		params.addBodyParameter("type", type);
		post(UrlConsts.SUBMIT_FEEDBACK_URL, params, new MyRequestCallBack<SubmitFeedbackResponse>(mContext, callback, SubmitFeedbackResponse.class));

	}

	@Override
	public void arrangeTaskToUser(String taskId, String userIds, Callback callback) {
		LogUtils.i("taskId----->" + taskId + ",userIds---->" + userIds);
		RequestParams params = new RequestParams();
		params.addBodyParameter("token", Config.token);
		params.addBodyParameter("taskId", taskId);
		params.addBodyParameter("userIds", userIds);
		post(UrlConsts.ARRANGE_TASKTOUSER, params, new MyRequestCallBack<ArrangTaskToUserResponse>(mContext, callback, ArrangTaskToUserResponse.class));
	}

	@Override
	public void getRemotionVersion(Callback callback) {
		RequestParams params = new RequestParams();
		// params.addBodyParameter("token",
		// "MjMwNnxiOGM2YXk3amR1fGFkbWluMXwxMTE=");
		params.addBodyParameter("token", Config.token);
		post(UrlConsts.GET_REMOTING, params, new MyRequestCallBack<RemoteResponse>(mContext, callback, RemoteResponse.class));
	}

	@Override
	public void updateTaskStatus(int status, long userTaskId, Callback callback) {
		LogUtils.i("status:" + status + ", userTaskId:" + userTaskId);
		RequestParams params = new RequestParams();
		params.addBodyParameter("token", Config.token);
		params.addBodyParameter("status", String.valueOf(status));
		params.addBodyParameter("userTaskId", String.valueOf(userTaskId));
		post(UrlConsts.UPDATE_TASK_STATUS, params, new MyRequestCallBack<Response>(mContext, callback, Response.class));
	}

	@Override
	public void createNotepad(String title, String context, int type, String files, String userTaskId, Callback callback) {
		LogUtils.i("title=" + title + ",context=" + context + ",type=" + type + ",files=" + files + ",userTaskId=" + userTaskId);
		RequestParams params = new RequestParams();
		params.addBodyParameter("token", Config.token);
		params.addBodyParameter("userTaskId", userTaskId);
		params.addBodyParameter("title", title);
		params.addBodyParameter("context", context);
		params.addBodyParameter("type", type + "");
		params.addBodyParameter("files", files);
		post(UrlConsts.CREATE_NOTEPAD, params, new MyRequestCallBack<CreatNotepadResponse>(mContext, callback, CreatNotepadResponse.class));
	}

	@Override
	public void addLinkman(String userNickName, Callback callback) {
		RequestParams params = new RequestParams();
		params.addBodyParameter("", "");
		post(UrlConsts.CREATE_LINKMAN, params, new MyRequestCallBack<ResponseDTO>(mContext, callback, ResponseDTO.class));
	}

	@Override
	public void setClock(String userTaskId, String userTaskClock, Callback callback) {
		LogUtils.i("userTaskId:" + userTaskId + ", userTaskClock:" + userTaskClock);
		RequestParams params = new RequestParams();
		params.addBodyParameter("token", Config.token);
		params.addBodyParameter("userTaskId", userTaskId);
		params.addBodyParameter("userTaskClock", userTaskClock);
		post(UrlConsts.SET_CLOCK, params, new MyRequestCallBack<Response>(mContext, callback, Response.class));
	}

}
