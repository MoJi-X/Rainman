import { UserLayout, TabLayout, RouteView, BlankLayout, PageView } from '@/components/layouts'

/**
 * 走菜单，走权限控制
 * @type {[null,null]}
 */
export const asyncRouterMap = [

  {
    path: '/',
    name: 'main',
    component: TabLayout,
    meta: { title: '首页' },
    redirect: '/dashboard/analysis',
    children: [
      //统计页
      {
        path: '/statistical',
        name: 'statistical',
        redirect: '/statistical/index',
        component: RouteView,
        meta: { title: '总览图', icon: 'dashboard', permission: [ 'statistical' ] },
        children: [
          {
            path: '/statistical/index',
            name: 'statisticalIndex',
            component: () => import('@/views/product/Statistical.vue'),
            meta: { title: '总览图', permission: [ 'statisticalIndex' ] }
          }
        ]
      },
      // index
      {
          path: '/dashboard',
          name: 'dashboard',
          redirect: '/dashboard/analysis',
          component: RouteView,
          meta: { title: '首页', icon: 'dashboard', permission: [ 'dashboard' ] },
          children: [
            {
              path: '/dashboard/analysis',
              name: 'Analysis',
              component: () => import('@/views/dashboard/Analysis.vue'),
              meta: { title: '首页', permission: [ 'dashboard' ] }
            },
          ]
      },
      //同步规划
      {
        path: '/planning',
        name: 'planning',
        component: RouteView,
        redirect: '/planning/syslist',
        meta: { title: '同步规划', icon: 'dashboard', permission: [ 'planning' ] },
        children: [
          {
            path: '/planning/syslist',
            name: 'syslist',
            component: () => import('@/views/product/planning/Syslist.vue'),
            meta: { title: '系统定级', permission: [ 'syslist' ] }
          },
          {
            path: '/planning/sysDetail',
            name: 'sysDetail',
            component: () => import('@/views/product/details/SysDetail.vue'),
            meta: { title: '系统定级流程发起', permission: [ 'sysDetail' ],hiddenHeaderContent:true}
          },
          {
            path: '/planning/reviewlist',
            name: 'reviewlist',
            component: () => import('@/views/product/planning/Reviewlist.vue'),
            meta: { title: '立项评审', permission: [ 'reviewlist' ] }
          },
          {
            path: '/planning/reviewDetail',
            name: 'reviewDetail',
            component: () => import('@/views/product/details/ReviewDetail.vue'),
            meta: { title: '立项评审流程发起', permission: [ 'reviewDetail' ],hiddenHeaderContent:true}
          },
          {
            path: '/planning/speciallist',
            name: 'speciallist',
            component: () => import('@/views/product/planning/Speciallist.vue'),
            meta: { title: '特需流程', permission: [ 'speciallist' ] }
          },
          {
            path: '/planning/specialDetail',
            name: 'specialDetail',
            component: () => import('@/views/product/details/SpecialDetail.vue'),
            meta: { title: '特需流程发起', permission: [ 'specialDetail' ],hiddenHeaderContent:true}
          },
        ]
      },
      //同步建设
      {
        path: '/construction',
        name: 'construction',
        component: RouteView,
        meta: { title: '同步建设', icon: 'dashboard', permission: [ 'construction' ] },
        children: [
          {
            path: '/construction/constrlist',
            name: 'constrlist',
            component: () => import('@/views/product/construction/Constrlist.vue'),
            meta: { title: '建设入网', permission: [ 'constrlist' ] }
          },
          {
            path: '/construction/constrDetail',
            name: 'constrDetail',
            component: () => import('@/views/product/details/ConstrDetail.vue'),
            meta: { title: '建设入网流程发起', permission: [ 'constrDetail' ],hiddenHeaderContent:true }
          },
          {
            path: '/construction/safelist',
            name: 'safelist',
            component: () => import('@/views/product/construction/Safelist.vue'),
            meta: { title: '安全验收', permission: [ 'safelist' ] }
          },
          {
            path: '/construction/safeDetail',
            name: 'safeDetail',
            component: () => import('@/views/product/details/SafeDetail.vue'),
            meta: { title: '安全验收流程发起', permission: [ 'safeDetail' ] ,hiddenHeaderContent:true}
          },
        ]
      },
      //同步运行
      {
        path: '/running',
        name: 'running',
        component: RouteView,
        meta: { title: '同步运行', icon: 'dashboard', permission: [ 'running' ] },
        children: [
          {
            path: '/running/changelist',
            name: 'changelist',
            component: () => import('@/views/product/running/Changelist.vue'),
            meta: { title: '变更报备', permission: [ 'changelist' ] }
          },
          {
            path: '/running/changeDetail',
            name: 'changeDetail',
            component: () => import('@/views/product/details/ChangeDetail.vue'),
            meta: { title: '变更报备流程发起', permission: [ 'changeDetail' ] }
          },
          {
            path: '/running/safelist',
            name: 'safelist',
            component: () => import('@/views/product/running/Safelist.vue'),
            meta: { title: '安全运维', permission: [ 'safelist' ] }
          },
          {
            path: '/running/safeRunDetail',
            name: 'safeRunDetail',
            component: () => import('@/views/product/details/SafeRunDetail.vue'),
            meta: { title: '安全运维流程发起', permission: [ 'safeRunDetail' ] }
          },
          {
            path: '/running/risklist',
            name: 'risklist',
            component: () => import('@/views/product/running/Risklist.vue'),
            meta: { title: '风险评估', permission: [ 'risklist' ] }
          },
          {
            path: '/running/riskDetail',
            name: 'riskDetail',
            component: () => import('@/views/product/details/RiskDetail.vue'),
            meta: { title: '风险评估流程发起', permission: [ 'riskDetail' ] }
          },
          {
            path: '/running/disposallist',
            name: 'disposallist',
            component: () => import('@/views/product/running/Disposallist.vue'),
            meta: { title: '处置备查', permission: [ 'disposallist' ] }
          },
          {
            path: '/running/disposalDetail',
            name: 'disposalDetail',
            component: () => import('@/views/product/details/DisposalDetail.vue'),
            meta: { title: '处置备查流程发起', permission: [ 'disposalDetail' ] }
          },
          {
            path: '/running/logoutlist',
            name: 'logoutlist',
            component: () => import('@/views/product/running/Logoutlist.vue'),
            meta: { title: '安全退网', permission: [ 'logoutlist' ] }
          },
          {
            path: '/running/logoutDetail',
            name: 'logoutDetail',
            component: () => import('@/views/product/details/LogoutDetail.vue'),
            meta: { title: '安全退网流程发起', permission: [ 'logoutDetail' ] }
          },
        ]
      },
      //项目清单库
      {
        path: '/product',
        name: 'product',
        redirect: '/product/list',
        component: RouteView,
        meta: { title: '项目清单库', icon: 'dashboard', permission: [ 'product' ] },
        children: [
          {
            path: '/product/list',
            name: 'productlist',
            component: () => import('@/views/product/Productlist.vue'),
            meta: { title: '项目清单库', permission: [ 'productlist' ] }
          },
          {
            path: '/product/productDetail',
            name: 'productDetail',
            component: () => import('@/views/product/details/ProductDetail.vue'),
            meta: { title: '项目系统详情', permission: [ 'productDetail' ] }
          }
        ]
      },
      //督查报表
      {
        path: '/inspector',
        name: 'inspector',
        redirect: '/inspector/syslist',
        component: RouteView,
        meta: { title: '督查报表', icon: 'dashboard', permission: [ 'inspector' ] },
        children: [
          {
            path: '/inspector/syslist',
            name: 'inspectorSyslist',
            component: () => import('@/views/product/inspector/Syslist.vue'),
            meta: { title: '系统定级', permission: [ 'inspectorSyslist' ] }
          },
          {
            path: '/inspector/reviewlist',
            name: 'inspectorReviewlist',
            component: () => import('@/views/product/inspector/Reviewlist.vue'),
            meta: { title: '立项评审', permission: [ 'inspectorReviewlist' ] }
          },
          {
            path: '/inspector/constrlist/:bdFileFlagCode',
            name: 'inspectorConstrlist',
            component: () => import('@/views/product/inspector/Constrlist.vue'),
            meta: { title: '建设入网', permission: [ 'inspectorConstrlist' ] }
          },
          {
            path: '/inspector/acceptlist',
            name: 'inspectorAcceptlist',
            component: () => import('@/views/product/inspector/Acceptlist.vue'),
            meta: { title: '安全验收', permission: [ 'inspectorAcceptlist' ] }
          },
          {
            path: '/inspector/newlist',
            name: 'inspectorNewlist',
            component: () => import('@/views/product/inspector/Newlist.vue'),
            meta: { title: '双新评估', permission: [ 'inspectorNewlist' ] }
          },
          {
            path: '/inspector/safelist',
            name: 'inspectorSafelist',
            component: () => import('@/views/product/inspector/Safelist.vue'),
            meta: { title: '安全运维', permission: [ 'inspectorSafelist' ] }
          },
          {
            path: '/inspector/risklist',
            name: 'inspectorRisklist',
            component: () => import('@/views/product/inspector/Risklist.vue'),
            meta: { title: '风险评估', permission: [ 'inspectorRisklist' ] }
          },
          {
            path: '/inspector/changelist',
            name: 'inspectorChangelist',
            component: () => import('@/views/product/inspector/Changelist.vue'),
            meta: { title: '变更报备', permission: [ 'inspectorChangelist' ] }
          },
          {
            path: '/inspector/disposallist',
            name: 'inspectorDisposallist',
            component: () => import('@/views/product/inspector/Disposallist.vue'),
            meta: { title: '处置备查', permission: [ 'inspectorDisposallist' ] }
          },
          {
            path: '/inspector/logoutlist',
            name: 'inspectorLogoutlist',
            component: () => import('@/views/product/inspector/Logoutlist.vue'),
            meta: { title: '安全退网', permission: [ 'inspectorLogoutlist' ] }
          }
        ]
      }
    ]
  },
  {
    path: '*', redirect: '/404', hidden: true
  }
]

/**
 * 基础路由
 * @type { *[] }
 */
export const constantRouterMap = [
  {
    path: '/user',
    component: UserLayout,
    redirect: '/user/login',
    hidden: true,
    children: [
      {
        path: 'login',
        name: 'login',
        component: () => import(/* webpackChunkName: "user" */ '@/views/user/Login')
      },
      {
        path: 'register',
        name: 'register',
        component: () => import(/* webpackChunkName: "user" */ '@/views/user/register/Register')
      },
      {
        path: 'register-result',
        name: 'registerResult',
        component: () => import(/* webpackChunkName: "user" */ '@/views/user/register/RegisterResult')
      },
      {
        path: 'alteration',
        name: 'alteration',
        component: () => import(/* webpackChunkName: "user" */ '@/views/user/alteration/Alteration')
      },
    ]
  },

  // {
  //   path: '/',
  //   name: 'index',
  //   component: TabLayout,
  //   meta: {title: '首页'},
  //   redirect: '/dashboard/workplace',
  //   children: [
  //     {
  //       path: '/online',
  //       name: 'online',
  //       redirect: '/online',
  //       component: RouteView,
  //       meta: {title: '在线开发', icon: 'dashboard', permission: ['dashboard']},
  //       children: [
  //         {
  //           path: '/online/auto/:code',
  //           name: 'report',
  //           component: () => import('@/views/modules/online/cgreport/OnlCgreportAutoList')
  //         },
  //       ]
  //     },
  //   ]
  // },

  {
    // OAuth2 APP页面路由
    path: '/oauth2-app',
    component: BlankLayout,
    redirect: '/oauth2-app/login',
    children: [
      {
        // OAuth2 登录路由
        path: 'login',
        name: 'login',
        component: () => import(/* webpackChunkName: "oauth2-app.login" */ '@/views/user/oauth2/OAuth2Login')
      },
    ]
  },

  {
    path: '/test',
    component: BlankLayout,
    redirect: '/test/home',
    children: [
      {
        path: 'home',
        name: 'TestHome',
        component: () => import('@/views/Home')
      }
    ]
  },
  {
    path: '/cmcc-oa',
    component: () => import('@/views/user/oauth2/CmccOa.vue')
  },
  {
    path: '/cmcc-token',
    component: () => import('@/views/user/oauth2/CmccToken.vue')
  },
  {
    path: '/404',
    component: () => import(/* webpackChunkName: "fail" */ '@/views/exception/404')
  },

]
