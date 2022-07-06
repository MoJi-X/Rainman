package com.rainman.modules.ists.cmcc.controller;

import com.rainman.modules.ists.cmcc.dto.UniteWork;
import com.rainman.modules.ists.cmcc.dto.UniteWorkItem;
import com.rainman.modules.ists.cmcc.util.UniteWorkServiceClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * Title: OaExampleController
 * Description: OA发送示例
 * @author 李京泽
 * @date 2021年12月7日
 */
@RestController
@RequestMapping("/ists.cmccoa/oaExample")
public class OaExampleController{

	@RequestMapping("/sentWrok")
	public String sentWrok(){
		/**
		 * <UniteWork>
		 *   <UniteWorkItems>
		 *     <UniteWorkItem>
		 *       <OptType>1</OptType>
		 *       <UniteWorkID>402809817d8e81f4017d8e90d34d000f507923</UniteWorkID>
		 *       <UniteWorkCurUserID>ADMIN</UniteWorkCurUserID>
		 *       <UniteWorkAccountid>ADMIN</UniteWorkAccountid>
		 *       <UniteWorkTitle>2021年12月06日 15时06分【评估计划审批流程】（admin）</UniteWorkTitle>
		 *       <UniteWorkCreateTime>2021-12-06 15:06:28</UniteWorkCreateTime>
		 *       <UniteWorkUserDealTime>2021-12-06 15:06:52</UniteWorkUserDealTime>
		 *       <UniteWorkStateUpdateTime>2021-12-06 15:06:52</UniteWorkStateUpdateTime>
		 *       <UniteWorkCompanyID>00360078001500000000</UniteWorkCompanyID>
		 *       <UniteWorkURL>http://10.154.146.69:8080/hncmccisms/cas4ALogin.do?oaCheck&amp;isUniteWork=1&amp;taskId=507923&amp;batchNumber=20211206150628195&amp;processInstanceId=507839</UniteWorkURL>
		 *       <UniteWorkType>0</UniteWorkType>
		 *       <UniteWorkState>0</UniteWorkState>
		 *       <UniteWorkAppExampleKey>20211206150628195</UniteWorkAppExampleKey>
		 *       <UniteWorkAppExampleType>评估计划审批</UniteWorkAppExampleType>
		 *       <UniteWorkAppID>xxaqglzcpt</UniteWorkAppID>
		 *       <UniteWorkHuanJi>1</UniteWorkHuanJi>
		 *       <UniteWorkCurStepName>部门领导审批</UniteWorkCurStepName>
		 *       <UniteWorkPreUserName>admin</UniteWorkPreUserName>
		 *       <UniteWorkField6>20211206150628195507923</UniteWorkField6>
		 *     </UniteWorkItem>
		 *   </UniteWorkItems>
		 * </UniteWork>
		 */

		//流程集合
		List<UniteWorkItem> items = new ArrayList<UniteWorkItem>();
		//单个流程
		UniteWorkItem item = new UniteWorkItem();
		//UniteWorkItem具体含义可参考规范《湖南移动统一信息平台sso登录及统一待办技术规范说明书（最新）.docx》

		item.setOptType("1");
		item.setUniteWorkId("402809817d8e81f4017d8e90d34d000f507923");
		item.setUniteWorkCurUserId("ADMIN");
		item.setUniteWorkAccountId("ADMIN");
		item.setUniteWorkTitle("2021年12月06日 15时06分【评估计划审批流程】（admin）");
		try {
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			item.setUniteWorkCreateTime(df.parse("2021-12-06 15:06:28"));
			item.setUniteWorkUserDealTime(df.parse("2021-12-06 15:06:52"));
			item.setUniteWorkStateUpdateTime(df.parse("2021-12-06 15:06:52"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		item.setUniteWorkCompanyId("00360078001500000000");
		item.setUniteWorkUrl("http://10.154.146.69:8080/hncmccisms/cas4ALogin.do?oaCheck&isUniteWork=1&taskId=507923&batchNumber=20211206150628195&processInstanceId=507839");
		//0:办件 1:阅件
		item.setUniteWorkType(0);
		//0:待办 2:已办 4:待阅 5:已阅 7:作废
		item.setUniteWorkState(0);
		item.setUniteWorkAppExampleKey("20211206150628195");
		item.setUniteWorkAppExampleType("评估计划审批");
		/**item.setUniteWorkAppID(UniteWorkServiceClient.getAppCode());**/
		//0:空 1:一般 2:紧急 3:特急
		item.setUniteWorkHuanJi(1);
		item.setUniteWorkCurStepName("部门领导审批");
		item.setUniteWorkPreUserName("admin");
		item.setUniteWorkField6("20211206150628195507923");


		items.add(item);
		UniteWork uniteWork = new UniteWork();
		uniteWork.setUniteWorkItems(items);
		UniteWorkServiceClient.requestWebService(uniteWork);
		return "success";
	}
}
