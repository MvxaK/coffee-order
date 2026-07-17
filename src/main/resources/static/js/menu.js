const coffeeContainer = document.getElementById("coffeeContainer");
const additivesContainer = document.getElementById("additivesContainer");

let modal = null;

const modalCoffeeName = document.getElementById("modalCoffeeName");
const modalCoffeeImage = document.getElementById("modalCoffeeImage");
const modalCoffeeDescription = document.getElementById("modalCoffeeDescription");
const modalCoffeePrice = document.getElementById("modalCoffeePrice");

const coffeeSize = document.getElementById("coffeeSize");
const coffeeQuantity = document.getElementById("coffeeQuantity");
const totalPrice = document.getElementById("totalPrice");

let coffees = [];
let additives = [];
let sizes = [];

let selectedCoffee = null;
let selectedCoffeeQuantity = 1;
let selectedAdditives = new Map();

let orderPreviewModal = null;
const orderPreviewContent = document.getElementById("orderPreviewContent");

let currentOrder = {
    userId: null,
    items: []
}

let currentUserId = null;

document.addEventListener("DOMContentLoaded", async () => {
    try {
        modal = new bootstrap.Modal(document.getElementById("coffeeModal"));
        orderPreviewModal = new bootstrap.Modal(document.getElementById("orderPreviewModal"));
        await loadData();
        renderCoffees();
        renderAdditives();
    } catch (error) {
        console.error("Error when initializing:", error);
    }
});

async function loadData() {
    const [coffeeRes, additiveRes, sizeRes, userRes] = await Promise.all([
        fetch("/api/coffees"),
        fetch("/api/additives"),
        fetch("/api/coffee-sizes"),
        fetch("/api/users/me")
    ]);

    coffees = await coffeeRes.json();
    additives = await additiveRes.json();
    sizes = await sizeRes.json();

    const userData = await userRes.json();
    currentUserId = userData.id;
}

function renderCoffees() {
    coffeeContainer.innerHTML = "";

    coffees.forEach(coffee => {
        const col = document.createElement("div");
        col.className = "col-lg-4 col-md-6";

        col.innerHTML = `
            <div class="card coffee-card">
                <img src="${coffee.url}" class="coffee-image">
                <div class="card-body">
                    <h5 class="card-title">${coffee.name}</h5>
                    <p class="text-muted">${coffee.description}</p>
                    <div class="d-flex justify-content-between align-items-center">
                        <span class="coffee-price">${coffee.price} ₸</span>
                        <button class="btn btn-warning">Choose</button>
                    </div>
                </div>
            </div>
        `;

        col.querySelector("button").onclick = () => openCoffee(coffee);

        coffeeContainer.appendChild(col);
    });
}

function openCoffee(coffee) {
    selectedCoffee = coffee;
    selectedCoffeeQuantity = 1;

    selectedAdditives.clear();
    additives.forEach(additive => selectedAdditives.set(additive.id, 0));

    document.querySelectorAll(".additive-quantity").forEach(span => span.textContent = "0");

    coffeeQuantity.textContent = "1";
    modalCoffeeName.textContent = coffee.name;
    modalCoffeeImage.src = coffee.url;
    modalCoffeeDescription.textContent = coffee.description;
    modalCoffeePrice.textContent = coffee.price + " ₸";

    renderSizes();
    calculateTotal();

    modal.show();
}

function renderSizes() {
    let html = "";
    sizes.forEach(size => {
        html += `
            <option value="${size.id}" data-price="${size.price}">
                ${size.size} (+${size.price} ₸)
            </option>
        `;
    });
    coffeeSize.innerHTML = html;
    coffeeSize.onchange = calculateTotal;
}

function renderAdditives() {
    additivesContainer.innerHTML = "";

    additives.forEach(additive => {
        selectedAdditives.set(additive.id, 0);

        const row = document.createElement("div");
        row.className = "additive-row";
        row.innerHTML = `
            <div class="additive-info">
                <img src="${additive.url}" class="additive-image" alt="${additive.name}">
                <div>
                    <div class="additive-name">${additive.name}</div>
                    <div class="additive-price">${additive.price} ₸</div>
                </div>
            </div>
            <div class="additive-controls">
                <button class="btn btn-outline-warning minus">-</button>
                <span class="additive-quantity">0</span>
                <button class="btn btn-outline-warning plus">+</button>
            </div>
        `;

        const quantitySpan = row.querySelector(".additive-quantity");

        row.querySelector(".minus").onclick = () => {
            const current = selectedAdditives.get(additive.id) ?? 0;
            if (current > 0) {
                selectedAdditives.set(additive.id, current - 1);
                quantitySpan.textContent = current - 1;
                calculateTotal();
            }
        };

        row.querySelector(".plus").onclick = () => {
            const current = selectedAdditives.get(additive.id) ?? 0;
            selectedAdditives.set(additive.id, current + 1);
            quantitySpan.textContent = current + 1;
            calculateTotal();
        };

        additivesContainer.appendChild(row);
    });
}

document.getElementById("coffeeMinus").onclick = () => {
    if (selectedCoffeeQuantity > 1) {
        selectedCoffeeQuantity--;
        coffeeQuantity.textContent = selectedCoffeeQuantity;
        calculateTotal();
    }
};

document.getElementById("coffeePlus").onclick = () => {
    selectedCoffeeQuantity++;
    coffeeQuantity.textContent = selectedCoffeeQuantity;
    calculateTotal();
};

function calculateTotal() {
    if (!selectedCoffee)
        return;

    let singleCupPrice = Number(selectedCoffee.price);
    const selectedSize = coffeeSize.options[coffeeSize.selectedIndex];
    if (selectedSize) {
        singleCupPrice += Number(selectedSize.dataset.price);
    }

    additives.forEach(additive => {
        const additiveQuantity = selectedAdditives.get(additive.id) ?? 0;
        singleCupPrice += Number(additive.price) * additiveQuantity;
    });

    let total = singleCupPrice * selectedCoffeeQuantity;

    totalPrice.textContent = total.toFixed(2) + " ₸";
}

document.getElementById("addToOrder").onclick = () => {
    let singleCupPrice = Number(selectedCoffee.price);
    const selectedSize = coffeeSize.options[coffeeSize.selectedIndex];
    if (selectedSize) {
        singleCupPrice += Number(selectedSize.dataset.price);
    }

    const itemRequests = [];
    additives.forEach(additive => {
        const qty = selectedAdditives.get(additive.id) ?? 0;
        if (qty > 0) {
            itemRequests.push({
                additiveId: Number(additive.id),
                additiveName: additive.name,
                quantity: qty,
                price: Number(additive.price)
            });
            singleCupPrice += Number(additive.price) * qty;
        }
    });

    const itemTotalPrice = singleCupPrice * selectedCoffeeQuantity;

    const orderItem = {
        id: Date.now() + Math.random().toString(36).substr(2, 9),
        coffeeId: Number(selectedCoffee.id),
        coffeeName: selectedCoffee.name,
        sizeId: Number(coffeeSize.value),
        sizeName: coffeeSize.options[coffeeSize.selectedIndex].text,
        quantity: selectedCoffeeQuantity,
        singlePrice: singleCupPrice,
        totalItemPrice: itemTotalPrice,
        requests: itemRequests
    };

    currentOrder.items.push(orderItem);
    modal.hide();
};

document.getElementById("submitFinalOrder").onclick = () => {
    if (currentOrder.items.length === 0) {
        alert("You did not choose coffee");
        return;
    }

    renderPreviewList();
    orderPreviewModal.show();
};

function renderPreviewList() {
    orderPreviewContent.innerHTML = "";
    let finalOrderTotal = 0;

    currentOrder.items.forEach((item, index) => {
        finalOrderTotal += item.totalItemPrice;

        const additivesList = item.requests
            .map(req => `${req.additiveName} (x${req.quantity})`)
            .join(", ");

        const row = document.createElement("div");
        row.className = `d-flex justify-content-between align-items-center py-3 ${index > 0 ? 'border-top' : ''}`;

        row.innerHTML = `
            <div>
                <strong>${item.coffeeName}</strong> <span class="text-muted">x ${item.quantity} count</span>
                <div class="text-muted small">Size: ${item.sizeName}</div>
                ${additivesList ? `<div class="text-success small">Additives: ${additivesList}</div>` : ''}
                <div class="fw-semibold small mt-1 text-secondary">
                    Price: ${item.singlePrice.toFixed(2)} ₸ each.
                </div>
            </div>
            <div class="text-end d-flex align-items-center gap-3">
                <span class="fw-bold text-nowrap">${item.totalItemPrice.toFixed(2)} ₸</span>
                <button class="btn btn-sm btn-outline-danger remove-item-btn" title="Delete">
                    &times;
                </button>
            </div>
        `;

        row.querySelector(".remove-item-btn").onclick = () => {
            removeItemFromOrder(item.id);
        };

        orderPreviewContent.appendChild(row);
    });

    const totalRow = document.createElement("div");
    totalRow.className = "d-flex justify-content-between align-items-center mt-3 pt-3 border-top border-2 border-dark fw-bold fs-5";
    totalRow.innerHTML = `
        <span>Total:</span>
        <span class="text-warning">${finalOrderTotal.toFixed(2)} ₸</span>
    `;
    orderPreviewContent.appendChild(totalRow);
}

function removeItemFromOrder(itemId) {
    currentOrder.items = currentOrder.items.filter(item => item.id !== itemId);
    renderPreviewList();

    if (currentOrder.items.length === 0) {
        alert("Order is empty");
        orderPreviewModal.hide();
    }
}

document.getElementById("confirmFinalOrder").onclick = async () => {
    const cleanItems = currentOrder.items.map(item => ({
        coffeeId: item.coffeeId,
        sizeId: item.sizeId,
        quantity: item.quantity,
        requests: item.requests.map(req => ({
            additiveId: req.additiveId,
            quantity: req.quantity
        }))
    }));

    const finalPayload = {
        userId: currentUserId,
        items: cleanItems
    };

    try {
        const response = await fetch("/api/order-processes/start", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(finalPayload)
        });

        if (!response.ok) {
            throw new Error(await response.text());
        }

        alert("Order has been created!");

        currentOrder.items = [];
        orderPreviewModal.hide();

    } catch (e) {
        console.error(e);
        alert("Failed to create order");
    }
};