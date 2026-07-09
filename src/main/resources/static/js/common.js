(function immediateAuthCheck() {
    const publicPaths = ['/auth/login','/auth/register','/'];
    const currentPath = window.location.pathname;
    const isPublic = publicPaths.some(path => currentPath === path || currentPath.startsWith(path + '/'));

    if (!isPublic && !isAuthenticated()) {
        console.log("Not authenticated, redirecting...");
        window.location.href = '/auth/login';
    }
})();

function getToken() {
    return localStorage.getItem('token');
}

function setToken(token) {
    localStorage.setItem('token', token);
}

function clearToken() {
    localStorage.removeItem('token');
}

function isAuthenticated() {
    const token = getToken();
    if (!token) return false;

    try {
        const payload = JSON.parse(atob(token.split('.')[1]));
        const now = Math.floor(Date.now() / 1000);

        if (payload.exp && payload.exp < now) {
            console.warn("Token expired, logging out...");
            clearToken();
            clearUser();
            return false;
        }
        return true;
    } catch (e) {
        return false;
    }
}

function getUser() {
    const userStr = localStorage.getItem('user');
    return userStr ? JSON.parse(userStr) : null;
}

function setUser(user) {
    localStorage.setItem('user', JSON.stringify(user));
}

function clearUser() {
    localStorage.removeItem('user');
}

async function logout() {
    try {
        await fetch('/api/auth/logout', { method: 'POST', credentials: 'include' });
    } catch (err) {
        console.error('Logout error:', err);
    } finally {
        clearToken();
        clearUser();
        window.location.href = '/auth/login';
    }
}

function getAuthHeaders() {
    const token = getToken();
    const headers = { 'Content-Type': 'application/json' };
    if (token) headers['Authorization'] = `Bearer ${token}`;
    return headers;
}

function getUserRoleFromToken() {
    const token = getToken();
    if (!token) return null;
    try {
        const payload = JSON.parse(atob(token.split('.')[1]));
        return payload.role || (payload.role ? payload.role[0] : null);
    } catch (err) {
        console.error('Failed to parse token payload', err);
        return null;
    }
}

function checkAdminAccess() {
    const user = getUser();
    if (user && user.role)
        return user.role.includes('ROLE_ADMIN');
    return getUserRoleFromToken() === 'ROLE_ADMIN';
}

function updateNavigation() {
    const rightSideLinks = document.getElementById('right-side-links');
    if (!rightSideLinks) return;

    let authHtml = '';

    if (isAuthenticated()) {
        const user = getUser();

        authHtml = `
            <li class="nav-item">
                <a class="nav-link" href="#" onclick="logout()">
                    <i class="bi bi-box-arrow-right me-1"></i>Logout
                </a>
            </li>`;

    } else {
        authHtml = `
            <li class="nav-item"><a class="nav-link" href="/auth/login"><i class="bi bi-box-arrow-in-right me-1"></i>Login</a></li>
            <li class="nav-item"><a class="nav-link" href="/auth/register"><i class="bi bi-person-plus me-1"></i>Register</a></li>`;
    }

    rightSideLinks.innerHTML = authHtml;
}

function checkAuth(publicPaths = ['/auth/login','/auth/register','/']) {
    const currentPath = window.location.pathname;
    const isPublic = publicPaths.some(path => currentPath === path || currentPath.startsWith(path + '/'));
    if (!isAuthenticated() && !isPublic)
        window.location.href = '/auth/login';
}

async function makeAuthenticatedRequest(url, options = {}) {
    options.headers = { ...getAuthHeaders(), ...(options.headers || {}) };
    options.credentials = 'include';

    const res = await fetch(url, options);
    if (res.status === 401 || res.status === 403) {
        logout();
        throw new Error('Authentication required');
    }
    return res;
}

window.addEventListener('focus', () => {
    if (!isAuthenticated() && !isPublicPath(window.location.pathname)) {
        window.location.href = '/auth/login';
    }
});

function isPublicPath(path) {
    const publicPaths = ['/auth/login','/auth/register','/'];
    return publicPaths.some(p => path === p || path.startsWith(p + '/'));
}

document.addEventListener('DOMContentLoaded', () => {
    checkAuth();
    updateNavigation();
});

window.addEventListener('storage', (e) => {
    if (e.key === 'token' || e.key === 'user') updateNavigation();
});