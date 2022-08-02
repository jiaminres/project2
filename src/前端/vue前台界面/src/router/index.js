import Vue from 'vue'
import VueRouter from 'vue-router'
import Login from '../components/nav/Login'
import Group from '../components/nav/Group'
import Block from '../components/nav/Block'
import Native from '../components/Native'
import Setting from '../components/nav/Setting'
import Edit from "../components/nav/Edit"
Vue.use(VueRouter)

const router = new VueRouter({

    routes: [
        {
            name: 'login',
            path: '/login',
            component: Login
        },
        {
            name: 'native',
            path: '/native',
            component: Native,
            props($route) {
                return {
                    //跳入交流页面
                    groupID: $route.query.groupID,
                    leaderID: $route.query.leaderID,
                    groupName: $route.query.groupName,
                    headImageAddress: $route.query.headImageAddress
                }
            },
            //防止无用户状态登录
            beforeEnter: (to, from, next) => {
                if (!localStorage.getItem("login") || localStorage.getItem("login") !== "true") {
                    alert("请先登录")
                    return
                }
                next();
            }
        },
        {
            name: 'group',
            path: '/group',
            component: Group,
            children: [{
                name: 'block',
                path: 'block',
                component: Block
            },
            {
                name: 'setting',
                path: 'setting',
                component: Setting
            },
            {
                name: 'edit',
                path: 'edit',
                component: Edit
            }],
            //防止无用户状态登录
            beforeEnter: (to, from, next) => {
                // if (!localStorage.getItem("login") || localStorage.getItem("login") !== "true") {
                //     alert("请先登录")
                //     return
                // }
                next();
            }

        }
    ]
})

export default router