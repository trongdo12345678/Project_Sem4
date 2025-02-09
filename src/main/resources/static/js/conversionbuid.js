
// Biến toàn cục
let allConversions = []; // Lưu toàn bộ dữ liệu từ server
let currentPage1 = 0; // Trang hiện tại
let pageSize1 = 3; // Số lượng bản ghi trên mỗi trang

// Hàm tải toàn bộ dữ liệu từ server
function loadAllConversions() {
	const productId = new URLSearchParams(window.location.search).get('id'); // Lấy productId từ URL
	if (!productId) return;

	// Gửi yêu cầu AJAX lấy toàn bộ dữ liệu
	$.ajax({
		url: `/admin/conversion`,
		method: 'GET',
		data: { productId: productId },
		success: function(response) {

			allConversions = response.map(conversion => ({
				id: conversion.id || 'N/A',
				from_unit_name: conversion.from_unit_name || 'N/A',
				to_unit_name: conversion.to_unit_name || 'N/A',
				conversion_rate: conversion.Conversion_rate || 0
			}));
			// Lưu dữ liệu nhận được vào mảng toàn cục
			currentPage1 = 0; // Reset về trang đầu
			renderStockTable1(); // Hiển thị bảng dữ liệu trang đầu tiên
			renderPagination1(); // Hiển thị phân trang
		},
		error: function(error) {
			console.error('Error fetching conversion data:', error);
		}
	});
}

// Hàm hiển thị bảng dữ liệu cho trang hiện tại
function renderStockTable1() {
    const tableBody = document.getElementById('conversionTable').querySelector('tbody');
    tableBody.innerHTML = ''; // Xóa dữ liệu cũ

    // Lấy dữ liệu cho trang hiện tại
    const start = currentPage1 * pageSize1;
    const end = Math.min(start + pageSize1, allConversions.length);
    const pageData = allConversions.slice(start, end);

    // Hiển thị từng dòng dữ liệu
    pageData.forEach((conversion, index) => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${start + index + 1}</td>
            <td>${conversion.from_unit_name}</td>
            <td>${conversion.to_unit_name}</td>
            <td>${conversion.conversion_rate}</td>
            <td>
                <button class="btn btn-danger btn-sm" onclick="deleteConversion(${conversion.id})">x</button>
            </td>
        `;
        tableBody.appendChild(row);
    });
}


// Hàm hiển thị phân trang
function renderPagination1() {
	const totalPages = Math.ceil(allConversions.length / pageSize1); // Tổng số trang
	let paginationHtml = '';

	// Nút "Previous"
	paginationHtml += `
                    <li class="page-item ${currentPage1 === 0 ? 'disabled' : ''}">
                        <button class="page-link" onclick="goToPage(${currentPage1 - 1})">&lt;</button>
                    </li>
                `;

	// Hiển thị các nút trang
	for (let i = 0; i < totalPages; i++) {
		paginationHtml += `
                        <li class="page-item ${i === currentPage1 ? 'active' : ''}">
                            <button class="page-link" onclick="goToPage(${i})">${i + 1}</button>
                        </li>
                    `;
	}

	// Nút "Next"
	paginationHtml += `
                    <li class="page-item ${currentPage1 === totalPages - 1 ? 'disabled' : ''}">
                        <button class="page-link" onclick="goToPage(${currentPage1 + 1})">&gt;</button>
                    </li>
                `;

	// Cập nhật HTML cho phân trang
	document.getElementById('pagination').innerHTML = paginationHtml;
}

// Hàm chuyển trang
function goToPage(page) {

	console.log("Navigating to page:", page); // Kiểm tra số trang được chọn

	if (page < 0 || page >= Math.ceil(allConversions.length / pageSize1)) return;
	currentPage1 = page;
	renderStockTable1(); // Hiển thị dữ liệu trang mới
	renderPagination1(); // Cập nhật phân trang
}

// Khi tải trang, lấy toàn bộ dữ liệu
$(document).ready(function() {
	loadAllConversions();
});
//zzzzzzzzzzzzzzzzzzzzzzzzzz

document.getElementById('add-conversion').addEventListener('click', function() {
	// Tạo một div chứa hàng mới
	const newRow = document.createElement('div');
	newRow.classList.add('form-row');

	// Tạo các input trong hàng mới
	newRow.innerHTML = `
                <input type="text" name="from_unit_name[]" placeholder="Nhập từ đơn vị">
                <input type="text" name="to_unit_name[]" placeholder="Nhập đến đơn vị">
                <input type="number" name="conversion_rate[]" placeholder="Tỉ lệ quy đổi">
            `;

	// Thêm hàng mới vào form
	document.getElementById('conversion-form').appendChild(newRow);
});

function deleteConversion(conversionId) {
    const productId = new URLSearchParams(window.location.search).get('id'); // Lấy productId từ URL

    if (!productId || !conversionId) return;

    if (!confirm("Do you want to delete this line ?")) return;

    // Gửi yêu cầu AJAX để xóa dữ liệu
	$.ajax({
	    url: '/admin/conversion/delete',
	    method: 'DELETE',
	    contentType: 'application/json',
	    data: JSON.stringify({ id: parseInt(conversionId), product_id: parseInt(productId) }), // Đảm bảo kiểu số
	    success: function(response) {
	        alert('Delete Completed!');
	        allConversions = allConversions.filter(conversion => conversion.id !== conversionId);
	        renderStockTable1();
	        renderPagination1();
	    },
	    error: function(error) {
	        console.error('Error deleting conversion:', error);
	        alert('Xóa thất bại. Vui lòng thử lại!');
	    }
	});
}



function validateNumber(input) {
	// Kiểm tra nếu giá trị không phải là một số hợp lệ hoặc là NaN
	if (isNaN(input.value) || input.value === "") {
		input.setCustomValidity("Please enter a valid number.");
	} else {
		input.setCustomValidity("");
	}
}

