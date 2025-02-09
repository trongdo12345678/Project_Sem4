let products = [];

// Lấy danh sách sản phẩm từ server
async function fetchProducts() {
    try {
        const response = await fetch('/businessManager/getProducts');
        if (!response.ok) throw new Error('Network response was not ok');

        const data = await response.json();
        if (Array.isArray(data) && data.length > 0) {
            products = data;
            document.getElementById('addDetailButton').style.display = 'inline-block';
        } else {
            console.warn("No products found.");
            alert("No product data available. Please check again later.");
            document.getElementById('addDetailButton').style.display = 'none';
        }
    } catch (error) {
        console.error("Error fetching products:", error);
        alert("Failed to load product data.");
        document.getElementById('addDetailButton').style.display = 'none';
    }
}

// Khởi chạy việc lấy sản phẩm
fetchProducts();

let detailIndex = 0;

// Thêm dòng chi tiết đơn hàng
function addDetail() {
    const detailsDiv = document.getElementById('details');
    detailIndex++;

    const newDetail = document.createElement('div');
    newDetail.className = 'detail-entry';
    newDetail.innerHTML = `
        <div class="form-row">
            <div class="form-group col-md-6">
                <label for="productSearch${detailIndex}">Product Name</label>
                <input type="text" id="productSearch${detailIndex}" class="form-control" placeholder="Search for a product..." oninput="filterProducts(${detailIndex})">
                <ul id="productList${detailIndex}" class="product-list" style="display: none;"></ul>
                <input type="hidden" id="id_product${detailIndex}" name="id_product" required>
            </div>
            <div class="form-group col-md-6">
                <label for="quantity_requested${detailIndex}">Quantity</label>
                <input type="number" id="quantity_requested${detailIndex}" name="quantity_requested" class="form-control" placeholder="Enter Quantity" min="1" oninput="validateQuantity(${detailIndex})" required>
            </div>
        </div>
        <input type="hidden" id="status${detailIndex}" name="status" value="Processing">
        <button type="button" class="btn btn-danger remove-detail-btn" onclick="removeDetail(this)">Remove</button>
    `;
    detailsDiv.appendChild(newDetail);
}

function validateQuantity(index) {
    const quantityInput = document.getElementById(`quantity_requested${index}`);
    const quantityValue = quantityInput.value;

    // Kiểm tra nếu giá trị không phải là số hoặc là NaN
	if (quantityValue < 1 || isNaN(quantityValue)) {
	        quantityInput.value = 1; // Đặt lại giá trị mặc định
	    }
}

// Lọc danh sách sản phẩm dựa trên văn bản tìm kiếm
function filterProducts(detailIndex) {
    const searchInput = document.getElementById(`productSearch${detailIndex}`);
    const productList = document.getElementById(`productList${detailIndex}`);
    const filterText = searchInput.value.toLowerCase();

    // Lọc sản phẩm
    const filteredProducts = products.filter(product =>
        product.product_name.toLowerCase().includes(filterText)
    );

    // Xóa danh sách hiện tại
    productList.innerHTML = '';
    productList.style.display = filteredProducts.length > 0 ? 'block' : 'none';

    if (filteredProducts.length > 0) {
        filteredProducts.forEach(product => {
            const li = document.createElement('li');
            li.textContent = product.product_name;
            li.onclick = () => selectProduct(product, detailIndex);
            productList.appendChild(li);
        });
        searchInput.classList.remove('is-invalid');
    } else {
        // Hiển thị thông báo lỗi nếu không có kết quả
        const noResult = document.createElement('li');
        noResult.textContent = 'No products found.';
        noResult.style.color = 'red';
        productList.appendChild(noResult);
        productList.style.display = 'block';
        searchInput.classList.add('is-invalid');
    }
}

// Chọn sản phẩm và gán giá trị vào trường id_product
function selectProduct(product, detailIndex) {
    document.getElementById(`productSearch${detailIndex}`).value = product.product_name;
    document.getElementById(`id_product${detailIndex}`).value = product.id;
    document.getElementById(`productList${detailIndex}`).style.display = 'none';
}

// Xóa một dòng chi tiết đơn hàng
function removeDetail(button) {
    button.closest('.detail-entry').remove();
}

// Tìm sản phẩm dựa trên văn bản nhập
function compareInputWithProducts(inputText) {
    const inputLower = inputText.toLowerCase().trim();
    return products.find(product => product.product_name.toLowerCase() === inputLower) || null;
}

// Kiểm tra sản phẩm khớp với văn bản nhập
function checkProductMatch(inputId) {
    const inputText = document.getElementById(inputId).value;
    const matchedProduct = compareInputWithProducts(inputText);
    if (matchedProduct) {
        alert(`Product found: ${matchedProduct.product_name}`);
    } else {
        alert("No matching product found.");
    }
}
