document.addEventListener("DOMContentLoaded", async () => {
    if (!isAuthenticated()) {
        logout();
        return;
    }

    try {
        await loadUser();
        await loadOrders();
    } catch (e) {
        console.error(e);
        alert("Failed to load profile.");
    }
});

async function loadUser() {
    const response = await makeAuthenticatedRequest("/api/users/me", {
        method: "GET",
        headers: getAuthHeaders()
    });

    if (response.status === 401 || response.status === 403) {
        logout();
        return;
    }

    if (!response.ok) {
        throw new Error("Failed to load user.");
    }

    const user = await response.json();

    document.getElementById("userEmail").textContent = user.email;
    document.getElementById("userRole").textContent = user.role.replace("ROLE_", "");
}

async function loadOrders() {
    const response = await makeAuthenticatedRequest("/api/profile/orders", {
        method: "GET",
        headers: getAuthHeaders()
    });

    if (response.status === 401 || response.status === 403) {
        logout();
        return;
    }

    if (!response.ok) {
        throw new Error("Failed to load orders.");
    }

    const orders = await response.json();

    document.getElementById("ordersCount").textContent = orders.length;
    document.getElementById("ordersBadge").textContent = orders.length;

    const container = document.getElementById("ordersContainer");

    container.innerHTML = "";

    orders.forEach(order => {
        container.insertAdjacentHTML("beforeend", createOrderCard(order));
    });

    bindButtons();
}

function bindButtons() {
    document.querySelectorAll(".details-btn").forEach(button => {
        button.addEventListener("click", () => {
            window.location.href = `/profile/orders/${button.dataset.id}`;
        });
    });

    document.querySelectorAll(".cancel-btn").forEach(button => {
        button.addEventListener("click", () => cancelOrder(button.dataset.id));
    });
}

async function cancelOrder(orderId) {
    if (!confirm("Cancel this order?")) {
        return;
    }

    try {
        const response = await makeAuthenticatedRequest(`/api/profile/orders/${orderId}/cancel`, {
            method: "POST",
            headers: getAuthHeaders()
        });

        if (response.ok || response.status === 204) {
            alert("Order cancellation requested.");
            await loadOrders();
            return;
        }

        const error = await response.text();
        alert(error);
    } catch (e) {
        console.error(e);
        alert("Server error.");
    }
}

function createOrderCard(order) {
    const orderStatus = order.orderStatus.toLowerCase();
    const paymentStatus = order.paymentStatus.toLowerCase();

    const canCancel = !["READY", "COMPLETED", "CANCELLED"].includes(order.orderStatus);

    return `
        <div class="order-card">
            <div class="order-header">
                <div>
                    <div class="order-id">Order #${order.id}</div>
                    <div class="order-date">${formatDate(order.createdAt)}</div>
                </div>
                <div class="d-flex gap-2 flex-wrap">
                    <span class="order-status ${orderStatus}">
                        ${formatEnum(order.orderStatus)}
                    </span>
                    <span class="payment-status ${paymentStatus}">
                        ${formatEnum(order.paymentStatus)}
                    </span>
                </div>
            </div>

            <div class="order-body">
                <div class="order-info">
                    <div class="order-info-item">
                        <span class="order-info-label">Items</span>
                        <span class="order-info-value">${order.itemsCount}</span>
                    </div>

                    <div class="order-info-item">
                        <span class="order-info-label">Total</span>
                        <span class="order-total">${order.totalAmount} ₸</span>
                    </div>
                </div>

                <div class="order-actions">
                    <button class="btn btn-outline-warning details-btn" data-id="${order.id}">
                        Details
                    </button>
                    ${canCancel ? `<button class="btn btn-outline-danger cancel-btn" data-id="${order.id}">Cancel</button>` : ""}
                </div>
            </div>
        </div>
    `;
}

function formatDate(date) {
    return new Date(date).toLocaleString();
}

function formatEnum(value) {
    return value
        .toLowerCase()
        .replaceAll("_", " ")
        .replace(/\b\w/g, c => c.toUpperCase());
}