import { getAction, deleteAction, putAction, postAction, httpAction } from '@/api/manage'
import Vue from 'vue'
import {UI_CACHE_DB_DICT_DATA } from "@/store/mutation-types"

//角色管理
const addRole = (params)=>postAction("/sys/role/add",params);
const editRole = (params)=>putAction("/sys/role/edit",params);
const checkRoleCode = (params)=>getAction("/sys/role/checkRoleCode",params);
const queryall = (params)=>getAction("/sys/role/queryall",params);

//用户管理
const addUser = (params)=>postAction("/sys/user/add",params);
const editUser = (params)=>putAction("/sys/user/edit",params);
const queryUserRole = (params)=>getAction("/sys/user/queryUserRole",params);
const getUserList = (params)=>getAction("/sys/user/list",params);
const frozenBatch = (params)=>putAction("/sys/user/frozenBatch",params);
//验证用户是否存在
const checkOnlyUser = (params)=>getAction("/sys/user/checkOnlyUser",params);
//改变密码
const changePassword = (params)=>putAction("/sys/user/changePassword",params);

//权限管理
const addPermission= (params)=>postAction("/sys/permission/add",params);
const editPermission= (params)=>putAction("/sys/permission/edit",params);
const getPermissionList = (params)=>getAction("/sys/permission/list",params);
const getSystemMenuList = (params)=>getAction("/sys/permission/getSystemMenuList",params);
const getSystemSubmenu = (params)=>getAction("/sys/permission/getSystemSubmenu",params);
const getSystemSubmenuBatch = (params) => getAction('/sys/permission/getSystemSubmenuBatch', params)
const queryTreeList = (params)=>getAction("/sys/permission/queryTreeList",params);
const queryTreeListForRole = (params)=>getAction("/sys/role/queryTreeList",params);
const queryListAsync = (params)=>getAction("/sys/permission/queryListAsync",params);
const queryRolePermission = (params)=>getAction("/sys/permission/queryRolePermission",params);
const saveRolePermission = (params)=>postAction("/sys/permission/saveRolePermission",params);
const queryPermissionsByUser = ()=>getAction("/sys/permission/getUserPermissionByToken");
const loadAllRoleIds = (params)=>getAction("/sys/permission/loadAllRoleIds",params);
const getPermissionRuleList = (params)=>getAction("/sys/permission/getPermRuleListByPermId",params);
const queryPermissionRule = (params)=>getAction("/sys/permission/queryPermissionRule",params);

// 部门管理
const queryDepartTreeList = (params)=>getAction("/sys/sysDepart/queryTreeList",params);
const queryDepartTreeSync = (params)=>getAction("/sys/sysDepart/queryDepartTreeSync",params);
const queryIdTree = (params)=>getAction("/sys/sysDepart/queryIdTree",params);
const queryParentName   = (params)=>getAction("/sys/sysDepart/queryParentName",params);
const searchByKeywords   = (params)=>getAction("/sys/sysDepart/searchBy",params);
const deleteByDepartId   = (params)=>deleteAction("/sys/sysDepart/delete",params);

//二级部门管理
const queryDepartPermission = (params)=>getAction("/sys/permission/queryDepartPermission",params);
const saveDepartPermission = (params)=>postAction("/sys/permission/saveDepartPermission",params);
const queryTreeListForDeptRole = (params)=>getAction("/sys/sysDepartPermission/queryTreeListForDeptRole",params);
const queryDeptRolePermission = (params)=>getAction("/sys/sysDepartPermission/queryDeptRolePermission",params);
const saveDeptRolePermission = (params)=>postAction("/sys/sysDepartPermission/saveDeptRolePermission",params);
const queryMyDepartTreeList = (params)=>getAction("/sys/sysDepart/queryMyDeptTreeList",params);

//日志管理
const deleteLog = (params)=>deleteAction("/sys/log/delete",params);
const deleteLogList = (params)=>deleteAction("/sys/log/deleteBatch",params);

//数据字典
const addDict = (params)=>postAction("/sys/dict/add",params);
const editDict = (params)=>putAction("/sys/dict/edit",params);
const treeList = (params)=>getAction("/sys/dict/treeList",params);
const addDictItem = (params)=>postAction("/sys/dictItem/add",params);
const editDictItem = (params)=>putAction("/sys/dictItem/edit",params);

//字典标签专用（通过code获取字典数组）
export const ajaxGetDictItems = (code, params)=>getAction(`/sys/dict/getDictItems/${code}`,params);
//从缓存中获取字典配置
function getDictItemsFromCache(dictCode) {
  if (Vue.ls.get(UI_CACHE_DB_DICT_DATA) && Vue.ls.get(UI_CACHE_DB_DICT_DATA)[dictCode]) {
    let dictItems = Vue.ls.get(UI_CACHE_DB_DICT_DATA)[dictCode];
    //console.log("-----------getDictItemsFromCache----------dictCode="+dictCode+"---- dictItems=",dictItems)
    return dictItems;
  }
}

//系统通告
const doReleaseData = (params)=>getAction("/sys/annountCement/doReleaseData",params);
const doReovkeData = (params)=>getAction("/sys/annountCement/doReovkeData",params);
//获取系统访问量
const getLoginfo = (params)=>getAction("/sys/loginfo",params);
const getVisitInfo = (params)=>getAction("/sys/visitInfo",params);

// 根据部门主键查询用户信息
const queryUserByDepId = (params)=>getAction("/sys/user/queryUserByDepId",params);

// 重复校验
const duplicateCheck = (params)=>getAction("/sys/duplicate/check",params);
// 加载分类字典
const loadCategoryData = (params)=>getAction("/sys/category/loadAllData",params);
const checkRuleByCode = (params) => getAction('/sys/checkRule/checkByCode', params)
//加载我的通告信息
const getUserNoticeInfo= (params)=>getAction("/sys/sysAnnouncementSend/getMyAnnouncementSend",params);
const getTransitURL = url => `/sys/common/transitRESTful?url=${encodeURIComponent(url)}`
// 中转HTTP请求
export const transitRESTful = {
  get: (url, parameter) => getAction(getTransitURL(url), parameter),
  post: (url, parameter) => postAction(getTransitURL(url), parameter),
  put: (url, parameter) => putAction(getTransitURL(url), parameter),
  http: (url, parameter) => httpAction(getTransitURL(url), parameter),
}
//首页  获取首页信息
const getHomeInfo = (params)=>getAction("/ists.home/listRoleName",params)
//同步系统公共创建草稿请求
const createDraft = (params)=>postAction("/ists.wf/launch?wfCode="+params.wfCode)
//系统详情
const getSysDetailInfo = (params)=>getAction("/ists.wf/getByWfInstanceId",params);
//资产信息分页查询
const getAssetsList = (params)=>postAction("/ists.bd/bdAssetsWfInstance/pageList?pageNo="+params.pageNo+"&pageSize="+params.pageSize,params)
//资产信息新增
const addAssetNew = (params)=>postAction("/ists.bd/bdAssetsWfInstance/add",params)
//资产信息修改
const updateAsset = (params)=>postAction("/ists.bd/bdAssets/edit",params)
//资产信息删除
const deleteAsset = (params)=>deleteAction("/ists.bd/bdAssets/delete",params)
//删除发起中流程数据
const deleteWfData = (params)=>deleteAction("/ists.wf/removeByWfInstanceId?wfInstanceId="+params.wfInstanceId)
//系统定级分页查询
const getSystemList = (params)=>postAction("/ists.bd/bdProjectWfInstance/pageForProjectRank",params);
//其他分页列表查询
const getOtherList = (params)=>postAction("/ists.bd/bdProjectWfInstance/pageByWfCode",params)
//系统定级 系统录入列表查询
const getSystemRecordList = (params)=>getAction("/ists.bd/bdProjectWfInstance/listByWfInstanceId",params)
//系统定级  系统录入(单个录入)
const sysAddSinger = (params)=>postAction("/ists.bd/bdProjectWfInstance/add",params);
//系统录入(修改)
const sysUpdateSinger = (params)=>postAction("/ists.bd/bdProjectWfInstance/updateByBdProjectWfInstanceId",params)
//系统录入(查询单个)
const sysDetailSinger = (params)=>getAction("/ists.bd/bdProjectWfInstance/getByBdProjectWfInstanceId",params)
//系统录入(删除单个1)
const sysDeleteSinger = (params)=>deleteAction("/ists.bd/bdProjectWfInstance/removeByBdProjectWfInstanceId",params)
//系统录入(删除单个2)
const sysDeleteSingerBp = (params)=>deleteAction("/ists.bd/bdProject/removeByBdProjectWfInstanceId?bdProjectId="+params.bdProjectWfInstanceId,params)
//项目总数
const projectTotal = (params)=>getAction("/ists.bd/bdProject/countByInitiatorCode",params)
//消息已阅读
const messageRead = (params)=>putAction("/sys/sysAnnouncementSend/editByAnntIdAndUserId",params)

//获取流程进度
const getStepInfo = (params)=>getAction("/ists.wf/listNodeInfo",params)
//获取流程意见
const getStepOpnion = (params)=>getAction("/ists.wf/listComment",params)
//数据暂存
const getSaveData = (params)=>putAction("/ists.wf/temporaryStorageByWfInstanceId?wfInstanceId="+params.wfInstanceId,params)
//流程处理
const getNextStep = (params)=>postAction("/ists.wf/submit",params)
//获取可选的系统（用于其他模块选择系统时的分页查询）
const getSelectSysList = (params)=>postAction("/ists.bd/bdProject/pageByWfCode",params)
//点击选择的系统查询
const getSelectSysDetail = (params)=>getAction("/ists.bd/bdProject/getByBdProjectId",params)
//督查报表列表查询接口
const getInspectorList = (params)=>postAction("/ists.bd/bdProject/pageByWfCodeAndWfNodeCode",params)
//双新评估列表分页查询接口
const doubleNewListData = (params)=>postAction("/ists.cmccoa/xjsyw/queryXjsywList?category="+params.category+"&page="+params.current+"&rows="+params.pageSize+"&year="+params.year+"&name="+params.name+"&type="+params.type,params)
//双新评估列表总数查询接口
const doubleTotalData = (params)=>postAction("/ists.cmccoa/xjsyw/queryXjsywListCount?category="+params.category+"&year="+params.year+"&name="+params.name+"&type="+params.type,params)
// 双新评估公共跳转链接
const doubleNewCommonUrl = ()=>postAction("/ists.cmccoa/xjsyw/getXjsywDetailViewUrl")
//个人信息查询
const getUserInfo = (params)=>getAction("/ists.home/loginUserInfo",params)
//项目清单库分页查询
const productList = (params)=>postAction("/ists.bd/bdProject/pageByWfCodeAndWfInstanceStateCode",params)
//项目清单库详情左侧树子节点
const productTreeNode = (params)=>getAction("/ists.bd/bdProjectWfInstance/listByWfCodeAndBdProjectId",params)
//暂存时保存选择系统的接口
const saveSysData = (params)=>postAction("/ists.bd/bdProjectWfInstance/saveOrUpdate?bdProjectId="+params.bdProjectId+"&wfInstanceId="+params.wfInstanceId,params)
//特需流程修改
const specialUpdate = (params)=>postAction("ists.bd/bdProject/updateById",params)
//风险点分页查询
const getRiskList = (params)=>postAction("/ists.bd/bdStrPointsWfInstance/pageList?pageNo="+params.pageNo+"&pageSize="+params.pageSize,params)
//风险点添加接口
const addRiskData = (params)=>postAction("/ists.bd/bdStrPointsWfInstance/add",params)
//风险点修改接口
const updateRiskData = (params)=>postAction("/ists.bd/bdStrPoints/edit?wfInstanceId="+params.wfInstanceId,params.bdStrPointsDto)
//风险点删除接口
const deleteRiskData = (params)=>deleteAction("/ists.bd/bdStrPoints/delete",params)
//总览图查询系统总数接口
const getModuleTotal = (params)=>getAction("/ists.wf/countByWfCodeAndBdFileFlagCode",params)
//总览图查询纳入三同步安全管理总数
const getSafeManageTotal = (params)=>getAction("/ists.wf/countByWf",params)
//总览图查询今年纳入总数
const getYearTotal = (params)=>getAction("/ists.wf/countByWfThisYear",params)
//总览图查询当月纳入总数
const getMonthTotal = (params)=>getAction("/ists.wf/countByWfThisMonth",params)
//总览图查询范围月份纳入总数
const getMonthRangeTotal = (params)=>getAction("/ists.wf/listCountByWfThisYearRangeMonth",params)
//总览图右侧查询范围
const getRightTotal = (params)=>getAction("/ists.wf/countByBdProject",params)
//总览图系统定级饼图查询
const getSysPie = (params)=>getAction("/ists.bd/bdProject/listBySystemGradingCode",params)
//总览图三同步部门开展情况
const getExtendData = (params)=>getAction("/ists.wf/listCountByOrgForWf",params)

//首页待办分页查询接口
const todoListData = (params)=>postAction("/ists.wf/pageByToDo?pageNo="+params.pageNo+"&pageSize="+params.pageSize,params)
//首页已办分页查询接口
const doneListData = (params)=>postAction("/ists.wf/pageByDone?pageNo="+params.pageNo+"&pageSize="+params.pageSize,params)
//首頁消息分页接口
const messageListData = (params)=>getAction("/sys/sysAnnouncementSend/getMyAnnouncementSend?pageNo="+params.pageNo+"&pageSize="+params.pageSize,params)
//移动OA跳回
const cmccOaToken = (params)=>postAction("/sys/getInfo?userInfo="+params.UserInfo+"&wfInstanceId="+params.wfInstanceId,params)
//联通跳回
const cmccLTToken = (params)=>postAction("/jeecg-boot/sys/getInfoByToken?access_token="+params.access_token,params)
//三同步统计1
const tryxTotal = (params)=>getAction("/ists.wf/countByWfCodeNqIneedCheckForBdProject",params)
//三同步统计2
const wysTotal = (params)=>getAction("/ists.wf/countByWfCodeNqAcceptForBdProject",params)
//三同步统计3
const trysTotal = (params)=>getAction("/ists.wf/countByWfCodeEqIneedCheckForBdProject",params)
//三同步统计4
const tryxxtTotal = (params)=>getAction("/ists.wf/countByWfCodeInAllForBdProject",params)
export {
  // imgView,
  // doMian,
  addRole,
  editRole,
  checkRoleCode,
  addUser,
  editUser,
  queryUserRole,
  getUserList,
  queryall,
  frozenBatch,
  checkOnlyUser,
  changePassword,
  getPermissionList,
  addPermission,
  editPermission,
  queryTreeList,
  queryListAsync,
  queryRolePermission,
  saveRolePermission,
  queryPermissionsByUser,
  loadAllRoleIds,
  getPermissionRuleList,
  queryPermissionRule,
  queryDepartTreeList,
  queryDepartTreeSync,
  queryIdTree,
  queryParentName,
  searchByKeywords,
  deleteByDepartId,
  deleteLog,
  deleteLogList,
  addDict,
  editDict,
  treeList,
  addDictItem,
  editDictItem,
  doReleaseData,
  doReovkeData,
  getLoginfo,
  getVisitInfo,
  queryUserByDepId,
  duplicateCheck,
  queryTreeListForRole,
  getSystemMenuList,
  getSystemSubmenu,
  getSystemSubmenuBatch,
  loadCategoryData,
  checkRuleByCode,
  queryDepartPermission,
  saveDepartPermission,
  queryTreeListForDeptRole,
  queryDeptRolePermission,
  saveDeptRolePermission,
  queryMyDepartTreeList,
  getUserNoticeInfo,
  getDictItemsFromCache,
  getSysDetailInfo,
  getAssetsList,
  addAssetNew,
  updateAsset,
  deleteAsset,
  createDraft,
  getSystemList,
  getOtherList,
  getSystemRecordList,
  sysAddSinger,
  sysUpdateSinger,
  sysDetailSinger,
  sysDeleteSinger,
  getStepInfo,
  getStepOpnion,
  getNextStep,
  getHomeInfo,
  getSelectSysList,
  getSelectSysDetail,
  getInspectorList,
  getUserInfo,
  productList,
  productTreeNode,
  specialUpdate,
  getModuleTotal,
  getRiskList,
  addRiskData,
  updateRiskData,
  deleteRiskData,
  getSafeManageTotal,
  getYearTotal,
  getMonthTotal,
  getMonthRangeTotal,
  getRightTotal,
  getSysPie,
  getExtendData,
  todoListData,
  doneListData,
  messageListData,
  doubleNewListData,
  doubleTotalData,
  doubleNewCommonUrl,
  cmccOaToken,
  cmccLTToken,
  tryxTotal,
  wysTotal,
  trysTotal,
  tryxxtTotal,
  sysDeleteSingerBp,
  deleteWfData,
  projectTotal,
  getSaveData,
  saveSysData,
  messageRead
}



