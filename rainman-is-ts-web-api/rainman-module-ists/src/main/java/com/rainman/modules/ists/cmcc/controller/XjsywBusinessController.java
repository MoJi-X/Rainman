package com.rainman.modules.ists.cmcc.controller;

import com.rainman.modules.ists.cmcc.config.RainmanCmccProperties;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * Title: XjsywBusinessController
 * Description: 新技术新业务数据获取接口
 * @author 李京泽
 * @date 2021年12月13日
 */
@Api(tags = "新技术新业务数据获取接口")
@RestController
@RequestMapping("/ists.cmccoa/xjsyw")
public class XjsywBusinessController {
	@Autowired
	RainmanCmccProperties rainmanCmccProperties;

	private static final int SUCCESS_STAT = 200;

	@AutoLog(value = "新技术新业务数据查询接口")
	@ApiOperation(value = "新技术新业务数据查询接口", notes = "新技术新业务数据查询接口")
	@PostMapping("/queryXjsywList")
	@ResponseBody
	@ApiImplicitParams({
			@ApiImplicitParam(name = "userInfo", value = "加密用户标识", dataTypeClass = String.class),
			@ApiImplicitParam(name = "name", value = "业务名称", dataTypeClass = String.class),
			@ApiImplicitParam(name = "year", value = "年度", dataTypeClass = String.class),
			@ApiImplicitParam(name = "type", value = "业务类型", dataTypeClass = String.class),
			@ApiImplicitParam(name = "category", value = "业务类别", dataTypeClass = String.class),
			@ApiImplicitParam(name = "page", value = "分页：当前页码", dataTypeClass = String.class),
			@ApiImplicitParam(name = "rows", value = "分页：分页数", dataTypeClass = String.class)
	})
	public String queryXjsywList(@RequestParam(name = "userInfo",required = false) String userInfo,
								 @RequestParam(name = "name",required = false) String name,
								 @RequestParam(name = "year",required = false) String year,
								 @RequestParam(name = "type",required = false) String type,
								 @RequestParam(name = "category",required = false) String category,
								 @RequestParam(name = "page",required = false) String page,
								 @RequestParam(name = "rows",required = false) String rows
								 ){
		Map<String,String> param = new HashMap<String,String>(7);
		if (StringUtils.isNotEmpty(userInfo)){
			param.put("userInfo",userInfo);
		}
		if (StringUtils.isNotEmpty(name)){
			param.put("name",name);
		}
		if (StringUtils.isNotEmpty(year)){
			param.put("year",year);
		}
		if (StringUtils.isNotEmpty(type)){
			param.put("type",type);
		}
		if (StringUtils.isNotEmpty(category)){
			param.put("category",category);
		}
		if (StringUtils.isNotEmpty(page)){
			param.put("page",page);
		}
		if (StringUtils.isNotEmpty(rows)){
			param.put("rows",rows);
		}
		String url = rainmanCmccProperties.getXjsywWeburl() + "/" + rainmanCmccProperties.getXjsywQueryList();
		return doGet(url,param);
	}

	@AutoLog(value = "新技术新业务数据查询接口")
	@ApiOperation(value = "新技术新业务数据查询接口", notes = "新技术新业务数据查询接口")
	@PostMapping("/queryXjsywListCount")
	@ResponseBody
	@ApiImplicitParams({
			@ApiImplicitParam(name = "userInfo", value = "加密用户标识", dataTypeClass = String.class),
			@ApiImplicitParam(name = "name", value = "业务名称", dataTypeClass = String.class),
			@ApiImplicitParam(name = "year", value = "年度", dataTypeClass = String.class),
			@ApiImplicitParam(name = "type", value = "业务类型", dataTypeClass = String.class),
			@ApiImplicitParam(name = "category", value = "业务类别", dataTypeClass = String.class),
	})
	public String queryXjsywListCount(@RequestParam(name = "userInfo",required = false) String userInfo,
									  @RequestParam(name = "name",required = false) String name,
									  @RequestParam(name = "year",required = false) String year,
									  @RequestParam(name = "type",required = false) String type,
									  @RequestParam(name = "category",required = false) String category){
		Map<String,String> param = new HashMap<String,String>(5);
		if (StringUtils.isNotEmpty(userInfo)){
			param.put("userInfo",userInfo);
		}
		if (StringUtils.isNotEmpty(name)){
			param.put("name",name);
		}
		if (StringUtils.isNotEmpty(year)){
			param.put("year",year);
		}
		if (StringUtils.isNotEmpty(type)){
			param.put("type",type);
		}
		if (StringUtils.isNotEmpty(category)){
			param.put("category",category);
		}

		String url = rainmanCmccProperties.getXjsywWeburl() + "/" + rainmanCmccProperties.getXjsywQueryCount();
		return doGet(url,param);
	}

	@AutoLog(value = "获取新技术新业务详情url地址")
	@ApiOperation(value = "详情url地址", notes = "详情url地址")
	@PostMapping("/getXjsywDetailViewUrl")
	@ResponseBody
	public String getXjsywDetailViewUrl(){
		return rainmanCmccProperties.getXjsywWeburl() + "/" + rainmanCmccProperties.getXjsywDetailView();
	}

	private String doGet(String url, Map<String, String> param) {

		// 创建Httpclient对象
		CloseableHttpClient httpclient = HttpClients.createDefault();

		String resultString = "";
		CloseableHttpResponse response = null;
		try {
			// 创建uri
			URIBuilder builder = new URIBuilder(url);
			if (param != null) {
				for (String key : param.keySet()) {
					builder.addParameter(key, param.get(key));
				}
			}
			URI uri = builder.build();

			// 创建http GET请求
			HttpGet httpGet = new HttpGet(uri);

			// 执行请求
			response = httpclient.execute(httpGet);
			// 判断返回状态是否为200
			if (response.getStatusLine().getStatusCode() == SUCCESS_STAT) {
				resultString = EntityUtils.toString(response.getEntity(), "UTF-8");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (response != null) {
					response.close();
				}
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return resultString;
	}
}
