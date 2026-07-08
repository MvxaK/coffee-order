const coffeeContainer = document.getElementById("coffeeContainer");
const additivesContainer = document.getElementById("additivesContainer");

const modal = new bootstrap.Modal(document.getElementById("coffeeModal"));

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
let currentOrder = null;

document.addEventListener("DOMContentLoaded", async () => {
    await loadData();
    renderCoffees();
});

async function loadData() {
    const [coffeeRes, additiveRes, sizeRes] = await Promise.all([
        fetch("/api/coffees"),
        fetch("/api/additives"),
        fetch("/api/coffee-sizes")
    ]);

    coffees = await coffeeRes.json();
    additives = await additiveRes.json();
    sizes = await sizeRes.json();
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
    selectedAdditives = new Map();

    coffeeQuantity.textContent = "1";

    modalCoffeeName.textContent = coffee.name;
    modalCoffeeImage.src = coffee.url;
    modalCoffeeDescription.textContent = coffee.description;
    modalCoffeePrice.textContent = coffee.price + " ₸";

    renderSizes();
    renderAdditives();
    calculateTotal();

    modal.show();
}

function renderSizes() {
    coffeeSize.innerHTML = "";

    sizes.forEach(size => {
        coffeeSize.innerHTML += `
            <option value="${size.id}" data-price="${size.price}">
                ${size.size} (+${size.price} ₸)
            </option>
        `;
    });

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
                <img src="${additive.url}" class="additive-image">
                <div>
                    <div class="additive-name">${additive.name}</div>
                    <div class="additive-price">${additive.price} ₸</div>
                </div>
            </div>

            <div class="additive-controls">
                <button class="btn btn-outline-warning minus">-</button>
                <span class="additive-quantity" data-id="${additive.id}">0</span>
                <button class="btn btn-outline-warning plus">+</button>
            </div>
        `;

        row.querySelector(".minus").onclick = () => changeAdditive(additive.id, -1);
        row.querySelector(".plus").onclick = () => changeAdditive(additive.id, 1);

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

function changeAdditive(id, delta) {
    const quantity = Math.max(0, (selectedAdditives.get(id) ?? 0) + delta);
    selectedAdditives.set(id, quantity);
    document.querySelector(`.additive-quantity[data-id="${id}"]`).textContent = quantity;
    calculateTotal();
}

function calculateTotal() {
    if (!selectedCoffee)
        return;

    let price = Number(selectedCoffee.price);
    const selectedSize = coffeeSize.options[coffeeSize.selectedIndex];

    if (selectedSize)
        price += Number(selectedSize.dataset.price);

    let total = price * selectedCoffeeQuantity;

    additives.forEach(additive => {
        total += Number(additive.price) * (selectedAdditives.get(additive.id) ?? 0);
    });

    totalPrice.textContent = total.toFixed(2) + " ₸";
}

document.getElementById("addToOrder").onclick = () => {
    currentOrder  = {
        userId: null,
        items: [
            {
                coffeeId: selectedCoffee.id,
                sizeId: Number(coffeeSize.value),
                quantity: selectedCoffeeQuantity,
                requests: []
            }
        ]
    };

    additives.forEach(additive => {
        const quantity = selectedAdditives.get(additive.id);

        if (quantity > 0) {
            currentOrder.items[0].requests.push({
                additiveId: additive.id,
                quantity: quantity
            });
        }
    });

    console.log(currentOrder);

    modal.hide();
};