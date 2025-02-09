//search text
function searchTable() {
	var searchTerm = $('#searchInput').val().toLowerCase();
	var rows = $('table tbody tr');

	rows.each(function() {
		var rowText = $(this).text().toLowerCase();
		if (rowText.indexOf(searchTerm) === -1) {
			$(this).hide();
		} else {
			$(this).show();
		}
	});
}

//delete thông báo product
function confirmDelete() {
	return confirm("Are you sure you want to delete this product?");
}

function confirmDeletePs() {
	return confirm("Are you sure you want to delete this product spe?");
}
function confirmDeletePi() {
	return confirm("Are you sure you want to delete this product img?");
}
//click vào xem chi tiết sản phẩm
function goToDetail(id) {
	window.location.href = '/admin/product/showProductDetail?id=' + id;
}
//phân trang
function loadPage(pageNumber) {
	$.ajax({
		type: "GET",
		url: "/admin/product/showProduct",
		data: {
			cp: pageNumber
		},
		success: function(response) {
			$('#pageContent').html(response);

			updatePagination(response.pv);
		},
		error: function(e) {
			console.log("Error: ", e);
		}
	});
}
// xóa nhieuf ảnh
function toggleSelectAll(selectAllCheckbox) {
	const checkboxes = document.querySelectorAll('.productCheckbox');
	checkboxes.forEach(checkbox => {
		checkbox.checked = selectAllCheckbox.checked;
	});

	toggleDeleteButton();
}

// Hàm xóa các sản phẩm đã chọn
function deleteSelectedImg() {
	const selectedProductIds = [];
	document.querySelectorAll('.productCheckbox:checked').forEach(checkbox => {
		selectedProductIds.push(parseInt(checkbox.value));
	});
	if (selectedProductIds.length > 0) {
		if (confirm('Are you sure you want to delete the selected products?')) {
			fetch('/admin/product/deleteSelected', {
				method: 'POST',
				headers: {
					'Content-Type': 'application/json'
				},
				body: JSON.stringify(selectedProductIds)
			})
				.then(response => {
					if (response.ok) {
						return response.text();
					} else {
						throw new Error('Failed to delete products.');
					}
				})
				.then(data => {
					console.log(data);
					window.location.reload();
				})
				.catch(error => {
					console.error('Error:', error);
					alert('An error occurred while deleting products: ' + error.message);
				});
		}
	} else {
		alert('Please select at least one product to delete.');
	}
}

// Hàm kiểm tra trạng thái của các checkbox và bật/tắt nút xóa
function toggleDeleteButton() {
	const checkboxes = document.querySelectorAll('.productCheckbox');
	const deleteBtn = document.getElementById('deleteSelectedBtn');

	const isAnyChecked = Array.from(checkboxes).some(checkbox => checkbox.checked);

	deleteBtn.disabled = !isAnyChecked;
}

// Hàm kiểm tra và thay đổi trạng thái "Select all"
function updateProductStatus(element) {
	const productId = element.getAttribute('data-id');
	const currentStatus = element.getAttribute('data-status');
	const statuses = ["Active", "InActive", "OutOfstock", "NewRelease"];

	let currentIndex = statuses.indexOf(currentStatus);
	let nextIndex = (currentIndex + 1) % statuses.length;
	let newStatus = statuses[nextIndex];

	// Hiển thị hộp thoại xác nhận
	const isConfirmed = confirm(`Are you sure you want to change the status to ${newStatus}?`);

	// Nếu người dùng xác nhận, thực hiện thay đổi trạng thái
	if (isConfirmed) {
		const statusElement = document.getElementById("status-" + productId);
		if (statusElement) {
			statusElement.innerText = newStatus;
		} else {
			console.error(`Status element with ID status-${productId} not found!`);
			return;
		}

		fetch(`/admin/product/updateStatus`, {
			method: 'POST',
			headers: {
				'Content-Type': 'application/json',
			},
			body: JSON.stringify({
				id: productId,
				status: newStatus,
			}),
		})
			.then(response => {
				if (response.status === 200) {
					element.setAttribute('data-status', newStatus);
				} else {
					console.error('Failed to update status:', response.status);
					statusElement.innerText = currentStatus;
				}
			})
			.catch(error => {
				console.error('Error updating status:', error);
				statusElement.innerText = currentStatus;
			});
	} else {
		// Nếu người dùng không xác nhận, không làm gì
		console.log('Status update was cancelled');
	}
}





// Nếu có ít nhất một checkbox được chọn thì hiển thị nút, ngược lại thì ẩn nút
deleteButton.style.display = isAnyChecked ? 'block' : 'none';







