//package com.rainman.modules.ists.bd.controller;
//
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.baomidou.mybatisplus.core.metadata.IPage;
//import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
//import com.rainman.modules.ists.bd.entity.BdFlowSheet;
//import com.rainman.modules.ists.bd.service.IBdFlowSheetService;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import lombok.extern.slf4j.Slf4j;
//import org.jeecg.common.api.vo.Result;
//import org.jeecg.common.aspect.annotation.AutoLog;
//import org.jeecg.common.system.base.controller.JeecgController;
//import org.jeecg.common.system.query.QueryGenerator;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.servlet.ModelAndView;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.util.Arrays;
//
// /**
//  * @Description: bd_flow_sheet
//  * @Author: yao-hai-tao
//  * @Date: 2021-11-23
//  * @Version: V1.0
//  */
//@Api(tags="bd_flow_sheet")
//@RestController
//@RequestMapping("/ists.bd/bdFlowSheet")
//@Slf4j
//public class BdFlowSheetController extends JeecgController<BdFlowSheet, IBdFlowSheetService> {
//	@Autowired
//	private IBdFlowSheetService bdFlowSheetService;
//
//	/**
//	 * 分页列表查询
//	 *
//	 * @param bdFlowSheet
//	 * @param pageNo
//	 * @param pageSize
//	 * @param req
//	 * @return
//	 */
//	@AutoLog(value = "bd_flow_sheet-分页列表查询")
//	@ApiOperation(value="bd_flow_sheet-分页列表查询", notes="bd_flow_sheet-分页列表查询")
//	@GetMapping(value = "/list")
//	public Result<?> queryPageList(BdFlowSheet bdFlowSheet,
//								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
//								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
//								   HttpServletRequest req) {
//		QueryWrapper<BdFlowSheet> queryWrapper = QueryGenerator.initQueryWrapper(bdFlowSheet, req.getParameterMap());
//		Page<BdFlowSheet> page = new Page<BdFlowSheet>(pageNo, pageSize);
//		IPage<BdFlowSheet> pageList = bdFlowSheetService.page(page, queryWrapper);
//		return Result.OK(pageList);
//	}
//
//	/**
//	 *   添加
//	 *
//	 * @param bdFlowSheet
//	 * @return
//	 */
//	@AutoLog(value = "bd_flow_sheet-添加")
//	@ApiOperation(value="bd_flow_sheet-添加", notes="bd_flow_sheet-添加")
//	@PostMapping(value = "/add")
//	public Result<?> add(@RequestBody BdFlowSheet bdFlowSheet) {
//		bdFlowSheetService.save(bdFlowSheet);
//		return Result.OK("添加成功！");
//	}
//
//	/**
//	 *  编辑
//	 *
//	 * @param bdFlowSheet
//	 * @return
//	 */
//	@AutoLog(value = "bd_flow_sheet-编辑")
//	@ApiOperation(value="bd_flow_sheet-编辑", notes="bd_flow_sheet-编辑")
//	@PutMapping(value = "/edit")
//	public Result<?> edit(@RequestBody BdFlowSheet bdFlowSheet) {
//		bdFlowSheetService.updateById(bdFlowSheet);
//		return Result.OK("编辑成功!");
//	}
//
//	/**
//	 *   通过id删除
//	 *
//	 * @param id
//	 * @return
//	 */
//	@AutoLog(value = "bd_flow_sheet-通过id删除")
//	@ApiOperation(value="bd_flow_sheet-通过id删除", notes="bd_flow_sheet-通过id删除")
//	@DeleteMapping(value = "/delete")
//	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
//		bdFlowSheetService.removeById(id);
//		return Result.OK("删除成功!");
//	}
//
//	/**
//	 *  批量删除
//	 *
//	 * @param ids
//	 * @return
//	 */
//	@AutoLog(value = "bd_flow_sheet-批量删除")
//	@ApiOperation(value="bd_flow_sheet-批量删除", notes="bd_flow_sheet-批量删除")
//	@DeleteMapping(value = "/deleteBatch")
//	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
//		this.bdFlowSheetService.removeByIds(Arrays.asList(ids.split(",")));
//		return Result.OK("批量删除成功!");
//	}
//
//	/**
//	 * 通过id查询
//	 *
//	 * @param id
//	 * @return
//	 */
//	@AutoLog(value = "bd_flow_sheet-通过id查询")
//	@ApiOperation(value="bd_flow_sheet-通过id查询", notes="bd_flow_sheet-通过id查询")
//	@GetMapping(value = "/queryById")
//	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
//		BdFlowSheet bdFlowSheet = bdFlowSheetService.getById(id);
//		if(bdFlowSheet==null) {
//			return Result.error("未找到对应数据");
//		}
//		return Result.OK(bdFlowSheet);
//	}
//
//    /**
//    * 导出excel
//    *
//    * @param request
//    * @param bdFlowSheet
//    */
//    @RequestMapping(value = "/exportXls")
//    public ModelAndView exportXls(HttpServletRequest request, BdFlowSheet bdFlowSheet) {
//        return super.exportXls(request, bdFlowSheet, BdFlowSheet.class, "bd_flow_sheet");
//    }
//
//    /**
//      * 通过excel导入数据
//    *
//    * @param request
//    * @param response
//    * @return
//    */
//    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
//    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
//        return super.importExcel(request, response, BdFlowSheet.class);
//    }
//
//}
