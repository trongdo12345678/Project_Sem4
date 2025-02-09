let allSpecifications = []; // Dữ liệu toàn bộ các product specification
let currentPage2 = 0; // Trang hiện tại
let pageSize2 = 3; // Số lượng bản ghi trên mỗi trang

// Hàm tải toàn bộ dữ liệu từ server
function loadAllSpecifications() {
    const productId = new URLSearchParams(window.location.search).get('id'); // Lấy productId từ URL
    if (!productId) return;

    // Gửi yêu cầu AJAX lấy toàn bộ dữ liệu
    $.ajax({
        url: '/admin/product/findAllspe',
        method: 'GET',
        data: { productId: productId },
		success: function(response) {
		    allSpecifications = response.map(specification => ({
		        id: specification.Id || 'N/A',
		        name_spe: specification.Name_spe || 'N/A',
		        des_spe: specification.Des_spe || 'N/A',
		    }));
		    currentPage2 = 0;
		    renderSpecificationsTable();
		    renderPagination2();
		},
        error: function(error) {
            console.error('Error fetching specifications data:', error);
        }
    });
}

// Hàm hiển thị bảng dữ liệu cho trang hiện tại
function renderSpecificationsTable() {
    const tableBody = document.getElementById('specificationsTableBody');
    tableBody.innerHTML = '';

    // Lấy dữ liệu cho trang hiện tại
    const start = currentPage2 * pageSize2;
    const end = Math.min(start + pageSize2, allSpecifications.length);
    const pageData = allSpecifications.slice(start, end);

	pageData.forEach((specification, index) => {
	    const row = document.createElement('tr');
	    row.innerHTML = `
	        <td>${start + index + 1}</td>
	        <td>${specification.name_spe}</td>
	        <td>${specification.des_spe}</td>
	        <td>
			<button class="btn btn-danger" onclick="deleteSpecification(this, ${specification.id})">Delete</button>
	        </td>
	    `;
	    tableBody.appendChild(row);
	});
}



// Hàm hiển thị phân trang
function renderPagination2() {
    const totalPages = Math.ceil(allSpecifications.length / pageSize2); // Tổng số trang
    let paginationHtml = '';

    // Nút "Previous"
    paginationHtml += `
        <li class="page-item ${currentPage2 === 0 ? 'disabled' : ''}">
            <button class="page-link" onclick="goToPage2(${currentPage2 - 1})">&lt;</button>
        </li>
    `;

    // Nút trang
    for (let i = 0; i < totalPages; i++) {
        paginationHtml += `
            <li class="page-item ${i === currentPage2 ? 'active' : ''}">
                <button class="page-link" onclick="goToPage2(${i})">${i + 1}</button>
            </li>
        `;
    }

    // Nút "Next"
    paginationHtml += `
        <li class="page-item ${currentPage2 === totalPages - 1 ? 'disabled' : ''}">
            <button class="page-link" onclick="goToPage2(${currentPage2 + 1})">&gt;</button>
        </li>
    `;

    document.getElementById('pagination1').innerHTML = paginationHtml;
}


// Hàm chuyển trang
function goToPage2(page) {
    if (page < 0 || page >= Math.ceil(allSpecifications.length / pageSize2)) return; // Kiểm tra trang hợp lệ
    currentPage2 = page;
    renderSpecificationsTable();
    renderPagination2();
}

$(document).ready(function() {
    loadAllSpecifications();
});
document.getElementById("add-specification-form").addEventListener("submit", function (e) {
    e.preventDefault(); // Ngăn form submit mặc định

    // Vô hiệu hóa nút submit để tránh việc gửi lại form nhiều lần
    const submitButton = document.querySelector('button[type="submit"]');
    submitButton.disabled = true; 
    const product_id = document.getElementById("product_id").value;
    const name_spe = document.getElementById("name_spe").value;
    const des_spe = document.getElementById("des_spe").value;

    // Gửi AJAX request để thêm specification
    fetch('/admin/product/addPs', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: new URLSearchParams({
            product_id: product_id,
            name_spe: name_spe,
            des_spe: des_spe
        })
    })
    .then(response => {
        if (!response.ok) {
            return response.text().then(error => {
                throw new Error(error);
            });
        }
        return response.json(); // Parse JSON từ phản hồi
    })
    .then(data => {
        if (!data.name_spe || !data.des_spe) {
            throw new Error("Invalid data format from server!");
        }
        
        // Thêm specification mới vào bảng
        addNewSpecificationToTable(data);

        // Cập nhật lại bảng và phân trang
        renderStockTable1();
        renderPagination1();

        alert("Specification added successfully!");
    })
    .catch(error => {
        console.error('Error:', error);
        alert('Failed to add specification! Details: ' + error.message);
    })
    .finally(() => {
        // Bật lại nút submit sau khi xử lý xong
        submitButton.disabled = false;
    });
});


// Hàm thêm Specification mới vào bảng
function addNewSpecificationToTable(specification) {
    if (!specification) {
        console.error("Specification data is undefined or null!");
        return;
    }

    const tableBody = document.getElementById('specificationTable').querySelector('tbody');
    const newRow = document.createElement('tr');
    newRow.innerHTML = `
        <td>${tableBody.children.length + 1}</td>
        <td>${specification.name_spe}</td>
        <td>${specification.des_spe}</td>
        <button class="btn btn-danger" onclick="deleteSpecification(this, ${specification.id})">Delete</button>
    `;
    tableBody.appendChild(newRow);

    allSpecifications.push(specification);
    const totalPages = Math.ceil(allSpecifications.length / pageSize2);
    if (totalPages > currentPage2 + 1) {
        currentPage2++;
    }

    renderSpecificationsTable();
    renderPagination2();
}
function deleteSpecification(button, specificationId) {
    const productId = new URLSearchParams(window.location.search).get('id'); // Lấy productId từ URL

    if (!productId || !specificationId) return;

    if (!confirm("Are you sure you want to delete this specification?")) return;
    $.ajax({
        url: '/admin/product/deletePs',
        method: 'DELETE',
        contentType: 'application/json',
        data: JSON.stringify({
            id: parseInt(specificationId),
            product_id: parseInt(productId)
        }),
        success: function(response) {
            alert('Deleted successfully!');

            // Tìm dòng chứa nút vừa bấm và xóa nó khỏi bảng
            const row = button.closest('tr');
            if (row) row.remove();
            // Hoặc cập nhật lại bảng và phân trang sau khi xóa
            allSpecifications = allSpecifications.filter(spec => spec.id !== specificationId);
            renderSpecificationsTable();
            renderPagination2();
        },
        error: function(error) {
            console.error('Error deleting specification:', error);
            alert('Delete failed. Please try again!');
        }
    });
}
document.addEventListener('DOMContentLoaded', function() {
    const form = document.getElementById('add-specification-form');
    const submitButton = form.querySelector('button[type="submit"]');

    const checkFormValidity = () => {
        submitButton.disabled = !form.checkValidity();
    };

    form.addEventListener('input', checkFormValidity);
    form.addEventListener('change', checkFormValidity);

    checkFormValidity();
});


