/**
 * Resume-Job Match System - Common JavaScript
 * Vue 3 CDN + Vanilla Fetch API (zero dependencies beyond Vue)
 * ================================================================
 */

//### 2.1 应用状态常量
//应用状态常亮，与后端 `Constants.java` 中的枚举值保持严格一致，避免前后端状态码对不上
//页面中通过 `Utils.getStatusText(status)` 和 `Utils.getStatusClass(status)` 统一获取，无需硬编码


/* ===================================================================
   1. Application Status Constants (mirrors backend Constants.java)
   =================================================================== */
const APP_STATUS = {
    PENDING:   1,//待查看
    VIEWED:    2,//已查看
    INTERVIEW: 3,//面试通知
    REJECTED:  4,//已拒绝
    ACCEPTED:  5//已录取
};

const STATUS_MAP = {
    [APP_STATUS.PENDING]:   { text: '待查看', cls: 'badge-pending' },
    [APP_STATUS.VIEWED]:    { text: '已查看', cls: 'badge-viewed' },
    [APP_STATUS.INTERVIEW]: { text: '面试通知', cls: 'badge-interview' },
    [APP_STATUS.REJECTED]:  { text: '已拒绝', cls: 'badge-rejected' },
    [APP_STATUS.ACCEPTED]:  { text: '已录取', cls: 'badge-accepted' }
};

const ROLE_USER  = 0;//普通用户/求职者
const ROLE_ADMIN = 1;//管理者

/*
### 2.2 认证模块 `Auth`
 **Token 存储策略**：JWT Token 存在 `localStorage`，每次 API 请求由 `Api._request()` 自动从 `Auth.getToken()` 获取并注入 `Authorization: Bearer <token>` 请求头
- **用户信息结构**：`{ id, username, realName, role, phone, email }` 存在 localStorage 的 `user` key 下
- **安全退出**：`clearAuth()` 清除全部认证数据，`logout()` 在清除后跳转登录页
- **容错处理**：`getUser()` 使用 try-catch 包裹 `JSON.parse`，防止存储数据损坏导致页面崩溃
 */



/* ===================================================================
   2. Auth Helper
   =================================================================== */
const Auth = {
    isLoggedIn() {
        return !!localStorage.getItem('token');
    },
    getToken() {
        return localStorage.getItem('token');
    },
    getUser() {
        try {
            return JSON.parse(localStorage.getItem('user') || 'null');
        } catch (e) {
            return null;
        }
    },
    setAuth(token, user) {
        localStorage.setItem('token', token);
        localStorage.setItem('user', JSON.stringify(user));
    },
    clearAuth() {
        localStorage.removeItem('token');
        localStorage.removeItem('user');
    },
    logout() {
        this.clearAuth();
        location.href = '/pages/login.html';
    },
    isAdmin() {
        const user = this.getUser();
        return user && user.role === ROLE_ADMIN;
    }
};

/*
### 2.3 API 请求模块 `Api` —— 核心网络层
**设计要点与关键决策**：

| 设计点 | 实现方式 | 原因 |
|--------|----------|------|
| URL 前缀自动补全 | 检测 `/api/` 前缀，无则自动添加 | 页面调用时写短路径即可，统一管理 |
| 空值过滤 | `filter(([,v]) => v !== undefined && v !== null && v !== '')` | 避免 `?keyword=&category=` 这类无效参数 |
| 401 全局拦截 | 清除认证 → 跳转登录 → 抛异常阻断后续 | 用户无感知的自动登出 |
| 403 全局拦截 | `alert('无权限执行此操作')` | 明确提示权限不足 |
| JSON/非JSON智能解析 | 检查 `content-type` 响应头 | 兼容文件下载等非 JSON 响应 |
| `postForm` 独立方法 | 不设 `Content-Type` | 浏览器自动生成 `multipart/form-data; boundary=...` |
| 无重试机制 | 单次请求即抛错 | 由调用方页面自行决定是否重试 |
 */




/* ===================================================================
   3. API Helper (vanilla fetch, no axios needed)
   =================================================================== */
const Api = {
    async _request(method, url, body, params) {
        //统一前缀
        let fullUrl = url.startsWith('/api/') ? url : '/api/' + url.replace(/^\//, '');
        if (params) {
            const qs = Object.entries(params)
                .filter(([, v]) => v !== undefined && v !== null && v !== '')
                .map(([k, v]) => encodeURIComponent(k) + '=' + encodeURIComponent(v))
                .join('&');
            if (qs) fullUrl += (fullUrl.includes('?') ? '&' : '?') + qs;
        }
        //统一请求头+token
        const headers = { 'Content-Type': 'application/json' };
        const token = Auth.getToken();
        if (token) headers['Authorization'] = 'Bearer ' + token;

        const opts = { method, headers };
        if (body && (method === 'POST' || method === 'PUT' || method === 'PATCH')) {
            opts.body = JSON.stringify(body);
        }

        try {
            //统一请求发送
            const res = await fetch(fullUrl, opts);
            //统一状态码处理
            if (res.status === 401) { Auth.clearAuth(); location.href = '/pages/login.html'; throw new Error('Unauthorized'); }
            if (res.status === 403) { alert('无权限执行此操作'); throw new Error('Forbidden'); }
            const ct = res.headers.get('content-type') || '';
            if (ct.includes('application/json')) return await res.json();
            return { code: res.ok ? 200 : res.status, message: res.ok ? 'ok' : 'error' };
        } catch (err) {
            if (err.message === 'Unauthorized' || err.message === 'Forbidden') throw err;
            console.error('[Api]', method, url, err);
            throw err;
        }
    },
    //统一提供方法：get post put delete
    get(url, params)       { return this._request('GET', url, null, params); },
    post(url, data)        { return this._request('POST', url, data, null); },
    put(url, data)         { return this._request('PUT', url, data, null); },
    delete(url, data)      { return this._request('DELETE', url, data || {}, null); },
    //postForm is for multipart/form-data, e.g. file uploads, where body is FormData instance
    async postForm(url, formData) {
        const fullUrl = url.startsWith('/api/') ? url : '/api/' + url.replace(/^\//, '');
        const token = Auth.getToken();

        const headers = {};
        if (token) {
            headers['Authorization'] = 'Bearer ' + token;
        }

        try {
            const res = await fetch(fullUrl, {
                method: 'POST',
                headers: headers,
                body: formData
            });

            if (res.status === 401) {
                Auth.clearAuth();
                location.href = '/pages/login.html';
                throw new Error('Unauthorized');
            }
            return await res.json();
        } catch (err) {
            console.error('[Api postForm]', url, err);
            throw err;
        }
    }
};


/*
### 2.4 工具函数 `Utils`
**关键设计细节**：

- **`formatSalary`**：自动将千位以上数值转为 `K` 表示（`10000` → `10K`），保留范围格式（`10000-20000` → `10K-20K`），非数字保持原样
- **`formatDate`**：使用 `padStart(2, '0')` 保证两位数月份/日期/时分，无效日期返回空字符串不崩溃
- **`getMatchLevel`/`getMatchClass`**：四级划分，与匹配分数圆环联动
- **`debounce`**：标准闭包实现，可在搜索框 `@input` 事件中使用

 */



/* ===================================================================
   4. Utilities
   =================================================================== */
const Utils = {
    formatDate(dateStr) {
        if (!dateStr) return '';
        const d = new Date(dateStr);
        if (isNaN(d.getTime())) return '';
        const pad = n => String(n).padStart(2, '0');
        return `${d.getFullYear()}-${pad(d.getMonth()+1)}-${pad(d.getDate())} ${pad(d.getHours())}:${pad(d.getMinutes())}`;
    },
    formatSalary(s) {
        if (!s && s !== 0) return '薪资面议';
        s = String(s).trim();
        if (s.includes('-')) return s.split('-').map(v => {
            const n = parseInt(v);
            return isNaN(n) ? v : (n >= 1000 ? Math.round(n/1000) + 'K' : n);
        }).join('-');
        const n = parseInt(s);
        return isNaN(n) ? s : (n >= 1000 ? Math.round(n/1000) + 'K' : String(n));
    },
    getStatusText(status)  { return STATUS_MAP[status]?.text || '未知'; },
    getStatusClass(status) { return STATUS_MAP[status]?.cls || ''; },
    getMatchLevel(score) {
        if (score >= 80) return '优秀';
        if (score >= 60) return '良好';
        if (score >= 40) return '一般';
        return '较低';
    },
    getMatchClass(score) {
        if (score >= 80) return 'high';
        if (score >= 60) return 'mid';
        return 'low';
    },
    truncate(str, len) {
        if (!str) return '';
        return str.length <= len ? str : str.slice(0, len) + '...';
    },
    debounce(fn, delay) {
        let t = null;
        return function(...args) { clearTimeout(t); t = setTimeout(() => fn.apply(this, args), delay); };
    }
};

/*
### 2.5 页面守卫 `PageGuard`
**调用时机**：每个页面在 `mounted()` 生命周期最开始调用，形成前端路由级别的权限控制：

```
用户页面: mounted() → initLayout('user') → PageGuard.requireAuth()
管理员页面: mounted() → initLayout('admin') → PageGuard.requireAdmin()
登录/注册页: mounted() → PageGuard.redirectIfAuth()
```

**返回值设计**：守卫返回 `null` 表示验证失败（已执行跳转），调用方检查返回值决定是否继续初始化。
 */




/* ===================================================================
   5. Page Guards
   =================================================================== */
const PageGuard = {
    requireAuth() {
        if (!Auth.isLoggedIn()) { location.href = '/pages/login.html'; return null; }
        return Auth.getUser();
    },
    requireAdmin() {
        const user = this.requireAuth();
        if (!user) return null;
        if (user.role !== ROLE_ADMIN) { location.href = '/pages/user/job-list.html'; return null; }
        return user;
    },
    redirectIfAuth() {
        if (!Auth.isLoggedIn()) return;
        const user = Auth.getUser();
        location.href = user && user.role === ROLE_ADMIN ? '/pages/admin/dashboard.html' : '/pages/user/job-list.html';
    }
};

/*
### 2.6 Toast 通知系统
- **容器惰性创建**：第一次调用时动态创建 `#toast-container`，后续调用复用
- **4种类型**：`success`(绿)、`error`(红)、`warning`(黄)、`info`(蓝)，对应不同 CSS 背景色
- **自动销毁**：显示3秒 → 0.3秒淡出动画 → 从DOM中移除
- **堆叠显示**：容器为 `flex-direction: column`，多条通知自动堆叠
 */




/* ===================================================================
   6. Toast Notifications
   =================================================================== */
const Toast = {
    show(msg, type) {
        type = type || 'info';
        const container = document.getElementById('toast-container') || (() => {
            const el = document.createElement('div');
            el.id = 'toast-container';
            el.className = 'toast-container';
            document.body.appendChild(el);
            return el;
        })();
        const toast = document.createElement('div');
        toast.className = 'toast toast-' + type;
        toast.textContent = msg;
        container.appendChild(toast);
        setTimeout(() => { toast.style.opacity = '0'; toast.style.transition = 'opacity .3s';
            setTimeout(() => toast.remove(), 300); }, 3000);
    },
    success(msg) { this.show(msg, 'success'); },
    error(msg)   { this.show(msg, 'error'); },
    warning(msg) { this.show(msg, 'warning'); },
    info(msg)    { this.show(msg, 'info'); }
};

/*

### 2.7 模态框控制 `Modal`

简单的显示/隐藏切换，通过 CSS 类 `.modal-overlay` 实现弹窗效果：
- `display: flex` → 遮罩层出现（居中布局）
- `display: none` → 隐藏
- 遮罩层 `@click.self` 可关闭弹窗，弹窗本体 `modal-box` 阻止事件冒泡
 */




/* ===================================================================
   7. Modal Helper
   =================================================================== */
const Modal = {
    open(id) {
        const el = document.getElementById(id);
        if (el) el.style.display = 'flex';
    },
    close(id) {
        const el = document.getElementById(id);
        if (el) el.style.display = 'none';
    }
};


/*

### 2.8 侧边栏渲染器 `Sidebar`

**渲染逻辑**：

1. 获取当前页面路径 `location.pathname`
2. 遍历菜单配置数组
3. **分组菜单**（有 `children` 属性）：先渲染分组标题，再渲染子菜单项，通过 `currentPath.includes(c.url)` 判定是否高亮
4. **一级菜单**（无 `children`）：直接渲染，同样按路径匹配高亮
5. 高亮样式：添加 `.active` 类 → 左侧蓝色渐变竖条 + 文字变白
 */

/* ===================================================================
   8. Sidebar Renderer
   =================================================================== */
const Sidebar = {
    render(menuItems) {
        const nav = document.getElementById('sidebarNav');
        if (!nav) return;
        const currentPath = location.pathname;
        let html = '';
        menuItems.forEach(item => {
            if (item.children) {
                const hasActive = item.children.some(c => currentPath.includes(c.url));
                html += `<div class="nav-group"><div class="nav-group-title">${item.title}</div>`;
                item.children.forEach(c => {
                    const active = currentPath.includes(c.url);
                    html += `<a href="${c.url}" class="nav-item${active ? ' active' : ''}"><span class="icon">${c.icon}</span>${c.label}</a>`;
                });
                html += '</div>';
            } else {
                const active = currentPath.includes(item.url);
                html += `<a href="${item.url}" class="nav-item${active ? ' active' : ''}"><span class="icon">${item.icon}</span>${item.label}</a>`;
            }
        });
        nav.innerHTML = html;
    }
};

/*
### 2.9 菜单配置 `MENU`
使用 emoji 作为图标，无需引入图标库，零依赖。
 */



/* ===================================================================
   9. Menu Configs
   =================================================================== */
const MENU = {
    admin: [
        { title: '管理后台', children: [
            { icon: '📊', label: '数据大屏', url: '/pages/admin/dashboard.html' },
            { icon: '👥', label: '用户管理', url: '/pages/admin/user-manage.html' },
            { icon: '💼', label: '岗位管理', url: '/pages/admin/job-manage.html' },
            { icon: '📄', label: '简历管理', url: '/pages/admin/resume-manage.html' },
            { icon: '📋', label: '投递管理', url: '/pages/admin/application-manage.html' },
            { icon: '📝', label: '系统日志', url: '/pages/admin/system-log.html' }
        ]}
    ],
    user: [
        { title: '求职中心', children: [
            { icon: '🏠', label: '岗位列表', url: '/pages/user/job-list.html' },
            { icon: '📄', label: '我的简历', url: '/pages/user/resume-list.html' },
            { icon: '🔍', label: '匹配结果', url: '/pages/user/match-result.html' },
            { icon: '📋', label: '我的投递', url: '/pages/user/my-applications.html' },
            { icon: '⭐', label: '我的收藏', url: '/pages/user/favorites.html' },
            { icon: '🔔', label: '消息通知', url: '/pages/user/notifications.html' },
            { icon: '👤', label: '个人中心', url: '/pages/user/profile.html' }
        ]}
    ]
};

/*
### 2.10 布局初始化器 `initLayout()`
**这是每个页面的标准初始化入口**。调用流程：

```
initLayout('user') 或 initLayout('admin')
    │
    ├── 1. 权限校验 (PageGuard)
    │      └── 失败 → return null → 页面不继续初始化
    │
    ├── 2. 设置顶栏 "你好，张三"
    │
    ├── 3. 渲染侧边栏菜单 + 高亮当前页
    │
    ├── 4. (仅用户端) GET /notification/unread-count
    │      └── 有未读 → 通知铃铛显示红点
    │
    └── return user → 页面获取 currentUser
```
 */




/* ===================================================================
   10. Layout Initializer
   =================================================================== */
function initLayout(role) {
    const user = role === 'admin' ? PageGuard.requireAdmin() : PageGuard.requireAuth();
    if (!user) return null;

    // Header user name
    const nameEl = document.getElementById('headerUserName');
    if (nameEl) nameEl.textContent = user.realName || user.username || '用户';

    // Sidebar menu
    Sidebar.render(role === 'admin' ? MENU.admin : MENU.user);

    // Unread notification count for users
    if (role === 'user') {
        Api.get('/notification/unread-count').then(res => {
            if (res && res.code === 200) {
                const dot = document.getElementById('notifyDot');
                if (dot && res.data && res.data > 0) dot.style.display = 'block';
            }
        }).catch(() => {});
    }

    return user;
}

/*
### 2.11 Vue 3 应用工厂 `createVueApp()`

- 自动合并：每个页面组件自动获得 `loading`、`error`、`currentUser` 三个公共 data 属性
- 全局属性注入：模板中可直接使用 `$api.get(...)`、`$utils.formatDate(...)`、`$toast.success(...)` 等
- 统一入口：所有页面的 Vue 应用都通过此工厂函数创建，保证行为一致
 */


/* ===================================================================
   11. Vue 3 App Factory
   =================================================================== */
function createVueApp(options) {
    options = options || {};
    const origData = options.data || (() => ({}));
    options.data = function() {
        return Object.assign({ loading: false, error: '', currentUser: Auth.getUser() },
            (typeof origData === 'function' ? origData.call(this) : origData));
    };
    const app = Vue.createApp(options);
    app.config.globalProperties.$api = Api;
    app.config.globalProperties.$utils = Utils;
    app.config.globalProperties.$auth = Auth;
    app.config.globalProperties.$toast = Toast;
    const el = document.getElementById('app');
    if (el) app.mount('#app');
    return app;
}

/*
### 2.12 URL 参数解析 & 兼容性别名

 */





/* ===================================================================
   12. URL Param Helper
   =================================================================== */
function getQueryParam(name) {
    return new URLSearchParams(location.search).get(name);
}

/* ===================================================================
   13. Legacy compatibility aliases
   =================================================================== */
function checkLogin()  { return PageGuard.requireAuth(); }
function logout()      { Auth.logout(); }
function formatDate(d) { return Utils.formatDate(d); }
function getUser()     { return Auth.getUser(); }
