let allPriceChanges = []; // Dữ liệu toàn bộ các thay đổi giá
let currentPagePrice = 0; // Trang hiện tại
let pageSizePrice = 5; // Số lượng bản ghi trên mỗi trang

// Hàm tải toàn bộ dữ liệu từ server
function loadAllPriceChanges() {
	const productId = new URLSearchParams(window.location.search).get('id');
	if (!productId) {
		console.error('Product ID not found in URL');
		return;
	}

	$.ajax({
		url: '/admin/product/findPrice',
		method: 'GET',
		data: { productId: productId },
		success: function(response) {
			console.log("Response received:", response);
			allPriceChanges = response.map(change => ({
				price: change.price || 'N/A',
				startDate: change.date_start,
				endDate: change.date_end,
				id: change.id || 'N/A'
			}));

			currentPagePrice = 0;
			renderPriceChangesTable();
			renderPricePagination();
		}

		,
		error: function(error) {
			console.error('Error fetching price changes data:', error);
		}
	});
}

// Hàm hiển thị bảng dữ liệu cho trang hiện tại
function renderPriceChangesTable() {
	const tableBody = document.getElementById('priceChangesTableBody');
	tableBody.innerHTML = '';

	const start = currentPagePrice * pageSizePrice;
	const end = Math.min(start + pageSizePrice, allPriceChanges.length);
	const pageData = allPriceChanges.slice(start, end);

	pageData.forEach((change, index) => {
		const row = document.createElement('tr');
		row.innerHTML = `
            <td>${start + index + 1}</td>
            <td>${change.price} $</td>
            <td>${formatDate(change.startDate)}</td>
            <td>${formatDate(change.endDate)}</td>
        `;
		tableBody.appendChild(row);
	});
}

// Hàm hiển thị phân trang
function renderPricePagination() {
	const totalPages = Math.ceil(allPriceChanges.length / pageSizePrice); // Tổng số trang
	let paginationHtml = ''; // Khởi tạo một biến chứa HTML phân trang

	// Nút "Previous"
	paginationHtml += `
        <li class="page-item ${currentPagePrice === 0 ? 'disabled' : ''}">
            <button class="page-link" onclick="goToPricePage(${currentPagePrice - 1})">&lt;</button>
        </li>
    `;

	// Nút trang
	for (let i = 0; i < totalPages; i++) {
		paginationHtml += `
            <li class="page-item ${i === currentPagePrice ? 'active' : ''}">
                <button class="page-link" onclick="goToPricePage(${i})">${i + 1}</button>
            </li>
        `;
	}

	// Nút "Next"
	paginationHtml += `
        <li class="page-item ${currentPagePrice === totalPages - 1 ? 'disabled' : ''}">
            <button class="page-link" onclick="goToPricePage(${currentPagePrice + 1})">&gt;</button>
        </li>
    `;

	document.getElementById('Pagination2').innerHTML = paginationHtml;
}

// Hàm chuyển trang
function goToPricePage(page) {
	if (page < 0 || page >= Math.ceil(allPriceChanges.length / pageSizePrice)) return;
	currentPagePrice = page;
	renderPriceChangesTable();
	renderPricePagination(); 
}

// Hàm xóa một thay đổi giá
function deletePriceChange(button, changeId) {
	if (!confirm("Are you sure you want to delete this price change?")) return;

	$.ajax({
		url: '/admin/product/deletePriceChange',
		method: 'DELETE',
		contentType: 'application/json',
		data: JSON.stringify({ id: changeId }),
		success: function(response) {
			alert('Deleted successfully!');
			const row = button.closest('tr');
			if (row) row.remove();
			allPriceChanges = allPriceChanges.filter(change => change.id !== changeId);
			renderPriceChangesTable();
			renderPricePagination();
		},
		error: function(error) {
			console.error('Error deleting price change:', error);
			alert('Delete failed. Please try again!');
		}
	});
}
function formatDate(dateString) {
	if (!dateString || dateString === 'Invalid Date') {
		return 'Present';
	}

	const date = new Date(dateString);
	if (isNaN(date.getTime())) {
		return 'Present';
	}

	// Định dạng ngày theo kiểu 'HH:mm dd/mm/yyyy'
	const hours = String(date.getHours()).padStart(2, '0');
	const minutes = String(date.getMinutes()).padStart(2, '0');
	const formattedDate = date.toLocaleDateString('en-GB', { day: '2-digit', month: '2-digit', year: 'numeric' });

	return `${hours}:${minutes} ${formattedDate}`;
}

// Hàm hiển thị bảng dữ liệu cho trang hiện tại
function renderPriceChangesTable() {
	const tableBody = document.getElementById('priceChangesTableBody');
	tableBody.innerHTML = ''; // Xóa dữ liệu cũ
	
	const start = currentPagePrice * pageSizePrice;
	const end = Math.min(start + pageSizePrice, allPriceChanges.length);
	const pageData = allPriceChanges.slice(start, end);

	pageData.forEach((change, index) => {
		const row = document.createElement('tr');
		row.innerHTML = `
            <td>${start + index + 1}</td>
            <td>${change.price} $</td>
            <td>${formatDate(change.startDate)}</td>
            <td>${formatDate(change.endDate)}</td>
        `;
		tableBody.appendChild(row);
	});
}

// Khi tải trang, lấy toàn bộ dữ liệu
$(document).ready(function() {
	loadAllPriceChanges();
});
// sort
document.getElementById('sortPriceChanges').addEventListener('change', function () {
    sortPriceChanges(this.value);
});

function sortPriceChanges(order) {
    if (order === 'asc') {
        allPriceChanges.sort((a, b) => parseFloat(a.price) - parseFloat(b.price));
    } else if (order === 'desc') {
        allPriceChanges.sort((a, b) => parseFloat(b.price) - parseFloat(a.price));
    } else {
        loadAllPriceChanges();
        return;
    }
    
    currentPagePrice = 0;
    renderPriceChangesTable();
    renderPricePagination();
}
