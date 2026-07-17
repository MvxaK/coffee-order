document.addEventListener("DOMContentLoaded", async () => {
    if (!isAuthenticated()) {
        logout();
        return;
    }

    const path = window.location.pathname.split("/");
    const orderId = path[path.length - 1];

    const orderIdElement = document.getElementById("orderId");
    const createdAtElement = document.getElementById("createdAt");
    const orderStatus = document.getElementById("orderStatus");
    const paymentStatus = document.getElementById("paymentStatus");
    const totalAmount = document.getElementById("totalAmount");
    const itemsCount = document.getElementById("itemsCount");
    const itemsContainer = document.getElementById("itemsContainer");
    const backButton = document.getElementById("backButton");

    if (backButton) {
        backButton.addEventListener("click", () => {
            window.location.href = "/profile";
        });
    }

    try {
        const response = await makeAuthenticatedRequest(`/api/profile/orders/${orderId}`, {
            method: "GET",
            headers: getAuthHeaders()
        });

        if (!response.ok) {
            if (response.status === 404) {
                alert("Order not found.");
                window.location.href = "/profile";
                return;
            }

            if (response.status === 401 || response.status === 403) {
                logout();
                return;
            }

            throw new Error(await response.text());
        }

        const order = await response.json();

        orderIdElement.textContent = order.id;
        createdAtElement.textContent = formatDate(order.createdAt);
        totalAmount.textContent = formatPrice(order.totalAmount);
        itemsCount.textContent = order.items.length;

        setStatus(orderStatus, order.orderStatus);
        setStatus(paymentStatus, order.paymentStatus);

        itemsContainer.innerHTML = "";

        order.items.forEach(item => {
            const additives = item.additives.length
                ? item.additives.map(additive => `
                    <div class="additive-item">
                        <div>
                            <div class="additive-name">${additive.additiveName}</div>
                            <div class="additive-quantity">x${additive.quantity}</div>
                        </div>
                        <div class="additive-price">${formatPrice(additive.price)}</div>
                    </div>
                `).join("")
                : `<div class="empty-additives">No additives</div>`;

            itemsContainer.insertAdjacentHTML("beforeend", `
                <div class="order-item card shadow-sm mb-4">
                    <div class="card-body">
                        <div class="order-item-header">
                            <div>
                                <div class="order-item-name">${item.coffeeName}</div>
                                <div class="order-item-size">${item.sizeName}</div>
                            </div>
                            <div class="order-item-price">${formatPrice(item.price)}</div>
                        </div>
                        <div class="order-item-meta">
                            <div>
                                <span>Quantity</span>
                                <strong>${item.quantity}</strong>
                            </div>
                        </div>
                        <hr>
                        <div class="additives-title mb-3">Additives</div>
                        <div class="additives-list">
                            ${additives}
                        </div>
                    </div>
                </div>
            `);
        });

    } catch (e) {
        alert('Failed to load order');
    }

    function setStatus(element, status) {
        element.textContent = status;
        element.className = "status-badge";

        switch (status) {
            case "CREATED":
                element.classList.add("status-created");
                break;
            case "PAID":
                element.classList.add("status-paid");
                break;
            case "PREPARING":
                element.classList.add("status-preparing");
                break;
            case "READY":
                element.classList.add("status-ready");
                break;
            case "COMPLETED":
                element.classList.add("status-completed");
                break;
            case "CANCELLED":
                element.classList.add("status-cancelled");
                break;
        }
    }

    function formatPrice(price) {
        return Number(price).toFixed(2) + " ₸";
    }

    function formatDate(date) {
        return new Date(date).toLocaleString();
    }
});